package com.kamaii.partner.ui.adapter;

import android.content.Context;
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
import com.kamaii.partner.model.TvideoModel;

import java.util.ArrayList;
import java.util.List;

public class TvideoAdapter extends RecyclerView.Adapter<TvideoAdapter.ViewHolder>  {

    private List<TvideoModel> newsPaperModelList;
    private Context context;
    private ArrayList<Integer> mSectionPositions;
    private OnSelectedItemListener onSelectedItemListener;
    private OnDataErrorListener dataErrorListener;


    public TvideoAdapter(Context context, List<TvideoModel> newsPaperModelList, OnSelectedItemListener onSelectedItemListener) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tvideo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        final int i=position;
        holder.txt_contain.setText(newsPaperModelList.get(position).getName());
        //Glide.with(this.context).load(newsPaperModelList.get(position).getUrl()).into(holder.img_sub_cat);
        String thumbnailMq = "http://img.youtube.com/vi/"+newsPaperModelList.get(position).getImage()+"/mqdefault.jpg" ;

        Glide.with(this.context).load(thumbnailMq).into(holder.img_sub_cat);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItemListener.setOnClick("",i,newsPaperModelList.get(position).getUrl(),"","");

            }
        });


    }





    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_contain;
        ImageView img_sub_cat;
        LinearLayout item_layout;
        ViewHolder(View itemView) {
            super(itemView);
            txt_contain = itemView.findViewById(R.id.txt_contain);
            img_sub_cat = itemView.findViewById(R.id.img_sub_cat);

            //txt_contain.setTypeface(AppController.getInstance().getTypeface("Poppins-SemiBold.otf"));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}

