package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.preferences.SharedPrefrence;

public class LocationPermissionActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc, readstorage, wakelock, background;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);


        findViewById(R.id.btnlocationapprove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check()) {

                    startActivity(new Intent(LocationPermissionActivity.this, BasicInfoActivity.class));
                }
            }
        });

    }

    public boolean check() {
        if (checkPermission()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission() {

        //Check, API Version is Above 23 than open Permission dialog
        if (Build.VERSION.SDK_INT >= 23) {
            boolean isFinelocation = false, iscrose = false /*isbackground = false*/;
            //camera Permission


            //fine location Permission
            try {
                int hasFinelocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                if (hasFinelocationPermission != PackageManager.PERMISSION_GRANTED) {
                    isFinelocation = false;
                } else {
                    isFinelocation = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isFinelocation = true;
            }

            try {
                int hasReadStoragePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                if (hasReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    iscrose = false;
                } else {
                    iscrose = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                iscrose = true;
            }

           /* try {
                int hasBackgroundlocationPermission = checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                if (hasBackgroundlocationPermission != PackageManager.PERMISSION_GRANTED) {

                    isbackground = false;
                } else {
                    isbackground = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isbackground = true;
            }*/

            if (!isFinelocation || !iscrose /*|| !isbackground*/) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, /*Manifest.permission.ACCESS_BACKGROUND_LOCATION*/}, 007);
            }
            //Must be all permission true otherwise again check permission call
            if (isFinelocation && iscrose /*&& isbackground*/) {

                startActivity(new Intent(LocationPermissionActivity.this, CheckSignInActivity.class));

                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                    storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                    fineLoc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                    corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);


                    readstorage = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.READ_STORAGE, readstorage);


                    wakelock = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.WACK_LOCK, wakelock);

                   /* background = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.BACKGROUND, background);
*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        if (Build.VERSION.SDK_INT >= 29){

            check();
        }
        else {
            startActivity(new Intent(LocationPermissionActivity.this, CheckSignInActivity.class));
        }
    }

}