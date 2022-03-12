package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnDataErrorListener;
import com.kamaii.partner.interfacess.OnSelectedItemListener;
import com.kamaii.partner.ui.activity.ChooseServiceActivity;
import com.kamaii.partner.ui.fragment.Services;
import com.kamaii.partner.ui.models.CategoryModel;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class AdapterCategoryServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Services services;
    LayoutInflater mLayoutInflater;
    private ArrayList<CategoryModel> productDTOList;
    private Context context;
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = AdapterCategoryServices.class.getSimpleName();
    private DialogInterface dialog_book;
    private OnSelectedItemListener onItemListener;

    private OnDataErrorListener dataErrorListener;
    public AdapterCategoryServices(Services services, ArrayList<CategoryModel> productDTOList, OnSelectedItemListener onItemListener) {
        this.services = services;
        context = services.getActivity();
        this.productDTOList = productDTOList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_camera, parent, false);
            return new ViewHolder1(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case TYPE_CAMERA:
                final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //services.addServices();
                        context.startActivity(new Intent(context, ChooseServiceActivity.class));
                    }
                });
                break;

            case TYPE_NORMAL:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                final int pos = position - 1;
                if (productDTOList.get(pos).getCat_name().equalsIgnoreCase("")){
                    myViewHolder.itemView.setVisibility(View.GONE);
                }
                myViewHolder.CTVproductname.setText(productDTOList.get(pos).getCat_name());

                Glide.with(context).
                        load(productDTOList.get(pos).getImage())
                        .placeholder(R.drawable.bg)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(myViewHolder.IVproduct);

                myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        onItemListener.setOnClick("",pos,"","","");
                    }
                });
                break;

        }

    }

    @Override
    public int getItemCount() {
        return productDTOList.size()+1;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView IVproduct;
        public TextView CTVproductname;


        public MyViewHolder(View view) {
            super(view);

            IVproduct = (ImageView) itemView.findViewById(R.id.image_view_item_category);
            CTVproductname = itemView.findViewById(R.id.text_view_item_category);

        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        public ViewHolder1(View v) {
            super(v);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_NORMAL;
        }
    }
    public void deleteDialog() {
        try {
            new AlertDialog.Builder(context)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(context.getResources().getString(R.string.delete_service))
                    .setMessage(context.getResources().getString(R.string.delete_service_msg))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            //deleteGallery();

                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}