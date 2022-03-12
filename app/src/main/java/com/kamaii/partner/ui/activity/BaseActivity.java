package com.kamaii.partner.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.FloatingWidgetService;
import com.kamaii.partner.Glob;
import com.kamaii.partner.HomeScreenFragment;
import com.kamaii.partner.LocationService;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.preferences.TextSizeFix;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.fragment.AddReferFragment;
import com.kamaii.partner.ui.fragment.AppUpdateFragment;
import com.kamaii.partner.ui.fragment.AppointmentFrag;
import com.kamaii.partner.ui.fragment.ArtistProfile;
import com.kamaii.partner.ui.fragment.ArtistProfileNewFragment;
import com.kamaii.partner.ui.fragment.MyOrders;
import com.kamaii.partner.ui.fragment.ChatList;
import com.kamaii.partner.ui.fragment.ContactUs;
import com.kamaii.partner.ui.fragment.CustomerBooking;
import com.kamaii.partner.ui.fragment.JobsFrag;
import com.kamaii.partner.ui.fragment.MainserviceFragment;
import com.kamaii.partner.ui.fragment.MyEarning;
import com.kamaii.partner.ui.fragment.MyOrders;
import com.kamaii.partner.ui.fragment.NewBookings;
import com.kamaii.partner.ui.fragment.Notification;
import com.kamaii.partner.ui.fragment.OrderDetailsFragment;
import com.kamaii.partner.ui.fragment.PackagingTrayFragment;
import com.kamaii.partner.ui.fragment.PaidFrag;
import com.kamaii.partner.ui.fragment.ProfileSetting;
import com.kamaii.partner.ui.fragment.PromotionFragment;
import com.kamaii.partner.ui.fragment.ReferCustomerFragment;
import com.kamaii.partner.ui.fragment.TVideoFragment;
import com.kamaii.partner.ui.fragment.TermsAndConditionFragment;
import com.kamaii.partner.ui.fragment.Tickets;
import com.kamaii.partner.ui.fragment.Wallet;
import com.kamaii.partner.ui.models.AddressModel;
import com.kamaii.partner.ui.models.GuideModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.CustomTypeFaceSpan;
import com.kamaii.partner.utils.FontCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static freemarker.template.utility.StringUtil.capitalize;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private String profile_step_title = "";
    private String profile_step_txt = "";
    public String getPlace_result = "";
    private String request_id = "";
    HashMap<String, String> responseHashMap;

    public void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(BaseActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 || requestCode == 1011) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

            getMyLocation();
        }

    }


    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 123;
    private String TAG = BaseActivity.class.getSimpleName();
    HashMap<String, String> parms = new HashMap<>();
    private FrameLayout frame;
    private View contentView;
    public NavigationView navigationView;
    public RelativeLayout header;
    public DrawerLayout drawer;
    public View navHeader;
    public ImageView menuLeftIV, ivSearch;
    Context mContext;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    public static final String TAG_MAIN = "main";
    public static final String TAG_BOOKING = "booking";
    public static final String TAG_CHAT = "chat";
    public static final String TAG_PROFILE = "profile";
    public static final String TAG_PROMOTION = "promotion";
    public static final String TAG_NOTIFICATION = "notification";
    public static final String TAG_DISCOUNT = "discount";
    public static final String TAG_HISTORY = "history";
    public static final String TAG_PROFILE_SETINGS = "profile_settings";
    public static final String TAG_TICKETS = "tickets";
    public static final String TAG_UPDATE = "update";
    public static final String TAG_TRIP = "trip";
    public static final String TAG_EARN = "earn";
    public static final String TAG_APPOINTMENT = "appointment";
    public static final String TAG_BOOKINGS_ALL = "jobs";
    public static final String TAG_SERVER_RESPONSE = "server response";
    public static final String TAG_BOOKINGS_cab = "cab";
    public static final String TAG_WALLET = "wallet";
    public static final String TAG_DOCUMENT = "document";
    public static final String TAG_TVIDEO = "tvideo";
    public static String CURRENT_TAG = TAG_MAIN;
    public static int navItemIndex = 0;
    private ArrayList<GuideModel> guideList = new ArrayList<>();

    private Handler mHandler;
    private static final float END_SCALE = 0.8f;
    InputMethodManager inputManager;
    CustomerBooking customerBooking = new CustomerBooking();
    private boolean shouldLoadHomeFragOnBackPress = true;
    public CustomTextViewBold headerNameTV, tvOnOff;
    public ImageView ivLogo, ivEditPersonal;
    private Location mylocation;
    public static GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private ImageView img_profile;
    private CustomTextViewBold tvName, dno, dyes;
    private CustomTextView tvEmail, tvOther, tvEnglish;
    public int location_check = 0;
    private LinearLayout llProfileClick;
    String type = "";
    String messagebody = "";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> parmsApprove = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    public Switch swOnOff;
    public ImageView editImage;
    private HashMap<String, String> paramsUpdate = new HashMap<>();
    private HashMap<String, String> collectParams;
    private HashMap<String, String> riderPaymentparams = new HashMap<>();
    LinearLayout laychat, layudoc,gujaratibtn,englishbtn;
    public Dialog dialogfinish, dialogRiderPayment;
    private SharedPreferences firebase;
    protected PowerManager.WakeLock mWakeLock;
    private ArtistDetailsDTO artistDetailsDTO;
    String devicetoken = "";
    public static ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    private HashMap<String, String> parmsversion = new HashMap<>();
    private Dialog dailograting, dialogTearmsAndCondition;
    CustomTextViewBold tvupdateapp;
    public ImageView img_sub_cat, update_dialog_close_img, rider_image, cancel_rider_payment;
    public CustomTextViewBold rider_name, rider_amount, collect_rider_amount, cancel_btn_rider_payment;
    public CustomTextView rider_handover_txt;
    int check_flag = 0;
    LocationService locationService;
    boolean logoutflag = false;
    public String offline_msg = "";
    public Intent mServiceIntent;
    Context ctx;
    String bg_locationFlag = "";

    public Context getCtx() {
        return ctx;
    }

    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextSizeFix.adjustFontScale(getResources().getConfiguration(), 1.0f, BaseActivity.this);
        setContentView(R.layout.activity_base);
        ctx = this;
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        header = findViewById(R.id.header);
        menuLeftIV = findViewById(R.id.menuLeftIV);
        ivLogo = findViewById(R.id.ivLogo);
        ivEditPersonal = findViewById(R.id.ivEditPersonal);

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            } else {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
        this.mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();


        /*final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "My Tag");
        this.mWakeLock.acquire();
        */
        mContext = BaseActivity.this;
        mHandler = new Handler();
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        prefrence = SharedPrefrence.getInstance(mContext);
        firebase = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");

        Log.e("BASE_DEVICETOKEN", "" + devicetoken);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsApprove.put(Consts.USER_ID, userDTO.getUser_id());


       /* locationService = new LocationService(getCtx());
        mServiceIntent = new Intent(getCtx(), locationService.getClass());
        if (!isMyServiceRunning(locationService.getClass())) {
            //startService(mServiceIntent);
        }*/


        if (getIntent().hasExtra(Consts.SCREEN_TAG)) {
            type = getIntent().getStringExtra(Consts.SCREEN_TAG);
            messagebody = getIntent().getStringExtra("messagebody");
            Log.e("rider1234567890", " SCREEN_TAG " + type);
        }
        Glob.BUBBLE_VALUE = "0";

        String str = android.os.Build.MODEL;
        Log.e("CHECK_CALLED", str + " brand " + Build.BRAND);

        askForSystemOverlayPermission();
        dialogfinish = new Dialog(BaseActivity.this, R.style.Theme_Dialog);
        dialogfinish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogfinish.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogfinish.setContentView(R.layout.dailog_approve);
        dialogfinish.setCancelable(false);

        dialogRiderPayment = new Dialog(BaseActivity.this, R.style.Theme_Dialog);
        dialogRiderPayment.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRiderPayment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogRiderPayment.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogRiderPayment.setContentView(R.layout.dialog_rider_payment);
        dialogRiderPayment.setCancelable(false);

        rider_image = dialogRiderPayment.findViewById(R.id.rider_image);
        cancel_rider_payment = dialogRiderPayment.findViewById(R.id.cancel_rider_payment);
        rider_name = dialogRiderPayment.findViewById(R.id.rider_name);
        rider_amount = dialogRiderPayment.findViewById(R.id.rider_amount);
        collect_rider_amount = dialogRiderPayment.findViewById(R.id.collect_rider_amount);
        cancel_btn_rider_payment = dialogRiderPayment.findViewById(R.id.cancel_btn_rider_payment);
        rider_handover_txt = dialogRiderPayment.findViewById(R.id.rider_handover_txt);

        laychat = dialogfinish.findViewById(R.id.laychat);
        layudoc = dialogfinish.findViewById(R.id.layudoc);
        dno = dialogfinish.findViewById(R.id.dno);
        dyes = dialogfinish.findViewById(R.id.dyes);
        dailograting = new Dialog(BaseActivity.this, R.style.Theme_Dialog);
        dailograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailograting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailograting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dailograting.setContentView(R.layout.dailog_update);
        dailograting.setCancelable(false);
        tvupdateapp = dailograting.findViewById(R.id.tvupdateapp);
        img_sub_cat = dailograting.findViewById(R.id.img_sub_cat);


        dialogTearmsAndCondition = new Dialog(BaseActivity.this, R.style.Theme_Dialog);
        dialogTearmsAndCondition.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTearmsAndCondition.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTearmsAndCondition.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogTearmsAndCondition.setContentView(R.layout.tearms_condition_dialog_layout);
        dialogTearmsAndCondition.setCancelable(false);
        gujaratibtn = dialogTearmsAndCondition.findViewById(R.id.gujaratibtn);
        englishbtn = dialogTearmsAndCondition.findViewById(R.id.englishbtn);


        getTermsAndConditionDialog();

        getVersion();
        setUpGClient();

        getRiderPaymentDialog();
        frame = (FrameLayout) findViewById(R.id.frame);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        contentView = findViewById(R.id.content);
        headerNameTV = findViewById(R.id.headerNameTV);
        tvOnOff = findViewById(R.id.tvOnOff);
        swOnOff = findViewById(R.id.swOnOff);
        editImage = findViewById(R.id.ivEditPersonal);
        menuLeftIV = (ImageView) findViewById(R.id.menuLeftIV);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);

        navHeader = navigationView.getHeaderView(0);
        img_profile = navHeader.findViewById(R.id.img_profile);
        tvName = navHeader.findViewById(R.id.tvName);
        tvEmail = navHeader.findViewById(R.id.tvEmail);
        tvEnglish = navHeader.findViewById(R.id.tvEnglish);
        tvOther = navHeader.findViewById(R.id.tvOther);
        llProfileClick = navHeader.findViewById(R.id.llProfileClick);

        getGuide();

        tvEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language("en");

            }
        });
        llProfileClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swOnOff.setVisibility(View.GONE);
                tvOnOff.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                navItemIndex = 5;
                CURRENT_TAG = TAG_PROFILE;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, new ArtistProfileNewFragment());
                fragmentTransaction.commitAllowingStateLoss();
                drawer.closeDrawers();
            }
        });
        tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                language("ar");

            }
        });
        Glide.with(mContext).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_profile);
        tvEmail.setText(userDTO.getEmail_id());
        tvName.setText(userDTO.getName());


        if (savedInstanceState == null) {
            Log.e("rider1234567890", "1");
            if (type != null) {

                Log.e("rider1234567890", " 2 " + type);

                if (type.equalsIgnoreCase(Consts.CHAT_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_CHAT;
                    loadHomeFragment(new ChatList(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.TICKET_COMMENT_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 10;
                    CURRENT_TAG = TAG_TICKETS;
                    loadHomeFragment(new Tickets(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.TICKET_STATUS_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 10;
                    CURRENT_TAG = TAG_TICKETS;
                    loadHomeFragment(new Tickets(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.WALLET_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 8;
                    CURRENT_TAG = TAG_WALLET;
                    loadHomeFragment(new Wallet(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.DECLINE_BOOKING_ARTIST_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    loadHomeFragment(new MyOrders(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.START_BOOKING_ARTIST_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_BOOKING;
                    loadHomeFragment(new CustomerBooking(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.BRODCAST_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 5;
                    CURRENT_TAG = TAG_NOTIFICATION;
                    loadHomeFragment(new Notification(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.ADMIN_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 5;
                    CURRENT_TAG = TAG_NOTIFICATION;
                    loadHomeFragment(new Notification(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.BOOK_ARTIST_NOTIFICATION)) {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    // loadHomeFragment(new MyOrders(), CURRENT_TAG);
                    loadHomeFragment(new MyOrders(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.SEND_RIDER_NOTIFICATION)) {
                    //   Toast.makeText(mContext, "jgj hkjkh kjgjgk", Toast.LENGTH_SHORT).show();
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 33;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    loadHomeFragment(new MyOrders(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.JOB_NOTIFICATION)) {

                    navItemIndex = 0;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    loadHomeFragment(new MyOrders(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.KRUPYA_KARKE_START_PREPARATION_KARE)) {
                    Log.e("notification_name", " base 1:-- start");

                    navItemIndex = 30;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    loadHomeFragmentRing(new MyOrders(), CURRENT_TAG, Consts.KRUPYA_KARKE_START_PREPARATION_KARE, messagebody);
                } else if (type.equalsIgnoreCase(Consts.KRUPYA_KARKE_REQUEST_FOR_RIDER_KARE)) {

                    navItemIndex = 300;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    loadHomeFragmentRing(new MyOrders(), CURRENT_TAG, Consts.KRUPYA_KARKE_REQUEST_FOR_RIDER_KARE, messagebody);
                } else if (type.equalsIgnoreCase(Consts.DELETE_JOB_NOTIFICATION)) {

                    navItemIndex = 0;
                    CURRENT_TAG = TAG_BOOKINGS_ALL;
                    loadHomeFragment(new JobsFrag(), CURRENT_TAG);
                } else if (type.equalsIgnoreCase(Consts.SEND_RESPONSE_NOTIFICATION)) {

                    Log.e("rider1234567890", " 222 " + "SEND_RESPONSE_NOTIFICATION");

                    //    navItemIndex = 10;
                    //   CURRENT_TAG = TAG_SERVER_RESPONSE;
                    // loadHomeFragment(new MyOrders(), CURRENT_TAG);
                    // sendServerResponse();

                } else if (type.equalsIgnoreCase(Consts.RIDER_PAYMENT_REQUEST_NOTIFICATION)) {

                    //    Log.e("rider1234567890"," 22 "+"show dialog");
                    loadHomeFragment(new MyOrders(), CURRENT_TAG);

                    showRiderPaymentDialog();
                } else {
                    ivSearch.setVisibility(View.GONE);
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_BOOKINGS_cab;
                    loadHomeFragment(new MyOrders(), CURRENT_TAG);
                }
            } else {

                Log.e("rider1234567890", " 220 " + "default called");

                ivSearch.setVisibility(View.GONE);
                navItemIndex = 0;
                CURRENT_TAG = TAG_BOOKINGS_cab;
                loadHomeFragment(new MyOrders(), CURRENT_TAG);
            }


        }

        menuLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerOpen();
            }
        });

        setUpNavigationView();
        Menu menu = navigationView.getMenu();

        changeColorItem(menu, R.id.nav_bookings_and_job);
        changeColorItem(menu, R.id.nav_personal);
        changeColorItem(menu, R.id.nav_other);
        changeColorItem(menu, R.id.nav_refer);
        changeColorItem(menu, R.id.nav_madeindia);
        changeColorItem(menu, R.id.nav_features);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyCustomFont(subMenuItem);
                }
            }
            applyCustomFont(mi);
        }

        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawerView, float slideOffset) {


                                         // Scale the View based on current slide offset
                                         final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                                         final float offsetScale = 1 - diffScaledOffset;
                                         contentView.setScaleX(offsetScale);
                                         contentView.setScaleY(offsetScale);

                                         // Translate the View, accounting for the scaled width
                                         final float xOffset = drawerView.getWidth() * slideOffset;
                                         final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                                         final float xTranslation = xOffset - xOffsetDiff;
                                         contentView.setTranslationX(xTranslation);

                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {

                                     }

                                     @Override
                                     public void onDrawerOpened(View drawerView) {

                                     }
                                 }
        );
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        if (userDTO.getIs_profile() == 1) {
            if (userDTO.getApproval_status() == 0) {
                getApproveStatus();
            }
        }

        if (userDTO.getIs_profile() == 0) {

            if (NetworkManager.isConnectToInternet(mContext)) {
                getCategory();
            } else {
                //    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }

        }


        getArtist();
    }

    private void getTermsAndConditionDialog() {

        gujaratibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTearmsAndCondition.dismiss();
                loadHomeFragmentWithData(new TermsAndConditionFragment(),"1234","gujarati");
            }
        });
        englishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTearmsAndCondition.dismiss();
                loadHomeFragmentWithData(new TermsAndConditionFragment(),"1234","english");
            }
        });
        dialogTearmsAndCondition.show();
    }

    public void sendServerResponse() {


        responseHashMap = new HashMap<>();
        responseHashMap.put(Consts.ARTIST_ID, userDTO.getUser_id());

        new HttpsRequest(Consts.SERVER_RESPONSE_API, responseHashMap, BaseActivity.this).stringPosttwo(TAG_SERVER_RESPONSE, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("SERVER_RESPONSE", " response " + response.toString());

            }
        });
    }

    public void showRiderPaymentDialog() {

        Log.e("rider1234567890", " 222 " + " dialog showed ");

        dialogRiderPayment.show();

        cancel_rider_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collectParams = new HashMap<>();
                collectParams.put(Consts.REQUEST_ID, request_id);
                collectParams.put(Consts.STATUS, "3");

                new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, BaseActivity.this).stringPost(TAG, new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        if (flag) {
                            dialogRiderPayment.dismiss();
                            getRiderPaymentDialog();
                            loadHomeFragment(new MyOrders(), CURRENT_TAG);
                            //    Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            //   Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        cancel_btn_rider_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancel_btn_rider_payment.setEnabled(false);
                cancel_btn_rider_payment.setClickable(false);
                collectParams = new HashMap<>();
                collectParams.put(Consts.REQUEST_ID, request_id);
                collectParams.put(Consts.STATUS, "3");

                new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, BaseActivity.this).stringPost(TAG, new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        if (flag) {
                            dialogRiderPayment.dismiss();
                            getRiderPaymentDialog();
                            loadHomeFragment(new MyOrders(), CURRENT_TAG);
                            // Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        collect_rider_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                collect_rider_amount.setEnabled(false);
                collect_rider_amount.setClickable(false);
                collectParams = new HashMap<>();
                collectParams.put(Consts.REQUEST_ID, request_id);
                collectParams.put(Consts.STATUS, "2");

                new HttpsRequest(Consts.ACCEPT_RIDER_PAYMENT_API, collectParams, BaseActivity.this).stringPost(TAG, new Helper() {
                    @Override
                    public void backResponse(boolean flag, String msg, JSONObject response) {
                        if (flag) {
                            dialogRiderPayment.dismiss();
                            getRiderPaymentDialog();
                            loadHomeFragment(new MyOrders(), CURRENT_TAG);
                            //      Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void getRiderPaymentDialog() {

        riderPaymentparams.put(Consts.ARTIST_ID, userDTO.getUser_id());
        Log.e("GET_RIDER_DATA_API", "" + riderPaymentparams.toString());

        new HttpsRequest(Consts.GET_RIDER_DATA_API, riderPaymentparams, BaseActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    try {

                        Log.e("GET_RIDER_DATA_API", "" + response.toString());

                        String rider_amount1 = response.getString("rider_amount");
                        Log.e("GET_RIDER_DATA_API", " rider_amount1 " + rider_amount1);

                        String rider_image1 = response.getString("rider_image");
                        String rider_name1 = response.getString("rider_name");
                        String rider_handover_txt1 = response.getString("partner_handover_text");
                        request_id = response.getString("request_id");

                        rider_amount.setText(Html.fromHtml("&#8377;" + rider_amount1));
                        rider_name.setText(rider_name1);
                        rider_handover_txt.setText(rider_handover_txt1);
                        Glide.with(BaseActivity.this).load(rider_image1).placeholder(R.drawable.dafault_product).into(rider_image);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getGuide() {

        new HttpsRequest(Consts.GET_GUIDE_SEQUENCE, this).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    try {

                        Log.e("guideList", "" + response.toString());

                        guideList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<GuideModel>>() {
                        }.getType();
                        guideList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        profile_step_title = guideList.get(7).getState();
                        profile_step_txt = guideList.get(7).getAppnotificationtext();


                        profile_step_title = guideList.get(11).getState();
                        profile_step_txt = guideList.get(11).getAppnotificationtext();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getSpotLight(View view, String title, String txt, String id) {


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public void getVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int version = pInfo.versionCode;
        parmsversion.put(Consts.USER_ID, userDTO.getUser_id());
        parmsversion.put("version", String.valueOf(version));

        Log.e("VERSION_PARAM", "" + parmsversion.toString());
        new HttpsRequest(Consts.GET_VERSION, parmsversion, BaseActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("version_res", "" + response.toString());
                    try {

                        Log.e("version_res", "" + response.toString());
                        int version = -1;

                        String update_image = "";
                        JSONObject jsonObjectmain = new JSONObject(String.valueOf(response));
                        JSONArray jsonArray = jsonObjectmain.getJSONArray("data");

                        String pname = getPackageName();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            String sversion = obj.getString("version_code");
                            String serverpname = obj.getString("package_name");
                            if (serverpname.equalsIgnoreCase(pname)) {
                                try {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    version = pInfo.versionCode;
                                    update_image = obj.getString("update_image");
                                } catch (PackageManager.NameNotFoundException e) {
                                    e.printStackTrace();
                                }

                                if (sversion.equalsIgnoreCase(String.valueOf(version))) {

                                } else {

                                    dailograting.show();
                                    String thumbnailMq = "http://img.youtube.com/vi/" + "iUJ4Omb8lro" + "/mqdefault.jpg";

                                    Glide.with(BaseActivity.this).load(update_image).into(img_sub_cat);

                                    img_sub_cat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/iUJ4Omb8lro"));
                                            try {
                                                startActivity(webIntent);
                                            } catch (ActivityNotFoundException ex) {
                                            }


                                        }
                                    });

                                    tvupdateapp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            final String appPackageName = getApplication().getPackageName();
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }
                                        }
                                    });

                                }
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

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
        }
    }


    public void getArtist() {

        if (prefrence.getBooleanValue("testing_account_otp")) {
            Log.e("testing_account_otp", "" + prefrence.getBooleanValue("testing_account_otp"));
            devicetoken = "";
        }

        Log.e("devicetoken", "1" + " devicetoken " + "->" + devicetoken + " onoff " + "->"+ " getDeviceName " + "->" + getDeviceName() + " uid " + "->" + userDTO.getUser_id());

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
                                try {

                                    // ProjectUtils.showToast(getActivity(), msg);
                                    artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);

                                    if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                                        prefrence.setValue(Consts.IS_ONLINE, "1");
                                    } else {
                                        prefrence.setValue(Consts.IS_ONLINE, "0");

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {


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


    public void changeColorItem(Menu menu, int id) {
        MenuItem tools = menu.findItem(id);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

    }

    public void applyCustomFont(MenuItem mi) {
        Typeface customFont = FontCache.getTypeface("Poppins-Regular.otf", BaseActivity.this);
        SpannableString spannableString = new SpannableString(mi.getTitle());
        spannableString.setSpan(new CustomTypeFaceSpan("", customFont), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(spannableString);
    }

    public void showImage() {
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        Glide.with(mContext).
                load(userDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_profile);
        tvName.setText(userDTO.getName());
    }

    public void loadHomeFragment(final Fragment fragment, final String TAG) {

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                swOnOff.setVisibility(View.GONE);
                tvOnOff.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    public void loadHomeFragmentRing(final Fragment fragment, final String TAG, String notification_name, String messagebody) {
        Log.e("notification_name", " base 2:-- " + notification_name);

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                swOnOff.setVisibility(View.GONE);
                tvOnOff.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("notification_name", notification_name);
                bundle1.putString("messagebody", messagebody);
                fragment.setArguments(bundle1);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    public void loadHomeFragmentWithData(final Fragment fragment, final String TAG, String data) {
        Log.e("loadHomeFragmentWith", " base 2:-- " + data);

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                swOnOff.setVisibility(View.GONE);
                tvOnOff.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("data", data);
                fragment.setArguments(bundle1);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        drawer.closeDrawers();

        invalidateOptionsMenu();
    }


    public void drawerOpen() {

        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }


    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);

                switch (menuItem.getItemId()) {
                    case R.id.nav_jobs:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MAIN;
                        fragmentTransaction.replace(R.id.frame, new JobsFrag());

                        break;

                    case R.id.nav_chat:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_CHAT;
                        fragmentTransaction.replace(R.id.frame, new ChatList());
                        break;

                    case R.id.nav_language:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        startActivity(new Intent(BaseActivity.this, SelectLanguageActivity.class).putExtra("pathway", true).putExtra("lang", Locale.getDefault().getLanguage()));
                        break;
                    case R.id.nav_bookings:
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                    //    fragmentTransaction.replace(R.id.frame, new NewBookings());
                        fragmentTransaction.replace(R.id.frame, new OrderDetailsFragment());
                        break;
                    case R.id.orders_details_new:
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_BOOKINGS_ALL;
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        fragmentTransaction.replace(R.id.frame, new OrderDetailsFragment());
                        break;
                    case R.id.nav_appointment:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_APPOINTMENT;
                        fragmentTransaction.replace(R.id.frame, new AppointmentFrag());
                        break;
                    case R.id.nav_notification:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new Notification());
                        break;

                    case R.id.nav_addrefer:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new AddReferFragment());
                        break;
                    case R.id.nav_viewerefer:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        fragmentTransaction.replace(R.id.frame, new ReferCustomerFragment());
                        break;

                    case R.id.nav_service:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_PROFILE;
                        fragmentTransaction.replace(R.id.frame, new MainserviceFragment());
                        break;
                    case R.id.nav_profile:
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_PROFILE;
                        fragmentTransaction.replace(R.id.frame, new ArtistProfileNewFragment());
                        break;

                    case R.id.nav_promotion:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 90;
                        CURRENT_TAG = TAG_PROMOTION;
                        fragmentTransaction.replace(R.id.frame, new PromotionFragment());
                        break;
                    case R.id.nav_earing:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_EARN;
                        fragmentTransaction.replace(R.id.frame, new MyEarning());
                        break;
                    case R.id.nav_history:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new PaidFrag());
                        break;
                    case R.id.nav_wallet:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        navItemIndex = 12;
                        CURRENT_TAG = TAG_WALLET;
                        fragmentTransaction.replace(R.id.frame, new Wallet());
                        break;
                    case R.id.nav_profilesetting:
                        ivSearch.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        navItemIndex = 13;
                        CURRENT_TAG = TAG_PROFILE_SETINGS;
                        fragmentTransaction.replace(R.id.frame, new ProfileSetting());
                        break;
                    case R.id.nav_tickets:
                        ivSearch.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        swOnOff.setVisibility(View.GONE);
                        navItemIndex = 14;
                        CURRENT_TAG = TAG_TICKETS;
                        fragmentTransaction.replace(R.id.frame, new Tickets());
                        break;


                    case R.id.nav_update:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);

                        navItemIndex = 15;
                        CURRENT_TAG = TAG_UPDATE;
                        fragmentTransaction.replace(R.id.frame, new AppUpdateFragment());
                        break;

                    case R.id.nav_contact:
                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 25;
                        CURRENT_TAG = TAG_HISTORY;
                        fragmentTransaction.replace(R.id.frame, new ContactUs());
                        break;

                    case R.id.nav_madeindia:

                        break;

                    case R.id.nav_signout:
                        confirmLogout();
                        break;

                    case R.id.nav_cabbookings:
                        swOnOff.setVisibility(View.VISIBLE);
                        tvOnOff.setVisibility(View.VISIBLE);

                        navItemIndex = 18;
                        CURRENT_TAG = TAG_BOOKINGS_cab;
                        fragmentTransaction.replace(R.id.frame, new MyOrders());
                        break;
                    case R.id.nav_cabbookings_new:
                        swOnOff.setVisibility(View.VISIBLE);
                        tvOnOff.setVisibility(View.VISIBLE);

                        navItemIndex = 18;
                        CURRENT_TAG = TAG_BOOKINGS_cab;
                        fragmentTransaction.replace(R.id.frame, new PackagingTrayFragment());
                        break;

                    case R.id.nav_home:
                        swOnOff.setVisibility(View.VISIBLE);
                        tvOnOff.setVisibility(View.VISIBLE);
                        fragmentTransaction.replace(R.id.frame, new HomeScreenFragment());
                        break;

                    case R.id.nav_tvideo:

                        swOnOff.setVisibility(View.GONE);
                        tvOnOff.setVisibility(View.GONE);
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 20;
                        CURRENT_TAG = TAG_TVIDEO;
                        fragmentTransaction.replace(R.id.frame, new TVideoFragment());
                        break;
                    default:
                        ivSearch.setVisibility(View.GONE);
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BOOKINGS_cab;
                        tvOnOff.setVisibility(View.VISIBLE);
                        swOnOff.setVisibility(View.VISIBLE);
                        fragmentTransaction.replace(R.id.frame, new MyOrders());
                        break;

                }
                fragmentTransaction.commitAllowingStateLoss();
                drawer.closeDrawers();
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);


                return true;
            }
        });

    }

    public void confirmLogout() {
        try {
            new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(getResources().getString(R.string.logout_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            com.kamaii.partner.Glob.BUBBLE_VALUE = "1";


                            dialog.dismiss();
                            prefrence.clearAllPreferences();
                            logoutflag = true;

                            isOnline("0");
                            Intent intent = new Intent(BaseActivity.this, CheckSignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {

        if (!Settings.canDrawOverlays(this)) { // WHAT IF THIS EVALUATES TO FALSE.

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));

            startActivityForResult(intent, 125);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {

            if (navItemIndex != 0) {

                navItemIndex = 0;
                CURRENT_TAG = TAG_MAIN;
                loadHomeFragment(new MyOrders(), CURRENT_TAG);
                return;
            }
        }

        //super.onBackPressed();
        clickDone();
    }

    public void clickDone() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Log.e("AAAAAAAAA", "ERRRRR" + artistDetailsDTO.getCab_booking_flag());
                        dialog.dismiss();

                        if (check_flag == 0) {

                            if (artistDetailsDTO.getCab_booking_flag().equalsIgnoreCase("1")) {

                                Intent intent = new Intent(BaseActivity.this, BasicInfoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {

                                if (prefrence.getValue(Consts.IS_ONLINE).equalsIgnoreCase("1")) {

                                    isOnline("0");
                                } else {

                                    finish();
                                }
                            }
                        } else {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_MAIN);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }

                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void clickProfile() {
        check_flag = 1;
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getResources().getString(R.string.incomplete_profile))
                .setMessage(getResources().getString(R.string.incomplete_profile_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkManager.isConnectToInternet(mContext)) {
                            Glob.BUBBLE_VALUE = "1";
//                            Intent intent = new Intent(mContext, EditPersnoalInfo.class);
                            Intent intent = new Intent(mContext, BasicInfoActivity.class);
                            intent.putExtra(Consts.CATEGORY_list, categoryDTOS);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_up, R.anim.stay);
                        } else {
                            // ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                            // ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                        }

                        dialog.dismiss();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.skip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void getMyLocation() {

        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(BaseActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(1000);
                    locationRequest.setFastestInterval(1000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback() {

                        @Override
                        public void onResult(@NonNull Result result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(BaseActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);


                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(BaseActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    //finish();
                                    break;
                            }
                        }


                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {

            Log.e("PLACE_APU", "baseactivity called");

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e("STR", "1");
                        String str = Build.BRAND;

                        if (str.equalsIgnoreCase("xiaomi")) {

                            if (!prefrence.getBooleanValue(Consts.EXTRA_PERMISSION_GRANT)) {

                                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                                intent.setClassName("com.miui.securitycenter",
                                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                                intent.putExtra("extra_pkgname", getPackageName());
                                startActivity(intent);
                                prefrence.setBooleanValue(Consts.EXTRA_PERMISSION_GRANT, true);
                            }
                        }
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;

                }
                break;
        }

        if (resultCode == Activity.RESULT_OK && requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            Log.e("PLACE_APU", "baseactivity 2 called");

            Place place = Autocomplete.getPlaceFromIntent(data);

            String final_user_address = place.getAddress();
            String landmark_name = place.getName();

            getPlace_result = landmark_name + ", " + final_user_address;
            // shipping_etLocationD.setText(final_user_address+", "+landmark_name);
        }

        if (requestCode == 23) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
            fragment.onActivityResult(requestCode, resultCode, data);
        }


        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    //Permission is not available. Display error text.
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
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

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = (earthRadius * c);

        return dist; // output distance, in MILES
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(BaseActivity.this)
                .enableAutoManage(BaseActivity.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    public void updateLocation() {
        //  Toast.makeText(mContext, "update called", Toast.LENGTH_LONG).show();

        Log.e("PARAM", "" + parms.toString());
        //ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_LOCATION_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("PARAMS2", "" + response.toString());

                } else {
                    //   ProjectUtils.showToast(mContext, msg);
//                    Log.e("PARAMS3", "" + response.toString());

                }
            }
        });
    }

    public void language(String language) {
        String languageToLoad = language; // your language

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        BaseActivity.this.getResources().updateConfiguration(config,
                BaseActivity.this.getResources().getDisplayMetrics());

        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);


    }

    public void getCategory() {
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        clickProfile();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }
        });
    }

    public void getApproveStatus() {
        new HttpsRequest(Consts.GET_APPROVAL_STATUS_API, parmsApprove, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        int approval_status = response.getInt("approval_status");
                        userDTO.setApproval_status(approval_status);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        if (approval_status == 0) {
                            Intent browserIntent = new Intent(BaseActivity.this, BasicInfoActivity.class);
                            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(browserIntent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }

    public void approveDailog() {

        dialogfinish.show();

        dno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfinish.dismiss();
            }
        });

        dyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfinish.dismiss();
            }
        });

        laychat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.link/c59fzm"));
                startActivity(browserIntent);
            }
        });

        layudoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ivSearch.setVisibility(View.GONE);
                navItemIndex = 0;
                CURRENT_TAG = TAG_DOCUMENT;

                dialogfinish.dismiss();
                Intent browserIntent = new Intent(BaseActivity.this, DocumentUploadTwoActivity.class);
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(browserIntent);
                finish();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

      /*  PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();*/

        if (CURRENT_TAG.equalsIgnoreCase(TAG_TVIDEO)) {

        } else {

            if (Glob.BUBBLE_VALUE.equalsIgnoreCase("0")) {

                if (prefrence.getValue(Consts.IS_ONLINE).equalsIgnoreCase("1")) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                        startService(new Intent(BaseActivity.this, FloatingWidgetService.class).putExtra("activity_background", true));

                    }
                }

            } else {

            }
        }


    }

    private void errorToast() {
        Toast.makeText(this, "Draw over other app permission not available. Can't start the application without the permission.", Toast.LENGTH_LONG).show();
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

        Log.e("devicetoken", "baseactivity");

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
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        offline_msg = object.getString("msg");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            if (!logoutflag) {

                                getArtist();
                                Intent i = new Intent();
                                i.setAction(Intent.ACTION_MAIN);
                                i.addCategory(Intent.CATEGORY_HOME);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            } else {

                            }

                        } else {

                        }


                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               /* Toast.makeText(mContext, "Try again. Server is not responding",
                        LENGTH_LONG).show();
*/
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        String str = Build.BRAND;
        Log.e("STR", " 3 " + str);

        if (str.equalsIgnoreCase("xiaomi")) {

            if (!prefrence.getBooleanValue(Consts.EXTRA_PERMISSION_GRANT)) {

                Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                startActivity(intent);
                prefrence.setBooleanValue(Consts.EXTRA_PERMISSION_GRANT, true);
            }
        }
        /*else if (str.equalsIgnoreCase("OPPO")){

            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //com.coloros.safecenter/.sysfloatwindow.FloatWindowListActivity
            ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");//悬浮窗管理页面
            intent.setComponent(comp);
            startActivity(intent);
        }*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            stopService(new Intent(BaseActivity.this, FloatingWidgetService.class).putExtra("activity_background", true));

        } else {

        }


    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        super.onDestroy();
        this.mWakeLock.release();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
