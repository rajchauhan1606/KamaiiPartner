package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.DTO.WalletHistory;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.AddMoney;
import com.kamaii.partner.ui.activity.BankDocument;
import com.kamaii.partner.ui.activity.BankWalletActivity;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BasicInfoActivity;
import com.kamaii.partner.ui.activity.DocumentUploadTwoActivity;
import com.kamaii.partner.ui.adapter.AdapterWalletHistory;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Wallet extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private LinearLayout llAddMoney;
    private CustomTextView tvAll, tvDebit, tvCredit;
    private CustomTextView tvAllSelect, tvDebitSelect, tvCreditSelect;
    private AdapterWalletHistory adapterWalletHistory;
    private ArrayList<WalletHistory> walletHistoryList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = Wallet.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo,btnUpdate;
    private String status = "";
    HashMap<String, String> parms;
    HashMap<String, String> parmsGetWallet = new HashMap<>();
    private CustomTextViewBold tvAdd,tvWallet;
    private String amt = "";
    private String currency = "";
    private BaseActivity baseActivity;
    LinearLayout btncashout;
    private Dialog dialog;
    CustomTextViewBold etamount;
    CustomTextView tvCancel;
    private HashMap<String, String> paramsRequest = new HashMap<>();
    String bank_details_flag = "";
    String cashout_btn_flag = "";
    String razorpay_key = "";
    String check_bank_tracker = "";
    String document_details_flag = "";
    String wallet_rate = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.ivLogo.setVisibility(View.GONE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.ic_wallet));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.editImage.setVisibility(View.GONE);
        parmsGetWallet.put(Consts.USER_ID, userDTO.getUser_id());

        parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());
        setUiAction(view);
        Log.e("getwallet","1");
        return view;
    }

    public void setUiAction(View v) {
        tvWallet = v.findViewById(R.id.tvWallet);

        tvAll = v.findViewById(R.id.tvAll);
        tvAll.setOnClickListener(this);

        tvDebit = v.findViewById(R.id.tvDebit);
        tvDebit.setOnClickListener(this);

        tvCredit = v.findViewById(R.id.tvCredit);
        tvCredit.setOnClickListener(this);

        tvAllSelect = v.findViewById(R.id.tvAllSelect);
        tvDebitSelect = v.findViewById(R.id.tvDebitSelect);
        tvCreditSelect = v.findViewById(R.id.tvCreditSelect);

        llAddMoney = v.findViewById(R.id.llAddMoney);
        llAddMoney.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        tvNo = v.findViewById(R.id.tvNo);
        btnUpdate = v.findViewById(R.id.btnUpdate);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);


        btncashout = v.findViewById(R.id.btncashout);

        dialog = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dailog_cashout);
        etamount=dialog.findViewById(R.id.etamount);
        tvCancel=dialog.findViewById(R.id.tvCancel);
        tvAdd=dialog.findViewById(R.id.tvAdd);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btncashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (document_details_flag.equalsIgnoreCase("0")){

                    Intent intent = new Intent(getActivity(), BasicInfoActivity.class);
                    intent.putExtra("from_wallet",true);
                    startActivity(intent);
                }else*/ if(check_bank_tracker.equalsIgnoreCase("0")){
                    Intent intent = new Intent(getActivity(), BankWalletActivity.class);
                    intent.putExtra("from_wallet",true);
                    startActivity(intent);
                }
                else {

                    if (cashout_btn_flag.equalsIgnoreCase("0")){

                        Toast.makeText(getActivity(), "Your request is already pending", Toast.LENGTH_SHORT).show();
                    }else {
                        dialog.show();
                    }
                }
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(etamount.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Enter Amount ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Double.parseDouble(etamount.getText().toString())<50){
                    Toast.makeText(getActivity(),"Amount Should Be Grater Than 50", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Double.parseDouble(etamount.getText().toString())> Double.parseDouble(amt)){
                    Toast.makeText(getActivity(),"Amount should be less than wallet amount", Toast.LENGTH_SHORT).show();
                    return;
                }


                requestPayment();
                //bookDailog();
            }
        });
    }
    public void requestPayment() {
        paramsRequest.put(Consts.AMOUNT, etamount.getText().toString());
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.WALLET_REQUEST_API, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    btncashout.setEnabled(false);
                    btncashout.setClickable(false);
                    btnUpdate.setBackground(getActivity().getDrawable(R.drawable.disable_btn));
                    //Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                  //  ProjectUtils.showLong(getActivity(), msg);

                    dialog.dismiss();
                    getWallet();
                } else {
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
                   // ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddMoney:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    Intent in = new Intent(getActivity(), AddMoney.class);
                    in.putExtra(Consts.AMOUNT, amt);
                    in.putExtra(Consts.CURRENCY, currency);
                    in.putExtra("wallet_rate", wallet_rate);
                    in.putExtra("razorpay_key", razorpay_key);
                    startActivity(in);
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.tvAll:
                setSelected(true, false, false);
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                getHistroy();
                break;
            case R.id.tvCredit:
                setSelected(false, false, true);
                status = "0";
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.STATUS, status);
                getHistroy();
                break;
            case R.id.tvDebit:
                setSelected(false, true, false);
                status = "1";
                parms = new HashMap<>();
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.STATUS, status);
                getHistroy();
                break;
        }
    }

    public void getHistroy() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_WALLET_HISTORY_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        Log.e("BANK_RES",""+response.toString());
                        wallet_rate = response.getString("wallet_rate");
                        document_details_flag = response.getString("bank_status2");
                        bank_details_flag = response.getString("bank_status");
                        check_bank_tracker = response.getString("check_bank_tracker");
                        cashout_btn_flag = response.getString("cashout_btn_flag");
                        razorpay_key = response.getString("razorpay_key");

                        if (cashout_btn_flag.equalsIgnoreCase("0")){
                            btncashout.setEnabled(false);
                            btncashout.setClickable(false);
                            btnUpdate.setBackground(getActivity().getDrawable(R.drawable.disable_btn));

                        }
                        walletHistoryList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<WalletHistory>>() {
                        }.getType();
                        walletHistoryList = (ArrayList<WalletHistory>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("getwallet","2");

        getWallet();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                      //  Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getHistroy();

                                        } else {
                                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }
                                }
        );

    }

    public void getWallet() {

        new HttpsRequest(Consts.GET_WALLET_API, parmsGetWallet, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e("getwallet",""+response.toString());
                        amt = response.getJSONObject("data").getString("amount");
                        currency = response.getJSONObject("data").getString("currency_type");
                        tvWallet.setText(currency + " " + amt);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                }
            }
        });
    }

    public void showData() {

        etamount.setText(amt);
        if (walletHistoryList.size() > 0) {
            tvNo.setVisibility(View.GONE);
            RVhistorylist.setVisibility(View.VISIBLE);
            adapterWalletHistory = new AdapterWalletHistory(Wallet.this, walletHistoryList);
            RVhistorylist.setAdapter(adapterWalletHistory);

        } else {
            tvNo.setVisibility(View.VISIBLE);
            RVhistorylist.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
     //   Log.e("ONREFREST_Firls", "FIRS");
        getHistroy();
    }

    public void setSelected(boolean firstBTN, boolean secondBTN, boolean thirdBTN) {
        if (firstBTN) {
            tvAllSelect.setVisibility(View.VISIBLE);
            tvDebitSelect.setVisibility(View.GONE);
            tvCreditSelect.setVisibility(View.GONE);

        }
        if (secondBTN) {
            tvDebitSelect.setVisibility(View.VISIBLE);
            tvAllSelect.setVisibility(View.GONE);
            tvCreditSelect.setVisibility(View.GONE);

        }
        if (thirdBTN) {
            tvCreditSelect.setVisibility(View.VISIBLE);
            tvAllSelect.setVisibility(View.GONE);
            tvDebitSelect.setVisibility(View.GONE);

        }
        tvAllSelect.setSelected(firstBTN);
        tvDebitSelect.setSelected(secondBTN);
        tvCreditSelect.setSelected(secondBTN);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
