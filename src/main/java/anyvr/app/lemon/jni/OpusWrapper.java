package anyvr.app.lemon.jni;

import anyvr.app.lemon.OpusException;

public class OpusWrapper {
    private static final int CHANNELS = 1;
    private static final int SAMPLE_RATE = 24000;
    private static final int MAX_FRAME_SIZE = 6 * 480;

    public static long createOpusDecoder() {
        final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        if (decoder == 0) {
            throw new OpusException("Creating Opus Decoder Error");
        }
        return decoder;
    }

    public static long createOpusEncoder() {
        final long encoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        if (encoder == 0) {
            throw new OpusException("Creating Opus Encoder Error");
        }
        return encoder;
    }

    public static void destroyOpusDecoder(final long decoder) {
        Opus.decoder_destroy(decoder);
    }

    public static void destroyOpusEncoder(final long encoder) {
        Opus.decoder_destroy(encoder);
    }

    public static int decode(final long decoder, byte[] input, byte[] output) {
        return Opus.decode(decoder, input, 0, input.length, output, 0,
                MAX_FRAME_SIZE, 0);
    }

    public static int fillTheGap(final long decoder, byte[] output) {
        return Opus.decode(decoder, null, 0, 0, output, 0,
                MAX_FRAME_SIZE, 0);
    }
}
