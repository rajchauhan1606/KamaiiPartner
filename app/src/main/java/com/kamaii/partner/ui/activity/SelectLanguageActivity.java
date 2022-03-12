package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.Locale;

public class SelectLanguageActivity extends AppCompatActivity implements View.OnClickListener {
    boolean lang_selected;
    Context context;
    Resources resources;
    CustomTextViewBold customTextViewBold;
    String def_sel = "eg";
    CustomTextViewBold eng, guj, hindi;
    boolean pathway = false;
    String lang = "";
    private SharedPrefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        prefrence = SharedPrefrence.getInstance(SelectLanguageActivity.this);
        eng = findViewById(R.id.eng);
        guj = findViewById(R.id.guj);
        hindi = findViewById(R.id.hindi);
        pathway = getIntent().getBooleanExtra("pathway", false);

        //  lang = getIntent().getStringExtra("lang");
        findViewById(R.id.english_card).setOnClickListener(this);
        findViewById(R.id.gujarati_card).setOnClickListener(this);
        findViewById(R.id.hindi_card).setOnClickListener(this);
        //bgcoloring();
        findViewById(R.id.btnnext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language(def_sel);
            }
        });
    }

    private void bgcoloring() {
        switch (lang) {
            case "eg":
                findViewById(R.id.english_card).setBackground(getResources().getDrawable(R.drawable.selected_drawable));
                findViewById(R.id.fimg).setVisibility(View.VISIBLE);
                eng.setTextColor(Color.WHITE);
                break;
            case "gu":
                findViewById(R.id.gujarati_card).setBackground(getResources().getDrawable(R.drawable.selected_drawable));
                findViewById(R.id.simg).setVisibility(View.VISIBLE);
                guj.setTextColor(Color.WHITE);
                break;
            case "hi":
                findViewById(R.id.hindi_card).setBackground(getResources().getDrawable(R.drawable.selected_drawable));
                findViewById(R.id.timg).setVisibility(View.VISIBLE);
                hindi.setTextColor(Color.WHITE);
                break;
        }
    }

    public void language(String language) {
        String languageToLoad = language; // your language
        prefrence.setValue(Consts.LANGUAGE, language);
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        SelectLanguageActivity.this.getResources().updateConfiguration(config,
                SelectLanguageActivity.this.getResources().getDisplayMetrics());

        dorediraction();
    }

    private void dorediraction() {
        if (pathway) {
            Log.e("abcfg","3");

            Intent in = new Intent(this, BaseActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_left);
        } else {
            Log.e("abcfg","4");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              //  if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("abcfg","1");
                    startActivity(new Intent(SelectLanguageActivity.this, LocationPermissionActivity.class));
                } else {
                    Log.e("abcfg","2");
                    startActivity(new Intent(SelectLanguageActivity.this, CheckSignInActivity.class));
                }
            }
            else {
                Log.e("abcfg","2");
                startActivity(new Intent(SelectLanguageActivity.this, CheckSignInActivity.class));
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left,
                        R.anim.anim_slide_out_left);

            }


        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        findViewById(R.id.english_card).setBackground(getResources().getDrawable(R.drawable.unselected_drawable));
        findViewById(R.id.gujarati_card).setBackground(getResources().getDrawable(R.drawable.unselected_drawable));
        findViewById(R.id.hindi_card).setBackground(getResources().getDrawable(R.drawable.unselected_drawable));
        findViewById(R.id.fimg).setVisibility(View.GONE);
        findViewById(R.id.simg).setVisibility(View.GONE);
        findViewById(R.id.timg).setVisibility(View.GONE);
        eng.setTextColor(Color.BLACK);
        guj.setTextColor(Color.BLACK);
        hindi.setTextColor(Color.BLACK);

        switch (v.getId()) {

            case R.id.english_card:
                def_sel = "eg";
                findViewById(R.id.english_card).setBackground(getResources().getDrawable(R.drawable.selected_drawable));
                findViewById(R.id.fimg).setVisibility(View.VISIBLE);
                eng.setTextColor(Color.WHITE);
                break;

            case R.id.gujarati_card:
                def_sel = "gu";
                findViewById(R.id.gujarati_card).setBackground(getResources().getDrawable(R.drawable.selected_drawable));
                findViewById(R.id.simg).setVisibility(View.VISIBLE);
                guj.setTextColor(Color.WHITE);
                break;

            case R.id.hindi_card:
                def_sel = "hi";
                findViewById(R.id.hindi_card).setBackground(getResources().getDrawable(R.drawable.selected_drawable));
                findViewById(R.id.timg).setVisibility(View.VISIBLE);
                hindi.setTextColor(Color.WHITE);
                break;
        }
    }
}