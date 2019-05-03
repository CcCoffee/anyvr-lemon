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
import anyvr.app.lemon.WritingVoiceFile;
import anyvr.app.lemon.jni.Opus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class PlayerVoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private static final int MAX_FRAME_SIZE = 6 * 480;
    private static final int CHANNELS = 1;
    private static final int SAMPLE_RATE = 24000;
    private static Logger logger = LogManager.getLogger(PlayerVoiceHandler.class);
    private String voiceFilePath;
    private final PlayerStore playerStore;
    private final Object lockDatagramId = new Object();

    public PlayerVoiceHandler(String voiceFilePath, PlayerStore playerStore) {
        this.voiceFilePath = voiceFilePath;
        this.playerStore = playerStore;
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        playerStore.remove(ctx.channel());
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Spec.PlayerVoice playerVoice) throws Exception {

        logger.info("UUID: " + playerVoice.getUuid());
        logger.info("Audio: " + Arrays.toString(playerVoice.getVoice().toByteArray()));

        UUID playerUuid = UUID.fromString(playerVoice.getUuid());

        Player player;
        if (!playerStore.isPlayerAlreadyExist(playerUuid)) {
            final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

            String audioFileName = voiceFilePath + playerUuid.toString() + ".raw";
            //WritingVoiceFile audioFile = new WritingVoiceFile(audioFileName);

            Path fileOutput = Paths.get(audioFileName);
            OutputStream outputStream = Files.newOutputStream(fileOutput);

            if (decoder == 0) {
                logger.error("Creating Decoder Error");
                return;
            }

            player = new Player(ctx.channel(), playerUuid, decoder, outputStream);
            playerStore.add(player);
            logger.info("Create new Player");
        } else {
            player = playerStore.getPlayer(playerUuid).get();
            logger.info("Update Player");
        }

        if (!isUdpInSequence(playerVoice.getDatagramOrderId(), player)) {
            return;
        }

        playerStore.findAnotherPlayer(playerUuid)
                .ifPresent((Player currentPlayer) -> {
                    currentPlayer.getChannel().writeAndFlush(playerVoice);
                });

        checkIfHaveToFillTheGap(player, playerVoice);
    }

    private boolean isUdpInSequence(int currentDatagrammId, Player player) {
        synchronized (lockDatagramId) {
            if (currentDatagrammId > player.getLastDatagramId()) {
                player.setLastDatagramId(currentDatagrammId);
                return true;
            }
            return false;
        }
    }

    //    private void checkIfHaveToFillTheGap(Player player, Spec.PlayerVoice playerVoice) throws IOException {
    //
    //        synchronized (player.getLock()) {
    //            byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];
    //
    //            if ((player.getLastTimestamp() == 0) || (player.getLastTimestamp() + 20) == playerVoice.getTimestamp()) {
    //                int currentFrameSize = Opus
    //                        .decode(player.getAudioDecoder(), playerVoice.getVoice().toByteArray(), 0, playerVoice.getVoice().toByteArray().length, output, 0, MAX_FRAME_SIZE, 0);
    //
    //                writePlayerAudioFile(currentFrameSize, output, player);
    //            } else {
    //                int fillGapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20) - 1;
    //                System.out.println("Fill Gap Counter: " + fillGapCounter);
    //                for (int i = 0; i < fillGapCounter; i++) {
    //
    //                    Opus.decode(player.getAudioDecoder(), null, 0, 0, output, 0,
    //                                    MAX_FRAME_SIZE, 0);
    //
    //
    //                    byte[] audioStream = new byte[480];
    //                    player.getVoiceFile().write(audioStream);
    //                }
    //            }
    //            player.setLastTimestamp(playerVoice.getTimestamp());
    //        }
    //    }

    private void checkIfHaveToFillTheGap(Player player, Spec.PlayerVoice playerVoice) throws IOException {

        synchronized (player.getLock()) {
            int fillGapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20);

            System.out.println(fillGapCounter);
            if ((fillGapCounter == 1) || (player.getLastTimestamp() == 0)) {
                writePlayerAudioFile(player, playerVoice);
            } else if (fillGapCounter > 1) {
                fillTheGap(player, fillGapCounter - 1);
                writePlayerAudioFile(player, playerVoice);
            } else {
                //throw Exception
            }
            player.setLastTimestamp(playerVoice.getTimestamp());
        }
    }

    private void fillTheGap(final Player player, final int fillGapCounter) {
        byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];
        for (int i = 0; i < fillGapCounter; i++) {
            Opus.decode(player.getAudioDecoder(), null, 0, 0, output, 0,
                    MAX_FRAME_SIZE, 0);

            byte[] audioStream = new byte[480];
            try {
                player.getVoiceFile().write(audioStream);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void writePlayerAudioFile(final Player player, final Spec.PlayerVoice playerVoice) {
        byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];

        int currentFrameSize = Opus
                .decode(player.getAudioDecoder(), playerVoice.getVoice().toByteArray(), 0, playerVoice.getVoice().toByteArray().length, output, 0,
                        MAX_FRAME_SIZE, 0);

        byte[] audioStream = new byte[currentFrameSize];
        for (int i = 0; i < currentFrameSize; i++) {
            audioStream[i] = output[i];
        }

        try {
            player.getVoiceFile().write(audioStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
