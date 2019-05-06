package anyvr.app.lemon.jni;

import anyvr.app.lemon.OpusException;

public class OpusDecoder {
    private static final int CHANNELS = 1;
    private static final int SAMPLE_RATE = 24000;
    private static final int MAX_FRAME_SIZE = 6 * 480;

    private final long decoder;

    public OpusDecoder() {
        this.decoder = createOpusDecoder();
    }

    public long createOpusDecoder() {
        final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        if (decoder == 0) {
            throw new OpusException("Creating Opus Decoder Error");
        }
        return decoder;
    }

    public int decode(byte[] input, byte[] output) {
        return Opus.decode(decoder, input, 0, input.length, output, 0,
                MAX_FRAME_SIZE, 0);
    }

    public void destroyOpusDecoder() {
        Opus.decoder_destroy(decoder);
    }

    public int decodeLostPacket() {
        byte[] output = new byte[100];
        return Opus.decode(decoder, null, 0, 0, output, 0,
                MAX_FRAME_SIZE, 0);
    }
}
