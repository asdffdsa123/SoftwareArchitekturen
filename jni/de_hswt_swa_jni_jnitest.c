#include "../bin/de_hswt_swa_jni_JNITest.h"
#include <jni.h>
#include <string.h>
#include <sys/resource.h>


void throwNPE(JNIEnv * env, const char* msg){
    	jclass npclass = env->FindClass("java/lang/NullPointerException");
	if(npclass != NULL){
	    env->ThrowNew(npclass, "string == null");
	}
}

jint JNICALL Java_de_hswt_swa_jni_JNITest_addiere (JNIEnv * env, jclass clazz, jint x, jint y){
    return (jint) x + y;
  }

  
  /*
 * Class:     de_hswt_swa_jni_JNITest
 * Method:    strlen
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_de_hswt_swa_jni_JNITest_strlen(JNIEnv * env, jclass clazz, jstring string){
    if(string == NULL){
	//throw Exception
	throwNPE(env, "string == null");
	return 0;
    }
    const char* data = env->GetStringUTFChars(string, NULL);
    return strlen(data);
}

/*
 * Class:     de_hswt_swa_jni_JNITest
 * Method:    getDate
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_de_hswt_swa_jni_JNITest_getDate(JNIEnv * env, jclass unused){
    jclass dateclass = env->FindClass("java/util/Date");
    if(dateclass == NULL){
	throwNPE(env, "Date class not found");
	return NULL;
    }
    jmethodID dateconstr = env->GetMethodID(dateclass, "<init>", "()V");
    jobject date = env->NewObject(dateclass, dateconstr);
    jmethodID toString = env->GetMethodID(dateclass, "toString", "()Ljava/lang/String;");
    return (jstring) env->CallObjectMethod(date, toString);
} 

/*
 * Class:     de_hswt_swa_jni_JNITest
 * Method:    getMemorySize
 * Signature: ()I
 */
JNIEXPORT jlong JNICALL Java_de_hswt_swa_jni_JNITest_getMemorySize(JNIEnv *, jclass){
    struct rusage usage;
    getrusage(RUSAGE_CHILDREN, &usage);
    return usage.ru_maxrss;
}




















