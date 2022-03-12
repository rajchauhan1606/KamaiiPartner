package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.RiderMultiIdOrderRvLayoutBinding;
import com.kamaii.partner.ui.models.newbooking.NewCartAdapter;
import com.kamaii.partner.ui.models.newbooking.Order;

import java.util.List;

public class RiderMultiIdOrderAdapter extends RecyclerView.Adapter<RiderMultiIdOrderAdapter.MultiViewHolder> {

    Context context;
    List<Order> arrayList;

    public RiderMultiIdOrderAdapter(Context context, List<Order> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MultiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RiderMultiIdOrderRvLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.rider_multi_id_order_rv_layout,parent,false);
        return new MultiViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiViewHolder holder, int position) {

        holder.binding.orderId.setText(arrayList.get(position).getOrderid());
        holder.binding.totalPaymentRider.setText(Html.fromHtml("&#8377;"+arrayList.get(position).getTotpayment()));
        holder.binding.cartRvRider.setLayoutManager(new LinearLayoutManager(context));
        NewCartAdapter cartAdapter = new NewCartAdapter(context,arrayList.get(position).getProducts());
        holder.binding.cartRvRider.setAdapter(cartAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MultiViewHolder extends RecyclerView.ViewHolder {
        RiderMultiIdOrderRvLayoutBinding binding;
        public MultiViewHolder(@NonNull RiderMultiIdOrderRvLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
