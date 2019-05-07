package anyvr.app.lemon.voiceFile;

import static com.almondtools.conmatch.datatypes.PrimitiveArrayMatcher.byteArrayContaining;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import anyvr.app.lemon.player.Player;

public class MixingVoiceFilesTest {

    private final String path = "tests/";

    @BeforeEach
    public void removeAudioFiles() {
        File file = new File("tests/");
        purgeFiles(file);
    }

    @Test
    public void mixVoiceFile() throws IOException {

        final byte[] exptectedSum = { 0, 3, 6, 6 };
        final MixingVoiceFiles mixingVoiceFiles = new MixingVoiceFiles(path);
        final UUID playerOneUuid = UUID.fromString("818743aa-7f7c-4b0b-a4a1-6c2d8aa6c7e5");
        final UUID playerTwoUuid = UUID.fromString("dea7ccda-1f8b-4db3-8fbf-71365e86bec5");

        final String playerOneFileName = VoiceFileNameUtils.voiceFileName(path, playerOneUuid);
        final String playerTwoFileName = VoiceFileNameUtils.voiceFileName(path, playerTwoUuid);

        Path playerOneFile = Paths.get(playerOneFileName);
        Path playerTwoFile = Paths.get(playerTwoFileName);

        OutputStream playerOneOutput = Files.newOutputStream(playerOneFile);
        OutputStream playerTwoOutput = Files.newOutputStream(playerTwoFile);

        final byte[] playerOneBytes = { 0, 1, 3, 5 };
        final byte[] playerTwoBytes = { 0, 2, 3, 1 };

        playerOneOutput.write(playerOneBytes);
        playerTwoOutput.write(playerTwoBytes);

        final Player playerOne = new Player(null, playerOneUuid, null, playerOneOutput, playerOneFileName);
        final Player playerTwo = new Player(null, playerTwoUuid, null, playerTwoOutput, playerTwoFileName);

        mixingVoiceFiles.mixVoiceFile(playerOne, playerTwo);

        Path mixingFile = Paths.get(VoiceFileNameUtils.mixVoiceFileName(path, playerOne.getPlayerId(), playerTwo.getPlayerId()));
        InputStream mixingInputStream = Files.newInputStream(mixingFile);

        final byte[] mixingBytes = mixingInputStream.readAllBytes();
        assertThat(mixingBytes, byteArrayContaining(exptectedSum));
    }

    private void purgeFiles(File dir) {
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }
}
