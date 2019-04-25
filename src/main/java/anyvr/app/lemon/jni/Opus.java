package anyvr.app.lemon.jni;

public class Opus {

    public static final int BANDWIDTH_FULLBAND = 1105;

    public static final int BANDWIDTH_MEDIUMBAND = 1102;

    public static final int BANDWIDTH_NARROWBAND = 1101;

    public static final int BANDWIDTH_SUPERWIDEBAND = 1104;

    public static final int BANDWIDTH_WIDEBAND = 1103;

    public static final int INVALID_PACKET = -4;

    public static final int MAX_PACKET = 1 + 1275;

    public static final int OPUS_AUTO = -1000;

    public static final int OPUS_OK = 0;

    static {
        try {
            System.loadLibrary("opusjni");
        } catch (Error | Exception e) {
            System.out.println("***********************************************************************************");
            System.out.println("*** Failed to load JNI bridge library: " + e.toString());
        }
    }

    public static native int decode(long decoder, byte[] input, int inputOffset, int inputLength, byte[] output,
            int outputOffset, int outputFrameSize, int decodeFEC);

    public static native long decoder_create(int Fs, int channels);

    public static native void decoder_destroy(long decoder);

    public static native int decoder_get_nb_samples(long decoder, byte[] packet, int offset, int length);

    public static native int decoder_get_size(int channels);

    public static native int encode(long encoder, byte[] input, int inputOffset, int inputFrameSize, byte[] output,
            int outputOffset, int outputLength);

    public static native long encoder_create(int Fs, int channels);

    public static native void encoder_destroy(long encoder);

    public static native int encoder_get_bandwidth(long encoder);

    public static native int encoder_get_bitrate(long encoder);

    public static native int encoder_get_complexity(long encoder);

    public static native int encoder_get_dtx(long encoder);

    public static native int encoder_get_inband_fec(long encoder);

    public static native int encoder_get_size(int channels);

    public static native int encoder_get_vbr(long encoder);

    public static native int encoder_get_vbr_constraint(long encoder);

    public static native int encoder_set_bandwidth(long encoder, int bandwidth);

    public static native int encoder_set_bitrate(long encoder, int bitrate);

    public static native int encoder_set_complexity(long encoder, int complexity);

    public static native int encoder_set_dtx(long encoder, int dtx);

    public static native int encoder_set_force_channels(long encoder, int forcechannels);

    public static native int encoder_set_inband_fec(long encoder, int inbandFEC);

    public static native int encoder_set_max_bandwidth(long encoder, int maxBandwidth);

    public static native int encoder_set_packet_loss_perc(long encoder, int packetLossPerc);

    public static native int encoder_set_vbr(long encoder, int vbr);

    public static native int encoder_set_vbr_constraint(long encoder, int use_cvbr);

    public static native int packet_get_bandwidth(byte[] data, int offset);

    public static native int packet_get_nb_channels(byte[] data, int offset);

    public static native int packet_get_nb_frames(byte[] packet, int offset, int length);
}
