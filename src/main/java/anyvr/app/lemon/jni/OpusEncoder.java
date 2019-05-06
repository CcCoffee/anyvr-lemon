package anyvr.app.lemon.jni;

import anyvr.app.lemon.OpusException;

public class OpusEncoder {
    private static final int CHANNELS = 1;
    private static final int SAMPLE_RATE = 24000;
    private static final int FRAME_SIZE = 480;
    private final long encoder;

    public OpusEncoder() {
        this.encoder = createOpusEncoder();
    }

    private long createOpusEncoder() {
        final long encoder = Opus.decoder_create(SAMPLE_RATE, CHANNELS);

        if (encoder == 0) {
            throw new OpusException("Creating Opus Encoder Error");
        }
        return encoder;
    }

    private int encode(byte[] input, byte[] output) {
        return Opus.encode(encoder, input, 0, FRAME_SIZE, output, 0, output.length);
    }

    public int getBandWidth() {
        return Opus.encoder_get_bandwidth(encoder);
    }

    public int getComplexity() {
        return Opus.encoder_get_complexity(encoder);
    }

    public int getBitrate() {
        return Opus.encoder_get_bitrate(encoder);
    }

    public void setBandwidth(int bandwidth) {
        Opus.encoder_set_bandwidth(encoder, bandwidth);
    }

    public void setBitrate(int bitrate) {
        Opus.encoder_set_bitrate(encoder, bitrate);
    }

    public void setComplexixity(int complexixity) {
        Opus.encoder_set_complexity(encoder, complexixity);
    }

    public void setMaxBandWidth(int maxBandWidth) {
        Opus.encoder_set_max_bandwidth(encoder, maxBandWidth);
    }

    public void setPacketLossPerc(int packetLossPerc) {
        Opus.encoder_set_packet_loss_perc(encoder, packetLossPerc);
    }

    public void destroyOpusEncoder() {
        Opus.encoder_destroy(encoder);
    }
}
