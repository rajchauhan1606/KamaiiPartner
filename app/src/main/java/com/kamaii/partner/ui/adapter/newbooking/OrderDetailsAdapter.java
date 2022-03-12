package com.kamaii.partner.ui.adapter.newbooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.OrderDetailsRecyclerLayoutBinding;
import com.kamaii.partner.ui.models.newbooking.Slot;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetails> {

    Context context;
    List<Slot> arrayList;

    public OrderDetailsAdapter(Context context, List<Slot> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public OrderDetails onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailsRecyclerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_details_recycler_layout,parent,false);
        return new OrderDetails(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetails holder, int position) {

        holder.binding.slotTime.setText(arrayList.get(position).getSlot());
        holder.binding.slotQty.setText(arrayList.get(position).getQty());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrderDetails extends RecyclerView.ViewHolder {
        OrderDetailsRecyclerLayoutBinding binding;

        public OrderDetails(@NonNull OrderDetailsRecyclerLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
