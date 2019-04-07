/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jni_test_example_Opus */

#ifndef _Included_jni_test_example_Opus
#define _Included_jni_test_example_Opus
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     jni_test_example_Opus
 * Method:    decode
 * Signature: (J[BII[BIII)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_decode
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint, jbyteArray, jint, jint, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    decoder_create
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_jni_test_example_Opus_decoder_1create
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    decoder_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_jni_test_example_Opus_decoder_1destroy
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    decoder_get_nb_samples
 * Signature: (J[BII)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_decoder_1get_1nb_1samples
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    decoder_get_size
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_decoder_1get_1size
  (JNIEnv *, jclass, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encode
 * Signature: (J[BII[BII)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encode
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint, jbyteArray, jint, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_create
 * Signature: (II)J
 */
JNIEXPORT jlong JNICALL Java_jni_test_example_Opus_encoder_1create
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_destroy
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_jni_test_example_Opus_encoder_1destroy
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_bandwidth
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1bandwidth
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_bitrate
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1bitrate
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_complexity
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1complexity
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_dtx
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1dtx
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_inband_fec
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1inband_1fec
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_size
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1size
  (JNIEnv *, jclass, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_vbr
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1vbr
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_get_vbr_constraint
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1get_1vbr_1constraint
  (JNIEnv *, jclass, jlong);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_bandwidth
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1bandwidth
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_bitrate
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1bitrate
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_complexity
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1complexity
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_dtx
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1dtx
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_force_channels
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1force_1channels
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_inband_fec
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1inband_1fec
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_max_bandwidth
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1max_1bandwidth
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_packet_loss_perc
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1packet_1loss_1perc
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_vbr
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1vbr
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    encoder_set_vbr_constraint
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_encoder_1set_1vbr_1constraint
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    packet_get_bandwidth
 * Signature: ([BI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_packet_1get_1bandwidth
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    packet_get_nb_channels
 * Signature: ([BI)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_packet_1get_1nb_1channels
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     jni_test_example_Opus
 * Method:    packet_get_nb_frames
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_jni_test_example_Opus_packet_1get_1nb_1frames
  (JNIEnv *, jclass, jbyteArray, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
