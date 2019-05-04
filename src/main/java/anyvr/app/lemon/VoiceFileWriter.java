package anyvr.app.lemon;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anyvr.Spec;
import anyvr.app.lemon.jni.Opus;

public class VoiceFileWriter {

    private static Logger logger = LogManager.getLogger(VoiceFileWriter.class);
    private static final int MAX_FRAME_SIZE = 6 * 480;
    private static final int CHANNELS = 1;
    private static final int SAMPLE_RATE = 24000;

    public void checkIfHaveToFillTheGap(Player player, Spec.PlayerVoice playerVoice) {
        int gapCounter = (int) ((playerVoice.getTimestamp() - player.getLastTimestamp()) / 20);

        if (gapCounter > 1 && (player.getLastTimestamp() != 0)) {
            fillTheGapWithZeros(player, gapCounter - 1);
        }
    }

    private void fillTheGapWithZeros(final Player player, final int fillGapCounter) {
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

    public void writePlayerAudioFile(final Player player, final Spec.PlayerVoice playerVoice) {
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
        player.setLastTimestamp(playerVoice.getTimestamp());
    }
}
