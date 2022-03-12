package com.kamaii.partner.ui.adapter;


import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.ui.fragment.CustomerBooking;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterCustomerBooking extends RecyclerView.Adapter<AdapterCustomerBooking.MyViewHolder> {
    private String TAG = AdapterCustomerBooking.class.getSimpleName();
    private CustomerBooking customerBooking;
    private Context mContext;
    private ArrayList<ArtistBooking> artistBooking;
    private UserDTO userDTO;
    private HashMap<String, String> paramsBookingOp;
    public AdapterCustomerBooking(CustomerBooking customerBooking, ArrayList<ArtistBooking> artistBooking, UserDTO userDTO) {
        this.customerBooking = customerBooking;
        mContext = customerBooking.getActivity();
        this.artistBooking = artistBooking;
        this.userDTO = userDTO;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_customer_bookings, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvName.setText(artistBooking.get(position).getUserName());
        holder.tvLocation.setText(artistBooking.get(position).getAddress());
        holder.tvDate.setText(ProjectUtils.changeDateFormate1(artistBooking.get(position).getBooking_date())+" "+artistBooking.get(position).getBooking_time());
        Glide.with(mContext).
                load(artistBooking.get(position).getUserImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivArtist);
        holder.tvDescription.setText(artistBooking.get(position).getDescription());
        if (artistBooking.get(position).getBooking_type().equalsIgnoreCase("0")||artistBooking.get(position).getBooking_type().equalsIgnoreCase("3")){

            if (artistBooking.get(position).getBooking_flag().equalsIgnoreCase("0")) {
                holder.llACDE.setVisibility(View.VISIBLE);
                holder.llTime.setVisibility(View.GONE);
                holder.llSt.setVisibility(View.GONE);
                holder.llFinishJob.setVisibility(View.GONE);

            } else if (artistBooking.get(position).getBooking_flag().equalsIgnoreCase("1")) {
                holder.llSt.setVisibility(View.VISIBLE);
                holder.llACDE.setVisibility(View.GONE);
                holder.llTime.setVisibility(View.GONE);
                holder.llFinishJob.setVisibility(View.GONE);
            } else if (artistBooking.get(position).getBooking_flag().equalsIgnoreCase("3")) {

                holder.llSt.setVisibility(View.GONE);
                holder.llACDE.setVisibility(View.GONE);
                holder.llTime.setVisibility(View.VISIBLE);
                holder.llFinishJob.setVisibility(View.VISIBLE);
                holder.llWork.setVisibility(View.GONE);

                SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

                try {
                    Date dt;

                    if(artistBooking.get(position).getWorking_min().equalsIgnoreCase("0")){
                        dt = sdf.parse("0.1");

                    }else {
                        dt = sdf.parse(artistBooking.get(position).getWorking_min());

                    }
                    sdf = new SimpleDateFormat("HH:mm:ss");
                    System.out.println(sdf.format(dt));
                    int min = dt.getHours() * 60 + dt.getMinutes();
                    int sec = dt.getSeconds();
                    holder.chronometer.setBase(SystemClock.elapsedRealtime() - (min * 60000 + sec * 1000));
                    holder.chronometer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }



        }else if(artistBooking.get(position).getBooking_type().equalsIgnoreCase("2")) {

            if (artistBooking.get(position).getBooking_flag().equalsIgnoreCase("0")) {
                holder.llACDE.setVisibility(View.VISIBLE);
                holder.llTime.setVisibility(View.GONE);
                holder.llSt.setVisibility(View.GONE);
                holder.llFinishJob.setVisibility(View.GONE);
                holder.llWork.setVisibility(View.GONE);

            } else if (artistBooking.get(position).getBooking_flag().equalsIgnoreCase("1")) {
                holder.llSt.setVisibility(View.VISIBLE);
                holder.llACDE.setVisibility(View.GONE);
                holder.llTime.setVisibility(View.GONE);
                holder.llFinishJob.setVisibility(View.GONE);
                holder.llWork.setVisibility(View.GONE);
            } else if (artistBooking.get(position).getBooking_flag().equalsIgnoreCase("3")) {

                holder.llSt.setVisibility(View.GONE);
                holder.llACDE.setVisibility(View.GONE);
                holder.llTime.setVisibility(View.VISIBLE);
                holder.llFinishJob.setVisibility(View.VISIBLE);
                holder.llWork.setVisibility(View.GONE);

                SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

                try {
                    Date dt;

                    if(artistBooking.get(position).getWorking_min().equalsIgnoreCase("0")){
                        dt = sdf.parse("0.1");

                    }else {
                        dt = sdf.parse(artistBooking.get(position).getWorking_min());

                    }
                    sdf = new SimpleDateFormat("HH:mm:ss");
                    System.out.println(sdf.format(dt));
                    int min = dt.getHours() * 60 + dt.getMinutes();
                    int sec = dt.getSeconds();
                    holder.chronometer.setBase(SystemClock.elapsedRealtime() - (min * 60000 + sec * 1000));
                    holder.chronometer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }
        holder.llFinishJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(mContext)) {

                    booking("3", position);

                } else {
           //         ProjectUtils.showToast(mContext, mContext.getResources().getString(R.string.internet_concation));
                }
            }
        });
    }
    public void booking(String req, int pos) {
        paramsBookingOp = new HashMap<>();
        paramsBookingOp.put(Consts.BOOKING_ID, artistBooking.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, artistBooking.get(pos).getUser_id());
        ProjectUtils.showProgressDialog(mContext, true, mContext.getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
              //      ProjectUtils.showToast(mContext, msg);
                    customerBooking.getBooking();


                } else {
               //     ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }


    @Override
    public int getItemCount() {

        return artistBooking.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llACDE, llAccept, llDecline, llSt, llStart, llCancel, llFinishJob,llWork;
        private CircleImageView ivArtist;
        private CustomTextViewBold tvName;
        private CustomTextView tvLocation,tvDescription,tvDate;
        private RelativeLayout llTime;
        private Chronometer chronometer;
        private CardView cardData;
        public MyViewHolder(View v) {
            super(v);
            cardData = v.findViewById(R.id.cardData);
            llACDE = v.findViewById(R.id.llACDE);
            llTime = v.findViewById(R.id.llTime);
            llSt = v.findViewById(R.id.llSt);
            llFinishJob = v.findViewById(R.id.llFinishJob);
            llWork = v.findViewById(R.id.llWork);

            ivArtist = v.findViewById(R.id.ivArtist);
            tvName = v.findViewById(R.id.tvName);
            tvDate = v.findViewById(R.id.tvDate);
            tvLocation = v.findViewById(R.id.tvLocation);
            tvDescription = v.findViewById(R.id.tvDescription);
            chronometer = v.findViewById(R.id.chronometer);
            llAccept = v.findViewById(R.id.llAccept);
            llDecline = v.findViewById(R.id.llDecline);
            llStart = v.findViewById(R.id.llStart);
            llCancel = v.findViewById(R.id.llCancel);


        }
    }




}