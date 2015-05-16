package cn.void_3.fourchess;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FourChess extends Activity {

    int FLAG, N = 10;
    Coordinate to_move;
    int chess_box[][] = new int[4][4];
    RadioButton chess_btn[][] = new RadioButton[4][4];
    int history[][][] = new int[N][4][4];
    Boolean first_step = true;boolean long_click=false;
    ImageButton retract,restart;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_chess);

        DisplayMetrics display=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int h=display.widthPixels;
        LinearLayout chessboard=(LinearLayout)findViewById(R.id.chess_board);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height=h;params.addRule(RelativeLayout.CENTER_IN_PARENT);
        chessboard.setLayoutParams(params);
        ImageView show=(ImageView)findViewById(R.id.game_back);
        Drawable background=Drawable.createFromPath(Welcome.getSDPath() + "/Android/data/cn.void_3.fourchess/missyou.png");
        show.setImageDrawable(background);
        //绑定
        Bunding();
        //初始化
        IniChessBox();
        //清除记录
        ClearHistory();
        RecordHistory();
        ScreeImplement(true);
        //呈现
        ChessImplement();
        //提示
        TextView red_hint=(TextView)findViewById(R.id.redshow);
        TextView blue_hint=(TextView)findViewById(R.id.blueshow);
        red_hint.setText("单击悔棋,长按返回!");
        blue_hint.setText("单击悔棋,长按返回!");
        retract=(ImageButton)findViewById(R.id.retract);
        retract.setOnLongClickListener(new ImageButton.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Button confirm=(Button)findViewById(R.id.blueyes);
                Button reject =(Button)findViewById(R.id.blueno);
                TextView show=(TextView)findViewById(R.id.blueshow);
                show.setText("返回菜单/重新开局");
                confirm.setVisibility(View.VISIBLE);reject.setVisibility(View.VISIBLE);
                confirm.setText("返回菜单");reject.setText("重新开局");
                long_click=true;confirm.setClickable(true);reject.setClickable(true);
                return false;
            }
        });
        restart=(ImageButton)findViewById(R.id.restart);
        restart.setOnLongClickListener(new ImageButton.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Button confirm=(Button)findViewById(R.id.redyes);
                Button reject =(Button)findViewById(R.id.redno);
                TextView show=(TextView)findViewById(R.id.redshow);
                show.setText("返回菜单/重新开局");
                confirm.setVisibility(View.VISIBLE);reject.setVisibility(View.VISIBLE);
                confirm.setText("返回菜单");reject.setText("重新开局");
                long_click=true;confirm.setClickable(true);reject.setClickable(true);
                return false;
            }
        });
    }
    //绑定
    public void Bunding() {

        chess_btn[0][0] = (RadioButton) findViewById(R.id.chess00);
        chess_btn[0][1] = (RadioButton) findViewById(R.id.chess01);
        chess_btn[0][2] = (RadioButton) findViewById(R.id.chess02);
        chess_btn[0][3] = (RadioButton) findViewById(R.id.chess03);

        chess_btn[1][0] = (RadioButton) findViewById(R.id.chess10);
        chess_btn[1][1] = (RadioButton) findViewById(R.id.chess11);
        chess_btn[1][2] = (RadioButton) findViewById(R.id.chess12);
        chess_btn[1][3] = (RadioButton) findViewById(R.id.chess13);

        chess_btn[2][0] = (RadioButton) findViewById(R.id.chess20);
        chess_btn[2][1] = (RadioButton) findViewById(R.id.chess21);
        chess_btn[2][2] = (RadioButton) findViewById(R.id.chess22);
        chess_btn[2][3] = (RadioButton) findViewById(R.id.chess23);

        chess_btn[3][0] = (RadioButton) findViewById(R.id.chess30);
        chess_btn[3][1] = (RadioButton) findViewById(R.id.chess31);
        chess_btn[3][2] = (RadioButton) findViewById(R.id.chess32);
        chess_btn[3][3] = (RadioButton) findViewById(R.id.chess33);
    }

    //初始化ChessBox
    public void IniChessBox() {
        int i, j;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                if (i == 0) chess_box[i][j] = 1;
                if (i == 3) chess_box[i][j] = 2;
                if (i == 1 || i == 2) chess_box[i][j] = 0;
            }
    }

    //清除记录
    public void ClearHistory() {
        int i, j, k;
        for (k = 0; k < N; k++)
            for (i = 0; i < 4; i++)
                for (j = 0; j < 4; j++)
                    history[k][i][j] = 0;
    }

    //吃棋
    public void ChessEat(int now_x, int now_y, int red_or_blue) {
        int i, s;
        if (red_or_blue == 1) {

            for (i = 0, s = 0; i < 4; i++)
                s = s * 10 + chess_box[now_x][i];
            switch (s) {
                case 2110:
                    chess_box[now_x][0] = 0;
                    break;
                case 211:
                    chess_box[now_x][1] = 0;
                    break;
                case 1120:
                    chess_box[now_x][2] = 0;
                    break;
                case 112:
                    chess_box[now_x][3] = 0;
                    break;
            }
            for (i = 0, s = 0; i < 4; i++)
                s = s * 10 + chess_box[i][now_y];
            switch (s) {
                case 2110:
                    chess_box[0][now_y] = 0;
                    break;
                case 211:
                    chess_box[1][now_y] = 0;
                    break;
                case 1120:
                    chess_box[2][now_y] = 0;
                    break;
                case 112:
                    chess_box[3][now_y] = 0;
                    break;
            }
        }
        if (red_or_blue == 2) {
            for (i = 0, s = 0; i < 4; i++)
                s = s * 10 + chess_box[now_x][i];
            switch (s) {
                case 1220:
                    chess_box[now_x][0] = 0;
                    break;
                case 122:
                    chess_box[now_x][1] = 0;
                    break;
                case 2210:
                    chess_box[now_x][2] = 0;
                    break;
                case 221:
                    chess_box[now_x][3] = 0;
                    break;
            }
            for (i = 0, s = 0; i < 4; i++)
                s = s * 10 + chess_box[i][now_y];
            switch (s) {
                case 1220:
                    chess_box[0][now_y] = 0;
                    break;
                case 122:
                    chess_box[1][now_y] = 0;
                    break;
                case 2210:
                    chess_box[2][now_y] = 0;
                    break;
                case 221:
                    chess_box[3][now_y] = 0;
                    break;
            }
        }
    }

    //呈现
    public void ChessImplement() {
        int i, j;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                if (chess_box[i][j] == 0)
                    chess_btn[i][j].setBackgroundResource(R.drawable.space_chess);
                if (chess_box[i][j] == 1)
                    chess_btn[i][j].setBackgroundResource(R.drawable.red_chess);
                if (chess_box[i][j] == 2)
                    chess_btn[i][j].setBackgroundResource(R.drawable.blue_chess);
            }
    }

    //显示
    public void ScreeImplement(Boolean clear) {
        TextView red = (TextView) findViewById(R.id.redshow);
        TextView blue = (TextView) findViewById(R.id.blueshow);
        Button blue_confirm=(Button)findViewById(R.id.blueyes);
        Button blue_reject =(Button)findViewById(R.id.blueno);
        Button red_confirm=(Button)findViewById(R.id.redyes);
        Button red_reject =(Button)findViewById(R.id.redno);
        if (clear) {
            red.setText("");
            blue.setText("");
            blue_confirm.setText("");blue_reject.setText("");
            red_confirm.setText("");red_reject.setText("");
            blue_confirm.setVisibility(View.INVISIBLE);blue_reject.setVisibility(View.INVISIBLE);
            red_confirm.setVisibility(View.INVISIBLE);red_reject.setVisibility(View.INVISIBLE);
            blue_confirm.setClickable(false);blue_reject.setClickable(false);
            red_confirm.setClickable(false);red_reject.setClickable(false);
            return;
        }

        if (CheckWin() == 1) {
            red.setText("恭喜，你赢了！");
            blue.setText("哎呀，你输了！");
        } else if (CheckWin() == 2) {
            red.setText("哎呀，你输了！");
            blue.setText("恭喜，你赢了！");
        }
    }

    //响应
    public void ChessClick(View v) {
        Coordinate now = getXYbyId(v.getId());
        if (chess_box[now.getX()][now.getY()] != 0) {
            if(first_step)ScreeImplement(true);
            if (to_move != null) {
                ChessImplement();
                if (chess_box[to_move.getX()][to_move.getY()] == 1)
                    chess_btn[to_move.getX()][to_move.getY()].setBackgroundResource(R.drawable.red_chess);
                else if (chess_box[to_move.getX()][to_move.getY()] == 2)
                    chess_btn[to_move.getX()][to_move.getY()].setBackgroundResource(R.drawable.blue_chess);
            }
            to_move = now;
            return;
        }
        if (first_step && to_move != null && chess_box[to_move.getX()][to_move.getY()] != 0) {
            FLAG = chess_box[to_move.getX()][to_move.getY()];
            first_step = false;
        }
        if (to_move != null && chess_box[to_move.getX()][to_move.getY()] == FLAG) {
            if (Math.abs(now.getX() - to_move.getX()) + Math.abs(now.getY() - to_move.getY()) == 1) {

                chess_box[now.getX()][now.getY()] = FLAG;
                chess_box[to_move.getX()][to_move.getY()] = 0;
                //吃棋
                ChessEat(now.getX(), now.getY(), FLAG);
                //更换FLAG
                if (FLAG == 1) FLAG = 2;
                else FLAG = 1;
                //记录
                RecordHistory();
                //呈现
                ChessImplement();
                long_click=false;
                ScreeImplement(false);
            }
        }
    }

    //输赢
    public int CheckWin() {
        int i, j, red = 0, blue = 0;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++) {
                if (chess_box[i][j] == 1) red++;
                if (chess_box[i][j] == 2) blue++;
            }
        if (red <= 1) return 2;
        if (blue <= 1) return 1;
        return 0;
    }

    //记录
    public void RecordHistory() {
        int i, j, k;
        //记录后移一位
        for (k = N - 2; k >= 0; k--)
            for (i = 0; i < 4; i++)
                for (j = 0; j < 4; j++)
                    history[k + 1][i][j] = history[k][i][j];
        //当前数据填入第一条记录
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                history[0][i][j] = chess_box[i][j];
    }

    //请求悔棋
    public void RedRequest(View v){
        Button confirm=(Button)findViewById(R.id.blueyes);
        Button reject =(Button)findViewById(R.id.blueno);
        TextView blueshow=(TextView)findViewById(R.id.blueshow);
        int i,j,sum=0;
        if(long_click){
            return;
        }
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                sum = sum + history[1][i][j];
        if (sum == 0) return;
        blueshow.setText("对方请求悔棋!");
        confirm.setVisibility(View.VISIBLE);reject.setVisibility(View.VISIBLE);
        confirm.setText("同意");reject.setText("拒绝");
        //允许点击
        confirm.setClickable(true);reject.setClickable(true);
    }
    public void BlueRequest(View v){
        Button confirm=(Button)findViewById(R.id.redyes);
        Button reject =(Button)findViewById(R.id.redno);
        TextView redshow=(TextView)findViewById(R.id.redshow);
        int i,j,sum=0;
        if(long_click){
            return;
        }
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                sum = sum + history[1][i][j];
        if (sum == 0) return;
        redshow.setText("对方请求悔棋!");
        confirm.setVisibility(View.VISIBLE);reject.setVisibility(View.VISIBLE);
        confirm.setText("同意");reject.setText("拒绝");
        //允许点击
        confirm.setClickable(true);reject.setClickable(true);
    }
    //允许悔棋
    public void BlueAgreeRetract(View v){

        if(long_click)finish();
        ScreeImplement(true);
        int i,j,sum=0;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                sum = sum + history[1][i][j];
        if (sum == 0) return;
        RetractChess();
    }
    public void RejectRetract(View v){
        if(long_click)ResetChessBoard(v);
        long_click=false;
        ScreeImplement(true);
    }
    public void RedAgreeRetract(View v){
        if(long_click)finish();
        ScreeImplement(true);
        int i,j,sum=0;
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                sum = sum + history[1][i][j];
        if (sum == 0) return;
        RetractChess();
    }
    //悔棋
    public void RetractChess() {
        int i, j, k;

        //取出第二条记录
        for (i = 0; i < 4; i++)
            for (j = 0; j < 4; j++)
                chess_box[i][j] = history[1][i][j];
        //记录前调一位
        for (k = 1; k < N; k++)
            for (i = 0; i < 4; i++)
                for (j = 0; j < 4; j++) {
                    history[k - 1][i][j] = history[k][i][j];
                    history[k][i][j] = 0;//清除后一位记录
                }
        //执行呈现
        ChessImplement();
        ScreeImplement(true);
        //切换FLAG
        if (FLAG == 1) FLAG = 2;
        else FLAG = 1;
    }

    //重置，响应复位按钮
    public void ResetChessBoard(View v) {
        //初始化ChessBox
        IniChessBox();
        //清除记录
        ClearHistory();
        //记录
        RecordHistory();
        //清除状态
        ScreeImplement(true);
        first_step = true;
        to_move = null;
        //呈现
        ChessImplement();
    }

    //获取坐标
    public Coordinate getXYbyId(int id) {
        Coordinate xy = new Coordinate();
        switch (id) {
            case R.id.chess00:
                xy.setXY(0, 0);
                break;
            case R.id.chess01:
                xy.setXY(0, 1);
                break;
            case R.id.chess02:
                xy.setXY(0, 2);
                break;
            case R.id.chess03:
                xy.setXY(0, 3);
                break;

            case R.id.chess10:
                xy.setXY(1, 0);
                break;
            case R.id.chess11:
                xy.setXY(1, 1);
                break;
            case R.id.chess12:
                xy.setXY(1, 2);
                break;
            case R.id.chess13:
                xy.setXY(1, 3);
                break;

            case R.id.chess20:
                xy.setXY(2, 0);
                break;
            case R.id.chess21:
                xy.setXY(2, 1);
                break;
            case R.id.chess22:
                xy.setXY(2, 2);
                break;
            case R.id.chess23:
                xy.setXY(2, 3);
                break;

            case R.id.chess30:
                xy.setXY(3, 0);
                break;
            case R.id.chess31:
                xy.setXY(3, 1);
                break;
            case R.id.chess32:
                xy.setXY(3, 2);
                break;
            case R.id.chess33:
                xy.setXY(3, 3);
                break;
        }
        return xy;
    }

    class Coordinate {
        private int x, y;

        public void setXY(int setx, int sety) {
            x = setx;
            y = sety;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}