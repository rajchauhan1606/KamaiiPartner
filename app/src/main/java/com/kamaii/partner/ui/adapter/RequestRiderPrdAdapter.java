package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.List;

public class RequestRiderPrdAdapter extends RecyclerView.Adapter<RequestRiderPrdAdapter.PrdPrdRequestViewHolder> {

    Context context;
    List<ProductDTO> arrayList;

    public RequestRiderPrdAdapter(Context context, List<ProductDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PrdPrdRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_rider_prd_list_recycler_layout, parent, false);

        return new PrdPrdRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrdPrdRequestViewHolder holder, int position) {
        holder.request_dialog_prd_name.setText(arrayList.get(position).getProduct_name());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class PrdPrdRequestViewHolder extends RecyclerView.ViewHolder {

        CustomTextViewBold request_dialog_prd_name;


        public PrdPrdRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            request_dialog_prd_name = itemView.findViewById(R.id.request_dialog_prd_name);

        }
    }
}
