#include <stdint.h>
#include <opus/opus.h>
#include "anyvr_app_lemon_jni_Opus.h"

JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_decode(JNIEnv *env, jclass clazz, jlong decoder, jbyteArray input,
										jint inputOffset, jint inputLength, jbyteArray output,
										jint outputOffset, jint outputFrameSize, jint decodeFEC)
{
	int ret;

	if (output)
	{
		jbyte *input_;

		if (input && inputLength)
		{
			input_ = (*env)->GetPrimitiveArrayCritical(env, input, 0);
			ret = input_ ? OPUS_OK : OPUS_ALLOC_FAIL;
		}
		else
		{
			input_ = 0;
			ret = OPUS_OK;
		}
		if (OPUS_OK == ret)
		{
			jbyte *output_ = (*env)->GetPrimitiveArrayCritical(env, output, 0);

			if (output_)
			{
				ret = opus_decode(
					(OpusDecoder *)(intptr_t)decoder,
					(unsigned char *)(input_ ? (input_ + inputOffset) : NULL),
					inputLength,
					(opus_int16 *)(output_ + outputOffset),
					outputFrameSize,
					decodeFEC);
				(*env)->ReleasePrimitiveArrayCritical(env, output, output_, 0);
			}
			else
				ret = OPUS_ALLOC_FAIL;
			if (input_)
			{
				(*env)->ReleasePrimitiveArrayCritical(
					env,
					input, input_, JNI_ABORT);
			}
		}
	}
	else
		ret = OPUS_BAD_ARG;
	return ret;
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

	if (OPUS_OK != error)
		decoder = 0;
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
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_decoder_1get_1nb_1samples(JNIEnv *env, jclass clazz, jlong decoder, jbyteArray packet, jint offset,
														   jint length)
{
	int ret;

	if (packet)
	{
		jbyte *packet_ = (*env)->GetPrimitiveArrayCritical(env, packet, NULL);

		if (packet_)
		{
			ret = opus_decoder_get_nb_samples(
				(OpusDecoder *)(intptr_t)decoder,
				(unsigned char *)(packet_ + offset),
				length);
			(*env)->ReleasePrimitiveArrayCritical(
				env,
				packet, packet_, JNI_ABORT);
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
										jint inputOffset, jint inputFrameSize, jbyteArray output,
										jint outputOffset, jint outputLength)
{
	int ret;

	if (input && output)
	{
		jbyte *input_ = (*env)->GetPrimitiveArrayCritical(env, input, 0);

		if (input_)
		{
			jbyte *output_ = (*env)->GetPrimitiveArrayCritical(env, output, 0);

			if (output_)
			{
				ret = opus_encode(
					(OpusEncoder *)(intptr_t)encoder,
					(opus_int16 *)(input_ + inputOffset),
					inputFrameSize,
					(unsigned char *)(output_ + outputOffset),
					outputLength);
				(*env)->ReleasePrimitiveArrayCritical(env, output, output_, 0);
			}
			else
				ret = OPUS_ALLOC_FAIL;
			(*env)->ReleasePrimitiveArrayCritical(
				env,
				input, input_, JNI_ABORT);
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
	OpusEncoder *encoder = opus_encoder_create(Fs, channels, OPUS_APPLICATION_AUDIO, &error);

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
 * Method:    encoder_get_dtx
 * Signature: (J)I
 */
JNIEXPORT jint JNICALLJava_anyvr_app_lemon_jni_Opus_encoder_1get_1dtx(JNIEnv *env, jclass clazz, jlong encoder)
{
	opus_int32 x;
	int ret = opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_GET_DTX(&x));

	return (OPUS_OK == ret) ? x : ret;
}

/*
 * Class:     Opus
 * Method:    encoder_get_inband_fec
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1inband_1fec(JNIEnv *env, jclass clazz, jlong encoder)
{
	opus_int32 x;
	int ret = opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_GET_INBAND_FEC(&x));

	return (OPUS_OK == ret) ? x : ret;
}

/*
 * Class:     Opus
 * Method:    encoder_get_size
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1size(JNIEnv *enc, jclass clazz, jint channels)
{
	return opus_encoder_get_size(channels);
}

/*
 * Class:     Opus
 * Method:    encoder_get_vbr
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1get_1vbr(JNIEnv *env, jclass clazz, jlong encoder)
{
	opus_int32 x;
	int ret = opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_GET_VBR(&x));

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
 * Method:    encoder_set_dtx
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1dtx(JNIEnv *env, jclass clazz, jlong encoder, jint dtx)
{
	opus_int32 x = dtx;

	return opus_encoder_ctl((OpusEncoder *)(intptr_t)encoder, OPUS_SET_DTX(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_force_channels
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1force_1channels(JNIEnv *env, jclass clazz, jlong encoder, jint forcechannels)
{
	opus_int32 x = forcechannels;

	return opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_SET_FORCE_CHANNELS(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_inband_fec
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1inband_1fec(JNIEnv *env, jclass clazz, jlong encoder, jint inbandFEC)
{
	opus_int32 x = inbandFEC;

	return opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_SET_INBAND_FEC(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_max_bandwidth
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALLJava_anyvr_app_lemon_jni_Opus_encoder_1set_1max_1bandwidth(JNIEnv *env, jclass clazz, jlong encoder, jint maxBandwidth)
{
	opus_int32 x = maxBandwidth;

	return opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_SET_MAX_BANDWIDTH(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_packet_loss_perc
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1packet_1loss_1perc(JNIEnv *env, jclass clazz, jlong encoder, jint packetLossPerc)
{
	opus_int32 x = packetLossPerc;

	return opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_SET_PACKET_LOSS_PERC(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_vbr
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1vbr(JNIEnv *env, jclass clazz, jlong encoder, jint vbr)
{
	opus_int32 x = vbr;

	return opus_encoder_ctl((OpusEncoder *)(intptr_t)encoder, OPUS_SET_VBR(x));
}

/*
 * Class:     Opus
 * Method:    encoder_set_vbr_constraint
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_encoder_1set_1vbr_1constraint(JNIEnv *env, jclass clazz, jlong encoder, jint cvbr)
{
	opus_int32 x = cvbr;

	return opus_encoder_ctl(
		(OpusEncoder *)(intptr_t)encoder,
		OPUS_SET_VBR_CONSTRAINT(x));
}

/*
 * Class:     Opus
 * Method:    packet_get_bandwidth
 * Signature: ([BI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_packet_1get_1bandwidth(JNIEnv *env, jclass clazz, jbyteArray data, jint offset)
{
	int ret;

	if (data)
	{
		jbyte *data_ = (*env)->GetPrimitiveArrayCritical(env, data, NULL);

		if (data_)
		{
			ret = opus_packet_get_bandwidth((unsigned char *)(data_ + offset));
			(*env)->ReleasePrimitiveArrayCritical(env, data, data_, JNI_ABORT);
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
 * Method:    packet_get_nb_channels
 * Signature: ([BI)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_packet_1get_1nb_1channels(JNIEnv *env, jclass clazz, jbyteArray data, jint offset)
{
	int ret;

	if (data)
	{
		jbyte *data_ = (*env)->GetPrimitiveArrayCritical(env, data, NULL);

		if (data_)
		{
			ret = opus_packet_get_nb_channels(
				(unsigned char *)(data_ + offset));
			(*env)->ReleasePrimitiveArrayCritical(env, data, data_, JNI_ABORT);
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
 * Method:    packet_get_nb_frames
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_anyvr_app_lemon_jni_Opus_packet_1get_1nb_1frames(JNIEnv *env, jclass clazz, jbyteArray packet, jint offset, jint length)
{
	int ret;

	if (packet)
	{
		jbyte *packet_ = (*env)->GetPrimitiveArrayCritical(env, packet, NULL);

		if (packet_)
		{
			ret = opus_packet_get_nb_frames(
				(unsigned char *)(packet_ + offset),
				length);
			(*env)->ReleasePrimitiveArrayCritical(
				env,
				packet, packet_, JNI_ABORT);
		}
		else
			ret = OPUS_ALLOC_FAIL;
	}
	else
		ret = OPUS_BAD_ARG;
	return ret;
}
