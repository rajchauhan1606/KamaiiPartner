package com.kamaii.partner.ui.adapter.newbooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.NewOrderRequestRecyclerSubLayoutBinding;
import com.kamaii.partner.ui.adapter.OrderItemsAdapter;
import com.kamaii.partner.ui.models.newbooking.PendingDatum;

import java.util.List;

public class AdapterNewOrderRequest extends RecyclerView.Adapter<AdapterNewOrderRequest.OrderViewHolder> {

    Context context;
    List<PendingDatum> arrayList;

    public AdapterNewOrderRequest(Context context, List<PendingDatum> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewOrderRequestRecyclerSubLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.new_order_request_recycler_sub_layout,parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.binding.newOrderTitle.setText(arrayList.get(position).getHead());
        holder.binding.newOrderRequestItemRv.setLayoutManager(new LinearLayoutManager(context));
        OrderItemsAdapter adapter = new OrderItemsAdapter(context,arrayList.get(position).getProducts());
        holder.binding.newOrderRequestItemRv.setAdapter(adapter);

        if (position == 0){
            holder.binding.newOrderTmpView1.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        NewOrderRequestRecyclerSubLayoutBinding binding;
        public OrderViewHolder(@NonNull  NewOrderRequestRecyclerSubLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
