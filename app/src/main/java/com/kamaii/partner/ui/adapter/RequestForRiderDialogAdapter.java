package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.RequestRiderDialogRecyclerLayoutBinding;
import com.kamaii.partner.interfacess.SelectOrderForRequest;
import com.kamaii.partner.ui.models.RequestRiderDialogModel;

import java.util.ArrayList;
import java.util.List;

public class RequestForRiderDialogAdapter extends RecyclerView.Adapter<RequestForRiderDialogAdapter.RequestViewHolder> {

    Context context;
    //List<RequestRiderDialogModel> arrayList;
    List<ArtistBooking> arrayList;
    List<String> checkbox_id;
    SelectOrderForRequest selectOrderForRequest;
    boolean multi_start_preparation = false;

    public RequestForRiderDialogAdapter(Context context, List<ArtistBooking> arrayList, boolean multi_start_preparation, SelectOrderForRequest selectOrderForRequest) {
        this.context = context;
        this.arrayList = arrayList;
        this.multi_start_preparation = multi_start_preparation;
        this.selectOrderForRequest = selectOrderForRequest;
        checkbox_id = new ArrayList<>();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RequestRiderDialogRecyclerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.request_rider_dialog_recycler_layout, parent, false);
        return new RequestViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {

        //    holder.binding.requestDialogPrdName.setText(Html.fromHtml(arrayList.get(position).getProduct_name()));

        if (multi_start_preparation) {

           /* if (position == 0) {
                holder.binding.requestDialogCheckbox.setChecked(true);
                checkbox_id.add(arrayList.get(position).getId());
                selectOrderForRequest.getOrders(position, checkbox_id);

            }*/
            if (arrayList.get(position).getBooking_flag().equalsIgnoreCase("1") && arrayList.get(position).getCustomer_booking_flag().equalsIgnoreCase("1") && arrayList.get(position).getRider_booking_flag().equalsIgnoreCase("0")) {

               /* holder.binding.requestDialogCheckbox.setChecked(true);
                checkbox_id.add(arrayList.get(position).getId());
                selectOrderForRequest.getOrders(position, checkbox_id);*/
                holder.binding.requestDialogBookingId.setText(arrayList.get(position).getOrder_no2());

                holder.binding.requestDialogPrdNameRv.setLayoutManager(new LinearLayoutManager(context));
                RequestRiderPrdAdapter adapter = new RequestRiderPrdAdapter(context, arrayList.get(position).getProduct());
                holder.binding.requestDialogPrdNameRv.setAdapter(adapter);
                holder.binding.requestDialogCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkbox_id.add(arrayList.get(position).getId());
                            selectOrderForRequest.getOrders(position, checkbox_id);
                        } else {
                            if (checkbox_id.contains(arrayList.get(position).getId())) {
                                checkbox_id.remove(arrayList.get(position).getId());
                                selectOrderForRequest.getOrders(position, checkbox_id);
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

            /*if (position == 0) {
                holder.binding.requestDialogCheckbox.setChecked(true);
                checkbox_id.add(arrayList.get(position).getId());
                selectOrderForRequest.getOrders(position, checkbox_id);

            }*/
            if (arrayList.get(position).getBooking_flag().equalsIgnoreCase("3") && arrayList.get(position).getCustomer_booking_flag().equalsIgnoreCase("3") && arrayList.get(position).getRider_booking_flag().equalsIgnoreCase("0")) {


                holder.binding.requestDialogBookingId.setText(arrayList.get(position).getOrder_no2());

                holder.binding.requestDialogPrdNameRv.setLayoutManager(new LinearLayoutManager(context));
                RequestRiderPrdAdapter adapter = new RequestRiderPrdAdapter(context, arrayList.get(position).getProduct());
                holder.binding.requestDialogPrdNameRv.setAdapter(adapter);
                holder.binding.requestDialogCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkbox_id.add(arrayList.get(position).getId());
                            selectOrderForRequest.getOrders(position, checkbox_id);
                        } else {
                            if (checkbox_id.contains(arrayList.get(position).getId())) {
                                checkbox_id.remove(arrayList.get(position).getId());
                                selectOrderForRequest.getOrders(position, checkbox_id);
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

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {
        RequestRiderDialogRecyclerLayoutBinding binding;

        RequestViewHolder(RequestRiderDialogRecyclerLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
