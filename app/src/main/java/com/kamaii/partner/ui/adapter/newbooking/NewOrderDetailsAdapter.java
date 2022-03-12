package com.kamaii.partner.ui.adapter.newbooking;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.OrderDetailsNewRvLayoutBinding;
import com.kamaii.partner.ui.models.newbooking.NewOrderDetailsModel;

import java.util.List;

public class NewOrderDetailsAdapter extends RecyclerView.Adapter<NewOrderDetailsAdapter.OrderViewHolder> {


    Context context;
    List<NewOrderDetailsModel> arrayList;


    public NewOrderDetailsAdapter(Context context, List<NewOrderDetailsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailsNewRvLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_details_new_rv_layout,parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        if (arrayList.get(position).getOrderflag().equalsIgnoreCase("4")){
            holder.binding.clock123First.setImageResource(R.drawable.ic_payment_done_green);
            holder.binding.orderStatusFirst.setText("Order Completed");
            holder.binding.tempView1First.setBackground(context.getResources().getDrawable(R.color.green));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_green_bg));
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.green_border_bg));
        }else if(arrayList.get(position).getOrderflag().equalsIgnoreCase("2")){
            holder.binding.clock123First.setImageResource(R.drawable.cancel);
            holder.binding.orderStatusFirst.setText("Order Cancel");
            holder.binding.tempView1First.setBackground(context.getResources().getDrawable(R.color.red));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_red_bg));
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.red_border_bg));
        }

        holder.binding.orderId.setText(arrayList.get(position).getOrderid());
        holder.binding.orderDetailsTime.setText(arrayList.get(position).getOrderdatetime());
        holder.binding.deliveryDateTime.setText(arrayList.get(position).getDeliverydatetime());
        holder.binding.totalPaymentFirst.setText(Html.fromHtml("&#8377;")+arrayList.get(position).getTotalamount());

        holder.binding.orderDetailsCartRvFirst.setLayoutManager(new LinearLayoutManager(context));

        AdapterOrderDetailsCart cart = new AdapterOrderDetailsCart(context,arrayList.get(position).getProductsArray());
        holder.binding.orderDetailsCartRvFirst.setAdapter(cart);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        OrderDetailsNewRvLayoutBinding binding;
        public OrderViewHolder(@NonNull OrderDetailsNewRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
