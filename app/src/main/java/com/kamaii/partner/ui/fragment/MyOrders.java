package com.kamaii.partner.ui.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.util.Log.e;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static freemarker.template.utility.StringUtil.capitalize;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.SystemClock;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.HomeScreenFragment;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.CancelBookingDialogBinding;
import com.kamaii.partner.databinding.DialogStartPreparationBinding;
import com.kamaii.partner.databinding.DialogThankyouBinding;
import com.kamaii.partner.databinding.FragmentMyOrdersBinding;
import com.kamaii.partner.databinding.NewOrderRequestBottomDialogLayoutBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.MyService;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.SliderAdapter;
import com.kamaii.partner.ui.adapter.newbooking.AdapterNewOrderRequest;
import com.kamaii.partner.ui.adapter.newbooking.AdapterPreparationCart;
import com.kamaii.partner.ui.adapter.newbooking.AdapterTotalOrders;
import com.kamaii.partner.ui.adapter.newbooking.OrderNewAdapter;
import com.kamaii.partner.ui.models.HomePageModel;
import com.kamaii.partner.ui.models.SiderModel;
import com.kamaii.partner.ui.models.newbooking.LunchDinnerDatum;
import com.kamaii.partner.ui.models.newbooking.NewOrders;
import com.kamaii.partner.ui.models.newbooking.PendingDatum;
import com.kamaii.partner.ui.models.newbooking.ProductsArray;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyOrders extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    boolean isPLAYING = false;
    HashMap<String, String> selectedidHashmap;
    String address_radio_txt = "Home";
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    public static int AUTOCOMPLETE_REQUEST_CODE = 10;
    private HashMap<String, String> parms = new HashMap<>();

    FragmentMyOrdersBinding binding;
    private BaseActivity baseActivity;
    private SharedPrefrence prefrence;
    String tiffin_order_tracker;
    private SharedPreferences firebase;
    String other_order_tracker;
    ProgressDialog progressDialog;
    private HashMap<String, String> paramsUpdate;
    private ArtistDetailsDTO artistDetailsDTO;
    String devicetoken = "";
    String tracker = "0";
    private HashMap<String, String> parmsearning = new HashMap<>();
    String address_flag = "";
    public BottomSheetDialog addressBottomSheet;
    public CustomTextView no_rider_found_txt, change_address_btn;
    public CustomTextViewBold address_submit;
    public CustomEditText location_etx, house_no_etx, society_name_etx;
    public String house_no_str = "", society_name_str = "", street_address_str = "";
    LinearLayout customAddress;
    LinearLayout address_lay_dest_location;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    public static String final_user_address = "";
    private ArrayList<HomePageModel> homePageArrayList = new ArrayList<>();
    private ArrayList<SiderModel> tvideoModelArrayList = new ArrayList<>();
    String yt_image_link = "";
    String share_profile_link = "";
    String refer_img_link = "";
    String cat_image_link = "";
    String online_msg = "";
    String offline_msg = "";
    RelativeLayout address_relative;
    String landmark_name = "";
    double lats = 0.0;
    double longs = 0.0;
    Location mylocation;
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    String live_address_str = "";
    String kono_data;
    List<LunchDinnerDatum> lunchDinnerArraylist;
    String todayorder;
    List<PendingDatum> pendingOrders;
    List<LunchDinnerDatum> manageOrderArrayList;
    List<NewOrders> orderArrayList;
    List<ProductsArray> preparationArray;
    String lunch_order;
    List<String> dropdownArraylist;
    String dinner_order;
    private UserDTO userDTO;
    String lunch_or_order, total_order;
    AdapterTotalOrders adapterTotalOrders;
    String order_ids;
    private String TAG = MyOrders.class.getSimpleName();
    BottomSheetDialog OrderRequestdialog;
    NewOrderRequestBottomDialogLayoutBinding bottomDialogBinding;
    int check = 0;
    CountDownTimer cT;
    boolean accept = false;
    CancelBookingDialogBinding cancelBinding;
    public Dialog cancelBookingDialog;
    private HashMap<String, String> paramsDecline;
    String notification_name = "0";
    String messagebody = "";
    DialogStartPreparationBinding dialogStartPreparationBinding;
    Dialog dialogPreparation;
    FusedLocationProviderClient fusedLocationProviderClient;
    int version = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        dialogPreparation = new Dialog(getContext());
        parmsearning.put(Consts.ARTIST_ID, userDTO.getUser_id());
        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        baseActivity.headerNameTV.setVisibility(View.VISIBLE);

        baseActivity.menuLeftIV.setVisibility(View.VISIBLE);
        baseActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        Bundle bundle = getArguments();

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionCode;

        if (bundle != null) {

            notification_name = bundle.getString("notification_name");
            messagebody = bundle.getString("messagebody");
            Log.e("notification_name", " 11:-  " + notification_name + " messagebody " + messagebody);
        }
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.headerNameTV.setText("My Orders");
        baseActivity.swOnOff.setVisibility(View.VISIBLE);
        baseActivity.tvOnOff.setVisibility(View.VISIBLE);

        cancelBookingDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        cancelBookingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelBookingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelBookingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        cancelBookingDialog.setContentView(R.layout.cancel_booking_dialog);
        cancelBookingDialog.setCancelable(false);
        cancelBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.cancel_booking_dialog, null, false);
        cancelBookingDialog.setContentView(cancelBinding.getRoot());

        OrderRequestdialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);
        bottomDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.new_order_request_bottom_dialog_layout, null, false);
        OrderRequestdialog.setContentView(bottomDialogBinding.getRoot());
        OrderRequestdialog.setCancelable(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (NetworkManager.isConnectToInternet(getContext())) {

            getArtist("0");
            getBookings("");
            getEarning();


            //  getBottomSheetDialog();
            //getOrders();
        } else {
            Fragment fragment = new CheckInternetFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }

        baseActivity.swOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (artistDetailsDTO != null) {
                        paramsUpdate = new HashMap<>();
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        if (artistDetailsDTO.getCab_booking_flag().equalsIgnoreCase("1")) {
                            //  Toast.makeText(getActivity(), "Your booking is running", Toast.LENGTH_SHORT).show();

                            baseActivity.swOnOff.setChecked(true);
                            return;
                        } else {
                            if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                                paramsUpdate.put(Consts.IS_ONLINE, "0");

                                //  binding.partnerTxt.setText(online_msg);
                                Log.e("online_offline", "online 1" + Consts.IS_ONLINE);
                                isOnline("0");
                            } else {
                                paramsUpdate.put(Consts.IS_ONLINE, "1");
                                //  binding.partnerTxt.setText(offline_msg);
                                Log.e("online_offline", "online 2" + Consts.IS_ONLINE);
                                isOnline("1");
                            }
                        }
                    } else {
                        baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
                        //   binding.trialbtnSubmit.setVisibility(View.GONE);
                        // Toast.makeText(getActivity(), getResources().getString(R.string.incomplete_profile_msg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (baseActivity.swOnOff.isChecked()) {
                        baseActivity.swOnOff.setChecked(false);
                        // baseActivity.stopService(mServiceIntent);

                    } else {
                        baseActivity.swOnOff.setChecked(true);
                    }
                    //     Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
                }
            }
        });

        getHomeScreenCliskListeners();
        return binding.getRoot();
    }

    private void findPlace() {

        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(baseActivity);

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

    }

    public void addAddress() {

        addAddressHashmap.put("addresstype", address_radio_txt);
        Log.e("Live_ADDRESS", "" + address_radio_txt);

        Log.e("addAddressHashmap", "" + addAddressHashmap.toString());
        new HttpsRequest(Consts.UPDATE_ADD_ADDRESS, addAddressHashmap, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    addressBottomSheet.dismiss();
                    Log.e("ADD_ADDRESS_API", "" + response.toString());
                } else {

                }
            }
        });
    }

    public void getEarning() {

        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_TODAYS_EARNING, parmsearning, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {
                    Log.e("Vider", "2");

                    try {

                        Log.e("earning", "" + response.toString());


                        String total_booking = response.getString("total_booking");
                        String total_earning = response.getString("total_earning");
                        address_flag = response.getString("address_added");

                        //   binding.totalBooking.setText(total_booking);

                        if (address_flag.equalsIgnoreCase("1")) {

                            showAddressDialog();
                            getMyLocation();

                        }
                        double tot_earn = Double.parseDouble(total_earning);
                        //  binding.todaysEarn.setText(String.valueOf(new DecimalFormat("##.##").format(tot_earn)));
                        gettvideo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {

        Log.e("Live_ADDRESS", "getmylocation called");

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {
                    //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

                    live_latitude = location.getLatitude();
                    Log.e("Live_ADDRESS", "" + live_latitude);
                    live_longitude = location.getLongitude();
                    Log.e("Live_ADDRESS", "" + live_longitude);

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        // Toast.makeText(mContext, "8", Toast.LENGTH_LONG).show();

                        List<Address> addresses = geocoder.getFromLocation(live_latitude, live_longitude, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        add = add + "\n" + obj.getCountryName();
                        add = add + "\n" + obj.getCountryCode();
                        add = add + "\n" + obj.getAdminArea();
                        add = add + "\n" + obj.getPostalCode();
                        add = add + "\n" + obj.getSubAdminArea();
                        add = add + "\n" + obj.getLocality();
                        add = add + "\n" + obj.getSubThoroughfare();

                        if (live_address_str.equals("")) {

                            live_address_str = obj.getAddressLine(0);
                            location_etx.setText(live_address_str);
                            addAddressHashmap.put("street_address", live_address_str);
                            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(live_latitude));
                            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(live_longitude));
                        }
                        Log.e("Live_ADDRESS", "" + live_address_str);
                        //   Log.e("IGA123214", "Address" + add);
                        // Toast.makeText(this, "Address=>" + add,
                        // Toast.LENGTH_SHORT).show();

                        // TennisAppActivity.showDialog(add);

                        //  etLocationD.setText(obj.getAddressLine(0));
                        //  drag_location_etx.setText(obj.getAddressLine(0));
                        // parms.put(Consts.LOCATION, obj.getAddressLine(0));


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void showAddressDialog() {

        addressBottomSheet = new BottomSheetDialog(getActivity());
        addressBottomSheet.setContentView(R.layout.activity_address_bottom_dialog);
        change_address_btn = addressBottomSheet.findViewById(R.id.change_address_btn);
        //RelativeLayout address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        addressBottomSheet.setCancelable(false);
        RadioGroup add_addressradioGroup;
        ImageView back = addressBottomSheet.findViewById(R.id.address_dialog_close_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBottomSheet.dismiss();
            }
        });

        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Poppins-Regular.otf");


        CustomTextViewBold ctvbTitle = addressBottomSheet.findViewById(R.id.ctvbTitle);
        CustomTextViewBold select_add_txt = addressBottomSheet.findViewById(R.id.select_add_txt);
        select_add_txt.setVisibility(View.GONE);
        ctvbTitle.setText("Business Address Verification");
        View add_view1 = addressBottomSheet.findViewById(R.id.add_view1);
        location_etx = addressBottomSheet.findViewById(R.id.location_etx);
        house_no_etx = addressBottomSheet.findViewById(R.id.house_no_etx);
        society_name_etx = addressBottomSheet.findViewById(R.id.society_name_etx);
        customAddress = addressBottomSheet.findViewById(R.id.customAddress);
        add_addressradioGroup = addressBottomSheet.findViewById(R.id.add_addressradioGroup);
        address_submit = addressBottomSheet.findViewById(R.id.address_submit);

        location_etx.setTypeface(font2);
        house_no_etx.setTypeface(font2);
        society_name_etx.setTypeface(font2);

        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        address_lay_dest_location = addressBottomSheet.findViewById(R.id.address_lay_dest_location);
        // LinearLayout lll = bottomSheetDialog.findViewById(R.id.lll);
        RecyclerView recyclerView = addressBottomSheet.findViewById(R.id.address_recycler);
        recyclerView.setVisibility(View.GONE);
        address_lay_dest_location.setVisibility(View.GONE);
        add_view1.setVisibility(View.GONE);
        customAddress.setVisibility(View.VISIBLE);

        change_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();
            }
        });


        add_addressradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.add_home_radio:
                        address_radio_txt = "Home";
                        // Toast.makeText(mContext, "Home  : --"+address_radio_txt, Toast.LENGTH_SHORT).show();
                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_work_radio:
                        address_radio_txt = "Work";
                        // Toast.makeText(mContext, "Work  : --"+address_radio_txt, Toast.LENGTH_SHORT).show();

                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                    case R.id.add_other_radio:
                        address_radio_txt = "Others";
                        // Toast.makeText(mContext, "Others  : --"+address_radio_txt, Toast.LENGTH_SHORT).show();

                        addAddressHashmap.put("addresstype", address_radio_txt);
                        break;

                }
            }
        });


        address_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                house_no_str = house_no_etx.getText().toString();
                society_name_str = society_name_etx.getText().toString();
                street_address_str = location_etx.getText().toString();

                if (house_no_str.equalsIgnoreCase("") || house_no_str.isEmpty() || house_no_str.equals(null)) {
                    Toast.makeText(baseActivity, "Please enter House no. / Flat no. / Floor / Building", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    addAddressHashmap.put("house_no", house_no_str);
                    addAddressHashmap.put("society_name", society_name_str);
                    addAddress();
                    //binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

                    //etLocationD.setText(house_no_str + ", " + society_name_str + ", " + street_address_str);

                    customAddress.setVisibility(View.GONE);
                    address_relative.setVisibility(View.VISIBLE);
                    address_lay_dest_location.setVisibility(View.VISIBLE);

                    addressBottomSheet.dismiss();
                }

            }
        });
        address_lay_dest_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findPlace();

            }
        });
        addressBottomSheet.show();
    }
    private void getHomeScreenCliskListeners() {

        binding.guideLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new ContactUs(), "8");

            }
        });
        binding.onlineServiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new Services(), "80000");

            }
        });
        binding.completeOrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new NewBookings(), "8000");

            }
        });
        binding.requestAmtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new Wallet(), "800");

            }
        });
        binding.kamaiiWalletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new Wallet(), "80");

            }
        });
        binding.stopRingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(MyService.class)) {

                    Intent intent = new Intent(getContext(), MyService.class);
                    getContext().stopService(intent);

                }

            }
        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) baseActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public void isOnline(String onoff) {

        Log.e("devicetoken", "myorder");

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }

        Log.e("devicetoken", "1" + " devicetoken " + "->" + devicetoken + " onoff " + "->" + onoff + " getDeviceName " + "->" + getDeviceName() + " uid " + "->" + userDTO.getUser_id());
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.onlineOffline(userDTO.getUser_id(), onoff, devicetoken, getDeviceName());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("BASE0", "" + response.toString());

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        Log.e("BASE1", "" + message);
                        Log.e("BASE2", "" + sstatus);


                        if (sstatus == 1) {

                            if (onoff.equalsIgnoreCase("1")) {
                               /* if (!isMyServiceRunning(mYourService.getClass())) {
                                    baseActivity.startService(mServiceIntent);
                                }*/
                            } else {
                                //baseActivity.stopService(mServiceIntent);
                            }
                            Log.e("BASSHIVAMKOLJH", "" + sstatus);
                            getArtist("1");
                        } else {
                            Log.e("BASSHIVAMKOLJH", "" + sstatus);
                            baseActivity.swOnOff.setChecked(false);
                           /* Toast.makeText(getActivity(), message + "--",
                                    LENGTH_LONG).show();*/
                        }


                    } else {
                        Log.e("devicetoken","111111");
                      /*  Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();
*/

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
/*

                Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();
*/


            }
        });

    }

    private void getBottomSheetDialog() {
        binding.myorderscr.setVisibility(View.GONE);
        binding.bottomdialog.setVisibility(View.VISIBLE);
        // bottomDialogBinding.txtarivaltimer.setText("12");
/*        bottomDialogBinding.orderItemRv.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterNewOrderRequest orderRequest = new AdapterNewOrderRequest(getContext(),pendingOrders);
        bottomDialogBinding.orderItemRv.setAdapter(orderRequest);

        bottomDialogBinding.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bottomDialogBinding.llDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderRequestdialog.dismiss();
            }
        });

        OrderRequestdialog.show();*/

        binding.orderItemRv.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterNewOrderRequest orderRequest = new AdapterNewOrderRequest(getContext(), pendingOrders);
        binding.orderItemRv.setAdapter(orderRequest);

        binding.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binding.originalBtnLinear.setVisibility(View.GONE);
                    binding.duplicateBtnLinear.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);
                    booking("1");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });

        binding.llDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("cancel_price",""+pendingOrders.get(0).getPrice());
                cancelBinding.cancelBookingTxt.setText("Cancellation charges of " + Html.fromHtml("&#8377;" + pendingOrders.get(0).getPrice()) + " will applay.");
                cancelBinding.cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelBookingDialog.dismiss();
                    }
                });

                cancelBinding.confirmbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            cancelBookingDialog.dismiss();
                            binding.originalBtnLinear.setVisibility(View.GONE);
                            binding.duplicateBtnLinear.setVisibility(View.VISIBLE);
//                    Intent intent = new Intent(getActivity(), MyService.class);
//                    getActivity().stopService(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        accept = true;
                        //  autodecline(bookingid);
                        autodeclineHoldOrders(order_ids);
                    }
                });

                cancelBookingDialog.show();
                // OrderRequestdialog.dismiss();
            }
        });

        //  OrderRequestdialog.show();

    }

    public void getArtist(String value) {

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getArtistByid(userDTO.getUser_id(), userDTO.getUser_id(), devicetoken);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (artistDetailsDTO.getIs_online().equalsIgnoreCase("0")) {

                                Log.e("SHOW_DIALOG", "condtion ma gayu");

                                //                              binding.partnerTxt.setText(online_msg);

                            } else {
                                Log.e("SHOW_DIALOG", "else ma gayu");

//                                binding.partnerTxt.setText(offline_msg);

                            }


                            if (value.equalsIgnoreCase("0")) {
                                showDatfirst();

                            } else {
                                showDataddd();
                            }


                        } else {

                        }


                    } else {

//                        Toast.makeText(getActivity(), "Please try again later",
//                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();
*/

            }
        });


    }

    public void autodeclineHoldOrders(String bid) {

        Log.e("auto_decline", " autodeclineHoldOrders called ");

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, "");
        paramsDecline.put("cat_id", "0");
        paramsDecline.put("sub_id", "0");
        paramsDecline.put("third_id", "0");
        paramsDecline.put("lat", "0");
        paramsDecline.put("lang", "0");
        paramsDecline.put("passvalue", "0");

        //   progressDialog.show();
        Log.e("decline_RES", " params " + paramsDecline.toString());
        new HttpsRequest(Consts.DECLINE_BOOKING_ON_BTN_CLICK, paramsDecline, getActivity()).stringPosttwo("decline_booking2", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  progressDialog.dismiss();
                if (flag) {

                    Log.e("decline_RES", " res " + response.toString());

                    try {

                        if (isPLAYING){
                            isPLAYING = true;

                            MediaPlayer mediaPlayer = new MediaPlayer();

                            if (!mediaPlayer.isPlaying()){
                                AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                                descriptor.close();

                                mediaPlayer.prepare();
                                mediaPlayer.setVolume(1f, 1f);
                                mediaPlayer.setLooping(false);
                                mediaPlayer.start();


                                binding.bottomdialog.setVisibility(View.GONE);


                                getBookings("");

                            }else {
                                mediaPlayer.stop();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
    }

    public void booking(String req) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        // Call<ResponseBody> callone = api.booking_operation(bookingid, req, userDTO.getUser_id());
        Call<ResponseBody> callone = api.booking_operation(order_ids, req, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("ACCEPT_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            binding.bottomdialog.setVisibility(View.GONE);

                            accept = true;
                            baseActivity.loadHomeFragment(new MyOrders(), "cab");


                        } else {

                        }


                    } else {

                    /*    Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

               /* Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    private void getOrders() {

        Log.e("getorders params",""+order_ids);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getallbookingfromid(userDTO.getUser_id(), order_ids);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("getOrders", " 1234 " + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {

                                orderArrayList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<NewOrders>>() {
                                }.getType();
                                orderArrayList = (ArrayList<NewOrders>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                Log.e("orderArrayList", " 2 " + orderArrayList.size());


                                setOrderAdapter();
                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            showData();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();

                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    private void setOrderAdapter() {

        binding.myorderNewRv.setLayoutManager(new LinearLayoutManager(getContext()));
        OrderNewAdapter orderNewAdapter = new OrderNewAdapter(getContext(), orderArrayList, userDTO, notification_name, messagebody);
        binding.myorderNewRv.setAdapter(orderNewAdapter);
    }

    private void showPreparationDialog(String flag) {


        if (flag.equalsIgnoreCase("1")) {
            dialogStartPreparationBinding.cartPrepqration.setLayoutManager(new LinearLayoutManager(getContext()));

            getStartPreparationData(messagebody);
        } else if (flag.equalsIgnoreCase("2")) {
            dialogStartPreparationBinding.cartPrepqration.setLayoutManager(new LinearLayoutManager(getContext()));

            getRequestForRider(messagebody);
            dialogStartPreparationBinding.orderStatusFirst.setText("Request For Rider");
            dialogStartPreparationBinding.timerRelativeFirst.setBackground(getContext().getResources().getDrawable(R.color.light_pink_bg));

            dialogStartPreparationBinding.preparationDialogYes.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.pink_color));
            //dialogStartPreparationBinding.preparationDialogCancel.setBackground(getContext().getResources().getDrawable(R.drawable.));
        }
        dialogStartPreparationBinding.preparationDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPreparation.dismiss();
            }
        });
        dialogStartPreparationBinding.preparationDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag.equalsIgnoreCase("1")){
                    Log.e("start","startpreparation called");
                    getStartPreparation(messagebody, "2");
                }else {
                    Log.e("start","requestrider called");

                    getRequestForrider(messagebody);
                }
            }
        });

    }

    public void getRequestForRider(String mes) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRest apiRest = retrofit.create(apiRest.class);
        Call<ResponseBody> call = apiRest.getallbookingfromrequestforriderid(userDTO.getUser_id(), mes);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String res = responseBody.string();

                        Log.e("getallbookingfromst", "" + res);

                        JSONObject object = new JSONObject(res);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            JSONObject jsonObject = object.getJSONObject("data");
                            String endtime = jsonObject.getString("end_time");
                            String endtime_tracker = jsonObject.getString("end_time_tracker");
                            String end_time_diff = jsonObject.getString("end_time_diff");
                            preparationArray = new ArrayList<>();

                            Type getpetDTO = new TypeToken<List<ProductsArray>>() {
                            }.getType();
                            preparationArray = (ArrayList<ProductsArray>) new Gson().fromJson(jsonObject.getJSONArray("products_array").toString(), getpetDTO);

                            Log.e("orderArrayList", " 2 " + orderArrayList.size());

                            AdapterPreparationCart cart = new AdapterPreparationCart(getContext(), preparationArray);
                            dialogStartPreparationBinding.cartPrepqration.setAdapter(cart);

                            if(endtime_tracker.equalsIgnoreCase("0")){

                                SimpleDateFormat format = new SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                            e("tracker", "1");
                            Date date = null;
                            try {
                                e("tracker", "2");

                                //   date = format.parse("2022-02-12T18:00:00");
                                date = format.parse(endtime);
                                dialogStartPreparationBinding.dialogCounter.setDate(date);//countdown starts
                                e("tracker", "3");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            dialogStartPreparationBinding.dialogCounter.setIsShowingTextDesc(true);
                            dialogStartPreparationBinding.dialogCounter.setTextSize(35);
                            dialogStartPreparationBinding.dialogCounter.setMaxTimeUnit(TimeUnits.DAY);
                            dialogStartPreparationBinding.dialogCounter.setTextColor(getContext().getResources().getColor(R.color.green));
                            e("tracker", "4");

                            dialogStartPreparationBinding.dialogCounter.setListener(new Counter.Listener() {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    e("TAG", "onTick: Counter - " + millisUntilFinished);

                                    if (millisUntilFinished == 0) {
                                        dialogStartPreparationBinding.dialogCounter.setVisibility(View.GONE);
                                        dialogStartPreparationBinding.preparationTimeFirst.setVisibility(View.VISIBLE);
                                    }
                                    e("tracker", "5");

                                }

                                @Override
                                public void onTick(long days, long hours, long minutes, long seconds) {
                                }
                            });

                            }else {
                                dialogStartPreparationBinding.dialogCounter.setVisibility(View.GONE);
                                dialogStartPreparationBinding.orderDelayTimer.setVisibility(View.VISIBLE);
                                dialogStartPreparationBinding.preparationTimeFirst.setVisibility(View.VISIBLE);

                                int stoppedMilliseconds = 0;

                                String chronoText = end_time_diff;
                                String array[] = chronoText.split(":");
                                if (array.length == 2) {
                                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                                            + Integer.parseInt(array[1]) * 1000;
                                } else if (array.length == 3) {
                                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                                            + Integer.parseInt(array[1]) * 60 * 1000
                                            + Integer.parseInt(array[2]) * 1000;
                                }

                                dialogStartPreparationBinding.orderDelayTimer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                                dialogStartPreparationBinding.orderDelayTimer.start();

                            }

                            dialogPreparation.show();

                        }


                    } else {
                        dialogPreparation.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void getStartPreparationData(String mes) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRest apiRest = retrofit.create(apiRest.class);
        Call<ResponseBody> call = apiRest.getallbookingfromstartpreprationid(userDTO.getUser_id(), mes);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String res = responseBody.string();

                        Log.e("getallbookingfromst", "start " + res);

                        JSONObject object = new JSONObject(res);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            JSONObject jsonObject = object.getJSONObject("data");
                            String endtime = jsonObject.getString("end_time");
                            String endtime_tracker = jsonObject.getString("end_time_tracker");
                            String end_time_diff = jsonObject.getString("end_time_diff");

                            preparationArray = new ArrayList<>();

                            Type getpetDTO = new TypeToken<List<ProductsArray>>() {
                            }.getType();
                            preparationArray = (ArrayList<ProductsArray>) new Gson().fromJson(jsonObject.getJSONArray("products_array").toString(), getpetDTO);

                            Log.e("orderArrayList", " 2 " + orderArrayList.size());

                            AdapterPreparationCart cart = new AdapterPreparationCart(getContext(), preparationArray);
                            dialogStartPreparationBinding.cartPrepqration.setAdapter(cart);

                            if (endtime_tracker.equalsIgnoreCase("0")){

                                   SimpleDateFormat format = new SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                            e("tracker", "1");
                            Date date = null;
                            try {
                                e("tracker", "2");

                                //   date = format.parse("2022-02-12T18:00:00");
                                date = format.parse(endtime);
                                dialogStartPreparationBinding.dialogCounter.setDate(date);//countdown starts
                                e("tracker", "3");

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            dialogStartPreparationBinding.dialogCounter.setIsShowingTextDesc(true);
                            dialogStartPreparationBinding.dialogCounter.setTextSize(35);
                            dialogStartPreparationBinding.dialogCounter.setMaxTimeUnit(TimeUnits.DAY);
                            dialogStartPreparationBinding.dialogCounter.setTextColor(getContext().getResources().getColor(R.color.green));
                            e("tracker", "4");

                            dialogStartPreparationBinding.dialogCounter.setListener(new Counter.Listener() {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    e("TAG", "onTick: Counter - " + millisUntilFinished);

                                    if (millisUntilFinished == 0) {
                                        dialogStartPreparationBinding.dialogCounter.setVisibility(View.GONE);
                                        dialogStartPreparationBinding.preparationTimeFirst.setVisibility(View.VISIBLE);
                                    }
                                    e("tracker", "5");

                                }

                                @Override
                                public void onTick(long days, long hours, long minutes, long seconds) {
                                }
                            });

                            }else {
                                dialogStartPreparationBinding.dialogCounter.setVisibility(View.GONE);
                                dialogStartPreparationBinding.orderDelayTimer.setVisibility(View.VISIBLE);
                                dialogStartPreparationBinding.preparationTimeFirst.setVisibility(View.VISIBLE);

                                int stoppedMilliseconds = 0;

                                String chronoText = end_time_diff;
                                String array[] = chronoText.split(":");
                                if (array.length == 2) {
                                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                                            + Integer.parseInt(array[1]) * 1000;
                                } else if (array.length == 3) {
                                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                                            + Integer.parseInt(array[1]) * 60 * 1000
                                            + Integer.parseInt(array[2]) * 1000;
                                }

                                dialogStartPreparationBinding.orderDelayTimer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                                dialogStartPreparationBinding.orderDelayTimer.start();

                            }



                            dialogPreparation.show();

                        }


                    } else {
                        dialogPreparation.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getRequestForrider(String b_id) {

        progressDialog.show();
        selectedidHashmap = new HashMap<>();
        selectedidHashmap.put(Consts.ARTIST_ID, userDTO.getUser_id());
        selectedidHashmap.put(Consts.BOOKING_ID, b_id);

        new HttpsRequest(Consts.REQUEST_FOR_RIDER_MULTIPLE_API, selectedidHashmap, getContext()).stringPosttwo("REQUESTFORRIDER", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                e("REQUEST_FOR_RIDER_API", "" + response.toString());
                if (flag) {

                    dialogPreparation.dismiss();
                    ((BaseActivity) getContext()).loadHomeFragment(new MyOrders(), "cab");

                } else {
                  //  ((BaseActivity) getContext()).loadHomeFragment(new MyOrders(), "cab");
                }
            }
        });
    }

    private void getStartPreparation(String b_id, String req) {

        e("getStartPreparatparams", " b_id " + b_id + " req: " + req);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.booking_operation(b_id, req, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        e("getOrders", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                dialogPreparation.dismiss();

                                ((BaseActivity) getContext()).loadHomeFragment(new MyOrders(), "cab");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            dialogPreparation.dismiss();

                            // showData();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();
                        dialogPreparation.dismiss();


                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "You can call now!!", Toast.LENGTH_SHORT).show();
            }
        }

        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }


    public void gettvideo() {
        Log.e("Vider", "1");// ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_PARTNER_HOMEPAGE_DATA, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {
                    Log.e("Vider", "2");

                    try {
                        Log.e("Vider", "3");

                        Log.e("SliderRES", "" + response.toString());
                        homePageArrayList = new ArrayList<>();
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<SiderModel>>() {
                        }.getType();

                        tvideoModelArrayList = (ArrayList<SiderModel>) new Gson().fromJson(response.getJSONObject("data").getJSONArray("catelog").toString(), getpetDTO);
                        binding.pagerhome.setAdapter(new SliderAdapter(getActivity(), tvideoModelArrayList));

                        HomePageModel homePageModel = new HomePageModel();
                        JSONObject jsonObject = response.getJSONObject("data");
                        homePageModel.setCatelogMsg(jsonObject.getString("catelog_msg"));
                        homePageModel.setYoutubeimage(jsonObject.getString("yt_image"));
                        homePageModel.setRefer_img(jsonObject.getString("refer_img"));
                        homePageModel.setCat_image(jsonObject.getString("cat_image"));
                        homePageModel.setShare_profile(jsonObject.getString("share_profile"));
                        homePageModel.setTotalCat(jsonObject.getString("total_cat"));
                        homePageModel.setTotalItem(jsonObject.getString("total_item"));
                        homePageModel.setTotalVisitProfile(jsonObject.getString("total_visit_profile"));
                        homePageModel.setItemVisit(jsonObject.getString("item_visit"));

                        homePageModel.setToday_earning(jsonObject.getString("today_earning"));
                        homePageModel.setTotal_earning(jsonObject.getString("total_earning"));
                        homePageModel.setRequest_amount(jsonObject.getString("request_amount"));
                        homePageModel.setKamaii_wallet(jsonObject.getString("kamaii_wallet"));
                        homePageModel.setOnline_services(jsonObject.getString("online_services"));
                        homePageModel.setToday_orders(jsonObject.getString("today_orders"));
                        homePageModel.setPending_orders(jsonObject.getString("pending_orders"));
                        homePageModel.setCompleted_orders(jsonObject.getString("completed_orders"));

                        offline_msg = jsonObject.getString("catelog_msg_off");
                        online_msg = jsonObject.getString("catelog_msg_on");

                        yt_image_link = jsonObject.getString("yt_image_link");
                        share_profile_link = jsonObject.getString("share_profile_link");
                        refer_img_link = jsonObject.getString("refer_img_link");
                        cat_image_link = jsonObject.getString("cat_image_link");

                        binding.partnerTxt.setText(homePageModel.getCatelogMsg());
                        binding.totalCat.setText(homePageModel.getTotalCat());
                        binding.totalItems.setText(homePageModel.getTotalItem());
                        //     binding.totalVisitorProfile.setText(homePageModel.getTotalVisitProfile());
                        //     binding.totalItemVisit.setText(homePageModel.getItemVisit());

                        Typeface typeface1 = Typeface.createFromAsset(baseActivity.getAssets(), "Lato-Heavy.ttf");
                        Typeface typeface2 = Typeface.createFromAsset(baseActivity.getAssets(), "Lato-Semibold.ttf");

                        binding.onlineService.setTypeface(typeface1);
                        binding.onlineServiceTxt.setTypeface(typeface2);
                        binding.onlineService.setText(homePageModel.getOnline_services());


                        binding.todayOrders.setTypeface(typeface1);
                        binding.todayOrdersTxt.setTypeface(typeface2);
                        binding.todayOrders.setText(homePageModel.getToday_orders());

                        binding.pendingOrders.setTypeface(typeface1);
                        binding.pendingOrdersTxt.setTypeface(typeface2);
                        binding.pendingOrders.setText(homePageModel.getPending_orders());

                        binding.completeOrders.setTypeface(typeface1);
                        binding.completeOrdersTxt.setTypeface(typeface2);
                        binding.completeOrders.setText(homePageModel.getCompleted_orders());

                        binding.todaysEarning.setTypeface(typeface1);
                        binding.todaysEarningTxt.setTypeface(typeface2);
                        binding.todaysEarning.setText(homePageModel.getToday_earning());

                        binding.totalEarning.setTypeface(typeface1);
                        binding.totalEarningTxt.setTypeface(typeface2);
                        binding.totalEarning.setText(homePageModel.getTotal_earning());

                        binding.requestAmt.setTypeface(typeface1);
                        binding.requestAmtTxt.setTypeface(typeface2);
                        binding.requestAmt.setText(homePageModel.getRequest_amount());

                        binding.kamaiiWallet.setTypeface(typeface1);
                        binding.kamaiiWalletTxt.setTypeface(typeface2);
                        binding.kamaiiWallet.setText(homePageModel.getKamaii_wallet());


                        Glide.with(getActivity()).load(homePageModel.getCat_image()).into(binding.homescreenImage);
                        Log.e("SliderRES", "" + homePageModel.getCat_image());
                        Glide.with(getActivity()).load(homePageModel.getYoutubeimage()).into(binding.homescreenImage2);
                        Glide.with(getActivity()).load(homePageModel.getShare_profile()).into(binding.homescreenImage3);
                        Glide.with(getActivity()).load(homePageModel.getRefer_img()).into(binding.homescreenImage5);

                        Log.e("tvideoModelArrayList", "" + tvideoModelArrayList.toString());

                        // tvideoModelArrayList = homePageArrayList.get(0).getCatelog();

                        getImageClick();
                        binding.dots.attachViewPager(binding.pagerhome);

                        Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });


    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.pagerhome.getCurrentItem() < tvideoModelArrayList.size() - 1) {
                            binding.pagerhome.setCurrentItem(binding.pagerhome.getCurrentItem() + 1);
                        } else {
                            binding.pagerhome.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }

    public void getFragment(String value) {
        switch (value) {

            case "1":
                baseActivity.navItemIndex = 23;
                baseActivity.loadHomeFragment(new MainserviceFragment(), "profile");
                break;
            case "2":
                baseActivity.navItemIndex = 24;
                baseActivity.loadHomeFragment(new TVideoFragment(), "tvideo");
                break;
            case "3":
                baseActivity.navItemIndex = 25;
                baseActivity.loadHomeFragment(new AddReferFragment(), "notification");
                break;
            case "4":
                baseActivity.navItemIndex = 26;
                baseActivity.loadHomeFragment(new MyEarning(), "earn");
                break;
            case "5":
                baseActivity.navItemIndex = 27;
                baseActivity.loadHomeFragment(new PromotionFragment(), "promotion");
                break;
            case "6":
                baseActivity.navItemIndex = 28;
                baseActivity.loadHomeFragment(new ContactUs(), "history");
                break;
            case "7":
                baseActivity.navItemIndex = 29;
                baseActivity.loadHomeFragment(new NewBookings(), "jobs");
                break;
            case "8":
                baseActivity.navItemIndex = 30;
                baseActivity.loadHomeFragment(new Wallet(), "wallet");
                break;
            case "9":
                baseActivity.navItemIndex = 31;
                baseActivity.loadHomeFragment(new PaidFrag(), "history");
                break;

        }
    }

    public void getImageClick() {

        binding.homescreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cat_image_link.equalsIgnoreCase("0")) {

                    getFragment(cat_image_link);
                } else {
                  //  addServices();
                }
            }
        });

        binding.homescreenImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!yt_image_link.equalsIgnoreCase("0")) {

                    getFragment(yt_image_link);
                } else {

                    baseActivity.navItemIndex = 32;
                    baseActivity.loadHomeFragment(new TVideoFragment(), "20");

                    // startActivity(new Intent(getActivity(), YoutubePlaylistActivity.class));
                }
            }
        });

        binding.homescreenImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!share_profile_link.equalsIgnoreCase("0")) {

                    getFragment(share_profile_link);
                } else {
                    baseActivity.navItemIndex = 33;
                    baseActivity.loadHomeFragment(new PromotionFragment(), "90");
                }
            }
        });

        binding.homescreenImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!refer_img_link.equalsIgnoreCase("0")) {

                    getFragment(refer_img_link);
                } else {
                    baseActivity.navItemIndex = 34;
                    baseActivity.loadHomeFragment(new AddReferFragment(), "notification");
                }
            }
        });

        binding.knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 38;
                baseActivity.loadHomeFragment(new SafetyFragment(), "200");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

             if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

                Log.e("PLACE_APU", "called");
                Place place = Autocomplete.getPlaceFromIntent(data);

                final_user_address = place.getAddress();
                landmark_name = place.getName();

                lats = place.getLatLng().latitude;
                longs = place.getLatLng().longitude;


                addAddressHashmap.put("street_address", landmark_name + final_user_address);
                addAddressHashmap.put(Consts.LATITUDE, String.valueOf(lats));
                addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(longs));

                location_etx.setText(landmark_name + ", " + place.getAddress());
                address_lay_dest_location.setVisibility(View.GONE);
                address_relative.setVisibility(View.GONE);
            }
        }
    }

    public void getBookings(String tracker) {

        Log.e("kono_data tracjer:-- ", "" + tracker);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllbooking(userDTO.getUser_id(), tracker,String.valueOf(version));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("NEW_BOOKING_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            JSONObject dataObject = object.getJSONObject("data");
                            tiffin_order_tracker = dataObject.getString("tiffin_order_tracker");
                            other_order_tracker = dataObject.getString("other_order_tracker");
                            kono_data = dataObject.getString("kono_data");
                            todayorder = dataObject.getString("todayorder");
                            lunch_order = dataObject.getString("lunch_order");
                            dinner_order = dataObject.getString("dinner_order");
                            lunch_or_order = dataObject.getString("lunch_or_order");
                            total_order = dataObject.getString("total_order");
                            order_ids = dataObject.getString("order_ids");

                            try {
                                pendingOrders = new ArrayList<>();
                                manageOrderArrayList = new ArrayList<>();
                                dropdownArraylist = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<PendingDatum>>() {
                                }.getType();
                                pendingOrders = (ArrayList<PendingDatum>) new Gson().fromJson(dataObject.getJSONArray("pending_data").toString(), getpetDTO);

                                if (pendingOrders.size() == 0) {
                                    Type getpetDTO1 = new TypeToken<List<LunchDinnerDatum>>() {
                                    }.getType();
                                    manageOrderArrayList = (ArrayList<LunchDinnerDatum>) new Gson().fromJson(dataObject.getJSONArray("lunch_dinner_data").toString(), getpetDTO1);

                                    Type getpetDTO2 = new TypeToken<List<String>>() {
                                    }.getType();
                                    dropdownArraylist = (ArrayList<String>) new Gson().fromJson(dataObject.getJSONArray("drop_down_array").toString(), getpetDTO2);

                                    Log.e("todayorder", " 1 " + todayorder);
                                    Log.e("BOOKING_TRACK", " 2 " + manageOrderArrayList.size());
                                    Log.e("BOOKING_TRACK", " 2 " + pendingOrders.size());


                                    if(pendingOrders.size() == 0 && manageOrderArrayList.size() == 0){

                                        baseActivity.loadHomeFragment(new HomeScreenFragment(),"home");
                                    }else {
                                        binding.myorderscr.setVisibility(View.VISIBLE);
                                    }



                                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, dropdownArraylist);
                                    binding.orderSpinner.setAdapter(adapter);
                                    loadSpinner();

                                    if (lunch_order.equalsIgnoreCase("0")) {
                                        binding.lunchOrderBtn.setVisibility(View.GONE);
                                    }else {
                                        binding.lunchOrderBtn.setVisibility(View.VISIBLE);

                                    }
                                    if (dinner_order.equalsIgnoreCase("0")) {
                                        binding.dinnerOrderBtn.setVisibility(View.GONE);
                                    }else {
                                        binding.dinnerOrderBtn.setVisibility(View.VISIBLE);

                                    }
                                    setBookingAdapter();
                                    getOrders();
                                } else {
                                    showData();
                                }

                                binding.todaysordercount.setText(Html.fromHtml(todayorder));

                                if (tiffin_order_tracker.equalsIgnoreCase("0")) {
                                    //   binding.tiffinLinear.setVisibility(View.GONE);
                                    //  binding.tiffinOtherView.setVisibility(View.GONE);

                                    binding.card.setVisibility(View.GONE);
                                } else if (other_order_tracker.equalsIgnoreCase("0")) {
                                    //binding.otherLinear.setVisibility(View.GONE);
                                    //binding.tiffinOtherView.setVisibility(View.GONE);
                                    binding.card.setVisibility(View.GONE);
                                }


                                if (lunch_order.equalsIgnoreCase("0")) {
                                    binding.lunchOrderBtn.setVisibility(View.GONE);
                                } else if (dinner_order.equalsIgnoreCase("0")) {
                                    binding.dinnerOrderBtn.setVisibility(View.GONE);

                                }
                                //showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else if (sstatus == 0){
                            progressDialog.dismiss();
                            baseActivity.loadHomeFragment(new HomeScreenFragment(),"home");

                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();
                        baseActivity.loadHomeFragment(new HomeScreenFragment(),"home");

                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                baseActivity.loadHomeFragment(new HomeScreenFragment(),"home");

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    public void getBookingsCopy(String tracker) {

        Log.e("kono_data tracjer:-- ", "" + tracker);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllbooking(userDTO.getUser_id(), tracker,String.valueOf(version));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("NEW_BOOKING_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            JSONObject dataObject = object.getJSONObject("data");
                            tiffin_order_tracker = dataObject.getString("tiffin_order_tracker");
                            other_order_tracker = dataObject.getString("other_order_tracker");
                            kono_data = dataObject.getString("kono_data");
                            todayorder = dataObject.getString("todayorder");
                            lunch_order = dataObject.getString("lunch_order");
                            dinner_order = dataObject.getString("dinner_order");
                            lunch_or_order = dataObject.getString("lunch_or_order");
                            total_order = dataObject.getString("total_order");
                            order_ids = dataObject.getString("order_ids");

                            try {

                                manageOrderArrayList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<LunchDinnerDatum>>() {
                                }.getType();
                                manageOrderArrayList = (ArrayList<LunchDinnerDatum>) new Gson().fromJson(dataObject.getJSONArray("lunch_dinner_data").toString(), getpetDTO);

                                Type getpetDTO2 = new TypeToken<List<String>>() {
                                }.getType();

                                Log.e("todayorder", " 1 " + todayorder);
                                Log.e("BOOKING_TRACK", " 2 " + manageOrderArrayList.size());
                                Log.e("BOOKING_TRACK", " 2 " + manageOrderArrayList.size());

                                binding.todaysordercount.setText(Html.fromHtml(todayorder));

                                if (tiffin_order_tracker.equalsIgnoreCase("0")) {
                                    //                                  binding.tiffinLinear.setVisibility(View.GONE);
//                                    binding.tiffinOtherView.setVisibility(View.GONE);
                                    binding.card.setVisibility(View.GONE);

                                } else if (other_order_tracker.equalsIgnoreCase("0")) {
                                    //    binding.otherLinear.setVisibility(View.GONE);
                                    //    binding.tiffinOtherView.setVisibility(View.GONE);
                                    binding.card.setVisibility(View.GONE);

                                }

                                if (lunch_order.equalsIgnoreCase("0")) {
                                    binding.lunchOrderBtn.setVisibility(View.GONE);
                                } else if (dinner_order.equalsIgnoreCase("0")) {
                                    binding.dinnerOrderBtn.setVisibility(View.GONE);

                                }

                                setBookingAdapter();
                                //showData();
                                getOrders();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();

                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    private void showData() {

        baseActivity.getVersion();

        progressDialog.dismiss();
        Log.e("todayorder", " 2 " + todayorder);

        dialogStartPreparationBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_start_preparation, null, false);
        dialogPreparation.setContentView(dialogStartPreparationBinding.getRoot());

        if (notification_name.equalsIgnoreCase("100122")) {
            showPreparationDialog("1");

        } else if (notification_name.equalsIgnoreCase("100123")) {
            showPreparationDialog("2");

        }

        if (lunch_or_order.equalsIgnoreCase("1")) {
            binding.dinnerOrderBtn.setBackground(getResources().getDrawable(R.color.lunch_dinner_bg));
            binding.dinnerOrderTxt.setTextColor(getResources().getColor(R.color.white));
            binding.dinnerOrder.setTextColor(getResources().getColor(R.color.white));

            binding.lunchOrderBtn.setBackground(getResources().getDrawable(R.color.light_green_bg));
            binding.lunchOrderTxt.setTextColor(getResources().getColor(R.color.dark_blue_color));
            binding.lunchOrder.setTextColor(getResources().getColor(R.color.dark_blue_color));

            binding.dinnerOrderBtn.setEnabled(false);
            binding.dinnerOrderBtn.setClickable(false);
            binding.lunchOrderBtn.setClickable(true);
            binding.lunchOrderBtn.setClickable(true);

        } else {

            binding.lunchOrderBtn.setBackground(getResources().getDrawable(R.color.lunch_dinner_bg));
            binding.lunchOrderTxt.setTextColor(getResources().getColor(R.color.white));
            binding.lunchOrder.setTextColor(getResources().getColor(R.color.white));

            binding.dinnerOrderBtn.setBackground(getResources().getDrawable(R.color.light_green_bg));
            binding.dinnerOrderTxt.setTextColor(getResources().getColor(R.color.dark_blue_color));
            binding.dinnerOrder.setTextColor(getResources().getColor(R.color.dark_blue_color));

            binding.dinnerOrderBtn.setEnabled(true);
            binding.dinnerOrderBtn.setClickable(true);
            binding.lunchOrderBtn.setClickable(false);
            binding.lunchOrderBtn.setClickable(false);
        }

        if(lunch_order.equalsIgnoreCase("1")){
            binding.lunchOrder.setText(Html.fromHtml(lunch_order));

        }else {
            binding.lunchOrder.setText(Html.fromHtml(lunch_order));
        }
        if (dinner_order.equalsIgnoreCase("1")) {
            binding.dinnerOrder.setText(Html.fromHtml(dinner_order));

        } else {
            binding.dinnerOrder.setText(Html.fromHtml(dinner_order));
        }




        if (pendingOrders.size() != 0) {


            int seocndserver = Integer.parseInt(pendingOrders.get(0).getSecond());

            long ssss = seocndserver * 1000;

            cT = new CountDownTimer(ssss, 1000) {

                public void onTick(long millisUntilFinished) {

                    Log.e("tracker", "6");

                    // int va = (int) ((millisUntilFinished % 60000) / 1000);
                    int va = (int) ((millisUntilFinished) / 1000);
                    Log.e("tracker", "show_timer:-- " + va);

                    binding.txtarivaltimer.setText("" + String.format("%02d", va));

                }

                public void onFinish() {

                    if (accept == false) {

                        try {
                            //  Log.e("tracker", "onfinish if ma gayu" + " getHold_order " + artistBookingsList.get(count_flag).getHold_order());

                            Intent intent = new Intent(getActivity(), MyService.class);
                            getActivity().stopService(intent);
                            decline(order_ids);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        Log.e("tracker", "onfinish else ma gayu");
                    }

                }
            };
            cT.start();

            getBottomSheetDialog();

        }

        if (kono_data.equalsIgnoreCase("0")) {
            binding.tiffin.setTextColor(getContext().getResources().getColor(R.color.dark_blue_color));
            binding.other.setTextColor(getContext().getResources().getColor(R.color.gray_wallet));
            binding.ordersDinnerLunch.setVisibility(View.VISIBLE);
        } else {
            binding.tiffin.setTextColor(getContext().getResources().getColor(R.color.gray_wallet));
            binding.other.setTextColor(getContext().getResources().getColor(R.color.dark_blue_color));
            binding.ordersDinnerLunch.setVisibility(View.GONE);

        }



        binding.lunchOrderBtn.setOnClickListener(this);
        binding.dinnerOrderBtn.setOnClickListener(this);
        binding.tiffinLinear.setOnClickListener(this);
        binding.otherLinear.setOnClickListener(this);
        binding.orderSpinner.setOnItemSelectedListener(this);

        //loadSpinner();

    }

    public void decline(String bid) {

        Log.e("auto_decline", " decline called ");

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, "");
        paramsDecline.put("cat_id", "0");
        paramsDecline.put("sub_id", "0");
        paramsDecline.put("third_id", "0");
        paramsDecline.put("lat", "0");
        paramsDecline.put("lang", "0");
        paramsDecline.put("passvalue", "0");

        progressDialog.show();

        new HttpsRequest(Consts.AUTO_DECLINE_BOOKING_API, paramsDecline, getActivity()).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    //     binding.trialbtnSubmit.setVisibility(View.VISIBLE);

                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();

                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.bottomdialog.setVisibility(View.GONE);


                        getBookings("");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
    }

    void loadSpinner() {

        binding.orderSpinner.setOnItemSelectedListener(this);

        binding.orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSpinnerClick(dropdownArraylist.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getSpinnerClick(String str) {


        progressDialog.show();
//        String spinner_item =  binding.orderSpinner.getSelectedItem().toString();

        Log.e("getLunchDinnerOrder", " item:-- " + str);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getallbookingartistdateclick(userDTO.getUser_id(), str,kono_data);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("getallbookingarti", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            JSONObject dataObject = object.getJSONObject("data");
                            tiffin_order_tracker = dataObject.getString("tiffin_order_tracker");
                            other_order_tracker = dataObject.getString("other_order_tracker");
                            kono_data = dataObject.getString("kono_data");
                            todayorder = dataObject.getString("todayorder");
                            lunch_order = dataObject.getString("lunch_order");
                            dinner_order = dataObject.getString("dinner_order");
                            lunch_or_order = dataObject.getString("lunch_or_order");
                            order_ids = dataObject.getString("order_ids");

                            try {

                                manageOrderArrayList = new ArrayList<>();
                                // dropdownArraylist = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<LunchDinnerDatum>>() {
                                }.getType();
                                manageOrderArrayList = (ArrayList<LunchDinnerDatum>) new Gson().fromJson(dataObject.getJSONArray("lunch_dinner_data").toString(), getpetDTO);

                                Type getpetDTO2 = new TypeToken<List<String>>() {
                                }.getType();
                                //   dropdownArraylist = (ArrayList<String>) new Gson().fromJson(dataObject.getJSONArray("drop_down_array").toString(), getpetDTO2);

                                Log.e("BOOKING_TRACK", " 2 " + dropdownArraylist.toString());
                                Log.e("BOOKING_TRACK", " 2 " + manageOrderArrayList.size());

                                if (lunch_order.equalsIgnoreCase("0")) {
                                    binding.lunchOrderBtn.setVisibility(View.GONE);
                                }else {
                                    binding.lunchOrderBtn.setVisibility(View.VISIBLE);

                                }
                                 if (dinner_order.equalsIgnoreCase("0")) {
                                    binding.dinnerOrderBtn.setVisibility(View.GONE);
                                }else {
                                     binding.dinnerOrderBtn.setVisibility(View.VISIBLE);

                                 }

                                setBookingAdapter();
                                getOrders();
                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();

                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });
    }

    private void setBookingAdapter() {

        binding.totalOrderRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterTotalOrders = new AdapterTotalOrders(getContext(), manageOrderArrayList, kono_data);
        binding.totalOrderRv.setAdapter(adapterTotalOrders);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tiffin_linear:
                getBookings("0");
                break;
            case R.id.other_linear:
                getBookings("1");
                break;
            case R.id.lunch_order_btn:
                binding.lunchOrderBtn.setBackground(getContext().getResources().getDrawable(R.color.lunch_dinner_bg));
                binding.lunchOrderTxt.setTextColor(getContext().getResources().getColor(R.color.white));
                binding.lunchOrder.setTextColor(getContext().getResources().getColor(R.color.white));

                binding.dinnerOrderBtn.setBackground(getContext().getResources().getDrawable(R.color.light_green_bg));
                binding.dinnerOrderTxt.setTextColor(getContext().getResources().getColor(R.color.dark_blue_color));
                binding.dinnerOrder.setTextColor(getContext().getResources().getColor(R.color.dark_blue_color));

                binding.dinnerOrderBtn.setEnabled(true);
                binding.dinnerOrderBtn.setClickable(true);
                binding.lunchOrderBtn.setClickable(false);
                binding.lunchOrderBtn.setClickable(false);
                getLunchDinnerOrder("0");
                break;
            case R.id.dinner_order_btn:

                binding.dinnerOrderBtn.setEnabled(false);
                binding.dinnerOrderBtn.setClickable(false);
                binding.lunchOrderBtn.setClickable(true);
                binding.lunchOrderBtn.setClickable(true);


                binding.dinnerOrderBtn.setBackground(getContext().getResources().getDrawable(R.color.lunch_dinner_bg));
                binding.dinnerOrderTxt.setTextColor(getContext().getResources().getColor(R.color.white));
                binding.dinnerOrder.setTextColor(getContext().getResources().getColor(R.color.white));

                binding.lunchOrderBtn.setBackground(getContext().getResources().getDrawable(R.color.light_green_bg));
                binding.lunchOrderTxt.setTextColor(getContext().getResources().getColor(R.color.dark_blue_color));
                binding.lunchOrder.setTextColor(getContext().getResources().getColor(R.color.dark_blue_color));

                getLunchDinnerOrder("1");
                break;
        }
    }

    private void getLunchDinnerOrder(String tracker) {


        progressDialog.show();
        String spinner_item = binding.orderSpinner.getSelectedItem().toString();

        Log.e("getLunchDinnerOrder", "tracker:-- " + tracker + " item:-- " + spinner_item);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getLunchDinnerOrder(userDTO.getUser_id(), tracker, spinner_item);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("getLunchDinnerOrder", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            JSONObject dataObject = object.getJSONObject("data");
                            tiffin_order_tracker = dataObject.getString("tiffin_order_tracker");
                            other_order_tracker = dataObject.getString("other_order_tracker");
                            kono_data = dataObject.getString("kono_data");
                            todayorder = dataObject.getString("todayorder");
                            lunch_order = dataObject.getString("lunch_order");
                            dinner_order = dataObject.getString("dinner_order");
                            lunch_or_order = dataObject.getString("lunch_or_order");
                            order_ids = dataObject.getString("order_ids");

                            try {

                                manageOrderArrayList = new ArrayList<>();
                                //       dropdownArraylist = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<LunchDinnerDatum>>() {
                                }.getType();
                                manageOrderArrayList = (ArrayList<LunchDinnerDatum>) new Gson().fromJson(dataObject.getJSONArray("lunch_dinner_data").toString(), getpetDTO);

                                Type getpetDTO2 = new TypeToken<List<String>>() {
                                }.getType();
                                //      dropdownArraylist = (ArrayList<String>) new Gson().fromJson(dataObject.getJSONArray("drop_down_array").toString(), getpetDTO2);

                                Log.e("BOOKING_TRACK", " 2 " + dropdownArraylist.toString());
                                Log.e("BOOKING_TRACK", " 2 " + manageOrderArrayList.size());

                                if (lunch_order.equalsIgnoreCase("0")) {
                                    binding.lunchOrderBtn.setVisibility(View.GONE);
                                } else if (dinner_order.equalsIgnoreCase("0")) {
                                    binding.dinnerOrderBtn.setVisibility(View.GONE);

                                }
                                setBookingAdapter();
                                getOrders();
                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();

                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //   Toast.makeText(getContext(), dropdownArraylist.get(position).toString(), Toast.LENGTH_SHORT).show();
        //  if (++check > 1) {
        getSpinnerClick(dropdownArraylist.get(position).toString());
        // }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDatfirst() {

        if (artistDetailsDTO != null) {
            //   baseActivity.swOnOff.setVisibility(View.VISIBLE);
            //  baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            //  binding.lay.setVisibility(View.VISIBLE);
            //      binding.sliderLayout.setVisibility(View.VISIBLE);
            //   binding.relativeTwo.setVisibility(View.VISIBLE);
            //    binding.thirdRelative.setVisibility(View.VISIBLE);
            //   binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            //   binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //     binding.fifthLinear.setVisibility(View.VISIBLE);
            //     binding.sixthLinear.setVisibility(View.VISIBLE);
            //    binding.seventhLinear.setVisibility(View.VISIBLE);
            //        binding.eightLinear.setVisibility(View.VISIBLE);
            //   binding.layfindtrip.setVisibility(View.VISIBLE);

            //baseActivity.tvOnOff.setText(g.getResources().getString(R.string.online));
            //     binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            //     binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);

          /*  if (!isMyServiceRunning(mYourService.getClass())) {
                baseActivity.startService(mServiceIntent);
            }*/
            //  binding.layfindtrip.setVisibility(View.VISIBLE);
            //   binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            //    binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //      binding.trialbtnSubmit.setVisibility(View.GONE);
            //     binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));
            // binding.pulsator.start();
            //    binding.pulsator.setVisibility(View.VISIBLE);

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            //     binding.lay.setVisibility(View.VISIBLE);
            //   binding.sliderLayout.setVisibility(View.VISIBLE);
            //      binding.relativeTwo.setVisibility(View.VISIBLE);
            //     binding.thirdRelative.setVisibility(View.VISIBLE);
            //       binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            //       binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //        binding.fifthLinear.setVisibility(View.VISIBLE);
            //        binding.sixthLinear.setVisibility(View.VISIBLE);
            //       binding.seventhLinear.setVisibility(View.VISIBLE);
            //       binding.eightLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            //       binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            //       baseActivity.stopService(mServiceIntent);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            //       binding.layfindtrip.setVisibility(View.GONE);
            //       binding.todaysStatusLinear.setVisibility(View.GONE);
            //       binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            //        binding.trialbtnSubmit.setVisibility(View.GONE);
            //       binding.txtoffonline.setTextColor(Color.BLACK);
            //       binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            //      binding.pulsator.setVisibility(View.GONE);

            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }

    public void showDataddd() {

        if (artistDetailsDTO != null) {
            //  baseActivity.swOnOff.setVisibility(View.VISIBLE);
            //  baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            //           binding.lay.setVisibility(View.VISIBLE);
            //     binding.sliderLayout.setVisibility(View.VISIBLE);
            //           binding.relativeTwo.setVisibility(View.VISIBLE);
            //      binding.thirdRelative.setVisibility(View.VISIBLE);
            //           binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            //           binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //          binding.fifthLinear.setVisibility(View.VISIBLE);
            //         binding.sixthLinear.setVisibility(View.VISIBLE);
            //           binding.seventhLinear.setVisibility(View.VISIBLE);
            //        binding.eightLinear.setVisibility(View.VISIBLE);

            //      binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            //           binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            //           binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            //   binding.layfindtrip.setVisibility(View.VISIBLE);
            //         binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            //       binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //   binding.trialbtnSubmit.setVisibility(View.GONE);
            //       binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));

            // binding.pulsator.start();
            //  binding.pulsator.setVisibility(View.VISIBLE);
            try {
                final MediaPlayer mediaPlayer = new MediaPlayer();

                AssetFileDescriptor descriptor = getActivity().getAssets().openFd("online.mpeg");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mediaPlayer.prepare();
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        mediaPlayer.stop();
                    }
                }.start();

            } catch (Exception e) {
                e.printStackTrace();
            }

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            //      binding.lay.setVisibility(View.VISIBLE);
            //     binding.trialbtnSubmit.setVisibility(View.GONE);

            //         binding.sliderLayout.setVisibility(View.VISIBLE);
            //       binding.relativeTwo.setVisibility(View.VISIBLE);
            //    binding.thirdRelative.setVisibility(View.VISIBLE);
            //       binding.thirdRelative.setVisibility(View.GONE);
            //    binding.forthRelative.setVisibility(View.VISIBLE);
            //       binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //        binding.fifthLinear.setVisibility(View.VISIBLE);
            //       binding.sixthLinear.setVisibility(View.VISIBLE);
            //       binding.seventhLinear.setVisibility(View.VISIBLE);
            //      binding.eightLinear.setVisibility(View.VISIBLE);


            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            //      binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            //      binding.layfindtrip.setVisibility(View.GONE);
            //      binding.todaysStatusLinear.setVisibility(View.GONE);
            //      binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            //      binding.txtoffonline.setTextColor(Color.BLACK);
            //      binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            //    binding.pulsator.setVisibility(View.GONE);
            try {
                final MediaPlayer mediaPlayer = new MediaPlayer();

                AssetFileDescriptor descriptor = getActivity().getAssets().openFd("offline.mpeg");
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();

                mediaPlayer.prepare();
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();


            } catch (Exception e) {
                e.printStackTrace();
            }
            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }


}