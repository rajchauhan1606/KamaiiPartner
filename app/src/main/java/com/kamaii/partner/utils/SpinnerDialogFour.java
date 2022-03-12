package com.kamaii.partner.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kamaii.partner.DTO.SkillsDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.model.CommonServiceModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by VARUN on 01/01/19.
 */
public class SpinnerDialogFour {

    ArrayList<CommonServiceModel> ThirdCateModel;
    ArrayList<SkillsDTO> skillsDTOS;
    Activity context;
    String dTitle;
    String closeTitle = "Close";
    OnSpinerItemClick onSpinerItemClick;
    AlertDialog alertDialog;
    int style;
    ListView listView;
    MyAdapterRadio myAdapterRadio;
    MyAdapterRadioother myAdapterRadioother;
    String[] arrayListid;
    int pos;
    public SpinnerDialogFour(Activity activity, ArrayList<CommonServiceModel> ThirdCateModel, String dialogTitle) {
        this.ThirdCateModel = ThirdCateModel;
        this.context = activity;
        this.dTitle = dialogTitle;
    }
   public SpinnerDialogFour(Activity activity, ArrayList<SkillsDTO> skillsDTOS, String dialogTitle, String closeTitle) {
        this.skillsDTOS = skillsDTOS;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.closeTitle = closeTitle;
    }


    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }


    public void showSpinerDialog() {
        Builder adb = new Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(R.layout.dialog_layout_radio, (ViewGroup) null);
        TextView close = (TextView) v.findViewById(R.id.close);
        TextView title = (TextView) v.findViewById(R.id.spinerTitle);
        title.setText(this.dTitle);
        listView = (ListView) v.findViewById(R.id.list);


        if(ThirdCateModel.get(0).getCat_id().equalsIgnoreCase("85")){
            String[] arrayListtt = ThirdCateModel.get(0).getService_name().split(",");
            arrayListid = ThirdCateModel.get(0).getService_id().split(",");


            myAdapterRadio = new MyAdapterRadio(this.context, arrayListtt);
            listView.setAdapter(myAdapterRadio);
            adb.setView(v);
        }else {
            myAdapterRadioother = new MyAdapterRadioother(this.context, this.ThirdCateModel);
            listView.setAdapter(myAdapterRadioother);
            adb.setView(v);
        }



        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(false);

        close.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SpinnerDialogFour.this.alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView) view.findViewById(R.id.text1);
                TextView textView2 = (TextView) view.findViewById(R.id.textView2);
                String selectedID = "";
                for (int j = 0; j < ThirdCateModel.size(); j++) {
                    if (t.getText().toString().equalsIgnoreCase(ThirdCateModel.get(j).toString())) {
                        pos = j;
                        selectedID = ThirdCateModel.get(j).getId();
                    }
                    if (j == i) {
                        ThirdCateModel.get(j).setSelected(true);
                    } else {
                        ThirdCateModel.get(j).setSelected(false);
                    }
                }
                onSpinerItemClick.onClick(t.getText().toString(), textView2.getText().toString(), pos);
                alertDialog.dismiss();
            }
        });


        this.alertDialog.show();
    }


    public class MyAdapterRadioother extends BaseAdapter {

        ArrayList<CommonServiceModel> arrayList;
        LayoutInflater inflater;


        public MyAdapterRadioother(Context context, ArrayList<CommonServiceModel> arrayList) {
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
            public CustomTextView text1,textView2;
            public RadioButton radioBtn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spinner_view_radio, parent, false);
                holder.text1 = (CustomTextView) convertView.findViewById(R.id.text1);
                holder.textView2 = (CustomTextView) convertView.findViewById(R.id.textView2);
                holder.radioBtn = (RadioButton) convertView.findViewById(R.id.radioBtn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text1.setText(arrayList.get(position).getService_name());
            holder.text1.setTypeface(null, Typeface.NORMAL);
            holder.textView2.setText(arrayList.get(position).getService_id());
            if (arrayList.get(position).isSelected()) {
                holder.radioBtn.setChecked(true);
            } else {
                holder.radioBtn.setChecked(false);
            }


            return convertView;
        }

    }

    public class MyAdapterRadio extends BaseAdapter {

        String[] items;
            private ArrayList<CommonServiceModel> stringArrayList=new ArrayList<>();
        LayoutInflater inflater;



        public MyAdapterRadio(Context context, String[] items) {
            this.items = items;
            inflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public class ViewHolder {
            public CustomTextView text1,textView2;
            public RadioButton radioBtn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spinner_view_radio, parent, false);
                holder.text1 = (CustomTextView) convertView.findViewById(R.id.text1);
                holder.textView2 = (CustomTextView) convertView.findViewById(R.id.textView2);
                holder.radioBtn = (RadioButton) convertView.findViewById(R.id.radioBtn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(items));
            ArrayList<String> stringListid = new ArrayList<String>(Arrays.asList(arrayListid));
           holder.text1.setText(stringList.get(position));
           holder.textView2.setText(stringListid.get(position));


            holder.text1.setTypeface(null, Typeface.NORMAL);



            return convertView;
        }

    }

}
