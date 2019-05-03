package anyvr.app.lemon.handler;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import anyvr.Spec;
import anyvr.app.lemon.AudioSamplesHelper;
import anyvr.app.lemon.Player;
import anyvr.app.lemon.PlayerStore;
import anyvr.app.lemon.jni.Opus;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

@ExtendWith(MockitoExtension.class)
public class PlayerVoiceHandlerTest {

    private static final int CHANNELS = 1;
    private static final int MAX_FRAME_SIZE = 6 * 480;

    private PlayerVoiceHandler playerVoiceHandler;

    private String voiceFilePath = "tests/";

    private PlayerStore playerStore;

    private float numberZerosPercentage = 0.07f;

    private ChannelHandlerContext channelContext;

    private long lastTimestemp;

    @BeforeEach
    public void test() {
        File file = new File("tests/");
        purgeFiles(file);
    }

    public long setup(final UUID playerUuid) throws Exception {
        channelContext = Mockito.mock(ChannelHandlerContext.class);
        final Channel channel = Mockito.mock(Channel.class);
        when(channelContext.channel()).thenReturn(channel);

        playerStore = Mockito.spy(PlayerStore.class);
        playerVoiceHandler = new PlayerVoiceHandler(voiceFilePath, playerStore);

        long encoder = Opus.encoder_create(24000, 1);
        byte[] outputEncode = new byte[MAX_FRAME_SIZE * CHANNELS * 2];

        final int packageLength = Opus.encode(encoder, AudioSamplesHelper.audioSampleOne, 0, 480, outputEncode, 0, outputEncode.length);

        final byte[] encodedVoice = Arrays.copyOfRange(outputEncode, 0, packageLength);

        int datagramId = 1;
        lastTimestemp = System.currentTimeMillis();
        final Spec.PlayerVoice playerVoice = Spec.PlayerVoice.newBuilder()
                .setUuid(playerUuid.toString())
                .setVoice(ByteString.copyFrom(encodedVoice))
                .setTimestamp(lastTimestemp)
                .setDatagramOrderId(datagramId)
                .build();

        playerVoiceHandler.channelRead0(channelContext, playerVoice);

        return encoder;
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
        System.out.println(name);
        Path fileInput = Paths.get(name);
        final InputStream inputStream = Files.newInputStream(fileInput);
        byte[] actualVoice = inputStream.readAllBytes();
        assertThat(actualVoice.length, is(equalTo(480)));

        float percentageZeros = (float) countNumberZeros(actualVoice) / 480;
        assertThat(percentageZeros, is(greaterThan(numberZerosPercentage)));
    }

    @Test
    public void writeVoiceFileWithGapTest() throws Exception {
        final UUID playerOneUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        final long encoder = setup(playerOneUuid);

        byte[] outputEncode = new byte[MAX_FRAME_SIZE * CHANNELS * 2];

        final int packageLength = Opus.encode(encoder, AudioSamplesHelper.audioSampleTwo, 0, 480, outputEncode, 0, outputEncode.length);

        final byte[] encodedVoice = Arrays.copyOfRange(outputEncode, 0, packageLength);

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
        assertThat(actualVoice.length, is(equalTo(1440)));
        int countNumberZeros = countNumberZeros(actualVoice);
        assertThat(countNumberZeros, is(greaterThan(480)));
    }

    @Test
    public void udpSequenceTest() throws Exception {
        final UUID playerUuid = UUID.fromString("7b694294-d74c-413f-bf32-0c855f6c181e");
        final long encoder = setup(playerUuid);

        byte[] outputEncode = new byte[MAX_FRAME_SIZE * CHANNELS * 2];
        final int packageLength = Opus.encode(encoder, AudioSamplesHelper.audioSampleTwo, 0, 480, outputEncode, 0, outputEncode.length);

        final byte[] encodedVoice = Arrays.copyOfRange(outputEncode, 0, packageLength);

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
        assertThat(actualVoice.length, is(equalTo(480)));
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
        for (File file: dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}
