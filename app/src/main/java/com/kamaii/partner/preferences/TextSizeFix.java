package com.kamaii.partner.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.interfacess.Consts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;


public class TextSizeFix {
    Context mContext;

    // constructor
    public TextSizeFix(Context context) {
        this.mContext = context;
    }

    public String getUserName() {
        return "test";
    }

    public static void adjustFontScale(Configuration configuration, float scale, Context context) {

        configuration.fontScale = scale;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        context.getResources().updateConfiguration(configuration, metrics);
    }

}