package anyvr.app.lemon.voiceFile;

import java.util.UUID;

public class VoiceFileNameUtils {

    private static final String FILE_TYPE = ".raw";

    public static String voiceFileName(final String path, final UUID playerId) {
        return path + playerId.toString() + FILE_TYPE;
    }

    public static String mixVoiceFileName(final String path, final UUID playerOneId,final UUID playerTwoId) {
        return path + playerOneId + "+" + playerTwoId + FILE_TYPE;
    }
}
