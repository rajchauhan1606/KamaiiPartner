package com.kamaii.partner.ui.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.ui.fragment.BookingWithRiderFragment;
import com.kamaii.partner.ui.fragment.CabBookingsFragment;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;


public class AdapterCartCab extends RecyclerView.Adapter<AdapterCartCab.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private CabBookingsFragment newBookings;
    private BookingWithRiderFragment bookingWithRiderFragment;
    private Context context;
    private ArrayList<ProductDTO> productDTOArrayList, productDTOArrayListtemp;
    String locationstatus = "";
    Boolean isCheck = true;

    public AdapterCartCab(CabBookingsFragment newBookings, ArrayList<ProductDTO> productDTOArrayList, String locationstatus) {
        this.newBookings = newBookings;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        context = newBookings.getActivity();
        productDTOArrayListtemp = isused();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public AdapterCartCab(BookingWithRiderFragment newBookings, ArrayList<ProductDTO> productDTOArrayList, String locationstatus) {
        this.bookingWithRiderFragment = newBookings;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        context = bookingWithRiderFragment.getActivity();
        productDTOArrayListtemp = isused();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("productDTOArrayList", "" + productDTOArrayList.toString());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        if (productDTOArrayListtemp.get(position).getVehicle_number().equalsIgnoreCase("")) {
            holder.tvvehiclenumber.setVisibility(View.GONE);
            holder.one.setVisibility(View.GONE);
        } else {
            holder.tvvehiclenumber.setVisibility(View.VISIBLE);
            holder.tvvehiclenumber.setText(productDTOArrayListtemp.get(position).getVehicle_number());
            holder.tvPrices.setVisibility(View.GONE);
            holder.one.setVisibility(View.VISIBLE);
            holder.tvPrices_title.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
        }
        if (!productDTOArrayListtemp.get(position).getQuantitydays().equalsIgnoreCase("")) {

            if (productDTOArrayListtemp.get(position).getQuantitydays().equalsIgnoreCase("0")) {
                holder.tvquantitydays.setText("Qty");
            } else {
                holder.tvquantitydays.setText("KM");
            }

        }

        if (!productDTOArrayListtemp.get(position).getQty_type().equalsIgnoreCase("")
        || productDTOArrayListtemp.get(position).getQty_type().equalsIgnoreCase(" ")
        || productDTOArrayListtemp.get(position).getQty_type().equalsIgnoreCase(null)){

            holder.booking_type.setVisibility(View.VISIBLE);
            holder.booking_type.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getQty_type()));
        }
        else {
            holder.booking_type.setVisibility(View.GONE);
        }

        if (!productDTOArrayListtemp.get(position).getQty_description().equalsIgnoreCase("")
                || productDTOArrayListtemp.get(position).getQty_description().equalsIgnoreCase(" ")
                || productDTOArrayListtemp.get(position).getQty_description().equalsIgnoreCase(null)) {

            holder.booking_des.setVisibility(View.VISIBLE);
            holder.booking_des.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getQty_description()));
        } else {
            holder.booking_des.setVisibility(View.GONE);
        }

        holder.shortdesc.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getShort_description()));
        Glide.with(context).load(productDTOArrayListtemp.get(position).getProduct_image()).placeholder(R.drawable.dafault_product).into(holder.ProductImg);

        if (AdapterCabBookings.pay_type.equalsIgnoreCase("0")) {
            // holder.binding.txtptype.setVisibility(View.GONE);
            holder.payment_mode_txt.setText("Online Payment");

        } else if (AdapterCabBookings.pay_type.equalsIgnoreCase("1")) {
            //holder.binding.txtptype.setVisibility(View.GONE);
            holder.payment_mode_txt.setText("Cash");

        } else if (AdapterCabBookings.pay_type.equalsIgnoreCase("2")) {
            //holder.binding.txtptype.setVisibility(View.GONE);
            holder.payment_mode_txt.setText("Wallet Payment");

        } else {
            holder.payment_mode_txt.setVisibility(View.GONE);
        }

        if (position == productDTOArrayListtemp.size()-1){
            holder.tmp_line_view.setVisibility(View.GONE);
        }

        if (!productDTOArrayListtemp.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
            holder.tvquantity.setText(productDTOArrayListtemp.get(position).getQty());
        }
        if (!productDTOArrayListtemp.get(position).getChange_price().equalsIgnoreCase("")) {
            // holder.tvPrice.setText(productDTOArrayListtemp.get(position).getChange_price() +" "+ "Rs");
            holder.tvPrice.setText(Html.fromHtml("&#x20B9;" + productDTOArrayListtemp.get(position).getChange_price()));

            double p = Double.parseDouble(productDTOArrayListtemp.get(position).getPrice());
            double t = ((p * 10) / 100) + p;
//            holder.tvPrices.setText(Html.fromHtml("&#x20B9;" + t));
//            holder.tvPrices.setTextColor(context.getResources().getColor(R.color.pink_color));

            Log.e("VARIATION",""+productDTOArrayListtemp.get(position).getVariation());
            if (productDTOArrayListtemp.get(position).getVariation() == null || productDTOArrayListtemp.get(position).getVariation().equalsIgnoreCase("&nbsp;") || productDTOArrayListtemp.get(position).getVariation().isEmpty()){
                holder.tvPrices.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getQty()));
            }else {
                //holder.tvPrices.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getQty()+" ("+productDTOArrayListtemp.get(position).getVariation()+") "));
                holder.tvPrices.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getQty()));
            }
//            holder.tvPrices.setPaintFlags(holder.tvPrices.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        if (!productDTOArrayListtemp.get(position).getProduct_name().equalsIgnoreCase("")) {
            holder.tvProductName.setText(position+1+". "+productDTOArrayListtemp.get(position).getProduct_name());
        }

        if (locationstatus.equalsIgnoreCase("0")) {

        } else if (locationstatus.equalsIgnoreCase("1")) {

            if (productDTOArrayListtemp.get(position).getCategory_id().equalsIgnoreCase("85")) {
                if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                    holder.tvService_charge.setVisibility(View.VISIBLE);
                    holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOArrayListtemp.get(position).getService_charge());
                    // holder.tvService_charge.setText(productDTOList.get(pos).getService_charge()  +" "+ productDTOList.get(pos).getRate());
                }
            } else {
                if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                    holder.tvService_charge.setVisibility(View.VISIBLE);
                    holder.tvService_charge.setText("Service Charge:" + " " + productDTOArrayListtemp.get(position).getService_charge());
                    //  holder.tvService_charge.setText(productDTOList.get(pos).getService_charge()  +" "+ productDTOList.get(pos).getRate());
                }
            }

        }
        holder.tvdesc.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getDescription()));

        holder.tvbdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    holder.tvdesc.setVisibility(View.VISIBLE);
                    holder.shortdesc.setVisibility(View.GONE);
                    holder.tvbdetails.setVisibility(View.GONE);
                    isCheck = false;
                } else {
                    holder.tvdesc.setVisibility(View.GONE);
                    isCheck = true;
                }
            }
        });




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
        public CustomTextViewBold booking_type,booking_des,tvPrices_title,tvPrices,payment_mode_txt, tvProductName, tvPrice, tvService_charge, tvquantity, tvquantitydays, tvroundtrip, tvbdetails, tvvehiclenumber;
        CustomTextView tvdesc, shortdesc;
        ImageView ProductImg;
        RelativeLayout one;
        TextView tmp_line_view;
        public MyViewHolder(View view) {
            super(view);

            ProductImg = view.findViewById(R.id.ProductImg);
            booking_type = view.findViewById(R.id.booking_type);
            booking_des = view.findViewById(R.id.booking_des);
            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            one =  view.findViewById(R.id.one);
            payment_mode_txt = (CustomTextViewBold) view.findViewById(R.id.payment_mode_txt);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tmp_line_view = view.findViewById(R.id.tmp_line_view);
            tvPrices =  view.findViewById(R.id.tvPrices);
            tvPrices_title =  view.findViewById(R.id.tvPrices_title);
            shortdesc = (CustomTextView) view.findViewById(R.id.shortdesc);
            rel_view = view.findViewById(R.id.rel_view);
            tvService_charge = view.findViewById(R.id.tvService_charge);
            lay_service_charge = view.findViewById(R.id.lay_service_charge);
            tvquantity = view.findViewById(R.id.tvquantity);
            tvquantitydays = view.findViewById(R.id.tvquantitydays);
            tvroundtrip = view.findViewById(R.id.tvroundtrip);
            tvdesc = view.findViewById(R.id.tvdesc);
            tvbdetails = view.findViewById(R.id.tvbdetails);
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
