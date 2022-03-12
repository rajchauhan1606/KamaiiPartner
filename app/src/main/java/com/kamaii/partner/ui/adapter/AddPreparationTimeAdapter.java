package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.AddPrepTime;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.List;

public class AddPreparationTimeAdapter extends RecyclerView.Adapter<AddPreparationTimeAdapter.PreparationViewholder> {

    Context context;
    List<String> arraylist;
    int pos = -1;
    AddPrepTime addPrepTime;
    public AddPreparationTimeAdapter(Context context, List<String> arraylist,AddPrepTime addPrepTime) {
        this.context = context;
        this.arraylist = arraylist;
        this.addPrepTime = addPrepTime;
    }

    @NonNull
    @Override
    public PreparationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_timer_recycler_layout, parent, false);
        return new PreparationViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreparationViewholder holder, int position) {

        holder.add_time_recycle_txt.setText(arraylist.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos = position;
                addPrepTime.getTime(arraylist.get(position));
                notifyDataSetChanged();
            }
        });

        if (pos == position){
            holder.time_recycler_main_relative.setBackground(context.getResources().getDrawable(R.drawable.curved_5dp_green_bg));
            holder.add_time_recycle_txt.setTextColor(context.getResources().getColor(R.color.white));
        }
        else {
            holder.time_recycler_main_relative.setBackground(context.getResources().getDrawable(R.drawable.curved_5dp_grey_bg));
            holder.add_time_recycle_txt.setTextColor(context.getResources().getColor(R.color.dark_blue_color));
        }
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class PreparationViewholder extends RecyclerView.ViewHolder {
        CustomTextViewBold add_time_recycle_txt;
        RelativeLayout time_recycler_main_relative;
        public PreparationViewholder(@NonNull View itemView) {
            super(itemView);

            add_time_recycle_txt = itemView.findViewById(R.id.add_time_recycle_txt);
            time_recycler_main_relative = itemView.findViewById(R.id.time_recycler_main_relative);
        }
    }
}
