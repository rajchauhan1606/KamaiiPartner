package com.kamaii.partner.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.ui.models.customerservicemodel;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReferCustomerServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater mLayoutInflater;
    private ArrayList<customerservicemodel> referCustomerArrayList;
    private Context context;


    public ReferCustomerServiceAdapter(Activity activity, ArrayList<customerservicemodel> referCustomerArrayList ) {
        context =activity;
        this.referCustomerArrayList = referCustomerArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refer_customer_service, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final int pos = position;

        myViewHolder.tvservicename.setText(referCustomerArrayList.get(pos).getProduct_name());
        myViewHolder.tv_customername.setText(referCustomerArrayList.get(pos).getName() );
        myViewHolder.tv_customerbookingdate.setText(referCustomerArrayList.get(pos).getBooking_date() );
     //   myViewHolder.tv_cashback.setText(referCustomerArrayList.get(pos).getBooking_date() );


        myViewHolder.llalyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // setonItemListener.Click(referCustomerArrayList.get(pos).getUser_id(),pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return referCustomerArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public CustomTextViewBold tvservicename, tv_customername,tv_customerbookingdate,tv_cashback;
        public CircleImageView ivimageservice;
        LinearLayout llalyout;

        public MyViewHolder(View view) {
            super(view);



            tv_customername = itemView.findViewById(R.id.tv_customername);
            tvservicename = itemView.findViewById(R.id.tvservicename);
            tv_customerbookingdate = itemView.findViewById(R.id.tv_customerbookingdate);
            tv_cashback = itemView.findViewById(R.id.tv_cashback);
            llalyout = itemView.findViewById(R.id.llalyout);



        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        public ViewHolder1(View v) {
            super(v);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return  position;

    }


}