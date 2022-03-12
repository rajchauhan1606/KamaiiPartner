package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.SelectServiceAdapter;
import com.kamaii.partner.ui.models.ServiceListModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectServicesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private String TAG = SelectServicesFragment.class.getSimpleName();
    ExpandableHeightGridView recyclerviw;
    CheckBox select_all_checkbox;
    SelectServiceAdapter serviceAdapter;
    private BaseActivity baseActivity;
    List<ServiceListModel> serviceList;
    int check_status = 0;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    public String services_id;
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> paramsServiceID = new HashMap<>();
    StringBuilder sb;
    List<String> checkBoxId;
    int flag_check=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_services, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.select_services));
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        init(view);

        select_all_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_status = 1;
                    loadAdapter();
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    if(flag_check==1){
                        flag_check=0;
                    }else {
                        check_status = 0;
                        loadAdapter();
                        serviceAdapter.notifyDataSetChanged();
                        checkBoxId.clear();
                        flag_check=0;
                    }

                }

            }
        });

        view.findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendServices();
            }
        });
        return view;
    }


    public void init(View view) {

        // swipeRefreshLayout = view.findViewById(R.id.select_services_swipe_refresh_layout);
        recyclerviw = view.findViewById(R.id.share_service_recyclerviw);
        //  swipeRefreshLayout.setOnRefreshListener(this);
        select_all_checkbox = view.findViewById(R.id.select_all_checkbox);
        serviceList = new ArrayList<>();
    }

    public void loadAdapter() {

        serviceAdapter = new SelectServiceAdapter(getActivity(), serviceList, check_status);
        //recyclerviw.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviw.setExpanded(true);
        recyclerviw.setAdapter(serviceAdapter);
    }

    private void sendServices() {
        services_id="";
        for (String ch:checkBoxId){
            services_id+=ch+"#";
        }
//        services_id = sb.toString();
        paramsServiceID.put(Consts.ARTIST_ID, userDTO.getUser_id());
        paramsServiceID.put(Consts.SELECTED_SERVICE_ID, services_id);
        new HttpsRequest(Consts.SEND_SERVICE_API, paramsServiceID, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    //ProjectUtils.showToast(Booking.this, msg);
                    try {
                        //  userBookingList = new ArrayList<>();
                        // Type getpetDTO = new TypeToken<List<CartModel>>() {
                        // }.getType();
                        // JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        String success_msg = jsonObject.getString("message");
                        String data_msg = jsonObject.getString("data");

                        Log.e("success_msg", "" + success_msg);


                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = data_msg;
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public void getServices() {


        //Log.e("UID_QTY", "5->" + quentity);
        //Log.e("HASHMAP", "5->" + paramsBookingOp2.toString());

        new HttpsRequest(Consts.GET_SHARED_SERVICE_API, params, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    //ProjectUtils.showToast(Booking.this, msg);
                    try {
                        //  userBookingList = new ArrayList<>();
                        // Type getpetDTO = new TypeToken<List<CartModel>>() {
                        // }.getType();
                        // JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        String success_msg = jsonObject.getString("message");

                        serviceList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            ServiceListModel serviceListModel = new ServiceListModel();
                            serviceListModel.setId(jsonObject1.getString("id"));
                            serviceListModel.setPrice(jsonObject1.getString("price"));
                            serviceListModel.setProduct_name(jsonObject1.getString("product_name"));
                            serviceList.add(serviceListModel);
                        }


                        loadAdapter();
                        Log.e("success_msg", "" + success_msg);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getServices();
        //  swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        // swipeRefreshLayout.setRefreshing(false);
        getServices();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public class SelectServiceAdapter extends BaseAdapter {

        Context context;
        List<ServiceListModel> arraylist;
        public int check_status = -1;

        public SelectServiceAdapter(Context context, List<ServiceListModel> arraylist, int check_status) {
            this.context = context;
            this.arraylist = arraylist;
            this.check_status = check_status;
            Log.e("check_status",""+String.valueOf(check_status));
            checkBoxId = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return arraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.service_list_recycle_layout, null);

            CustomTextViewBold prd_name, prd_price;
            CustomTextView prd_mrp;
            CheckBox checkBox_img;

            prd_name = view.findViewById(R.id.service_recycle_name);
            prd_price = view.findViewById(R.id.select_service_tvPrice);
            //prd_mrp = view.findViewById(R.id.select_service_tvDiscountPrice);
            checkBox_img = view.findViewById(R.id.select_checkbox);

            prd_name.setText(arraylist.get(position).getProduct_name());
            prd_price.setText(arraylist.get(position).getPrice());

            checkBox_img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!checkBoxId.contains(arraylist.get(position).getId())) {
                            checkBoxId.add(arraylist.get(position).getId());
                        }
//                        Log.e("CHECKIDLISTSIZE",""+checkBoxId.size() + arraylist.size());
                        if(checkBoxId.size() >= arraylist.size()){
                            select_all_checkbox.setChecked(true);
                        }
                    } else {

                        if (checkBoxId.contains(arraylist.get(position).getId())){

                            checkBoxId.remove(arraylist.get(position).getId());
                            flag_check=1;
                            select_all_checkbox.setChecked(false);
                        }
                    }
                }
            });

            if (select_all_checkbox.isChecked()) {
                checkBox_img.setChecked(true);

                if (!checkBoxId.contains(arraylist.get(position).getId())) {
                    checkBoxId.add(arraylist.get(position).getId());
                }
                Log.e("CHECKBOXID",""+checkBoxId.toString());
            }else {
                Toast.makeText(context, "f"+position, Toast.LENGTH_SHORT).show();
            }
            return view;
        }
    }

}