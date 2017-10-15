package org.soraworld.fourchess.mode;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.soraworld.fourchess.R;


public class AIActivity extends Activity {

    Coordinate to_move;
    int yui_box[][] = new int[4][4];
    RadioButton yui_btn[][] = new RadioButton[4][4];

    static {
        //System.loadLibrary("ai-chess");
    }

    public native int[] getSolution(int comp_in[]);

    public void getAISolution() {
        int i, j;
        int solo[] = new int[16];
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                solo[4 * i + j] = yui_box[i][j];
        solo = getSolution(solo);
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                yui_box[i][j] = solo[4 * i + j];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int h = display.widthPixels;
        LinearLayout chessboard = findViewById(R.id.ai_board);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = h;
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        chessboard.setLayoutParams(params);
        //绑定
        Bunding();
        //初始化
        IniChessBox();
        ScreeImplement(true);
        //呈现
        ChessImplement();
    }

    //绑定
    public void Bunding() {

        yui_btn[0][0] = findViewById(R.id.yui00);
        yui_btn[0][1] = findViewById(R.id.yui01);
        yui_btn[0][2] = findViewById(R.id.yui02);
        yui_btn[0][3] = findViewById(R.id.yui03);

        yui_btn[1][0] = findViewById(R.id.yui10);
        yui_btn[1][1] = findViewById(R.id.yui11);
        yui_btn[1][2] = findViewById(R.id.yui12);
        yui_btn[1][3] = findViewById(R.id.yui13);

        yui_btn[2][0] = findViewById(R.id.yui20);
        yui_btn[2][1] = findViewById(R.id.yui21);
        yui_btn[2][2] = findViewById(R.id.yui22);
        yui_btn[2][3] = findViewById(R.id.yui23);

        yui_btn[3][0] = findViewById(R.id.yui30);
        yui_btn[3][1] = findViewById(R.id.yui31);
        yui_btn[3][2] = findViewById(R.id.yui32);
        yui_btn[3][3] = findViewById(R.id.yui33);
    }

    //初始化ChessBox
    public void IniChessBox() {
        int i, j;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                if (i == 0) yui_box[i][j] = 1;
                if (i == 3) yui_box[i][j] = 2;
                if (i == 1 || i == 2) yui_box[i][j] = 0;
            }
    }

    //吃棋
    public void ChessEat(int now_x, int now_y) {
        int i, s;
        for (i = 0, s = 0; i < 4; i++)
            s = s * 10 + yui_box[now_x][i];
        switch (s) {
            case 1220:
                yui_box[now_x][0] = 0;
                break;
            case 122:
                yui_box[now_x][1] = 0;
                break;
            case 2210:
                yui_box[now_x][2] = 0;
                break;
            case 221:
                yui_box[now_x][3] = 0;
                break;
        }
        for (i = 0, s = 0; i < 4; i++)
            s = s * 10 + yui_box[i][now_y];
        switch (s) {
            case 1220:
                yui_box[0][now_y] = 0;
                break;
            case 122:
                yui_box[1][now_y] = 0;
                break;
            case 2210:
                yui_box[2][now_y] = 0;
                break;
            case 221:
                yui_box[3][now_y] = 0;
                break;
        }
    }

    //呈现
    public void ChessImplement() {
        int i, j;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                if (yui_box[i][j] == 0) {
                    yui_btn[i][j].setBackgroundResource(R.drawable.space_chess);
                    yui_btn[i][j].setClickable(true);
                }
                if (yui_box[i][j] == 1) {
                    yui_btn[i][j].setBackgroundResource(R.drawable.red_chess);
                    yui_btn[i][j].setClickable(false);
                }
                if (yui_box[i][j] == 2) {
                    yui_btn[i][j].setBackgroundResource(R.drawable.blue_chess);
                    yui_btn[i][j].setClickable(true);
                }
            }
    }

    //显示
    public void ScreeImplement(Boolean clear) {
        TextView ai = findViewById(R.id.aishow);
        Button confirm = findViewById(R.id.aiyes);
        Button reject = findViewById(R.id.aino);
        if (clear) {
            ai.setText("");
            confirm.setText("");
            reject.setText("");
            confirm.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
            confirm.setClickable(false);
            reject.setClickable(false);
            return;
        }

        if (CheckWin() == 1) {
            ai.setText("哎呀，你输了！");
        } else if (CheckWin() == 2) {
            ai.setText("恭喜，你赢了！");
        }
    }

    //响应
    public void ChessClick(View v) {
        Coordinate now = getXYbyId(v.getId());
        if (yui_box[now.getX()][now.getY()] == 2) {
            to_move = now;
            return;
        }
        if (to_move != null) {
            if (Math.abs(now.getX() - to_move.getX()) + Math.abs(now.getY() - to_move.getY()) == 1) {

                yui_box[now.getX()][now.getY()] = 2;
                yui_box[to_move.getX()][to_move.getY()] = 0;
                //吃棋
                ChessEat(now.getX(), now.getY());
                //呈现
                ChessImplement();//CheckWin();
                //getAISolution
                getAISolution();
                //再次呈现
                ChessImplement();//CheckWin();
            }
        }
    }

    //输赢
    public int CheckWin() {
        int i, j, red = 0, blue = 0;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                if (yui_box[i][j] == 1) red++;
                if (yui_box[i][j] == 2) blue++;
            }
        if (red <= 1) return 2;
        if (blue <= 1) return 1;
        return 0;
    }

    //获取坐标
    public Coordinate getXYbyId(int id) {
        Coordinate xy = new Coordinate();
        switch (id) {
            case R.id.yui00:
                xy.setXY(0, 0);
                break;
            case R.id.yui01:
                xy.setXY(0, 1);
                break;
            case R.id.yui02:
                xy.setXY(0, 2);
                break;
            case R.id.yui03:
                xy.setXY(0, 3);
                break;

            case R.id.yui10:
                xy.setXY(1, 0);
                break;
            case R.id.yui11:
                xy.setXY(1, 1);
                break;
            case R.id.yui12:
                xy.setXY(1, 2);
                break;
            case R.id.yui13:
                xy.setXY(1, 3);
                break;

            case R.id.yui20:
                xy.setXY(2, 0);
                break;
            case R.id.yui21:
                xy.setXY(2, 1);
                break;
            case R.id.yui22:
                xy.setXY(2, 2);
                break;
            case R.id.yui23:
                xy.setXY(2, 3);
                break;

            case R.id.yui30:
                xy.setXY(3, 0);
                break;
            case R.id.yui31:
                xy.setXY(3, 1);
                break;
            case R.id.yui32:
                xy.setXY(3, 2);
                break;
            case R.id.yui33:
                xy.setXY(3, 3);
                break;
        }
        return xy;
    }

    private class Coordinate {
        private int x, y;

        void setXY(int setx, int sety) {
            x = setx;
            y = sety;
        }

        public int getX() {
            return x;
        }

        int getY() {
            return y;
        }
    }
}
