package anyvr.app.lemon.handler;

import static java.util.UUID.fromString;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.Player;
import anyvr.app.lemon.PlayerStore;
import anyvr.app.lemon.jni.Opus;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class VoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private String outPutFileName = "/app/logs/";
    private static final int MAX_PACKET_SIZE = 4 * 1276;
    private static final int MAX_FRAME_SIZE = 6 * 480;
    private static final int CHANNELS = 1;
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final int SAMPLE_RATE = 24000;
    private static Logger logger = LogManager.getLogger(VoiceHandler.class);

    private PlayerStore playerStore = new PlayerStore();

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        //        Channel incoming = ctx.channel();
        //        channels.add(incoming);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        playerStore.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Spec.PlayerVoice playerVoice) throws Exception {

        if (playerVoice == null) {
            throw new NullPointerException("Player Voice is null");
        }

        if (playerVoice.getUuid() == null) {
            throw new NullPointerException("Uuid is null");
        }

        logger.info("UUID: " + playerVoice.getUuid());
        logger.info("Audio: " + Arrays.toString(playerVoice.getVoice().toByteArray()));

        UUID playerUuid = UUID.fromString(playerVoice.getUuid());

        Player player;
        if (!playerStore.isPlayerAlreadyExist(playerUuid)) {
            final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

            String audioFileName = outPutFileName + playerUuid.toString();
            Path fileOutput = Paths.get(audioFileName);
            OutputStream audioFile = Files.newOutputStream(fileOutput);

            if (decoder == 0) {
                //throw Exception
                logger.error("Creating Decoder Error");
            }

            player = new Player(ctx.channel(), playerUuid, decoder, audioFile);
            playerStore.add(player);
            logger.info("Create new Player");
        } else {
            player = playerStore.getPlayer(playerUuid).get();
            logger.info("Update Player");
        }

        playerStore.findAnotherPlayer(playerUuid)
                .ifPresent((Player currentPlayer) -> {
                    currentPlayer.getChannel().writeAndFlush(playerVoice);
                });

        writeFile(player, playerVoice);
    }

    private void writeFile(Player player, Spec.PlayerVoice playerVoice) throws IOException {

        synchronized (player.getLock()) {
            byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];

            int currentFrameSize = Opus
                    .decode(player.getAudioDecoder(), playerVoice.getVoice().toByteArray(), 0, playerVoice.getVoice().toByteArray().length, output, 0,
                            MAX_FRAME_SIZE, 0);

            if (player.getLastTimestamp() == 0) {
                byte[] audioStream = new byte[currentFrameSize * CHANNELS * 2];
                for (int i = 0; i < currentFrameSize * CHANNELS * 2; i++) {
                    audioStream[i] = output[i];
                }
                player.getAudioFile().write(audioStream);
            } else {
                if (player.getLastTimestamp() + 20 == playerVoice.getTimestamp()) {
                    byte[] audioStream = new byte[currentFrameSize * CHANNELS * 2];
                    for (int i = 0; i < currentFrameSize * CHANNELS * 2; i++) {
                        audioStream[i] = output[i];
                    }
                    player.getAudioFile().write(audioStream);
                } else {
                    int fillGapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20);

                    for (int i = 0; i < fillGapCounter; i++) {
                        byte[] audioStream = new byte[currentFrameSize * CHANNELS * 2];
                        player.getAudioFile().write(audioStream);
                    }
                }
            }
            player.setLastTimestamp(playerVoice.getTimestamp());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
