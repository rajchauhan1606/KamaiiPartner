package com.kamaii.partner.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kamaii.partner.Glob;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.ui.adapter.ReferCustomerServiceAdapter;
import com.kamaii.partner.ui.models.ReferServiceModel;
import com.kamaii.partner.ui.models.customerservicemodel;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerViewServiceActivity extends AppCompatActivity {


    String cususerid="";
    private String TAG = CustomerViewServiceActivity.class.getSimpleName();
    LinearLayout llBack;
    CustomTextViewBold tvrefer_name,tv_refer_mobile,tv_refer_email,tv_refer_service,tv_total_commsion;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<customerservicemodel> customerservicemodelArrayList=new ArrayList<>();
    ReferServiceModel referServiceModel;
    private CircleImageView ivimagee;
    ReferCustomerServiceAdapter referCustomerServiceAdapter;
    RecyclerView rvrefercustomerservice;
    CustomTextViewBold tvNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_service);

        cususerid=getIntent().getStringExtra(Consts.USER_ID);
        llBack=findViewById(R.id.llBack);
        tvrefer_name=findViewById(R.id.tvrefer_name);
        tv_refer_mobile=findViewById(R.id.tv_refer_mobile);
        tv_refer_email=findViewById(R.id.tv_refer_email);
        ivimagee=findViewById(R.id.ivimagee);
        tv_refer_service=findViewById(R.id.tv_refer_service);
        tv_total_commsion=findViewById(R.id.tv_total_commsion);
        rvrefercustomerservice=findViewById(R.id.rvrefercustomerservice);
        tvNo=findViewById(R.id.tvNo);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (NetworkManager.isConnectToInternet(CustomerViewServiceActivity.this)) {
            viewreferservice();

        } else {
            ProjectUtils.showToast(CustomerViewServiceActivity.this, getResources().getString(R.string.internet_concation));
        }
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID,cususerid);
        return parms;
    }

    public void viewreferservice() {
        ProjectUtils.showProgressDialog(this, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.VIEW_REFER_SERVICE, getparm(), CustomerViewServiceActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        referServiceModel = new Gson().fromJson(response.getJSONObject("data").toString(), ReferServiceModel.class);
                        customerservicemodelArrayList = new ArrayList<>();
                        customerservicemodelArrayList = referServiceModel.getSer_list();
                        mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvrefercustomerservice.setLayoutManager(mLayoutManager);
                        referCustomerServiceAdapter = new ReferCustomerServiceAdapter(CustomerViewServiceActivity.this, customerservicemodelArrayList);
                        rvrefercustomerservice.setAdapter(referCustomerServiceAdapter);


                        if(customerservicemodelArrayList.size()==0){
                            tvNo.setVisibility(View.VISIBLE);
                            rvrefercustomerservice.setVisibility(View.INVISIBLE);
                        }
                       shodata();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    rvrefercustomerservice.setVisibility(View.INVISIBLE);
                    ProjectUtils.showToast(CustomerViewServiceActivity.this, msg);
                }


            }
        });
    }

    private  void shodata(){
        tvrefer_name.setText(referServiceModel.getName());
        tv_refer_mobile.setText(referServiceModel.getMobile());
        tv_refer_email.setText(referServiceModel.getEmail_id());
        tv_refer_service.setText("Total Service : "+referServiceModel.getTotal_use_service());

        if(referServiceModel.getTotal_commision().equalsIgnoreCase("null")){
            tv_total_commsion.setText("Refer Amount : 0");
        }else {
            tv_total_commsion.setText("Refer Amount : "+referServiceModel.getTotal_commision());
        }
            Glide.with(CustomerViewServiceActivity.this).
                    load(referServiceModel.getImage())
                    .placeholder(R.drawable.default_image)
                    .dontAnimate()
                    .into(ivimagee);


    }
    @Override
    public void onBackPressed() {
        com.kamaii.partner.Glob.BUBBLE_VALUE ="0";
        finish();
    }
}
