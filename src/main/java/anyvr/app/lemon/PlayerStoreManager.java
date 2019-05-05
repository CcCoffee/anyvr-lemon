package anyvr.app.lemon;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.jni.Opus;
import anyvr.app.lemon.jni.OpusWrapper;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerStoreManager {

    private static Logger logger = LogManager.getLogger(PlayerStoreManager.class);
    private final PlayerStore playerStore;
    private final String voiceFilePath;

    public Player getPlayer(final UUID playerUuid, final Channel channel) throws IOException {
        if (!playerStore.isPlayerAlreadyExist(playerUuid)) {

            final Player player = createNewPlayer(playerUuid, channel);
            playerStore.add(player);
            logger.info("Create new Player");
            return player;
        } else {
            final Player player = playerStore.getPlayer(playerUuid).get();
            logger.info("Update Player");
            return player;
        }
    }

    private Player createNewPlayer(final UUID playerUuid, final Channel channel) throws IOException {
        final long decoder = OpusWrapper.createOpusDecoder();
        final String voiceFileName = VoiceFileNameUtils.voiceFileName(voiceFilePath, playerUuid);
        final Path fileOutput = Paths.get(voiceFileName);
        final OutputStream outputStream = Files.newOutputStream(fileOutput);

        return new Player(channel, playerUuid, decoder, outputStream, voiceFileName);
    }

    public void sendMessageToOtherPlayer(final UUID playerUuid, final Spec.PlayerVoice playerVoice) {
        playerStore.findAnotherPlayer(playerUuid)
                .ifPresent((Player currentPlayer) -> {
                    currentPlayer.getChannel().writeAndFlush(playerVoice);
                });
    }
}
