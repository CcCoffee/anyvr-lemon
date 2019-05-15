package anyvr.app.lemon.handler;

import static anyvr.app.lemon.jni.OpusConf.FRAME_SIZE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import anyvr.Spec;
import anyvr.app.lemon.tools.AudioSamplesHelper;
import anyvr.app.lemon.player.Player;
import anyvr.app.lemon.player.PlayerStore;
import anyvr.app.lemon.voiceFile.VoiceFileWriter;
import anyvr.app.lemon.jni.OpusEncoder;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

@ExtendWith(MockitoExtension.class)
public class PlayerVoiceHandlerTest {

    private PlayerVoiceHandler playerVoiceHandler;

    private String voiceFilePath = "tests/";

    private PlayerStore playerStore;

    private static final float NUMBER_ZEROS_PERCENTAGE_WITHOUT_GAP = 0.12f;

    private ChannelHandlerContext channelContext;

    private long lastTimestemp;

    @BeforeEach
    public void test() {
        File file = new File("tests/");
        purgeFiles(file);
    }

    public OpusEncoder setup(final UUID playerUuid) throws Exception {
        channelContext = Mockito.mock(ChannelHandlerContext.class);
        final Channel channel = Mockito.mock(Channel.class);
        when(channelContext.channel()).thenReturn(channel);

        playerStore = Mockito.spy(PlayerStore.class);
        playerVoiceHandler = new PlayerVoiceHandler(voiceFilePath, playerStore, new VoiceFileWriter());

        OpusEncoder opusEncoder = new OpusEncoder();
        final byte[] encodedVoice = opusEncoder.encode(AudioSamplesHelper.audioSampleOne);

        int datagramId = 1;
        lastTimestemp = System.currentTimeMillis();
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice.newBuilder()
                .setUuid(playerUuid.toString())
                .setVoice(ByteString.copyFrom(encodedVoice))
                .setTimestamp(lastTimestemp)
                .setDatagramOrderId(datagramId)
                .build();

        playerVoiceHandler.channelRead0(channelContext, playerVoice);

        return opusEncoder;
    }

    @Test
    public void addPlayerTest() throws Exception {
        final UUID playerUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        setup(playerUuid);

        verify(channelContext, times(1)).channel();
        verify(playerStore, times(1)).isPlayerAlreadyExist(playerUuid);
        verify(playerStore, times(1)).add(any());
        verify(playerStore, times(0)).getPlayer(playerUuid);
    }

    @Test
    public void removePlayer() throws Exception {
        final UUID playerUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        setup(playerUuid);

        playerVoiceHandler.channelInactive(channelContext);
        assertThat(playerStore.getPlayer(playerUuid).isPresent(), is(equalTo(false)));
    }

    @Test
    public void writeVoiceFileWithoutGapTest() throws Exception {
        final UUID playerUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        setup(playerUuid);

        assertThat(playerStore.getPlayer(playerUuid).isPresent(), is(equalTo(true)));

        final Player player = playerStore.getPlayer(playerUuid).get();
        player.getVoiceFile().close();

        String name = voiceFilePath + playerUuid.toString() + ".raw";
        Path fileInput = Paths.get(name);
        final InputStream inputStream = Files.newInputStream(fileInput);
        byte[] actualVoice = inputStream.readAllBytes();
        assertThat(actualVoice.length, is(equalTo(FRAME_SIZE)));

        float percentageZeros = (float) countNumberZeros(actualVoice) / FRAME_SIZE;
        assertThat(percentageZeros, is(lessThan(NUMBER_ZEROS_PERCENTAGE_WITHOUT_GAP)));
    }

    @Test
    public void writeVoiceFileWithGapTest() throws Exception {
        final UUID playerOneUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        OpusEncoder opusEncoder = setup(playerOneUuid);

        final byte[] encodedVoice = opusEncoder.encode(AudioSamplesHelper.audioSampleOne);

        int datagramId = 2;
        long currentTimestemp = lastTimestemp + 40;
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice.newBuilder()
                .setUuid(playerOneUuid.toString())
                .setVoice(ByteString.copyFrom(encodedVoice))
                .setTimestamp(currentTimestemp)
                .setDatagramOrderId(datagramId)
                .build();

        playerVoiceHandler.channelRead0(channelContext, playerVoice);

        String name = voiceFilePath + playerOneUuid.toString() + ".raw";
        Path fileInput = Paths.get(name);
        final InputStream inputStream = Files.newInputStream(fileInput);
        byte[] actualVoice = inputStream.readAllBytes();
        assertThat(actualVoice.length, is(equalTo(FRAME_SIZE * 3)));
        int countNumberZeros = countNumberZeros(actualVoice);
        assertThat(countNumberZeros, is(greaterThan(FRAME_SIZE)));
    }

    @Test
    public void udpSequenceTest() throws Exception {
        final UUID playerUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        OpusEncoder opusEncoder = setup(playerUuid);

        final byte[] encodedVoice = opusEncoder.encode(AudioSamplesHelper.audioSampleTwo);

        int datagramId = 0;
        long currentTimestemp = lastTimestemp + 40;
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice.newBuilder()
                .setUuid(playerUuid.toString())
                .setVoice(ByteString.copyFrom(encodedVoice))
                .setTimestamp(currentTimestemp)
                .setDatagramOrderId(datagramId)
                .build();

        playerVoiceHandler.channelRead0(channelContext, playerVoice);

        String name = voiceFilePath + playerUuid.toString() + ".raw";
        Path fileInput = Paths.get(name);
        final InputStream inputStream = Files.newInputStream(fileInput);
        byte[] actualVoice = inputStream.readAllBytes();
        assertThat(actualVoice.length, is(equalTo(FRAME_SIZE)));
    }

    private int countNumberZeros(byte[] actualVoice) {
        int numberZeros = 0;
        for(int i = 0; i < actualVoice.length; i++) {
            if(actualVoice[i] == 0) {
                numberZeros++;
            }
        }
        return numberZeros;
    }

    private void purgeFiles(File dir) {
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}
