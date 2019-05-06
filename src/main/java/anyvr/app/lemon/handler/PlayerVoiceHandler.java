package anyvr.app.lemon.handler;

import java.util.Arrays;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.Player;
import anyvr.app.lemon.PlayerStore;
import anyvr.app.lemon.PlayerVoiceService;
import anyvr.app.lemon.VoiceFileWriter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PlayerVoiceHandler extends SimpleChannelInboundHandler<Spec.PlayerVoice> {

    private static Logger logger = LogManager.getLogger(PlayerVoiceHandler.class);
    private final PlayerStore playerStore;
    private final Object lockDatagramId = new Object();
    private final VoiceFileWriter voiceFileWriter;
    private final PlayerVoiceService playerVoiceService;

    public PlayerVoiceHandler(String voiceFilePath, PlayerStore playerStore, VoiceFileWriter voiceFileWriter) {
        this.playerStore = playerStore;
        this.playerVoiceService = new PlayerVoiceService(playerStore, voiceFilePath);
        this.voiceFileWriter = voiceFileWriter;
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        playerStore.remove(ctx.channel());
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Spec.PlayerVoice playerVoice) throws Exception {
        logger.info("UUID: " + playerVoice.getUuid());
        logger.info("Audio: " + Arrays.toString(playerVoice.getVoice().toByteArray()));

        UUID playerId = UUID.fromString(playerVoice.getUuid());

        Player player = playerVoiceService.getPlayer(playerId, ctx.channel());

        if (!isUdpInSequence(playerVoice.getDatagramOrderId(), player)) {
            return;
        }

        voiceFileWriter.checkIfHaveToFillTheGap(player, playerVoice);
        voiceFileWriter.writePlayerAudioFile(player, playerVoice);

        playerVoiceService.sendMessageToOtherPlayer(playerId, playerVoice); //ToDo Test
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
