package anyvr.app.lemon;

import java.util.UUID;

public class VoiceFileNameUtils {

    public static String voiceFileName(final String path, final UUID playerUuid) {
        return path + playerUuid.toString() + ".raw";
    }

    public static String mixingAudioFiles(final String path, final UUID playerOneUuid,final UUID playerTwoUuid) {
        return path + playerOneUuid + "+" + playerTwoUuid + ".raw";
    }
}
