package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.SetOnCheckboxClick;
import com.kamaii.partner.interfacess.SetonItemListener;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.model.CommonServiceModel;
import com.kamaii.partner.model.ThirdCateModel;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.adapter.AdapterCategoryServices;
import com.kamaii.partner.ui.adapter.AdapterServices;
import com.kamaii.partner.ui.adapter.AddServiceAdapter;
import com.kamaii.partner.ui.adapter.SubCateAdapter;
import com.kamaii.partner.ui.fragment.Services;
import com.kamaii.partner.ui.models.CategoryModel;
import com.kamaii.partner.ui.models.Description;
import com.kamaii.partner.ui.models.MaxQtyEdittextModel;
import com.kamaii.partner.ui.models.RateModel;
import com.kamaii.partner.ui.models.SubCateModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ExpandableHeightGridView;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.ItemDecorationAlbumColumns;
import com.kamaii.partner.utils.MainFragment;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialog;
import com.kamaii.partner.utils.SpinnerDialogDescription;
import com.kamaii.partner.utils.SpinnerDialogFour;
import com.kamaii.partner.utils.SpinnerDialogRate;
import com.kamaii.partner.utils.SpinnerDialogSubCate;
import com.kamaii.partner.utils.SpinnerDialogThird;
import com.webknight.filemanager.Constant;
import com.webknight.filemanager.activity.ImagePickActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = Services.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rv_services, rv_services_cat, rv_services_sub_car;
    private ArrayList<ProductDTO> productDTOList;
    private AdapterServices adapterServices;
    private Bundle bundle;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout rlView;
    private CustomTextViewBold tvNotFound;
    private HashMap<String, String> paramsUpdate;
    public Dialog dialogEditProduct;
    private HashMap<String, File> paramsFile;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    LinearLayout addbtnsubmit;
    Bitmap bm;
    ImageCompression imageCompression;
    File file = null;
    Bitmap bitmap = null;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private ArrayList<CategoryModel> categoryarraylist = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayList = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListserivce = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListnew = new ArrayList<>();
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();
    ArrayList<Description> descriptionArrayList = new ArrayList<>();
    ArrayList<CommonServiceModel> fourModelArrayList = new ArrayList<>();
    ArrayList<ProductDTO> productDTOArrayListnew = new ArrayList<>();
    ArrayList<RateModel> rateArrayList = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    private SpinnerDialogSubCate spinnerDialogsubcate;
    private com.kamaii.partner.utils.SpinnerDialogThird SpinnerDialogThird;
    private SpinnerDialogFour spinnerDialogFour;
    private SpinnerDialogDescription spinnerDialogDes;
    private SpinnerDialogRate spinnerDialogRate;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> paramsucategory = new HashMap<>();
    private HashMap<String, String> paramsrate = new HashMap<>();
    private HashMap<String, String> paramsthird = new HashMap<>();
    private HashMap<String, String> paramsFour = new HashMap<>();
    private HashMap<String, String> paramsservice = new HashMap<>();
    ViewFlipper viewflipper;
    AdapterCategoryServices adapterCategoryServices;
    SubCateAdapter subCateAdapter;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsSubcatImage = new HashMap<>();
    //  private SwipeRefreshLayout swipeRefreshLayout;

    String catidmain = "";
    private BaseActivity baseActivity;
    ProgressDialog progressDialog;
    String maxQtystr = "", category_id = "", sub_category_id = "", third_id = "", vechilenumber = "", servicename = "", serviceid = "";
    boolean image_flag = false;
    String encodedBase64 = "";
    ImageView service_lock;
    String send_img_str = null;
    String send_price_str = null;
    String description_id = null;
    CustomTextViewBold select_service_name, p_des, tvcat, tvFiltersub, tvhird, tvservice;
    CustomTextView p_desdeep, etvechial_number_title, tvrate;
    CustomEditText etvechial_number, etImageD;
    TextInputEditText manual_price_etx;
    private RelativeLayout manualServiceRelative, manual_service_relative;
    LinearLayout add_service_btn;
    Button manual_photo_btn;
    ImageView manual_service_img;
    ExpandableHeightGridView service_name_recyclerview;
    AddServiceAdapter serviceAdapter;
    String Service_id_str = "";
    ArrayList<String> serviceIdList = new ArrayList<>();
    ArrayList<String> descriptionList = new ArrayList<>();
    ArrayList<String> priceList = new ArrayList<>();
    ArrayList<Integer> positionList = new ArrayList<>();
    ArrayList<String> vehiclenumList = new ArrayList<>();
    ArrayList<String> maxQtyList = new ArrayList<>();
    boolean vehicle_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);
        // binding1 = DataBindingUtil.setContentView(this, R.layout.activity_add_services);

        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        tvcat = findViewById(R.id.tvcat);
        p_desdeep = findViewById(R.id.p_desdeep);
        manual_photo_btn = findViewById(R.id.manual_photo_btn);
        manual_service_img = findViewById(R.id.manual_service_img);
        p_des = findViewById(R.id.p_des);
        tvFiltersub = findViewById(R.id.tvFiltersub);
        tvhird = findViewById(R.id.tvhird);
        tvservice = findViewById(R.id.tvservice);
        select_service_name = findViewById(R.id.select_service_name);
        etvechial_number_title = findViewById(R.id.etvechial_number_title);
        //  etvechial_number = findViewById(R.id.etvechial_number);
        etImageD = findViewById(R.id.etImageD);
        tvrate = findViewById(R.id.tvrate);
        manualServiceRelative = findViewById(R.id.manual_service_relative);
        etvechial_number_title = findViewById(R.id.etvechial_number_title);
        add_service_btn = findViewById(R.id.add_service_btn);
        manual_price_etx = findViewById(R.id.manual_price_etx);
        service_name_recyclerview = findViewById(R.id.service_name_recyclerview);
        manual_service_relative = findViewById(R.id.manual_service_relative);

        init();

    }

    private void init() {

        showUiAction(view);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        paramsservice.put(Consts.ARTIST_ID, userDTO.getUser_id());
        // getrate();
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        Typeface font2 = Typeface.createFromAsset(this.getAssets(), "Lato-Heavy.ttf");

        Typeface font = Typeface.createFromAsset(this.getAssets(), "Lato-Semibold.ttf");
        Typeface font3 = Typeface.createFromAsset(this.getAssets(), "Lato-Black.ttf");

        // binding1.addserviceTitle.setTypeface(font2);
        if (NetworkManager.isConnectToInternet(AddServicesActivity.this)) {

            getArtist();
            getCategory();
            dialogProduct();
        } else {
            ProjectUtils.showToast(this, getResources().getString(R.string.internet_concation));
        }

        setOnClickListeners();
    }


    public void setOnClickListeners() {

        findViewById(R.id.add_servicellBack).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_servicellBack:
                onBackPressed();
                break;
        }
    }


    public void addServices() {
        dialogProduct();

    }

    public void dialogProduct() {

        paramsUpdate = new HashMap<>();
        paramsFile = new HashMap<>();
        paramsucategory = new HashMap<>();
        paramsthird = new HashMap<>();
        paramsFour = new HashMap<>();


        tvcat.setText(getResources().getString(R.string.all_categories));
        tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
        tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
        tvservice.setText(getResources().getString(R.string.all_service_name));
//        etvechial_number.setText("");
        etImageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                //    Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvFiltersub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                    Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        tvhird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                    Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourModelArrayList.size() > 0) {
                    spinnerDialogFour.showSpinerDialog();
                } else {
                    Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.no_service_name_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        tvrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rateArrayList.size() > 0) {
                    spinnerDialogRate.showSpinerDialog();
                } else {
                    Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.no_rate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        add_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String cat = tvcat.getText().toString().trim();
                String subcat = tvFiltersub.getText().toString().trim();
                String third = tvhird.getText().toString().trim();


                if (NetworkManager.isConnectToInternet(AddServicesActivity.this)) {
                    if (cat.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                        Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.val_category), Toast.LENGTH_SHORT).show();
                    } else if (subcat.equalsIgnoreCase(getResources().getString(R.string.all_sub_categories))) {
                        Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.val_subcatogry), Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkarss(category_id)) {
                            vehicle_flag = true;
                            if (third.equalsIgnoreCase(getResources().getString(R.string.all_sub__level_categories))) {
                                Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.val_sublevelcatogry), Toast.LENGTH_SHORT).show();
                            }  else {
                                addProduct();
                            }
                        } else {
                            addProduct();
                        }


                    }


                } else {
                    Toast.makeText(AddServicesActivity.this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                // swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                if (flag) {
                    try {

                        artistDetailsDTO = new ArtistDetailsDTO();
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }


    public void showUiAction(View v) {

        builder = new BottomSheet.Builder(AddServicesActivity.this).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(AddServicesActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(AddServicesActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        picUri = FileProvider.getUriForFile(AddServicesActivity.this.getApplicationContext(), AddServicesActivity.this.getApplicationContext().getPackageName() + ".fileprovider", file);
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
                        if (ProjectUtils.hasPermissionInManifest(AddServicesActivity.this, PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(AddServicesActivity.this, PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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

        //  showData();

    }

    public void getCategory() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, AddServicesActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Log.e("getAllCaegory", "" + response.toString());
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                        Collections.sort(categoryDTOS, new Comparator<CategoryDTO>() {
                            @Override
                            public int compare(CategoryDTO lhs, CategoryDTO rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        progressDialog.dismiss();
                        manualServiceRelative.setVisibility(View.GONE);

                        spinnerDialogCate = new SpinnerDialog(AddServicesActivity.this, categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvcat.setText(item);
                                paramsucategory.put(Consts.CATEGORY_ID, id);
                                paramsthird.put(Consts.CATEGORY_ID, id);
                                paramsUpdate.put(Consts.CATEGORY_ID, id);
                                paramsFour.put("cat_id", id);
                                category_id = id;

                                tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
                                tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
                                tvservice.setText(getResources().getString(R.string.all_service_name));
                                // etvechial_number.setText("");
                                if (checkarss(category_id)) {

                                    vehicle_flag = true;

                                    // etvechial_number.setVisibility(View.VISIBLE);
                                    // etvechial_number_title.setVisibility(View.VISIBLE);
                                } else {

                                    vehicle_flag = false;

                                    // etvechial_number.setVisibility(View.GONE);
                                    // etvechial_number_title.setVisibility(View.GONE);
                                }
                                getSubCategory();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddServicesActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void getSubCategory() {

        progressDialog.show();
        new HttpsRequest(Consts.GET_SERVICE_SUB_CATEGORY_API, paramsucategory, AddServicesActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    try {
                        tvFiltersub.setVisibility(View.VISIBLE);
                        subCateModelArrayListserivce = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<SubCateModel>>() {
                        }.getType();
                        subCateModelArrayListserivce = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Collections.sort(subCateModelArrayListserivce, new Comparator<SubCateModel>() {
                            @Override
                            public int compare(SubCateModel lhs, SubCateModel rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        manual_service_relative.setVisibility(View.GONE);

                        spinnerDialogsubcate = new SpinnerDialogSubCate(AddServicesActivity.this, subCateModelArrayListserivce, getResources().getString(R.string.select_subcategory));
                        spinnerDialogsubcate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvFiltersub.setText(item);
                                paramsUpdate.put(Consts.sub_category_id, id);
                                paramsthird.put(Consts.sub_category_id, id);
                                paramsFour.put("sub_id", id);
                                sub_category_id = id;
                                getThirdCategory();

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    Toast.makeText(AddServicesActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getThirdCategory() {
        progressDialog.show();

        new HttpsRequest(Consts.get_sublevel_cat, paramsthird, AddServicesActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        tvhird.setVisibility(View.VISIBLE);

                        thirdCateModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ThirdCateModel>>() {
                        }.getType();
                        thirdCateModelArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        Collections.sort(thirdCateModelArrayList, new Comparator<ThirdCateModel>() {
                            @Override
                            public int compare(ThirdCateModel lhs, ThirdCateModel rhs) {
                                return lhs.getCat_name().compareTo(rhs.getCat_name());
                            }
                        });
                        manual_service_relative.setVisibility(View.GONE);

                        SpinnerDialogThird = new SpinnerDialogThird(AddServicesActivity.this, thirdCateModelArrayList, getResources().getString(R.string.select_sublevelcategory));
                        SpinnerDialogThird.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvhird.setText(item);
                                paramsFour.put("third_id", id);
                                paramsUpdate.put("third_id", id);
                                third_id = id;
                                getServiceName();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvhird.setVisibility(View.GONE);

                    thirdCateModelArrayList.clear();
                    paramsFour.put("third_id", "0");
                    // Toast.makeText(this,"Not Found",Toast.LENGTH_SHORT).show();
                    getServiceName();


                }
            }
        });
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

    public void getServiceName() {
        progressDialog.show();
        new HttpsRequest(Consts.getAllservice, paramsFour, AddServicesActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {
                        Log.e("SERVICE_RES", "" + response.toString());
                        //   tvservice.setVisibility(View.VISIBLE);
                        fourModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CommonServiceModel>>() {
                        }.getType();
                        fourModelArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        descriptionArrayList = fourModelArrayList.get(0).getDescription();

                        serviceAdapter = new AddServiceAdapter(AddServicesActivity.this, fourModelArrayList, descriptionArrayList, setonItemListener, vehicle_flag);
                        service_name_recyclerview.setAdapter(serviceAdapter);
                        service_name_recyclerview.setExpanded(true);
                        select_service_name.setVisibility(View.VISIBLE);
                        service_name_recyclerview.setVisibility(View.VISIBLE);


                        manual_service_relative.setVisibility(View.GONE);


                        manual_photo_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getImage();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    fourModelArrayList.clear();
                    tvservice.setVisibility(View.GONE);
                    Toast.makeText(AddServicesActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSubCatImage() {

        new HttpsRequest(Consts.GET_SUBCAT_IMAGE_API, parmsSubcatImage, AddServicesActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    //ProjectUtils.showToast(Booking.this, msg);
                    try {
                        //   Log.e("RESPONSE_SERVICE", "" + response.toString());
                        spinnerDialogDes.showSpinerDialog();

                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String image_uri = jsonObject1.getString("image");


                        // Log.e("success_msg", "" + success_msg);
                        // Log.d("image_uri", "" + image_uri);
                        send_img_str = image_uri;
                        Glide.with(AddServicesActivity.this).load(image_uri).into(manual_service_img);

                        manual_service_relative.setVisibility(View.VISIBLE);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //  Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }

    public void getImage() {



        Intent intent1 = new Intent(this, ImagePickActivity.class);
        intent1.putExtra(com.webknight.filemanager.activity.ImagePickActivity.IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    private SetOnCheckboxClick setonItemListener = new SetOnCheckboxClick() {

        @Override
        public void Click(String id, String price, String des, int position) {


            if (positionList.contains(position)) {

                serviceIdList.remove(id);
                positionList.remove(String.valueOf(position));

            } else {
                serviceIdList.add(id);
                positionList.add(position);

            }
        }
    };

    public void getrate() {
        new HttpsRequest(Consts.GET_RATE, paramsrate, AddServicesActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        rateArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<RateModel>>() {
                        }.getType();
                        rateArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        spinnerDialogRate = new SpinnerDialogRate(AddServicesActivity.this, rateArrayList, getResources().getString(R.string.select_rate));// With 	Animation
                        spinnerDialogRate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvrate.setText(item);


                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(AddServicesActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void showData() {
        categoryarraylist = new ArrayList<>();
        categoryarraylist = artistDetailsDTO.getCategory();

        productDTOList = new ArrayList<>();
        if (productDTOList.size() <= 0) {
            tvNotFound.setVisibility(View.GONE);
        }

        rv_services_cat.setVisibility(View.GONE);
        rv_services_sub_car.setVisibility(View.GONE);
        rv_services.setVisibility(View.VISIBLE);

        productDTOList = artistDetailsDTO.getProducts();

        if (productDTOList.size() == 0) {
            tvNotFound.setVisibility(View.VISIBLE);
        }
        Log.e(TAG, "showData: kamaii " + productDTOList.get(0).getProduct_name());

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(AddServicesActivity.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                etImageD.setText(imagePath);
                                Log.e("image", imagePath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(AddServicesActivity.this.getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(AddServicesActivity.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                etImageD.setText(imagePath);
                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(AddServicesActivity.this, MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }


    public void addProduct() {
        //  Toast.makeText(AddServicesActivity.this, "description id :"+description_id, Toast.LENGTH_SHORT).show();

        serviceid = serviceIdList.toString();

        if (vehicle_flag) {

            Log.e("serviceid6", "Toast" + "1");
            for (int i = 0; i < fourModelArrayList.size(); i++) {
                if (positionList.contains(i)) {
                    int pos = i;
                    priceList.add(serviceAdapter.priceList.get(pos).getEdittextValue());
                    vehiclenumList.add(serviceAdapter.vehicleNumberList.get(pos).getVehicleNumber());
                    descriptionList.add(serviceAdapter.descriptionList.get(pos));
                }
            }

        } else {

            Log.e("serviceid6", "Toast" + "2" + " " + fourModelArrayList.size());

            for (int i = 0; i < fourModelArrayList.size(); i++) {

                if (positionList.contains(i)) {

                    Log.e("III", "" + i);
                    priceList.add(serviceAdapter.priceList.get(i).getEdittextValue());
                    descriptionList.add(serviceAdapter.descriptionList.get(i));
                    maxQtyList.add(serviceAdapter.maxQtyList.get(i).getMaxQty());
                }
            }


        }

        //  Log.e("ID12345", "" + descriptionList.toString());
        // Log.e("ID12345", "" + priceList.toString());

        send_price_str = priceList.toString();
        description_id = descriptionList.toString();
        vechilenumber = vehiclenumList.toString();
        maxQtystr = maxQtyList.toString();


        Log.e("serviceid", "sd" + serviceid);
        Log.e("serviceid", "third_id" + third_id);

        Log.e("serviceid1", "description_id" + description_id);

        Log.e("serviceid2", "send_price_str" + send_price_str);

        Log.e("serviceid3", "maxQtystr" + maxQtystr);

        Log.e("serviceid4", "vechilenumber" + vechilenumber);

        // send_price_str = manual_price_etx.getText().toString();
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.addProduct(category_id, sub_category_id, third_id, userDTO.getUser_id(), servicename, serviceid, vechilenumber, encodedBase64, send_price_str, description_id, maxQtystr);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                ResponseBody responseBody2 = response.body();

                // Log.e("ADD_PRODUCT_RES", "" + responseBody2.toString());

                try {

                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        Log.e("serviceid123", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            Toast.makeText(AddServicesActivity.this, message, Toast.LENGTH_SHORT).show();

                            onBackPressed();

                        } else {

                            Toast.makeText(AddServicesActivity.this, message, Toast.LENGTH_SHORT).show();
                        }


                    } else {

                        Toast.makeText(AddServicesActivity.this, "Try Again Later ", Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(AddServicesActivity.this, "Try again. Server is not responding", Toast.LENGTH_SHORT).show();


            }
        });
    }
}