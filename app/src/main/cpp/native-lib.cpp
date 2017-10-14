#include <jni.h>
#include <string>
#include "AIChess.h"

JNIEXPORT jintArray JNICALL
Java_org_soraworld_fourchess_AIChess_getSolution(JNIEnv *env, jobject instance,
                                                 jintArray comp_in_) {
    jint *comp_in = env->GetIntArrayElements(comp_in_, NULL);

    // TODO

    env->ReleaseIntArrayElements(comp_in_, comp_in, 0);

    int i = 0, j = 0, sign = 1;
    int comp[4][4];
    jint native[16];
    //【传入comp_in[16]】
    env->GetIntArrayRegion(comp_in_, 0, 16, native);
    //【转换4x4矩阵】
    for (i = 0; i < 4; i++)
        for (j = 0; j < 4; j++)
            comp[i][j] = native[4 * i + j];
    //【执行博弈树搜索】
    AIChess chess(sign, comp);
    chess.GetChessCP(comp);
    //【转换4x4矩阵】
    for (i = 0; i < 4; i++)
        for (j = 0; j < 4; j++)
            native[4 * i + j] = comp[i][j];
    //【传出解决方案】
    env->SetIntArrayRegion(comp_in_, 0, 16, native);
    return comp_in_;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_org_soraworld_fourchess_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
