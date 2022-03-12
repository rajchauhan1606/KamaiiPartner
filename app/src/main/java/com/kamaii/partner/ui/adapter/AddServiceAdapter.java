package com.kamaii.partner.ui.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.SetonItemListener;
import com.kamaii.partner.interfacess.SetOnCheckboxClick;
import com.kamaii.partner.model.CommonServiceModel;
import com.kamaii.partner.ui.activity.AddServicesActivity;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.models.Description;
import com.kamaii.partner.ui.models.EditTextModel;
import com.kamaii.partner.ui.models.MaxQtyEdittextModel;
import com.kamaii.partner.ui.models.VehicleModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.SpinnerDialogDescription;

import java.util.ArrayList;
import java.util.List;

public class AddServiceAdapter extends BaseAdapter {

    Activity context;
    List<CommonServiceModel> arraylist;
    SetOnCheckboxClick setonItemListener;
    ArrayList<Description> descriptionArrayList = new ArrayList<>();
    ArrayList<String> service_id = new ArrayList<>();
    public ArrayList<EditTextModel> priceList = new ArrayList<>();
    public ArrayList<VehicleModel> vehicleNumberList = new ArrayList<>();
    public ArrayList<MaxQtyEdittextModel> maxQtyList = new ArrayList<>();
    public ArrayList<String> descriptionList = new ArrayList<>();
    boolean vehiclenum = false;


    public AddServiceAdapter(Activity context, List<CommonServiceModel> arraylist, ArrayList<Description> descriptionArrayList, SetOnCheckboxClick setonItemListener, boolean vehiclenum) {
        this.context = context;
        this.arraylist = arraylist;
        this.descriptionArrayList = descriptionArrayList;
        this.vehiclenum = vehiclenum;
        Log.e("vehiclenum", "" + vehiclenum);
        this.setonItemListener = setonItemListener;
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

        View view = LayoutInflater.from(context).inflate(R.layout.add_service_recycle_layout, null);

        CheckBox add_service_checkbox;
        RelativeLayout vehicle_number_relative, maximum_qty_relative,add_max_item_relative;
        CustomTextViewBold p_des, add_service_name_txt;
        CustomEditText vechial_number, add_service_price, max_qty_number;
        CustomTextView p_desdeep;
        SpinnerDialogDescription spinnerDialogDes;

        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setVehicleNumber(arraylist.get(position).getPrice());

        MaxQtyEdittextModel maxQtyEdittextModel = new MaxQtyEdittextModel();
        maxQtyEdittextModel.setMaxQty(arraylist.get(position).getMaxmiumvalue());

        vehicleNumberList.add(vehicleModel);
        maxQtyList.add(maxQtyEdittextModel);

        EditTextModel editTextModel = new EditTextModel();
        editTextModel.setEdittextValue(arraylist.get(position).getPrice());

        priceList.add(editTextModel);

        vehicle_number_relative = view.findViewById(R.id.vehicle_number_relative);
        vechial_number = view.findViewById(R.id.vechial_number);
        max_qty_number = view.findViewById(R.id.max_qty_number);
        maximum_qty_relative = view.findViewById(R.id.maximum_qty_relative);
        add_max_item_relative = view.findViewById(R.id.add_max_item_relative);
        p_desdeep = view.findViewById(R.id.add_prd_desdeep);
        add_service_checkbox = view.findViewById(R.id.add_service_checkbox);
        p_des = view.findViewById(R.id.add_p_des);
        add_service_price = view.findViewById(R.id.add_service_price);
        add_service_name_txt = view.findViewById(R.id.add_service_name_txt);


        add_service_name_txt.setText(arraylist.get(position).getService_name());
        p_desdeep.setText(Html.fromHtml(descriptionArrayList.get(0).getDescription()));

        descriptionList.add(descriptionArrayList.get(0).getId());
        Log.e("descriptionList",""+descriptionList.toString());


        max_qty_number.setText(arraylist.get(position).getMaxmiumvalue());

        if (vehiclenum) {

            maximum_qty_relative.setVisibility(View.GONE);
            add_max_item_relative.setVisibility(View.GONE);
            vehicle_number_relative.setVisibility(View.VISIBLE);

            vechial_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if ((vechial_number.getText().length() + 1 == 3 || vechial_number.getText().length() + 1 == 6 || vechial_number.getText().length() + 1 == 9)) {
                        if (before - count < 0) {
                            vechial_number.setText(vechial_number.getText() + " ");
                            vechial_number.setSelection(vechial_number.getText().length());
                        }
                    }
                    vehicleNumberList.get(position).setVehicleNumber(vechial_number.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {


            vehicle_number_relative.setVisibility(View.GONE);
            maximum_qty_relative.setVisibility(View.VISIBLE);
            add_max_item_relative.setVisibility(View.VISIBLE);

            max_qty_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    maxQtyList.get(position).setMaxQty(max_qty_number.getText().toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        add_service_price.setText(arraylist.get(position).getPrice());
        String five = p_des.getText().toString().trim();


        add_service_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String five = p_des.getText().toString().trim();
                    String price = add_service_price.getText().toString();
                    service_id.add(arraylist.get(position).getService_id());
                    setonItemListener.Click(arraylist.get(position).getService_id(), price, five, position);

                } else {
                    String five = p_des.getText().toString().trim();
                    String price = add_service_price.getText().toString();
                    service_id.remove(arraylist.get(position).getService_id());
                    setonItemListener.Click(arraylist.get(position).getService_id(), price, five, position);

                }
            }
        });

        add_service_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                priceList.get(position).setEdittextValue(add_service_price.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerDialogDes = new SpinnerDialogDescription(context, descriptionArrayList, context.getResources().getString(R.string.select_description));
        spinnerDialogDes.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {

                p_des.setText(item);

            }
        });


        p_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogDes.showSpinerDialog();
            }
        });
        return view;
    }

}
