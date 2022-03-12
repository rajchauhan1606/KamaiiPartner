package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnDataErrorListener;
import com.kamaii.partner.interfacess.OnSelectedItemListener;
import com.kamaii.partner.ui.models.CategoryModel;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;


public class AdapterCategoryShippingServices extends RecyclerView.Adapter<AdapterCategoryShippingServices.ViewHolder> {

    private List<CategoryModel> newsPaperModelList;
    private Context context;
    private ArrayList<Integer> mSectionPositions;
    private OnSelectedItemListener onSelectedItemListener;
    private OnDataErrorListener dataErrorListener;

    //int[] rainbow;
    public AdapterCategoryShippingServices(Context context, List<CategoryModel> newsPaperModelList, OnSelectedItemListener onSelectedItemListener) {
        this.context = context;
        this.newsPaperModelList = newsPaperModelList;

        this.onSelectedItemListener = onSelectedItemListener;


        //  rainbow = context.getResources().getIntArray(R.array.gradientcolors);
    }

    @Override
    public int getItemCount() {
        return newsPaperModelList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cate_shipping, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        final int i=position;

        Log.e("newsPaperModelList",":--- "+newsPaperModelList.get(position).getIs_map());

        holder.txt_contain.setText(newsPaperModelList.get(position).getCat_name());
        holder.range.setText(newsPaperModelList.get(position).getKmrange());
        Glide.with(this.context).load(newsPaperModelList.get(position).getImage()).into(holder.img_sub_cat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                onSelectedItemListener.setOnClick("",i,"","","");
            }
        });


    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_contain;
        ImageView img_sub_cat;
        LinearLayout item_layout;
        CustomTextViewBold range;
        ViewHolder(View itemView) {
            super(itemView);
            txt_contain = itemView.findViewById(R.id.text_view_item_category);
            img_sub_cat = itemView.findViewById(R.id.image_view_item_category);
            range = itemView.findViewById(R.id.range);

            //txt_contain.setTypeface(AppController.getInstance().getTypeface("Poppins-SemiBold.otf"));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

