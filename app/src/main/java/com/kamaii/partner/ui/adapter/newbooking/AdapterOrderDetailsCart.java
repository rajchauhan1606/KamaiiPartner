package com.kamaii.partner.ui.adapter.newbooking;

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
import com.kamaii.partner.databinding.PreparationDialogCartlistBinding;
import com.kamaii.partner.ui.models.newbooking.ProductsArray;

import java.util.List;

public class AdapterOrderDetailsCart extends RecyclerView.Adapter<AdapterOrderDetailsCart.DetailViewHolder> {

    Context context;
    List<ProductsArray> arrayList;

    public AdapterOrderDetailsCart(Context context, List<ProductsArray> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartNewBookingLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_new_booking_layout,parent,false);
        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {

        holder.binding.newBookingCartPrdName.setText(arrayList.get(position).getServiceName()+" x ");
        holder.binding.newBookingCartPrdQty.setText(arrayList.get(position).getQty());
        holder.binding.newBookingCartPrice.setText(Html.fromHtml("&#8377;"+arrayList.get(position).getSprice()));
        if (arrayList.get(position).getCooking_instruction().equalsIgnoreCase("null")){
            holder.binding.newCartMain.setBackground(null);
            holder.binding.instructionRel.setVisibility(View.GONE);
        }else {
            //holder.binding.newCartMain.setBackground(context.getResources().getDrawable(R.drawable.custom_card_relative_background));
            holder.binding.newCartMain.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_blue));
            holder.binding.cookingInstructionTxt.setText(arrayList.get(position).getCooking_instruction());
            holder.binding.instructionRel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        CartNewBookingLayoutBinding binding;
        public DetailViewHolder(@NonNull CartNewBookingLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
