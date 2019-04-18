package anyvr.app.lemon;

import java.util.ArrayList;

public class AudioCaching {

    private final Object lockAudioVoice = new Object();
    private AudioVoice audioVoices;

    public AudioCaching() {
        //audioVoices = new ArrayList<>();
        //audioVoices = Collections.synchronizedList(audioVoices);
    }

    public void findPlayerWithSameTimestamp(AudioVoice currentAudioVoice) {

//        audioVoices.stream().filter(voice -> {
//            AudioVoice oldAudioVoice2 = (AudioVoice) voice;
//            if((oldAudioVoice2.getTimestemps() + 20) < currentAudioVoice.getTimestemps()) {
//                return true;
//            } else {
//                return false;
//            }
//        }).findFirst();

        //override write on Disk
    }
}
