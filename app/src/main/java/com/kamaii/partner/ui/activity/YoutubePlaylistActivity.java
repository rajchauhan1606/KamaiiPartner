package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;

public class YoutubePlaylistActivity extends AppCompatActivity {
    private WebView youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_playlist);
        
        init();
        findViewById(R.id.youtubeIVback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        
        youtube = findViewById(R.id.youtube_webview);
        youtube.setWebViewClient(new MyBrowser2());
        youtube.getSettings().setLoadsImagesAutomatically(true);
        youtube.getSettings().setJavaScriptEnabled(true);
        youtube.loadUrl("https://www.youtube.com/channel/UCD_NolR9RK9FQ6kQtQ0hHcQ/featured");

    }

    private class MyBrowser2 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}