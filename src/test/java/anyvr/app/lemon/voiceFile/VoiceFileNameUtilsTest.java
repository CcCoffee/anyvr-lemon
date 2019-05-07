package anyvr.app.lemon.voiceFile;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class VoiceFileNameUtilsTest {

    private static final String FILE_TYPE = ".raw";

    @Test
    public void voiceFileNameTest() {
        final String path =  "tests/";
        UUID playerId = UUID.randomUUID();

        String exptectedFileName = path + playerId.toString() + FILE_TYPE;
        String actualFileName = VoiceFileNameUtils.voiceFileName(path, playerId);

        assertThat(actualFileName, is(equalTo(exptectedFileName)));
    }

    @Test
    public void mixVoiceFileNameTest() {
        final String path =  "tests/";
        UUID playerOneId = UUID.randomUUID();
        UUID playerTwoId = UUID.randomUUID();

        String exptectedFileName = path + playerOneId + "+" + playerTwoId + FILE_TYPE;
        String actualFileName = VoiceFileNameUtils.mixVoiceFileName(path, playerOneId, playerTwoId);

        assertThat(actualFileName, is(equalTo(exptectedFileName)));
    }
}
