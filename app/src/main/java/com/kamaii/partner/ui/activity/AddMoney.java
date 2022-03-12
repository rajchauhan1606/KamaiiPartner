package com.kamaii.partner.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.partner.DTO.UserDTO;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

public class AddMoney extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private String TAG = AddMoney.class.getSimpleName();
    private Context mContext;
    private CustomEditText etAddMoney;
    private CustomTextView tv1000, tv1500, tv2000, llltxt;
    private LinearLayout cbAdd;
    int rs = 0;
    int final_rs = 0;
    private HashMap<String, String> parmas = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private String amt = "";
    private String currency = "";
    private String wallet_rate = "";
    private String razorpay_key = "";
    private CustomTextView tvWallet;
    private ImageView ivBack;
    private Dialog dialog;
    private LinearLayout llPaypall, llStripe, llCancel, llPayuMoney, llrozopay;
    Button btn_pay;
    int amount = 0;
    CustomTextViewBold llltxtamount;
    private String productName = "WalletPartner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        mContext = AddMoney.this;

        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmas.put(Consts.USER_ID, userDTO.getUser_id());
        llltxt = findViewById(R.id.llltxt);
        llltxtamount = findViewById(R.id.llltxtamount);
        setUiAction();
    }

    public void setUiAction() {
        tvWallet = findViewById(R.id.tvWallet);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent().hasExtra(Consts.AMOUNT)) {
            amt = getIntent().getStringExtra(Consts.AMOUNT);
            currency = getIntent().getStringExtra(Consts.CURRENCY);
            wallet_rate = getIntent().getStringExtra("wallet_rate");
            razorpay_key = getIntent().getStringExtra("razorpay_key");

            tvWallet.setText(currency + " " + amt);
        }

        cbAdd = findViewById(R.id.cbAdd);
        cbAdd.setOnClickListener(this);

        etAddMoney = findViewById(R.id.etAddMoney);
        etAddMoney.setSelection(etAddMoney.getText().length());

        tv1000 = findViewById(R.id.tv1000);
        tv1000.setOnClickListener(this);

        tv1500 = findViewById(R.id.tv1500);
        tv1500.setOnClickListener(this);

        tv2000 = findViewById(R.id.tv2000);
        tv2000.setOnClickListener(this);

        tv1000.setText("+ " + currency + " 1000");
        tv1500.setText("+ " + currency + " 1500");
        tv2000.setText("+ " + currency + " 2000");

        llltxtamount.setText("2%");
        llltxt.setText(" " + "convernience fees will be applied");
    }

    @Override
    public void onClick(View v) {
        if (etAddMoney.getText().toString().trim().equalsIgnoreCase("")) {


        } else {


        }

        switch (v.getId()) {
            case R.id.tv1000:
                rs = 1000;
                final_rs = rs;
                etAddMoney.setText(final_rs + "");
                etAddMoney.setSelection(etAddMoney.getText().length());
                break;
            case R.id.tv1500:
                rs = 1500;
                final_rs = rs;
                etAddMoney.setText(final_rs + "");
                etAddMoney.setSelection(etAddMoney.getText().length());
                break;
            case R.id.tv2000:
                rs = 2000;
                final_rs = rs;
                etAddMoney.setText(final_rs + "");
                etAddMoney.setSelection(etAddMoney.getText().length());
                break;
            case R.id.cbAdd:
                if (etAddMoney.getText().toString().length() > 0 && Float.parseFloat(etAddMoney.getText().toString().trim()) > 0) {
                    if (NetworkManager.isConnectToInternet(mContext)) {
                        parmas.put(Consts.AMOUNT, ProjectUtils.getEditTextValue(etAddMoney));
                        double extraadd = (Double.parseDouble(ProjectUtils.getEditTextValue(etAddMoney)) * Double.parseDouble(wallet_rate)) / 100;
                        double total = Double.parseDouble(ProjectUtils.getEditTextValue(etAddMoney)) + extraadd;

                        DecimalFormat newFormat = new DecimalFormat("####");
                        float mainprice = Float.valueOf(newFormat.format(total));

                        Checkout checkout = new Checkout();
                        // with testing_key  checkout.setKeyID("rzp_test_cN1FC6C2mOYSwF");
                       // checkout.setKeyID("rzp_live_WxwhNFQFzmlsOU");
                        checkout.setKeyID(razorpay_key);
                        checkout.setImage(R.drawable.logo);
                        final Activity activity = this;
                        try {
                            JSONObject options = new JSONObject();

                            options.put("name", "Kamaii Services Pvt Ltd");

                            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                         //   options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                            options.put("theme.color", "#3399cc");
                            options.put("currency", "INR");
                            options.put("amount", String.valueOf(mainprice * 100));//pass amount in currency subunits
                            options.put("prefill.email", userDTO.getEmail_id());
                            options.put("prefill.contact", userDTO.getMobile());
                            JSONObject retryObj = new JSONObject();
                            retryObj.put("enabled", true);
                            retryObj.put("max_count", 4);
                            options.put("retry", retryObj);

                            checkout.open(activity, options);

                        } catch (Exception e) {
                            Log.e(TAG, "Error in starting Razorpay Checkout", e);
                        }


                    } else {
                        ProjectUtils.showLong(mContext, getResources().getString(R.string.internet_concation));
                    }
                } else {
                    ProjectUtils.showLong(mContext, getResources().getString(R.string.val_money));
                }
                break;
        }
    }


    public void addMoney() {
        new HttpsRequest(Consts.ADD_MONEY_API, parmas, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(mContext, msg);
                    finish();
                } else {
                    ProjectUtils.showLong(mContext, msg);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPaymentSuccess(String s) {
        addMoney();
        Log.e("RAZORPAY", " SUCCESS " + s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("RAZORPAY", " FAILURE " + s);

    }
}
