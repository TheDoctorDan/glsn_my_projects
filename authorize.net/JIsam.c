#include <jni.h>

/*
 * Class:     JIsam
 * Method:    create
 * Signature: (Ljava/lang/String;II)I
 */
JNIEXPORT jint JNICALL Java_JIsam_create
  (JNIEnv *env, jobject jobj, jstring filename, jint key_len, jint record_size){
	/* int isam_create(String file_name, int key_len, int record_size); */

}

/*
 * Class:     JIsam
 * Method:    setup_new_file
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_JIsam_setup_1new_1file
  (JNIEnv *end, jobject jobj, jint fdes, jint key_len, jint record_size){
	/* int setup_new_file(int	fdes, int key_len, int record_size); */
}

/*
 * Class:     JIsam
 * Method:    initialize_alloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_JIsam_initialize_1alloc
  (JNIEnv *env, jobject jobj){
	/* int initialize_alloc(); */
}

/*
 * Class:     JIsam
 * Method:    insert
 * Signature: (ILjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_JIsam_insert
  (JNIEnv *env, jobject jobj, jint fdes, jstring key){
	/* long isam_insert(int fdes, String key) */
}

/*
 * Class:     JIsam
 * Method:    next
 * Signature: (ILjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_JIsam_next
  (JNIEnv *env, jobject jobj, jint fdes, jstring key){
	/* long	next(int fdes, String key); */
}

/*
 * Class:     JIsam
 * Method:    prev
 * Signature: (ILjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_JIsam_prev
  (JNIEnv *env, jobject jobj, jint fdes, jstring key){
	/* long	prev(int fdes, String key); */
}

/*
 * Class:     JIsam
 * Method:    fkl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_JIsam_fkl
  (JNIEnv *env, jobject jobj, jint fdes){
	/* int	fkl(int fdes); */
}

/*
 * Class:     JIsam
 * Method:    frl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_JIsam_frl
  (JNIEnv *env, jobject jobj, jint fdes){
	/* int	frl(int fdes); */
}

/*
 * Class:     JIsam
 * Method:    lock
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_JIsam_lock
  (JNIEnv *env, jobject jobj, jint fdes){
	/* int	lock(int fdes); */
}

/*
 * Class:     JIsam
 * Method:    unlock
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_JIsam_unlock
  (JNIEnv *env, jobject jobj, jint fdes){
	/* void	unlock(int fdes); */
}

/*
 * Class:     JIsam
 * Method:    wait_lock
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_JIsam_wait_1lock
  (JNIEnv *env, jobject jobj, jint fdes){
	/* int	wait_lock(int fdes); */
}

/*
 * Class:     JIsam
 * Method:    r_lock
 * Signature: (IJI)I
 */
JNIEXPORT jint JNICALL Java_JIsam_r_1lock
  (JNIEnv *env, jobject jobj, jint fdes, jlong where, jint area){
	/* int	r_lock(int fdes, long where, int area); */
}

/*
 * Class:     JIsam
 * Method:    r_unlock
 * Signature: (IJI)V
 */
JNIEXPORT void JNICALL Java_JIsam_r_1unlock
  (JNIEnv *env, jobject jobj, jint fdes, jlong where, jint area){
	/* void	r_unlock(int fdes, long where, int area); */
}


/*
 * Class:     JIsam
 * Method:    print_lock_list
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_JIsam_print_1lock_1list
  (JNIEnv *env, jobject jobj){
	/* void	print_lock_list(); */
}

