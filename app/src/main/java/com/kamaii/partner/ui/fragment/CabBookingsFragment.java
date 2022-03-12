package com.kamaii.partner.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
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
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.LocationService;
import com.kamaii.partner.databinding.CancelBookingDialogBinding;
import com.kamaii.partner.databinding.DailogArProductBinding;
import com.kamaii.partner.databinding.FragmentCabBookingsBinding;
import com.kamaii.partner.databinding.SlotDataLayoutBinding;
import com.kamaii.partner.interfacess.GetTimeSlotData;
import com.kamaii.partner.service.YourService;
import com.kamaii.partner.ui.activity.AddServicesActivity;
import com.kamaii.partner.ui.activity.EditPersnoalInfo;
import com.kamaii.partner.ui.activity.YoutubePlaylistActivity;

import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.model.CommonServiceModel;
import com.kamaii.partner.model.ThirdCateModel;
import com.kamaii.partner.ui.adapter.AddressBottomSheetAdapter;
import com.kamaii.partner.ui.adapter.OrderItemsAdapter;
import com.kamaii.partner.ui.adapter.SliderAdapter;
import com.kamaii.partner.ui.adapter.SlotDataAdapter;
import com.kamaii.partner.ui.adapter.TimeSlotAdapter;
import com.kamaii.partner.ui.models.CategoryModel;
import com.kamaii.partner.ui.models.Description;
import com.kamaii.partner.ui.models.GuideModel;
import com.kamaii.partner.ui.models.HomePageModel;
import com.kamaii.partner.ui.models.ProductArrayModel;
import com.kamaii.partner.ui.models.SiderModel;
import com.kamaii.partner.ui.models.SlotDataModel;
import com.kamaii.partner.ui.models.SubCateModel;
import com.kamaii.partner.ui.models.TimeSlotModel;
import com.kamaii.partner.ui.models.TypeAddressModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.SpinnerDialog;
import com.kamaii.partner.utils.SpinnerDialogDescription;
import com.kamaii.partner.utils.SpinnerDialogFour;
import com.kamaii.partner.utils.SpinnerDialogSubCate;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.MyService;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.AdapterCabBookings;
import com.kamaii.partner.ui.models.AddressModel;
import com.kamaii.partner.utils.FileUtility;
import com.takusemba.spotlight.OnSpotlightListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.prefs.PreferencesManager;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static com.kamaii.partner.ui.activity.BaseActivity.CURRENT_TAG;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static freemarker.template.utility.StringUtil.capitalize;

public class CabBookingsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GetTimeSlotData {

    public static String final_user_address = "";
    private BaseActivity baseActivity;
    private FragmentCabBookingsBinding binding;
    CustomTextViewBold total_booking, todays_earn;
    private ArrayList<ArtistBooking> artistBookingsList = new ArrayList<>();
    private ArrayList<ArtistBooking> artistBookingsListnew = new ArrayList<>();
    private ArrayList<TimeSlotModel> artistTimeSlotList = new ArrayList<>();
    private ArrayList<ProductArrayModel> productArrayList;
    private LinearLayoutManager mLayoutManager;
    private String TAG = CabBookingsFragment.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parmsartist = new HashMap<>();
    public static HashMap<String, File> paramsFile;
    private HashMap<String, String> paramsucategory = new HashMap<>();
    private HashMap<String, String> paramsthird = new HashMap<>();
    private HashMap<String, String> paramsFour = new HashMap<>();
    public static int AUTOCOMPLETE_REQUEST_CODE = 10;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    List<SlotDataModel> slotDataList;
    SlotDataLayoutBinding binding11;

    private HashMap<String, String> paramsUpdate;
    private HashMap<String, String> paramsDecline;
    private LayoutInflater myInflater;
    private AdapterCabBookings adapterAllBookings;
    Context mContext;
    public static Uri uri = null;
    private ArtistDetailsDTO artistDetailsDTO;
    private String bookingid = "";
    private String hold_order = "";
    String currentdate = "";
    LocalBroadcastManager bm;
    private SharedPreferences firebase;
    String devicetoken = "";
    ProgressDialog progressDialog;
    String dcatid = "", dsubcatid = "", dthirdcatid = "", dclatititute = "", dclongitiute = "";
    String category_id = "", sub_category_id = "", third_id = "", vechilenumber = "", servicename = "", serviceid = "";
    String encodedBase64 = "";
    private HashMap<String, String> parmsSubcatImage = new HashMap<>();
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    HashMap<String, String> getAddressHashmap = new HashMap<>();
    CountDownTimer cT;
    private GoogleMap googleMap;
    boolean accept = false;
    int rider_flag = 0;
    boolean shouldStopLoop = false;
    private ArrayList<HomePageModel> homePageArrayList = new ArrayList<>();
    private ArrayList<SiderModel> tvideoModelArrayList = new ArrayList<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsearning = new HashMap<>();
    private HashMap<String, String> parmsdemobooking = new HashMap<>();
    public Dialog dialogEditProduct, cancelBookingDialog;
    DailogArProductBinding binding1;
    CancelBookingDialogBinding cancelBinding;
    private SpinnerDialog spinnerDialogCate;
    private SpinnerDialogSubCate spinnerDialogsubcate;
    private com.kamaii.partner.utils.SpinnerDialogThird SpinnerDialogThird;
    private SpinnerDialogFour spinnerDialogFour;
    private SpinnerDialogDescription spinnerDialogDes;
    private HashMap<String, String> collectParams;

    private ArrayList<GuideModel> guideList = new ArrayList<>();
    private ArrayList<CategoryModel> categoryarraylist = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListserivce = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew = new ArrayList<>();
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();
    ArrayList<Description> descriptionArrayList = new ArrayList<>();
    ArrayList<CommonServiceModel> fourModelArrayList = new ArrayList<>();
    boolean map_flag = false;
    int current_page = 0;
    int slot_click_current_page = 0;
    boolean slot_click = false;
    Dialog dialog;
    ImageView close;
    ViewPager2 pagerhome;
    DotsIndicator dotsIndicator;
    Dialog demoDialog;
    CustomTextViewBold demo_textview;
    ImageView demo_cancel;
    ListView demodialogListView;
    List<CategoryDTO> categoryList;
    String send_price_str = null;
    String description_id = null;
    String send_img_str = null;

    String slider_image_link = "";
    String yt_image_link = "";
    String share_profile_link = "";
    String refer_img_link = "";
    String cat_image_link = "";
    String online_msg = "";
    String offline_msg = "";

    String switch_step_title = "";
    String switch_step_txt = "";
    String category_step_title = "";
    String category_step_txt = "";
    String service_step_title = "";
    String service_step_txt = "";
    String promotion_step_title = "";
    String promotion_step_txt = "";
    String tvideo_step_title = "";
    String tvideo_step_txt = "";
    String refer_step_title = "";
    String refer_step_txt = "";
    String address_flag = "";

    private static final String INTRO_CARD = "fab_intro";
    private static final String INTRO_SWITCH = "switch_intro";
    private static final String INTRO_CAT = "cat_intro";
    private static final String INTRO_SERVICE = "service_intro";
    private static final String INTRO_REFER = "refer";
    private static final String INTRO_PROMOTION = "promotion";
    private static final String INTRO_VIDEO = "tvideo";
    private static final String INTRO_MENU = "menuicon";
    private static final String INTRO_CHANGE_POSITION = "change_position_intro";
    private static final String INTRO_SEQUENCE = "sequence_intro";
    private boolean isRevealEnabled = true;
    private SpotlightView spotLight;
    PreferencesManager mPreferencesManager;
    HashMap<String, String> findriderHashmap = new HashMap<>();
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    String address_radio_txt = "Home";
    public BottomSheetDialog addressBottomSheet;
    public CustomTextView no_rider_found_txt, change_address_btn;
    public CustomTextViewBold address_submit;
    public CustomEditText location_etx, house_no_etx, society_name_etx;
    public String house_no_str = "", society_name_str = "", street_address_str = "";
    LinearLayout customAddress;
    LinearLayout address_lay_dest_location;
    RelativeLayout address_relative;
    String landmark_name = "";
    double lats = 0.0;
    double longs = 0.0;
    Location mylocation;
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    String live_address_str = "";
    RecyclerView timeslot_recyclerview;
    TimeSlotAdapter timeSlotAdapter;
    FusedLocationProviderClient fusedLocationProviderClient;
    int count_flag = 0;
    PendingIntent pendingIntent;
    Intent mServiceIntent;
    YourService mYourService;
    OrderItemsAdapter orderItemsAdapter;
    List<SlotDataModel> slotDataModelList;
    List<ProductDTO> orderItemList;
    HashMap<String, String> slotMap;
    int version = 0;
    String date_time_time = "";
    String date_time_date = "";
    String date_time_date2 = "";
    String notification_name = "0";

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cab_bookings, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Places.initialize(getActivity(), getResources().getString(R.string.api_key));
        Bundle bundle = getArguments();


        if (bundle != null){

            notification_name = bundle.getString("notification_name");
            Log.e("notification_name"," 11:-  "+notification_name);
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionCode;

       // binding.mapView.onCreate(savedInstanceState);
        paramsDecline = new HashMap<>();
        categoryList = new ArrayList<>();
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");
        myInflater = LayoutInflater.from(getActivity());
        parmsartist.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsdemobooking.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.USER_ID, userDTO.getUser_id());
        parmsearning.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.DEVICE_TOKEN, devicetoken);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        mPreferencesManager = new PreferencesManager(getActivity());

        baseActivity.ivLogo.setVisibility(View.VISIBLE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dailog_ar_product, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());


        dialogEditProduct.setCancelable(true);
        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());


        cancelBookingDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        cancelBookingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelBookingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelBookingDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        cancelBookingDialog.setContentView(R.layout.cancel_booking_dialog);
        cancelBookingDialog.setCancelable(false);
        cancelBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.cancel_booking_dialog, null, false);
        cancelBookingDialog.setContentView(cancelBinding.getRoot());

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        demoDialog = new Dialog(getActivity());
        demoDialog.setContentView(R.layout.demo_booking_dialog);
        demodialogListView = demoDialog.findViewById(R.id.demolistview);
        demo_textview = demoDialog.findViewById(R.id.demodialogTitle);
        demo_cancel = demoDialog.findViewById(R.id.demodialogcancel);

        Typeface font2 = Typeface.createFromAsset(getContext().getAssets(), "Lato-Heavy.ttf");

        demo_textview.setTypeface(font2);
        Log.e("name", "" + prefrence.getValue(Consts.NAME));
        binding.partnerName.setText("Hello " + userDTO.getName() + "!");
        googleApiClient = baseActivity.googleApiClient;
        //baseActivity.setUpGClient();
        //getGuide();
        mYourService = new YourService();
        mServiceIntent = new Intent(baseActivity, mYourService.getClass());

        binding.stopRingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(MyService.class)) {

                    Intent intent = new Intent(getContext(), MyService.class);
                    getContext().stopService(intent);

                }

            }
        });
        binding1.etvechialNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if ((binding1.etvechialNumber.getText().length() + 1 == 3 || binding1.etvechialNumber.getText().length() + 1 == 6 || binding1.etvechialNumber.getText().length() + 1 == 9)) {
                    if (before - count < 0) {
                        binding1.etvechialNumber.setText(binding1.etvechialNumber.getText() + " ");
                        binding1.etvechialNumber.setSelection(binding1.etvechialNumber.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        binding.progress.getProgressDrawable().setColorFilter(
                R.color.pink_color, android.graphics.PorterDuff.Mode.SRC_IN);
        // binding.pulsator.start();

        //Toast.makeText(baseActivity, "uybawetcfgahcg fbajshgdb", LENGTH_SHORT).show();


        Date todayyyy = new Date();
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentdate = formatt.format(todayyyy);

        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);

     //   binding.mapView.onResume();

      /*  binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location buttont
                // googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map

                googleMap.setMyLocationEnabled(true);
                  *//*  MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style);
                    googleMap.setMapStyle(style);*//*

                Location location = googleMap.getMyLocation();

                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
                    //googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition(cameraPosition));

                } else {

                    if (!prefrence.getValue(Consts.LATITUDE).equalsIgnoreCase("")) {
                        LatLng soruce = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));

                        if (soruce != null) {
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(soruce).zoom(14).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                }
            }
        });
*/
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

   /*     binding.profileLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new ArtistProfile(), "8");
            }
        });

        binding.bLinearOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 52;

                baseActivity.loadHomeFragment(new MyEarning(), "8");

            }
        });
        binding.bLinearTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 53;

                baseActivity.loadHomeFragment(new MyEarning(), "8");

            }
        });
       */
        binding.catLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), AddServicesActivity.class));
                //addServices();
            }
        });

        binding.serviceLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 42;

                baseActivity.loadHomeFragment(new MainserviceFragment(), "8");
            }
        });
        binding.todaysEarningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 42;

                baseActivity.loadHomeFragment(new MyEarning(), "8");
            }
        });
        binding.totalEarningCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 42;

                baseActivity.loadHomeFragment(new MyEarning(), "8");
            }
        });

        /*binding.serviceVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 41;
                baseActivity.loadHomeFragment(new MainserviceFragment(), "8");
            }
        });*/


      /*  binding.trialbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefrence.getValue(Consts.IS_ONLINE).equalsIgnoreCase("1")) {

                    demoDialog.show();

                    demo_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            demoDialog.dismiss();
                        }
                    });

                } else {
                    //         Toast.makeText(baseActivity, "Please, first online and try again", Toast.LENGTH_SHORT).show();
                }


            }
        });*/


        baseActivity.headerNameTV.setText("");
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.rvBooking.setLayoutManager(mLayoutManager);

        binding.timeslotRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.timeslotNewRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

/*
        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    adapterAllBookings.filter(newText.toString());

                } else {


                }
                return false;
            }
        });
*/
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getBookings();

            getEarning();

            checkaddress();
            // getSpinnerCategory();

        } else {
            // Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), LENGTH_SHORT).show();
            Fragment fragment = new CheckInternetFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }


        mContext = getActivity();


        paramsFile = new HashMap<>();

        getArtist("0");


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
                        binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
                        binding.layfindtrip.setVisibility(View.GONE);
                        binding.todaysStatusLinear.setVisibility(View.GONE);
                        binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);
                        //   binding.trialbtnSubmit.setVisibility(View.GONE);
                        binding.txtoffonline.setTextColor(Color.BLACK);
                        binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));
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


        binding.llDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelBinding.cancelBookingTxt.setText("Cancellation charges of " + Html.fromHtml("&#8377;" + artistBookingsList.get(count_flag).getPrice()) + " will applay.");
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

                            binding.originalBtnLinear.setVisibility(View.GONE);
                            binding.duplicateBtnLinear.setVisibility(View.VISIBLE);
//                    Intent intent = new Intent(getActivity(), MyService.class);
//                    getActivity().stopService(intent);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        accept = true;
                        //  autodecline(bookingid);
                        autodeclineHoldOrders(hold_order);
                    }
                });

                cancelBookingDialog.show();

            }
        });

        binding.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    binding.originalBtnLinear.setVisibility(View.GONE);
                    binding.duplicateBtnLinear.setVisibility(View.VISIBLE);

             //       binding.mapView.setVisibility(View.GONE);
                    //  binding.trialbtnSubmit.setVisibility(View.GONE);
                    binding.rvBookingNested.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.horizontalscrollBtn.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);

                    if (checkarss(artistBookingsList.get(count_flag).getProduct().get(0).getCategory_id())) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(count_flag).getAddress());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                booking("1");

            }
            // }
        });

        return binding.getRoot();
    }


    public void getSlotDetails() {

        // progressDialog.show();
        Log.e("orderSummeryCount", " clicked " + artistBookingsList.get(artistBookingsList.size() - 1).getNew_order_slot_key());
        slotMap = new HashMap<>();
        slotMap.put("Order_id", artistBookingsList.get(artistBookingsList.size() - 1).getNew_order_slot_key());
        Log.e("orderSummeryCount", " clicked " + slotMap.toString());
        new HttpsRequest(Consts.GET_SLOT_DETAILS_API, slotMap, getActivity()).stringPosttwo("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  progressDialog.dismiss();
                if (flag) {
                    Log.e("SLOT_DATA", "" + response.toString());

                    try {

                        Type getType = new TypeToken<List<SlotDataModel>>() {
                        }.getType();

                        slotDataList = new Gson().fromJson(response.getJSONArray("data").toString(), getType);

                        Log.e("slotDataList", "" + slotDataList.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Dialog alertDialog = new Dialog(getActivity());
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    binding11 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.slot_data_layout, null, false);
                    alertDialog.setContentView(binding11.getRoot());
//                            alertDialog.setCancelable(false);

                    CustomTextViewBold dialog_title = alertDialog.findViewById(R.id.slot_title);
                    ImageView close_slot_dialog = alertDialog.findViewById(R.id.close_slot_dialog);

                    dialog_title.setText("Order Details");
                    close_slot_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    RecyclerView slotdata_recyclerview = alertDialog.findViewById(R.id.slotdata_recyclerview);
                    SlotDataAdapter slotDataAdapter = new SlotDataAdapter(getActivity(), slotDataList);
                    slotdata_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    slotdata_recyclerview.setAdapter(slotDataAdapter);

                    alertDialog.show();
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SpotlightSequence.getInstance(getActivity(), null)
                        .addSpotlight(binding.catLinear, category_step_title, category_step_txt, INTRO_CAT)
                        .addSpotlight(binding.serviceLinear, service_step_title, service_step_txt, INTRO_SERVICE)
                        .addSpotlight(baseActivity.swOnOff, switch_step_title, switch_step_txt, INTRO_SWITCH)
                        .addSpotlight(binding.homescreenImage3, promotion_step_title, promotion_step_txt, INTRO_PROMOTION)
                        *//* .addSpotlight(binding.homescreenImage5, refer_step_title, refer_step_txt, INTRO_REFER)
                         .addSpotlight(binding.homescreenImage2, tvideo_step_title, tvideo_step_txt, INTRO_VIDEO)
                        *//*.startSequence();
            }
        }, 400);*/
    }


    public void booking(String req) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        // Call<ResponseBody> callone = api.booking_operation(bookingid, req, userDTO.getUser_id());
        Call<ResponseBody> callone = api.booking_operation(hold_order, req, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        //   Log.e("ACCEPT_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            binding.layaccpet.setVisibility(View.GONE);

                            accept = true;
                            baseActivity.loadHomeFragment(new CabBookingsFragment(), "cab");


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

    public void getArtistcpoy() {
        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }
        Log.e("confirmation_for_start", "getArtistcpoy called");

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

                        //   Log.e("Artist_copy", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {
/*
                        Toast.makeText(getActivity(), "Please try again later",
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

             /*   Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();
*/

            }
        });

    }

    public void getImageClick() {

        binding.homescreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cat_image_link.equalsIgnoreCase("0")) {

                    getFragment(cat_image_link);
                } else {
                    addServices();
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

    public void addServices() {
        dialogEditProduct.show();
        //  dialogProduct();

    }

/*
    public void dialogProduct() {

        paramsUpdate = new HashMap<>();
        paramsFile = new HashMap<>();
        paramsucategory = new HashMap<>();
        paramsthird = new HashMap<>();
        paramsFour = new HashMap<>();


        binding1.tvcat.setText(getResources().getString(R.string.all_categories));
        binding1.tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
        binding1.tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
        binding1.tvservice.setText(getResources().getString(R.string.all_service_name));
        binding1.etvechialNumber.setText("");

        binding1.tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                    //        Toast.makeText(getActivity(), getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvFiltersub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                    //        Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvhird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                    //     Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourModelArrayList.size() > 0) {
                    spinnerDialogFour.showSpinerDialog();
                } else {
                    //   Toast.makeText(getActivity(), getResources().getString(R.string.no_service_name_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvNoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDTOS.clear();
                binding1.tvFiltersub.setVisibility(View.GONE);
                binding1.tvhird.setVisibility(View.GONE);
                binding1.tvservice.setVisibility(View.GONE);
                binding1.etvechialNumberTitle.setVisibility(View.GONE);
                binding1.etvechialNumber.setVisibility(View.GONE);
                vechilenumber = "";
                subCateModelArrayListserivce.clear();
                thirdCateModelArrayList.clear();
                fourModelArrayList.clear();
                dialogEditProduct.dismiss();
                // getSpinnerCategory();

            }
        });

        binding1.tvYesPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String cat = binding1.tvcat.getText().toString().trim();
                String subcat = binding1.tvFiltersub.getText().toString().trim();
                String third = binding1.tvhird.getText().toString().trim();
                String four = binding1.tvservice.getText().toString().trim();
                String five = binding1.pDes.getText().toString().trim();
                vechilenumber = binding1.etvechialNumber.getText().toString().trim();


                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (cat.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.val_category), Toast.LENGTH_SHORT).show();
                    } else if (subcat.equalsIgnoreCase(getResources().getString(R.string.all_sub_categories))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.val_subcatogry), Toast.LENGTH_SHORT).show();
                    } else if (four.equalsIgnoreCase(getResources().getString(R.string.all_service_name))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.val_sname), Toast.LENGTH_SHORT).show();
                    } else if (five.equalsIgnoreCase(getResources().getString(R.string.select_description))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.validate_des), Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkarss(category_id)) {
                            if (third.equalsIgnoreCase(getResources().getString(R.string.all_sub__level_categories))) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.val_sublevelcatogry), Toast.LENGTH_SHORT).show();
                            } else if (vechilenumber.equalsIgnoreCase("")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.val_pnumber), Toast.LENGTH_SHORT).show();

                            } else {
                                addProduct();
                            }
                        } else {
                            addProduct();
                        }


                    }


                } else {
                    //   Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
*/

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


/*
    public void addProduct() {

        //  Toast.makeText(getActivity(), "description id :"+description_id, Toast.LENGTH_SHORT).show();
        // Log.e("description_id", "" + description_id);
        send_price_str = binding1.manualPriceEtx.getText().toString();
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.addProduct(category_id, sub_category_id, third_id, userDTO.getUser_id(), servicename, serviceid, vechilenumber, encodedBase64, send_price_str, description_id, "");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        // Log.e("ADD_PRODUCT_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            //.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


                            binding1.tvFiltersub.setVisibility(View.GONE);
                            binding1.tvhird.setVisibility(View.GONE);
                            binding1.tvservice.setVisibility(View.GONE);
                            binding1.etvechialNumber.setVisibility(View.GONE);
                            binding1.etvechialNumberTitle.setVisibility(View.GONE);

                            vechilenumber = "";
                            categoryDTOS.clear();
                            subCateModelArrayListserivce.clear();
                            thirdCateModelArrayList.clear();
                            fourModelArrayList.clear();
                            dialogEditProduct.dismiss();

                            baseActivity.loadHomeFragment(new MainserviceFragment(), "8");
                            //  getArtist();

                        } else {

                            // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        //   Toast.makeText(getActivity(), "Try Again Later ", Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                //  Toast.makeText(getActivity(), "Try again. Server is not responding", Toast.LENGTH_SHORT).show();


            }
        });


    }
*/

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


                                if (artistDetailsDTO.getIs_online().equalsIgnoreCase("0")) {

                                    Log.e("SHOW_DIALOG", "condtion ma gayu");

                                    binding.partnerTxt.setText(online_msg);

                                } else {
                                    Log.e("SHOW_DIALOG", "else ma gayu");

                                    binding.partnerTxt.setText(offline_msg);

                                }


                                if (value.equalsIgnoreCase("0")) {
                                    showDatfirst();

                                } else {
                                    showDataddd();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
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

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public void isOnline(String onoff) {

        Log.e("devicetoken", "cabbooking");

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
                    Log.e("BASE0", " response " + response.toString());

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        Log.e("BASE1", " message " + message);
                        Log.e("BASE2", " sstatus " + sstatus);


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

/*
    public void getGuide() {

        new HttpsRequest(Consts.GET_GUIDE_SEQUENCE, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {

                        Log.e("guideList", "" + response.toString());

                        guideList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<GuideModel>>() {
                        }.getType();
                        guideList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        switch_step_title = guideList.get(0).getState();
                        switch_step_txt = guideList.get(0).getAppnotificationtext();
                        category_step_title = guideList.get(1).getState();
                        category_step_txt = guideList.get(1).getAppnotificationtext();
                        service_step_title = guideList.get(2).getState();
                        service_step_txt = guideList.get(2).getAppnotificationtext();
                        promotion_step_title = guideList.get(3).getState();
                        promotion_step_txt = guideList.get(3).getAppnotificationtext();
                        refer_step_title = guideList.get(4).getState();
                        refer_step_txt = guideList.get(4).getAppnotificationtext();
                        tvideo_step_title = guideList.get(5).getState();
                        tvideo_step_txt = guideList.get(5).getAppnotificationtext();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
*/

    public void getBookings() {

        shouldStopLoop = true;
        // Toast.makeText(baseActivity, "getBookings ", LENGTH_SHORT).show();
        //   progressDialog.show();
        // Retrofit retrofit = apiClient.getClient2();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCabdhavalnew(userDTO.getUser_id(), version, "", "", "");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("CAB_RES_cab_first", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            int payment_status = object.getInt("payment_status");
                            String total_order = object.getString("total_order_count");

                            try {
                                String rider_amount1 = object.getString("rider_amount");

                                String rider_image1 = object.getString("rider_image");
                                String rider_name1 = object.getString("rider_name");
                                String rider_handover_txt1 = object.getString("partner_handover_text");
                                String request_id = object.getString("request_id");

                                artistBookingsList = new ArrayList<>();
                                //     orderItemList = new ArrayList<>();
                                artistTimeSlotList = new ArrayList<>();
                                productArrayList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                Type getpetDTO2 = new TypeToken<List<TimeSlotModel>>() {
                                }.getType();
                                Type getpetDTO3 = new TypeToken<List<ProductArrayModel>>() {
                                }.getType();
                                productArrayList = (ArrayList<ProductArrayModel>) new Gson().fromJson(object.getJSONArray("product_array").toString(), getpetDTO3);

                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                Log.e("BOOKING_TRACK", " 1 " + artistBookingsList.toString() + "ID:---- " + artistBookingsList.get(count_flag).getId());
                                artistTimeSlotList = (ArrayList<TimeSlotModel>) new Gson().fromJson(object.getJSONArray("slot_array").toString(), getpetDTO2);

                                Log.e("BOOKING_TRACK", " 2 " + artistTimeSlotList.toString());

                                binding.orderSummeryCount.setText(total_order);

                                binding.orderSummaryRelative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e("orderSummeryCount", " clicked ");
                                        getSlotDetails();
                                    }
                                });


                                if (artistBookingsList.size() == 0) {

                                    //   binding.trialbtnSubmit.setVisibility(View.VISIBLE);
                                } else {

                                    for (int i = 0; i < artistBookingsList.size(); i++) {
                                        artistBookingsListnew.add(artistBookingsList.get(i));
                                    }
                                    //   binding.trialbtnSubmit.setVisibility(View.GONE);

                                }

                                if (artistBookingsListnew.size() > 6) {

                                    binding.idPBLoading.setVisibility(View.VISIBLE);
                                }else {
                                    binding.idPBLoading.setVisibility(View.GONE);

                                }

                              /*  for (int i =0;i < artistBookingsList.size();i++){
                                    orderItemList = artistBookingsList.get(i).getProduct();
                                }*/
                                //Collections.reverse(artistBookingsList);

                                if (payment_status == 1) {

                                    baseActivity.rider_amount.setText(Html.fromHtml("&#8377;" + rider_amount1));
                                    baseActivity.rider_name.setText(rider_name1);
                                    baseActivity.rider_handover_txt.setText(rider_handover_txt1);
                                    Glide.with(getActivity()).load(rider_image1).placeholder(R.drawable.dafault_product).into(baseActivity.rider_image);
                                    baseActivity.cancel_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //       Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //      Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    baseActivity.cancel_btn_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            Log.e("collectParams", "" + collectParams.toString());


                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });


                                    baseActivity.collect_rider_amount.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "2");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //      Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    baseActivity.dialogRiderPayment.show();
                                }
                                setBookingAdapter();
                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            binding.idPBLoading.setVisibility(View.GONE);
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

    public void getBookingsLazy(int range) {

        shouldStopLoop = true;
        // Toast.makeText(baseActivity, "getBookings ", LENGTH_SHORT).show();
        //   progressDialog.show();
        // Retrofit retrofit = apiClient.getClient2();

        Log.e("LAZY_PARAMS", " params range :- " + range + " starttime:- " + artistBookingsList.get(0).getPslotstarttime() + " endtime:- " + artistBookingsList.get(0).getPslotendtime());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCabdhavalnew(userDTO.getUser_id(), version, String.valueOf(range), artistBookingsList.get(0).getPslotstarttime(), artistBookingsList.get(0).getPslotendtime());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("Lazy_CAB_RES_cab", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            int payment_status = object.getInt("payment_status");

                            Log.e("STATUS"," 1 :-- "+sstatus);
                            try {
                                String rider_amount1 = object.getString("rider_amount");

                                String rider_image1 = object.getString("rider_image");
                                String rider_name1 = object.getString("rider_name");
                                String rider_handover_txt1 = object.getString("partner_handover_text");
                                String request_id = object.getString("request_id");

                                artistBookingsList = new ArrayList<>();
                                //     orderItemList = new ArrayList<>();
                                artistTimeSlotList = new ArrayList<>();
                                productArrayList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                            /*    Type getpetDTO2 = new TypeToken<List<TimeSlotModel>>() {
                                }.getType();
                                Type getpetDTO3 = new TypeToken<List<ProductArrayModel>>() {
                                }.getType();
                                productArrayList = (ArrayList<ProductArrayModel>) new Gson().fromJson(object.getJSONArray("product_array").toString(), getpetDTO3);
                            */
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                for (int i = 0; i < artistBookingsList.size(); i++) {
                                    artistBookingsListnew.add(artistBookingsList.get(i));
                                }

                                Log.e("BOOKING_TRACK", " 1 " + artistBookingsList.toString() + "ID:---- " + artistBookingsList.get(count_flag).getId());
                              //  artistTimeSlotList = (ArrayList<TimeSlotModel>) new Gson().fromJson(object.getJSONArray("slot_array").toString(), getpetDTO2);

                                Log.e("BOOKING_TRACK", " 2 " + artistTimeSlotList.toString());


                               /* binding.orderSummaryRelative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e("orderSummeryCount", " clicked ");
                                        getSlotDetails();
                                    }
                                });

*/
                                if (artistBookingsList.size() == 0) {

                                    //   binding.trialbtnSubmit.setVisibility(View.VISIBLE);
                                } else {
                                    //   binding.trialbtnSubmit.setVisibility(View.GONE);

                                }

                              /*  for (int i =0;i < artistBookingsList.size();i++){
                                    orderItemList = artistBookingsList.get(i).getProduct();
                                }*/
                                //Collections.reverse(artistBookingsList);

/*
                                if (payment_status == 1) {

                                    baseActivity.rider_amount.setText(Html.fromHtml("&#8377;" + rider_amount1));
                                    baseActivity.rider_name.setText(rider_name1);
                                    baseActivity.rider_handover_txt.setText(rider_handover_txt1);
                                    Glide.with(getActivity()).load(rider_image1).placeholder(R.drawable.dafault_product).into(baseActivity.rider_image);
                                    baseActivity.cancel_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //       Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //      Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    baseActivity.cancel_btn_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            Log.e("collectParams", "" + collectParams.toString());


                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });


                                    baseActivity.collect_rider_amount.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "2");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //      Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    baseActivity.dialogRiderPayment.show();
                                }
*/
                                //setBookingAdapter();
                                //showData();

                                setAdapterAllBookings();



                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                            Log.e("STATUS"," 0 :-- "+sstatus);
                        //    Toast.makeText(getActivity(), "No more orders!!", Toast.LENGTH_SHORT).show();
                            binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        Log.e("STATUS"," unsuccessful :-- ");
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

    public void getTimeSlotWiseBookings(String date_time_time, String date_time_date, String date_time_date2) {

        progressDialog.show();


        Log.e("slot_click_current_page", " slot_click_current_page " + slot_click_current_page);
        shouldStopLoop = true;
        // Toast.makeText(baseActivity, "getBookings ", LENGTH_SHORT).show();
        // progressDialog.show();
        // Retrofit retrofit = apiClient.getClient2();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCabDhavalClick(userDTO.getUser_id(), date_time_time, date_time_date, date_time_date2, version, String.valueOf(0));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("getTimeSlotWiseBoo_RES", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            int payment_status = object.getInt("payment_status");
                            String total_order_count = object.getString("total_order_count");


                            try {
                                String rider_amount1 = object.getString("rider_amount");

                                String rider_image1 = object.getString("rider_image");
                                String rider_name1 = object.getString("rider_name");
                                String rider_handover_txt1 = object.getString("partner_handover_text");
                                String request_id = object.getString("request_id");

                                artistBookingsListnew = new ArrayList<>();
                                artistBookingsList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                             /*   Type getpetDTO2 = new TypeToken<List<TimeSlotModel>>() {
                                }.getType();*/
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);

                                /*for (int i = 0; i < artistBookingsList.size(); i++) {
                                    artistBookingsListnew.add(artistBookingsList.get(i));
                                }*/

                                if (artistBookingsList.size() == 0) {

                                    //   binding.trialbtnSubmit.setVisibility(View.VISIBLE);
                                } else {
                                    for (int i = 0; i < artistBookingsList.size(); i++) {
                                        artistBookingsListnew.add(artistBookingsList.get(i));
                                    }
                                    //   binding.trialbtnSubmit.setVisibility(View.GONE);

                                }
                                binding.orderSummeryCount.setText(total_order_count);
                                if (artistBookingsListnew.size() == 1) {

                                    binding.idPBLoading.setVisibility(View.GONE);
                                }
                                else {
                                    binding.idPBLoading.setVisibility(View.VISIBLE);

                                }
                                //Collections.reverse(artistBookingsList);
                                slot_click = true;

                                if (payment_status == 1) {

                                    baseActivity.rider_amount.setText(Html.fromHtml("&#8377;" + rider_amount1));
                                    baseActivity.rider_name.setText(rider_name1);
                                    baseActivity.rider_handover_txt.setText(rider_handover_txt1);
                                    Glide.with(getActivity()).load(rider_image1).placeholder(R.drawable.dafault_product).into(baseActivity.rider_image);
                                    baseActivity.cancel_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //       Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //      Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    baseActivity.cancel_btn_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            Log.e("collectParams", "" + collectParams.toString());


                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });


                                    baseActivity.collect_rider_amount.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "2");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //      Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    baseActivity.dialogRiderPayment.show();
                                }
                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
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

    public void getTimeSlotWiseBookingsLazy(String date_time_time, String date_time_date, String date_time_date2, int range) {

    //    progressDialog.show();


        Log.e("version", " version " + version + " date_time_date2 " + date_time_date2);
        shouldStopLoop = true;
        // Toast.makeText(baseActivity, "getBookings ", LENGTH_SHORT).show();
        // progressDialog.show();
        // Retrofit retrofit = apiClient.getClient2();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCabDhavalClick(userDTO.getUser_id(), date_time_time, date_time_date, date_time_date2, version, String.valueOf(range));
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("CAB_RES_cab", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            int payment_status = object.getInt("payment_status");

                            try {
                                String rider_amount1 = object.getString("rider_amount");

                                String rider_image1 = object.getString("rider_image");
                                String rider_name1 = object.getString("rider_name");
                                String rider_handover_txt1 = object.getString("partner_handover_text");
                                String request_id = object.getString("request_id");

                             //   artistBookingsListnew = new ArrayList<>();
                                artistBookingsList = new ArrayList<>();

                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                Type getpetDTO2 = new TypeToken<List<TimeSlotModel>>() {
                                }.getType();
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                                if (artistBookingsList.size() == 0) {

                                    //   binding.trialbtnSubmit.setVisibility(View.VISIBLE);
                                } else {

                                    for (int i = 0; i < artistBookingsList.size(); i++) {
                                        artistBookingsListnew.add(artistBookingsList.get(i));
                                    }
                                    //   binding.trialbtnSubmit.setVisibility(View.GONE);

                                }

                                if (artistBookingsListnew.size() > 3) {

                                    binding.idPBLoading.setVisibility(View.VISIBLE);
                                }else {
                                    binding.idPBLoading.setVisibility(View.GONE);

                                }
                                //Collections.reverse(artistBookingsList);
                                slot_click = true;

                               /* if (payment_status == 1) {

                                    baseActivity.rider_amount.setText(Html.fromHtml("&#8377;" + rider_amount1));
                                    baseActivity.rider_name.setText(rider_name1);
                                    baseActivity.rider_handover_txt.setText(rider_handover_txt1);
                                    Glide.with(getActivity()).load(rider_image1).placeholder(R.drawable.dafault_product).into(baseActivity.rider_image);
                                    baseActivity.cancel_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //       Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //      Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    baseActivity.cancel_btn_rider_payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "3");

                                            Log.e("collectParams", "" + collectParams.toString());


                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });


                                    baseActivity.collect_rider_amount.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            collectParams = new HashMap<>();
                                            collectParams.put(Consts.REQUEST_ID, request_id);
                                            collectParams.put(Consts.STATUS, "2");

                                            new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, getActivity()).stringPost(TAG, new Helper() {
                                                @Override
                                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                                    if (flag) {
                                                        baseActivity.dialogRiderPayment.dismiss();
                                                        baseActivity.getRiderPaymentDialog();
                                                        //  baseActivity.loadHomeFragment(new CabBookingsFragment(), CURRENT_TAG);
                                                        //      Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    baseActivity.dialogRiderPayment.show();
                                }
                               */// showData();
                                setAdapterAllBookings();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                          //  Toast.makeText(getActivity(), "No more orders!!", Toast.LENGTH_SHORT).show();
                            binding.idPBLoading.setVisibility(View.GONE);
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
    public void onPause() {
        super.onPause();
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
                        getSlider();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });
    }


    public void getDemoBooking() {

        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_DEMO_BOOKING, parmsdemobooking, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                if (flag) {
                    try {
                        demoDialog.dismiss();
                        Log.e("paramsdemobooking", "" + response.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });
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

    public void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(),
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
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

    @Override
    public void onLocationChanged(Location location) {
        if (!map_flag) {


            mylocation = location;
            // Toast.makeText(mContext, "1", Toast.LENGTH_LONG).show();

            if (mylocation != null) {
                //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

                live_latitude = mylocation.getLatitude();
                //  Log.e("lat", "" + latitude.toString());
                live_longitude = mylocation.getLongitude();
                //   Log.e("lang", "" + longitude.toString());

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

                        Log.e("Live_ADDRESS", "" + live_address_str);

                        live_address_str = obj.getAddressLine(0);
                        location_etx.setText(obj.getAddressLine(0));
                        addAddressHashmap.put("street_address", obj.getAddressLine(0));
                        addAddressHashmap.put(Consts.LATITUDE, String.valueOf(live_latitude));
                        addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(live_longitude));
                    }
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

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void getTimeSlotData(String date_time_time, String date_time_date, String date_time_date2) {

        this.date_time_time = date_time_time;
        this.date_time_date = date_time_date;
        this.date_time_date2 = date_time_date2;
        getTimeSlotWiseBookings(date_time_time, date_time_date, date_time_date2);

        Log.e("getTimeSlo_params", "" + userDTO.getUser_id() + " " + date_time_time + " " + date_time_date);

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

    public void getSlider() {

        Log.e("Vider", "0");


        gettvideo();
        Log.e("Vider", "5");
    }

    public void showData() {


        Log.e("confirmation_for_start", " show data called ");

        if (artistBookingsList.size() == 0) {

            baseActivity.getVersion();
            Log.e("artist", "121");
            binding.tvNo.setVisibility(View.GONE);
            binding.rvBookingNested.setVisibility(View.GONE);
            binding.rvBooking.setVisibility(View.GONE);
            binding.horizontalscrollBtn.setVisibility(View.GONE);

            binding.rlSearch.setVisibility(View.GONE);
            // binding.mapView.setVisibility(View.VISIBLE);
            //  binding.trialbtnSubmit.setVisibility(View.VISIBLE);
            binding.lay.setVisibility(View.VISIBLE);
            //   binding.pulsator.setVisibility(View.VISIBLE);
        //    binding.sliderLayout.setVisibility(View.VISIBLE);
            //        binding.forthRelative.setVisibility(View.VISIBLE);
            binding.relativeTwo.setVisibility(View.VISIBLE);
            //   binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
     //       binding.fifthLinear.setVisibility(View.VISIBLE);
     //       binding.sixthLinear.setVisibility(View.VISIBLE);
            binding.seventhLinear.setVisibility(View.VISIBLE);
            //    binding.eightLinear.setVisibility(View.VISIBLE);

            if (artistDetailsDTO != null) {
                if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                    //   binding.pulsator.setVisibility(View.VISIBLE);
                    //binding.trialbtnSubmit.setVisibility(View.GONE);

                } else {
                    binding.pulsator.setVisibility(View.GONE);
                    //  binding.trialbtnSubmit.setVisibility(View.GONE);

                }
            }
        } else {
            for (int i = 0; i < artistBookingsList.size(); i++) {

                Log.e("tracker", "1");

                if (artistBookingsList.get(i).getBooking_flag().equalsIgnoreCase("0")) {

                    count_flag = i;
                    Log.e("tracker", "2");

                    binding.layaccpet.setVisibility(View.VISIBLE);
                    //   binding.mapView.setVisibility(View.VISIBLE);
                    binding.sliderLayout.setVisibility(View.GONE);
                    binding.relativeTwo.setVisibility(View.GONE);
                    binding.thirdRelative.setVisibility(View.GONE);
                    //           binding.forthRelative.setVisibility(View.GONE);
                    binding.scrollviewHomescreen.setVisibility(View.GONE);
                    binding.fifthLinear.setVisibility(View.GONE);
                    binding.sixthLinear.setVisibility(View.GONE);
                    binding.seventhLinear.setVisibility(View.VISIBLE);
                    //              binding.eightLinear.setVisibility(View.VISIBLE);

                    //       binding.trialbtnSubmit.setVisibility(View.GONE);

                    binding.lay.setVisibility(View.GONE);
                    try {
                        Log.e("tracker", "3");

                        bookingid = artistBookingsList.get(count_flag).getId();
                        hold_order = artistBookingsList.get(count_flag).getHold_order();
                        Log.e("tracker", "4");

                        int seocndserver = Integer.parseInt(artistBookingsList.get(count_flag).getSecond());
                        Log.e("finalConvertedF", "abcd5" + " " + seocndserver);

                        int timer = Integer.parseInt(artistBookingsList.get(count_flag).getStarttimer());
                        //    if (seocndserver < timer) {

                        Log.e("tracker", "5");

                        // seocndserver = timer - seocndserver;
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
                                        Log.e("tracker", "onfinish if ma gayu" + " getHold_order " + artistBookingsList.get(count_flag).getHold_order());

                                        Intent intent = new Intent(getActivity(), MyService.class);
                                        getActivity().stopService(intent);
                                        decline(artistBookingsList.get(count_flag).getHold_order());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {

                                    Log.e("tracker", "onfinish else ma gayu");
                                }

                            }
                        };
                        cT.start();

                        Log.d("tracker", "101");

                        String locationstatus = artistBookingsList.get(count_flag).getLocation_status();
                        Log.e("locationstatus", " :  : " + locationstatus);

                        if (locationstatus.equalsIgnoreCase("1")) {

                            if (artistBookingsList.get(count_flag).getRider_order().equalsIgnoreCase("0")) {
                                binding.tvroundtrip.setVisibility(View.VISIBLE);
                                binding.tvroundtrip.setText("Shipping");
                            } else {
                                binding.dropLinear.setVisibility(View.GONE);
                                binding.llLocationDest.setVisibility(View.GONE);
                                binding.vone2.setVisibility(View.GONE);
                                binding.tvroundtrip.setVisibility(View.VISIBLE);
                                binding.tvroundtrip.setText("Rider");
                            }

                        } else if (locationstatus.equalsIgnoreCase("0")) {
                            binding.tvroundtrip.setVisibility(View.VISIBLE);
                            binding.tvroundtrip.setText("Pick Up");
                        } else {
                            binding.tvroundtrip.setVisibility(View.GONE);
                        }

                        if (artistBookingsList.get(count_flag).getPay_type().equalsIgnoreCase("0")) {
                            Log.e("PAYMENT_TRACKER", "1=>" + artistBookingsList.get(count_flag).getPay_type());
                            binding.txtptype.setText("Online Payment");

                        } else if (artistBookingsList.get(count_flag).getPay_type().equalsIgnoreCase("1")) {
                            Log.e("PAYMENT_TRACKER", "2=>" + artistBookingsList.get(count_flag).getPay_type());
                            binding.txtptype.setText("Cash");

                        } else if (artistBookingsList.get(count_flag).getPay_type().equalsIgnoreCase("2")) {
                            Log.e("PAYMENT_TRACKER", "3=>" + artistBookingsList.get(count_flag).getPay_type());
                            binding.txtptype.setText("Wallet Payment");
                        }
                        binding.priceDigit.setText(Html.fromHtml("&#8377;" + artistBookingsList.get(count_flag).getPartnertotal()));
                        binding.orderNo.setText(artistBookingsList.get(count_flag).getOrder_count());

                        dcatid = artistBookingsList.get(count_flag).getProduct().get(0).getCategory_id();
                        dsubcatid = artistBookingsList.get(count_flag).getProduct().get(0).getSub_category_id();
                        dthirdcatid = artistBookingsList.get(count_flag).getProduct().get(0).getSublevel_category();

                        binding.tvLocation.setText(artistBookingsList.get(count_flag).getAddress());
                        binding.txtcat.setText(artistBookingsList.get(count_flag).getCategory_name());


                    } catch (Exception e) {

                    }

                } else {

         //           binding.mapView.setVisibility(View.GONE);
                    binding.rvBookingNested.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.horizontalscrollBtn.setVisibility(View.VISIBLE);

                }
            }
            binding.pulsator.setVisibility(View.GONE);
            binding.sliderLayout.setVisibility(View.GONE);
            binding.relativeTwo.setVisibility(View.GONE);
            binding.thirdRelative.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setVisibility(View.GONE);
            binding.fifthLinear.setVisibility(View.GONE);
            binding.sixthLinear.setVisibility(View.GONE);
            binding.seventhLinear.setVisibility(View.VISIBLE);

            binding.lay.setVisibility(View.GONE);

//            orderItemsAdapter = new OrderItemsAdapter(getActivity(), productArrayList);
//            binding.orderItemRv.setLayoutManager(new LinearLayoutManager(getActivity()));
//            binding.orderItemRv.setAdapter(orderItemsAdapter);


            binding.bookingNestedscroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {

                        if (slot_click) {
                            slot_click_current_page++;
                            Log.e("slot_click_current_page", "" + slot_click_current_page);
                            // getBookings2(current_page);
                            binding.idPBLoading.setVisibility(View.VISIBLE);
                            getTimeSlotWiseBookingsLazy(date_time_time, date_time_date, date_time_date2, slot_click_current_page);
                        } else {
                            current_page++;
                            //   Log.e("CURRENT_PAGE", "" + current_page + " count "+ totalItemCount);
                            Log.e("CURRENT_PAGE", "" + current_page);
                            // getBookings2(current_page);
                            binding.idPBLoading.setVisibility(View.VISIBLE);
                            getBookingsLazy(current_page);
                        }
                    }
                }
            });
            setAdapterAllBookings();

        }


    }

    public void setAdapterAllBookings() {

        Log.e("confirmation_for_start", "adapter called");

        // adapterAllBookings = new AdapterCabBookings(CabBookingsFragment.this, artistBookingsList, userDTO, myInflater, baseActivity);
        Log.e("confirmation_for_start", "confirmation_for_start 300: 10 : "+artistBookingsListnew.get(0).getOrder_no2());

        adapterAllBookings = new AdapterCabBookings(CabBookingsFragment.this, artistBookingsListnew, userDTO, myInflater, baseActivity,notification_name);
        Log.e("confirmation_for_start", "adapter called 2");

        binding.rvBooking.setAdapter(adapterAllBookings);
        Log.e("confirmation_for_start", "adapter called 3" );

        binding.rvBooking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e("confirmation_for_start", "adapter called 4");

                progressDialog.dismiss();

                binding.rvBooking
                        .getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
            }
        });
        // progressDialog.dismiss();
    }

    public void setBookingAdapter() {
        timeSlotAdapter = new TimeSlotAdapter(getActivity(), artistTimeSlotList, this);
        binding.timeslotNewRv.setAdapter(timeSlotAdapter);
        // progressDialog.dismiss();
        Log.e("BOOKING_TRACK", " 3 ");

    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;

            } else {
                value = false;
            }

        }
        return value;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void autodecline(String bid) {

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, "");
        paramsDecline.put("cat_id", dcatid);
        paramsDecline.put("sub_id", dsubcatid);
        paramsDecline.put("third_id", dthirdcatid);
        paramsDecline.put("lat", dclatititute);
        paramsDecline.put("lang", dclongitiute);
        paramsDecline.put("passvalue", "0");

        //   progressDialog.show();
        Log.e("decline_RES", " params " + paramsDecline.toString());
        new HttpsRequest(Consts.DECLINE_BOOKING_API2, paramsDecline, getActivity()).stringPosttwo("decline_booking2", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  progressDialog.dismiss();
                if (flag) {

                    binding.rvBookingNested.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.horizontalscrollBtn.setVisibility(View.VISIBLE);

                    Log.e("decline_RES", " res " + response.toString());

                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();

                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.layaccpet.setVisibility(View.GONE);


                        getBookingscopy("3");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


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
        paramsDecline.put("cat_id", dcatid);
        paramsDecline.put("sub_id", dsubcatid);
        paramsDecline.put("third_id", dthirdcatid);
        paramsDecline.put("lat", dclatititute);
        paramsDecline.put("lang", dclongitiute);
        paramsDecline.put("passvalue", "0");

        //   progressDialog.show();
        Log.e("decline_RES", " params " + paramsDecline.toString());
        new HttpsRequest(Consts.DECLINE_BOOKING_ON_BTN_CLICK, paramsDecline, getActivity()).stringPosttwo("decline_booking2", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  progressDialog.dismiss();
                if (flag) {

                    binding.rvBookingNested.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.horizontalscrollBtn.setVisibility(View.VISIBLE);

                    Log.e("decline_RES", " res " + response.toString());

                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();

                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.layaccpet.setVisibility(View.GONE);


                        getBookingscopy("3");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
    }

    public void decline(String bid) {

        Log.e("auto_decline", " decline called ");

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, "");
        paramsDecline.put("cat_id", dcatid);
        paramsDecline.put("sub_id", dsubcatid);
        paramsDecline.put("third_id", dthirdcatid);
        paramsDecline.put("lat", dclatititute);
        paramsDecline.put("lang", dclongitiute);
        paramsDecline.put("passvalue", "0");

        progressDialog.show();

        new HttpsRequest(Consts.AUTO_DECLINE_BOOKING_API, paramsDecline, getActivity()).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    binding.rvBookingNested.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.GONE);
                    binding.horizontalscrollBtn.setVisibility(View.GONE);

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


                        binding.layaccpet.setVisibility(View.GONE);


                        getBookingscopy("3");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
    }


    public void showDatfirst() {

        if (artistDetailsDTO != null) {
            //   baseActivity.swOnOff.setVisibility(View.VISIBLE);
            //  baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
      //      binding.sliderLayout.setVisibility(View.VISIBLE);
            binding.relativeTwo.setVisibility(View.VISIBLE);
            //    binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
       //     binding.fifthLinear.setVisibility(View.VISIBLE);
       //     binding.sixthLinear.setVisibility(View.VISIBLE);
            binding.seventhLinear.setVisibility(View.VISIBLE);
            //        binding.eightLinear.setVisibility(View.VISIBLE);
            //   binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);

          /*  if (!isMyServiceRunning(mYourService.getClass())) {
                baseActivity.startService(mServiceIntent);
            }*/
            //  binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //      binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));
            // binding.pulsator.start();
            //    binding.pulsator.setVisibility(View.VISIBLE);

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
         //   binding.sliderLayout.setVisibility(View.VISIBLE);
            binding.relativeTwo.setVisibility(View.VISIBLE);
            //     binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
    //        binding.fifthLinear.setVisibility(View.VISIBLE);
    //        binding.sixthLinear.setVisibility(View.VISIBLE);
            binding.seventhLinear.setVisibility(View.VISIBLE);
            //       binding.eightLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            //       baseActivity.stopService(mServiceIntent);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.todaysStatusLinear.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            //        binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            binding.pulsator.setVisibility(View.GONE);

            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }

    public void showDataddd() {

        if (artistDetailsDTO != null) {
            //  baseActivity.swOnOff.setVisibility(View.VISIBLE);
            //  baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
       //     binding.sliderLayout.setVisibility(View.VISIBLE);
            binding.relativeTwo.setVisibility(View.VISIBLE);
            //      binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
  //          binding.fifthLinear.setVisibility(View.VISIBLE);
   //         binding.sixthLinear.setVisibility(View.VISIBLE);
            binding.seventhLinear.setVisibility(View.VISIBLE);
            //        binding.eightLinear.setVisibility(View.VISIBLE);

            //      binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            //   binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.todaysStatusLinear.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //   binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));

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
            binding.lay.setVisibility(View.VISIBLE);
            //     binding.trialbtnSubmit.setVisibility(View.GONE);

   //         binding.sliderLayout.setVisibility(View.VISIBLE);
            binding.relativeTwo.setVisibility(View.VISIBLE);
            //    binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //    binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
    //        binding.fifthLinear.setVisibility(View.VISIBLE);
     //       binding.sixthLinear.setVisibility(View.VISIBLE);
            binding.seventhLinear.setVisibility(View.VISIBLE);
            //      binding.eightLinear.setVisibility(View.VISIBLE);


            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.todaysStatusLinear.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            binding.pulsator.setVisibility(View.GONE);
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter actionReceiver = new IntentFilter();
        actionReceiver.addAction("nameOfTheActionShouldBeUniqueName");
        bm.registerReceiver(onJsonReceived, actionReceiver);
    }


    private BroadcastReceiver onJsonReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                getBookings();


            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        bm.unregisterReceiver(onJsonReceived);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void opencamrea() {


        ImagePicker.Companion.with(getActivity())
                .crop(3, 2)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(23);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

            } else if (requestCode == 3) {
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");

                uri = getImageUri(getActivity(), profilePicture);
                String filepath = FileUtility.getPath(getActivity(), uri);
                FileUtility fileUtility = new FileUtility();
                filepath = fileUtility.compressImage(getActivity(), filepath);
                File file = new File(filepath);
                paramsFile.put(Consts.IMAGE, file);
                AdapterCabBookings.img_upload.setVisibility(View.VISIBLE);
                AdapterCabBookings.img_upload.setImageURI(uri);


            } else if (requestCode == 23) {


                Uri imageUri = data.getData();

                String path = FileUtility.getPath(getActivity(), imageUri);
                File file = new File(path);
                paramsFile.put(Consts.IMAGE, file);
                AdapterCabBookings.lldauploadImageLayout.setVisibility(View.GONE);
                AdapterCabBookings.img_upload.setVisibility(View.VISIBLE);
                AdapterCabBookings.img_upload.setImageURI(imageUri);

            } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

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

        } else {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void getBookingscopy(final String position) {

        progressDialog.show();
        Log.e("confirmation_for_start", "multiple 5");

        Log.e("getBookingscopy_called", "getBookingscopy called");
        //Toast.makeText(baseActivity, "getBookingscopy called", LENGTH_SHORT).show();
        //   progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCabdhavalnew(userDTO.getUser_id(), version, "", "", "");
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                progressDialog.dismiss();
                binding.tvNo.setVisibility(View.GONE);
//                binding.trialbtnSubmit.setVisibility(View.GONE);
                binding.rvBookingNested.setVisibility(View.VISIBLE);
                binding.rvBooking.setVisibility(View.VISIBLE);
                binding.horizontalscrollBtn.setVisibility(View.VISIBLE);

                try {
                    if (response.isSuccessful()) {
                        Log.e("confirmation_for_start", "response successful");

                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        Log.e("booking_copy", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        String total_order_count = object.getString("total_order_count");

                        if (sstatus == 1) {

                            try {
                                artistBookingsList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                                for (int i = 0; i < artistBookingsList.size(); i++) {
                                    artistBookingsListnew.add(artistBookingsList.get(i));
                                }
                                //     Collections.reverse(artistBookingsList);
                                if (artistBookingsListnew.size() > 6) {

                                    binding.idPBLoading.setVisibility(View.VISIBLE);
                                }else {
                                    binding.idPBLoading.setVisibility(View.GONE);

                                }

                                binding.orderSummeryCount.setText(total_order_count);
                                getArtistcpoy();

                                showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            artistBookingsList = new ArrayList<>();

                            getArtistcpoy();


                            showData();

                        }


                    } else {

                        binding.rvBookingNested.setVisibility(View.GONE);
                        binding.rvBooking.setVisibility(View.GONE);
                        binding.horizontalscrollBtn.setVisibility(View.GONE);

                        binding.rlSearch.setVisibility(View.GONE);
                        binding.lay.setVisibility(View.VISIBLE);
                        //      binding.trialbtnSubmit.setVisibility(View.VISIBLE);
       //                 binding.sliderLayout.setVisibility(View.VISIBLE);
                        binding.relativeTwo.setVisibility(View.VISIBLE);
                        //     binding.thirdRelative.setVisibility(View.VISIBLE);
                        binding.thirdRelative.setVisibility(View.GONE);
                        //          binding.forthRelative.setVisibility(View.VISIBLE);
                        binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //            binding.fifthLinear.setVisibility(View.VISIBLE);
            //            binding.sixthLinear.setVisibility(View.VISIBLE);
                        binding.seventhLinear.setVisibility(View.VISIBLE);
                        //            binding.eightLinear.setVisibility(View.VISIBLE);
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
                        LENGTH_LONG).show();
*/

            }
        });


    }

/*
    public void getCategory() {


        new HttpsRequest(Consts.GET_DEMO_BOOKING_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
//                progressDialog.dismiss();

                if (flag) {
                    try {
                        //  Log.e("category_response", "" + response.toString());
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryDTOS);
                        demodialogListView.setAdapter(categoryListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //  Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
*/

    public void checkaddress() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMapCategory(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                // Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        String status = object.getString("status");
                        BaseActivity.addressModelArrayList = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonElements = object.getJSONArray("data");
                            for (int i = 0; i < jsonElements.length(); i++) {
                                JSONObject obj = jsonElements.getJSONObject(i);


                                String id = obj.getString("id");
                                String cat_id = obj.getString("cat_id");
                                String statuss = obj.getString("status");


                                BaseActivity.addressModelArrayList.add(new AddressModel(id, cat_id, statuss));

                            }
                        } else {

                        }
                    } else {
                     /*   Toast.makeText(getActivity(), "Try again. Server is not responding",
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


            }
        });
    }

/*
    class CategoryListAdapter extends BaseAdapter {

        ArrayList<CategoryDTO> arraylist;

        public CategoryListAdapter(ArrayList<CategoryDTO> arraylist) {
            this.arraylist = arraylist;
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.demo_booking_layout, null);

            CustomTextView customTextView;
            customTextView = view.findViewById(R.id.demoitem);
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Lato-Semibold.ttf");
            customTextView.setTypeface(font);
            customTextView.setText(arraylist.get(position).getCat_name());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parmsdemobooking.put("cat_id", arraylist.get(position).getId());

                    if (parmsdemobooking.size() != 0) {
                        Log.e("paramsdemobooking", "" + parmsdemobooking.toString());
                        getDemoBooking();

                    }
                }
            });
            return view;
        }
    }
*/
}
