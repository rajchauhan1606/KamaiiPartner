package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.EarningDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.ReferCustomerAdapter;
import com.kamaii.partner.ui.models.ReferCustomer;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReferCustomerFragment extends Fragment {

    private View view;
    ReferCustomerAdapter referCustomerAdapter;
    private ArrayList<ReferCustomer> referCustomerArrayList=new ArrayList<>();
    RecyclerView rvrefercustomer;
    private LinearLayoutManager mLayoutManager;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private String TAG = ReferCustomerFragment.class.getSimpleName();
    CustomTextViewBold tvNo,txttotalreferamount;
    private BaseActivity baseActivity;
    private SearchView svSearch;
    private HashMap<String, String> paramsRequest = new HashMap<>();
    EarningDTO earningDTO;
    private ArrayList<EarningDTO.ChartData> chartDataList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_refercustomerfragment, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.viewrefer));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);

        rvrefercustomer=view.findViewById(R.id.rvrefercustomer);
        tvNo=view.findViewById(R.id.tvNo);
        svSearch=view.findViewById(R.id.svSearch);
        txttotalreferamount=view.findViewById(R.id.txttotalreferamount);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        paramsRequest.put(Consts.ARTIST_ID, userDTO.getUser_id());
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getEarning();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    if (newText.length() > 0) {
                        referCustomerAdapter.filter(newText.toString());
                    } else {
                        viewrefer();

                    }
                } catch (Exception e) {

                }
                return false;
            }
        });
        if (NetworkManager.isConnectToInternet(getActivity())) {
            viewrefer();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        view.findViewById(R.id.add_refer_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AddReferFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void getEarning() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.MY_EARNING1_API, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        earningDTO = new Gson().fromJson(response.getJSONObject("data").toString(), EarningDTO.class);
                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }
        });

    }
    public void showData() {


        txttotalreferamount.setText(earningDTO.getCurrency_symbol() + earningDTO.getReferral_earning());


    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            viewrefer();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
    }

    public void viewrefer() {
       // ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.VIEW_REFER, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
               // ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        //ProjectUtils.pauseProgressDialog();
                        referCustomerArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ReferCustomer>>() {
                        }.getType();
                        referCustomerArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        rvrefercustomer.setLayoutManager(mLayoutManager);
                        referCustomerAdapter = new ReferCustomerAdapter(getActivity(), referCustomerArrayList);
                        rvrefercustomer.setAdapter(referCustomerAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    rvrefercustomer.setVisibility(View.GONE);
                    svSearch.setVisibility(View.GONE);
                  //  ProjectUtils.pauseProgressDialog();
                 //   ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


}
