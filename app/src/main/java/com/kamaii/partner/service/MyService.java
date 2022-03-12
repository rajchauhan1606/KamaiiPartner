package com.kamaii.partner.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kamaii.partner.interfacess.Consts;

import java.io.IOException;

public class MyService extends Service {

    private static final String TAG = "MyService";
    boolean isPLAYING = false;

    boolean not_decline = false;
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        Log.e("shivakashi", "1");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("shivakashi", "2");
        // Toast.makeText(this, "MyService Started.", Toast.LENGTH_SHORT).show();

        if (intent.hasExtra("not_decline")){

            not_decline = intent.getBooleanExtra("not_decline", false);
        }
        if (not_decline) {
            Log.e("shivakashi", "3");
        } else {
            Log.e("shivakashi", "4");
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor descriptor = null;

            if (!isPLAYING) {
                isPLAYING = true;
                if (!mediaPlayer.isPlaying()) {
                    Log.e("shivakashi", "5");
                    try {
                        Log.e("shivakashi", "6");
                        descriptor = this.getAssets().openFd("accept.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                        Log.e("shivakashi", "7");
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                try {
                                    Log.e("shivakashi", "8");
                                    mediaPlayer.start();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("shivakashi", "9");
                    mediaPlayer.stop();
                }
            }
            else {
                isPLAYING = false;
                mediaPlayer.stop();
            }
        }
        //If service is killed while starting, it restarts.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            mediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

}