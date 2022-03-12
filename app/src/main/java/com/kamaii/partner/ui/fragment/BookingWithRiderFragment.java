package com.kamaii.partner.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentBookingWithRiderBinding;
import com.kamaii.partner.databinding.FragmentCabBookingsBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.MyService;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.AdapterBookingWithRider;
import com.kamaii.partner.ui.adapter.AdapterBookingWithRider;
import com.kamaii.partner.ui.models.AddressModel;
import com.kamaii.partner.utils.FileUtility;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.turf.TurfConversion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static freemarker.template.utility.StringUtil.capitalize;

public class BookingWithRiderFragment extends Fragment {

    private BaseActivity baseActivity;
    private FragmentBookingWithRiderBinding binding;
    private ArrayList<ArtistBooking> artistBookingsList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private String TAG = FragmentBookingWithRiderBinding.class.getSimpleName();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> parmsartist = new HashMap<>();
    public static HashMap<String, File> paramsFile;
    private HashMap<String, String> paramsUpdate;
    private HashMap<String, String> paramsDecline;
    private LayoutInflater myInflater;
    private AdapterBookingWithRider adapterAllBookings;
    Context mContext;
    public static Uri uri = null;
    private ArtistDetailsDTO artistDetailsDTO;
    private String bookingid = "";
    String currentdate = "";
    LocalBroadcastManager bm;
    private SharedPreferences firebase;
    String devicetoken = "";
    ProgressDialog progressDialog;
    String dcatid = "", dsubcatid = "", dthirdcatid = "", dclatititute = "", dclongitiute = "";
    CountDownTimer cT;
    private GoogleMap googleMap;
    boolean accept = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // View view =  inflater.inflate(R.layout.fragment_booking_with_rider, container, false);

        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_with_rider, container, false);
        prefrence = SharedPrefrence.getInstance(getActivity());
        firebase = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        binding.mapView.onCreate(savedInstanceState);
        paramsDecline = new HashMap<>();
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        devicetoken = firebase.getString(Consts.DEVICE_TOKEN, "");
        myInflater = LayoutInflater.from(getActivity());
        parmsartist.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parmsartist.put(Consts.USER_ID, userDTO.getUser_id());
        parmsartist.put(Consts.DEVICE_TOKEN, devicetoken);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        checkaddress();
        binding.pulsator.start();

        Date todayyyy = new Date();
        SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        currentdate = formatt.format(todayyyy);

        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);

        binding.mapView.onResume();
        binding.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location buttont
                // googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                Location location = googleMap.getMyLocation();

                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
                    //googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newCameraPosition(cameraPosition));

                } else {

                    if (!prefrence.getValue(Consts.LATITUDE).equalsIgnoreCase("")) {
                        LatLng soruce = new LatLng(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)));

                        if (soruce != null) {
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(soruce).zoom(14).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }


                }


            }
        });
        baseActivity.headerNameTV.setText("fhvbjhabvahbvdhjad");
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        binding.rvBooking.setLayoutManager(mLayoutManager);

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


                }
                return false;
            }
        });
        if (NetworkManager.isConnectToInternet(getActivity())) {


            getBookings();


        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_concation), LENGTH_SHORT).show();

        }


        mContext = getActivity();


        paramsFile = new HashMap<>();

        getArtist("0");


        baseActivity.swOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    if (artistDetailsDTO != null) {

                        paramsUpdate = new HashMap<>();
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        if (artistDetailsDTO.getCab_booking_flag().equalsIgnoreCase("1")) {
                            Toast.makeText(getActivity(), "Your booking is running", Toast.LENGTH_SHORT).show();

                            baseActivity.swOnOff.setChecked(true);
                            return;
                        } else {

                            if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                                paramsUpdate.put(Consts.IS_ONLINE, "0");
                                isOnline("0");
                            } else {
                                paramsUpdate.put(Consts.IS_ONLINE, "1");
                                isOnline("1");
                            }
                        }


                    } else {
                        baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
                        binding.txtoffonline.setText("You're Offline");
                        baseActivity.swOnOff.setChecked(false);
                        baseActivity.tvOnOff.setTextColor(Color.WHITE);
                        binding.layfindtrip.setVisibility(View.GONE);
                        binding.txtoffonline.setTextColor(Color.BLACK);
                        binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));
                        Toast.makeText(getActivity(), getResources().getString(R.string.incomplete_profile_msg), Toast.LENGTH_SHORT).show();


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


        binding.llDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                accept = true;
                decline(bookingid);
            }
        });

        binding.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().stopService(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                booking("1");
            }
        });
        return binding.getRoot();
    }


    public void booking(String req) {


        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.booking_operation(bookingid, req, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        //  Log.e("ACCEPT_RES",""+s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            binding.layaccpet.setVisibility(View.GONE);

                            accept = true;
                            getBookingscopy("0");


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
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


    public void getArtistcpoy() {


        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getArtistByid(userDTO.getUser_id(), userDTO.getUser_id(), devicetoken);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        //   Log.e("Artist_copy",""+s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
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


    public void getArtist(String value) {

        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getArtistByid(userDTO.getUser_id(), userDTO.getUser_id(), devicetoken);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistDetailsDTO = new Gson().fromJson(object.getJSONObject("data").toString(), ArtistDetailsDTO.class);


                                if (value.equalsIgnoreCase("0")) {
                                    showDatfirst();

                                } else {
                                    showDataddd();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
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
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }
    public void isOnline(String onoff) {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.onlineOffline(userDTO.getUser_id(), onoff,devicetoken,getDeviceName());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            getArtist("1");


                        } else {


                            baseActivity.swOnOff.setChecked(false);
                            Toast.makeText(getActivity(), message,
                                    LENGTH_LONG).show();
                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
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


    public void getBookings() {

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCab(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();


                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        Log.e("getBookings", "" + responseBody.toString());
                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistBookingsList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                                Collections.reverse(artistBookingsList);

                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {

                        }


                    } else {

                        Toast.makeText(getActivity(), "Try Again Later ",
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


    @Override
    public void onPause() {
        super.onPause();
    }


    public void showData() {


        if (artistBookingsList.size() == 0) {

            binding.tvNo.setVisibility(View.VISIBLE);
            binding.rvBooking.setVisibility(View.GONE);
            binding.rlSearch.setVisibility(View.GONE);
            binding.mapView.setVisibility(View.VISIBLE);
            binding.lay.setVisibility(View.VISIBLE);
            binding.pulsator.setVisibility(View.VISIBLE);

            if (artistDetailsDTO != null) {
                if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                    binding.pulsator.setVisibility(View.VISIBLE);
                } else {
                    binding.pulsator.setVisibility(View.GONE);
                }
            }
        } else {
            for (int i = 0; i < artistBookingsList.size(); i++) {

                if (artistBookingsList.get(0).getBooking_flag().equalsIgnoreCase("0")) {
                    binding.layaccpet.setVisibility(View.VISIBLE);
                    binding.lay.setVisibility(View.GONE);
                    try {
                        bookingid = artistBookingsList.get(0).getId();
                        int seocndserver = Integer.parseInt(artistBookingsList.get(0).getSecond());
                        int timer = Integer.parseInt(artistBookingsList.get(0).getStarttimer());
                        if (seocndserver < timer) {
                            seocndserver = timer - seocndserver;
                            long ssss = seocndserver * 1000;


                            cT = new CountDownTimer(ssss, 1000) {

                                public void onTick(long millisUntilFinished) {

                                    int va = (int) ((millisUntilFinished % 60000) / 1000);

                                    binding.txtarivaltimer.setText("" + String.format("%02d", va));

                                }

                                public void onFinish() {


                                }
                            };
                            cT.start();


                            if (checkarss(artistBookingsList.get(0).getProduct().get(0).getCategory_id())) {
                                dcatid = artistBookingsList.get(0).getProduct().get(0).getCategory_id();
                                dsubcatid = artistBookingsList.get(0).getProduct().get(0).getSub_category_id();
                                dthirdcatid = artistBookingsList.get(0).getProduct().get(0).getSublevel_category();
                                dclatititute = artistBookingsList.get(0).getC_latitude();
                                dclongitiute = artistBookingsList.get(0).getC_longitude();

                                binding.tvLocation.setText(artistBookingsList.get(0).getAddress());

                                if (dsubcatid.equalsIgnoreCase("453") || dsubcatid.equalsIgnoreCase("242") || dsubcatid.equalsIgnoreCase("455")) {
                                    binding.tvroundtrip.setVisibility(View.GONE);
                                } else {

                                    if (artistBookingsList.get(0).getProduct().get(0).getRoundtrip().equalsIgnoreCase("1")) {
                                        binding.tvroundtrip.setVisibility(View.VISIBLE);
                                        binding.tvroundtrip.setText("Round Trip");
                                    } else if (artistBookingsList.get(0).getProduct().get(0).getRoundtrip().equalsIgnoreCase("0")) {
                                        binding.tvroundtrip.setVisibility(View.VISIBLE);
                                        binding.tvroundtrip.setText("One Way");
                                    } else {
                                        binding.tvroundtrip.setVisibility(View.GONE);
                                    }

                                }


                            } else {

                                dcatid = artistBookingsList.get(0).getProduct().get(0).getCategory_id();
                                dsubcatid = artistBookingsList.get(0).getProduct().get(0).getSub_category_id();
                                dthirdcatid = artistBookingsList.get(0).getProduct().get(0).getSublevel_category();
                                dclatititute = artistBookingsList.get(0).getC_latitude();
                                dclongitiute = artistBookingsList.get(0).getC_longitude();
                                String locationstatus = artistBookingsList.get(0).getLocation_status();
                                if (locationstatus.equalsIgnoreCase("1")) {
                                    binding.tvroundtrip.setVisibility(View.VISIBLE);
                                    binding.tvroundtrip.setText("Delivery");
                                } else if (locationstatus.equalsIgnoreCase("0")) {
                                    binding.tvroundtrip.setVisibility(View.VISIBLE);
                                    binding.tvroundtrip.setText("Pick Up");
                                } else {
                                    binding.tvroundtrip.setVisibility(View.GONE);
                                }
                                binding.tvLocation.setText(artistBookingsList.get(0).getAddress());
                            }

                            binding.txtcat.setText(artistBookingsList.get(0).getCategory_name());

                        } else {


                        }

                    } catch (Exception e) {

                    }

                } else {

                }
            }
            binding.rvBooking.setVisibility(View.VISIBLE);
            binding.pulsator.setVisibility(View.GONE);
            binding.mapView.setVisibility(View.GONE);
            binding.lay.setVisibility(View.GONE);
            adapterAllBookings = new AdapterBookingWithRider(BookingWithRiderFragment.this, artistBookingsList, userDTO, myInflater);
            binding.rvBooking.setAdapter(adapterAllBookings);

        }


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

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void decline(String bid) {

        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, bid);
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, "");
        paramsDecline.put("cat_id", dcatid);
        paramsDecline.put("sub_id", dsubcatid);
        paramsDecline.put("third_id", dthirdcatid);
        paramsDecline.put("lat", dclatititute);
        paramsDecline.put("lang", dclongitiute);
        paramsDecline.put("passvalue", "0");

        progressDialog.show();

        new HttpsRequest(Consts.DECLINE_BOOKING_ON_BTN_CLICK, paramsDecline, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {

                    binding.rvBooking.setVisibility(View.GONE);


                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();

                        AssetFileDescriptor descriptor = getActivity().getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();


                        binding.layaccpet.setVisibility(View.GONE);


                        getBookingscopy("3");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {


                }


            }
        });
    }

    public void showDatfirst() {

        if (artistDetailsDTO != null) {
            baseActivity.swOnOff.setVisibility(View.GONE);
            baseActivity.tvOnOff.setVisibility(View.GONE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText("You're Online");
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));
            // binding.pulsator.start();
            binding.pulsator.setVisibility(View.VISIBLE);

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText("You're Offline");
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            binding.pulsator.setVisibility(View.GONE);

            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }

    public void showDataddd() {

        if (artistDetailsDTO != null) {
            baseActivity.swOnOff.setVisibility(View.GONE);
            baseActivity.tvOnOff.setVisibility(View.GONE);
        }

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            binding.lay.setVisibility(View.VISIBLE);
            binding.layfindtrip.setVisibility(View.VISIBLE);

            baseActivity.tvOnOff.setText(getResources().getString(R.string.online));
            binding.txtoffonline.setText("You're Online");
            prefrence.setValue(Consts.IS_ONLINE, "1");
            binding.txtoffonline.setTextColor(Color.parseColor("#18750c"));
            baseActivity.tvOnOff.setTextColor(Color.GREEN);

            baseActivity.swOnOff.setChecked(true);
            binding.layfindtrip.setVisibility(View.VISIBLE);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.lightgreenthree));

            // binding.pulsator.start();
            binding.pulsator.setVisibility(View.VISIBLE);
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

            //baseActivity.swOnOff.colo(getResources().getColor(R.color.white));

        } else {
            prefrence.setValue(Consts.IS_ONLINE, "0");
            binding.lay.setVisibility(View.VISIBLE);
            baseActivity.tvOnOff.setText(getResources().getString(R.string.offline));
            binding.txtoffonline.setText("You're Offline");
            baseActivity.swOnOff.setChecked(false);
            baseActivity.tvOnOff.setTextColor(Color.WHITE);
            binding.layfindtrip.setVisibility(View.GONE);
            binding.txtoffonline.setTextColor(Color.BLACK);
            binding.layonline.setBackgroundColor(getResources().getColor(R.color.textcolor));

            // binding.pulsator.stop();
            binding.pulsator.setVisibility(View.GONE);
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
            // baseActivity.swOnOff.setHighlightColor(getResources().getColor(R.color.white));
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bm = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter actionReceiver = new IntentFilter();
        actionReceiver.addAction("nameOfTheActionShouldBeUniqueName");
        bm.registerReceiver(onJsonReceived, actionReceiver);
    }


    private BroadcastReceiver onJsonReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {

                getBookings();


            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        bm.unregisterReceiver(onJsonReceived);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void opencamrea() {


        ImagePicker.Companion.with(getActivity())
                .crop()
                .cameraOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(23);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

            } else if (requestCode == 3) {
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");

                uri = getImageUri(getActivity(), profilePicture);
                String filepath = FileUtility.getPath(getActivity(), uri);
                FileUtility fileUtility = new FileUtility();
                filepath = fileUtility.compressImage(getActivity(), filepath);
                File file = new File(filepath);
                paramsFile.put(Consts.IMAGE, file);
                AdapterBookingWithRider.img_upload.setVisibility(View.VISIBLE);
                AdapterBookingWithRider.img_upload.setImageURI(uri);


            }
            if (requestCode == 23) {


                Uri imageUri = data.getData();

                String path = FileUtility.getPath(getActivity(), imageUri);
                File file = new File(path);
                paramsFile.put(Consts.IMAGE, file);
                AdapterBookingWithRider.img_upload.setVisibility(View.VISIBLE);
                AdapterBookingWithRider.img_upload.setImageURI(imageUri);
            }


        } else {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void getBookingscopy(final String position) {
        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getAllBookingArtistCab(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                binding.tvNo.setVisibility(View.GONE);
                binding.rvBooking.setVisibility(View.VISIBLE);
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        //   Log.e("booking_copy",""+s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                artistBookingsList = new ArrayList<>();
                                Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                                }.getType();
                                artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);


                                Collections.reverse(artistBookingsList);


                                getArtistcpoy();


                                showData();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            artistBookingsList = new ArrayList<>();

                            getArtistcpoy();


                            showData();

                        }


                    } else {

                        binding.rvBooking.setVisibility(View.GONE);
                        binding.rlSearch.setVisibility(View.GONE);
                        binding.lay.setVisibility(View.VISIBLE);

                        Toast.makeText(getActivity(), "Try Again Later ",
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

    public void checkaddress() {
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.getMapCategory(userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                //    Log.e("RES", response.message());
                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        String status = object.getString("status");
                        BaseActivity.addressModelArrayList = new ArrayList<>();

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonElements = object.getJSONArray("data");
                            for (int i = 0; i < jsonElements.length(); i++) {
                                JSONObject obj = jsonElements.getJSONObject(i);


                                String id = obj.getString("id");
                                String cat_id = obj.getString("cat_id");
                                String statuss = obj.getString("status");


                                BaseActivity.addressModelArrayList.add(new AddressModel(id, cat_id, statuss));

                            }
                        } else {

                        }
                    } else {
                        Toast.makeText(getActivity(), "Try again. Server is not responding",
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


            }
        });
    }


}
