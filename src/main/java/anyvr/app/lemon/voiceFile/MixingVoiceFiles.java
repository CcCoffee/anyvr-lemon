package anyvr.app.lemon.voiceFile;

import static anyvr.app.lemon.voiceFile.VoiceFileNameUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import anyvr.app.lemon.player.Player;
import com.google.common.primitives.Shorts;

public class MixingVoiceFiles {

    private String path;

    public MixingVoiceFiles(final String path) {
        this.path = path;
    }

    public void mixVoice(final Player playerOne, final Player playerTwo) throws IOException {

        Path playerOneFile = Paths.get(voiceFileName(path, playerOne.getPlayerId()));
        Path playerTwoFile = Paths.get(voiceFileName(path, playerTwo.getPlayerId()));
        Path outputFile = Paths.get(VoiceFileNameUtils.mixVoiceFileName(path, playerOne.getPlayerId(), playerTwo.getPlayerId()));

        InputStream playerOneStream = Files.newInputStream(playerOneFile);
        InputStream playerTwoStream = Files.newInputStream(playerTwoFile);

        OutputStream outputStream = Files.newOutputStream(outputFile);
        for (; ; ) {

            final byte[] playerOneBytes = new byte[2]; //16 Bit pcm
            final byte[] playerTwoBytes = new byte[2]; //16 Bit pcm

            int err = playerOneStream.read(playerOneBytes, 0, playerOneBytes.length);
            if (err == -1) {
                break;
            }

            err = playerTwoStream.read(playerTwoBytes, 0, playerTwoBytes.length);

            if (err == -1) {
                break;
            }

            final short playerOneValue = Shorts.fromByteArray(playerOneBytes);
            final short playerTwoValue = Shorts.fromByteArray(playerTwoBytes);

            final short sum = (short) ((int) playerOneValue + (int) playerTwoValue);

            final byte[] sumInBytes = Shorts.toByteArray(sum);

            outputStream.write(sumInBytes, 0, sumInBytes.length);
        }
        outputStream.close();
    }
}
