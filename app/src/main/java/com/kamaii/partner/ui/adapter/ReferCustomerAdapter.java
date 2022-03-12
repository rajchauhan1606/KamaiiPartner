package com.kamaii.partner.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.partner.Glob;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.ui.activity.CustomerViewServiceActivity;
import com.kamaii.partner.ui.models.ReferCustomer;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReferCustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback{

    LayoutInflater mLayoutInflater;
    private ArrayList<ReferCustomer> referCustomerArrayList;
    ArrayList<ReferCustomer> historyDTOList;
    private Context context;
    int call_pos = -1;


    public ReferCustomerAdapter(Activity activity, ArrayList<ReferCustomer> referCustomerArrayList ) {
        context =activity;
        this.referCustomerArrayList = referCustomerArrayList;
        this.historyDTOList = new ArrayList<ReferCustomer>();
        this.historyDTOList.addAll(referCustomerArrayList);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_refer_customer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final int pos = position;


            myViewHolder.tvrefer_name.setText(referCustomerArrayList.get(pos).getName());
            myViewHolder.tv_refer_mobile.setText(referCustomerArrayList.get(pos).getMobile() );
            Glide.with(context).
                    load(referCustomerArrayList.get(pos).getImage())
                    .placeholder(R.drawable.default_image)
                    .dontAnimate()
                    .into(myViewHolder.ivimage);


            myViewHolder.llalyout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(referCustomerArrayList.get(pos).getCus_id().equalsIgnoreCase("")){
                        Toast.makeText(context,"Customer Not Registered", Toast.LENGTH_LONG).show();
                    }else {

                        if(referCustomerArrayList.get(pos).getCus_id()==null){
                            Toast.makeText(context,"Customer Not Registered", Toast.LENGTH_LONG).show();
                        }else {
                            com.kamaii.partner.Glob.BUBBLE_VALUE ="1";
                            Intent intent=new Intent(context, CustomerViewServiceActivity.class);
                            intent.putExtra(Consts.USER_ID,referCustomerArrayList.get(pos).getCus_id());
                            context.startActivity(intent);
                        }

                    }

                    // setonItemListener.Click(referCustomerArrayList.get(pos).getUser_id(),pos);
                }
            });


        ((MyViewHolder) holder).call_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_pos = position;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},100);
                }
                else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+referCustomerArrayList.get(call_pos).getMobile()));
                    context.startActivity(callIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return referCustomerArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public CustomTextViewBold tvrefer_name, tv_refer_mobile,tv_refer_email;
        public CircleImageView ivimage;
        LinearLayout llalyout;
        ImageView call_img;
        public MyViewHolder(View view) {
            super(view);

            tv_refer_mobile = itemView.findViewById(R.id.tv_refer_mobile);
            tvrefer_name = itemView.findViewById(R.id.tvrefer_name);
            ivimage = itemView.findViewById(R.id.ivimage);
            llalyout = itemView.findViewById(R.id.llalyout);
            call_img = itemView.findViewById(R.id.call_img);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 100){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+referCustomerArrayList.get(call_pos).getMobile()));
                context.startActivity(callIntent);
            }
            else {
                Toast.makeText(context, "Call Permission Require!!", Toast.LENGTH_SHORT).show();
            }
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

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        referCustomerArrayList.clear();
        if (charText.length() == 0) {
            referCustomerArrayList.addAll(historyDTOList);
        } else {
            for (ReferCustomer historyDTO : historyDTOList) {
                if (historyDTO.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    referCustomerArrayList.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}