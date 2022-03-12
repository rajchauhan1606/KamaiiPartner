/*
 * *
 *  * Created by WebKnight Infosystem on 14/5/20 10:52 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 14/5/20 10:37 AM
 *
 */

package com.webknight.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.webknight.filemanager.R;
import com.webknight.filemanager.filter.entity.Directory;

import java.util.ArrayList;

public class FolderListAdapter extends BaseAdapter<Directory, FolderListAdapter.FolderListViewHolder> {
    private FolderListListener mListener;

    public FolderListAdapter(Context ctx, ArrayList<Directory> list) {
        super(ctx, list);
    }

    @Override
    public FolderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.vw_layout_item_folder_list,
                parent, false);
        return new FolderListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FolderListViewHolder holder, int position) {
        holder.mTvTitle.setText(mList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFolderListClick(mList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FolderListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle;

        public FolderListViewHolder(View itemView) {
            super(itemView);

            mTvTitle = itemView.findViewById(R.id.tv_folder_title);
        }
    }

    public interface FolderListListener {
        void onFolderListClick(Directory directory);
    }

    public void setListener(FolderListListener listener) {
        this.mListener = listener;
    }
}
