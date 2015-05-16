void CopyComp(int compy[4][4], int comp[4][4]){
	int i, j;
	for (i = 0; i < 4; i++)
	for (j = 0; j < 4; j++)
		compy[i][j] = comp[i][j];
}
class AIChess
{
public:
	AIChess(int sign_in, int comp_in[4][4]);//初始化
	void SetChessPT();//设置位置信息
	void CalScoreMX();//计算解决方案，设置最高分scoremx
	int  CalScorePT();
	int  CalScoreUP();
	int  CalScoreDN();
	int  CalScoreLF();
	int  CalScoreRG();
	int  CalScoreKL(int xx, int yy);
	void SetChessCP();
	void SetChessKL(int xx, int yy);
	int  GetScoreMX(){ return scoremx; }
	void GetChessCP(int comp_out[4][4]);

private:
	static int killsign[2][4];
	int sign;//标志
	int sum;//有效棋子总数
	int move;//要移动的棋子ID
	int drc[4];//4个棋子的上级移动方向
	int scoremx = 0;//最后总分
	int comp[4][4];//初始棋局&最终返回棋局
	int compy[4][4];//中间变量
	int x[4]; int y[4];//4个棋子的位置
};
int AIChess::killsign[2][4] = { { 2110, 211, 1120, 112 }, { 1220, 122, 2210, 221 } };
AIChess::AIChess(int sign_in, int comp_in[4][4]){
	sign = sign_in;//传入标志位
	CopyComp(comp, comp_in);//传入棋局
	SetChessPT();//锁定标志位棋子位置及数量
	//初始化完成
	CalScoreMX();//计算总分，设置移动棋子ID及移动方向
	SetChessCP();//根据棋子ID及移动方向布置解决方案棋局
}
void AIChess::SetChessPT(){
	int i, j;
	sum = 0;
	for (i = 0; i < 4; i++)
	for (j = 0; j < 4; j++){
		if (comp[i][j] == sign){
			x[sum] = i; y[sum++] = j;
		}
	}
}
void AIChess::CalScoreMX(){
	//选择4棋子中的最高分
	int i, score[4];
	for (i = 0; i < sum; i++){
		move = i;
		score[i] = CalScorePT();
	}
	scoremx = score[0];
	for (i = 1; i < sum; i++){
		if (score[i]>scoremx){
			scoremx = score[i]; move = i;
		}
	}
	//scoremx设置完毕；move棋子ID设置完毕
}
int  AIChess::CalScorePT(){
	int i, score[4];
	score[0] = CalScoreUP();
	score[1] = CalScoreDN();
	score[2] = CalScoreLF();
	score[3] = CalScoreRG();
	for (i = 1; i < 4; i++){
		if (score[i]>score[0]){
			score[0] = score[i]; drc[move] = i;
		}
	}
	return score[0];
}
int  AIChess::CalScoreUP(){
	int score = 0;
	CopyComp(compy, comp);//拷贝初始化棋局
	if (x[move] == 0)return -1000;
	else{
		if (compy[x[move] - 1][y[move]] == 0){
			compy[x[move] - 1][y[move]] = compy[x[move]][y[move]];
			compy[x[move]][y[move]] = 0;
			if (x[move] == 3)score += 5;
			if (x[move] == 2)score += 3;
			score += CalScoreKL(x[move] - 1, y[move]);
			//【执行递归】
			if (sign == 1){
				AIChess opchess(2, compy);
				score -= opchess.GetScoreMX() / 2;
			}
			return score;
		}
		else return -1000;
	}
}
int  AIChess::CalScoreDN(){
	int score = 0;
	CopyComp(compy, comp);//拷贝初始化棋局
	if (x[move] == 3)return -1000;
	else{
		if (compy[x[move] + 1][y[move]] == 0){
			compy[x[move] + 1][y[move]] = compy[x[move]][y[move]];
			compy[x[move]][y[move]] = 0;
			if (x[move] == 0)score += 5;
			if (x[move] == 1)score += 3;
			score += CalScoreKL(x[move] + 1, y[move]);
			//【执行递归】
			if (sign == 1){
				AIChess opchess(2, compy);
				score -= opchess.GetScoreMX() / 2;
			}
			return score;
		}
		else return -1000;
	}
}
int  AIChess::CalScoreLF(){
	int score = 0;
	CopyComp(compy, comp);//拷贝初始化棋局
	if (y[move] == 0)return -1000;
	else{
		if (compy[x[move]][y[move] - 1] == 0){
			compy[x[move]][y[move] - 1] = compy[x[move]][y[move]];
			compy[x[move]][y[move]] = 0;
			if (y[move] == 3)score += 5;
			if (y[move] == 2)score += 3;
			score += CalScoreKL(x[move], y[move] - 1);
			//【执行递归】
			if (sign == 1){
				AIChess opchess(2, compy);
				score -= opchess.GetScoreMX() / 2;
			}
			return score;
		}
		else return -1000;
	}
}
int  AIChess::CalScoreRG(){
	int score = 0;
	CopyComp(compy, comp);//拷贝初始化棋局
	if (y[move] == 3)return -1000;
	else{
		if (compy[x[move]][y[move] + 1] == 0){
			compy[x[move]][y[move] + 1] = compy[x[move]][y[move]];
			compy[x[move]][y[move]] = 0;
			if (x[move] == 0)score += 5;
			if (x[move] == 1)score += 3;
			score += CalScoreKL(x[move], y[move] + 1);
			//【执行递归】
			if (sign == 1){
				AIChess opchess(2, compy);
				score -= opchess.GetScoreMX() / 2;
			}
			return score;
		}
		else return -1000;
	}
}
void AIChess::SetChessCP(){
	switch (drc[move]){
	case 0:	comp[x[move] - 1][y[move]] = comp[x[move]][y[move]];
		comp[x[move]][y[move]] = 0; SetChessKL(x[move] - 1, y[move]); break;
	case 1:	comp[x[move] + 1][y[move]] = comp[x[move]][y[move]];
		comp[x[move]][y[move]] = 0; SetChessKL(x[move] + 1, y[move]); break;
	case 2:	comp[x[move]][y[move] - 1] = comp[x[move]][y[move]];
		comp[x[move]][y[move]] = 0; SetChessKL(x[move], y[move] - 1); break;
	case 3:	comp[x[move]][y[move] + 1] = comp[x[move]][y[move]];
		comp[x[move]][y[move]] = 0; SetChessKL(x[move], y[move] + 1); break;
	}
}
int  AIChess::CalScoreKL(int xx, int yy){
	int i, sss, score = 0;

	for (i = 0, sss = 0; i < 4; i++)
		sss = sss * 10 + compy[xx][i];
	if (sss == killsign[sign - 1][0]){ compy[xx][0] = 0; score += 50; }
	if (sss == killsign[sign - 1][1]){ compy[xx][1] = 0; score += 50; }
	if (sss == killsign[sign - 1][2]){ compy[xx][2] = 0; score += 50; }
	if (sss == killsign[sign - 1][3]){ compy[xx][3] = 0; score += 50; }
	for (i = 0, sss = 0; i < 4; i++)
		sss = sss * 10 + compy[i][yy];
	if (sss == killsign[sign - 1][0]){ compy[0][yy] = 0; score += 50; }
	if (sss == killsign[sign - 1][1]){ compy[1][yy] = 0; score += 50; }
	if (sss == killsign[sign - 1][2]){ compy[2][yy] = 0; score += 50; }
	if (sss == killsign[sign - 1][3]){ compy[3][yy] = 0; score += 50; }
	return score;
}
void AIChess::SetChessKL(int xx, int yy){
	int i, sss;
	for (i = 0, sss = 0; i < 4; i++)
		sss = sss * 10 + comp[xx][i];
	if (sss == killsign[sign - 1][0])comp[xx][0] = 0;
	if (sss == killsign[sign - 1][1])comp[xx][1] = 0;
	if (sss == killsign[sign - 1][2])comp[xx][2] = 0;
	if (sss == killsign[sign - 1][3])comp[xx][3] = 0;
	for (i = 0, sss = 0; i < 4; i++)
		sss = sss * 10 + comp[i][yy];
	if (sss == killsign[sign - 1][0])comp[0][yy] = 0;
	if (sss == killsign[sign - 1][1])comp[1][yy] = 0;
	if (sss == killsign[sign - 1][2])comp[2][yy] = 0;
	if (sss == killsign[sign - 1][3])comp[3][yy] = 0;
}
void AIChess::GetChessCP(int comp_out[4][4]){
	CopyComp(comp_out, comp);
}