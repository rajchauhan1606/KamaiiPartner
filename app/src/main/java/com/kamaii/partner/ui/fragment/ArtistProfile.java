package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentArtistProfileBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BasicInfoActivity;
import com.kamaii.partner.ui.activity.EditPersnoalInfo;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.MainFragment;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static freemarker.template.utility.StringUtil.capitalize;

public class ArtistProfile extends Fragment implements View.OnClickListener {

    private String TAG = ArtistProfile.class.getSimpleName();
    private FragmentArtistProfileBinding binding;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArtistDetailsDTO artistDetailsDTO;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private PersnoalInfo persnoalInfo = new PersnoalInfo();
    private ImageGallery imageGallery = new ImageGallery();
    private PreviousWork previousWork = new PreviousWork();
    private Services services = new Services();
    private Reviews reviews = new Reviews();
    private DocumentUploadFragment documentUploadFragment = new DocumentUploadFragment();
    private Bundle bundle;
    private ViewPagerAdapter adapter;
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private HashMap<String, String> paramsUpdate;
    private HashMap<String, File> paramsFile;
    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    String pathOfImage2 = "";
    Bitmap bm;
    ImageCompression imageCompression, imageCompression2;
    File file;
    private HashMap<String, String> params;
    private BaseActivity baseActivity;
    private HashMap<String, String> paramsDeleteImg = new HashMap<>();
    ProgressDialog progressDialog;
    boolean bg_image_bannew_flag = false;
    public boolean product_image_flag = false;
    private SharedPreferences firebase;
    String devicetoken = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_profile, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_profile));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.editImage.setVisibility(View.GONE);
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");


        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());


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
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
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

        setUiAction();


        return binding.getRoot();
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

        if (prefrence.getBooleanValue("testing_account_otp")){
            Log.e("testing_account_otp",""+prefrence.getBooleanValue("testing_account_otp"));
            devicetoken="";
        }

        Log.e("devicetoken", "1" + devicetoken);
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.onlineOffline(userDTO.getUser_id(), onoff, devicetoken,getDeviceName());
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


                            getArtistSecond();


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

    public void setUiAction() {
        binding.ivEditPersonal.setOnClickListener(this);

        binding.ivArtist.setOnClickListener(this);
        // binding.galleryImg.setOnClickListener(this);
        binding.galleryImg.setOnClickListener(this);

        baseActivity.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (categoryDTOS.size() > 0) {
                        Intent intent = new Intent(getActivity(), EditPersnoalInfo.class);
                        intent.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                        intent.putExtra(Consts.CATEGORY_list, categoryDTOS);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                    } else {
                    //    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.try_after));
                    }
                } else {
                   // ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getCategory();
            getArtist();

        } else {
          //  ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        //  ProjectUtils.showToast(getActivity(),"work");

    }

    public void getCategory() {
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                }
            }
        });
    }

    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        Log.e("PARTNER_ADDRESS", "" + response.toString());
                        progressDialog.dismiss();
                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);

                        Log.e("updateProfileSelf", "artist_banner" + artistDetailsDTO.getArtist_banner());
                        Glide.with(getContext()).load(artistDetailsDTO.getArtist_banner()).into(binding.artistBgImg);
                        Log.e("PARTNER_ADDRESS", " " + artistDetailsDTO.toString());
                        Log.e("PARTNER_ADDRESS", " " + artistDetailsDTO.getLocation());

                        showDataFirst();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void getArtistSecond() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        Log.e("PARTNER_ADDRESS", "" + response.toString());
                        progressDialog.dismiss();
                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);

                        Log.e("updateProfileSelf", "artist_banner" + artistDetailsDTO.getArtist_banner());
                        Glide.with(getContext()).load(artistDetailsDTO.getArtist_banner()).into(binding.artistBgImg);
                        Log.e("PARTNER_ADDRESS", " " + artistDetailsDTO.toString());
                        Log.e("PARTNER_ADDRESS", " " + artistDetailsDTO.getLocation());

                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    public void showDataFirst() {

        bundle = new Bundle();
        bundle.putSerializable(Consts.ARTIST_DTO, artistDetailsDTO);
        binding.tvProfileComplete.setText(artistDetailsDTO.getCompletePercentages() + "% " + getResources().getString(R.string.completion));

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));

            baseActivity.tvOnOff.setTextColor(Color.GREEN);


        } else {

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));

            baseActivity.tvOnOff.setTextColor(Color.WHITE);

        }
        if (artistDetailsDTO.getTotalJob().equalsIgnoreCase("1") || artistDetailsDTO.getTotalJob().equalsIgnoreCase("0")) {
            binding.tvJobComplete.setText(artistDetailsDTO.getTotalJob() + " " + getResources().getString(R.string.job_comleted));
        } else {
            binding.tvJobComplete.setText(artistDetailsDTO.getTotalJob() + " " + getResources().getString(R.string.jobs_comleted));
        }
        binding.tvName.setText(artistDetailsDTO.getName());
        Glide.with(getActivity()).
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivArtist);

        if (!artistDetailsDTO.getSociety_name().equalsIgnoreCase("")) {

            binding.landmarkName.setText(Html.fromHtml("<b>Landmark : </b>")+artistDetailsDTO.getSociety_name());
        } else {
            binding.landmarkName.setVisibility(View.GONE);
        }

        if (!artistDetailsDTO.getHouse_no().equalsIgnoreCase("")) {

            binding.blockNo.setText(Html.fromHtml("<b>Block No : </b>")+artistDetailsDTO.getHouse_no());
        } else {
            binding.blockNo.setVisibility(View.GONE);
        }

        binding.tvLocation.setText(Html.fromHtml("<b>Address : </b>")+artistDetailsDTO.getLocation());


        binding.ratingbar.setRating(Float.parseFloat(artistDetailsDTO.getAva_rating()));
        binding.tvRating.setText("(" + artistDetailsDTO.getAva_rating() + "/5)");
        persnoalInfo.setArguments(bundle);
        imageGallery.setArguments(bundle);
        previousWork.setArguments(bundle);
        reviews.setArguments(bundle);
        services.setArguments(bundle);
        // documentUploadFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(persnoalInfo, "Info");
        //  adapter.addFragment(services, "Services");
        adapter.addFragment(imageGallery, "Gallery");
        adapter.addFragment(previousWork, "Orders");
        adapter.addFragment(reviews, "Reviews");
        //  adapter.addFragment(documentUploadFragment, "Document");

        binding.pager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.pager);

        binding.pager.setCurrentItem(prefrence.getIntValue("tab"));
        prefrence.setIntValue("tab", 0);
    }

    public void showData() {

        bundle = new Bundle();
        bundle.putSerializable(Consts.ARTIST_DTO, artistDetailsDTO);
        binding.tvProfileComplete.setText(artistDetailsDTO.getCompletePercentages() + "% " + getResources().getString(R.string.completion));

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));

            baseActivity.tvOnOff.setTextColor(Color.GREEN);
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


        } else {

            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));

            baseActivity.tvOnOff.setTextColor(Color.WHITE);

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
        }
        if (artistDetailsDTO.getTotalJob().equalsIgnoreCase("1") || artistDetailsDTO.getTotalJob().equalsIgnoreCase("0")) {
            binding.tvJobComplete.setText(artistDetailsDTO.getTotalJob() + " " + getResources().getString(R.string.job_comleted));
        } else {
            binding.tvJobComplete.setText(artistDetailsDTO.getTotalJob() + " " + getResources().getString(R.string.jobs_comleted));
        }
        binding.tvName.setText(artistDetailsDTO.getName());
        Glide.with(getActivity()).
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivArtist);

        if (!artistDetailsDTO.getSociety_name().equalsIgnoreCase("")) {

            binding.landmarkName.setText(artistDetailsDTO.getSociety_name());
        } else {
            binding.landmarkName.setVisibility(View.GONE);
        }

        if (!artistDetailsDTO.getHouse_no().equalsIgnoreCase("")) {

            binding.blockNo.setText(artistDetailsDTO.getHouse_no());
        } else {
            binding.blockNo.setVisibility(View.GONE);
        }

        binding.tvLocation.setText(artistDetailsDTO.getLocation());


        binding.ratingbar.setRating(Float.parseFloat(artistDetailsDTO.getAva_rating()));
        binding.tvRating.setText("(" + artistDetailsDTO.getAva_rating() + "/5)");
        persnoalInfo.setArguments(bundle);
        imageGallery.setArguments(bundle);
        previousWork.setArguments(bundle);
        reviews.setArguments(bundle);
        services.setArguments(bundle);
        // documentUploadFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(persnoalInfo, "Info");
        //  adapter.addFragment(services, "Services");
        adapter.addFragment(imageGallery, "Gallery");
        adapter.addFragment(previousWork, "Orders");
        adapter.addFragment(reviews, "Reviews");
        //  adapter.addFragment(documentUploadFragment, "Document");

        binding.pager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.pager);

        binding.pager.setCurrentItem(prefrence.getIntValue("tab"));
        prefrence.setIntValue("tab", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEditPersonal:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (categoryDTOS.size() > 0) {
                        Intent intent = new Intent(getActivity(), EditPersnoalInfo.class);
                        intent.putExtra(Consts.ARTIST_DTO, artistDetailsDTO);
                        intent.putExtra(Consts.CATEGORY_list, categoryDTOS);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                    } else {
                    //    ProjectUtils.showLong(getActivity(), getResources().getString(R.string.try_after));
                    }
                } else {
                   // ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }


                break;
            case R.id.ivArtist:
                if (artistDetailsDTO != null) {

                    ImagePicker.Companion.with(getActivity())
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.incomplete_profile_msg));
                }
                break;

            case R.id.gallery_img:
                //  Toast.makeText(baseActivity, "qwer", Toast.LENGTH_SHORT).show();
                bg_image_bannew_flag = true;
                ImagePicker.Companion.with(getActivity())
                        .crop()//Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
        }
    }


    public void tempimage(String toString, String s) {
        // Toast.makeText(getActivity(), "ccc", Toast.LENGTH_LONG).show();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void isOnline() {
        new HttpsRequest(Consts.ONLINE_OFFLINE_API, paramsUpdate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    // ProjectUtils.showToast(getActivity(), "Online");
                    getArtistSecond();

                } else {
                    // ProjectUtils.showToast(getActivity(), "Offline");
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

    public void getBoolean() {

        product_image_flag = true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("IMAGE123456", "1" + product_image_flag);

        if (product_image_flag) {
            Log.e("IMAGE123456", "11 product_image_flag" + product_image_flag);

            if (resultCode == RESULT_OK && requestCode == 23) {

                //  Toast.makeText(baseActivity, "6", Toast.LENGTH_SHORT).show();
                Uri picUri = data.getData();

                pathOfImage = picUri.getPath();
                Log.e("IMAGE123456", " image path " + pathOfImage);

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
                                    .into(imageGallery.binding1.galleryPhoto);

                            imageGallery.imagepath = file;

                            Log.e("IMAGE_PATH", "" + imageGallery.imagepath);
                            if (NetworkManager.isConnectToInternet(getActivity())) {
                                imageGallery.uploadImage();
                                imageGallery.binding1.galleryClose.performClick();
                                imageGallery.progressDialog.dismiss();
                                imageGallery.dialogEditProduct.dismiss();
                                baseActivity.navItemIndex = 233;
                                baseActivity.loadHomeFragment(new ArtistProfile(), "artist");
                            } else {
                               // ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                            }

                            imageGallery.dialogEditProduct.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } else if (!bg_image_bannew_flag) {

            Log.e("IMAGE123456", "2" + product_image_flag);
            if (resultCode == RESULT_OK && requestCode == 23) {

                //  Toast.makeText(baseActivity, "6", Toast.LENGTH_SHORT).show();
                Uri picUri = data.getData();

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
                                    .into(binding.ivArtist);
                            paramsFile = new HashMap<>();
                            paramsFile.put(Consts.IMAGE, file);
                            //   Log.e("image", imagePath);
                            params = new HashMap<>();
                            params.put(Consts.USER_ID, userDTO.getUser_id());
                            if (NetworkManager.isConnectToInternet(getActivity())) {
                                updateProfileSelf();
                            } else {
                                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                            }


                            // Log.e("image", imagePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } else {
            Log.e("IMAGE123456", "3");
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
                                    .into(binding.artistBgImg);

                            paramsFile = new HashMap<>();
                            paramsFile.put(Consts.BANNER_IMAGE, file);
                            //   Log.e("image", imagePath);
                            params = new HashMap<>();
                            params.put(Consts.USER_ID, userDTO.getUser_id());
                            if (NetworkManager.isConnectToInternet(getActivity())) {
                                updateProfileSelf();
                            } else {
                               // ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }


    }

    public void startCropping(Uri uri, int requestCode) {
        //   Toast.makeText(baseActivity, "hjhjhjhj", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    public void updateProfileSelf() {

        Log.e("updateProfileSelf", "" + "updateProfileSelf");
        Log.e("updateProfileSelf", "" + "params" + params.toString());
        Log.e("updateProfileSelf", "" + "paramsFile" + paramsFile.toString());

        new HttpsRequest(Consts.UPDATE_PROFILE_API, params, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("updateProfileSelf", "" + response.toString());

                if (flag) {
                    try {

                        //ProjectUtils.showToast(getActivity(), msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        baseActivity.showImage();

                        if (!bg_image_bannew_flag) {
                            Glide.with(getActivity()).
                                    load(userDTO.getImage())
                                    .placeholder(R.drawable.dummyuser_image)
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivArtist);

                        } else {
                            Log.e("updateProfileSelf", "aSC");
                            getArtist();
                            bg_image_bannew_flag = false;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    Log.e("updateProfileSelf", "" + response.toString());

                    //   ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void deleteImage() {
        paramsDeleteImg.put(Consts.USER_ID, userDTO.getUser_id());
        new HttpsRequest(Consts.DELETE_PROFILE_IMAGE_API, paramsDeleteImg, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    userDTO.setImage("");
                    artistDetailsDTO.setImage("");
                    prefrence.setParentUser(userDTO, Consts.USER_DTO);
                    showDataFirst();
                } else {
                    //   ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


}
