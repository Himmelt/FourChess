package org.soraworld.fourchess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.soraworld.fourchess.mode.AIActivity;
import org.soraworld.fourchess.mode.PPActivity;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GameClick(View view) {
        this.startActivity(new Intent(this, PPActivity.class));
    }

    public void AIClick(View view) {
        this.startActivity(new Intent(this, AIActivity.class));
    }

}
