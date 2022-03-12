package com.kamaii.partner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BasicInfoActivity;
import com.kamaii.partner.ui.activity.DocumentUploadTwoActivity;
import com.kamaii.partner.ui.activity.PersonalDocumentation;
import com.kamaii.partner.ui.fragment.AppUpdateFragment;

public class Help extends AppCompatActivity {

    private WebView mWebView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        linearLayout = findViewById(R.id.llBack);
        mWebView = (WebView) findViewById(R.id.mWebView);
        mWebView.setWebViewClient(new MyBrowser2());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Consts.Help_URL);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Help.this, BasicInfoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

    }

    private class MyBrowser2 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}