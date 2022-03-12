package com.kamaii.partner.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.ShippingDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSelectedItemListener;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.EditPersnoalInfo;
import com.kamaii.partner.ui.adapter.AdapterCategoryShippingServices;
import com.kamaii.partner.ui.adapter.SubCateShippingAdapter;
import com.kamaii.partner.ui.models.CategoryModel;
import com.kamaii.partner.ui.models.GuideModel;
import com.kamaii.partner.ui.models.SubCateModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ItemDecorationAlbumColumns;
import com.kamaii.partner.utils.ProjectUtils;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class ServiceShippingFragment extends Fragment {

    private String TAG = ServiceShippingFragment.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rv_services_cat, rv_services_sub_car;
    private CustomTextViewBold tvNotFound;
    private Dialog dialogEditProduct;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    RadioGroup radioGroup;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    CheckBox shipping_check, rider_check, flexible_check;
    private ArrayList<CategoryModel> categoryarraylist = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew = new ArrayList<>();
    ArrayList<ProductDTO> productDTOArrayListnew = new ArrayList<>();
    private HashMap<String, String> parmsaddproduct = new HashMap<>();
    ViewFlipper viewflipper;
    AdapterCategoryShippingServices adapterCategoryServices;
    SubCateShippingAdapter subCateAdapter;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> serviceparms = new HashMap<>();
    CustomTextViewBold tvtitle, tvNo, tvYesPro;
    CustomTextView my_l, max_txt, current_txt;
    ImageView close_ship_dialog;
    String catidmain = "";
    private BaseActivity baseActivity;
    CheckBox live_check, current_check;
    CustomTextView shipping_txt, rider_txt, flexible_txt;
    String shipping = "", mylocation = "2", subcatid = "", maxprice = "1", rider = "1", p_address = "", p_house_no = "";
    private HashMap<String, String> parmsshipping = new HashMap<>();
    ShippingDTO shippingDTO;
    private ArrayList<ShippingDTO> shippingDTOArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    CustomEditText edt_s_price;
    RadioButton rb;
    public String currentt_location_flag = "";
    public String my_location_flag = "";
    public String shipping_flag = "";
    public String rider_flag = "";
    public String flexible_time_self_assign_rider_flag = "";
    public String min_order_price_flag = "";
    public String house_no = "";
    public String partner_address = "";
    RelativeLayout current_location_relative, my_location_relative, max_item_relative, radioButton_shipping, radioButton_rider, radioButton_flexible;
    LinearLayout enterlocationlinear;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);

    double current_lat, current_long;
    CustomEditText shipping_ed_society;
    CustomTextView shipping_your_location_txt;
    CustomTextViewBold shipping_etLocationD;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services_shippping, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.serviceee));

        Places.initialize(getApplicationContext(), getResources().getString(R.string.api_key));

        // artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        parmsaddproduct.put(Consts.USER_ID, userDTO.getUser_id());
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditProduct.setContentView(R.layout.dailog_service_shipping);

        tvtitle = dialogEditProduct.findViewById(R.id.tvtitle);
        my_location_relative = dialogEditProduct.findViewById(R.id.my_location_relative);
        current_location_relative = dialogEditProduct.findViewById(R.id.current_location_relative);
        enterlocationlinear = dialogEditProduct.findViewById(R.id.enterlocationlinear);
        max_item_relative = dialogEditProduct.findViewById(R.id.max_item_relative);

        Typeface font2 = Typeface.createFromAsset(getContext().getAssets(), "Lato-Heavy.ttf");
        Typeface font3 = Typeface.createFromAsset(getContext().getAssets(), "Lato-Black.ttf");

        tvtitle.setTypeface(font2);
        tvNo = dialogEditProduct.findViewById(R.id.tvNoedit);
        close_ship_dialog = dialogEditProduct.findViewById(R.id.close_ship_dialog);
        radioButton_shipping = dialogEditProduct.findViewById(R.id.radioButton_shipping);
        radioButton_rider = dialogEditProduct.findViewById(R.id.radioButton_rider);
        radioButton_flexible = dialogEditProduct.findViewById(R.id.radioButton_flexible);

        tvYesPro = dialogEditProduct.findViewById(R.id.tvYesPro);
        tvYesPro.setTypeface(font3);
        live_check = dialogEditProduct.findViewById(R.id.live_check);
        current_check = dialogEditProduct.findViewById(R.id.current_check);
        shipping_your_location_txt = dialogEditProduct.findViewById(R.id.shipping_your_location_txt);
        shipping_ed_society = dialogEditProduct.findViewById(R.id.shipping_ed_society);
        shipping_etLocationD = dialogEditProduct.findViewById(R.id.shipping_etLocationD);


        // live_check.setChecked(true);

        radioGroup = dialogEditProduct.findViewById(R.id.radiogroup);

        shipping_check = dialogEditProduct.findViewById(R.id.shipping_check);
        shipping_txt = dialogEditProduct.findViewById(R.id.shipping_txt);
        rider_txt = dialogEditProduct.findViewById(R.id.rider_txt);
        flexible_txt = dialogEditProduct.findViewById(R.id.flexible_txt);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Lato-Semibold.ttf");
        shipping_txt.setTypeface(font);

        rider_check = dialogEditProduct.findViewById(R.id.rider_check);
        rider_txt.setTypeface(font);

        flexible_check = dialogEditProduct.findViewById(R.id.flexible_check);
        flexible_txt.setTypeface(font);

        max_txt = dialogEditProduct.findViewById(R.id.max_txt);
        max_txt.setTypeface(font);


        my_l = dialogEditProduct.findViewById(R.id.my_l);
        current_txt = dialogEditProduct.findViewById(R.id.current_txt);
        my_l.setTypeface(font);
        current_txt.setTypeface(font);

        max_txt = dialogEditProduct.findViewById(R.id.max_txt);

        // cbmylocation=dialogEditProduct.findViewById(R.id.cbmylocation);
        edt_s_price = dialogEditProduct.findViewById(R.id.edt_s_price);
        edt_s_price.setTypeface(font);
        dialogEditProduct.setCancelable(true);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        shipping_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rider = "0";
                    rider_check.setChecked(false);
                    flexible_check.setChecked(false);
                }
            }
        });

        rider_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rider = "1";
                    shipping_check.setChecked(false);
                    flexible_check.setChecked(false);
                }
            }
        });

        shipping_etLocationD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // findPlace();
                dialogEditProduct.dismiss();

                startActivity(new Intent(getActivity(), EditPersnoalInfo.class).putExtra("fromshippingdialog",true).putExtra("address",partner_address));
//                baseActivity.navItemIndex = 405;
  //              baseActivity.loadHomeFragment(new ArtistProfile(), "artist");
            }
        });
        flexible_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rider = "2";
                    rider_check.setChecked(false);
                    shipping_check.setChecked(false);
                }
            }
        });


        // radioButton_rider
        return view;
    }

    private void findPlace() {


        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(getApplicationContext());

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

        //  imm.hideSoftInputFromWindow(v.getWindowToken(), 0);




    }


    private void getShippingType() {

        // Log.e("GETSHIPPING",""+serviceparms.toString());
        new HttpsRequest(Consts.GET_SERVICE_SHIPPING_TYPE_API, serviceparms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("GETSHIPPING", "" + response.toString());

                    try {
                        currentt_location_flag = response.getString("current_location");
                        my_location_flag = response.getString("my_location");
                        shipping_flag = response.getString("shipping");
                        rider_flag = response.getString("rider");
                        flexible_time_self_assign_rider_flag = response.getString("flexible_time_self_assign_rider");
                        min_order_price_flag = response.getString("min_order_price");
                        house_no = response.getString("house_no");
                        partner_address = response.getString("address");
                        // = partner_address;

                        if (currentt_location_flag.equals("0")) {
                            current_location_relative.setVisibility(View.GONE);
                        } else {
                            current_location_relative.setVisibility(View.VISIBLE);
                            // shipping_ed_society.setText(house_no);
                            shipping_etLocationD.setText(partner_address);
                        }
                        if (my_location_flag.equals("0")) {
                            my_location_relative.setVisibility(View.GONE);
                        } else {
                            my_location_relative.setVisibility(View.VISIBLE);

                        }
                        if (shipping_flag.equals("0")) {
                            radioButton_shipping.setVisibility(View.GONE);
                            // radioButton_rider.setVisibility(View.GONE);
                        } else {
                            radioButton_shipping.setVisibility(View.VISIBLE);
                            //  radioButton_rider.setVisibility(View.VISIBLE);

                        }
                        if (rider_flag.equals("0")) {
                            // radioButton_shipping.setVisibility(View.GONE);
                            radioButton_rider.setVisibility(View.GONE);
                        } else {
                            //  radioButton_shipping.setVisibility(View.VISIBLE);
                            radioButton_rider.setVisibility(View.VISIBLE);

                        }
                        if (flexible_time_self_assign_rider_flag.equals("0")) {
                            radioButton_flexible.setVisibility(View.GONE);
                        } else {
                            radioButton_flexible.setVisibility(View.VISIBLE);
                        }
                        if (min_order_price_flag.equals("0")) {
                            max_item_relative.setVisibility(View.GONE);
                        } else {
                            max_item_relative.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getshippingproduct(serviceparms.get(Consts.SUB_CAT_ID), serviceparms.get(Consts.SUB_CAT_NAME));
                } else {
                    Log.e("GETSHIPPING", "else called");
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            Place place = Autocomplete.getPlaceFromIntent(data);

            String final_user_address = place.getAddress();
            String landmark_name = place.getName();

            shipping_etLocationD.setText(landmark_name + ", " + final_user_address);
        }

    }

    public void getArtist() {
        // ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        Log.e("ARTISTDTO", "" + artistDetailsDTO.toString());
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }


    public void getshippingproduct(final String subid, final String name) {

        parmsshipping.put(Consts.USER_ID, userDTO.getUser_id());
        parmsshipping.put(Consts.SUB_CAT_ID, subid);
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_SERVICE_SHIPPING, parmsshipping, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        // shippingDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ShippingDTO.class);

                        String s = response.toString();
                        Log.e("smylocation_RESPONSE", "" + s);
                        JSONArray jsonarray = response.getJSONArray("data");

                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject c = jsonarray.getJSONObject(i);


                            String id = c.getString("id");
                            String sub_cat_id = c.getString("sub_cat_id");
                            String servershipping = c.getString("shipping");
                            String my_location = c.getString("my_location");
                            String smaxPrice = c.getString("maxprice");
                            String r1 = c.getString("rider");

                            shippingDTO = new ShippingDTO();

                            shippingDTO.setId(id);
                            shippingDTO.setSub_cat_id(sub_cat_id);
                            shippingDTO.setShipping(servershipping);
                            shippingDTO.setMy_location(my_location);
                            shippingDTO.setMaxprice(smaxPrice);
                            shippingDTO.setRider(r1);
                            shippingDTOArrayList.add(shippingDTO);

                            for (int j = 0; j < shippingDTOArrayList.size(); j++) {

                                if (shippingDTOArrayList.get(j).getSub_cat_id().equalsIgnoreCase(subid)) {

                                    if (shippingDTOArrayList.get(j).getShipping().equalsIgnoreCase("0")) {
                                        shipping = "0";
                                    } else {
                                        shipping = "1";
                                    }

                                    // Toast.makeText(baseActivity, "mylocation 1:"+shippingDTOArrayList.get(j).getMy_location(), Toast.LENGTH_SHORT).show();
                                    if (shippingDTOArrayList.get(j).getRider().equalsIgnoreCase("0")) {
                                        rider = "0";

                                    } else if (shippingDTOArrayList.get(j).getRider().equalsIgnoreCase("1")) {
                                        rider = "1";
                                    } else if (shippingDTOArrayList.get(j).getRider().equalsIgnoreCase("2")) {
                                        rider = "2";

                                    } else {
                                        rider = "3";

                                    }

                                    if (shippingDTOArrayList.get(j).getMy_location().equalsIgnoreCase("0")) {
                                        mylocation = "0";

                                    } else if (shippingDTOArrayList.get(j).getMy_location().equalsIgnoreCase("1")) {
                                        mylocation = "1";
                                    } else {
                                        mylocation = "2";
                                    }
                                    maxprice = smaxPrice;
                                    dialogProduct(rider, shipping, mylocation, name, subid, maxprice);
                                }
                            }
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    // ProjectUtils.showToast( Booking.this, msg);

                } else {
                    // Toast.makeText(baseActivity, "else called", Toast.LENGTH_SHORT).show();
                    dialogProduct("1", "1", "1", name, subid, maxprice);
                }


            }
        });
    }

    public void showUiAction(View v) {

        rv_services_cat = v.findViewById(R.id.rv_services_cat);
        rv_services_sub_car = v.findViewById(R.id.rv_services_sub_car);


        viewflipper = v.findViewById(R.id.viewflipper);
        tvNotFound = v.findViewById(R.id.tvNotFound);


    }


    public void showData() {

        //getshippingproduct();
        categoryarraylist = new ArrayList<>();
        categoryarraylist = artistDetailsDTO.getCategory();

        if (categoryarraylist.size() <= 0) {
            // categoryarraylist.add(new CategoryModel());
        }
        rv_services_cat.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rv_services_cat.addItemDecoration(new ItemDecorationAlbumColumns(
                2,
                1));

        Collections.sort(categoryarraylist, new Comparator<CategoryModel>() {
            @Override
            public int compare(CategoryModel lhs, CategoryModel rhs) {
                return lhs.getCat_name().compareTo(rhs.getCat_name());
            }
        });

        for (int i = 0; i < categoryarraylist.size(); i++) {

            if (categoryarraylist.get(i).getIs_map().equalsIgnoreCase("1")) {
                categoryarraylist.remove(i);
            }
        }

        adapterCategoryServices = new AdapterCategoryShippingServices(getActivity(), categoryarraylist, onItemListener);
        rv_services_cat.setAdapter(adapterCategoryServices);

        if (categoryarraylist.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            adapterCategoryServices.notifyDataSetChanged();
        }


        subCateModelArrayList = new ArrayList<>();
        subCateModelArrayList = artistDetailsDTO.getSubcategory();


        if (subCateModelArrayList.size() <= 0) {
            subCateModelArrayListnew.addAll(subCateModelArrayList);
        }
        productDTOArrayListnew = new ArrayList<>();
        if (subCateModelArrayListnew.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 1));


            Collections.sort(subCateModelArrayListnew, new Comparator<SubCateModel>() {
                @Override
                public int compare(SubCateModel lhs, SubCateModel rhs) {
                    return lhs.getCat_name().compareTo(rhs.getCat_name());
                }
            });
            subCateAdapter = new SubCateShippingAdapter(getActivity(), subCateModelArrayListnew, onsubItemListener, userDTO.getUser_id());
            rv_services_sub_car.setAdapter(subCateAdapter);
        } else {
            if (categoryarraylist.size() >= 0) {
                tvNotFound.setVisibility(View.GONE);

            } else {
                tvNotFound.setVisibility(View.VISIBLE);

            }
            rv_services_sub_car.setVisibility(View.GONE);

        }

    }


    private ArrayList<SubCateModel> getIndexByProperty(String yourString) {
        ArrayList<SubCateModel> temo = new ArrayList<>();

        for (int i = 0; i < subCateModelArrayList.size(); i++) {
            if (subCateModelArrayList != null && subCateModelArrayList.get(i).getCat_id().equalsIgnoreCase(yourString)) {
                temo.add(subCateModelArrayList.get(i));
            }
        }
        return temo;
    }

    private OnSelectedItemListener onItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String name, String shipping, String mylocation) {

            catidmain = categoryarraylist.get(position).getId();
            subCateModelArrayListnew = getIndexByProperty(catidmain);

            if (subCateModelArrayList.size() <= 0) {
                subCateModelArrayListnew.addAll(subCateModelArrayList);
            }

            if (subCateModelArrayListnew.size() > 0) {
                tvNotFound.setVisibility(View.GONE);
                rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 1));

                subCateAdapter = new SubCateShippingAdapter(getActivity(), subCateModelArrayListnew, onsubItemListener, userDTO.getUser_id());
                rv_services_sub_car.setAdapter(subCateAdapter);
            } else {
                tvNotFound.setVisibility(View.VISIBLE);
                rv_services_sub_car.setVisibility(View.GONE);
            }
            subCateAdapter.notifyDataChanged(subCateModelArrayListnew);
            viewflipper.showNext();

        }
    };
    private OnSelectedItemListener onsubItemListener = new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String name, String shipping, String mylocation) {

            serviceparms.put(Consts.ARTIST_ID, userDTO.getUser_id());
            serviceparms.put(Consts.SUB_CAT_ID, selectionString);
            serviceparms.put(Consts.SUB_CAT_NAME, name);
            getShippingType();

            // productDTOArrayListnew= getIndexByPropertyproduct(catidmain,subid);
            //   adapterServices.notifyDataSetChanged();
            // adapterServices = new AdapterServices(ServiceShippingFragment.this, productDTOArrayListnew);
            // rv_services.setAdapter(adapterServices);
            //  viewflipper.showNext();
            // dialogProduct(selectionString,position,name,shipping,mylocation);
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

    }


    public void dialogProduct(String rider1, String sshipping, String smylocation, String name, String subid, String maxpricee) {


        dialogEditProduct.show();

        tvtitle.setText(name);
        close_ship_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditProduct.dismiss();
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditProduct.dismiss();

            }
        });

        rider = rider1;
        subcatid = subid;
        shipping = sshipping;
        mylocation = smylocation;
        Log.e("smylocation", "" + smylocation);

        edt_s_price.setText(maxpricee);
        if (smylocation.equalsIgnoreCase("1")) {
            Log.e("smylocation", "1 ma gayu");
            current_check.setChecked(false);
            enterlocationlinear.setVisibility(View.GONE);
            live_check.setChecked(true);
        } else if (smylocation.equalsIgnoreCase("2")) {
            Log.e("smylocation", "2 ma gayu");
            current_check.setChecked(true);
            enterlocationlinear.setVisibility(View.VISIBLE);
            live_check.setChecked(false);
            enterlocationlinear.setVisibility(View.VISIBLE);
        } else if (smylocation.equalsIgnoreCase("0")) {
            Log.e("smylocation", "3 ma gayu");
            current_check.setChecked(true);
            enterlocationlinear.setVisibility(View.VISIBLE);
            live_check.setChecked(false);
            mylocation = "2";

       //     current_check.setChecked(false);
        }


        // Toast.makeText(baseActivity, "rider---"+rider1, Toast.LENGTH_SHORT).show();
        if (rider1.equalsIgnoreCase("0")) {
            shipping_check.setChecked(true);
        } else if (rider1.equalsIgnoreCase("1")) {
            rider_check.setChecked(true);
        } else if (rider1.equalsIgnoreCase("2")) {
            flexible_check.setChecked(true);
        } else {
            shipping_check.setChecked(false);
            rider_check.setChecked(true);
            rider = "1";
            flexible_check.setChecked(false);
        }



        live_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mylocation = "1";
                    current_check.setChecked(false);
                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                } else {
                    mylocation = "0";
                    //  Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
                }
            }
        });

        current_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mylocation = "2";
                    live_check.setChecked(false);
                    enterlocationlinear.setVisibility(View.VISIBLE);
                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                } else {
                    mylocation = "0";
                    enterlocationlinear.setVisibility(View.GONE);
                    //  Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
                }
            }
        });



        tvYesPro.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Geocoder coder = new Geocoder(getActivity());
                            List<Address> address;
                            GeoPoint p1 = null;

                            address = coder.getFromLocationName(shipping_etLocationD.getText().toString(), 5);

                            Address location = address.get(0);
                            current_lat = location.getLatitude();
                            current_long = location.getLongitude();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (NetworkManager.isConnectToInternet(getActivity())) {


                            if (current_check.isChecked()) {
                                if (!currentt_location_flag.equalsIgnoreCase("0") && shipping_etLocationD.getText().toString().equalsIgnoreCase("")) {
                                    Toast.makeText(baseActivity, "Please Enter Your Address", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (edt_s_price.getText().toString().equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), "Please Enter Price", Toast.LENGTH_LONG).show();
                            } else {
                                maxprice = edt_s_price.getText().toString();
                                addProduct();
                            }

                            //  }

                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }

                    }
                });

    }

    public void addProduct() {

        if (!shipping_check.isChecked() && !rider_check.isChecked() && !flexible_check.isChecked()) {
            rider = "3";
        }
        parmsaddproduct.put(Consts.SUB_CAT_ID, subcatid);
        parmsaddproduct.put(Consts.SHIPPING, shipping);
        parmsaddproduct.put("rider", rider);
        parmsaddproduct.put(Consts.MY_LOCATION, mylocation);
        parmsaddproduct.put("house_no", shipping_ed_society.getText().toString());
        parmsaddproduct.put("address", shipping_etLocationD.getText().toString());
        parmsaddproduct.put("lat", String.valueOf(current_lat));
        parmsaddproduct.put("long", String.valueOf(current_long));
        parmsaddproduct.put(Consts.MAX_PRICE, maxprice);

        Log.e("parmsaddproduct", "" + parmsaddproduct.toString());
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_SERVICE_SHIPPING, parmsaddproduct, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    Log.e("parmsaddproduct", "res :-" + response.toString());
                    ProjectUtils.showToast(getActivity(), msg);
                    dialogEditProduct.dismiss();
                    subCateAdapter.notifyDataChanged(subCateModelArrayListnew);
                    //subCateAdapter=new SubCateShippingAdapter(getActivity(),subCateModelArrayListnew,onsubItemListener,userDTO.getUser_id());
                    //  rv_services_sub_car.setAdapter(subCateAdapter);
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                    dialogEditProduct.dismiss();
                }
            }
        });
    }


}
