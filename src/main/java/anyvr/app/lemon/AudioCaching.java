package anyvr.app.lemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class AudioCaching {

    private List<List> audioVoices;

    public AudioCaching() {
        audioVoices = new ArrayList<>();
        audioVoices = Collections.synchronizedList(audioVoices);
    }

    public void findPlayerWithSameTimestamp(AudioVoice currentAudioVoice) {

        audioVoices.stream().filter(voice -> {
            AudioVoice oldAudioVoice2 = (AudioVoice) voice;
            if((oldAudioVoice2.getTimestemps() + 20) < currentAudioVoice.getTimestemps()) {
                return true;
            } else {
                return false;
            }
        }).findFirst();

        //override write on Disk
    }
}
