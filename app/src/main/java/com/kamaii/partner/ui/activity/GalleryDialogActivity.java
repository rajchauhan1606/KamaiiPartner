package com.kamaii.partner.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.GalleryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityGalleryDialogBindingImpl;
import com.kamaii.partner.databinding.GalleryDialogBinding;
import com.kamaii.partner.databinding.GalleryDialogBindingImpl;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.model.ThirdCateModel;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.adapter.AdapterGallery;
import com.kamaii.partner.ui.fragment.ArtistProfile;
import com.kamaii.partner.ui.fragment.ImageGallery;
import com.kamaii.partner.ui.models.SubCateModel;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.SpinnerDialog;
import com.kamaii.partner.utils.SpinnerDialogSubCate;
import com.kamaii.partner.utils.SpinnerDialogThird;
import com.kamaii.partner.utils.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GalleryDialogActivity extends AppCompatActivity {

    private String TAG = ImageGallery.class.getSimpleName();
    ActivityGalleryDialogBindingImpl binding1;
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rvGallery;
    private ArrayList<GalleryDTO> galleryList;
    private AdapterGallery adapterGallery;
    private Bundle bundle;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout llBack;
    private TouchImageView ivZoom;
    private RelativeLayout rlZoomImg, rlView;
    private ImageView ivClose;
    private CustomTextViewBold tvNotFound;
    private ArtistProfile parentFrag;
    private HashMap<String, String> paramsUpdate;
    private HashMap<String, File> paramsFile;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    BottomSheet.Builder builder;
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
    public Dialog dialogEditProduct;
    LinearLayout gallery_btn;
    ProgressDialog progressDialog;

    private HashMap<String, String> parmsCategory = new HashMap<>();
    private HashMap<String, String> paramsucategory = new HashMap<>();
    private HashMap<String, String> paramsrate = new HashMap<>();
    private HashMap<String, String> paramsthird = new HashMap<>();
    private HashMap<String, String> paramsFour = new HashMap<>();

    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    ArrayList<SubCateModel> subCateModelArrayListserivce = new ArrayList<>();
    ArrayList<ThirdCateModel> thirdCateModelArrayList = new ArrayList<>();

    private SpinnerDialog spinnerDialogCate;
    private SpinnerDialogSubCate spinnerDialogsubcate;
    private SpinnerDialogThird SpinnerDialogThird;
    String category_id = "", sub_category_id = "", third_id = "", vechilenumber = "", servicename = "", serviceid = "";
    String pathOfImage2 = "";
    ImageCompression imageCompression2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding1 = DataBindingUtil.setContentView(this, R.layout.activity_gallery_dialog);

        prefrence = SharedPrefrence.getInstance(GalleryDialogActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);


        progressDialog = new ProgressDialog(GalleryDialogActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());

        addServices();
    }


    public void getCategory() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, GalleryDialogActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
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
                        spinnerDialogCate = new SpinnerDialog(GalleryDialogActivity.this, categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.galleryTvcat.setText(item);
                                paramsucategory.put(Consts.CATEGORY_ID, id);
                                paramsthird.put(Consts.CATEGORY_ID, id);
                                paramsUpdate.put(Consts.CATEGORY_ID, id);
                                paramsFour.put("cat_id", id);
                                category_id = id;

                                binding1.gallerySubcat.setText(getResources().getString(R.string.all_sub_categories));
                                binding1.galleryThirdcat.setText(getResources().getString(R.string.all_sub__level_categories));

                                getSubCategory();

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void getSubCategory() {

        progressDialog.show();
        new HttpsRequest(Consts.GET_SERVICE_SUB_CATEGORY_API, paramsucategory, GalleryDialogActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    try {
                        binding1.gallerySubcat.setVisibility(View.VISIBLE);
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

                        spinnerDialogsubcate = new SpinnerDialogSubCate(GalleryDialogActivity.this, subCateModelArrayListserivce, getResources().getString(R.string.select_subcategory));
                        spinnerDialogsubcate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.gallerySubcat.setText(item);
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

                }
            }
        });
    }


    public void getThirdCategory() {
        progressDialog.show();

        new HttpsRequest(Consts.get_sublevel_cat, paramsthird, GalleryDialogActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        binding1.galleryThirdcat.setVisibility(View.VISIBLE);

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

                        SpinnerDialogThird = new SpinnerDialogThird(GalleryDialogActivity.this, thirdCateModelArrayList, getResources().getString(R.string.select_sublevelcategory));
                        SpinnerDialogThird.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.galleryThirdcat.setText(item);
                                paramsFour.put("third_id", id);
                                paramsUpdate.put("third_id", id);
                                third_id = id;
                                binding1.galleryLinear.setVisibility(View.VISIBLE);
                                //getServiceName();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    binding1.galleryThirdcat.setVisibility(View.GONE);

                    thirdCateModelArrayList.clear();
                    paramsFour.put("third_id", "0");
                    // Toast.makeText(GalleryDialogActivity.this,"Not Found",Toast.LENGTH_SHORT).show();
                    binding1.galleryLinear.setVisibility(View.VISIBLE);


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

    public void addServices() {
        getCategory();
        paramsUpdate = new HashMap<>();
        paramsFile = new HashMap<>();
        paramsucategory = new HashMap<>();
        paramsthird = new HashMap<>();
        paramsFour = new HashMap<>();
        binding1.galleryTvcat.setText(getResources().getString(R.string.all_categories));
        binding1.gallerySubcat.setText(getResources().getString(R.string.all_sub_categories));
        binding1.galleryThirdcat.setText(getResources().getString(R.string.all_sub__level_categories));
        binding1.galleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(GalleryDialogActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
            }
        });
        binding1.galleryTvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                    Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });
        binding1.gallerySubcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                    Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding1.galleryThirdcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                    Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding1.galleryClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialogEditProduct.cancel();
                categoryDTOS.clear();
                binding1.gallerySubcat.setVisibility(View.GONE);
                binding1.galleryThirdcat.setVisibility(View.GONE);
                subCateModelArrayListserivce.clear();
                thirdCateModelArrayList.clear();

                onBackPressed();
//                getCategory();

            }
        });
        binding1.gallerySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String cat = binding1.galleryTvcat.getText().toString().trim();
                String subcat = binding1.gallerySubcat.getText().toString().trim();
                String third = binding1.galleryThirdcat.getText().toString().trim();


                if (NetworkManager.isConnectToInternet(GalleryDialogActivity.this)) {
                    if (cat.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                        Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.val_category), Toast.LENGTH_SHORT).show();
                    } else if (subcat.equalsIgnoreCase(getResources().getString(R.string.all_sub_categories))) {
                        Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.val_subcatogry), Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkarss(category_id)) {
                            if (third.equalsIgnoreCase(getResources().getString(R.string.all_sub__level_categories))) {
                                Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.val_sublevelcatogry), Toast.LENGTH_SHORT).show();
                            } else if (vechilenumber.equalsIgnoreCase("")) {
                                Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.val_pnumber), Toast.LENGTH_SHORT).show();

                            } else {
                                addProduct();
                            }
                        } else {
                            addProduct();
                        }
                    }
                } else {
                    Toast.makeText(GalleryDialogActivity.this, getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 23) {

            Uri picUri = data.getData();

            pathOfImage = picUri.getPath();
            imageCompression = new ImageCompression(GalleryDialogActivity.this);
            imageCompression.execute(pathOfImage);
            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                @Override
                public void processFinish(String imagePath) {
                    try {
                        file = new File(imagePath);
                        Glide.with(GalleryDialogActivity.this).load("file://" + imagePath)
                                .thumbnail(0.5f)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding1.galleryPhoto);
                        paramsFile = new HashMap<>();
                        paramsFile.put(Consts.IMAGE, file);
                        //   Log.e("image", imagePath);
                        // params = new HashMap<>();
                        // params.put(Consts.USER_ID, userDTO.getUser_id());


                        // Log.e("image", imagePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            if (resultCode == RESULT_OK && requestCode == 23) {

                //    Toast.makeText(baseActivity, "7", Toast.LENGTH_SHORT).show();
                Log.e("asdhsabjc", "`146");
                Uri picUri = data.getData();
                Log.e("asdhsabjc", "`146" + picUri);

                pathOfImage2 = picUri.getPath();
                imageCompression2 = new ImageCompression(GalleryDialogActivity.this);
                imageCompression2.execute(pathOfImage2);
                imageCompression2.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        try {
                            file = new File(imagePath);
                            Glide.with(GalleryDialogActivity.this).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding1.galleryPhoto);

                            paramsFile = new HashMap<>();
                            paramsFile.put(Consts.BANNER_IMAGE, file);
                            //   Log.e("image", imagePath);
                            // params = new HashMap<>();
                            // params.put(Consts.USER_ID, userDTO.getUser_id());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }
    }

    public void addProduct() {

        //  Toast.makeText(GalleryDialogActivity.this, "description id :"+description_id, Toast.LENGTH_SHORT).show();

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.addProduct(category_id, sub_category_id, third_id, userDTO.getUser_id(), servicename, serviceid, vechilenumber, "encodedBase64", "send_price_str", "description_id", "");
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

                            binding1.gallerySubcat.setVisibility(View.GONE);
                            binding1.galleryThirdcat.setVisibility(View.GONE);
                            binding1.galleryLinear.setVisibility(View.GONE);

                            vechilenumber = "";
                            categoryDTOS.clear();
                            subCateModelArrayListserivce.clear();
                            thirdCateModelArrayList.clear();
                          //  dialogEditProduct.dismiss();
                            getCategory();

                        } else {

                        }


                    } else {

                        Toast.makeText(GalleryDialogActivity.this, "Try Again Later ", Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(GalleryDialogActivity.this, "Try again. Server is not responding", Toast.LENGTH_SHORT).show();


            }
        });

    }

}