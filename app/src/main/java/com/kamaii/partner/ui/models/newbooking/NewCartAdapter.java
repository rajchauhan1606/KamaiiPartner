package com.kamaii.partner.ui.models.newbooking;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.CartNewBookingLayoutBinding;

import java.util.List;

public class NewCartAdapter extends RecyclerView.Adapter<NewCartAdapter.NewCartViewholder> {

    Context context;
    List<ProductsArray> orderList;

    public NewCartAdapter(Context context, List<ProductsArray> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    @NonNull
    @Override
    public NewCartViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartNewBookingLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_new_booking_layout,parent,false);
        return new NewCartViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewCartViewholder holder, int position) {

        holder.binding.newBookingCartPrdName.setText(orderList.get(position).getServiceName());
        holder.binding.newBookingCartPrdQty.setText(" x "+orderList.get(position).getQty());
        holder.binding.newBookingCartPrice.setText(Html.fromHtml("&#8377;"+orderList.get(position).getSprice()));
        if (orderList.get(position).getCooking_instruction().equalsIgnoreCase("null")){
            holder.binding.newCartMain.setBackground(null);
            holder.binding.instructionRel.setVisibility(View.GONE);
        }else {
            //holder.binding.newCartMain.setBackground(context.getResources().getDrawable(R.drawable.custom_card_relative_background));
            holder.binding.newCartMain.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_blue));
            holder.binding.cookingInstructionTxt.setText(orderList.get(position).getCooking_instruction());
            holder.binding.instructionRel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class NewCartViewholder extends RecyclerView.ViewHolder {
        CartNewBookingLayoutBinding binding;
        public NewCartViewholder(@NonNull CartNewBookingLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
