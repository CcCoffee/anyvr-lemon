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

    /**
     * Creates an OpusDecoder structure, returns a pointer to it or 0 on error.
     *
     * @param Fs       Sample rate to decode to
     * @param channels number of channels to decode to(1/2)
     *
     * @return A pointer to the OpusDecoder structure created, 0 on error.
     */
    public static native long decoder_create(int Fs, int channels);

    /**
     * Destroys an OpusDecoder, freeing it's resources.
     *
     * @param decoder Address of the structure (as returned from decoder_create)
     */
    public static native void decoder_destroy(long decoder);

    /**
     * Returns the number of samples in an opus packet
     *
     * @param decoder The decoder to use.
     * @param packet  Array holding the packet.
     * @param offset  Offset into packet where the actual packet begins.
     * @param length  Length of the packet.
     *
     * @return the number of samples in <tt>packet</tt> .
     */
    public static native int decoder_get_nb_samples(long decoder, byte[] packet, int offset, int length);

    /**
     * Returns the size in bytes required for an OpusDecoder structure.
     *
     * @param channels number of channels (1/2)
     *
     * @return the size in bytes required for an OpusDecoder structure.
     */
    public static native int decoder_get_size(int channels);

    /**
     * Encodes the input from <tt>input</tt> into an opus packet in <tt>output</tt>.
     *
     * @param encoder        The encoder to use.
     * @param input          Array containing PCM encoded input.
     * @param inputOffset    Offset to use into the <tt>input</tt> array
     * @param inputFrameSize The number of samples per channel in <tt>input</tt>.
     * @param output         Array where the encoded packet will be stored.
     * @param outputOffset
     * @param outputLength   The number of available bytes in <tt>output</tt>.
     *
     * @return The number of bytes written in <tt>output</tt>, or a negative on
     *         error.
     */
    public static native int encode(long encoder, byte[] input, int inputOffset, int inputFrameSize, byte[] output,
            int outputOffset, int outputLength);

    /**
     * Creates an OpusEncoder structure, returns a pointer to it casted to long. The
     * native function's <tt>application</tt> parameter is always set to
     * OPUS_APPLICATION_VOIP.
     *
     * @param Fs       Sample rate of the input PCM
     * @param channels number of channels in the input (1/2)
     *
     * @return A pointer to the OpusEncoder structure created, 0 on error
     */
    public static native long encoder_create(int Fs, int channels);

    /**
     * Destroys an OpusEncoder, freeing it's resources.
     *
     * @param encoder Address of the structure (as returned from encoder_create)
     */
    public static native void encoder_destroy(long encoder);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Returns the
     * current encoder audio bandwidth .
     *
     * @param encoder The encoder to use
     *
     * @return the current encoder audio bandwidth
     */
    public static native int encoder_get_bandwidth(long encoder);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Returns the
     * current encoder bitrate.
     *
     * @param encoder The encoder to use
     *
     * @return The current encoder bitrate.
     */
    public static native int encoder_get_bitrate(long encoder);

    public static native int encoder_get_complexity(long encoder);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Returns the
     * current DTX setting of the encoder.
     *
     * @param encoder The encoder to use
     *
     * @return the current DTX setting of the encoder.
     */
    public static native int encoder_get_dtx(long encoder);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Returns the
     * current inband FEC encoder setting.
     *
     * @param encoder The encoder to use
     *
     * @return the current inband FEC encoder setting.
     */
    public static native int encoder_get_inband_fec(long encoder);

    /**
     * Returns the size in bytes required for an OpusEncoder structure.
     *
     * @param channels number of channels (1/2)
     *
     * @return the size in bytes required for an OpusEncoder structure.
     */
    public static native int encoder_get_size(int channels);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Returns the
     * current encoder VBR setting
     *
     * @param encoder The encoder to use
     *
     * @return The current encoder VBR setting.
     */
    public static native int encoder_get_vbr(long encoder);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Returns the
     * current VBR constraint encoder setting.
     *
     * @param encoder The encoder to use
     *
     * @return the current VBR constraint encoder setting.
     */
    public static native int encoder_get_vbr_constraint(long encoder);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder audio bandwidth.
     *
     * @param encoder   The encoder to use
     * @param bandwidth The bandwidth to set, should be one of
     *                  <tt>BANDWIDTH_FULLBAND</tt>, <tt>BANDWIDTH_MEDIUMBAND</tt>,
     *                  <tt>BANDWIDTH_NARROWBAND</tt>,
     *                  <tt>BANDWIDTH_SUPERWIDEBAND</tt> or
     *                  <tt>BANDWIDTH_WIDEBAND</tt>.
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_bandwidth(long encoder, int bandwidth);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder bitrate
     *
     * @param encoder The encoder to use
     * @param bitrate The bitrate to set
     *
     * @return <tt>OPUS_OK</tt> on success
     */
    public static native int encoder_set_bitrate(long encoder, int bitrate);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder complexity setting.
     *
     * @param encoder    The encoder to use
     * @param complexity The complexity level, from 1 to 10
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_complexity(long encoder, int complexity);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the DTX
     * setting of the encoder.
     *
     * @param encoder The encoder to use
     * @param dtx     0 to turn DTX off, non-zero to turn it on
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_dtx(long encoder, int dtx);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the force
     * channels setting of the encoder.
     *
     * @param encoder       The encoder to use
     * @param forcechannels Number of channels
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_force_channels(long encoder, int forcechannels);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder FEC setting.
     *
     * @param encoder   The encoder to use
     * @param inbandFEC 0 to turn FEC off, non-zero to turn it on.
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_inband_fec(long encoder, int inbandFEC);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * maximum audio bandwidth to be used by the encoder.
     *
     * @param encoder      The encoder to use
     * @param maxBandwidth The maximum bandwidth to use, should be one of
     *                     <tt>BANDWIDTH_FULLBAND</tt>,
     *                     <tt>BANDWIDTH_MEDIUMBAND</tt>,
     *                     <tt>BANDWIDTH_NARROWBAND</tt>,
     *                     <tt>BANDWIDTH_SUPERWIDEBAND</tt> or
     *                     <tt>BANDWIDTH_WIDEBAND</tt>
     *
     * @return <tt>OPUS_OK</tt> on success.
     */
    public static native int encoder_set_max_bandwidth(long encoder, int maxBandwidth);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder's expected packet loss percentage.
     *
     * @param encoder        The encoder to use
     * @param packetLossPerc
     *
     * @return OPUS_OK on success.
     */
    public static native int encoder_set_packet_loss_perc(long encoder, int packetLossPerc);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder VBR setting
     *
     * @param encoder The encoder to use
     * @param vbr     0 to turn VBR off, non-zero to turn it on.
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_vbr(long encoder, int vbr);

    /**
     * Wrapper around the native <tt>opus_encoder_ctl</tt> function. Sets the
     * encoder VBR constraint setting
     *
     * @param encoder  The encoder to use
     * @param use_cvbr 0 to turn VBR constraint off, non-zero to turn it on.
     *
     * @return OPUS_OK on success
     */
    public static native int encoder_set_vbr_constraint(long encoder, int use_cvbr);

    /**
     * Returns the audio bandwidth of an Opus packet, one of
     * <tt>BANDWIDTH_FULLBAND</tt>, <tt>BANDWIDTH_MEDIUMBAND</tt>,
     * <tt>BANDWIDTH_NARROWBAND</tt>, <tt>BANDWIDTH_SUPERWIDEBAND</tt> or
     * <tt>BANDWIDTH_WIDEBAND</tt>, or <tt>INVALID_PACKET</tt> on error.
     *
     * @param data   Array holding the packet.
     * @param offset Offset into packet where the actual packet begins.
     *
     * @return one of <tt>BANDWIDTH_FULLBAND</tt>, <tt>BANDWIDTH_MEDIUMBAND</tt>,
     *         <tt>BANDWIDTH_NARROWBAND</tt>, <tt>BANDWIDTH_SUPERWIDEBAND</tt>,
     *         <tt>BANDWIDTH_WIDEBAND</tt>, or <tt>INVALID_PACKET</tt> on error.
     */
    public static native int packet_get_bandwidth(byte[] data, int offset);

    /**
     * Returns the number of channels encoded in an Opus packet.
     *
     * @param data   Array holding the packet.
     * @param offset Offset into packet where the actual packet begins.
     *
     * @return the number of channels encoded in <tt>data</tt>.
     */
    public static native int packet_get_nb_channels(byte[] data, int offset);

    /**
     * Returns the number of frames in an Opus packet.
     *
     * @param packet Array holding the packet.
     * @param offset Offset into packet where the actual packet begins.
     * @param length Length of the packet.
     *
     * @return the number of frames in <tt>packet</tt>.
     */
    public static native int packet_get_nb_frames(byte[] packet, int offset, int length);
}
