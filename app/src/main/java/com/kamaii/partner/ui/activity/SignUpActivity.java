package com.kamaii.partner.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private CustomEditText CETfirstname, CETemailadd, CETenterpassword, edtmono;
    CustomTextViewBold CTVsignin,btn_sign_up;
    private String TAG = SignUpActivity.class.getSimpleName();
    private RelativeLayout RRsncbar;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private CustomTextViewBold tvTerms, tvPrivacy;
    private CheckBox termsCB;
    private ImageView ivEnterShow, ivReEnterShow;
    private boolean isHide = false;
    String mobileno="";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SignUpActivity.this);
        setContentView(R.layout.activity_sign_up);
        mContext = SignUpActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        //Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));

        progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        setUiAction();
    }

    public void setUiAction() {
        RRsncbar = findViewById(R.id.RRsncbar);
        termsCB = (CheckBox) findViewById(R.id.termsCB);
        CETfirstname = findViewById(R.id.CETfirstname);
        CETemailadd = findViewById(R.id.CETemailadd);
        CETenterpassword = findViewById(R.id.CETenterpassword);
        edtmono = findViewById(R.id.edtmono);

        btn_sign_up = findViewById(R.id.CBsignUp);
        CTVsignin = findViewById(R.id.CTVsignin);
        tvTerms = findViewById(R.id.tvTerms);
        tvPrivacy = findViewById(R.id.tvPrivacy);

        btn_sign_up.setOnClickListener(this);
        CTVsignin.setOnClickListener(this);
        tvTerms.setOnClickListener(this);
        tvPrivacy.setOnClickListener(this);
        if (getIntent() != null) {
            mobileno = getIntent().getStringExtra(Consts.MOBILE);
            edtmono.setText(mobileno);

        }
      //  ivEnterShow = findViewById(R.id.ivEnterShow);
      //  ivReEnterShow = findViewById(R.id.ivReEnterShow);
       // ivReEnterShow.setOnClickListener(this);
       // ivEnterShow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CBsignUp:
                clickForSubmit();
                break;
            case R.id.CTVsignin:
                startActivity(new Intent(mContext, SignInActivity.class));
                finish();
                break;
            case R.id.tvTerms:
                startActivity(new Intent(mContext, Terms.class));
                break;
            case R.id.tvPrivacy:
                startActivity(new Intent(mContext, Privacy.class));
                break;
        }
    }

    public void sendotp() {


        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.send_otp(ProjectUtils.getEditTextValue(edtmono),"1");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {

                       // Toast.makeText(mContext, "response get", Toast.LENGTH_SHORT).show();

                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("register123otp_response",""+s);

                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        String is_varified = object.getString("is_verified");
                        if (sstatus==1)
                        {

                            try {
                                //Toast.makeText(mContext, "response get2", Toast.LENGTH_SHORT).show();

                             String otp=object.getString("otp");

                                Intent intent=new Intent(SignUpActivity.this, OtpActivity.class);
                                intent.putExtra(Consts.NAME, ProjectUtils.getEditTextValue(CETfirstname));
                                intent.putExtra(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(CETemailadd));
                               // intent.putExtra(Consts.PASSWORD, ProjectUtils.getEditTextValue(CETenterpassword));
                                intent.putExtra(Consts.REFERRAL_CODE, "");
                                intent.putExtra(Consts.IS_VARIFIED, is_varified);
                                intent.putExtra(Consts.MOBILE, ProjectUtils.getEditTextValue(edtmono));
                                intent.putExtra(Consts.OTP,otp);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }else {
                            //Toast.makeText(mContext, "response get3", Toast.LENGTH_SHORT).show();

                            Toast.makeText(SignUpActivity.this, message,
                                    LENGTH_LONG).show();
                        }


                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Try Again Later ",
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
                Toast.makeText(SignUpActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();
            }
        });

    }
    public void clickForSubmit() {

        if (!ProjectUtils.isPhoneNumberValid(edtmono.getText().toString().trim())) {
            Toast.makeText(SignUpActivity.this,getResources().getString(R.string.val_mobile), Toast.LENGTH_SHORT).show();

        }
        else if (!validation(CETfirstname, getResources().getString(R.string.val_name))) {
            return;
        } else if (!ProjectUtils.isEmailValid(CETemailadd.getText().toString().trim())) {
            Toast.makeText(SignUpActivity.this,getResources().getString(R.string.val_email), Toast.LENGTH_SHORT).show();

        }  else if (!validateTerms()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {


                sendotp();
            } else {
                Toast.makeText(SignUpActivity.this,getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

               // ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    private boolean validateTerms() {
        if (termsCB.isChecked()) {
            return true;
        } else {
            Toast.makeText(SignUpActivity.this,getResources().getString(R.string.trms_acc), Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private boolean checkpass() {
        if (CETenterpassword.getText().toString().trim().equals("")) {
            Toast.makeText(SignUpActivity.this,getResources().getString(R.string.val_pass), Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(CETfirstname));
        parms.put(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(CETemailadd));
       // parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(CETenterpassword));
        parms.put(Consts.REFERRAL_CODE, "");
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(edtmono));
        parms.put(Consts.ROLE, "1");
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        Log.e(TAG, parms.toString());
        return parms;
    }


    public HashMap<String, String> getparmtwo() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(edtmono));
        Log.e(TAG, parms.toString());
        return parms;
    }



    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {

            Toast.makeText(SignUpActivity.this,msg, Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        finish();
    }
}
