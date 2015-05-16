#include"cn_void_3_fourchess_AIChess.h"
#include"AIChess.h"

//【JNI函数入口】
JNIEXPORT jintArray JNICALL Java_cn_void_13_fourchess_AIChess_getSolution(JNIEnv *env, jobject thiz, jintArray comp_in){
	int i=0,j=0,sign=1;
	int comp[4][4];
	jint native[16];
	//【传入comp_in[16]】
	env->GetIntArrayRegion(comp_in,0,16,native);
	//【转换4x4矩阵】
	for(i=0;i<4;i++)
	for(j=0;j<4;j++)
	comp[i][j]=native[4*i+j];
	//【执行博弈树搜索】
	AIChess chess(sign, comp);
	chess.GetChessCP(comp);
	//【转换4x4矩阵】
	for(i=0;i<4;i++)
	for(j=0;j<4;j++)
	native[4*i+j]=comp[i][j];
	//【传出解决方案】
	env->SetIntArrayRegion(comp_in,0,16, native);
	return comp_in;
}