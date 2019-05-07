package anyvr.app.lemon.jni;

import static anyvr.app.lemon.jni.OpusConf.*;

import java.util.Arrays;

public class OpusDecoder {
    private static final int MAX_FRAME_SIZE = 2 * FRAME_SIZE;

    private final long decoder;

    public OpusDecoder() {
        this.decoder = createOpusDecoder();
    }

    private long createOpusDecoder() {
        final long decoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        if (decoder == 0) {
            throw new OpusException("Creating Opus Decoder Error");
        }
        return decoder;
    }

    public byte[] decode(byte[] input) {
        byte[] output = new byte[MAX_FRAME_SIZE * 2];
        int frameSize = Opus.decode(decoder, input, 0, input.length, output, 0,
                MAX_FRAME_SIZE, 0);
        return Arrays.copyOfRange(output, 0, frameSize);
    }

    public void destroyOpusDecoder() {
        Opus.decoder_destroy(decoder);
    }

    public int decodeLostPacket() {
        return Opus.decode(decoder, null, 0, 0, null, 0,
                MAX_FRAME_SIZE, 0);
    }
}
