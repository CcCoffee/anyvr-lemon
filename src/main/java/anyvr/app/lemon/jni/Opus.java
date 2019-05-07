package anyvr.app.lemon.jni;

public class Opus {

    static {
        try {
            System.loadLibrary("opusjni");
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

    public static native int decode(long decoder, byte[] input, int inputOffset, int inputLength, byte[] output,
            int outputOffset, int outputFrameSize, int decodeFEC);

    public static native long decoder_create(int Fs, int channels);

    public static native void decoder_destroy(long decoder);

    public static native int encode(long encoder, byte[] input, int inputOffset, int inputFrameSize, byte[] output,
            int outputOffset, int outputLength);

    public static native long encoder_create(int Fs, int channels);

    public static native void encoder_destroy(long encoder);

    public static native int encoder_get_bandwidth(long encoder);

    public static native int encoder_get_bitrate(long encoder);

    public static native int encoder_get_complexity(long encoder);

    public static native int encoder_set_bandwidth(long encoder, int bandwidth);

    public static native int encoder_set_bitrate(long encoder, int bitrate);

    public static native int encoder_set_complexity(long encoder, int complexity);

    public static native int encoder_set_max_bandwidth(long encoder, int maxBandwidth);

    public static native int encoder_set_packet_loss_perc(long encoder, int packetLossPerc);
}
