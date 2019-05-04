package anyvr.app.lemon;

import static anyvr.app.lemon.VoiceFileNameUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.google.common.primitives.Shorts;
import com.google.protobuf.Mixin;

public class MixingVoiceFiles {

    private String path;

    public MixingVoiceFiles(final String path) {
        this.path = path;
    }

    public void mixVoice(final Player playerOne, final Player playerTwo) throws IOException {

        Path playerOneFile = Paths.get(voiceFileName(path, playerOne.getUuid()));
        Path playerTwoFile = Paths.get(voiceFileName(path, playerTwo.getUuid()));
        Path outputFile = Paths.get(VoiceFileNameUtils.mixingAudioFiles(path, playerOne.getUuid(), playerTwo.getUuid()));

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

            System.out.println("playerOneBytes: "+ Arrays.toString(playerOneBytes));
            System.out.println("playerTwoBytes: "+ Arrays.toString(playerTwoBytes));
            System.out.println("PlayerOneValue: "+ playerOneValue);
            System.out.println("PlayerTwoValue: "+ playerTwoValue);

            final short sum = (short) ((int) playerOneValue + (int) playerTwoValue);

            final byte[] sumInBytes = Shorts.toByteArray(sum);

            outputStream.write(sumInBytes, 0, sumInBytes.length);
        }
        outputStream.close();
    }
}
