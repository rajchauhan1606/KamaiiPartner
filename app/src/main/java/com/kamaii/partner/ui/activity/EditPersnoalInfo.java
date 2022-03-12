package com.kamaii.partner.ui.activity;

import android.Manifest;
import android.app.Activity;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.interfacess.OnAddressSelected;
import com.kamaii.partner.ui.adapter.AddressBottomSheetAdapter;
import com.kamaii.partner.ui.models.TypeAddressModel;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.Glob;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.utils.CustomButton;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.MainFragment;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EditPersnoalInfo extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.OnConnectionFailedListener, OnAddressSelected, GoogleApiClient.ConnectionCallbacks {

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG = EditPersnoalInfo.class.getSimpleName();
    private Context mContext;
    private CustomEditText esociety, etCategoryD, etNameD, etBioD, etAboutD, etCityD, etCountryD, etLocationD, etRateD, etBankName, etBenificiaryName, etacc_no, etifsc_code;
    private CustomTextViewBold tvText;
    private CustomButton btnSubmit;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();

    private LinearLayout llBack;
    private CustomTextView bioLength, aboutLength;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private ArtistDetailsDTO artistDetailsDTO;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1000;
    private double lats = 0;
    private double longs = 0;
    private HashMap<String, String> paramsUpdate = new HashMap<>();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private ImageView ivBanner;
    BottomSheet.Builder builder;
    private HashMap<String, String> parmsshipping = new HashMap<>();
    String final_user_address = "";
    String landmark_name = "";
    private String address_id = "";

    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;
    private HashMap<String, File> paramsFile = new HashMap<>();
    BottomSheetDialog addressBottomSheet;
    CustomTextView no_rider_found_txt, change_address_btn;
    CustomTextViewBold address_submit;
    CustomEditText location_etx, house_no_etx, society_name_etx;
    String house_no_str = "", society_name_str = "", street_address_str = "";
    LinearLayout customAddress;
    LinearLayout address_lay_dest_location;
    RelativeLayout address_relative;
    int address_adapter_counter = 0;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    HashMap<String, String> getAddressHashmap = new HashMap<>();
    ArrayList<TypeAddressModel> typeAddressList = new ArrayList<>();
    String address_radio_txt = "Home";
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    RadioGroup add_addressradioGroup;

    boolean fromshippingdialog = false;
    String get_address = "";
    HashMap<String, String> deleteAddressHashmap = new HashMap<>();
    boolean map_flag = false;
    private GoogleApiClient googleApiClient;
    Location mylocation;

    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    boolean live_address_str = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_persnoal_info);
        mContext = EditPersnoalInfo.this;
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        Places.initialize(getApplicationContext(), API_KEY);

        //   Places.initialize(EditPersnoalInfo.this, getResources().getString(R.string.api_key));

        progressDialog = new ProgressDialog(EditPersnoalInfo.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        setUpGClient();
        if (getIntent().hasExtra(Consts.CATEGORY_list)) {
            categoryDTOS = (ArrayList<CategoryDTO>) getIntent().getSerializableExtra(Consts.CATEGORY_list);
            artistDetailsDTO = (ArtistDetailsDTO) getIntent().getSerializableExtra(Consts.ARTIST_DTO);
        }
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        if (fromshippingdialog) {
            get_address = getIntent().getStringExtra("address");

        }
        getArtist();
        setUiAction();
        getAddress();


    }

    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        Log.e("BANNER", "" + response.toString());
                        progressDialog.dismiss();
                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);

                        Log.e("updateProfileSelf", "artist_banner" + artistDetailsDTO.getArtist_banner());
                        //    Glide.with(getContext()).load(artistDetailsDTO.getArtist_banner()).into(binding.artistBgImg);

                        if (!artistDetailsDTO.getLocation().equalsIgnoreCase("")) {
                            etLocationD.setText(artistDetailsDTO.getLocation());
                        } else {
                            etLocationD.setText("Select Your Location");
                        }
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(EditPersnoalInfo.this)
                .enableAutoManage(EditPersnoalInfo.this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void setUiAction() {
        etCategoryD = (CustomEditText) findViewById(R.id.etCategoryD);
        etNameD = (CustomEditText) findViewById(R.id.etNameD);
        etBioD = (CustomEditText) findViewById(R.id.etBioD);
        etAboutD = (CustomEditText) findViewById(R.id.etAboutD);
        etCityD = (CustomEditText) findViewById(R.id.etCityD);
        etCountryD = (CustomEditText) findViewById(R.id.etCountryD);
        etLocationD = (CustomEditText) findViewById(R.id.etLocationD);
        etRateD = (CustomEditText) findViewById(R.id.etRateD);
        etBankName = (CustomEditText) findViewById(R.id.etBankName);
        etBenificiaryName = (CustomEditText) findViewById(R.id.etBenificiaryName);
        etacc_no = (CustomEditText) findViewById(R.id.etacc_no);
        esociety = (CustomEditText) findViewById(R.id.esociety);
        etifsc_code = (CustomEditText) findViewById(R.id.etifsc_code);

        addressBottomSheet = new BottomSheetDialog(this);
        addressBottomSheet.setContentView(R.layout.activity_address_bottom_dialog);
        change_address_btn = addressBottomSheet.findViewById(R.id.change_address_btn);
        location_etx = addressBottomSheet.findViewById(R.id.location_etx);
        house_no_etx = addressBottomSheet.findViewById(R.id.house_no_etx);
        society_name_etx = addressBottomSheet.findViewById(R.id.society_name_etx);
        customAddress = addressBottomSheet.findViewById(R.id.customAddress);
        add_addressradioGroup = addressBottomSheet.findViewById(R.id.add_addressradioGroup);
        address_submit = addressBottomSheet.findViewById(R.id.address_submit);
        address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        address_lay_dest_location = addressBottomSheet.findViewById(R.id.address_lay_dest_location);


        tvText = (CustomTextViewBold) findViewById(R.id.tvText);
        btnSubmit = (CustomButton) findViewById(R.id.btnSubmit);
        llBack = (LinearLayout) findViewById(R.id.llBack);
        ivBanner = (ImageView) findViewById(R.id.ivBanner);
        bioLength = (CustomTextView) findViewById(R.id.bioLength);
        aboutLength = (CustomTextView) findViewById(R.id.aboutLength);

        etCategoryD.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
        etLocationD.setOnClickListener(this);
        etLocationD.setInputType(InputType.TYPE_NULL);

        llBack.setOnClickListener(this);
        ivBanner.setOnClickListener(this);



        etBioD.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                bioLength.setText(String.valueOf(s.length()) + "/40");

            }
        });
        etAboutD.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                aboutLength.setText(String.valueOf(s.length()) + "/200");

            }
        });

        builder = new BottomSheet.Builder(EditPersnoalInfo.this).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }

                                    prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(EditPersnoalInfo.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_pic)), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });

        spinnerDialogCate = new SpinnerDialog((Activity) mContext, categoryDTOS, getResources().getString(R.string.select_cate));// With 	Animation
        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                etCategoryD.setText(item);

                tvText.setText(getResources().getString(R.string.commis_msg) + categoryDTOS.get(position).getCurrency_type() + categoryDTOS.get(position).getPrice());


            }
        });

        if (artistDetailsDTO != null) {
            showData();
        }

        etRateD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        });


    }


    public void deleteaddress(String address_id) {

        deleteAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        deleteAddressHashmap.put(Consts.ADDRESS_ID, address_id);
        new HttpsRequest(Consts.DELETE_ADDRESS, deleteAddressHashmap, this).stringPost("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    if (msg.equals("0")) {

                        getAddress();

                        finish();
                        startActivity(getIntent());
                    }
                } else {

                }
            }
        });
    }


    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);
        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    public void showData() {
        for (int j = 0; j < categoryDTOS.size(); j++) {
            if (categoryDTOS.get(j).getId().equalsIgnoreCase(artistDetailsDTO.getCategory_id())) {
                categoryDTOS.get(j).setSelected(true);
                etCategoryD.setText(categoryDTOS.get(j).getCat_name());
                tvText.setText(getResources().getString(R.string.commis_msg) + categoryDTOS.get(j).getCurrency_type() + categoryDTOS.get(j).getPrice());


            }
        }

        spinnerDialogCate = new SpinnerDialog((Activity) mContext, categoryDTOS, getResources().getString(R.string.select_cate));// With 	Animation
        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                etCategoryD.setText(item);
                tvText.setText(getResources().getString(R.string.commis_msg) + categoryDTOS.get(position).getCurrency_type() + categoryDTOS.get(position).getPrice());


            }
        });
        etCategoryD.setText(artistDetailsDTO.getCategory_name());
        etNameD.setText(artistDetailsDTO.getName());
        etBioD.setText(artistDetailsDTO.getBio());
        etAboutD.setText(artistDetailsDTO.getAbout_us());
        etCityD.setText(artistDetailsDTO.getCity());
        etCountryD.setText(artistDetailsDTO.getCountry());
        etLocationD.setText(artistDetailsDTO.getLocation());
        etRateD.setText(artistDetailsDTO.getPrice());
        etBankName.setText(artistDetailsDTO.getBank_name());
        etBenificiaryName.setText(artistDetailsDTO.getBenifeciry_name());
        etacc_no.setText(artistDetailsDTO.getAccount_no());
        etifsc_code.setText(artistDetailsDTO.getIfsc_code());


        Glide.with(mContext).
                load(artistDetailsDTO.getBanner_image())
                .placeholder(R.drawable.banner_img)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivBanner);


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

            case R.id.etLocationD:
                showAddressDialog();
                break;
            case R.id.btnSubmit:
                if (NetworkManager.isConnectToInternet(mContext)) {
                    submitPersonalProfile();
                } else {
                    ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.ivBanner:
                builder.show();
                break;
            case R.id.llBack:
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
                break;
        }
    }

    private void showAddressDialog() {


        //RelativeLayout address_relative = addressBottomSheet.findViewById(R.id.address_relative);

        ImageView back = addressBottomSheet.findViewById(R.id.address_dialog_close_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBottomSheet.dismiss();
            }
        });

        Typeface font2 = Typeface.createFromAsset(this.getAssets(), "Poppins-Regular.otf");


        location_etx.setTypeface(font2);
        house_no_etx.setTypeface(font2);
        society_name_etx.setTypeface(font2);

        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());

        // LinearLayout lll = bottomSheetDialog.findViewById(R.id.lll);
        RecyclerView recyclerView = addressBottomSheet.findViewById(R.id.address_recycler);

        if (typeAddressList.size() == 0) {
            address_relative.setVisibility(View.GONE);
        }

        AddressBottomSheetAdapter dialogadapter = new AddressBottomSheetAdapter(this, typeAddressList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dialogadapter);


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
                    Toast.makeText(EditPersnoalInfo.this, "Please enter House no. / Flat no. / Floor / Building", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addAddressHashmap.put("house_no", house_no_str);
                    addAddressHashmap.put("society_name", society_name_str);
                    addAddress();
                    //binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

                    if (house_no_str.equalsIgnoreCase("") || society_name_str.equalsIgnoreCase("")) {

                        if (house_no_str.equalsIgnoreCase("") && society_name_str.equalsIgnoreCase("")) {
                            etLocationD.setText(street_address_str);
                        } else if (house_no_str.equalsIgnoreCase("")) {
                            etLocationD.setText(society_name_str + ", " + street_address_str);
                        } else if (society_name_str.equalsIgnoreCase("")) {
                            etLocationD.setText(house_no_str + ", " + street_address_str);
                        }
                    } else {
                        etLocationD.setText(house_no_str + ", " + society_name_str + ", " + street_address_str);
                    }
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        com.kamaii.partner.Glob.BUBBLE_VALUE = "0";
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public void addAddress() {
        addAddressHashmap.put("addresstype", address_radio_txt);
        Log.e("addAddressHashmap", "" + addAddressHashmap.toString());
        new HttpsRequest(Consts.ADD_ADDRESS, addAddressHashmap, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    // Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    getAddress();
                    Log.e("ADD_ADDRESS_API", "" + response.toString());
                } else {

                }
            }
        });
    }

    public void getAddress() {

        getAddressHashmap.put("cust_id", userDTO.getUser_id());
        parmsshipping.put(Consts.ARTIST_ID, userDTO.getUser_id());

        new HttpsRequest(Consts.GET_ADDRESS, getAddressHashmap, this).stringPost("GET_ADDRESS", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {

                    Type getaddDTO = new TypeToken<List<TypeAddressModel>>() {
                    }.getType();
                    try {
                        typeAddressList = (ArrayList<TypeAddressModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getaddDTO);

                        if (typeAddressList.size() != 0) {

                            if (address_relative.getVisibility() == View.GONE) {
                                address_relative.setVisibility(View.VISIBLE);
                            }
                            address_id = typeAddressList.get(typeAddressList.size() - 1).getAddress_id();
                        } else {

                            address_lay_dest_location.setVisibility(View.GONE);
                            customAddress.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    address_lay_dest_location.setVisibility(View.GONE);
                    customAddress.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {


            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                final_user_address = place.getAddress();
                landmark_name = place.getName();


                lats = place.getLatLng().latitude;
                longs = place.getLatLng().longitude;

                addAddressHashmap.put("street_address", landmark_name + final_user_address);
                addAddressHashmap.put(Consts.LATITUDE, String.valueOf(lats));
                addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(longs));

                location_etx.setText(landmark_name + ", " + place.getAddress());
                customAddress.setVisibility(View.VISIBLE);
                address_lay_dest_location.setVisibility(View.GONE);
                address_relative.setVisibility(View.GONE);
                // etLocationD.setText(selectedCarmenFeature.placeName());
            }


        }

    }

    private void findPlace() {

        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(EditPersnoalInfo.this);

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);
    }


    public void submitPersonalProfile() {
        if (!validation(etNameD, getResources().getString(R.string.val_name))) {
            return;
        } else if (!validation(etCityD, getResources().getString(R.string.val_city))) {
            return;
        } else if (!validation(etLocationD, getResources().getString(R.string.val_location))) {
            return;
        } else if (etLocationD.getText().toString().equalsIgnoreCase("Select Your Location")) {
            Toast.makeText(mContext, getResources().getString(R.string.val_location), Toast.LENGTH_SHORT).show();
        }


        else {
            if (NetworkManager.isConnectToInternet(mContext)) {

                prefrence.setValue(Consts.EDIT_ADDRESS, ProjectUtils.getEditTextValue(etLocationD));
                paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                paramsUpdate.put(Consts.NAME, ProjectUtils.getEditTextValue(etNameD));
                paramsUpdate.put(Consts.BIO, "");
                paramsUpdate.put(Consts.ABOUT_US, ProjectUtils.getEditTextValue(etAboutD));
                paramsUpdate.put(Consts.CITY, ProjectUtils.getEditTextValue(etCityD));
                paramsUpdate.put(Consts.COUNTRY, "");
                paramsUpdate.put(Consts.LOCATION, ProjectUtils.getEditTextValue(etLocationD));
                paramsUpdate.put(Consts.BANK_NAME, "");
                paramsUpdate.put(Consts.HOUSE_NO, esociety.getText().toString());
                paramsUpdate.put(Consts.ACCOUNT_HOLDER_NAME, "");
                paramsUpdate.put(Consts.ACCOUNT_NO, "");
                paramsUpdate.put(Consts.IFSC_CODE, "");
                paramsUpdate.put("address_id", address_id);
                paramsFile.put(Consts.BANNER_IMAGE, file);
                if (lats != 0)
                    paramsUpdate.put(Consts.LATITUDE, String.valueOf(lats));

                if (longs != 0)
                    paramsUpdate.put(Consts.LONGITUDE, String.valueOf(longs));

                Log.e("PARTNER_ADDRESS", " edit_PARAM " + paramsUpdate.toString());
                updateProfile();
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

    public void updateProfile() {

        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_ARTIST_API, paramsUpdate, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    Log.e("PARTNER_ADDRESS", " edit_response " + response.toString());

                    try {
                        ProjectUtils.showToast(mContext, msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        userDTO.setIs_profile(1);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        finish();
                        overridePendingTransition(R.anim.stay, R.anim.slide_down);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }


    @Override
    public void setAddress(int position, String id, String house_no, String street_add, String soc_name) {

        //binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));
        address_id = id;

        if (house_no.equalsIgnoreCase("") || soc_name.equalsIgnoreCase("")) {

            if (house_no.equalsIgnoreCase("") && soc_name.equalsIgnoreCase("")) {
                etLocationD.setText(street_add);
            } else if (house_no.equalsIgnoreCase("")) {
                etLocationD.setText(soc_name + ", " + street_add);
            } else if (soc_name.equalsIgnoreCase("")) {
                etLocationD.setText(house_no + ", " + street_add);
            }
        } else {
            etLocationD.setText(house_no + ", " + soc_name + ", " + street_add);
        }

        //etLocationD.setText(house_no + ", " + soc_name + ", " + street_add);
        addressBottomSheet.dismiss();
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

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(EditPersnoalInfo.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(EditPersnoalInfo.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        } else {
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 109) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                findPlace();
            } else {
                //   Toast.makeText(mContext, "Location Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(EditPersnoalInfo.this,
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
                            .requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
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
                                            .checkSelfPermission(EditPersnoalInfo.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);


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

                    if (live_address_str) {

                        Log.e("Live_ADDRESS", "AAAAAAA:---- " + obj.getAddressLine(0));

                        live_address_str = false;
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
}
