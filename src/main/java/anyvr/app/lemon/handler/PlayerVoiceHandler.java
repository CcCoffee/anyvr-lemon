package anyvr.app.lemon.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.Player;
import anyvr.app.lemon.PlayerStore;
import anyvr.app.lemon.jni.Opus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerVoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private final String outPutFileName = "/app";
    private static final int MAX_FRAME_SIZE = 6 * 480;
    private static final int CHANNELS = 1;
    private static final int SAMPLE_RATE = 24000;
    private static Logger logger = LogManager.getLogger(PlayerVoiceHandler.class);
    private PlayerStore playerStore = new PlayerStore();
    private volatile int lastDatagramId;
    private final Object lockDatagramId = new Object();

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        playerStore.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final Spec.PlayerVoice playerVoice) throws Exception {
        IsUdpIsInSequence(playerVoice.getDatagramOrderId());

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

        checkIfHaveToFillTheGap(player, playerVoice);
    }

    private boolean IsUdpIsInSequence(int currentDatagrammId) {
        synchronized (lockDatagramId) {
            if (currentDatagrammId > lastDatagramId) {
                lastDatagramId = currentDatagrammId;
                return true;
            }
            return false;
        }
    }

    private void checkIfHaveToFillTheGap(Player player, Spec.PlayerVoice playerVoice) throws IOException {

        synchronized (player.getLock()) {
            byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];

            int currentFrameSize = Opus
                    .decode(player.getAudioDecoder(), playerVoice.getVoice().toByteArray(), 0, playerVoice.getVoice().toByteArray().length, output, 0,
                            MAX_FRAME_SIZE, 0);

            if ((player.getLastTimestamp() == 0) || (player.getLastTimestamp() + 20) == playerVoice.getTimestamp()) {
                writePlayerAudioFile(currentFrameSize, output, player);
            } else {
                int fillGapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20);

                for (int i = 0; i < fillGapCounter; i++) {
                    byte[] audioStream = new byte[currentFrameSize * CHANNELS * 2];
                    player.getAudioFile().write(audioStream);
                }
            }
            player.setLastTimestamp(playerVoice.getTimestamp());
        }
    }

    private void writePlayerAudioFile(int currentFrameSize, byte[] output, Player player) throws IOException {
        byte[] audioStream = new byte[currentFrameSize * CHANNELS * 2];
        for (int i = 0; i < currentFrameSize * CHANNELS * 2; i++) {
            audioStream[i] = output[i];
        }
        player.getAudioFile().write(audioStream);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
