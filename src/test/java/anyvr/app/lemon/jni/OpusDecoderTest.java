package anyvr.app.lemon.jni;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import anyvr.app.lemon.tools.AudioSamplesHelper;

public class OpusDecoderTest {

    private static final int FRAME_SIZE = 480;

    @Test
    public void decodeTest() {
        OpusDecoder opusDecoder = new OpusDecoder();
        final byte[] decoded = opusDecoder.decode(AudioSamplesHelper.opusSampeOne);

        assertThat(decoded.length, is(equalTo(FRAME_SIZE)));
    }
}
