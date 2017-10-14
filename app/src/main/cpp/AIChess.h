#ifndef FOURCHESS_AICHESS_H
#define FOURCHESS_AICHESS_H

inline void CopyComp(int compy[4][4], int comp[4][4]) {
    int i, j;
    for (i = 0; i < 4; i++)
        for (j = 0; j < 4; j++)
            compy[i][j] = comp[i][j];
}

class AIChess {
public:
    AIChess(int sign_in, int comp_in[4][4]);//初始化
    void SetChessPT();//设置位置信息
    void CalScoreMX();//计算解决方案，设置最高分scoremx
    int CalScorePT();

    int CalScoreUP();

    int CalScoreDN();

    int CalScoreLF();

    int CalScoreRG();

    int CalScoreKL(int xx, int yy);

    void SetChessCP();

    void SetChessKL(int xx, int yy);

    int GetScoreMX() { return scoremx; }

    void GetChessCP(int comp_out[4][4]);

private:
    int killsign[2][4] = {{2110, 211, 1120, 112},
                          {1220, 122, 2210, 221}};
    int sign;//标志
    int sum;//有效棋子总数
    int move;//要移动的棋子ID
    int drc[4];//4个棋子的上级移动方向
    int scoremx = 0;//最后总分
    int comp[4][4];//初始棋局&最终返回棋局
    int compy[4][4];//中间变量
    int x[4];
    int y[4];//4个棋子的位置
};

#endif //FOURCHESS_AICHESS_H
