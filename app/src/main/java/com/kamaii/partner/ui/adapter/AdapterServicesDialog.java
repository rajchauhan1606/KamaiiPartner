package com.kamaii.partner.ui.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.ui.fragment.NewBookings;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterServicesDialog extends RecyclerView.Adapter<AdapterServicesDialog.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private NewBookings newBookings;
    private Context context;
    private ArrayList<ProductDTO> productDTOList;
    Boolean isCheck= true;
    String locationstatus="";
    int maxmimumvalue=0;

    public AdapterServicesDialog(NewBookings newBookings, ArrayList<ProductDTO> productDTOList, String locationstatus) {
        this.newBookings = newBookings;
        this.productDTOList = productDTOList;
        this.locationstatus = locationstatus;
        context = newBookings.getActivity();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services_dialog, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int pos) {
      final   int  position=pos;
        Glide
                .with(context)
                .load(productDTOList.get(position).getProduct_image())
                .placeholder(R.drawable.default_image)
                .into(holder.iv_bottom_foster);


        holder.tvPrice.setText(productDTOList.get(position).getPrice() +" "+ productDTOList.get(position).getRate());
        holder.tvProductName.setText(productDTOList.get(position).getProduct_name());
        holder.tvdesc.setText(Html.fromHtml(productDTOList.get(position).getDescription()));

        double price= Double.parseDouble(productDTOList.get(position).getPrice());

        int discountprice= (int) ((price*Consts.DISCOUNT_RATE/100) +price);
        holder.tvDiscountPrice.setText(String.valueOf(discountprice) + " "+productDTOList.get(position).getRate());
       holder.tvDiscountPrice.setPaintFlags(holder.tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



        if(!productDTOList.get(position).getQuantitydays().equalsIgnoreCase("")){

            if(productDTOList.get(position).getQuantitydays().equalsIgnoreCase("0")){
                holder.tvquantitydays.setText("Qty");
            }else {
                holder.tvquantitydays.setText("KM");
            }

        }

        holder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity= Integer.parseInt(holder.tvquantity.getText().toString());

                if(quantity>1){
                    quantity--;
                    holder.tvquantity.setText(String.valueOf(quantity));

                    int servicecharge=0;
                    if(!productDTOList.get(position).getService_charge().equalsIgnoreCase("")){
                        servicecharge= Integer.parseInt(productDTOList.get(position).getService_charge());
                    }else {

                    }
                    int   price= Integer.parseInt(productDTOList.get(position).getPrice());
                    productDTOList.get(position).setQuantityvalue(String.valueOf(quantity));


                 }else {
                    //  Toast.makeText(context,"You",Toast.LENGTH_LONG).show();


                }


            }
        });


        holder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity= Integer.parseInt(holder.tvquantity.getText().toString());

                if(!productDTOList.get(position).getMaxmiumvalue().equalsIgnoreCase("")){
                    maxmimumvalue= Integer.parseInt(productDTOList.get(position).getMaxmiumvalue());
                }
                if(quantity<maxmimumvalue){
                    // Toast.makeText(context,"ok",Toast.LENGTH_LONG).show();
                    quantity++;
                    holder.tvquantity.setText(String.valueOf(quantity));

                    int servicecharge=0;
                    if(!productDTOList.get(position).getService_charge().equalsIgnoreCase("")){
                        servicecharge= Integer.parseInt(productDTOList.get(position).getService_charge());
                    }
                    int   price= Integer.parseInt(productDTOList.get(position).getPrice());
                    // int total=quantitytotal*quantity;
                    // Toast.makeText(context,String.valueOf(total),Toast.LENGTH_LONG).show();

                    productDTOList.get(position).setQuantityvalue(String.valueOf(quantity));


                }else {

                    Toast.makeText(context,"Not Available" , Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (productDTOList.get(position).isSelected()) {
                    productDTOList.get(position).setSelected(false);


                } else {


                   String quantity= productDTOList.get(position).getQuantityvalue();


                   if(quantity.equalsIgnoreCase("")){
                       quantity="1";
                   }

                    if(productDTOList.get(position).getQuantityvalue()==null){
                        quantity="1";
                    }

                    productDTOList.get(position).setQuantityvalue(quantity);
                    productDTOList.get(position).setSelected(true);


                }
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView iv_bottom_foster;
        public LinearLayout rel_view;
        public CustomTextViewBold tvProductName, tvPrice,tvDiscountPrice,tvdesc,tvquantity,tvquantitydays;
        public CheckBox cbSelect;
        public ImageView img_plus,img_minus;
        public MyViewHolder(View view) {
            super(view);

            iv_bottom_foster = view.findViewById(R.id.iv_bottom_foster);
            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tvDiscountPrice = (CustomTextViewBold) view.findViewById(R.id.tvDiscountPrice);
            tvdesc = (CustomTextViewBold) view.findViewById(R.id.tvdesc);
            rel_view =  view.findViewById(R.id.rel_view);
            cbSelect =  view.findViewById(R.id.cbSelect);
            tvquantity =  view.findViewById(R.id.tvquantity);
            img_plus =  view.findViewById(R.id.img_plus);
            img_minus =  view.findViewById(R.id.img_minus);
            tvquantitydays =  view.findViewById(R.id.tvquantitydays);

        }
    }



}
