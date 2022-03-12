package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentTvideoBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSelectedItemListener;
import com.kamaii.partner.model.TvideoModel;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.adapter.TvideoAdapter;
import com.kamaii.partner.ui.fragment.AppUpdateFragment;
import com.kamaii.partner.ui.fragment.TVideoFragment;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.mapbox.mapboxsdk.Mapbox;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    TvideoAdapter tvideoAdapter;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    private ArrayList<TvideoModel> tvideoModelArrayList=new ArrayList<>();
    private String TAG = TVideoFragment.class.getSimpleName();
    RecyclerView rvtvideo;
    CustomTextViewBold tvNo;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tranning_activity);


        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        parms.put("artist_id",userDTO.getUser_id());
        llBack=findViewById(R.id.llBack);
        rvtvideo=findViewById(R.id.rvtvideo);
        tvNo=findViewById(R.id.tvNo);
        mLayoutManager = new LinearLayoutManager(this);
        rvtvideo.setLayoutManager(mLayoutManager);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TrainingActivity.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

        gettvideo();

    }

    public void gettvideo() {

        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_TVIDEO2, parms, TrainingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                //   ProjectUtils.pauseProgressDialog();
                //binding.swipeRefreshLayout.setRefreshing(false);
                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    rvtvideo.setVisibility(View.VISIBLE);



                    try {
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<TvideoModel>>() {
                        }.getType();
                        tvideoModelArrayList = (ArrayList<TvideoModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);




                        tvideoAdapter=new TvideoAdapter(TrainingActivity.this,tvideoModelArrayList,onsubItemListener);
                        rvtvideo.setAdapter(tvideoAdapter);

                        update_status();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    tvNo.setVisibility(View.VISIBLE);
                    rvtvideo.setVisibility(View.GONE);

                }
            }
        });


    }

    public void update_status() {

        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_FIVESTATUS, parms, TrainingActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                //   ProjectUtils.pauseProgressDialog();
                //binding.swipeRefreshLayout.setRefreshing(false);
                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    private OnSelectedItemListener onsubItemListener=new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String url, String shipping, String mylocation) {


            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                TrainingActivity.this.startActivity(webIntent);
            } catch (ActivityNotFoundException ex) {
            }

        }
    };

}