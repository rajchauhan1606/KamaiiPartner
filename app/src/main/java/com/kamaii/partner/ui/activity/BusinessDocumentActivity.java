package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.DocumentDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.fragment.DocumentUploadFragment;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ExpandableHeightGridView;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class BusinessDocumentActivity extends AppCompatActivity {
    Button btn_submit;
    private String TAG = DocumentUploadFragment.class.getSimpleName();
    private View view;
    ImageView layrcfront, layrcback, layinsuarnce, laycarpermit, laypoliceverification, laycarphoto, layownerphoto, layselfie, laydrivingfront, laydrivingback;
    // ImageView layrcfront;
    private Dialog dialog;
    EditText etcarno;
    LinearLayout laycamera, laygalley;
    CustomTextViewBold tvCancel;
    String pathone = "", pathtwo = "", paththree = "", pathfour = "", pathfive = "", pathsix = "", pathseven = "", patheight = "", pathnine = "", pathten = "", pathelven = "", pathtwelve = "", paththirteen = "", carmodelnamestring = "", carcategorystring = "", citystring = "", pathfourteen = "",
            bookingcarstring = "", carno = "", rno = "";
    String flag = "";

    String[] city = {"Ahmedabad", "Amreli", "Gandhinagar", "Vadodara", "Rajkot", "Surat", "Bhavanagar", "Lunawada", "Godhra", "Anand", "Banas", "Kantha", "Bharuch", "Dohad", "Jamnagar"
            , "Junagadh", "Kachchh", "Kheda", "Mahesana", "Narmada", "Navsari", "Panch", "Mahals", "Patan", "Porbandar", "Sabar", "Surendranagar", "Tapi", "The Dangs", "Valsad", "Modasa"
            , "Mumbai", "Delhi", "Pune", "Other City"};
    String[] carcategory = {"Select car category", "Hatchback Car", "Sedan Car", "SUV Car", "Luxury Car"};
    String[] carmodelname = {"Select car model", "Hyundai Xcent", "Hyundai Accent", "Hyundai i10", "Maruti Swift Dzire", "Maruti Alto", "Tata Indica", "Tata Zest",
            "Toyota Innova Crysta", "Nissan Sunny",
            "Chevrolet Spark", "Honda WRV", "Honda City", "Mahindra TUV300", "Mahindra Bolero", "Hyundai EON", "Maruti Ertiga", "Maruti WagonR", "Maruti Eeco", "Maruti Celerio",
            "Tata Itos", "Chevrolet Tavera", "Toyota Etios", "Nissan Datsun Go", "Honda Amaze", "Honda BRV", "Mahindra Xylo", "Mahindra Marazzo", " Ford Figo Aspire", "Rickshaw Bajaj",
            "Bike", "Others"};
    List<String> CarModelList;
    Spinner spicity, spicarmodelname, spicarmcategory;
    private HashMap<String, String> params;
    CheckBox checkone, checktwo, checkthree, checkfour;
    private HashMap<String, File> paramsFile;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    ProgressDialog progressDialog;
    File fileone = null, filetwo = null, filethree = null, filefour = null, filefive = null, filesix = null, fileseven = null, fileeight = null, filenine = null, fileten = null, fileeleven = null, filetwelve = null, filethirteen = null, filefourteen = null;
    private Bundle bundle;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<DocumentDTO> documentDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> carparms = new HashMap<>();
    private BaseActivity baseActivity;
    Uri uri;
    public static LinearLayout layall;
    CustomTextViewBold laymsg;
    String edit = "0";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    CustomTextViewBold tvcat;
    String category_id = "";
    LinearLayout layallrcbook, layalldlicence, laycarnformation, layselectcategory;

    LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_document);

        prefrence = SharedPrefrence.getInstance(BusinessDocumentActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        carparms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        documentDTOArrayList = new ArrayList<>();
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog = new ProgressDialog(BusinessDocumentActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        CarModelList = new ArrayList<>();
        params = new HashMap<>();
        paramsFile = new HashMap<>();

        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        layrcfront = findViewById(R.id.layrcfront);
        layrcback = findViewById(R.id.layrcback);
        layinsuarnce = findViewById(R.id.layinsuarnce);
        laycarpermit = findViewById(R.id.laycarpermit);
        laypoliceverification = findViewById(R.id.laypoliceverification);
        layselfie = findViewById(R.id.layselfie);
        laycarphoto = findViewById(R.id.laycarphoto);
        layownerphoto = findViewById(R.id.layownerphoto);
        btn_submit = findViewById(R.id.btn_submit);
        checkone = findViewById(R.id.checkone);
        checktwo = findViewById(R.id.checktwo);
        checkthree = findViewById(R.id.checkthree);
        checkfour = findViewById(R.id.checkfour);
        laymsg = findViewById(R.id.laymsg);
        layall = findViewById(R.id.layall);
        laydrivingfront = findViewById(R.id.laydrivingfront);
        laydrivingback = findViewById(R.id.laydrivingback);
        tvcat = findViewById(R.id.tvcat);
        layallrcbook = findViewById(R.id.layallrcbook);
        layalldlicence = findViewById(R.id.layalldlicence);
        laycarnformation = findViewById(R.id.laycarnformation);
        layselectcategory = findViewById(R.id.layselectcategory);
        etcarno = findViewById(R.id.etcarno);


        spicarmodelname = findViewById(R.id.spicarmodelname);
        spicarmcategory = findViewById(R.id.spicarmcategory);

        llBack = findViewById(R.id.llBack);
        
        etcarno.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if ((etcarno.getText().length() + 1 == 3 || etcarno.getText().length() + 1 == 6 || etcarno.getText().length() + 1 == 9)) {
                    if (before - count < 0) {
                        etcarno.setText(etcarno.getText() + " ");
                        etcarno.setSelection(etcarno.getText().length());
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
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BusinessDocumentActivity.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });
        getCarModelname();
        ArrayAdapter aa = new ArrayAdapter(BusinessDocumentActivity.this, android.R.layout.simple_spinner_item, city);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spicarmodelname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carmodelnamestring = CarModelList.get(position);
                // Toast.makeText(BusinessDocumentActivity.this,carmodelname[position] , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter aaggghhh = new ArrayAdapter(BusinessDocumentActivity.this, android.R.layout.simple_spinner_item, carcategory);
        aaggghhh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spicarmcategory.setAdapter(aaggghhh);

        spicarmcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carcategorystring = carcategory[position];

                // Toast.makeText(BusinessDocumentActivity.this,carcategory[position] , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog = new Dialog(BusinessDocumentActivity.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dailog_camera_gallery);
        dialog.setCancelable(false);
        laycamera = dialog.findViewById(R.id.laycamera);
        laygalley = dialog.findViewById(R.id.laygalley);
        tvCancel = dialog.findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        laycamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        laygalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        laydrivingfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "12";
                        // dialog.show();

                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "12";

                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }


            }
        });

        laydrivingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "13";
                        // dialog.show();

                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "13";

                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }


            }
        });

        layrcfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "3";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "3";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });


        layrcback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "4";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "4";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });

        layinsuarnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "5";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "5";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        laycarpermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "6";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "6";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });
        laypoliceverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "7";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "7";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });

        laycarphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "8";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "8";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        layownerphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "9";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "9";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });


        layselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "11";
                        ImagePicker.Companion.with(BusinessDocumentActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "11";
                    ImagePicker.Companion.with(BusinessDocumentActivity.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {
                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {

                        if (edit.equalsIgnoreCase("1")) {
                           // Toast.makeText(baseActivity, "edit if", Toast.LENGTH_SHORT).show();
                            btn_submit.setText("Submit");
                            edit = "0";
                        } else {
                          //  Toast.makeText(baseActivity, "edit else", Toast.LENGTH_SHORT).show();

                            Click();

                        }


                    } else {
                       // Toast.makeText(BusinessDocumentActivity.this, "Already approved document. Good Luck!", LENGTH_LONG).show();
                    }
                } else {
                    Click();
                }

            }
        });
    }


    private int loop(String val, String[] array) {
        return new ArrayList<String>(Arrays.asList(array)).indexOf(val);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 23) {


                Uri imageUri = data.getData();

                if (flag.equalsIgnoreCase("3")) {
                    layrcfront.setImageURI(imageUri);
                    paththree = imageUri.getPath();
                    // paththree = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("4")) {
                    layrcback.setImageURI(imageUri);
                    pathfour = imageUri.getPath();
                    // pathfour = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("5")) {
                    layinsuarnce.setImageURI(imageUri);
                    pathfive = imageUri.getPath();
                    //  pathfive = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("6")) {
                    laycarpermit.setImageURI(imageUri);
                    pathsix = imageUri.getPath();
                    //  pathsix = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("7")) {
                    laypoliceverification.setImageURI(imageUri);
                    pathseven = imageUri.getPath();
                    //   pathseven = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("8")) {
                    laycarphoto.setImageURI(imageUri);
                    patheight = imageUri.getPath();
                    // patheight = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("9")) {
                    layownerphoto.setImageURI(imageUri);
                    pathnine = imageUri.getPath();
                    //   pathnine = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("11")) {
                    layselfie.setImageURI(imageUri);
                    pathelven = imageUri.getPath();
                    // pathelven = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("12")) {
                    laydrivingfront.setImageURI(imageUri);
                    pathtwelve = imageUri.getPath();
                    // pathelven = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                } else if (flag.equalsIgnoreCase("13")) {
                    laydrivingback.setImageURI(imageUri);
                    paththirteen = imageUri.getPath();
                    // pathelven = FileUtility.getPath(BusinessDocumentActivity.this,imageUri);
                }

            }
        } else {

        }
    }

    public void getCarModelname() {

        new HttpsRequest(Consts.GET_CAR_MODEL, carparms, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    Log.e("PARAMS2", "" + response.toString());

                    JSONObject jsonObject = response;
                    try {
                        CarModelList.clear();
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            CarModelList.add(jsonObject1.getString("cat_name"));
                        }
                        ArrayAdapter aaggg = new ArrayAdapter(BusinessDocumentActivity.this, android.R.layout.simple_spinner_item, CarModelList);
                        aaggg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spicarmodelname.setAdapter(aaggg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                  //  ProjectUtils.showToast(BusinessDocumentActivity.this, msg);
//                    Log.e("PARAMS3", "" + response.toString());

                }
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void updateprofile() {
        MultipartBody.Part fileToSendfive = null;
        MultipartBody.Part fileToSendsix = null;
        MultipartBody.Part fileToSendseven = null;
        MultipartBody.Part fileToSendele = null;
        MultipartBody.Part fileToSendfourteen = null;
        MultipartBody.Part fileToSendthree = null;
        MultipartBody.Part fileToSendfour = null;
        MultipartBody.Part fileToSendtwelve = null;
        MultipartBody.Part fileToSendthirteen = null;
        MultipartBody.Part fileToSendeight = null;
        MultipartBody.Part fileToSendnine = null;

        if (!patheight.trim().isEmpty()) {
            RequestBody requestBodyeight = RequestBody.create(MediaType.parse("multipart/form-data"), fileeight);
            fileToSendeight = MultipartBody.Part.createFormData("selfi_with_car_photo", fileeight.getName(), requestBodyeight);

        }
        if (!pathnine.trim().isEmpty()) {
            RequestBody requestBodynine = RequestBody.create(MediaType.parse("multipart/form-data"), filenine);
            fileToSendnine = MultipartBody.Part.createFormData("owner_photo", filenine.getName(), requestBodynine);
        }

        if (!paththree.trim().isEmpty()) {
            RequestBody requestBodyth = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendthree = MultipartBody.Part.createFormData("rc_book", filethree.getName(), requestBodyth);

        }

        if (!pathfour.trim().isEmpty()) {
            RequestBody requestBodyfour = RequestBody.create(MediaType.parse("multipart/form-data"), filefour);
            fileToSendfour = MultipartBody.Part.createFormData("rc_book_back", filefour.getName(), requestBodyfour);

        }

        if (!pathfive.trim().isEmpty()) {
            RequestBody requestBodyfive = RequestBody.create(MediaType.parse("multipart/form-data"), filefive);
            fileToSendfive = MultipartBody.Part.createFormData("car_insurance", filefive.getName(), requestBodyfive);
        }


        if (!pathsix.trim().isEmpty()) {
            RequestBody requestBodysix = RequestBody.create(MediaType.parse("multipart/form-data"), filesix);
            fileToSendsix = MultipartBody.Part.createFormData("car_permit", filesix.getName(), requestBodysix);
        }


        if (!pathseven.trim().isEmpty()) {
            RequestBody requestBodyse = RequestBody.create(MediaType.parse("multipart/form-data"), fileseven);
            fileToSendseven = MultipartBody.Part.createFormData("police_verification", fileseven.getName(), requestBodyse);

        }

        if (!pathelven.trim().isEmpty()) {
            RequestBody requestBodyele = RequestBody.create(MediaType.parse("multipart/form-data"), fileeleven);
            fileToSendele = MultipartBody.Part.createFormData("selfie_photo", fileeleven.getName(), requestBodyele);

        }

        if (!pathtwelve.trim().isEmpty()) {
            RequestBody requestBodytwelve = RequestBody.create(MediaType.parse("multipart/form-data"), filetwelve);
            fileToSendtwelve = MultipartBody.Part.createFormData("driving_licence_front", filetwelve.getName(), requestBodytwelve);

        }

        if (!paththirteen.trim().isEmpty()) {
            RequestBody requestBodythirteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethirteen);
            fileToSendthirteen = MultipartBody.Part.createFormData("driving_licence_back", filethirteen.getName(), requestBodythirteen);


        }
        Log.e("carmodelName",""+carmodelnamestring);
        RequestBody carmodel = RequestBody.create(MediaType.parse("text/plain"), carmodelnamestring);
        //RequestBody city = RequestBody.create(MediaType.parse("text/plain"), citystring);
        Log.e("finalbookingstr", "" + bookingcarstring);
        RequestBody bokingcar = RequestBody.create(MediaType.parse("text/plain"), bookingcarstring);
        RequestBody carcategory = RequestBody.create(MediaType.parse("text/plain"), carcategorystring);
        RequestBody carnooo = RequestBody.create(MediaType.parse("text/plain"), etcarno.getText().toString());
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());
        // RequestBody cat_idreq = RequestBody.create(MediaType.parse("text/plain"), category_id);

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.upload_business_data(carmodel, bokingcar, carcategory, carnooo, fileToSendthree, fileToSendfour, fileToSendfive, fileToSendsix, fileToSendseven, fileToSendeight, fileToSendnine, userid, fileToSendele, fileToSendtwelve, fileToSendthirteen);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                //  Log.e("RES", response.message());

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        if (sstatus == 1) {
                           // Toast.makeText(BusinessDocumentActivity.this, message, LENGTH_LONG).show();
                            //getArtist();
                            Intent intent1 = new Intent(BusinessDocumentActivity.this, DocumentUploadTwoActivity.class);
                            startActivity(intent1);
                            finish();
                            overridePendingTransition(R.anim.stay, R.anim.slide_down);
                        } else if (sstatus == 3) {
                           // Toast.makeText(BusinessDocumentActivity.this, message, LENGTH_LONG).show();
                        } else {
                          //  Toast.makeText(BusinessDocumentActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(BusinessDocumentActivity.this, "Try again. Server is not responding",
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
                Toast.makeText(BusinessDocumentActivity.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();
            }
        });

    }


    public void Click() {
        if (checkone.isChecked()) {
            bookingcarstring = bookingcarstring + checkone.getText().toString();

        }
        if (checktwo.isChecked()) {
            bookingcarstring = bookingcarstring + "," + checktwo.getText().toString();
            Log.e("checkbox_test", "" + bookingcarstring);
        }
        if (checkthree.isChecked()) {
            bookingcarstring = bookingcarstring + "," + checkthree.getText().toString();
            Log.e("checkbox_test", "" + bookingcarstring);

        }
        if (checkfour.isChecked()) {
            bookingcarstring = bookingcarstring + "," + checkfour.getText().toString();
            Log.e("checkbox_test", "" + bookingcarstring);

        }
        if (paththree.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Rc Book Front Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathfour.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Rc Book Back Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathtwelve.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Driving Licence Front Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (paththirteen.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Driving Licence Back Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (carmodelnamestring.equalsIgnoreCase("Select Model Name")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Select Car Model", Toast.LENGTH_LONG).show();
            return;
        } else if (carcategorystring.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Select Car Category", Toast.LENGTH_LONG).show();
            return;
        } else if (patheight.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Selfie With Car Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathnine.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Upload Owner Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (bookingcarstring.equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Please Select Booking", Toast.LENGTH_LONG).show();
            return;
        } else if (etcarno.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(BusinessDocumentActivity.this, "Car no required", Toast.LENGTH_LONG).show();
            return;
        } else {

            //  bookingcarstring = "";

            filethree = new File(paththree);
            filefour = new File(pathfour);
            filefive = new File(pathfive);
            filesix = new File(pathsix);
            fileseven = new File(pathseven);
            fileeight = new File(patheight);
            filenine = new File(pathnine);
            fileeleven = new File(pathelven);
            filetwelve = new File(pathtwelve);
            filethirteen = new File(paththirteen);

            if (NetworkManager.isConnectToInternet(BusinessDocumentActivity.this)) {
                updateprofile();
            } else {
                ProjectUtils.showToast(BusinessDocumentActivity.this, getResources().getString(R.string.internet_concation));
            }


        }
    }

}
