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

    private String voicePath;

    public MixingVoiceFiles(final String voicePath) {
        this.voicePath = voicePath;
    }

    public void mixVoiceFile(final Player playerOne, final Player playerTwo) throws IOException {

        Path playerOneFile = Paths.get(voiceFileName(voicePath, playerOne.getPlayerId()));
        Path playerTwoFile = Paths.get(voiceFileName(voicePath, playerTwo.getPlayerId()));
        Path outputFile = Paths.get(VoiceFileNameUtils.mixVoiceFileName(voicePath, playerOne.getPlayerId(), playerTwo.getPlayerId()));

        InputStream playerOneStream = Files.newInputStream(playerOneFile);
        InputStream playerTwoStream = Files.newInputStream(playerTwoFile);
        OutputStream mixingStream = Files.newOutputStream(outputFile);

        mix(playerOneStream, playerTwoStream, mixingStream);

        mixingStream.close();
    }

    private void mix(InputStream playerOneStream, InputStream playerTwoStream, OutputStream mixingStream) throws IOException {

        while(true) {

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

            mixingStream.write(sumInBytes, 0, sumInBytes.length);
        }
        mixingStream.close();
    }
}
