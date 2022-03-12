package com.kamaii.partner.ui.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentArtistProfileNewBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.models.newbooking.ArtistProfileDtoNew;
import com.kamaii.partner.utils.FileDownloader;
import com.kamaii.partner.utils.FileUtility;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArtistProfileNewFragment extends Fragment implements View.OnClickListener {

    FragmentArtistProfileNewBinding binding;
    private ViewPagerAdapter adapter;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> params;
    private HashMap<String, File> paramsFile;
    File file;
    private Bundle bundle;

    String pathOfImage;
    private BaseActivity baseActivity;
    private ArtistProfileDtoNew artistDetailsDTO;
    ProgressDialog progressDialog;
    private HashMap<String, String> paramsUpdate;
    private SharedPreferences firebase;
    String devicetoken = "";
    ImageCompression imageCompression;
    private HashMap<String, String> parms = new HashMap<>();
    DownloadManager manager;
    File pictureFileDir = null;
    MyDetailsFragment myDetailsFragment = new MyDetailsFragment();
    DocumentTabFragment documentTabFragment = new DocumentTabFragment();
    boolean artist_pic_flag = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_artist_profile_new, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_profile));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.editImage.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        binding.artistProfilePic.setOnClickListener(this);
        binding.downloadAgreement.setOnClickListener(this);

        getArtist();

        return binding.getRoot();
    }



    public void getArtist() {
    //    progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_PROFILE_DATA_API, parms, getActivity()).stringPosttwo(Consts.GET_ARTIST_PROFILE_DATA_API, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        Log.e("GET_ARTIST_PROFILE", "" + response.toString());
                    //    progressDialog.dismiss();
                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistProfileDtoNew.class);


                        showDataFirst();

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

        binding.artistName.setText(artistDetailsDTO.getName());
        Glide.with(getActivity()).
                load(artistDetailsDTO.getProfileImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.artistProfilePic);


        documentTabFragment.setArguments(bundle);
        myDetailsFragment.setArguments(bundle);

        // documentUploadFragment.setArguments(bundle);

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(myDetailsFragment,"MYDETAILS");
        adapter.addFragment(documentTabFragment,"DOCUMENTS");
        binding.pager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.pager);
        binding.pager.setCurrentItem(prefrence.getIntValue("tab"));
        prefrence.setIntValue("tab", 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.artist_profile_pic:
                artist_pic_flag = true;
                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;

            case R.id.download_agreement:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
                    } else {

                        download();
                    }
                }
                break;
        }
    }

    public void download()
    {
      //  pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Kamaii");
        new DownloadFile().execute(artistDetailsDTO.getDownloadAgreement(), "Kamaii-Agreement.pdf");
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.v("TAG", "doInBackground() Method invoked ");

            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File pdfFile = new File(folder, fileName);
            Log.v("TAG", "doInBackground() pdfFile invoked " + pdfFile.getAbsolutePath());
            Log.v("TAG", "doInBackground() pdfFile invoked " + pdfFile.getAbsoluteFile());

            try {
                pdfFile.createNewFile();
                Log.v("TAG", "doInBackground() file created" + pdfFile);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "doInBackground() error" + e.getMessage());
                Log.e("TAG", "doInBackground() error" + e.getStackTrace());


            }
            boolean flag = FileDownloader.downloadFile(fileUrl, pdfFile);
            String success = "0";
            if (flag){
                success = "1";
            }
            Log.v("TAG", "doInBackground() file download completed");

            return success;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("1")){
                Toast.makeText(getContext(), "File Downloaded!!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

          /*  if (requestCode == 23){

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
                                    .into(binding.artistProfilePic);
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
            }  */

            if (requestCode == 23){

                if (!artist_pic_flag){

                    documentTabFragment.onActivityResult(requestCode,resultCode,data);
                    //Log.e("IMAGE123456", "11" + pathOfImage);

                }else {

                    Uri picUri = data.getData();
                    pathOfImage = picUri.getPath();

                    String path = FileUtility.getPath(getActivity(), picUri);
                    File file = new File(path);
                    //  file = new File(imagePath);


                    Glide.with(getContext()).load(picUri)
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(binding.artistProfilePic);
                    paramsFile = new HashMap<>();
                    paramsFile.put(Consts.IMAGE, file);
                    //   Log.e("image", imagePath);
                    params = new HashMap<>();
                    params.put(Consts.USER_ID, userDTO.getUser_id());
                    if (NetworkManager.isConnectToInternet(getContext())) {
                        updateProfileSelf();
                    } else {
                        ProjectUtils.showToast(getContext(), getResources().getString(R.string.internet_concation));
                    }

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1011){
           if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
               download();
           }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
    public void updateProfileSelf() {

        Log.e("updateProfileSelf", "" + "updateProfileSelf");
        Log.e("updateProfileSelf", "" + "params" + params.toString());
        Log.e("updateProfileSelf", "" + "paramsFile" + paramsFile.toString());

        new HttpsRequest(Consts.UPDATE_PROFILE_API, params, paramsFile, getActivity()).imagePost(Consts.UPDATE_PROFILE_API, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                Log.e("updateProfileSelf", "" + response.toString());

                if (flag) {
                    try {

                        //ProjectUtils.showToast(getActivity(), msg);

                        userDTO = new Gson().fromJson(response.getJSONObject("data").toString(), UserDTO.class);
                        prefrence.setParentUser(userDTO, Consts.USER_DTO);
                        baseActivity.showImage();

                   //     if (!bg_image_bannew_flag) {
                            Glide.with(getActivity()).
                                    load(userDTO.getImage())
                                    .placeholder(R.drawable.dummyuser_image)
                                    .dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.artistProfilePic);

                      /*  } else {
                            Log.e("updateProfileSelf", "aSC");
                            getArtist();
                            bg_image_bannew_flag = false;
                        }*/


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

}