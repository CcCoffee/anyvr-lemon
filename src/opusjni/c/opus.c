#include <stdint.h>
#include <opus/opus.h>
#include "anyvr_app_lemon_jni_Opus.h"

jbyte* opus_decode_convert_to_native_byte_array(JNIEnv *env, jbyteArray jni_input, jint input_length, int* ret) {
    jbyte *native_input;
    if (jni_input && input_length) {
        native_input = (*env)->GetPrimitiveArrayCritical(env, jni_input, 0);
        *ret = native_input ? OPUS_OK : OPUS_ALLOC_FAIL;
    } else{
        native_input = 0;
        *ret = OPUS_OK;
    }
    return native_input;
}

JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_decode(JNIEnv *env, jclass clazz, jlong decoder, jbyteArray jni_input,
                                                            jint input_offset, jint input_length, jbyteArray jni_output,
                                                            jint output_offset, jint output_frameSize, jint decode_FEC)
{
    int opus_status;
    if (jni_output)
    {
        jbyte *native_input = opus_decode_convert_to_native_byte_array(env, jni_input, input_length, &opus_status);
        if (opus_status == OPUS_OK)
        {
            jbyte *native_output = (*env)->GetPrimitiveArrayCritical(env, jni_output, 0);

            if (native_output)
            {
                opus_status = opus_decode(
                        (OpusDecoder *)(intptr_t)decoder,
                        (unsigned char *)(native_input ? (native_input + input_offset) : NULL),
                        input_length,
                        (opus_int16 *)(native_output + output_offset),
                        output_frameSize,
                        decode_FEC);
                (*env)->ReleasePrimitiveArrayCritical(env, jni_output, native_output, 0);
            }
            else
                opus_status = OPUS_ALLOC_FAIL;
            if (native_input)
            {
                (*env)->ReleasePrimitiveArrayCritical(
                        env,
                        jni_input, native_input, JNI_ABORT);
            }
        }
    }
    else
        opus_status = OPUS_BAD_ARG;
    return opus_status;
}

/*
 * Class:     Opus
 * Method:    decoder_create
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_anyvr_app_lemon_jni_Opus_decoder_1create(JNIEnv *env, jclass clazz, jint Fs, jint channels)
{
    int error;
    OpusDecoder *decoder = opus_decoder_create(Fs, channels, &error);

    if (OPUS_OK != error) {
        decoder = 0;
    }
    return (jlong)(intptr_t)decoder;
}

/*
 * Class:     Opus
 * Method:    decoder_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_anyvr_app_lemon_jni_Opus_decoder_1destroy(JNIEnv *env, jclass clazz, jlong decoder)
{
opus_decoder_destroy((OpusDecoder *)(intptr_t)decoder);
}

/*
 * Class:     Opus
 * Method:    decoder_get_nb_samples
 * Signature: (J[BII)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_decoder_1get_1nb_1samples(JNIEnv *env, jclass clazz, jlong decoder, jbyteArray jni_packet, jint offset,
                                                                               jint length)
{
    int ret;

    if (jni_packet)
    {
        jbyte *native_packet = (*env)->GetPrimitiveArrayCritical(env, jni_packet, NULL);

        if (native_packet)
        {
            ret = opus_decoder_get_nb_samples(
                    (OpusDecoder *)(intptr_t)decoder,
                    (unsigned char *)(native_packet + offset),
                    length);
            (*env)->ReleasePrimitiveArrayCritical(
                    env,
                    jni_packet, native_packet, JNI_ABORT);
        }
        else
            ret = OPUS_ALLOC_FAIL;
    }
    else
        ret = OPUS_BAD_ARG;
    return ret;
}

/*
 * Class:     Opus
 * Method:    decoder_get_size
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_decoder_1get_1size(JNIEnv *env, jclass clazz, jint channels)
{
    return opus_decoder_get_size(channels);
}

/*
 * Class:     Opus
 * Method:    encode
 * Signature: (J[BII[BII)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encode(JNIEnv *env, jclass clazz, jlong encoder, jbyteArray input,
                                                            jint input_offset, jint input_frame_size, jbyteArray output,
                                                            jint output_offset, jint output_length)
{
    int ret;

    if (input && output)
    {
        jbyte *native_input = (*env)->GetPrimitiveArrayCritical(env, input, 0);

        if (native_input)
        {
            jbyte *output_ = (*env)->GetPrimitiveArrayCritical(env, output, 0);

            if (output_)
            {
                ret = opus_encode(
                        (OpusEncoder *)(intptr_t)encoder,
                        (opus_int16 *)(native_input + input_offset),
                        input_frame_size,
                        (unsigned char *)(output_ + output_offset),
                        output_length);
                (*env)->ReleasePrimitiveArrayCritical(env, output, output_, 0);
            }
            else
                ret = OPUS_ALLOC_FAIL;
            (*env)->ReleasePrimitiveArrayCritical(
                    env,
                    input, native_input, JNI_ABORT);
        }
        else
            ret = OPUS_ALLOC_FAIL;
    }
    else
        ret = OPUS_BAD_ARG;
    return ret;
}

/*
 * Class:     Opus
 * Method:    encoder_create
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1create(JNIEnv *env, jclass clazz, jint Fs, jint channels)
{
    int error;
    OpusEncoder *encoder = opus_encoder_create(Fs, channels, OPUS_APPLICATION_VOIP, &error);

    if (OPUS_OK != error)
        encoder = 0;
    return (jlong)(intptr_t)encoder;
}

/*
 * Class:     Opus
 * Method:    encoder_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1destroy(JNIEnv *env, jclass clazz, jlong encoder)
{
opus_encoder_destroy((OpusEncoder *)(intptr_t)encoder);
}

/*
 * Class:     Opus
 * Method:    encoder_get_bandwidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1bandwidth(JNIEnv *env, jclass clazz, jlong encoder)
{
    opus_int32 x;
    int ret = opus_encoder_ctl(
    (OpusEncoder *)(intptr_t)encoder,
            OPUS_GET_BANDWIDTH(&x));

    return (OPUS_OK == ret) ? x : ret;
}

/*
 * Class:     Opus
 * Method:    encoder_get_bitrate
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1bitrate(JNIEnv *env, jclass clazz, jlong encoder)
{
    opus_int32 x;
    int ret = opus_encoder_ctl(
    (OpusEncoder *)(intptr_t)encoder,
            OPUS_GET_BITRATE(&x));

    return (OPUS_OK == ret) ? x : ret;
}

/*
 * Class:     Opus
 * Method:    encoder_get_complexity
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1complexity(JNIEnv *env, jclass clazz, jlong encoder)
{
    opus_int32 x;
    int ret = opus_encoder_ctl(
    (OpusEncoder *)(intptr_t)encoder,
            OPUS_GET_COMPLEXITY(&x));

    return (OPUS_OK == ret) ? x : ret;
}

/*
 * Class:     Opus
 * Method:    encoder_get_vbr_constraint
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1vbr_1constraint(JNIEnv *env, jclass clazz, jlong encoder)
{
    opus_int32 x;
    int ret = opus_encoder_ctl(
    (OpusEncoder *)(intptr_t)encoder,
            OPUS_GET_VBR_CONSTRAINT(&x));

    return (OPUS_OK == ret) ? x : ret;
}

/*
 * Class:     Opus
 * Method:    encoder_set_bandwidth
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1bandwidth(JNIEnv *env, jclass clazz, jlong encoder, jint bandwidth)
{
    opus_int32 x = bandwidth;

    return opus_encoder_ctl(
            (OpusEncoder *)(intptr_t)encoder,
            OPUS_SET_BANDWIDTH(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_bitrate
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1bitrate(JNIEnv *env, jclass clazz, jlong encoder, jint bitrate)
{
    opus_int32 x = bitrate;

    return opus_encoder_ctl(
            (OpusEncoder *)(intptr_t)encoder,
            OPUS_SET_BITRATE(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_complexity
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1complexity(JNIEnv *env, jclass clazz, jlong encoder, jint complexity)
{
    opus_int32 x = complexity;

    return opus_encoder_ctl(
            (OpusEncoder *)(intptr_t)encoder,
            OPUS_SET_COMPLEXITY(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_packet_loss_perc
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1packet_1loss_1perc(JNIEnv *env, jclass clazz, jlong encoder, jint packet_loss_perc)
{
    opus_int32 x = packet_loss_perc;

    return opus_encoder_ctl(
            (OpusEncoder *)(intptr_t)encoder,
            OPUS_SET_PACKET_LOSS_PERC(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_max_bandwidth
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1max_1bandwidth(JNIEnv *env, jclass clazz, jlong encoder, jint maxBandwidth)
{
	opus_int32 x = maxBandwidth;

	return opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_SET_MAX_BANDWIDTH(x));
}
