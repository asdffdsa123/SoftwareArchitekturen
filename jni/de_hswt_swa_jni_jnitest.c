#include "../bin/de_hswt_swa_jni_JNITest.h"
#include <jni.h>
#include <string.h>
#include <dirent.h> 


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
 * Method:    dir
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_de_hswt_swa_jni_JNITest_dir(JNIEnv * env, jclass unused){
    jclass listclass = env->FindClass("java/util/ArrayList");
    jmethodID listconstr = env->GetMethodID(listclass, "<init>", "()V");
    jobject list = env->NewObject(listclass, listconstr);
    jmethodID addMeth = env->GetMethodID(listclass, "add", "(Ljava/lang/Object;)Z");
    DIR *d;
    struct dirent *dir;
    d = opendir(".");
    if (d)
    {
      while ((dir = readdir(d)) != NULL)
      {
	env->CallBooleanMethod(list, addMeth, env->NewStringUTF(dir->d_name));
      }

      closedir(d);
    }

    return list;
}



















