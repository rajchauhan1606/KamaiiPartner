package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.ui.fragment.SelectServicesFragment;
import com.kamaii.partner.ui.models.ServiceListModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceAdapter extends BaseAdapter {

    Context context;
    List<ServiceListModel> arraylist;
    public static int check_status = -1;
    List<String> checkBoxId;
    public SelectServiceAdapter(Context context, List<ServiceListModel> arraylist,int check_status) {
        this.context = context;
        this.arraylist = arraylist;
        this.check_status = check_status;
        checkBoxId = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.service_list_recycle_layout,null);

        CustomTextViewBold prd_name,prd_price;
        CustomTextView prd_mrp;
        CheckBox checkBox_img;

        prd_name = view.findViewById(R.id.service_recycle_name);
        prd_price = view.findViewById(R.id.select_service_tvPrice);
        //prd_mrp = view.findViewById(R.id.select_service_tvDiscountPrice);
        checkBox_img = view.findViewById(R.id.select_checkbox);

        prd_name.setText(arraylist.get(position).getProduct_name());
        prd_price.setText(arraylist.get(position).getPrice());

        checkBox_img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBoxId.add(arraylist.get(position).getId());
                }
                else {
                   checkBoxId.remove(arraylist.get(position).getId());
                }
            }
        });
        CheckBox c = view.findViewById(R.id.select_all_checkbox);

        return view;
    }
}
