package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.databinding.FragmentTvideoBinding;
import com.kamaii.partner.ui.activity.YoutubePlaylistActivity;
import com.mapbox.mapboxsdk.Mapbox;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSelectedItemListener;
import com.kamaii.partner.model.TvideoModel;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.TvideoAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TVideoFragment extends Fragment {

    private WebView youtube;

    private BaseActivity baseActivity;
    FragmentTvideoBinding binding;
    private LinearLayoutManager mLayoutManager;
    TvideoAdapter tvideoAdapter;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    private ArrayList<TvideoModel> tvideoModelArrayList=new ArrayList<>();
    private String TAG = TVideoFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tvideo, container, false);

        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        baseActivity.headerNameTV.setText(getContext().getResources().getString(R.string.tvideo));

        init();

        //gettvideo();
        return binding.getRoot();
    }

    private void init() {

        binding.fragmentYoutubeWebview.setWebViewClient(new MyBrowser2());
        binding.fragmentYoutubeWebview.getSettings().setLoadsImagesAutomatically(true);
        binding.fragmentYoutubeWebview.getSettings().setJavaScriptEnabled(true);
        binding.fragmentYoutubeWebview.loadUrl("https://www.youtube.com/channel/UCD_NolR9RK9FQ6kQtQ0hHcQ/featured");

    }

    private class MyBrowser2 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }



}
