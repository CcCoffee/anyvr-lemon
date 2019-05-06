package anyvr.app.lemon;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;

public class VoiceFileWriter {

    private static Logger logger = LogManager.getLogger(VoiceFileWriter.class);

    public void checkIfHaveToFillTheGap(Player player, Spec.PlayerVoice playerVoice) {
        int gapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20);

        if (gapCounter > 1 && (player.getLastTimestamp() != 0)) {
            fillTheGapWithZeros(player, gapCounter - 1);
        }
    }

    private void fillTheGapWithZeros(final Player player, final int fillGapCounter) {
        for (int i = 0; i < fillGapCounter; i++) {
            player.getOpusDecoder().decodeLostPacket();

            byte[] audioStream = new byte[480];
            try {
                player.getVoiceFile().write(audioStream);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void writePlayerAudioFile(final Player player, final Spec.PlayerVoice playerVoice) {

        final byte[] decodedVoice = player.getOpusDecoder()
                .decode(playerVoice.getVoice().toByteArray());
        try {
            player.getVoiceFile().write(decodedVoice);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        player.setLastTimestamp(playerVoice.getTimestamp());
    }
}
