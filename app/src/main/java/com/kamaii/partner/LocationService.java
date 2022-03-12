package com.kamaii.partner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.newlocation.SensorRestarterBroadcastReceiver;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.LocationUpdatesService;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.CheckSignInActivity;
import com.kamaii.partner.ui.activity.OtpActivity;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class LocationService extends Service {

    //private LocationListener listener;
    private LocationManager locationManager;

    HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private String TAG = "tagg";
    double latitude;
    double longitute;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    //    private static final int LOCATION_INTERVAL = 100;
    private static final float LOCATION_DISTANCE = 3f;

    LocationUpdatesService locationUpdatesService;

    public LocationService(Context applicationContext) {
        super();
        Log.e("HERE", "here I am!");
    }

    public LocationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

         Log.e("SHIVAKOSHTI", "CHECKING");
        return null;
    }

    private UserDTO userDTO;

    public int counter = 0;

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = (earthRadius * c);

        return dist; // output distance, in MILES
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             Log.e("SHIVAKOSHTI", "CHECKING1111");
            prefrence = SharedPrefrence.getInstance(getApplicationContext());
            userDTO = prefrence.getParentUser(Consts.USER_DTO);
            // locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

       /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Log.e("SHIVAKOSHTI", "CHECKING11234");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, listener);*/
            initializeLocationManager();

            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.PASSIVE_PROVIDER,
                        LOCATION_INTERVAL,
                        LOCATION_DISTANCE,
                        mLocationListeners[0]
                );
            } catch (java.lang.SecurityException ex) {
                // Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                // Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
        } else {
            startForeground(1, new Notification());
        }
        Log.e("SHIVAKOSHTI", "CHECKING1111");
        prefrence = SharedPrefrence.getInstance(getApplicationContext());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        // locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

       /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Log.e("SHIVAKOSHTI", "CHECKING11234");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, listener);*/
        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            // Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            // Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
    }

    public void updateLocation() {
        new HttpsRequest(Consts.UPDATE_LOCATION_API, parms, getApplicationContext()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ///  ProjectUtils.showToast(getApplicationContext(), "CHanging");
                } else {
                    ///  ProjectUtils.showToast(getApplicationContext(), msg);
                }
            }
        });
        ///   Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
//        sendotp();
    }

    public void updatelocationnew(String user_id, String s, String s1, String s2) {
       // Log.e("SHIVAM", "lat" + s1 + "->" + s2);
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.updateLocation(user_id, s, s1, s2);
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
                           // Log.e("SHIVAM", message + "SHIVAM");
                            //ProjectUtils.showToast(getApplicationContext(), message + "SHIVAM");
                        }

                    } else {
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


      /*  ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.SEND_OTP, getparmtwo(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
              //  ProjectUtils.showToast(mContext, msg);
                ProjectUtils.pauseProgressDialog();
                if (flag)
                {
                    try {

                        JSONObject jsonObject=response.getJSONObject("data");
                        String user_id=  jsonObject.getString("user_id");
                        String mobile= jsonObject.getString("mobile");
                        String otp  =jsonObject.getString("otp");
                        //  ProjectUtils.showToast(mContext, msg);


                      // overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    showSickbar(msg);
                   // ProjectUtils.showToast(mContext, msg);
                }


            }
        });*/
    }


    @Override
    public void onDestroy() {
      //  Log.e("SHIVAKOSHTI", "onDestroy");
        super.onDestroy();

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
                    sendBroadcast(broadcastIntent);
                    stoptimertask();
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    // Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++) + "*****" + latitude + "->" + longitute + "");
                initializeLocationManager();
                updatelocationnew(userDTO.getUser_id(), "3", latitude + "", longitute + "");
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
   /*public LocationListener  listener=new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location) {
        double latitude=location.getLatitude();
        double longitute=location.getLongitude();

        Log.e("Service_lat",""+latitude);
        Log.e("Service_lat2",""+longitute);
        if(prefrence.getValue(Consts.LATITUDE).equalsIgnoreCase("")){
            prefrence.setValue(Consts.LATITUDE, latitude + "");
            prefrence.setValue(Consts.LONGITUDE, longitute + "");


            parms.put(Consts.USER_ID, userDTO.getUser_id());
            parms.put(Consts.ROLE, "1");
            parms.put(Consts.LATITUDE, latitude + "");
            parms.put(Consts.LONGITUDE, longitute + "");

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitute, 1);
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                Log.e("IGA", "Address" + add);
                // Toast.makeText(this, "Address=>" + add,
                // Toast.LENGTH_SHORT).show();

                // TennisAppActivity.showDialog(add);


                parms.put(Consts.LOCATION, obj.getAddressLine(0));


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            updateLocation();
        }

        try {

            double test=distance(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)), latitude, longitute);


            if (test> 0.05)
            { // if distance < 0.1 miles we take locations as equal
                prefrence.setValue(Consts.LATITUDE, latitude + "");
                prefrence.setValue(Consts.LONGITUDE, longitute + "");
                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.ROLE, "1");
                parms.put(Consts.LATITUDE, latitude + "");
                parms.put(Consts.LONGITUDE, longitute + "");

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitute, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();
                    Log.e("IGA", "Address" + add);
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();

                    // TennisAppActivity.showDialog(add);


                    parms.put(Consts.LOCATION, obj.getAddressLine(0));


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                updateLocation();
            }
        }catch (Exception e){

        }

    }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

    }

        @Override
        public void onProviderEnabled(String provider) {

    }

        @Override
        public void onProviderDisabled(String provider) {
        Intent i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }
    };*/

    private void initializeLocationManager() {
     //   Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            // Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            //  Toast.makeText(getApplicationContext(), "JAY HANUMAN", Toast.LENGTH_SHORT).show();
            // Log.e(TAG, "onLocationChanged: " + location);
            //  Log.e(TAG, "getLatitude: " + location.getLatitude());
            //  Log.e(TAG, "getLongitude: " + location.getLongitude());
            mLastLocation.set(location);
            latitude = location.getLatitude();
            longitute = location.getLongitude();
//            updatelocationnew(userDTO.getUser_id(), "1", latitude + "", longitute + "");
          //  Log.e("SHIVAM", "" + latitude);
         //   Log.e("SHIVAM", "" + longitute);
            if (prefrence.getValue(Consts.LATITUDE).equalsIgnoreCase("")) {
                prefrence.setValue(Consts.LATITUDE, latitude + "");
                prefrence.setValue(Consts.LONGITUDE, longitute + "");


                parms.put(Consts.USER_ID, userDTO.getUser_id());
                parms.put(Consts.ROLE, "1");
                parms.put(Consts.LATITUDE, latitude + "");
                parms.put(Consts.LONGITUDE, longitute + "");


                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitute, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getSubThoroughfare();
                    //      Log.e("IGA", "Address" + add);
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();

                    // TennisAppActivity.showDialog(add);


                    parms.put(Consts.LOCATION, obj.getAddressLine(0));


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                updatelocationnew(userDTO.getUser_id(), "1", latitude + "", longitute + "");
//                updateLocation();
            }

            try {
                double test = distance(Double.parseDouble(prefrence.getValue(Consts.LATITUDE)), Double.parseDouble(prefrence.getValue(Consts.LONGITUDE)), latitude, longitute);
                if (test > 0.05) { // if distance < 0.1 miles we take locations as equal
                //    Log.e("location", "location_Called");
                    prefrence.setValue(Consts.LATITUDE, latitude + "");
                    prefrence.setValue(Consts.LONGITUDE, longitute + "");
                    parms.put(Consts.USER_ID, userDTO.getUser_id());
                    parms.put(Consts.ROLE, "1");
                    parms.put(Consts.LATITUDE, latitude + "");
                    parms.put(Consts.LONGITUDE, longitute + "");

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitute, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        add = add + "\n" + obj.getCountryName();
                        add = add + "\n" + obj.getCountryCode();
                        add = add + "\n" + obj.getAdminArea();
                        add = add + "\n" + obj.getPostalCode();
                        add = add + "\n" + obj.getSubAdminArea();
                        add = add + "\n" + obj.getLocality();
                        add = add + "\n" + obj.getSubThoroughfare();
                        //   Log.e("IGA", "Address" + add);
                        // Toast.makeText(this, "Address=>" + add,
                        // Toast.LENGTH_SHORT).show();

                        // TennisAppActivity.showDialog(add);


                        parms.put(Consts.LOCATION, obj.getAddressLine(0));


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    updatelocationnew(userDTO.getUser_id(), "1", latitude + "", longitute + "");
//                    updateLocation();
                }
            } catch (Exception e) {

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            //  Log.e(TAG, "onProviderDisabled: " + provider);
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //  Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
}
