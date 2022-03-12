package com.kamaii.partner.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;


public class AppService extends JobService {
    HashMap<String, String> parms = new HashMap<>();
    private UserDTO userDTO;
    private String TAG = BaseActivity.class.getSimpleName();
    private ArrayList<ArtistBooking> artistBookingsList=new ArrayList<>();
    private SharedPrefrence prefrence;
    boolean isWorking = false;
    boolean jobCancelled = false;
    Context context;


    @Override
    public void onCreate() {


       // Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context=getApplicationContext();
        return START_STICKY;
    }





    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started!");
        isWorking = true;
        // We need 'jobParameters' so we can call 'jobFinished'
        startWorkOnNewThread(params); // Services do NOT run on a separate thread

        return isWorking;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before being completed.");
        jobCancelled=true;
        boolean needsReschedule = isWorking;
        jobFinished(params, needsReschedule);
        return needsReschedule;
    }


    private void startWorkOnNewThread(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            public void run() {
                doWork(jobParameters);
            }
        }).start();
    }
    private void doWork(JobParameters jobParameters)
    {
        if (jobCancelled)
            return;
        prefrence = SharedPrefrence.getInstance(getApplicationContext());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        isWorking = false;
        Log.d(TAG, "Job finished!");
        boolean needsReschedule = false;
        jobFinished(jobParameters, needsReschedule);

    }

    @Override
    public void onStart(Intent intent, int startId)
    {

        super.onStart(intent, startId);
    }
    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        stopSelf();
        super.onDestroy();
    }
}