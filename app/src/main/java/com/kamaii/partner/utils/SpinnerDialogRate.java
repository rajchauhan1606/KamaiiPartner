package com.kamaii.partner.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kamaii.partner.DTO.SkillsDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.ui.models.RateModel;

import java.util.ArrayList;


public class SpinnerDialogRate {
    ArrayList<RateModel> RateModel;
    ArrayList<SkillsDTO> skillsDTOS;
    Activity context;
    String dTitle;
    String closeTitle = "Close";
    OnSpinerItemClick onSpinerItemClick;
    AlertDialog alertDialog;
    Dialog dialog;
    int style;
    ListView listView;
    MyAdapterRadio myAdapterRadio;
    int pos;
    public SpinnerDialogRate(Activity activity, ArrayList<RateModel> RateModel, String dialogTitle) {
        this.RateModel = RateModel;
        this.context = activity;
        this.dTitle = dialogTitle;
    }
   public SpinnerDialogRate(Activity activity, ArrayList<SkillsDTO> skillsDTOS, String dialogTitle, String closeTitle) {
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
        View v = this.context.getLayoutInflater().inflate(R.layout.dialog_layout_rate, (ViewGroup) null);

        adb.setView(v);
       // final Dialog dialog = new Dialog(context);
        dialog = adb.create();
        //dialog.setTitle("Title...");

        TextView close = (TextView) v.findViewById(R.id.close);
        listView = (ListView) v.findViewById(R.id.list);

        myAdapterRadio = new MyAdapterRadio(this.context, this.RateModel);
        listView.setAdapter(myAdapterRadio);
        close.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               // SpinnerDialogRate.this.alertDialog.dismiss();
                SpinnerDialogRate.this.dialog.dismiss();
            }
        });
        RateModel.get(pos).setSelected(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView) view.findViewById(R.id.text1);
                String selectedID = "";
                for (int j = 0; j < RateModel.size(); j++) {
                    if (t.getText().toString().equalsIgnoreCase(RateModel.get(j).toString())) {
                        pos = j;
                        selectedID = RateModel.get(j).getId();
                    }
                    if (j == i) {
                        RateModel.get(j).setSelected(true);
                    } else {
                        RateModel.get(j).setSelected(false);
                    }
                }
                onSpinerItemClick.onClick(t.getText().toString(), selectedID, pos);
                dialog.dismiss();
               // alertDialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
     /*   Builder adb = new Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(R.layout.dialog_layout_radio, (ViewGroup) null);

        adb.setView(v);


        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(false);*/




       // this.alertDialog.show();
    }




    public class MyAdapterRadio extends BaseAdapter {

        ArrayList<RateModel> arrayList;
        LayoutInflater inflater;


        public MyAdapterRadio(Context context, ArrayList<RateModel> arrayList) {
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
            public RadioButton radioBtn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.spinner_view_radio, parent, false);
                holder.text1 = (CustomTextView) convertView.findViewById(R.id.text1);
                holder.radioBtn = (RadioButton) convertView.findViewById(R.id.radioBtn);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text1.setText(arrayList.get(position).getName());
            holder.text1.setTypeface(null, Typeface.NORMAL);

            if (arrayList.get(position).isSelected()) {
                holder.radioBtn.setChecked(true);
            } else {
                holder.radioBtn.setChecked(false);
            }


            return convertView;
        }

    }

}
