package com.kamaii.partner.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentTermsAndConditionBinding;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.ui.activity.BaseActivity;


public class TermsAndConditionFragment extends Fragment {

    FragmentTermsAndConditionBinding binding;
    BaseActivity baseActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_terms_and_condition, container, false);

        baseActivity.ivLogo.setVisibility(View.VISIBLE);
        baseActivity.headerNameTV.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.menuLeftIV.setVisibility(View.GONE);
        baseActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        binding.termsWebview.setWebViewClient(new MyWebViewClient());
        binding.termsWebview.getSettings().setLoadsImagesAutomatically(true);
        binding.termsWebview.getSettings().setJavaScriptEnabled(true);
        binding.termsWebview.loadUrl(Consts.TERMS_AND_CONDITION_URL);

        binding.termsSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkboxbtn.isChecked()){
                    baseActivity.loadHomeFragment(new MyOrders(),"myorders");
                }else {
                    Toast.makeText(getActivity(), "ascascasdcsda sdadsa dscs", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity)context;
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }


}