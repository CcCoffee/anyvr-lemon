package anyvr.app.lemon;

import static anyvr.app.lemon.jni.OpusConf.CHANNELS;
import static anyvr.app.lemon.jni.OpusConf.FRAME_SIZE;
import static anyvr.app.lemon.jni.OpusConf.SAMPLE_RATE;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import anyvr.app.lemon.jni.Opus;

public class OpusTest {
    private static final int OPUS_BAD_ARG = -1;
    private static final int MAX_FRAME_SIZE = 2 * FRAME_SIZE;

    @Test
    public void encodeTest() {
        final long encoder = Opus.encoder_create(SAMPLE_RATE, CHANNELS);

        byte[] outputEncode = new byte[MAX_FRAME_SIZE * CHANNELS * 2];
        final int packageLength = Opus.encode(encoder, AudioSamplesHelper.audioSampleOne, 0, AudioSamplesHelper.audioSampleOne.length, outputEncode, 0, outputEncode.length);

        //encoder should not be  -1

        assertThat(encoder, is(not(OPUS_BAD_ARG)));
        assertThat(packageLength, is(greaterThan(0)));
    }

    @Test
    public void decodeTest() {
        final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        byte[] output = new byte[MAX_FRAME_SIZE * 2];
        final int packageLength = Opus.decode(decoder, AudioSamplesHelper.opusSampeOne, 0, AudioSamplesHelper.opusSampeOne.length, output, 0, MAX_FRAME_SIZE, 0);

        //encoder should not be  -1

        assertThat(decoder, is(not(OPUS_BAD_ARG)));
        assertThat(packageLength, is(greaterThan(0)));
    }

    @Test
    public void decoderDestroyTest() {
        final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        Opus.decoder_destroy(decoder);
    }

    @Test
    public void encoderDestroyTest() {
        final long encoder = Opus.encoder_create(SAMPLE_RATE, CHANNELS);

        Opus.encoder_destroy(encoder);
    }

    @Test
    public void encoderBitrateTest() {
        final long encoder = Opus.encoder_create(SAMPLE_RATE, CHANNELS);

        final int opus_status = Opus.encoder_set_bitrate(encoder, 64000);

        final int actualBitrate = Opus.encoder_get_bitrate(encoder);

        assertThat(opus_status, is(not(-1)));
        assertThat(actualBitrate, is(equalTo(64000)));
    }

    @Test
    public void encoderComplexictyTest() {
        final long encoder = Opus.encoder_create(SAMPLE_RATE, CHANNELS);

        final int opus_status = Opus.encoder_set_complexity(encoder, 5);
        final int actualComplexity = Opus.encoder_get_complexity(encoder);

        assertThat(opus_status, is(not(-1)));
        assertThat(actualComplexity, is(equalTo(5)));
    }

    @Test
    public void encoderPacketLossTest() {
        final long encoder = Opus.encoder_create(SAMPLE_RATE, CHANNELS);

        final int opus_status = Opus.encoder_set_packet_loss_perc(encoder, 1);

        assertThat(opus_status, is(not(-1)));
    }

}
