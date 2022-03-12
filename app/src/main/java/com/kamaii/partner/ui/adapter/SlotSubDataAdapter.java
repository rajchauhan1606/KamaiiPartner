package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.List;

public class SlotSubDataAdapter extends RecyclerView.Adapter<SlotSubDataAdapter.SlotSubViewHolder> {

    Context context;
    List<String> arraylist;
    List<String> arraylist2;

    public SlotSubDataAdapter(Context context, List<String> arraylist, List<String> arraylist2) {
        this.context = context;
        this.arraylist = arraylist;
        this.arraylist2 = arraylist2;
    }

    @NonNull
    @Override
    public SlotSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slotsubdata_recycle_layout,parent,false);
        return new SlotSubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotSubViewHolder holder, int position) {
        holder.slot_subdata_item_name.setText(arraylist.get(position));
        holder.slotsub_item_qty.setText(arraylist2.get(position));
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class SlotSubViewHolder extends RecyclerView.ViewHolder {

        CustomTextViewBold slot_subdata_item_name;
        CustomTextViewBold slotsub_item_qty;

        public SlotSubViewHolder(@NonNull View itemView) {
            super(itemView);

            slot_subdata_item_name = itemView.findViewById(R.id.slot_subdata_item_name);
            slotsub_item_qty = itemView.findViewById(R.id.slotsub_item_qty);
        }
    }
}
