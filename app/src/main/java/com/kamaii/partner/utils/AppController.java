package com.kamaii.partner.utils;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppController extends Application {
    Environment environment;
    private static AppController instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        environment = Environment.TEST;
    }
    public static AppController getInstance()
    {
        return instance;
    }
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
