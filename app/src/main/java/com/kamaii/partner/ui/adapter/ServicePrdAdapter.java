package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ServicePrdRecyclerLayoutBinding;
import com.kamaii.partner.ui.models.ServicePrdVariationModel;

import java.util.List;

public class ServicePrdAdapter extends RecyclerView.Adapter<ServicePrdAdapter.PrdViewHolder> {

    Context context;
    List<ServicePrdVariationModel> arrayList;

    public ServicePrdAdapter(Context context, List<ServicePrdVariationModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PrdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServicePrdRecyclerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.service_prd_recycler_layout,parent,false);
        return new PrdViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PrdViewHolder holder, int position) {

        holder.binding.prdRecyclerGrm.setText(arrayList.get(position).getType());
        holder.binding.prdVariation.setText(arrayList.get(position).getTypename());
        holder.binding.prdRecyclerMaxqty.setText(arrayList.get(position).getMaxqty());
        holder.binding.prdRecyclerQty.setText(arrayList.get(position).getMinqty());
        holder.binding.prdRecyclerPrice.setText(arrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PrdViewHolder extends RecyclerView.ViewHolder {
        ServicePrdRecyclerLayoutBinding binding;

        public PrdViewHolder(@NonNull ServicePrdRecyclerLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
