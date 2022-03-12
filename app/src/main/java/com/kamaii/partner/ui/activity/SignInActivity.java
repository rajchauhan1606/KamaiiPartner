package com.kamaii.partner.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.Glob;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.utils.CustomButton;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private CustomEditText CETemailadd, CETenterpassword;
    private CustomButton CBsignIn;
    private CustomTextView CTVBforgot;
    CustomTextView CTVsignup;
    private String TAG = SignInActivity.class.getSimpleName();
    private RelativeLayout RRsncbar;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private ImageView ivEnterShow;
    private boolean isHide = false;
    EditText edt_mono;
    private HashMap<String, String> parms = new HashMap<>();

    String mobileno="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(SignInActivity.this);
        setContentView(R.layout.activity_sign_in);
        mContext = SignInActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));
        progressDialog=new ProgressDialog(SignInActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        setUiAction();
    }

    public void setUiAction() {
        RRsncbar = findViewById(R.id.RRsncbar);
        CETemailadd = findViewById(R.id.CETemailadd);
        CETenterpassword = findViewById(R.id.CETenterpassword);
        CBsignIn = findViewById(R.id.CBsignIn);
        CTVsignup = findViewById(R.id.CTVsignup);
        CTVBforgot = findViewById(R.id.CTVBforgot);

        CBsignIn.setOnClickListener(this);
        CTVBforgot.setOnClickListener(this);
        CTVsignup.setOnClickListener(this);
        if (getIntent() != null) {
            mobileno = getIntent().getStringExtra(Consts.MOBILE);
            CETemailadd.setText(mobileno);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.CTVBforgot:
               // startActivity(new Intent(mContext, ForgotPass.class));

                final Dialog dialog = new Dialog(SignInActivity.this);
                dialog.setContentView(R.layout.custom_dialog_forgot);

                 edt_mono =  dialog.findViewById(R.id.edt_mono);



                TextView txt_cancel =  dialog.findViewById(R.id.txt_cancel);

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                TextView txt_continue =  dialog.findViewById(R.id.txt_continue);

                txt_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     //   submitForm();

                        if (!ValidateMobile()) {
                            return;
                        } else {
                            if (NetworkManager.isConnectToInternet(mContext)) {
                                parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(edt_mono));
                                ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
                                new HttpsRequest(Consts.FORGET_PASSWORD_API, parms, mContext).stringPost(TAG, new Helper() {
                                    @Override
                                    public void backResponse(boolean flag, String msg, JSONObject response) {
                                        ProjectUtils.pauseProgressDialog();
                                        if (flag) {
                                            Toast.makeText(SignInActivity.this,msg, Toast.LENGTH_SHORT).show();

                                           // ProjectUtils.showToast(mContext, msg);
                                            dialog.dismiss();

                                        } else {
                                            Toast.makeText(SignInActivity.this,msg, Toast.LENGTH_SHORT).show();

                                            //ProjectUtils.showToast(mContext, msg);
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(SignInActivity.this,getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

                             //   ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                            }
                        }
                    }
                });

                dialog.show();
                break;
            case R.id.CBsignIn:
                clickForSubmit();
                break;
            case R.id.CTVsignup:
                startActivity(new Intent(mContext, SignUpActivity.class));
                break;

        }
    }



    public boolean ValidateMobile() {
        if (!ProjectUtils.isPhoneNumberValid(edt_mono.getText().toString().trim())) {
            Toast.makeText(SignInActivity.this,getResources().getString(R.string.val_mobile), Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }




    public void login() {
        progressDialog.show();
        new HttpsRequest(Consts.LOGIN_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                       // ProjectUtils.showToast(mContext, msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        com.kamaii.partner.Glob.BUBBLE_VALUE ="0";
                        Intent in = new Intent(mContext, BaseActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(SignInActivity.this,msg, Toast.LENGTH_SHORT).show();

                    //ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    public void clickForSubmit() {
        if (!ProjectUtils.isPhoneNumberValid(CETemailadd.getText().toString().trim())) {
            Toast.makeText(SignInActivity.this,getResources().getString(R.string.val_mobile), Toast.LENGTH_SHORT).show();

        } else if (!ProjectUtils.isPasswordValid(CETenterpassword.getText().toString().trim())) {

            Toast.makeText(SignInActivity.this,getResources().getString(R.string.val_pass), Toast.LENGTH_SHORT).show();

        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                login();
            } else {
                Toast.makeText(SignInActivity.this,getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

               // ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }


    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(CETemailadd));
        parms.put(Consts.PASSWORD, ProjectUtils.getEditTextValue(CETenterpassword));
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.ROLE, "1");
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


}
