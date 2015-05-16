package cn.void_3.fourchess;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyMenu extends Activity {

    public void GameClick(View v){
        Intent intent=new Intent(this,FourChess.class);
        this.startActivity(intent);
    }
    public void AIClick(View v){
        Intent intent=new Intent(this,AIChess.class);
        this.startActivity(intent);
    }
    public void AboutClick(View v){
        Intent intent=new Intent(this,About.class);
        this.startActivity(intent);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageView show=(ImageView)findViewById(R.id.menu_back);
        Drawable background=Drawable.createFromPath(Welcome.getSDPath() + "/Android/data/cn.void_3.fourchess/missyou.png");
        show.setImageDrawable(background);
    }
}