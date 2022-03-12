package com.kamaii.partner.ui.adapter.newbooking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.TotalOrderRvLayoutBinding;
import com.kamaii.partner.ui.models.newbooking.LunchDinnerDatum;

import java.util.List;

public class AdapterTotalOrders extends RecyclerView.Adapter<AdapterTotalOrders.TotalOrderViewHolder> {

    Context context;
    List<LunchDinnerDatum> arrayList;
    OrderDetailsAdapter adapter;
    boolean flag = true;
    String kono_data;
    public AdapterTotalOrders(Context context, List<LunchDinnerDatum> arrayList,String kono_data) {
        this.context = context;
        this.arrayList = arrayList;
        this.kono_data = kono_data;
        Log.e("kono data flag:-- ",""+kono_data);
    }

    @NonNull
    @Override
    public TotalOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            TotalOrderRvLayoutBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.total_order_rv_layout, parent, false);

            return  new TotalOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalOrderViewHolder holder, int position) {

        if (arrayList.size()==1 || position == arrayList.size()-1){
            holder.binding.viewTotalOrder.setVisibility(View.GONE);
        }
        if (kono_data.equalsIgnoreCase("0")){
            holder.binding.prdName.setText(arrayList.get(position).getServiceName()+" x ");
            holder.binding.prdQty.setText(arrayList.get(position).getQty());

            if (arrayList.get(position).getSmallDescription().equalsIgnoreCase("")){

                holder.binding.orderProducts.setVisibility(View.GONE);
            }else {
                holder.binding.orderProducts.setVisibility(View.VISIBLE);
                holder.binding.orderProducts.setText(arrayList.get(position).getSmallDescription());
            }
            holder.binding.orderdetailsRv.setLayoutManager(new LinearLayoutManager(context));
            OrderDetailsAdapter adapter = new OrderDetailsAdapter(context,arrayList.get(position).getSlot());
            holder.binding.orderdetailsRv.setAdapter(adapter);
            holder.binding.totalRvLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (flag){
                        Log.e("hhhh","123");
                        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)holder.binding.forwardArraowImgRel.getLayoutParams();
                        relativeParams.setMargins(0, 0, 0, 0);  // left, top, right, bottom
                        holder.binding.forwardArraowImgRel.setLayoutParams(relativeParams);
                        holder.binding.forwardArraowImg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_down));
                        holder.binding.orderdetailsRv.setVisibility(View.VISIBLE);
                        flag = false;
                    }else {
                        Log.e("hhhh","678");
                        flag = true;
                        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)holder.binding.forwardArraowImgRel.getLayoutParams();
                        relativeParams.setMargins(0, 5, 0, 0);  // left, top, right, bottom
                        holder.binding.forwardArraowImgRel.setLayoutParams(relativeParams);
                        holder.binding.forwardArraowImg.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_right));
                        holder.binding.orderdetailsRv.setVisibility(View.GONE);

                    }
                }
            });
        }
        else {

            holder.binding.orderProducts.setVisibility(View.GONE);
            holder.binding.prdName.setText(arrayList.get(position).getServiceName()+" x ");
            holder.binding.prdQty.setText(arrayList.get(position).getQty());

        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class TotalOrderViewHolder extends RecyclerView.ViewHolder {
        TotalOrderRvLayoutBinding binding;
        public TotalOrderViewHolder(@NonNull  TotalOrderRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
