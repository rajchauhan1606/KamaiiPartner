package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.partner.DTO.GalleryDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.ui.activity.DeepImage;
import com.kamaii.partner.ui.fragment.ImageGallery;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterGallery extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ImageGallery imageGallery;
    LayoutInflater mLayoutInflater;
    private ArrayList<GalleryDTO> gallery;
    private Context context;
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = AdapterGallery.class.getSimpleName();
    private DialogInterface dialog_book;

    public AdapterGallery(ImageGallery imageGallery, ArrayList<GalleryDTO> gallery) {
        this.imageGallery = imageGallery;
        context = imageGallery.getActivity();
        this.gallery = gallery;
        Log.e("GALLERY_ADAPTER", "" + gallery.get(0).getCategory_path());
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery, parent, false);
        return new MyViewHolder(view);
        //}
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        Glide
                .with(context)
                .load(gallery.get(position).getImage())
                .placeholder(R.drawable.single_logo)
                .into(myViewHolder.iv_bottom_foster);

        myViewHolder.iv_bottom_foster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeepImage.class);
                intent.putExtra("imaging_id", parms.put(Consts.ID, gallery.get(position).getId()));
                intent.putExtra("imaging", gallery.get(position).getImage());
                intent.putExtra("user_id", gallery.get(position).getUser_id());
                context.startActivity(intent);
//                        imageGallery.showImg(gallery.get(position).getImage());
            }
        });

        myViewHolder.category_path.setText(Html.fromHtml(gallery.get(position).getCategory_path()));
        myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parms.put(Consts.ID, gallery.get(position).getId());
                parms.put(Consts.USER_ID, gallery.get(position).getUser_id());
                deleteDialog();
            }
        });



    }

    @Override
    public int getItemCount() {
        return gallery.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_bottom_foster, ivDelete;
        CustomTextViewBold category_path;

        public MyViewHolder(View view) {
            super(view);

            iv_bottom_foster = (ImageView) itemView.findViewById(R.id.iv_bottom_foster);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            category_path = itemView.findViewById(R.id.txt_contain);


        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        public ViewHolder1(View v) {
            super(v);
        }
    }


    public void deleteDialog() {
        try {
            new AlertDialog.Builder(context)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(context.getResources().getString(R.string.delete_gallery))
                    .setMessage(context.getResources().getString(R.string.delete_gallery_msg))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            deleteGallery();

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

    public void deleteGallery() {
        new HttpsRequest(Consts.DELETE_GALLERY_API, parms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    imageGallery.getParentData();
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

}