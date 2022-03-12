package com.kamaii.partner.ui.adapter;


import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;


public class AdapterInvoiceService extends RecyclerView.Adapter<AdapterInvoiceService.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<ProductDTO> productDTOArrayList, productDTOArrayListtemp;
    String locationstatus = "";

    Boolean isCheck = true;

    public AdapterInvoiceService(Context context, ArrayList<ProductDTO> productDTOArrayList, String locationstatus) {
        this.context = context;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        productDTOArrayListtemp = isused();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("INVOICE_ADAPTERLIST",""+productDTOArrayList.toString());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_booking, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        if (productDTOArrayListtemp.get(position).getVehicle_number().equalsIgnoreCase("")) {
            holder.tvvehiclenumber.setVisibility(View.GONE);
        } else {
            holder.tvvehiclenumber.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvPrices.setVisibility(View.GONE);
            holder.tvPrices_title.setVisibility(View.GONE);
            holder.tvvehiclenumber.setText(productDTOArrayListtemp.get(position).getVehicle_number());
        }

        if (position == productDTOArrayListtemp.size()-1){

            holder.tmp_line_view.setVisibility(View.GONE);
        }
        Glide.with(context).load(productDTOArrayListtemp.get(position).getProduct_image()).placeholder(R.drawable.dafault_product).into(holder.ProductImg);
        if (!productDTOArrayListtemp.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
        }
        if (!productDTOArrayListtemp.get(position).getPrice().equalsIgnoreCase("")) {

            holder.tvPrices.setText(productDTOArrayListtemp.get(position).getQuantitydays() + " "+productDTOArrayListtemp.get(position).getVariation());
            holder.tvPrice.setText(Html.fromHtml("&#x20B9;"+productDTOArrayListtemp.get(position).getChange_price()));
        }

        if (!productDTOArrayListtemp.get(position).getProduct_name().equalsIgnoreCase("")) {
            holder.tvProductName.setText(productDTOArrayListtemp.get(position).getProduct_name());
        }


        if (checkarss(productDTOArrayListtemp.get(position).getCategory_id())) {
            if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
            }
        } else {
            if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
            }
        }



        holder.tvmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    holder.tvdesc.setVisibility(View.VISIBLE);
                    isCheck = false;
                } else {
                    holder.tvdesc.setVisibility(View.GONE);
                    isCheck = true;
                }
            }
        });


    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;
            } else {
                value = false;

            }

        }
        return value;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productDTOArrayListtemp.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout rel_view, lay_service_charge;
        public CustomTextViewBold tvPrices_title,tvProductName, tvPrice, tvService_charge, tvquantity, tvquantitydays, tvmore, tvvehiclenumber;
        ImageView ProductImg;
        CustomTextView tvdesc,tvPrices;
TextView tmp_line_view;
        public MyViewHolder(View view) {
            super(view);


            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            ProductImg =  view.findViewById(R.id.ProductImg);
            tvPrices =  view.findViewById(R.id.tvPrices);
            tmp_line_view =  view.findViewById(R.id.tmp_line_view);
            tvPrices_title =  view.findViewById(R.id.tvPrices_title);
            rel_view = view.findViewById(R.id.rel_view);
            tvService_charge = view.findViewById(R.id.tvService_charge);
            lay_service_charge = view.findViewById(R.id.lay_service_charge);
            tvquantity = view.findViewById(R.id.tvquantity);
            tvquantitydays = view.findViewById(R.id.tvquantitydays);

            tvdesc = view.findViewById(R.id.tvdesc);
            tvmore = view.findViewById(R.id.tvmore);
            tvvehiclenumber = view.findViewById(R.id.tvvehiclenumber);

        }
    }

    private ArrayList<ProductDTO> isused() {
        ArrayList<ProductDTO> arrayList = new ArrayList<>();
        for (int i = 0; i < productDTOArrayList.size(); i++) {
            if (productDTOArrayList.get(i).getIs_used().equalsIgnoreCase("1")) {
                arrayList.add(productDTOArrayList.get(i));
            }
        }
        return arrayList;
    }


}
