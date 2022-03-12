package com.kamaii.partner.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.kamaii.partner.Glob;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityCheckSignInBinding;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;


public class CheckSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private String TAG = SignInActivity.class.getSimpleName();
    ActivityCheckSignInBinding binding;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(CheckSignInActivity.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_sign_in);
        mContext = CheckSignInActivity.this;
        progressDialog = new ProgressDialog(CheckSignInActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        setUiAction();
    }

    public void setUiAction() {
        binding.CBsignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.CBsignIn:
                clickForSubmit();
                break;


        }
    }

    public void clickForSubmit() {
        if (!ProjectUtils.isPhoneNumberValid(binding.CETemailadd.getText().toString().trim())) {
            ProjectUtils.showToast(mContext, getResources().getString(R.string.val_mobile));

        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {

                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    public void login() {
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.checksignin(ProjectUtils.getEditTextValue(binding.CETemailadd), "1");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                //   Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {


                            sendotp();
                        } else if (sstatus == 2) {
                            Toast.makeText(CheckSignInActivity.this, message,
                                    LENGTH_LONG).show();
                        } else {
                            Intent in = new Intent(mContext, SignUpActivity.class);
                            in.putExtra(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.CETemailadd));
                            startActivity(in);

                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        }


                    } else {
                        Toast.makeText(CheckSignInActivity.this, "Try again. Server is not responding",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(CheckSignInActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Glob.BUBBLE_VALUE = "0";
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void sendotp() {
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.send_otp(ProjectUtils.getEditTextValue(binding.CETemailadd),"1");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("SENDOTP_RES",""+s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {

                            try {

                                String otp = object.getString("otp");

                                Intent intent = new Intent(CheckSignInActivity.this, OtpActivity.class);
                                intent.putExtra("signin_flag", true);
                                intent.putExtra(Consts.MOBILE, ProjectUtils.getEditTextValue(binding.CETemailadd));
                                intent.putExtra(Consts.OTP, otp);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(CheckSignInActivity.this, message,
                                    LENGTH_LONG).show();
                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CheckSignInActivity.this, "Try Again Later ",
                                LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CheckSignInActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();


            }
        });

    }

}