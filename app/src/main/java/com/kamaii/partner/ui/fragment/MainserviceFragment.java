package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.AdapterServices;
import com.kamaii.partner.ui.models.GuideModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.wooplr.spotlight.prefs.PreferencesManager;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainserviceFragment extends Fragment implements View.OnClickListener {
    public View view;
    public BaseActivity baseActivity;
    PreferencesManager mPreferencesManager;
    public Services servicesfrag = new Services();
    public ServiceShippingFragment serviceShippingFragment = new ServiceShippingFragment();
    public FragmentManager fragmentManager;
    public CustomTextViewBold tvPaid, tvUnPaid;
    TextView tvpaid_txt, tvunpaid_txt;
    public ArrayList<GuideModel> guideList = new ArrayList<>();
    String shipping_guide_title = "";
    String shipping_guide_txt = "";
    public String TAG = ServiceShippingFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mainservice, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.invoice));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);

        mPreferencesManager = new PreferencesManager(getActivity());
        fragmentManager = getChildFragmentManager();
        tvUnPaid =  view.findViewById(R.id.tvUnPaid);
        tvPaid =  view.findViewById(R.id.tvPaid);
        tvpaid_txt = view.findViewById(R.id.tvpaid_txt);
        tvunpaid_txt = view.findViewById(R.id.tvunpaid_txt);

        tvPaid.setOnClickListener(this);
        tvUnPaid.setOnClickListener(this);
        //getGuide();
       // baseActivity.header.setBackgroundColor(getResources().getColor(R.color.white));
       // baseActivity.headerNameTV.setTextColor(getResources().getColor(R.color.dark_blue_color));
       // baseActivity.menuLeftIV.setImageTintList(getResources().getColorStateList(R.color.dark_blue_color));


        fragmentManager.beginTransaction().add(R.id.frame, servicesfrag).commit();
        return view;
    }

    public void getGuide() {

        new HttpsRequest(Consts.GET_GUIDE_SEQUENCE, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {

                        Log.e("guideList", "" + response.toString());

                        guideList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<GuideModel>>() {
                        }.getType();
                        guideList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        shipping_guide_title = guideList.get(11).getState();
                        shipping_guide_txt = guideList.get(11).getAppnotificationtext();

                        baseActivity.getSpotLight(tvUnPaid,shipping_guide_title,shipping_guide_txt,"s");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaid:
                setSelected(true, false);
                fragmentManager.beginTransaction().replace(R.id.frame, servicesfrag).commit();
                break;
            case R.id.tvUnPaid:
                setSelected(false, true);
                fragmentManager.beginTransaction().replace(R.id.frame, serviceShippingFragment).commit();
                break;
        }

    }

    public void setSelected(boolean firstBTN, boolean secondBTN) {
        if (firstBTN) {
           // tvPaid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tvPaid.setTextColor(getResources().getColor(R.color.pink_color));
           // tvUnPaid.setBackgroundColor(getResources().getColor(R.color.white));
            tvUnPaid.setTextColor(getResources().getColor(R.color.dark_blue_color));
            tvpaid_txt.setVisibility(View.VISIBLE);
            tvunpaid_txt.setVisibility(View.GONE);
        }
        if (secondBTN) {
         //   tvPaid.setBackgroundColor(getResources().getColor(R.color.white));
            tvPaid.setTextColor(getResources().getColor(R.color.dark_blue_color));
          //  tvUnPaid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tvUnPaid.setTextColor(getResources().getColor(R.color.pink_color));
            tvpaid_txt.setVisibility(View.GONE);
            tvunpaid_txt.setVisibility(View.VISIBLE);

        }
        tvPaid.setSelected(firstBTN);
        tvUnPaid.setSelected(secondBTN);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
