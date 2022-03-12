package com.kamaii.partner.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityOtpBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class OtpActivity extends AppCompatActivity implements LocationListener {

    private Context mContext;
    private String TAG = OtpActivity.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private SharedPreferences firebase;
    private boolean isHide = false;
    private ActivityOtpBinding binding;
    ImageView img_back;
    ImageView btn_signIn;
    String mobile = "", mainotp = "", verificationCode = "", name = "", email = "", pasword = "", is_varified = "", signin_varified = "";
    ProgressDialog progressDialog;
    private RelativeLayout RRsncbar;
    CustomEditText otp_receieved;
    TextInputEditText edtotp1, edtotp2, edtotp3, edtotp4, edtotp5, etotp0;
    TextView txt_timer, txt_resend_timer;
    boolean flag = false;
    CustomTextViewBold txt_resend, otp_txt;
    public int counter;
    Double latitude;
    Double longitude;
    private Location mylocation;
    String partner_location = "";
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private FusedLocationProviderClient mFusedLocationClient;
    String device_token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectUtils.Fullscreen(OtpActivity.this);
        setContentView(R.layout.activity_otp);

        mContext = OtpActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e("tokensss", firebase.getString(Consts.DEVICE_TOKEN, ""));

        RRsncbar = findViewById(R.id.RRsncbarr);
        progressDialog = new ProgressDialog(OtpActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        otp_txt = findViewById(R.id.otp_txt);
        txt_resend_timer = findViewById(R.id.txt_resend_timer);
        flag = getIntent().getBooleanExtra("signin_flag", false);
        if (flag) {
            mobile = getIntent().getStringExtra(Consts.MOBILE);
            mainotp = getIntent().getStringExtra(Consts.OTP);

            if (mainotp.equalsIgnoreCase("123456")){
                prefrence.setBooleanValue("testing_account_otp",true);
            }
        } else {
            name = getIntent().getStringExtra(Consts.NAME);
            email = getIntent().getStringExtra(Consts.EMAIL_ID);
            //pasword=getIntent().getStringExtra(Consts.PASSWORD);
            mobile = getIntent().getStringExtra(Consts.MOBILE);
            mainotp = getIntent().getStringExtra(Consts.OTP);
            is_varified = getIntent().getStringExtra(Consts.IS_VARIFIED);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btn_signIn = findViewById(R.id.CBsignIn);
        otp_receieved = findViewById(R.id.otp_receieved);
        txt_timer = findViewById(R.id.txt_timer);
        txt_resend = findViewById(R.id.txt_resend);

        otp_txt.setText(mobile);
        otp_receieved.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (otp_receieved.getText().toString().length() == 6)     //size as per your requirement
                {
                    otp_receieved.requestFocus();
                    btn_signIn.performClick();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));

                Geocoder geocoder = new Geocoder(OtpActivity.this, Locale.getDefault());
                try {
                    // Toast.makeText(mContext, "8", Toast.LENGTH_LONG).show();

                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();

                    partner_location = obj.getAddressLine(0);
                    //   Log.e("IGA123214", "Address" + add);
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();

                    // TennisAppActivity.showDialog(add);

                    //  etLocationD.setText(obj.getAddressLine(0));
                    //  drag_location_etx.setText(obj.getAddressLine(0));
                    // parms.put(Consts.LOCATION, obj.getAddressLine(0));


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp_receieved.getText().toString().trim().isEmpty()) {


                    showSickbar("Please enter valid otp");

                    //  ProjectUtils.showToast(mContext, "Please enter valid otp");
                    setfocus();
                    return;
                }
                String otp = otp_receieved.getText().toString().trim();
                if (!otp.trim().isEmpty()) {
                    if (mainotp.equalsIgnoreCase(otp)) {

                        if (flag) {

                            login();
                            // Toast.makeText(mContext, "login called", Toast.LENGTH_SHORT).show();
                        } else {
                            register();
                        }
                    } else {
                        Toast.makeText(OtpActivity.this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showSickbar("Please enter valid otp");
                    //  ProjectUtils.showToast(mContext, "Please enter otp");
                    setfocus();
                }

            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                sendotp();
                txt_resend_timer.setVisibility(View.VISIBLE);
                txt_resend.setClickable(false);
                txt_resend.setTextColor(getResources().getColor(R.color.otp_resend_color));
                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt_resend_timer.setText(String.valueOf(counter));
                        counter++;
                    }

                    @Override
                    public void onFinish() {
                        counter = 0;
                        txt_resend_timer.setVisibility(View.GONE);
                        txt_resend.setTextColor(getResources().getColor(R.color.colorAccent));
                        txt_resend.setClickable(true);
                    }
                }.start();
            }
        });

    }

    public void sendotp() {


        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.send_otp(mobile, "1");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                String otp = object.getString("otp");
                                mainotp = otp;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(OtpActivity.this, message,
                                    LENGTH_LONG).show();
                        }


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(OtpActivity.this, "Try Again Later ",
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
                Toast.makeText(OtpActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();


            }
        });


    }

    public HashMap<String, String> getResendparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.MOBILE, mobile);
        Log.e(TAG, parms.toString());
        return parms;
    }


    public void register() {

        Log.e(TAG + "register123_response", " register called ");

        progressDialog.show();
        new HttpsRequest(Consts.REGISTER_API, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  ProjectUtils.showToast(mContext, msg);
                progressDialog.dismiss();
                if (flag) {
                    try {

                        Log.e(TAG + "register123_response", response.toString());

                        //  ProjectUtils.showToast(mContext, msg);
                        //   verify(user_id);
                        // overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);


                        Intent in = new Intent(mContext, BasicInfoActivity.class);
                        startActivity(in);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(mContext, msg);

                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);

                }


            }
        });
    }

    public void showSickbar(String msg) {
        Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();

    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.NAME, name);
        parms.put(Consts.EMAIL_ID, email);
        //  parms.put(Consts.PASSWORD, pasword);
        parms.put(Consts.REFERRAL_CODE, "");
        parms.put(Consts.MOBILE, mobile);
        parms.put(Consts.ROLE, "1");
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, firebase.getString(Consts.DEVICE_TOKEN, ""));
        parms.put(Consts.DEVICE_ID, "12345");
        Log.e(TAG + "register123", parms.toString());
        return parms;
    }


    public HashMap<String, String> getparmotp(String userid) {

        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", userid);
        parms.put("mobile", mobile);


        return parms;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        finish();
    }

    public void setfocus() {

        if (otp_receieved.getText().toString().trim().isEmpty()) {
            otp_receieved.requestFocus();
            return;
        }
    }

    public HashMap<String, String> getloginparm() {

        if (mainotp.equalsIgnoreCase("123456")){
            device_token = "";
        }else {
            device_token =  firebase.getString(Consts.DEVICE_TOKEN, "");
        }


        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.MOBILE, mobile);
        parms.put("otp", mainotp);
        parms.put(Consts.DEVICE_TYPE, "ANDROID");
        parms.put(Consts.DEVICE_TOKEN, device_token);
        parms.put(Consts.DEVICE_ID, "12345");
        parms.put(Consts.ROLE, "1");
        parms.put(Consts.ADDRESS, partner_location);
        parms.put(Consts.LATITUDE, String.valueOf(latitude));
        parms.put(Consts.LONGITUDE, String.valueOf(longitude));
        Log.e(TAG + " Login", parms.toString());
        return parms;
    }

    public void login() {
        progressDialog.show();
        new HttpsRequest(Consts.LOGIN_API, getloginparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                Log.e(TAG + " Login_response_flag", response.toString());

                if (flag) {
                    try {
                        Log.e(TAG + " Login_response", response.toString());

                        // ProjectUtils.showToast(mContext, msg);
                        if (mainotp.equalsIgnoreCase("123456")){
                            Log.e("testing_account_otp" , "testing");

                            prefrence.setBooleanValue("testing_account_otp",true);
                        }
                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);

                        String is_v = userDTO.getIs_verified();
                        prefrence.setBooleanValue(Consts.IS_REGISTERED, true);
                        com.kamaii.partner.Glob.BUBBLE_VALUE = "0";

                        if (is_v.equalsIgnoreCase("0")) {
                            Intent in = new Intent(mContext, BasicInfoActivity.class);
                            in.putExtra("from_splash_status", "1");
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        } else {
                            Intent in = new Intent(mContext, BaseActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_left);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();

                    //ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(mContext, "location called", Toast.LENGTH_SHORT).show();

        mylocation = location;
        // Toast.makeText(mContext, "1", Toast.LENGTH_LONG).show();

        if (mylocation != null) {
            //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

            latitude = mylocation.getLatitude();
            //  Log.e("lat", "" + latitude.toString());
            longitude = mylocation.getLongitude();
            //   Log.e("lang", "" + longitude.toString());


        }
    }

}
