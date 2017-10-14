package org.soraworld.fourchess;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;


public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        WebView view = findViewById(R.id.about_page);
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        view.loadUrl("http://void-3.cn/?page_id=82");

        ImageView anime = findViewById(R.id.imageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) anime.getDrawable();
        animationDrawable.start();
    }

    public void Refresh(View v) {
        WebView page = findViewById(R.id.about_page);
        page.reload();
    }

    public void NextPage(View v) {
        WebView page = findViewById(R.id.about_page);
        page.goForward();
    }

    public void PrePage(View v) {
        WebView page = findViewById(R.id.about_page);
        page.goBack();
    }

    public void ClosePage(View v) {
        finish();
    }
}


