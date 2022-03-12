package com.kamaii.partner;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.model.TvideoModel;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BasicInfoActivity;
import com.kamaii.partner.ui.activity.CheckSignInActivity;
import com.kamaii.partner.ui.activity.SelectLanguageActivity;
import com.kamaii.partner.ui.activity.TrainingActivity;
import com.kamaii.partner.ui.fragment.TVideoFragment;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SplashActivity extends AppCompatActivity {

    private SharedPrefrence prefference;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK};
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc, readstorage, wakelock, background;
    private Handler handler = new Handler();
    private static int SPLASH_TIME_OUT = 3000;
    Context mContext;
    MediaPlayer mediaPlayer;


    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = SplashActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        prefference = SharedPrefrence.getInstance(SplashActivity.this);
        //check();
        language(prefference.getValue(Consts.LANGUAGE));
        String str = Build.BRAND;
        Log.e("STR", " 3 " + str);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    scheduleJob(SplashActivity.this,BACKGROUND_SERVICE_JOB_ID);
                } else {
                    startService(new Intent(SplashActivity.this, AppService.class));
                }
*/
                    check_state();

                } else {
                    startActivity(new Intent(SplashActivity.this, SelectLanguageActivity.class));
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);

                }
            }
        }, 3000);




        /*Runnable mTask = new Runnable() {
            @Override
            public void run() {

                mediaPlayer = new MediaPlayer();
                AssetFileDescriptor descriptor = null;
            *//*try {
                descriptor = getAssets().openFd("start.wav");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mediaPlayer.prepare();
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }*//*


                if (prefference.getBooleanValue(Consts.IS_REGISTERED)) {

                *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    scheduleJob(SplashActivity.this,BACKGROUND_SERVICE_JOB_ID);
                } else {
                    startService(new Intent(SplashActivity.this, AppService.class));
                }
*//*
                    check_state();

                } else {
                    startActivity(new Intent(SplashActivity.this, SelectLanguageActivity.class));
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);

                }


            }

        };*/

    }




    /*public boolean checkPermission() {

        //Check, API Version is Above 23 than open Permission dialog
        if (Build.VERSION.SDK_INT >= 23) {
            boolean isFinelocation = false, iscrose = false, isbackground = false;
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

            try {
                int hasBackgroundlocationPermission = checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
                if (hasBackgroundlocationPermission != PackageManager.PERMISSION_GRANTED) {
                    isbackground = false;
                } else {
                    isbackground = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isbackground = true;
            }

            if (!isFinelocation || !iscrose || !isbackground) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 007);
            }
            //Must be all permission true otherwise again check permission call
            if (isFinelocation && iscrose && isbackground) {
                return true;
            } else {
                return false;
            }



        } else {
            return true;
        }
    }*/


    /*public boolean check() {
        if (checkPermission()) {
            handler.postDelayed(mTask, SPLASH_TIME_OUT);
            return true;
        } else {
            return false;
        }
    }*/

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void language(String language) {
        String languageToLoad = language; // your language

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        SplashActivity.this.getResources().updateConfiguration(config,
                SplashActivity.this.getResources().getDisplayMetrics());

    }

    public void check_state() {

        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put("artist_id", userDTO.getUser_id());

        Retrofit retrofit = apiClient.getClient();
        apiRest apiRest = retrofit.create(apiRest.class);
        Call<ResponseBody> call = apiRest.checkApprove(userDTO.getUser_id());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {

                        ResponseBody responseBody = response.body();
                        String s = responseBody.string();

                        Log.e("check approve res",""+s);
                        JSONObject jsonObject = new JSONObject(s);
                        int status = jsonObject.getInt("status");
                        Log.e("STATUS_ERROR", "" + status);


                        if (status == 0) {

                            Intent in = new Intent(SplashActivity.this, BasicInfoActivity.class);
                            in.putExtra("from_splash_status", "0");
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        } else if (status == 1) {

                            Intent in = new Intent(SplashActivity.this, BasicInfoActivity.class);
                            in.putExtra("from_splash_status", "1");
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        } else if (status == 2) {

                            Intent in = new Intent(SplashActivity.this, BaseActivity.class);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        } else if (status == 3) {

                            Intent in = new Intent(SplashActivity.this, CheckSignInActivity.class);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        }

                    } else {
                        Log.e("STATUS_ERROR", "" + response.errorBody().string());

                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
      /*  new HttpsRequest(Consts.CHECK_APPROVE, parms, SplashActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                //   ProjectUtils.pauseProgressDialog();
                //binding.swipeRefreshLayout.setRefreshing(false);
                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                if (flag) {
                    Intent in = new Intent(SplashActivity.this, BaseActivity.class);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                } else {
                    Intent in = new Intent(SplashActivity.this, BasicInfoActivity.class);
                    in.putExtra("from_splash", true);
                    startActivity(in);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                }
            }
        });
*/

    }
}


