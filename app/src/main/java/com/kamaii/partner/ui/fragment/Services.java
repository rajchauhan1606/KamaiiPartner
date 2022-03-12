package com.kamaii.partner.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.DailogArProductBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.DialogClose;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSelectedItemListener;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.model.CommonServiceModel;
import com.kamaii.partner.model.ThirdCateModel;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.AddServicesActivity;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BasicInfoActivity;
import com.kamaii.partner.ui.adapter.AdapterCategoryServices;
import com.kamaii.partner.ui.adapter.AdapterServices;
import com.kamaii.partner.ui.adapter.SubCateAdapter;
import com.kamaii.partner.ui.models.CategoryModel;
import com.kamaii.partner.ui.models.Description;
import com.kamaii.partner.ui.models.RateModel;
import com.kamaii.partner.ui.models.SubCateModel;
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

import static android.app.Activity.RESULT_OK;

public class Services extends Fragment implements DialogClose {

    private String TAG = Services.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rv_services_cat, rv_services_sub_car;
    //ExpandableHeightGridView rv_services;
    RecyclerView rv_services;
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
    DailogArProductBinding binding1;
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
    private SpinnerDialogThird SpinnerDialogThird;
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
    String category_id = "", sub_category_id = "", third_id = "", vechilenumber = "", servicename = "", serviceid = "";
    boolean image_flag = false;
    String encodedBase64 = "";
    ImageView service_lock;
    String send_img_str = null;
    String send_price_str = null;
    String description_id = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services, container, false);
        addbtnsubmit = view.findViewById(R.id.addbtnSubmit);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.serviceee));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.editImage.setVisibility(View.GONE);
        bundle = this.getArguments();
        // swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_services);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dailog_ar_product, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());


        dialogEditProduct.setCancelable(false);
        // artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        paramsservice.put(Consts.ARTIST_ID, userDTO.getUser_id());
        // getrate();
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        Typeface font2 = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Heavy.ttf");

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Semibold.ttf");
        Typeface font3 = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Black.ttf");

        binding1.addserviceTitle.setTypeface(font2);
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();
            getCategory();
        } else {
        //    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        addbtnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //chooseServiceActivity.addServices();
                //addServices();
                startActivity(new Intent(getActivity(), AddServicesActivity.class));
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

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                // swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                if (flag) {
                    try {

                        Log.e("SERVICE_RES",""+response.toString());

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
        rv_services = v.findViewById(R.id.rv_services);
        rv_services_cat = v.findViewById(R.id.rv_services_cat);
        rv_services_sub_car = v.findViewById(R.id.rv_services_sub_car);


        viewflipper = v.findViewById(R.id.viewflipper);
        tvNotFound = v.findViewById(R.id.tvNotFound);
        rlView = v.findViewById(R.id.rlView);

        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                                        picUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", file);
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
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

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
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
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
                        binding1.manualServiceRelative.setVisibility(View.GONE);

                        spinnerDialogCate = new SpinnerDialog(getActivity(), categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvcat.setText(item);
                                paramsucategory.put(Consts.CATEGORY_ID, id);
                                paramsthird.put(Consts.CATEGORY_ID, id);
                                paramsUpdate.put(Consts.CATEGORY_ID, id);
                                paramsFour.put("cat_id", id);
                                category_id = id;

                                binding1.tvFiltersub.setText(getResources().getString(R.string.all_sub_categories));
                                binding1.tvhird.setText(getResources().getString(R.string.all_sub__level_categories));
                                binding1.tvservice.setText(getResources().getString(R.string.all_service_name));
                                binding1.etvechialNumber.setText("");
                                if (checkarss(category_id)) {
                                    binding1.etvechialNumber.setVisibility(View.VISIBLE);
                                    binding1.etvechialNumberTitle.setVisibility(View.VISIBLE);
                                } else {
                                    binding1.etvechialNumber.setVisibility(View.GONE);
                                    binding1.etvechialNumberTitle.setVisibility(View.GONE);
                                }
                                getSubCategory();

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
                //    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void getSubCategory() {

        progressDialog.show();
        new HttpsRequest(Consts.GET_SERVICE_SUB_CATEGORY_API, paramsucategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    try {
                        binding1.tvFiltersub.setVisibility(View.VISIBLE);
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
                        binding1.manualServiceRelative.setVisibility(View.GONE);

                        spinnerDialogsubcate = new SpinnerDialogSubCate(getActivity(), subCateModelArrayListserivce, getResources().getString(R.string.select_subcategory));
                        spinnerDialogsubcate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvFiltersub.setText(item);
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

                //    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getThirdCategory() {
        progressDialog.show();

        new HttpsRequest(Consts.get_sublevel_cat, paramsthird, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        binding1.tvhird.setVisibility(View.VISIBLE);

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
                        binding1.manualServiceRelative.setVisibility(View.GONE);

                        SpinnerDialogThird = new SpinnerDialogThird(getActivity(), thirdCateModelArrayList, getResources().getString(R.string.select_sublevelcategory));
                        SpinnerDialogThird.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvhird.setText(item);
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
                    binding1.tvhird.setVisibility(View.GONE);
                    thirdCateModelArrayList.clear();
                    paramsFour.put("third_id", "0");
                    // Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_SHORT).show();
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
        new HttpsRequest(Consts.getAllservice, paramsFour, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        binding1.tvservice.setVisibility(View.VISIBLE);
                        fourModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CommonServiceModel>>() {
                        }.getType();
                        fourModelArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        descriptionArrayList = fourModelArrayList.get(0).getDescription();
                        binding1.manualServiceRelative.setVisibility(View.GONE);

                        spinnerDialogFour = new SpinnerDialogFour(getActivity(), fourModelArrayList, getResources().getString(R.string.select_sname));
                        spinnerDialogFour.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvservice.setText(item);
                                servicename = item;
                                serviceid = id;
                                binding1.manualPriceEtx.setText(fourModelArrayList.get(position).getPrice());
                                parmsSubcatImage.put("service_name", item);
                                getSubCatImage();

                            }
                        });

                        binding1.manualPhotoBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(ChooseServiceActivity.this, "jkjlkjjlk", Toast.LENGTH_SHORT).show();
                                getImage();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    fourModelArrayList.clear();
                    binding1.tvservice.setVisibility(View.GONE);
               //     Toast.makeText(getActivity(), "Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSubCatImage() {

        //  Log.e("parmsSubcatImage", "" + parmsSubcatImage.toString());
        new HttpsRequest(Consts.GET_SUBCAT_IMAGE_API, parmsSubcatImage, getActivity()).stringPost(TAG, new Helper() {
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
                        Glide.with(getActivity()).load(image_uri).into(binding1.manualServiceImg);

                        binding1.manualServiceRelative.setVisibility(View.VISIBLE);

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

        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(com.webknight.filemanager.activity.ImagePickActivity.IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }

    public void getrate() {
        new HttpsRequest(Consts.GET_RATE, paramsrate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        rateArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<RateModel>>() {
                        }.getType();
                        rateArrayList = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        spinnerDialogRate = new SpinnerDialogRate(getActivity(), rateArrayList, getResources().getString(R.string.select_rate));// With 	Animation
                        spinnerDialogRate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.tvrate.setText(item);


                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
               //     Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        adapterServices = new AdapterServices(Services.this, productDTOList, userDTO,this);
           rv_services.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_services.setAdapter(adapterServices);



    }

    private ArrayList<SubCateModel> getIndexByProperty(String yourString) {
        ArrayList<SubCateModel> temo = new ArrayList<>();

        for (int i = 0; i < subCateModelArrayList.size(); i++) {
            if (subCateModelArrayList != null && subCateModelArrayList.get(i).getCat_id().equalsIgnoreCase(yourString)) {
                temo.add(subCateModelArrayList.get(i));
            }
        }

        return temo;// not there is list
    }

    private ArrayList<ProductDTO> getIndexByPropertyproduct(String catid, String subid) {
        ArrayList<ProductDTO> temo = new ArrayList<>();

        for (int i = 0; i < productDTOList.size(); i++) {
            if (productDTOList != null && productDTOList.get(i).getCategory_id().equalsIgnoreCase(catid) && productDTOList.get(i).getSub_category_id().equalsIgnoreCase(subid)) {
                temo.add(productDTOList.get(i));
            }
        }
        return temo;// not there is list
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
                rv_services_sub_car.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                rv_services_sub_car.addItemDecoration(new ItemDecorationAlbumColumns(
                        2,
                        4));
                // ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_padding);
                //   rec_category.addItemDecoration(itemDecoration);
                subCateAdapter = new SubCateAdapter(getActivity(), subCateModelArrayListnew, onsubItemListener);
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

            String subid = selectionString;
       viewflipper.showNext();

        }
    };

    public void getParentData() {

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getArtist();


        } else {
        //    Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

        }
    }

    public void getrefresh() {

        adapterServices.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {

            getArtist();

        } else {
       //     Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();

        }

    }

    public void addServices() {
        dialogEditProduct.show();
        dialogProduct();

    }

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
        binding1.etImageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        binding1.tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
             //       Toast.makeText(getActivity(), getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvFiltersub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
               //     Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvhird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
         //           Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding1.tvservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fourModelArrayList.size() > 0) {
                    spinnerDialogFour.showSpinerDialog();
                } else {
             //       Toast.makeText(getActivity(), getResources().getString(R.string.no_service_name_found), Toast.LENGTH_SHORT).show();

                }
            }
        });


        binding1.tvrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rateArrayList.size() > 0) {
                    spinnerDialogRate.showSpinerDialog();
                } else {
              //      Toast.makeText(getActivity(), getResources().getString(R.string.no_rate_found), Toast.LENGTH_SHORT).show();

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
                binding1.etvechialNumber.setVisibility(View.GONE);
                binding1.etvechialNumberTitle.setVisibility(View.GONE);
                vechilenumber = "";
                subCateModelArrayListserivce.clear();
                thirdCateModelArrayList.clear();
                fourModelArrayList.clear();
                dialogEditProduct.dismiss();
                getCategory();

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
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);
                                binding1.etImageD.setText(imagePath);
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
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);
                                binding1.etImageD.setText(imagePath);
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
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }


    public void addProduct() {

        //  Toast.makeText(getActivity(), "description id :"+description_id, Toast.LENGTH_SHORT).show();
        Log.e("description_id", "" + description_id);
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

                     //       Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


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
                            //  getArtist();
                            getArtist();
                            getCategory();

                        } else {

                        //    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

           //     Toast.makeText(getActivity(), "Try again. Server is not responding", Toast.LENGTH_SHORT).show();


            }
        });



    }

    @Override
    public void onClose(Dialog dialog) {

        baseActivity.loadHomeFragment(new MainserviceFragment(),"asd");
    }





}
