package com.kamaii.partner.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.Help;
import com.kamaii.partner.LocationService;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.ImagePickSetOnclickListner;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.adapter.AddressBottomSheetAdapter;
import com.kamaii.partner.ui.fragment.Wallet;
import com.kamaii.partner.utils.CustomButton;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.FileUtility;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.InputOpenFieldView;
import com.kamaii.partner.utils.MainFragment;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialog;
import com.kamaii.partner.utils.SpinnerDialogAvailableTime;
import com.webknight.filemanager.Constant;
import com.webknight.filemanager.activity.ImagePickActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class BasicInfoActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private String TAG = BasicInfoActivity.class.getSimpleName();
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private Context mContext;
    private CustomEditText etNameD, etAboutD, etLocationD, etacc_no;
    private CustomTextViewBold artist_name, etCityD, etCategoryD;
    private CustomButton btnSubmit;
    private LinearLayout llBack;
    String address_radio_txt = "Home";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    File fileone = null, filetwo = null, filethree = null;

    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private ArtistDetailsDTO artistDetailsDTO;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1000;
    private double lats = 0;
    private double longs = 0;
    private BaseActivity baseActivity = new BaseActivity();
    String user_profile_uri_str = "";
    private HashMap<String, String> paramsphoto;
    ProgressDialog progressDialog;
    String pathone = "", pathtwo = "", paththree = "";
    String flag = "";

    private HashMap<String, String> paramsUpdate = new HashMap<>();
    private HashMap<String, String> paramsgatdata = new HashMap<>();
    private HashMap<String, String> paramsCat = new HashMap<>();
    private UserDTO userDTO;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private SharedPrefrence prefrence;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage = "";
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;
    private HashMap<String, File> paramsFile = new HashMap<>();
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    CustomEditText etrefernamenumber;
    HashMap<String, String> parms = new HashMap<>();
    String get_business_name = "";
    String get_location = "";
    String get_city = "";
    String get_profile_uri = "";
    String get_des = "";
    String get_reference_code = "";


    String pan_card = "";
    String adhar_card_back = "";
    String adhar_card = "";
    String cat_id = "";
    String cat_name = "";
    String final_approval = "";
    String house_no = "";
    String bank_status = "";
    CustomTextView basicinfo_map_pin;
    GoogleMap mMap;
    Double live_latitude;
    Double live_longitude;
    boolean logoutflag = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    LinearLayout basicinfo_linear_layout;
    RelativeLayout map_fragment_linear, map_relative;
    TextInputLayout three, three123;
    LatLng latLng123;
    boolean map_flag = false;
    CustomTextView change_address_btn;
    CustomEditText house_no_etx, society_name_etx;
    RadioGroup add_addressradioGroup;
    RadioButton add_home_radio, add_work_radio, add_other_radio;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    String final_user_address = "";
    String landmark_name = "";
    String house_no_str = "";
    String society_name_str = "";
    String street_address_str = "";
    String live_address_str = "";
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    ArrayList<String> apinnerList;
    private FusedLocationProviderClient mFusedLocationClient;
    boolean flaging = true;
    String category_id = "";
    SpinnerDialogAvailableTime availableTime;
    ImageView layaafront, layaaback, laypancard;
    ScrollView scrollView;

    boolean from_wallet = false;
    String from_splash_status = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ){

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        mContext = BasicInfoActivity.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        Places.initialize(BasicInfoActivity.this, getResources().getString(R.string.api_key));
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        layaafront = findViewById(R.id.layaafront);
        layaaback = findViewById(R.id.layaaback);
        laypancard = findViewById(R.id.laypancard);
        from_wallet = getIntent().getBooleanExtra("from_wallet", false);


        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        etCityD = findViewById(R.id.etCityD);
        scrollView = findViewById(R.id.basic_info_scroll);
        add_addressradioGroup = findViewById(R.id.add_addressradioGroup);
        add_home_radio = findViewById(R.id.add_home_radio);
        add_work_radio = findViewById(R.id.add_work_radio);
        add_other_radio = findViewById(R.id.add_other_radio);
        change_address_btn = findViewById(R.id.change_address_btn);
        house_no_etx = findViewById(R.id.house_no_etx);
        society_name_etx = findViewById(R.id.society_name_etx);
        basicinfo_map_pin = findViewById(R.id.basicinfo_map_pin);
        map_relative = findViewById(R.id.map_relative);
        change_address_btn = findViewById(R.id.change_address_btn);
        house_no_etx = findViewById(R.id.house_no_etx);
        society_name_etx = findViewById(R.id.society_name_etx);

        etLocationD = (CustomEditText) findViewById(R.id.etLocationD);

        apinnerList = new ArrayList<>();
        apinnerList.add("Ahmedabad");
        apinnerList.add("Others");

        if (getIntent().hasExtra("from_splash_status")) {

            from_splash_status = getIntent().getStringExtra("from_splash_status");

            if (from_splash_status.equalsIgnoreCase("1")) {
                scrollView.setVisibility(View.GONE);
                findViewById(R.id.btnSubmit).setVisibility(View.GONE);
                findViewById(R.id.laymsg).setVisibility(View.VISIBLE);
                findViewById(R.id.help_chat_linear).setVisibility(View.VISIBLE);
                findViewById(R.id.youtube_card).setVisibility(View.VISIBLE);
            }
        }
        findViewById(R.id.helppage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BasicInfoActivity.this, Help.class));
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                live_latitude = location.getLatitude();
                live_longitude = location.getLongitude();
                //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));

                Geocoder geocoder = new Geocoder(BasicInfoActivity.this, Locale.getDefault());
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
                        etLocationD.setText(obj.getAddressLine(0));
                        addAddressHashmap.put("street_address", obj.getAddressLine(0));
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
        });

        availableTime = new SpinnerDialogAvailableTime(this, apinnerList, "Select City");
        availableTime.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                flaging = false;
                etCityD.setText(item);
                etCityD.setClickable(true);
            }
        });

        layaafront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = "1";

                ImagePicker.Companion.with(BasicInfoActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                // dialog.show();


            }
        });

        layaaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = "2";
                ImagePicker.Companion.with(BasicInfoActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);


            }
        });

        laypancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flag = "14";

                ImagePicker.Companion.with(BasicInfoActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                // dialog.show();


            }
        });

        /*ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, apinnerList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        *///Setting the ArrayAdapter data on the Spinner
        // etCityD.setAdapter(aa);

        //Glide.with(this).load(R.drawable.lo).into(profile_img);

        etrefernamenumber = findViewById(R.id.etrefernamenumber);

        Log.e("TRACKER---", "1");
        getCategory();
        Log.e("TRACKER---", "2");
        getAddress();
        Log.e("TRACKER---", "3");
        setUiAction();
        Log.e("TRACKER---", "4");
        Log.e("TRACKER---", "5");

        findViewById(R.id.logout_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();

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

                            Intent intent = new Intent(BasicInfoActivity.this, CheckSignInActivity.class);
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

    private void getAddress() {

        //RelativeLayout address_relative = addressBottomSheet.findViewById(R.id.address_relative);

        RadioGroup add_addressradioGroup;
        ImageView back = findViewById(R.id.address_dialog_close_img);

        Typeface font2 = Typeface.createFromAsset(this.getAssets(), "Lato-Heavy.ttf");


        add_addressradioGroup = findViewById(R.id.add_addressradioGroup);
        //   address_submit = findViewById(R.id.address_submit);

        etLocationD.setTypeface(font2);
        house_no_etx.setTypeface(font2);
        society_name_etx.setTypeface(font2);

        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        //address_relative = findViewById(R.id.address_relative);
        // address_lay_dest_location = findViewById(R.id.address_lay_dest_location);
        // LinearLayout lll = bottomSheetDialog.findViewById(R.id.lll);
        RecyclerView recyclerView = findViewById(R.id.address_recycler);


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

    }

    private void findPlace() {
        Log.e("PLACE_API", "PLACE API CALLED 2");

        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(BasicInfoActivity.this);

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

    }

    public void getCategory() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API2, parmsCategory, this).stringPost("Kamaii", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

                if (flag) {
                    try {
                        Log.e("CAT_RES", "" + response.toString());

                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").getJSONObject(0).getJSONArray("getallcat").toString(), getpetDTO);


                        spinnerDialogCate = new SpinnerDialog(BasicInfoActivity.this, categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                flaging = false;
                                etCategoryD.setText(item);
                                etCategoryD.setClickable(true);
                                category_id = id;
                                submitCat(category_id);
                                //submitPersonalProfile(category_id);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
                    //  Toast.makeText(DocumentUploadTwoActivity.this, msg, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void setUiAction() {
        etCategoryD = findViewById(R.id.etCategoryD);
        etNameD = (CustomEditText) findViewById(R.id.etNameD);
        etAboutD = (CustomEditText) findViewById(R.id.etAboutD);
        artist_name = (CustomTextViewBold) findViewById(R.id.artist_name);

        btnSubmit = (CustomButton) findViewById(R.id.btnSubmit);
        llBack = (LinearLayout) findViewById(R.id.llBack);

        etCategoryD.setOnClickListener(this);
        etCityD.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        llBack.setOnClickListener(this);
        basicinfo_map_pin.setOnClickListener(this);

        //  etLocationD.setEnabled(false);
        artist_name.setText("Hello, " + userDTO.getName());
        paramsgatdata.put(Consts.USER_ID, userDTO.getUser_id());

        etNameD.setText(userDTO.getName());
        etNameD.setKeyListener(null);

        getBasicInfoData();

    }

    public void showData() {

        if (!cat_name.equalsIgnoreCase("null") || cat_name.equals(null)) {

            etCategoryD.setText(cat_name);
        }
        if (!adhar_card.equalsIgnoreCase("0")) {
            Glide.with(this).load(adhar_card).into(layaafront);
        }
        if (!adhar_card_back.equalsIgnoreCase("0")) {
            Glide.with(this).load(adhar_card_back).into(layaaback);
        }
        if (!pan_card.equalsIgnoreCase("0")) {
            Glide.with(this).load(pan_card).into(laypancard);
        }
        etNameD.setText(get_business_name);
        if (!house_no.equalsIgnoreCase("null") || house_no.equals(null)) {

            house_no_etx.setText(house_no);
        }
        // etAboutD.setText(get_des);
        if (!get_city.equalsIgnoreCase("null") || get_city.equals(null)) {

            Log.e("CITY_TRACK", " city :- " + get_city);

            etCityD.setText(get_city);
        }
        if (!get_location.equalsIgnoreCase("null") || get_location.equals(null) || get_location.equals("") || get_location.equalsIgnoreCase("")) {

            Log.e("CITY_location", " city :- " + get_city);

            etLocationD.setText(get_location);
        }
        // etacc_no.setText(artistDetailsDTO.getAccount_no());
//        etrefernamenumber.setText(get_reference_code);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etCategoryD:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    if (categoryDTOS.size() > 0)
                        spinnerDialogCate.showSpinerDialog();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;

            case R.id.etCityD:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    if (apinnerList.size() > 0)
                        availableTime.showSpinerDialog();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                break;

            case R.id.btnSubmit:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submitPersonalProfile(category_id);
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }

                //startActivity(new Intent(BasicInfoActivity.this, DocumentUploadTwoActivity.class));
                break;
            case R.id.add_profile_pic:
                //  Toast.makeText(mContext, "image clicked", Toast.LENGTH_SHORT).show();
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.llBack:
                Intent intent1 = new Intent(BasicInfoActivity.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;

            case R.id.basicinfo_map_pin:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        findPlace();
                    }
                });
                thread.start();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        com.kamaii.partner.Glob.BUBBLE_VALUE = "0";
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public void getImage() {


        Intent intent1 = new Intent(BasicInfoActivity.this, ImagePickActivity.class);
        intent1.putExtra(com.webknight.filemanager.activity.ImagePickActivity.IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {

            Log.e("PLACE_API", "PLACE API CALLED result");
            Place place = Autocomplete.getPlaceFromIntent(data);

            final_user_address = place.getAddress();
            landmark_name = place.getName();


            lats = place.getLatLng().latitude;
            longs = place.getLatLng().longitude;

            etLocationD.setText(landmark_name + final_user_address);
            addAddressHashmap.put("street_address", landmark_name + final_user_address);
            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(lats));

            Log.e("PLACE_API", "latitude :-- " + String.valueOf(lats));
            Log.e("PLACE_API", "longitude :-- " + String.valueOf(longs));
            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(longs));

        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 23) {

                Log.e("PLACE_API", "PLACE API CALLED 3");


                Uri picUri = data.getData();

                if (flag.equalsIgnoreCase("1")) {

                    layaafront.setImageURI(picUri);
                    pathone = picUri.getPath();
                    // pathone = FileUtility.getPath(BasicInfoActivity.this,picUri);

                } else if (flag.equalsIgnoreCase("2")) {

                    layaaback.setImageURI(picUri);
                    pathtwo = picUri.getPath();
                    //pathtwo = FileUtility.getPath(BasicInfoActivity.this,picUri);

                } else if (flag.equalsIgnoreCase("14")) {
                    laypancard.setImageURI(picUri);
                    paththree = picUri.getPath();
                    // pathelven = FileUtility.getPath(BasicInfoActivity.this,picUri);
                }


            }
        }

        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //getMyLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }


    }

    public void submitPersonalProfile(String cat_id) {
        // Toast.makeText(mContext, ""+user_profile_uri_str, Toast.LENGTH_SHORT).show();
        //


        if (etCategoryD.getText().toString().equals("Select Food Category")) {
            Toast.makeText(mContext, "Please select food category", Toast.LENGTH_SHORT).show();
            return;
        } else if (etCityD.getText().toString().equals("Select City")) {
            Toast.makeText(mContext, "Please select your city", Toast.LENGTH_SHORT).show();
            return;
        } else if (!validation(etLocationD, getResources().getString(R.string.val_location))) {
            return;
        } else if (!validation(house_no_etx, getResources().getString(R.string.house_no_validate))) {
            return;
        }else if (pathone.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathtwo.equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
            return;
        }  /*else if (pathOfImage.equals("") || pathOfImage.equals(null)) {
            Toast.makeText(mContext, "Upload your profile image!!", Toast.LENGTH_SHORT).show();
            return;
        }*/ else {
            if (NetworkManager.isConnectToInternet(mContext)) {

                fileone = new File(pathone);
                filetwo = new File(pathtwo);
                filethree = new File(paththree);

                if (from_wallet) {

                    if (pathone.equalsIgnoreCase("")) {
                        Toast.makeText(BasicInfoActivity.this, "Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
                        return;
                    } else if (pathtwo.equalsIgnoreCase("")) {
                        Toast.makeText(BasicInfoActivity.this, "Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        uploadImages();

                    }
                } else {
                    addAddress();

                }


            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(mContext, msg);
            return false;
        } else {
            return true;
        }
    }

    public void addAddress() {
        Log.e("PLACE_API", "address function called");
        house_no_str = house_no_etx.getText().toString();
        society_name_str = society_name_etx.getText().toString();
        street_address_str = etLocationD.getText().toString();

        addAddressHashmap.put("house_no", house_no_str);
        addAddressHashmap.put("society_name", society_name_str);
        addAddressHashmap.put("addresstype", address_radio_txt);

        Log.e("PLACE_API", "address params:-- " + addAddressHashmap.toString());
        new HttpsRequest(Consts.ADD_ADDRESS, addAddressHashmap, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    // Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    Log.e("PLACE_API", "address response" + response.toString());

                    paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                    paramsUpdate.put(Consts.NAME, ProjectUtils.getEditTextValue(etNameD));
                    paramsUpdate.put(Consts.BIO, "");
//                    paramsUpdate.put(Consts.ABOUT_US, ProjectUtils.getEditTextValue(etAboutD));

                    Log.e("CITY____", "" + etCityD.getText().toString());
                    paramsUpdate.put(Consts.CITY, etCityD.getText().toString());
                    paramsUpdate.put(Consts.CATEGORY_ID, category_id);
                    paramsUpdate.put(Consts.CATEGORY_NAME, etCategoryD.getText().toString());
                    paramsUpdate.put(Consts.COUNTRY, "");
                    // paramsUpdate.put(Consts.LOCATION, ProjectUtils.getEditTextValue(etLocationD));
                    paramsUpdate.put(Consts.BANK_NAME, "");
                    paramsUpdate.put(Consts.ACCOUNT_HOLDER_NAME, "");
                    paramsUpdate.put(Consts.ACCOUNT_NO, "");
                    paramsUpdate.put(Consts.LOCATION, etLocationD.getText().toString());
                    paramsUpdate.put(Consts.IFSC_CODE, "");
//                    paramsUpdate.put("ref_number", etrefernamenumber.getText().toString());
                    paramsFile.put(Consts.BANNER_IMAGE, file);
                    Log.e("PLACE_API", "live_lat" + String.valueOf(live_latitude));

                    Log.e("PLACE_API", "live_long" + String.valueOf(live_longitude));

                    paramsUpdate.put(Consts.LATITUDE, String.valueOf(live_latitude));
                    paramsUpdate.put(Consts.LONGITUDE, String.valueOf(live_longitude));
                    //    Log.e("Params", "" + paramsUpdate.toString());
                    //   Log.e("PARAM", paramsUpdate.toString());
                    updateProfile();

                } else {

                }
            }
        });
    }


    public void getBasicInfoData() {

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_BASIC_INFO_DATA_API, paramsgatdata, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                Log.e("ABC", response.toString());
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {

                        String s = response.toString();
                        Log.e("BASIC_INFO", "" + s);
                        get_business_name = response.getJSONObject("data").getString("bussiness_name");
                        get_location = response.getJSONObject("data").getString("location");
                        get_city = response.getJSONObject("data").getString("city");
                        get_profile_uri = response.getJSONObject("data").getString("user_image");
                        //   Log.e("get_profile_uri",""+get_profile_uri);
                        get_des = response.getJSONObject("data").getString("desc");
                        get_reference_code = response.getJSONObject("data").getString("ref_number");
                        pan_card = response.getJSONObject("data").getString("pan_card");
                        adhar_card_back = response.getJSONObject("data").getString("adhar_card_back");
                        adhar_card = response.getJSONObject("data").getString("adhar_card");
                        cat_id = response.getJSONObject("data").getString("cat_id");
                        cat_name = response.getJSONObject("data").getString("cat_name");
                        final_approval = response.getJSONObject("data").getString("final_approval");
                        house_no = response.getJSONObject("data").getString("house_no");
                        bank_status = response.getJSONObject("data").getString("bank_status");

                        if (!from_wallet) {

                            showData();
                        } else {
                            if (final_approval.equalsIgnoreCase("1")) {
                                {

                                    scrollView.setVisibility(View.GONE);
                                    findViewById(R.id.btnSubmit).setVisibility(View.GONE);
                                    findViewById(R.id.laymsg).setVisibility(View.VISIBLE);
                                    findViewById(R.id.help_chat_linear).setVisibility(View.VISIBLE);
                                    findViewById(R.id.youtube_card).setVisibility(View.VISIBLE);
                                }
                            } else {
                                showData();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //  ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }


    public void updateProfile() {
        Log.e("PLACE_API", "update profile called");
        Log.e("PLACE_API", "update api params" + paramsUpdate.toString());


        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_ARTIST_API, paramsUpdate, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

               // Log.e("BASICINFI_ABC", response.toString());
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Log.e("PLACE_API", "update api response;-- " + response.toString());

                        //   ProjectUtils.showToast(mContext, msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        userDTO.setIs_profile(1);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);


                        updateprofile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("BASICINFI_ABC", e.toString());

                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {


        mylocation = location;
        // Toast.makeText(mContext, "1", Toast.LENGTH_LONG).show();

        if (mylocation != null) {
            //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

            live_latitude = mylocation.getLatitude();
            Log.e("lat", "" + live_latitude.toString());
            live_longitude = mylocation.getLongitude();
            Log.e("lang", "" + live_longitude.toString());

          /*  Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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



            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
        }
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
    public void onConnected(@Nullable Bundle bundle) {
        //checkPermissions();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void updateprofile() {

        Log.e("dhaval tag ttt", " updateprofile 2 called");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);


        RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);

        MultipartBody.Part fileToSendfourteen = null;

        if (!paththree.trim().isEmpty()) {
            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
        }

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.Add_PERSONAL_DOC(fileToSendone, fileToSendtwo, fileToSendfourteen, userid);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        Log.e("WEBBYKNIGHT", "" + s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {


                            update_status();


                        } else if (sstatus == 3) {
                            Intent intent1 = new Intent(BasicInfoActivity.this, CheckSignInActivity.class);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(BasicInfoActivity.this, message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BasicInfoActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {

                        Log.e("DOCUMENT_ERROR", "" + response.errorBody().string());
                        Toast.makeText(BasicInfoActivity.this, "Try again. Server is not responding" + "999",
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
                progressDialog.dismiss();

                Log.e("failure", "" + t.toString());


                update_status();


                /*Toast.makeText(BasicInfoActivity.this, "Server Did Not Responding and Try again" + t,
                        LENGTH_LONG).show();*/
            }
        });

    }

    public void uploadImages() {

        Log.e("dhaval tag ttt", " updateprofile 2 called");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);


        RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);

        MultipartBody.Part fileToSendfourteen = null;

        if (!paththree.trim().isEmpty()) {
            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
        }

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.Add_PERSONAL_DOC22(fileToSendone, fileToSendtwo, fileToSendfourteen, userid);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        Log.e("WEBBYKNIGHT", "" + s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {

                            if (bank_status.equalsIgnoreCase("0")){

                                startActivity(new Intent(BasicInfoActivity.this,BankDocument.class));
                                finish();
                            }else {

                                onBackPressed();
                                finish();
                            }


                        } else if (sstatus == 3) {
                            Intent intent1 = new Intent(BasicInfoActivity.this, CheckSignInActivity.class);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(BasicInfoActivity.this, message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BasicInfoActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {

                        Log.e("DOCUMENT_ERROR", "" + response.errorBody().string());
                        Toast.makeText(BasicInfoActivity.this, "Try again. Server is not responding" + "999",
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
                progressDialog.dismiss();

                Log.e("failure", "" + t.toString());

                /*Toast.makeText(BasicInfoActivity.this, "Server Did Not Responding and Try again" + t,
                        LENGTH_LONG).show();*/
            }
        });

    }

    public void submitCat(String category_idNew) {
        if (category_idNew.equals("-1")) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(BasicInfoActivity.this)) {
                paramsCat.put("artist_id", userDTO.getUser_id());
                paramsCat.put("cat_id", category_idNew);
                uploadCategory();
            } else {
                ProjectUtils.showToast(BasicInfoActivity.this, getResources().getString(R.string.internet_concation));
            }
        }
    }

    public void uploadCategory() {


        ProjectUtils.showProgressDialog(BasicInfoActivity.this, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_CATEGORY_ID, paramsCat, BasicInfoActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        Log.e("basicinfostep1", "" + response.toString());
                        //  ProjectUtils.showToast(DocumentUploadTwoActivity.this, msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //  ProjectUtils.showToast(DocumentUploadTwoActivity.this, msg);
                }


            }
        });
    }

    public void update_status() {
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_APPROVAL, parms, BasicInfoActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                //binding.swipeRefreshLayout.setRefreshing(false);
                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                if (flag) {

                    try {
                        scrollView.setVisibility(View.GONE);
                        findViewById(R.id.btnSubmit).setVisibility(View.GONE);
                        findViewById(R.id.laymsg).setVisibility(View.VISIBLE);
                        findViewById(R.id.help_chat_linear).setVisibility(View.VISIBLE);
                        findViewById(R.id.youtube_card).setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //  ProjectUtils.showToast(DocumentUploadTwoActivity.this, msg);
                }
                progressDialog.dismiss();

            }
        });


    }


}