package com.kamaii.partner.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.DialogClose;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.fragment.Services;
import com.kamaii.partner.ui.models.ServicePrdVariationModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialogAvailableTime;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Services services;
    LayoutInflater mLayoutInflater;
    private ArrayList<ProductDTO> productDTOList;
    private Context context;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> Availableparms = new HashMap<>();
    private HashMap<String, String> sendAvailableTimeparms = new HashMap<>();
    private HashMap<String, String> parmsedit = new HashMap<>();
    private String TAG = AdapterServices.class.getSimpleName();
    private DialogInterface dialog_book;
    Boolean isCheck = true;
    private Dialog dialogedit;
    CustomTextViewBold ctvbTitle, tveditservice, ma_ti, round_ti;
    ImageView tveditcancel;
    RadioButton radioseditquantity, radioyeditdays;
    String quantitydays = "";
    CheckBox cbshipping;
    CustomEditText etvalue;
    LinearLayout layroundtrip;
    String roundtrip = "0";
    String totalvalue = "";
    UserDTO userDTO;
    DialogClose dialogClose;
    List<ServicePrdVariationModel> servicePrdList;
    public AdapterServices(Services services, ArrayList<ProductDTO> productDTOList, UserDTO userDTO, DialogClose dialogClose) {
        this.services = services;
        context = services.getActivity();
        this.productDTOList = productDTOList;
        this.userDTO = userDTO;
        this.dialogClose = dialogClose;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Availableparms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        sendAvailableTimeparms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        Log.e("productDTOArrayListnew", "Adapter Called" + productDTOList.size());
    }

    public AdapterServices(Context services, ArrayList<ProductDTO> productDTOList) {
        context = services;
        this.productDTOList = productDTOList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("productDTOArrayListnew", "Adapter Called" + productDTOList.size());
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services, parent, false);
        return new MyViewHolder(view);
    }

    public boolean checkarss(String catid) {

        boolean value = true;
        return value;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final int pos = position;
        holder.setIsRecyclable(false);

        ArrayList<String> availableTimeSlot = new ArrayList<>();
        availableTimeSlot = productDTOList.get(position).getAvailable_preparationtime();
        dialogedit = new Dialog(context, R.style.Theme_Dialog);
        dialogedit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogedit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialogedit.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogedit.setContentView(R.layout.dailog_editservice);
        dialogedit.setCancelable(true);

        servicePrdList = productDTOList.get(position).getService_type_array();
        ((MyViewHolder) holder).service_prd_rv.setLayoutManager(new LinearLayoutManager(context));
        ServicePrdAdapter servicePrdAdapter = new ServicePrdAdapter(context,servicePrdList);
        ((MyViewHolder) holder).service_prd_rv.setAdapter(servicePrdAdapter);
        myViewHolder.CTVproductname.setText(productDTOList.get(pos).getProduct_name());
        if (productDTOList.get(position).getIs_map().equalsIgnoreCase("1")) {

            myViewHolder.service_total_visit.setVisibility(View.GONE);
        } else {
            myViewHolder.service_total_visit.setText(Html.fromHtml(productDTOList.get(position).getService_total_visit()));

        }

        //Log.e("productDTOArrayListnew",""+productDTOList.get(pos).getProduct_name());
        Log.e("DESCRIPTION", "" + productDTOList.get(pos).getDescription());

        if (!productDTOList.get(pos).getDescription().equalsIgnoreCase("&nbsp;&nbsp;")) {
            myViewHolder.tvdesc.setText(Html.fromHtml(productDTOList.get(pos).getDescription()));
            myViewHolder.more_details_linear_layout.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.more_details_linear_layout.setVisibility(View.GONE);

        }

        Log.e("PATH", "" + Html.fromHtml(productDTOList.get(pos).getPath()));
        myViewHolder.service_path.setText(Html.fromHtml(productDTOList.get(pos).getPath()));
        myViewHolder.CTVproductprice.setText(Html.fromHtml(productDTOList.get(pos).getPerkm()));
        myViewHolder.tvquantity.setText(Html.fromHtml(productDTOList.get(pos).getMaxkm()));
        //  myViewHolder.CTVproductprice.setText(productDTOList.get(pos).getPrice() + " "+productDTOList.get(pos).getRate());
        myViewHolder.tvbdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    myViewHolder.tvdesc.setVisibility(View.VISIBLE);
                    isCheck = false;
                } else {
                    myViewHolder.tvdesc.setVisibility(View.GONE);
                    isCheck = true;
                }
            }
        });


        if (productDTOList.get(position).getStatus().equals("0")) {
            myViewHolder.swOnOff.setChecked(false);
            myViewHolder.swOnOff.setEnabled(false);
            myViewHolder.swOnOff.setClickable(false);
        } else {

            if (productDTOList.get(position).getLock().equals("1")) {

                if (productDTOList.get(pos).getIs_visible().equalsIgnoreCase("1")) {

                    myViewHolder.swOnOff.setChecked(true);

                    myViewHolder.service_lock.setVisibility(View.VISIBLE);
                    myViewHolder.swOnOff.setClickable(false);
                } else {
                    myViewHolder.swOnOff.setChecked(false);
                    myViewHolder.service_lock.setVisibility(View.VISIBLE);

                }

                //myViewHolder.swOnOff.setEnabled(false);

                //   myViewHolder.service_lock.setVisibility(View.VISIBLE);
            } else {
                if (productDTOList.get(pos).getIs_visible().equalsIgnoreCase("1")) {

                    myViewHolder.swOnOff.setChecked(true);

                } else {
                    myViewHolder.swOnOff.setChecked(false);

                }
                //   myViewHolder.service_lock.setVisibility(View.GONE);
                myViewHolder.swOnOff.setClickable(true);
                myViewHolder.swOnOff.setEnabled(true);
            }
        }

        if (!productDTOList.get(pos).getVehicle_number().equalsIgnoreCase("")) {
            myViewHolder.CTVvechieno.setVisibility(View.VISIBLE);
            myViewHolder.CTVvechieno.setText(productDTOList.get(pos).getVehicle_number());
        } else {
            myViewHolder.CTVvechieno.setVisibility(View.GONE);
          //  myViewHolder.ivedit.setVisibility(View.VISIBLE);
        }

        if (!productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("")) {

            if (productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("0")) {
                myViewHolder.tvquantitydays.setText("Qty");
            } else {
                myViewHolder.tvquantitydays.setText("KM");
            }
        }

        if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
            // myViewHolder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
        }
        if (!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")) {
            // myViewHolder.layservicecharge.setVisibility(View.VISIBLE);
            // myViewHolder.tvs_charge.setText(productDTOList.get(pos).getService_charge()+ " "+productDTOList.get(pos).getRate());
        }

        Log.e("AVAILABLE_STATUS_NIMESH", productDTOList.get(position).getAvailable_status());
        if (productDTOList.get(position).getAvailable_status().equalsIgnoreCase("1")) {
            myViewHolder.available_switch.setChecked(true);
            myViewHolder.available_txt.setText("Ready");
        } else {
            myViewHolder.available_switch.setChecked(false);
            myViewHolder.available_txt.setText("Not Ready");
        }

        myViewHolder.available_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myViewHolder.available_txt.setText("Available");
                    Availableparms.put("product_id", productDTOList.get(position).getId());
                    Availableparms.put("available_status", "1");
                    availableStatus();
                } else {
                    myViewHolder.available_txt.setText("Not Available");
                    Availableparms.put("product_id", productDTOList.get(position).getId());
                    Availableparms.put("available_status", "0");
                    availableStatus();
                }
            }
        });

        Glide.with(context).
                load(productDTOList.get(pos).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.dummyuser_image)
                .into(myViewHolder.IVproduct);




      /*  if (productDTOList.get(pos).getStatus().equalsIgnoreCase("1")) {
            myViewHolder.swOnOff.setEnabled(true);
        } else {
            myViewHolder.swOnOff.setEnabled(false);

        }*/

        //    myViewHolder.swOnOff.setChecked(productDTOList.get(pos).getStatus().equalsIgnoreCase("1") ?true :false);


        myViewHolder.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                parms.put(Consts.service_id, productDTOList.get(pos).getId());
                if (b) {
                    parms.put(Consts.is_visible, "1");
                    deleteGallery();
                } else {
                    parms.put(Consts.is_visible, "0");
                    deleteGallery();
                }
            }
        });

        myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parms.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                // deleteDialog();
            }
        });

        SpinnerDialogAvailableTime availableTimeSpinner = new SpinnerDialogAvailableTime(((Activity) context), availableTimeSlot, "Select time");

        availableTimeSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String itemId, int position) {

                sendAvailableTimeparms.put("hour", item);
                myViewHolder.available_time_spinner.setText(item);
                Log.e("ITEM_TIME_SPINNER", "" + item);
                sendAvailableTimeparms.put("product_id", productDTOList.get(pos).getId());
                sendavailableTime();
            }
        });

        myViewHolder.available_time_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                availableTimeSpinner.showSpinerDialog();
            }
        });

        if (!productDTOList.get(pos).getGet_hour_selected().equalsIgnoreCase("")) {
            //  int spinner_pos = availableTimeSlot.indexOf(productDTOList.get(pos).getGet_hour_selected());
            //  Log.e("spinner_pos",""+spinner_pos + "  "+ productDTOList.get(pos).getGet_hour_selected());
            myViewHolder.available_time_spinner.setText(productDTOList.get(pos).getGet_hour_selected());
        }
        /*ArrayAdapter arrayAdapter = new ArrayAdapter(context,android.R.layout.simple_spinner_item,availableTimeSlot);
       // arrayAdapter.setDropDownViewResource(R.layout.spinner_layout_with_line);
       // arrayAdapter.setDropDownViewResource(R.layout.spinner_view_radio);


        myViewHolder.available_time_spinner.setAdapter(arrayAdapter);

        if (!productDTOList.get(pos).getGet_hour_selected().equalsIgnoreCase("")){
            int spinner_pos = arrayAdapter.getPosition(productDTOList.get(pos).getGet_hour_selected());
            Log.e("spinner_pos",""+spinner_pos + "  "+ productDTOList.get(pos).getGet_hour_selected());
            myViewHolder.available_time_spinner.setSelection(spinner_pos);
        }
        //List<String> finalAvailableTimeSlot = availableTimeSlot;
        myViewHolder.available_time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sendAvailableTimeparms.put("hour", finalAvailableTimeSlot.get(position));
                sendAvailableTimeparms.put("product_id", productDTOList.get(pos).getId());
                sendavailableTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        myViewHolder.ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tveditcancel = dialogedit.findViewById(R.id.tveditcancel);
                tveditservice = dialogedit.findViewById(R.id.tveditservice);
                radioseditquantity = dialogedit.findViewById(R.id.radioseditquantity);
                radioyeditdays = dialogedit.findViewById(R.id.radioyeditdays);
                cbshipping = dialogedit.findViewById(R.id.cbshipping);
                etvalue = dialogedit.findViewById(R.id.etvalue);
                layroundtrip = dialogedit.findViewById(R.id.layroundtrip);
                ma_ti = dialogedit.findViewById(R.id.ma_ti);
                round_ti = dialogedit.findViewById(R.id.round_ti);
                ctvbTitle = dialogedit.findViewById(R.id.ctvbTitle);

                Typeface font2 = Typeface.createFromAsset(context.getAssets(), "Lato-Heavy.ttf");

                Typeface font = Typeface.createFromAsset(context.getAssets(), "Lato-Semibold.ttf");
                Typeface font3 = Typeface.createFromAsset(context.getAssets(), "Lato-Black.ttf");

                ctvbTitle.setTypeface(font2);
                ma_ti.setTypeface(font);
                round_ti.setTypeface(font);
                etvalue.setTypeface(font);
                radioseditquantity.setTypeface(font);
                radioyeditdays.setTypeface(font);
                tveditservice.setTypeface(font3);
                if (!productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("")) {

                    if (productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("0")) {
                        radioseditquantity.setChecked(true);
                    } else {
                        radioyeditdays.setChecked(true);
                    }

                }

                if (!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")) {
                    etvalue.setText(productDTOList.get(pos).getMaxmiumvalue());
                }
                dialogedit.show();

                if (productDTOList.get(pos).getSub_category_id().equalsIgnoreCase("434")) {
                    // myViewHolder.ivedit.setVisibility(View.GONE);

                    if (checkarss(productDTOList.get(pos).getCategory_id())) {
                        layroundtrip.setVisibility(View.VISIBLE);
                        if (productDTOList.get(pos).getRoundtrip().equalsIgnoreCase("0")) {
                            roundtrip = "0";

                            cbshipping.setChecked(false);
                        } else {
                            roundtrip = "1";
                            cbshipping.setChecked(true);
                        }
                    } else {
                        layroundtrip.setVisibility(View.GONE);
                    }

                } else {
                    layroundtrip.setVisibility(View.GONE);

                }


                cbshipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            roundtrip = "1";
                            // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                        } else {
                            roundtrip = "0";
                            //  Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
                        }
                    }
                });


                tveditcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogedit.dismiss();
                    }
                });

                tveditservice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int maxvalue = Integer.parseInt(etvalue.getText().toString());
                        if (!validation(etvalue, "Please Enter Value")) {
                            return;
                        } else if (maxvalue < 1) {
                            Toast.makeText(context, "Please Select Atleast 1 Value", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            // Toast.makeText(context,"Ok",Toast.LENGTH_LONG).show();
                            if (radioseditquantity.isChecked()) {
                                quantitydays = "0";
                            } else {
                                quantitydays = "1";
                            }


                            if (roundtrip.equalsIgnoreCase("1")) {

                                if (etvalue.getText().toString().equalsIgnoreCase(productDTOList.get(pos).getMaxmiumvalue())) {
                                    parmsedit.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                                    parmsedit.put(Consts.QUANTITYDAYS, quantitydays);
                                    parmsedit.put(Consts.MAXIMUMVALUE, etvalue.getText().toString());
                                    parmsedit.put(Consts.ROUNDTRIP, roundtrip);
                                } else {
                                    int maxv = Integer.parseInt(etvalue.getText().toString());
                                    totalvalue = String.valueOf(maxv * 2);
                                    parmsedit.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                                    parmsedit.put(Consts.QUANTITYDAYS, quantitydays);
                                    parmsedit.put(Consts.MAXIMUMVALUE, totalvalue);
                                    parmsedit.put(Consts.ROUNDTRIP, roundtrip);
                                }

                            } else {
                                parmsedit.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                                parmsedit.put(Consts.QUANTITYDAYS, quantitydays);
                                parmsedit.put(Consts.MAXIMUMVALUE, etvalue.getText().toString());
                                parmsedit.put(Consts.ROUNDTRIP, roundtrip);
                            }


                            // editproduct();

                            new HttpsRequest(Consts.EDIT_PRODUCT_API, parmsedit, context).stringPost(TAG, new Helper() {
                                @Override
                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                    if (flag) {
                                        if (quantitydays.equalsIgnoreCase("0")) {
                                            myViewHolder.tvquantitydays.setText("Qty");
                                            productDTOList.get(pos).setQuantitydays("0");
                                        } else {
                                            productDTOList.get(pos).setQuantitydays("1");
                                            myViewHolder.tvquantitydays.setText("KM");
                                        }


                                        if (roundtrip.equalsIgnoreCase("1")) {

                                            productDTOList.get(pos).setRoundtrip(roundtrip);

                                            if (!totalvalue.equalsIgnoreCase("")) {
                                                //  myViewHolder.tvquantity.setText(totalvalue);
                                                productDTOList.get(pos).setMaxmiumvalue(totalvalue);
                                            } else {
                                                // myViewHolder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
                                            }

                                        } else {
                                            productDTOList.get(pos).setRoundtrip(roundtrip);

                                            // myViewHolder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
                                        }

                                 //       ProjectUtils.showToast(context, msg);
                                        dialogedit.dismiss();

                                        dialogClose.onClose(dialogedit);
                                        //notifyDataSetChanged();
                                        //  services.getrefresh();
                                    } else {
                                        ProjectUtils.showLong(context, msg);
                                    }
                                }
                            });
                        }


                        // Toast.makeText(context,quantitydays,Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(context, msg);
            return false;
        } else {
            return true;
        }
    }

    public void editproduct() {

        new HttpsRequest(Consts.EDIT_PRODUCT_API, parmsedit, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

              //      ProjectUtils.showToast(context, msg);
                    dialogedit.dismiss();
                    //  services.getrefresh();
                } else {
                //    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivDelete, ivedit;
        public ImageView IVproduct, service_lock;
        public CustomTextViewBold available_txt, service_total_visit, service_path, CTVproductname, CTVproductprice, tvs_charge, tvquantity, tvquantitydays, tvbdetails, CTVvechieno;
        public LinearLayout layservicecharge;
        public Switch swOnOff, available_switch;
        CustomTextViewBold available_time_spinner;
        CustomTextView tvdesc;
        LinearLayout more_details_linear_layout;
        RecyclerView service_prd_rv;
        public MyViewHolder(View view) {
            super(view);

            service_prd_rv = itemView.findViewById(R.id.service_prd_rv);
            available_switch = itemView.findViewById(R.id.switch_available);
            more_details_linear_layout = itemView.findViewById(R.id.more_details_linear_layout);
            available_time_spinner = itemView.findViewById(R.id.available_time_spinner);
            available_txt = itemView.findViewById(R.id.available_txt);
            IVproduct = itemView.findViewById(R.id.IVproduct);
            CTVproductname = itemView.findViewById(R.id.CTVproductname);
            service_total_visit = itemView.findViewById(R.id.service_total_visit);
            service_lock = itemView.findViewById(R.id.service_lock);
            CTVproductprice = itemView.findViewById(R.id.CTVproductprice);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            tvdesc = itemView.findViewById(R.id.tvdesc);
            service_path = itemView.findViewById(R.id.service_path);
            tvs_charge = itemView.findViewById(R.id.tvs_charge);
            layservicecharge = itemView.findViewById(R.id.layservicecharge);
            ivedit = itemView.findViewById(R.id.ivedit);
            tvquantity = view.findViewById(R.id.tvquantity);
            tvquantitydays = view.findViewById(R.id.tvquantitydays);
            swOnOff = view.findViewById(R.id.swOnOff_service);
            tvbdetails = view.findViewById(R.id.more);
            CTVvechieno = view.findViewById(R.id.CTVvechieno);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;

    }

    public void deleteDialog() {
        try {
            new AlertDialog.Builder(context)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(context.getResources().getString(R.string.delete_service))
                    .setMessage(context.getResources().getString(R.string.delete_service_msg))
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
        new HttpsRequest(Consts.serviceVisiblity, parms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    // notifyDataSetChanged();
                    services.getParentData();
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

    public void availableStatus() {
        Log.e("AvailabeParams", "" + Availableparms.toString());
        new HttpsRequest(Consts.serviceAvailability, Availableparms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("AVAILABLE_RES", "" + response.toString());
                    //services.getParentData();
                } else {
                    // ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

    public void sendavailableTime() {
        Log.e("sendAvailabeParams", "" + sendAvailableTimeparms.toString());
        new HttpsRequest(Consts.SEND_AVAILABLE_TIME, sendAvailableTimeparms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("sendAvailabeParams", "success" + flag);

                if (flag) {
                    //services.getParentData();
                } else {
                    // ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

}