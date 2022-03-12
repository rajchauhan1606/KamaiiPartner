package com.kamaii.partner.ui.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.SlotDataLayoutBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.GetTimeSlotData;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.ui.models.SlotDataModel;
import com.kamaii.partner.ui.models.TimeSlotModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewholder> {

    Context context;
    List<TimeSlotModel> arrayList;
    List<SlotDataModel> slotDataList;
    List<Integer> colorRelativeLayoutList;
    List<Integer> colorTextViewList;
    int tracker = 0;
    SlotDataLayoutBinding binding1;
    HashMap<String, String> slotMap;
    boolean click_flag = false;
    int pos = 0;
    GetTimeSlotData getTimeSlotData;


    public TimeSlotAdapter(Context context, List<TimeSlotModel> arrayList, GetTimeSlotData getTimeSlotData) {
        this.context = context;
        this.arrayList = arrayList;
        this.getTimeSlotData = getTimeSlotData;
        colorRelativeLayoutList = new ArrayList<Integer>();
        colorTextViewList = new ArrayList<>();
        slotDataList = new ArrayList<>();
        slotMap = new HashMap<>();
    }

    @NonNull
    @Override
    public TimeSlotViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_timeslot_recycle_layout, parent, false);
        return new TimeSlotViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewholder holder, int position) {


       // Log.e("SLOT_DATA", "" + arrayList.get(position).getOrder_id());

      /*  colorRelativeLayoutList.add(R.drawable.slot_1_bg);
        colorRelativeLayoutList.add(R.drawable.slot_2_bg);
        colorRelativeLayoutList.add(R.drawable.slot_3_bg);
        colorRelativeLayoutList.add(R.drawable.slot_advance_bg);

        colorTextViewList.add(R.color.green_slide_color);
        colorTextViewList.add(R.color.mapbox_blue);
        colorTextViewList.add(R.color.pink_color);
        colorTextViewList.add(R.color.yellow_color);


        if (position == 0) {
            tracker = 0;
        }*/
        holder.timeslot_txt.setText(arrayList.get(position).getDate_time());
        holder.timeslot_view.setText(arrayList.get(position).getDate_time());
        if (pos == position){
            holder.timeslot_view.setVisibility(View.VISIBLE);
            holder.timeslot_txt.setTextColor(context.getResources().getColor(R.color.yellow_color));
        }else {
            holder.timeslot_view.setVisibility(View.GONE);
            holder.timeslot_txt.setTextColor(context.getResources().getColor(R.color.white));

        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = position;
                notifyDataSetChanged();
                getTimeSlotData.getTimeSlotData(arrayList.get(position).getDate_time_time(),arrayList.get(position).getDate_time_date(),arrayList.get(position).getDate_time_date2());
                Log.e("getTimeSlotWiseBo_params",""+" "+ "date_time_time"+arrayList.get(position).getDate_time_time()+" "+"date_time_date"+arrayList.get(position).getDate_time_date());

            }
        });
     /*   if (arrayList.get(position).getSlotname().equalsIgnoreCase("Slot 2")) {

            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.slot_2_bg));
            holder.timeslot_txt.setTextColor(context.getResources().getColor(R.color.mapbox_blue));

        } else if (arrayList.get(position).getSlotname().equalsIgnoreCase("Slot 3")) {

            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.slot_3_bg));
            holder.timeslot_txt.setTextColor(context.getResources().getColor(R.color.pink_color));

        } else if (arrayList.get(position).getSlotname().equalsIgnoreCase("Future")) {

            holder.timeslot_txt.setText("Advance");
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.slot_advance_bg));
            holder.timeslot_txt.setTextColor(context.getResources().getColor(R.color.yellow_color));

        } else {
            holder.relativeLayout.setBackground(context.getResources().getDrawable(colorRelativeLayoutList.get(tracker)));
            holder.timeslot_txt.setTextColor(context.getResources().getColor(colorTextViewList.get(tracker)));

        }*/


        //  holder.relativeLayout.setBackground(context.getResources().getDrawable(colorRelativeLayoutList.get(tracker)));
        //   holder.timeslot_txt.setTextColor(context.getResources().getColor(colorTextViewList.get(tracker)));

       /* if (tracker == colorRelativeLayoutList.size() - 1) {
            tracker = 0;
        } else {
            tracker++;
        }*/


      /*  holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   if (!click_flag){

                    click_flag = true;

                    int p = position;
                    Log.e("SLOT_DATA", "2" + " position: " + p + " " + arrayList.get(p).getOrder_id());


                    slotMap.put("Order_id", arrayList.get(position).getOrder_id());
                    slotMap.put("slotname", arrayList.get(position).getSlotname());
                    slotMap.put("slotno", arrayList.get(position).getSlotno());

                    Log.e("SLOT_DATA", "" + slotMap.toString());

                    new HttpsRequest(Consts.GET_SLOT_DETAILS_API, slotMap, context).stringPosttwo("Tag", new Helper() {
                        @Override
                        public void backResponse(boolean flag, String msg, JSONObject response) {

                            if (flag) {
                                Log.e("SLOT_DATA", "" + response.toString());

                                try {

                                    Type getType = new TypeToken<List<SlotDataModel>>() {
                                    }.getType();

                                    slotDataList = new Gson().fromJson(response.getJSONArray("data").toString(), getType);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                Dialog alertDialog = new Dialog(context);
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                                binding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slot_data_layout, null, false);
                                alertDialog.setContentView(binding1.getRoot());
//                            alertDialog.setCancelable(false);

                                CustomTextViewBold dialog_title = alertDialog.findViewById(R.id.slot_title);
                                ImageView close_slot_dialog = alertDialog.findViewById(R.id.close_slot_dialog);

                                dialog_title.setText(arrayList.get(position).getSlotname() + " - " + "Order Details");
                                close_slot_dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });

                                RecyclerView slotdata_recyclerview = alertDialog.findViewById(R.id.slotdata_recyclerview);
                                SlotDataAdapter slotDataAdapter = new SlotDataAdapter(context, slotDataList);
                                slotdata_recyclerview.setLayoutManager(new LinearLayoutManager(context));
                                slotdata_recyclerview.setAdapter(slotDataAdapter);

                                alertDialog.show();
                            }
                        }
                    });

                }

          //  }
        });
*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class TimeSlotViewholder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        CustomTextViewBold timeslot_txt,timeslot_view;

        public TimeSlotViewholder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.slot_relative);
            timeslot_txt = itemView.findViewById(R.id.timeslot_txt);
            timeslot_view = itemView.findViewById(R.id.timeslot_view);
        }
    }
}
