package anyvr.app.lemon.jni;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.LessThan;

import anyvr.app.lemon.AudioSamplesHelper;
import anyvr.app.lemon.jni.OpusDecoder;
import anyvr.app.lemon.jni.OpusEncoder;

public class OpusEncoderTest {

    private static final int FRAME_SIZE = 480;
    private static final int MAX_PACKET_SIZE = FRAME_SIZE * 2;

    @Test
    public void decodeTest() {
        OpusEncoder opusEncoder = new OpusEncoder();
        final byte[] decoded = opusEncoder.encode(AudioSamplesHelper.audioSampleOne);

        assertThat(decoded.length, is(lessThan(MAX_PACKET_SIZE)));
    }
}
