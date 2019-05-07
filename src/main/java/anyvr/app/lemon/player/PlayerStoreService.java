package anyvr.app.lemon.player;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.voiceFile.VoiceFileNameUtils;
import anyvr.app.lemon.jni.OpusDecoder;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerStoreService {

    private static Logger logger = LogManager.getLogger(PlayerStoreService.class);
    private final PlayerStore playerStore;
    private final String voiceFilePath;

    public Player getPlayer(final UUID playerId, final Channel channel) throws IOException {
        if (!playerStore.isPlayerAlreadyExist(playerId)) {

            final Player player = createNewPlayer(playerId, channel);
            playerStore.add(player);
            logger.info("Create new Player");
            return player;
        } else {
            final Player player = playerStore.getPlayer(playerId).get();
            logger.info("Update Player");
            return player;
        }
    }

    private Player createNewPlayer(final UUID playerId, final Channel channel) throws IOException {
        final OpusDecoder opusDecoder = new OpusDecoder();
        final String voiceFileName = VoiceFileNameUtils.voiceFileName(voiceFilePath, playerId);
        final Path fileOutput = Paths.get(voiceFileName);
        final OutputStream outputStream = Files.newOutputStream(fileOutput);

        return new Player(channel, playerId, opusDecoder, outputStream, voiceFileName);
    }

    public void sendMessageToOtherPlayer(final UUID playerId, final Spec.PlayerVoice playerVoice) {
        playerStore.findAnotherPlayer(playerId)
                .ifPresent((Player currentPlayer) ->
                        currentPlayer.getChannel().writeAndFlush(playerVoice)
                );
    }
}
