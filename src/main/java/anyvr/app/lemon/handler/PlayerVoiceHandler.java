package anyvr.app.lemon.handler;

import java.io.IOException;
import java.io.InputStream;
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
import anyvr.app.lemon.PlayerStoreManager;
import anyvr.app.lemon.ServerHandlerInitializer;
import anyvr.app.lemon.VoiceFileNameUtils;
import anyvr.app.lemon.VoiceFileWriter;
import anyvr.app.lemon.jni.Opus;
import com.google.common.primitives.Shorts;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerVoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private static Logger logger = LogManager.getLogger(PlayerVoiceHandler.class);
    private String voiceFilePath;
    private final PlayerStore playerStore;
    private final Object lockDatagramId = new Object();
    private final VoiceFileWriter voiceFileWriter = new VoiceFileWriter(); //ToDo
    private final PlayerStoreManager playerStoreManager;

    public PlayerVoiceHandler(String voiceFilePath, PlayerStore playerStore) {
        this.voiceFilePath = voiceFilePath;
        this.playerStore = playerStore;
        this.playerStoreManager = new PlayerStoreManager(playerStore, voiceFilePath);
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

        Player player = playerStoreManager.getPlayer(playerUuid, ctx.channel());

        if (!isUdpInSequence(playerVoice.getDatagramOrderId(), player)) {
            return;
        }

        voiceFileWriter.checkIfHaveToFillTheGap(player, playerVoice);
        voiceFileWriter.writePlayerAudioFile(player, playerVoice);

        playerStoreManager.sendMessageToOtherPlayer(playerUuid, playerVoice); //ToDo Test
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
    //            int gapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20);
    //
    //            if ((gapCounter == 1) || (player.getLastTimestamp() == 0)) {
    //                writePlayerAudioFile(player, playerVoice);
    //            } else if (gapCounter > 1) {
    //                fillTheGap(player, gapCounter - 1);
    //                writePlayerAudioFile(player, playerVoice);
    //            } else {
    //                //throw Exception
    //            }
    //            player.setLastTimestamp(playerVoice.getTimestamp());
    //    }
    //
    //    private void fillTheGap(final Player player, final int fillGapCounter) {
    //        byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];
    //        for (int i = 0; i < fillGapCounter; i++) {
    //            Opus.decode(player.getAudioDecoder(), null, 0, 0, output, 0,
    //                    MAX_FRAME_SIZE, 0);
    //
    //            byte[] audioStream = new byte[480];
    //            try {
    //                player.getVoiceFile().write(audioStream);
    //            } catch (IOException e) {
    //                logger.error(e.getMessage());
    //            }
    //        }
    //    }
    //
    //    private void writePlayerAudioFile(final Player player, final Spec.PlayerVoice playerVoice) {
    //        byte[] output = new byte[MAX_FRAME_SIZE * CHANNELS * 2];
    //
    //        int currentFrameSize = Opus
    //                .decode(player.getAudioDecoder(), playerVoice.getVoice().toByteArray(), 0, playerVoice.getVoice().toByteArray().length, output, 0,
    //                        MAX_FRAME_SIZE, 0);
    //
    //        byte[] audioStream = new byte[currentFrameSize];
    //        for (int i = 0; i < currentFrameSize; i++) {
    //            audioStream[i] = output[i];
    //        }
    //
    //        try {
    //            player.getVoiceFile().write(audioStream);
    //        } catch (IOException e) {
    //            logger.error(e.getMessage());
    //        }
    //    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
