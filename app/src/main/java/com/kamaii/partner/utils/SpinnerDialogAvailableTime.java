package com.kamaii.partner.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kamaii.partner.DTO.SkillsDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.model.ThirdCateModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerDialogAvailableTime {
    ArrayList<ThirdCateModel> ThirdCateModel;
    ArrayList<SkillsDTO> skillsDTOS;
    ArrayList<String> timeList;
    Activity context;
    String dTitle;
    String closeTitle = "Close";
    OnSpinerItemClick onSpinerItemClick;
    AlertDialog alertDialog;
    int style;
    ListView listView;
    MyAdapterRadio myAdapterRadio;

    int pos;
    public SpinnerDialogAvailableTime(Activity activity, ArrayList<String> ThirdCateModel, String dialogTitle) {
        this.timeList = ThirdCateModel;
        this.context = activity;
        this.dTitle = dialogTitle;
    }
    public SpinnerDialogAvailableTime(Activity activity, ArrayList<SkillsDTO> skillsDTOS, String dialogTitle, String closeTitle) {
        this.skillsDTOS = skillsDTOS;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.closeTitle = closeTitle;
    }




    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }


    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(R.layout.spinner_dialog_layout_2, (ViewGroup) null);
        ImageView close =  v.findViewById(R.id.close);
        CustomTextViewBold title =  v.findViewById(R.id.spinerTitle);
        title.setText(this.dTitle);
        listView = (ListView) v.findViewById(R.id.list);

        myAdapterRadio = new MyAdapterRadio(this.context, this.timeList);
        listView.setAdapter(myAdapterRadio);
        adb.setView(v);


        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(false);

        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SpinnerDialogAvailableTime.this.alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView) view.findViewById(R.id.text1);
                String selectedID = "";
                for (int j = 0; j < timeList.size(); j++) {
                    if (t.getText().toString().equalsIgnoreCase(timeList.get(j).toString())) {
                        pos = j;
                      //  selectedID = timeList.get(j).getId();
                    }
                }
                onSpinerItemClick.onClick(t.getText().toString(), selectedID, pos);
                alertDialog.dismiss();
            }
        });


        this.alertDialog.show();
    }




    public class MyAdapterRadio extends BaseAdapter {

        ArrayList<String> arrayList;
        LayoutInflater inflater;


        public MyAdapterRadio(Context context, ArrayList<String> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            public CustomTextView text1;
           // public RadioButton radioBtn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.available_time_spinner_layout, parent, false);
                holder.text1 = (CustomTextView) convertView.findViewById(R.id.text1);

             //   holder.radioBtn = (RadioButton) convertView.findViewById(R.id.radioBtn);

             //   holder.radioBtn.setVisibility(View.GONE);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text1.setText(arrayList.get(position));
            holder.text1.setTypeface(null, Typeface.NORMAL);



            return convertView;
        }

    }

}

