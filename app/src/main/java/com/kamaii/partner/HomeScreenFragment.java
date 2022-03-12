package com.kamaii.partner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.databinding.DailogArProductBinding;
import com.kamaii.partner.databinding.FragmentHomeScreenBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.model.CommonServiceModel;
import com.kamaii.partner.model.ThirdCateModel;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.MyService;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.AddServicesActivity;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.AdapterCabBookings;
import com.kamaii.partner.ui.adapter.SliderAdapter;
import com.kamaii.partner.ui.fragment.AddReferFragment;
import com.kamaii.partner.ui.fragment.ArtistProfile;
import com.kamaii.partner.ui.fragment.CabBookingsFragment;
import com.kamaii.partner.ui.fragment.CheckInternetFragment;
import com.kamaii.partner.ui.fragment.ContactUs;
import com.kamaii.partner.ui.fragment.MainserviceFragment;
import com.kamaii.partner.ui.fragment.MyEarning;
import com.kamaii.partner.ui.fragment.NewBookings;
import com.kamaii.partner.ui.fragment.PaidFrag;
import com.kamaii.partner.ui.fragment.PromotionFragment;
import com.kamaii.partner.ui.fragment.SafetyFragment;
import com.kamaii.partner.ui.fragment.Services;
import com.kamaii.partner.ui.fragment.TVideoFragment;
import com.kamaii.partner.ui.fragment.Wallet;
import com.kamaii.partner.ui.models.AddressModel;
import com.kamaii.partner.ui.models.CategoryModel;
import com.kamaii.partner.ui.models.Description;
import com.kamaii.partner.ui.models.GuideModel;
import com.kamaii.partner.ui.models.HomePageModel;
import com.kamaii.partner.ui.models.SiderModel;
import com.kamaii.partner.ui.models.SubCateModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.FileUtility;
import com.kamaii.partner.utils.SpinnerDialog;
import com.kamaii.partner.utils.SpinnerDialogDescription;
import com.kamaii.partner.utils.SpinnerDialogFour;
import com.kamaii.partner.utils.SpinnerDialogSubCate;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.turf.TurfConversion;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.prefs.PreferencesManager;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static freemarker.template.utility.StringUtil.capitalize;

public class HomeScreenFragment extends Fragment {

    private BaseActivity baseActivity;
    private FragmentHomeScreenBinding binding;
    CustomTextViewBold total_booking, todays_earn;
    private ArrayList<ArtistBooking> artistBookingsList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private String TAG = HomeScreenFragment.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parmsartist = new HashMap<>();
    public static HashMap<String, File> paramsFile;
    private HashMap<String, String> paramsucategory = new HashMap<>();
    private HashMap<String, String> paramsthird = new HashMap<>();
    private HashMap<String, String> paramsFour = new HashMap<>();

    private HashMap<String, String> paramsUpdate;
    private HashMap<String, String> paramsDecline;
    private LayoutInflater myInflater;
    Context mContext;
    public static Uri uri = null;
    private ArtistDetailsDTO artistDetailsDTO;
    private String bookingid = "";
    String currentdate = "";
    LocalBroadcastManager bm;
    private SharedPreferences firebase;
    String devicetoken = "";
    ProgressDialog progressDialog;
    String dcatid = "", dsubcatid = "", dthirdcatid = "", dclatititute = "", dclongitiute = "";
    String category_id = "", sub_category_id = "", third_id = "", vechilenumber = "", servicename = "", serviceid = "";
    String encodedBase64 = "";
    private HashMap<String, String> parmsSubcatImage = new HashMap<>();

    private DirectionsRoute currentRoute;
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
    private SpinnerDialog spinnerDialogCate;
    private SpinnerDialogSubCate spinnerDialogsubcate;
    private com.kamaii.partner.utils.SpinnerDialogThird SpinnerDialogThird;
    private SpinnerDialogFour spinnerDialogFour;
    private SpinnerDialogDescription spinnerDialogDes;

    private ArrayList<GuideModel> guideList = new ArrayList<>();
    private ArrayList<CategoryModel> categoryarraylist = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListserivce = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew = new ArrayList<>();
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();
    ArrayList<Description> descriptionArrayList = new ArrayList<>();
    ArrayList<CommonServiceModel> fourModelArrayList = new ArrayList<>();

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


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
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

        //getGuide();

        //binding.progress.getProgressDrawable().setColorFilter(
          //      R.color.pink_color, android.graphics.PorterDuff.Mode.SRC_IN);

        //Toast.makeText(baseActivity, "uybawetcfgahcg fbajshgdb", LENGTH_SHORT).show();


        binding.sixthLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.loadHomeFragment(new TVideoFragment(), "20");
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
        Date todayyyy = new Date();
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentdate = formatt.format(todayyyy);


        baseActivity.swOnOff.setVisibility(View.VISIBLE);
        baseActivity.tvOnOff.setVisibility(View.VISIBLE);


        binding.guideLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new ContactUs(), "8");

                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPreferencesManager.resetAll();
                        SpotlightSequence.getInstance(getActivity(), null)
                                .addSpotlight(binding.catLinear, category_step_title, category_step_txt, INTRO_CAT)
                                .addSpotlight(binding.serviceLinear, service_step_title, service_step_txt, INTRO_SERVICE)
                                .addSpotlight(baseActivity.swOnOff, switch_step_title, switch_step_txt, INTRO_SWITCH)
                                .addSpotlight(baseActivity.menuLeftIV, "Drawer Menu", "Click to know App Features", INTRO_MENU)
                                .addSpotlight(binding.homescreenImage3, promotion_step_title, promotion_step_txt, INTRO_PROMOTION)
                                .startSequence();


                    }
                }, 400);*/
            }
        });

     /*   binding.profileLinear.setOnClickListener(new View.OnClickListener() {
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

        binding.serviceVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.navItemIndex = 41;
                baseActivity.loadHomeFragment(new MainserviceFragment(), "8");
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

        binding.todayOrdersTxtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new CabBookingsFragment(), "80");

            }
        });

        binding.pendingOrdersTxtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.navItemIndex = 55;
                baseActivity.loadHomeFragment(new CabBookingsFragment(), "80");

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

        binding.trialbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoDialog.show();

                demo_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        demoDialog.dismiss();
                    }
                });


            }
        });


        baseActivity.headerNameTV.setText("");

        if (NetworkManager.isConnectToInternet(getActivity())) {


            gettvideo();
            checkaddress();
            getCategory();
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
                                //binding.partnerTxt.setText(online_msg);

                                isOnline("0");
                            } else {
                                paramsUpdate.put(Consts.IS_ONLINE, "1");
                                //  binding.partnerTxt.setText(offline_msg);

                                isOnline("1");
                            }
                        }
                    } else {
                        baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
                        binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
                        //binding.layfindtrip.setVisibility(View.GONE);
                        //     binding.todaysStatusLinear.setVisibility(View.GONE);
          //              binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);
                        //   binding.trialbtnSubmit.setVisibility(View.GONE);
                        binding.txtoffonline.setTextColor(Color.BLACK);
                        binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));
                        // Toast.makeText(getActivity(), getResources().getString(R.string.incomplete_profile_msg), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (baseActivity.swOnOff.isChecked()) {
                        baseActivity.swOnOff.setChecked(false);
                    } else {
                        baseActivity.swOnOff.setChecked(true);
                    }
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void getImageClick() {

        binding.homescreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cat_image_link.equalsIgnoreCase("0")) {

                    getFragment(cat_image_link);
                } else {
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

                baseActivity.navItemIndex = 32;
                baseActivity.loadHomeFragment(new TVideoFragment(), "20");
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

    public void getArtist(String value) {

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
                                    binding.partnerTxt.setText(online_msg);
                                } else {
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

                        Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getContext(), "Try again. Server is not responding",
                        LENGTH_LONG).show();


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
                    Log.e("BASE", "" + response.toString());

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        Log.e("BASE", "" + message);
                        Log.e("BASE", "" + sstatus);

                        if (sstatus == 1) {

                            getArtist("1");


                        } else {


                            baseActivity.swOnOff.setChecked(false);
                           /* Toast.makeText(getActivity(), message + "--",
                                    LENGTH_LONG).show();*/
                        }


                    } else {

                        Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();


            }
        });

    }

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


                        /*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SpotlightSequence.getInstance(getActivity(), null)
                                        .addSpotlight(binding.catLinear, category_step_title, category_step_txt, INTRO_SERVICE)
                                        .addSpotlight(binding.serviceLinear, service_step_title, service_step_txt, INTRO_SWITCH)
                                        .addSpotlight(baseActivity.swOnOff, switch_step_title, switch_step_txt, INTRO_CAT)
                                        .addSpotlight(binding.homescreenImage3, promotion_step_title, promotion_step_txt, INTRO_CAT)
                                        .addSpotlight(binding.homescreenImage5, refer_step_title, refer_step_txt, INTRO_CAT)
                                        .addSpotlight(binding.homescreenImage2, tvideo_step_title, tvideo_step_txt, INTRO_CAT)
                                        .startSequence();
                            }
                        }, 400);
*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void getEarning() {

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

                        //           binding.totalBooking.setText(total_booking);

                        double tot_earn = Double.parseDouble(total_earning);
                        //         binding.todaysEarn.setText(String.valueOf(new DecimalFormat("##.##").format(tot_earn)));
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
        progressDialog.show();
        Log.e("Vider", "1");// ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_PARTNER_HOMEPAGE_DATA, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

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
                        //      binding.totalVisitorProfile.setText(homePageModel.getTotalVisitProfile());
                        //      binding.totalItemVisit.setText(homePageModel.getItemVisit());


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
                      //  Glide.with(getActivity()).load(homePageModel.getShare_profile()).into(binding.homescreenImage3);
                        Glide.with(getActivity()).load(homePageModel.getRefer_img()).into(binding.homescreenImage5);

                        Log.e("tvideoModelArrayList", "" + tvideoModelArrayList.toString());

                        // tvideoModelArrayList = homePageArrayList.get(0).getCatelog();

                        getImageClick();
                      /*  CompositePageTransformer pageTransformer = new CompositePageTransformer();
                        pageTransformer.addTransformer(new MarginPageTransformer(40));
                        pageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                            @Override
                            public void transformPage(@NonNull View page, float position) {
                                float r = 1 - Math.abs(position);
                                page.setScaleY(0.95f + r + 0.05f);
                            }
                        });
*/
                        //    binding.pagerhome.setPageTransformer(pageTransformer);
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

    public void getSlider() {

        Log.e("Vider", "0");

     /*   binding.bannerDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.sliderLayout.setVisibility(View.GONE);
            }
        });
*/
        //Log.e("Vider", "5");
    }

    public void showData() {


        if (artistBookingsList.size() == 0) {

            Log.e("artist", "121");
          //  binding.tvNo.setVisibility(View.GONE);
            // binding.mapView.setVisibility(View.VISIBLE);
            //  binding.trialbtnSubmit.setVisibility(View.VISIBLE);
         //   binding.lay.setVisibility(View.VISIBLE);
            // binding.sliderLayout.setVisibility(View.VISIBLE);
            //       binding.forthRelative.setVisibility(View.VISIBLE);
            //       binding.relativeTwo.setVisibility(View.VISIBLE);
            // binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //    binding.fifthLinear.setVisibility(View.VISIBLE);
            //    binding.sixthLinear.setVisibility(View.VISIBLE);
            //    binding.seventhLinear.setVisibility(View.VISIBLE);
            //    binding.eightLinear.setVisibility(View.VISIBLE);

            if (artistDetailsDTO != null) {
                if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                    //binding.trialbtnSubmit.setVisibility(View.GONE);

                } else {
                    //  binding.trialbtnSubmit.setVisibility(View.GONE);

                }
            }
        } else {
            for (int i = 0; i < artistBookingsList.size(); i++) {

                if (artistBookingsList.get(0).getBooking_flag().equalsIgnoreCase("0")) {
                    binding.sliderLayout.setVisibility(View.GONE);
                    //           binding.relativeTwo.setVisibility(View.GONE);
                    binding.thirdRelative.setVisibility(View.GONE);
                    //           binding.forthRelative.setVisibility(View.GONE);
                    binding.scrollviewHomescreen.setVisibility(View.GONE);
                    //            binding.fifthLinear.setVisibility(View.GONE);
                    //            binding.sixthLinear.setVisibility(View.GONE);
                    //            binding.seventhLinear.setVisibility(View.VISIBLE);
                    //         binding.eightLinear.setVisibility(View.VISIBLE);


                    //       binding.trialbtnSubmit.setVisibility(View.GONE);

                    binding.lay.setVisibility(View.GONE);
                    try {
                        bookingid = artistBookingsList.get(0).getId();
                        int seocndserver = Integer.parseInt(artistBookingsList.get(0).getSecond());
                        int timer = Integer.parseInt(artistBookingsList.get(0).getStarttimer());
                        if (seocndserver < timer) {
                            seocndserver = timer - seocndserver;
                            long ssss = seocndserver * 1000;


                            cT = new CountDownTimer(ssss, 1000) {

                                public void onTick(long millisUntilFinished) {

                                    int va = (int) ((millisUntilFinished % 60000) / 1000);

                                }

                                public void onFinish() {

                                    if (accept == false) {

                                        try {
                                            Intent intent = new Intent(getActivity(), MyService.class);
                                            getActivity().stopService(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            };
                            cT.start();


                            if (checkarss(artistBookingsList.get(0).getProduct().get(0).getCategory_id())) {
                                dcatid = artistBookingsList.get(0).getProduct().get(0).getCategory_id();
                                dsubcatid = artistBookingsList.get(0).getProduct().get(0).getSub_category_id();
                                dthirdcatid = artistBookingsList.get(0).getProduct().get(0).getSublevel_category();
                                dclatititute = artistBookingsList.get(0).getC_latitude();
                                dclongitiute = artistBookingsList.get(0).getC_longitude();

                            } else {

                                dcatid = artistBookingsList.get(0).getProduct().get(0).getCategory_id();
                                dsubcatid = artistBookingsList.get(0).getProduct().get(0).getSub_category_id();
                                dthirdcatid = artistBookingsList.get(0).getProduct().get(0).getSublevel_category();
                                dclatititute = artistBookingsList.get(0).getC_latitude();
                                dclongitiute = artistBookingsList.get(0).getC_longitude();
                                Log.e("tvLocationdest", "" + artistBookingsList.get(0).getDestinationaddress());

                                String locationstatus = artistBookingsList.get(0).getLocation_status();
                            }

                        } else {


                        }

                    } catch (Exception e) {

                    }

                } else {

                    //         binding.trialbtnSubmit.setVisibility(View.GONE);
                }
            }
            //   binding.trialbtnSubmit.setVisibility(View.GONE);
            // binding.mapView.setVisibility(View.VISIBLE);
            binding.sliderLayout.setVisibility(View.GONE);
            //         binding.relativeTwo.setVisibility(View.GONE);
            binding.thirdRelative.setVisibility(View.GONE);
            //        binding.forthRelative.setVisibility(View.GONE);
            binding.scrollviewHomescreen.setVisibility(View.GONE);
            //      binding.fifthLinear.setVisibility(View.GONE);
            //      binding.sixthLinear.setVisibility(View.GONE);
            //      binding.seventhLinear.setVisibility(View.VISIBLE);
            //         binding.eightLinear.setVisibility(View.VISIBLE);


            binding.lay.setVisibility(View.GONE);

        }


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

    public void showDatfirst() {

        if (artistDetailsDTO != null) {
            baseActivity.swOnOff.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            //    binding.sliderLayout.setVisibility(View.VISIBLE);
            //binding.relativeTwo.setVisibility(View.VISIBLE);
            //   binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //        binding.fifthLinear.setVisibility(View.VISIBLE);
            //       binding.sixthLinear.setVisibility(View.VISIBLE);
            //        binding.seventhLinear.setVisibility(View.VISIBLE);
            //        binding.eightLinear.setVisibility(View.VISIBLE);

            //   binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            //  binding.layfindtrip.setVisibility(View.VISIBLE);
            //binding.todaysStatusLinear.setVisibility(View.VISIBLE);
   //         binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //      binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            //      binding.sliderLayout.setVisibility(View.VISIBLE);
            //        binding.relativeTwo.setVisibility(View.VISIBLE);
            //      binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //        binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //       binding.fifthLinear.setVisibility(View.VISIBLE);
            //      binding.sixthLinear.setVisibility(View.VISIBLE);
            //      binding.seventhLinear.setVisibility(View.VISIBLE);
            //          binding.eightLinear.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            //binding.layfindtrip.setVisibility(View.GONE);
            //     binding.todaysStatusLinear.setVisibility(View.GONE);
    //        binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            //        binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));


            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }

    public void showDataddd() {

        if (artistDetailsDTO != null) {
            baseActivity.swOnOff.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setVisibility(View.VISIBLE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
          //  binding.lay.setVisibility(View.VISIBLE);
            //    binding.sliderLayout.setVisibility(View.VISIBLE);
            // binding.relativeTwo.setVisibility(View.VISIBLE);
            //    binding.thirdRelative.setVisibility(View.VISIBLE);
         //   binding.thirdRelative.setVisibility(View.GONE);
            // binding.forthRelative.setVisibility(View.VISIBLE);
            //binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //     binding.fifthLinear.setVisibility(View.VISIBLE);
            //    binding.sixthLinear.setVisibility(View.VISIBLE);
            //     binding.seventhLinear.setVisibility(View.VISIBLE);
            //        binding.eightLinear.setVisibility(View.VISIBLE);

            //      binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_online));
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            //   binding.layfindtrip.setVisibility(View.VISIBLE);
            //binding.todaysStatusLinear.setVisibility(View.VISIBLE);
     //       binding.scrollviewHomescreen.setPadding(0, 0, 0, 0);

            //   binding.trialbtnSubmit.setVisibility(View.GONE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));


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

            //     binding.sliderLayout.setVisibility(View.VISIBLE);
            //    binding.relativeTwo.setVisibility(View.VISIBLE);
            //     binding.thirdRelative.setVisibility(View.VISIBLE);
            binding.thirdRelative.setVisibility(View.GONE);
            //      binding.forthRelative.setVisibility(View.VISIBLE);
            binding.scrollviewHomescreen.setVisibility(View.VISIBLE);
            //      binding.fifthLinear.setVisibility(View.VISIBLE);
            //     binding.sixthLinear.setVisibility(View.VISIBLE);
            //      binding.seventhLinear.setVisibility(View.VISIBLE);
            //     binding.eightLinear.setVisibility(View.VISIBLE);


            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText(getResources().getString(R.string.you_re_offline));
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            //binding.layfindtrip.setVisibility(View.GONE);
            //       binding.todaysStatusLinear.setVisibility(View.GONE);
       //     binding.scrollviewHomescreen.setPadding(0, 20, 0, 0);

            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));


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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        /*if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtistcat();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

        }*/
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


            }
            if (requestCode == 23) {


                Uri imageUri = data.getData();

                String path = FileUtility.getPath(getActivity(), imageUri);
                File file = new File(path);
                paramsFile.put(Consts.IMAGE, file);
                AdapterCabBookings.lldauploadImageLayout.setVisibility(View.GONE);
                AdapterCabBookings.img_upload.setVisibility(View.VISIBLE);
                AdapterCabBookings.img_upload.setImageURI(imageUri);
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


    public void getCategory() {


        new HttpsRequest(Consts.GET_DEMO_BOOKING_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

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
                        Toast.makeText(getActivity(), "Try again. Server is not responding",
                                LENGTH_LONG).show();


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
}