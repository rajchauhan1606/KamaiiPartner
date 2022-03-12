package com.kamaii.partner.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentNewBookingsBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.EndlessRecyclerOnScrollListener;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.AdapterAllBookings;
import com.kamaii.partner.ui.adapter.AdapterAllBookingsOld;
import com.kamaii.partner.ui.models.ArtistBookingOld;
import com.kamaii.partner.utils.FileUtility;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewBookings extends Fragment implements View.OnClickListener {

    private BaseActivity baseActivity;
    private FragmentNewBookingsBinding binding;
    private ArrayList<ArtistBooking> artistBookingsList = new ArrayList<>();
    private ArrayList<ArtistBooking> artistBookingsListnew = new ArrayList<>();
    private ArrayList<ArtistBookingOld> artistBookingsListold = new ArrayList<>();
    private ArrayList<ArtistBookingOld> artistBookingsListoldnew = new ArrayList<>();
    EndlessRecyclerOnScrollListener scrollListener;
    private ArrayList<ArtistBooking> artistBookingsListSection;
    private ArrayList<ArtistBooking> artistBookingsListSection1;
    private ArrayList<ProductDTO> productDTOArrayList = new ArrayList<>();
    private HashMap<String, String> parmsversion = new HashMap<>();
    private LinearLayoutManager mLayoutManager;
    private String TAG = NewBookings.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsartist = new HashMap<>();
    private LayoutInflater myInflater;
    private LayoutInflater myInflaterold;
    private AdapterAllBookings adapterAllBookings;
    private AdapterAllBookingsOld adapterAllBookingsold;
    private ArtistDetailsDTO artistDetailsDTO;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean cameraAccepted, storageAccepted, accessNetState, fineLoc, corasLoc;
    private SharedPrefrence prefference;
    Context mContext;
    public static HashMap<String, File> paramsFile;
    public static Uri uri = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HashMap<String, String> paramsUpdate;
    int current_page = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_bookings, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        myInflater = LayoutInflater.from(getActivity());
        myInflaterold = LayoutInflater.from(getActivity());
        parmsartist.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.USER_ID, userDTO.getUser_id());

        setUiAction();
        mContext = getActivity();
        prefference = SharedPrefrence.getInstance(mContext);

        paramsFile = new HashMap<>();


        return binding.getRoot();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUiAction() {
        binding.tvStatus.setText(getResources().getString(R.string.pending));

        baseActivity.headerNameTV.setText(getResources().getString(R.string.bookings));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.rvBooking.setLayoutManager(mLayoutManager);
        binding.rvBookingOld.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.e("CURRENT_PAGE", "123");

        binding.newbookingScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    current_page++;
                    //   Log.e("CURRENT_PAGE", "" + current_page + " count "+ totalItemCount);
                    Log.e("CURRENT_PAGE", "" + current_page);
                    // getBookings2(current_page);
                    binding.idPBLoading.setVisibility(View.VISIBLE);
                    getBookingsCopy(current_page);
                }
            }
        });

     /*   scrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemCount) {

                current_page = page;
                Log.e("CURRENT_PAGE", "" + current_page + " count "+ totalItemCount);
               // getBookings2(current_page);
                getBookings(current_page);
            }

        };
        binding.rvBooking.addOnScrollListener(scrollListener);
        binding.rvBookingOld.addOnScrollListener(scrollListener);*/

        binding.tvPendings.setOnClickListener(this);
        binding.tvAccepted.setOnClickListener(this);
        binding.tvRejected.setOnClickListener(this);
        binding.tvCompleted.setOnClickListener(this);
        binding.tvcurrent.setOnClickListener(this);

        binding.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    adapterAllBookings.filter(newText.toString());

                } else {
                    parms.put(Consts.BOOKING_FLAG, "4");
                    getBookings(current_page);

                }
                return false;
            }
        });


        //binding.tvCompleted.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line_two));
        binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
        binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
        binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
        binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));

        binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
        binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
        binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
        binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));
        binding.tvCompleted.setTextColor(getResources().getColor(R.color.white));


        binding.tvStatus.setText(getResources().getString(R.string.com));

        if (NetworkManager.isConnectToInternet(getActivity())) {
            // getArtist();
            parms.put(Consts.BOOKING_FLAG, "4");
            getBookings(current_page);


        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }
    }


    public void getBookings(int range) {

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put("range", String.valueOf(range));

        binding.idPBLoading.setVisibility(View.GONE);
        ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                if (flag) {
                    Log.e("api_res", "" + response.toString());
                    binding.tvNo.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    // binding.rvBookingOld.setVisibility(View.VISIBLE);
                    binding.rlSearch.setVisibility(View.VISIBLE);
                    try {

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ArtistBooking artistBooking = new ArtistBooking();
                        }
                        Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                        }.getType();
                        artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0; i < artistBookingsList.size(); i++) {
                            artistBookingsListnew.add(artistBookingsList.get(i));
                        }

                        // getBookingsOld(range);
                        //showData();

                        //   Collections.reverse(artistBookingsList);
                        adapterAllBookings = new AdapterAllBookings(NewBookings.this, artistBookingsListnew, userDTO, myInflater);
                        binding.rvBooking.setAdapter(adapterAllBookings);

                        if (artistBookingsListnew.size() > 6) {

                            binding.idPBLoading.setVisibility(View.VISIBLE);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    binding.idPBLoading.setVisibility(View.GONE);

                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.GONE);
                    binding.rvBookingOld.setVisibility(View.GONE);
                    binding.rlSearch.setVisibility(View.GONE);

                }
            }
        });


    }

    public void getBookingsCopy(int range) {

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put("range", String.valueOf(range));


        //ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                if (flag) {
                    Log.e("api_res", "" + response.toString());
                    binding.tvNo.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    // binding.rvBookingOld.setVisibility(View.VISIBLE);
                    binding.rlSearch.setVisibility(View.VISIBLE);
                    try {

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ArtistBooking artistBooking = new ArtistBooking();
                        }
                        Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                        }.getType();
                        artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0; i < artistBookingsList.size(); i++) {
                            artistBookingsListnew.add(artistBookingsList.get(i));
                        }

                        // getBookingsOld(range);
                        //showData();

                        //   Collections.reverse(artistBookingsListnew);
                        adapterAllBookings = new AdapterAllBookings(NewBookings.this, artistBookingsListnew, userDTO, myInflater);
                        binding.rvBooking.setAdapter(adapterAllBookings);
                        // ProjectUtils.pauseProgressDialog();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    if (artistBookingsListnew.size() != 0) {
                        binding.idPBLoading.setVisibility(View.GONE);

                        showData();
                    } else {
                        binding.idPBLoading.setVisibility(View.GONE);

                        binding.tvNo.setVisibility(View.VISIBLE);
                        binding.rvBooking.setVisibility(View.GONE);
                        binding.rvBookingOld.setVisibility(View.GONE);
                        binding.rlSearch.setVisibility(View.GONE);
                    }

                }
            }
        });


    }

/*
    public void getBookingsOld(int range) {

        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put("range", String.valueOf(range));


        ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_OLD_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                //  Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                if (flag) {
                    Log.e("api_res", "" + response.toString());
                    binding.tvNo.setVisibility(View.GONE);
                    binding.rvBooking.setVisibility(View.VISIBLE);
                    binding.rvBookingOld.setVisibility(View.VISIBLE);
                    binding.rlSearch.setVisibility(View.VISIBLE);
                    try {
                        Type getpetDTO = new TypeToken<List<ArtistBookingOld>>() {
                        }.getType();
                        artistBookingsListold = (ArrayList<ArtistBookingOld>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        for (int i = 0;i < artistBookingsListold.size();i++){
                            artistBookingsListoldnew.add(artistBookingsListold.get(i));
                        }
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    binding.tvNo.setVisibility(View.VISIBLE);
                    binding.rvBooking.setVisibility(View.GONE);
                    binding.rvBookingOld.setVisibility(View.GONE);

                    binding.rlSearch.setVisibility(View.GONE);

                }
            }
        });


    }
*/

    public void showData() {

        /*Collections.reverse(artistBookingsListold);
        adapterAllBookingsold = new AdapterAllBookingsOld(NewBookings.this, artistBookingsListoldnew, userDTO, myInflaterold);
        binding.rvBookingOld.setAdapter(adapterAllBookingsold);
*/
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvPendings:


                binding.tvPendings.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));

                binding.tvPendings.setTextColor(getResources().getColor(R.color.white));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));


                binding.tvStatus.setText(getResources().getString(R.string.pending));
                parms.put(Consts.BOOKING_FLAG, "0");
                getBookings(current_page);
                break;
            case R.id.tvcurrent:


                binding.tvcurrent.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));


                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.white));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));

                binding.tvStatus.setText(getResources().getString(R.string.curent));
                parms.put(Consts.BOOKING_FLAG, "3");
                getBookings(current_page);
                break;
            case R.id.tvAccepted:


                binding.tvAccepted.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));


                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.white));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));

                binding.tvStatus.setText(getResources().getString(R.string.acc));
                parms.put(Consts.BOOKING_FLAG, "1");
                getBookings(current_page);
                break;
            case R.id.tvRejected:
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line_two));

                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));


                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.white));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));

                binding.tvStatus.setText(getResources().getString(R.string.rej));
                parms.put(Consts.BOOKING_FLAG, "2");
                artistBookingsListnew = new ArrayList<>();
                current_page = 0;
                getBookings(current_page);
                break;
            case R.id.tvCompleted:
                binding.tvCompleted.setBackground(getResources().getDrawable(R.drawable.button_line_two));
                binding.tvAccepted.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvRejected.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvPendings.setBackground(getResources().getDrawable(R.drawable.button_line));
                binding.tvcurrent.setBackground(getResources().getDrawable(R.drawable.button_line));

                binding.tvPendings.setTextColor(getResources().getColor(R.color.black));
                binding.tvAccepted.setTextColor(getResources().getColor(R.color.black));
                binding.tvRejected.setTextColor(getResources().getColor(R.color.black));
                binding.tvcurrent.setTextColor(getResources().getColor(R.color.black));
                binding.tvCompleted.setTextColor(getResources().getColor(R.color.white));


                binding.tvStatus.setText(getResources().getString(R.string.com));
                parms.put(Consts.BOOKING_FLAG, "4");
                artistBookingsListnew = new ArrayList<>();
                current_page = 0;
                getBookings(current_page);
                break;

        }

    }


    public void opencamrea() {


        if (!hasPermissions(getActivity(), permissions)) {
            ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            getPhotoFromCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CAMERA_ACCEPTED, cameraAccepted);

                    storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.MODIFY_AUDIO_ACCEPTED, accessNetState);

                    fineLoc = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.FINE_LOC, fineLoc);

                    corasLoc = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    prefference.setBooleanValue(Consts.CORAS_LOC, corasLoc);
                    getPhotoFromCamera();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void getPhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);


        File file = new File(Environment.getExternalStorageDirectory(), "OkyBookPartner" + String.valueOf(System.currentTimeMillis()) + ".png");
        disableException();
        uri = Uri.fromFile(file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);

    }

    public void disableException() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
            } else if (requestCode == 3) {
                // Uri imageUri = data.getData();


                String filepath = FileUtility.getPath(getActivity(), uri);
                FileUtility fileUtility = new FileUtility();
                filepath = fileUtility.compressImage(getActivity(), filepath);
                File file = new File(filepath);
                paramsFile.put(Consts.IMAGE, file);
                AdapterAllBookings.img_upload.setVisibility(View.VISIBLE);
                AdapterAllBookings.img_upload.setImageURI(uri);


            }


        } else {

        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


}
