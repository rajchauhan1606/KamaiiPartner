package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnReasonSelectedListener;
import com.kamaii.partner.ui.models.CancelReasonModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;

public class ReasonAdapter extends BaseAdapter {

    Context context;
    List<CancelReasonModel> arrayList;
    OnReasonSelectedListener reasonSelectedListener;
    List<CheckBox> checkBoxList;
    int pos = -1;
    public ReasonAdapter(Context context, List<CancelReasonModel> arrayList,OnReasonSelectedListener reasonSelectedListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.reasonSelectedListener = reasonSelectedListener;
        checkBoxList = new ArrayList<>();
        Log.e("cancelreason",""+arrayList.toString());
    }

    @Override
    public int getCount() {
        return arrayList.size();
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

        View view = LayoutInflater.from(context).inflate(R.layout.cancel_order_resons_layout,null);

        CustomTextView reason_txt = view.findViewById(R.id.reason_txt);
        CheckBox reason_checkbox = view.findViewById(R.id.cancell_reason_checkbox);

        reason_txt.setText(arrayList.get(position).getCancel_reason());

        reason_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             pos = position;
             notifyDataSetChanged();
            }
        });

        if (pos == position){
            reason_checkbox.setChecked(true);
            reasonSelectedListener.onReasonSelected(pos,arrayList.get(position).getCancel_reason());
        }
        else {
            reason_checkbox.setChecked(false);
        }

        return view;
    }
}
