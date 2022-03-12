package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnAddressSelected;
import com.kamaii.partner.ui.activity.EditPersnoalInfo;
import com.kamaii.partner.ui.models.TypeAddressModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.List;

public class AddressBottomSheetAdapter extends RecyclerView.Adapter<com.kamaii.partner.ui.adapter.AddressBottomSheetAdapter.AddressViewholder> {

    Context context;
    List<TypeAddressModel> homeList;
    OnAddressSelected onAddressSelected;

    public AddressBottomSheetAdapter(Context context, List<TypeAddressModel> homeList, OnAddressSelected onAddressSelected) {
        this.context = context;
        this.homeList = homeList;
        this.onAddressSelected = onAddressSelected;
    }

    @NonNull
    @Override
    public AddressViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_recycle_layout, parent, false);
        return new AddressViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewholder holder, int position) {

        holder.add_type.setText(homeList.get(position).getAdd_type());

        if (homeList.get(position).getHouse_no().equalsIgnoreCase("") ||
                homeList.get(position).getSociety_name().equalsIgnoreCase("")){

            if (homeList.get(position).getHouse_no().equalsIgnoreCase("") && homeList.get(position).getSociety_name().equalsIgnoreCase("")){
                holder.address_txt.setText(homeList.get(position).getStreet_address());

                //etLocationD.setText(street_address_str);
            }
            else if (homeList.get(position).getHouse_no().equalsIgnoreCase("")){
                holder.address_txt.setText(homeList.get(position).getSociety_name() + ", " + homeList.get(position).getStreet_address());

                //etLocationD.setText(society_name_str + ", " + street_address_str);
            }
            else if (homeList.get(position).getSociety_name().equalsIgnoreCase("")){
                holder.address_txt.setText(homeList.get(position).getHouse_no() + ", " + homeList.get(position).getStreet_address());

               // etLocationD.setText(house_no_str + ", "+ street_address_str);
            }
        }
        else {
            holder.address_txt.setText(homeList.get(position).getHouse_no() + ", " + homeList.get(position).getSociety_name() + ", " + homeList.get(position).getStreet_address());
        }

        if (position == homeList.size() - 1) {
            holder.line.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressSelected.setAddress(position, homeList.get(position).getAddress_id(),homeList.get(position).getHouse_no(), homeList.get(position).getStreet_address(), homeList.get(position).getSociety_name());
            }
        });

        holder.delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditPersnoalInfo) context).deleteaddress(homeList.get(position).getAddress_id());
                int newPosition = holder.getAdapterPosition();
                homeList.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, homeList.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    class AddressViewholder extends RecyclerView.ViewHolder {

        CustomTextViewBold add_type;
        CustomTextView address_txt;
        TextView line;
        ImageView delete_address;
        public AddressViewholder(@NonNull View itemView) {
            super(itemView);

            add_type = itemView.findViewById(R.id.add_type);
            address_txt = itemView.findViewById(R.id.address_txt);
            line = itemView.findViewById(R.id.add_recycle_view);
            delete_address = itemView.findViewById(R.id.delete_address);
        }
    }
}
