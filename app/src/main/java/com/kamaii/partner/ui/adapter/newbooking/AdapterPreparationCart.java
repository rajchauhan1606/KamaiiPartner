package com.kamaii.partner.ui.adapter.newbooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.PreparationDialogCartlistBinding;
import com.kamaii.partner.ui.models.newbooking.ProductsArray;

import java.util.List;

public class AdapterPreparationCart extends RecyclerView.Adapter<AdapterPreparationCart.PreparationViewHolder> {

    Context context;
    List<ProductsArray> arrayList;

    public AdapterPreparationCart(Context context, List<ProductsArray> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PreparationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PreparationDialogCartlistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.preparation_dialog_cartlist,parent,false);
        return new PreparationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PreparationViewHolder holder, int position) {

        holder.binding.tvProductName.setText(arrayList.get(position).getServiceName());
        holder.binding.tvPrices.setText(arrayList.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PreparationViewHolder extends RecyclerView.ViewHolder {
        PreparationDialogCartlistBinding binding;
        public PreparationViewHolder(@NonNull PreparationDialogCartlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
