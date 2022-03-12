package com.kamaii.partner.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.RequestRiderDialogRecyclerLayoutBinding;
import com.kamaii.partner.interfacess.GetCheckedItem;
import com.kamaii.partner.interfacess.SelectOrderForRequest;

import java.util.ArrayList;
import java.util.List;

public class PartnerConfirmationAdapter extends RecyclerView.Adapter<PartnerConfirmationAdapter.ConfirmationViewholder> {

    Context context;
    //List<RequestRiderDialogModel> arrayList;
    List<ArtistBooking> arrayList;
    List<String> checkbox_id;
    GetCheckedItem selectOrderForRequest;
    boolean multi_start_preparation = false;


    public PartnerConfirmationAdapter(Context context, List<ArtistBooking> arrayList, boolean multi_start_preparation,GetCheckedItem getCheckedItem) {
        this.context = context;
        this.arrayList = arrayList;
        this.selectOrderForRequest = getCheckedItem;
        this.multi_start_preparation = multi_start_preparation;
        checkbox_id = new ArrayList<>();
    }

    @NonNull
    @Override
    public ConfirmationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestRiderDialogRecyclerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.request_rider_dialog_recycler_layout, parent, false);
        return new ConfirmationViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmationViewholder holder, @SuppressLint("RecyclerView") int position) {

        //    holder.binding.requestDialogPrdName.setText(Html.fromHtml(arrayList.get(position).getProduct_name()));

        Log.e("confirmation_for_start", "confirmation_for_start 300: 1 : "+arrayList.get(0).getOrder_no2());

        if (multi_start_preparation) {
            Log.e("confirmation_for_start", "confirmation_for 111");

           /* if (position == 0) {
                holder.binding.requestDialogCheckbox.setChecked(true);
                checkbox_id.add(arrayList.get(position).getId());
                selectOrderForRequest.getCheckedItems(position, checkbox_id);

            }*/
            if (arrayList.get(position).getBooking_flag().equalsIgnoreCase("1") && arrayList.get(position).getCustomer_booking_flag().equalsIgnoreCase("1") && arrayList.get(position).getRider_booking_flag().equalsIgnoreCase("0")) {

                Log.e("confirmation_for_start", "confirmation_for 112");

               /* holder.binding.requestDialogCheckbox.setChecked(true);
                checkbox_id.add(arrayList.get(position).getId());
                selectOrderForRequest.getCheckedItems(position, checkbox_id);*/
                holder.binding.requestDialogBookingId.setText(arrayList.get(position).getOrder_no2());

                holder.binding.requestDialogPrdNameRv.setLayoutManager(new LinearLayoutManager(context));
                RequestRiderPrdAdapter adapter = new RequestRiderPrdAdapter(context, arrayList.get(position).getProduct());
                holder.binding.requestDialogPrdNameRv.setAdapter(adapter);
                holder.binding.requestDialogCheckbox.setChecked(true);
                if (!checkbox_id.contains(arrayList.get(position).getId())) {

                    checkbox_id.add(arrayList.get(position).getId());
                    selectOrderForRequest.getCheckedItems(position, checkbox_id);
                }
                holder.binding.requestDialogCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (!checkbox_id.contains(arrayList.get(position).getId())) {

                                checkbox_id.add(arrayList.get(position).getId());
                            }
                            selectOrderForRequest.getCheckedItems(position, checkbox_id);
                        } else {
                            if (checkbox_id.contains(arrayList.get(position).getId())) {
                                checkbox_id.remove(arrayList.get(position).getId());
                                selectOrderForRequest.getCheckedItems(position, checkbox_id);
                            }
                        }
                    }
                });
                //holder.binding.
            } else {

                holder.binding.requestDialogCheckbox.setVisibility(View.GONE);
                holder.binding.requestDialogPrdName.setVisibility(View.GONE);
                holder.binding.requestDialogPrdNameRv.setVisibility(View.GONE);
                holder.binding.requestDialogBookingId.setVisibility(View.GONE);
                holder.binding.requestRiderLine.setVisibility(View.GONE);
            }
        } else {

            Log.e("confirmation_for_start", "confirmation_for_start 222");

            /*if (position == 0) {
                holder.binding.requestDialogCheckbox.setChecked(true);
                checkbox_id.add(arrayList.get(position).getId());
                selectOrderForRequest.getCheckedItems(position, checkbox_id);

            }*/
            if (arrayList.get(position).getBooking_flag().equalsIgnoreCase("3") && arrayList.get(position).getCustomer_booking_flag().equalsIgnoreCase("3") && arrayList.get(position).getRider_booking_flag().equalsIgnoreCase("0")) {


                holder.binding.requestDialogBookingId.setText(arrayList.get(position).getId());

                holder.binding.requestDialogPrdNameRv.setLayoutManager(new LinearLayoutManager(context));
                RequestRiderPrdAdapter adapter = new RequestRiderPrdAdapter(context, arrayList.get(position).getProduct());
                holder.binding.requestDialogPrdNameRv.setAdapter(adapter);
                holder.binding.requestDialogCheckbox.setChecked(true);
                if (!checkbox_id.contains(arrayList.get(position).getId())) {

                    checkbox_id.add(arrayList.get(position).getId());
                    selectOrderForRequest.getCheckedItems(position, checkbox_id);
                }
                holder.binding.requestDialogCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (!checkbox_id.contains(arrayList.get(position).getId())) {

                                checkbox_id.add(arrayList.get(position).getId());
                            }
                            selectOrderForRequest.getCheckedItems(position, checkbox_id);
                        } else {
                            if (checkbox_id.contains(arrayList.get(position).getId())) {
                                checkbox_id.remove(arrayList.get(position).getId());
                                selectOrderForRequest.getCheckedItems(position, checkbox_id);
                            }
                        }
                    }
                });
                //holder.binding.
            } else {

                holder.binding.requestDialogCheckbox.setVisibility(View.GONE);
                holder.binding.requestDialogPrdName.setVisibility(View.GONE);
                holder.binding.requestDialogPrdNameRv.setVisibility(View.GONE);
                holder.binding.requestDialogBookingId.setVisibility(View.GONE);
                holder.binding.requestRiderLine.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ConfirmationViewholder extends RecyclerView.ViewHolder {
        RequestRiderDialogRecyclerLayoutBinding binding;

        public ConfirmationViewholder(RequestRiderDialogRecyclerLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
