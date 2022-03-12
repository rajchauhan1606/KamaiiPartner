package com.kamaii.partner.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.kamaii.partner.databinding.GalleryDialogBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.model.ThirdCateModel;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BankDocument;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BirthdayCardActivity;
import com.kamaii.partner.ui.activity.GalleryDialogActivity;
import com.kamaii.partner.ui.adapter.AdapterGallery;
import com.kamaii.partner.ui.models.SubCateModel;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.MainFragment;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialog;
import com.kamaii.partner.utils.SpinnerDialogSubCate;
import com.kamaii.partner.utils.SpinnerDialogThird;
import com.kamaii.partner.utils.TouchImageView;

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

public class ImageGallery extends Fragment implements View.OnClickListener {
    private String TAG = ImageGallery.class.getSimpleName();
    public View view;
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
    GalleryDialogBinding binding1;
    public Dialog dialogEditProduct;
    LinearLayout gallery_btn;
    ProgressDialog progressDialog;
    public HashMap<String, String> uploadparams = new HashMap<>();
    public HashMap<String, File> uploadfile = new HashMap<>();
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

    File imagepath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parentFrag = ((ArtistProfile) ImageGallery.this.getParentFragment());

        bundle = this.getArguments();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        gallery_btn = view.findViewById(R.id.gallery_btn);
        showUiAction(view);

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getActivity(), GalleryDialogActivity.class));
                addServices();
                // addGalleryClick();
            }
        });

        return view;
    }

    public void showUiAction(View v) {

        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());

        rvGallery = (RecyclerView) v.findViewById(R.id.rvGallery);
        tvNotFound = (CustomTextViewBold) v.findViewById(R.id.tvNotFound);
        llBack = (LinearLayout) v.findViewById(R.id.llBack);
        ivZoom = (TouchImageView) v.findViewById(R.id.ivZoom);
        rlZoomImg = (RelativeLayout) v.findViewById(R.id.rlZoomImg);
        rlView = (RelativeLayout) v.findViewById(R.id.rlView);
        ivClose = (ImageView) v.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);

        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));


        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.gallery_dialog, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());
        dialogEditProduct.setCancelable(false);

        showData();

    }


    public void showData() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        galleryList = new ArrayList<>();
        galleryList = artistDetailsDTO.getGallery();

        if (galleryList.size() <= 0) {
            galleryList.add(new GalleryDTO());
        }
        if (galleryList.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            rlView.setVisibility(View.VISIBLE);
            Log.e("GALLERYLIST_SIZE", "" + galleryList.size());
            adapterGallery = new AdapterGallery(ImageGallery.this, galleryList);
            rvGallery.setLayoutManager(gridLayoutManager);
            rvGallery.setAdapter(adapterGallery);
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
            rlView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                rlZoomImg.setVisibility(View.GONE);
                break;
        }
    }

    public void showImg(String imgURL) {
        rlZoomImg.setVisibility(View.VISIBLE);
        Glide
                .with(getActivity())
                .load(imgURL)
                .placeholder(R.drawable.dummyuser_image)
                .into(ivZoom);
    }

    public void getParentData() {
        parentFrag.getArtist();
    }

    public void addGalleryClick() {
        //dialogGallery();
    }


    public void getCategory() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_GALLERY_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
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
                        spinnerDialogCate = new SpinnerDialog(getActivity(), categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.galleryTvcat.setText(item);
                                paramsucategory.put(Consts.CATEGORY_ID, id);
                                paramsthird.put(Consts.CATEGORY_ID, id);
                                uploadparams.put(Consts.CATEGORY_ID, id);
                                uploadparams.put(Consts.USER_ID, userDTO.getUser_id());
                                paramsucategory.put(Consts.USER_ID, userDTO.getUser_id());
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
        new HttpsRequest(Consts.GET_GALLERY_SERVICE_SUB_CATEGORY_API, paramsucategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();

                Log.e("getAllSUBCaegory", "" + response.toString());

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

                        spinnerDialogsubcate = new SpinnerDialogSubCate(getActivity(), subCateModelArrayListserivce, getResources().getString(R.string.select_subcategory));
                        spinnerDialogsubcate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                binding1.gallerySubcat.setText(item);
                                paramsUpdate.put(Consts.sub_category_id, id);
                                uploadparams.put(Consts.sub_category_id, id);
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

        new HttpsRequest(Consts.get_sublevel_cat, paramsthird, getActivity()).stringPost(TAG, new Helper() {
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

                        SpinnerDialogThird = new SpinnerDialogThird(getActivity(), thirdCateModelArrayList, getResources().getString(R.string.select_sublevelcategory));
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
                    // Toast.makeText(getActivity(),"Not Found",Toast.LENGTH_SHORT).show();
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
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding1 = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.gallery_dialog, null, false);
        dialogEditProduct.setContentView(binding1.getRoot());
        dialogEditProduct.setCancelable(true);
        dialogEditProduct.show();

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

                parentFrag.getBoolean();
                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);

                dialogEditProduct.dismiss();
                //  binding1.galleryPhoto.setBackground(getResources().getDrawable(R.drawable.uploadfile));
            }
        });
        binding1.galleryTvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                //    Toast.makeText(getActivity(), getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });
        binding1.gallerySubcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                //    Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding1.galleryThirdcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                //    Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding1.galleryClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditProduct.dismiss();
                categoryDTOS.clear();
                binding1.gallerySubcat.setVisibility(View.GONE);
                binding1.galleryThirdcat.setVisibility(View.GONE);
                subCateModelArrayListserivce.clear();
                thirdCateModelArrayList.clear();
//                getCategory();

            }
        });

        dialogProduct();

    }

    public void dialogProduct() {
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

                parentFrag.product_image_flag = true;
                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                dialogEditProduct.dismiss();
            }
        });
        binding1.galleryTvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();
                } else {
                //    Toast.makeText(getActivity(), getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });
        binding1.gallerySubcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subCateModelArrayListserivce.size() > 0) {
                    spinnerDialogsubcate.showSpinerDialog();
                } else {
                //    Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding1.galleryThirdcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thirdCateModelArrayList.size() > 0) {
                    SpinnerDialogThird.showSpinerDialog();
                } else {
                 //   Toast.makeText(getActivity(), getResources().getString(R.string.no_sub_cate_level_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding1.galleryClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDTOS.clear();
                binding1.gallerySubcat.setVisibility(View.GONE);
                binding1.galleryThirdcat.setVisibility(View.GONE);
                subCateModelArrayListserivce.clear();
                thirdCateModelArrayList.clear();
                dialogEditProduct.dismiss();
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
              //  dialogEditProduct.dismiss();

                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (cat.equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.val_select_category), Toast.LENGTH_SHORT).show();
                    } else if (subcat.equalsIgnoreCase(getResources().getString(R.string.all_sub_categories))) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.val_select_subcatogry), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void addProduct() {

        //  Toast.makeText(getActivity(), "description id :"+description_id, Toast.LENGTH_SHORT).show();

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
                            dialogEditProduct.dismiss();
                            getCategory();

                        } else {

                        }


                    } else {

                    //    Toast.makeText(getActivity(), "Try Again Later ", Toast.LENGTH_SHORT).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            //    Toast.makeText(getActivity(), "Try again. Server is not responding", Toast.LENGTH_SHORT).show();


            }
        });

    }


    public void uploadImage() {

        Log.e("IMAGEPATH", "" + imagepath);
        Log.e("IMAGEPATH", "" + uploadparams.toString());
        uploadfile.put("image", imagepath);
        Log.e("IMAGEPATH", "uploadfile  " + uploadfile.toString());
        progressDialog.show();
        new HttpsRequest(Consts.UPLOAD_GALLERY_IMAGE_API, uploadparams, uploadfile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
               // Log.e("IMAGEPATH", "response" + response.toString());
                progressDialog.dismiss();
                if (flag) {
                    try {
                   //     ProjectUtils.showToast(getActivity(), msg);

//                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        //     prefrence.setParentUser(userDTO, Consts.USER_DTO);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.e("IMAGEPATH", "error" + response.toString());

                    //   ProjectUtils.showToast(getActivity(), msg);
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


        if (resultCode == RESULT_OK && requestCode == 23) {

            //   Toast.makeText(getActivity(), "6", Toast.LENGTH_SHORT).show();
            Uri picUri = data.getData();

            Log.e("PIC_URI", "" + picUri);
            pathOfImage = picUri.getPath();
            imageCompression = new ImageCompression(getActivity());
            imageCompression.execute(pathOfImage);
            imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                @Override
                public void processFinish(String imagePath) {
                    try {
                        file = new File(imagePath);
                        Glide.with(getActivity()).load("file://" + imagePath)
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
                imageCompression2 = new ImageCompression(getActivity());
                imageCompression2.execute(pathOfImage2);
                imageCompression2.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        try {
                            file = new File(imagePath);
                            Glide.with(getActivity()).load("file://" + imagePath)
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


    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    public void addGallery() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_GALLERY_API, paramsUpdate, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);
                    parentFrag.getArtist();
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


}
