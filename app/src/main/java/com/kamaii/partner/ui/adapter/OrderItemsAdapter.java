package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.OrderItemsRecyclerItemsBinding;
import com.kamaii.partner.ui.models.ProductArrayModel;
import com.kamaii.partner.ui.models.SlotDataModel;
import com.kamaii.partner.ui.models.newbooking.Product;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemsViewHolder> {

    Context context;
    List<Product> arrayList;

    public OrderItemsAdapter(Context context, List<Product> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public OrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemsRecyclerItemsBinding view = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.order_items_recycler_items,parent,false);
        return new OrderItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsViewHolder holder, int position) {

        holder.binding.itemName.setText(arrayList.get(position).getProductName()+" x ");
        holder.binding.itemQty.setText(arrayList.get(position).getQty());
        holder.binding.orderAmt.setText(Html.fromHtml("&#8377;")+arrayList.get(position).getOrderAmount());
        holder.binding.deliveryTime.setText(arrayList.get(position).getDeliveryTime());
        holder.binding.preparationTime.setText(arrayList.get(position).getPreprationTime());
        if (position == arrayList.size()-1){

            holder.binding.orderLineView2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrderItemsViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold item_name,item_amount;
        TextView order_line_view2;
        OrderItemsRecyclerItemsBinding binding;
        public OrderItemsViewHolder(@NonNull OrderItemsRecyclerItemsBinding itemView) {
            super(itemView.getRoot());
           binding = itemView;
        }
    }
}
