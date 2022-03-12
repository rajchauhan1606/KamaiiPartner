package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.ui.models.SlotDataModel;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;

public class SlotDataAdapter extends RecyclerView.Adapter<SlotDataAdapter.SlotViewholder> {

    Context context;
    List<SlotDataModel> slotDataList;
    List<String> subProductList;
    List<String> subProductQtyList;

    public SlotDataAdapter(Context context, List<SlotDataModel> slotDataList) {
        this.context = context;
        this.slotDataList = slotDataList;


        Log.e("BOOKING_TRACK"," slot_data_list_array"+slotDataList.toString());
    }

    @NonNull
    @Override
    public SlotViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.slot_list_item_recycle_layout,parent,false);
        return new SlotViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewholder holder, int position) {
        holder.slot_item_name.setText(position+1+". "+slotDataList.get(position).getProduct_name());
        holder.slot_item_qty.setText(slotDataList.get(position).getProduct_qty());
        if (!slotDataList.get(position).getSubproduct().equalsIgnoreCase("0")){

            subProductList = new ArrayList<>();
            subProductQtyList = new ArrayList<>();
            holder.slotdata_rv.setVisibility(View.VISIBLE);

            String[] array = slotDataList.get(position).getSubproduct().split("\\|");

            for(int i=0; i< array.length; i++)
            {
                System.out.println(array[i]);
                subProductList.add(array[i]);
              //  Log.e("subProductList",""+array[i]);
            }

            String array2[] = slotDataList.get(position).getSubproductcount().split("\\|");

            for(int i=0; i< array2.length; i++)
            {
                System.out.println(array[i]);
                subProductQtyList.add(array2[i]);
                //  Log.e("subProductList",""+array[i]);
            }

            SlotSubDataAdapter slotSubDataAdapter = new SlotSubDataAdapter(context,subProductList,subProductQtyList);
            holder.slotdata_rv.setLayoutManager(new LinearLayoutManager(context));
            holder.slotdata_rv.setAdapter(slotSubDataAdapter);

        }
        else {
            holder.slotdata_rv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return slotDataList.size();
    }

    class SlotViewholder extends RecyclerView.ViewHolder {

        CustomTextViewBold slot_item_name;
        CustomTextViewBold slot_item_qty;
        RecyclerView slotdata_rv;
        public SlotViewholder(@NonNull View itemView) {
            super(itemView);

            slot_item_name = itemView.findViewById(R.id.slot_item_name);
            slot_item_qty = itemView.findViewById(R.id.slot_item_qty);
            slotdata_rv = itemView.findViewById(R.id.slotdata_rv);
        }
    }
}
