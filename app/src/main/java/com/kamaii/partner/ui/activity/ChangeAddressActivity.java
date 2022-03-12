package com.kamaii.partner.ui.activity;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityChangeAddressBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.preferences.SharedPrefrence;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChangeAddressActivity extends AppCompatActivity implements View.OnClickListener {

    FusedLocationProviderClient fusedLocationProviderClient;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    String house_no_str = "", society_name_str = "", street_address_str = "";
    public static String final_user_address = "";
    String landmark_name = "";
    double lats = 0.0;
    double longs = 0.0;
    String address_radio_txt = "Home";
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    String live_address_str = "";
    ActivityChangeAddressBinding binding;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    public static int AUTOCOMPLETE_REQUEST_CODE = 10;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ChangeAddressActivity.this, R.layout.activity_change_address);
        prefrence = SharedPrefrence.getInstance(ChangeAddressActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(ChangeAddressActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());

        binding.addAddressSubmit.setOnClickListener(this);
        binding.changeAddressBtn.setOnClickListener(this);
        getMyLocation();
    }

    private void getMyLocation() {

        Log.e("Live_ADDRESS", "getmylocation called");

        if (ActivityCompat.checkSelfPermission(ChangeAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ChangeAddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location != null) {
                    //Toast.makeText(mContext, "2", Toast.LENGTH_LONG).show();

                    live_latitude = location.getLatitude();
                    Log.e("Live_ADDRESS", "" + live_latitude);
                    live_longitude = location.getLongitude();
                    Log.e("Live_ADDRESS", "" + live_longitude);

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        // Toast.makeText(mContext, "8", Toast.LENGTH_LONG).show();

                        List<Address> addresses = geocoder.getFromLocation(live_latitude, live_longitude, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        add = add + "\n" + obj.getCountryName();
                        add = add + "\n" + obj.getCountryCode();
                        add = add + "\n" + obj.getAdminArea();
                        add = add + "\n" + obj.getPostalCode();
                        add = add + "\n" + obj.getSubAdminArea();
                        add = add + "\n" + obj.getLocality();
                        add = add + "\n" + obj.getSubThoroughfare();

                        if (live_address_str.equals("")) {

                            live_address_str = obj.getAddressLine(0);
                            binding.locationEtx.setText(live_address_str);
                            addAddressHashmap.put("street_address", live_address_str);
                            addAddressHashmap.put(Consts.LATITUDE, String.valueOf(live_latitude));
                            addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(live_longitude));
                        }
                        Log.e("Live_ADDRESS", "" + live_address_str);
                        //   Log.e("IGA123214", "Address" + add);
                        // Toast.makeText(this, "Address=>" + add,
                        // Toast.LENGTH_SHORT).show();

                        // TennisAppActivity.showDialog(add);

                        //  etLocationD.setText(obj.getAddressLine(0));
                        //  drag_location_etx.setText(obj.getAddressLine(0));
                        // parms.put(Consts.LOCATION, obj.getAddressLine(0));


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_address_submit:
                getSubmitAddress();
                break;

            case R.id.change_address_btn:
                findPlace();
                break;



        }
    }

    private void getSubmitAddress() {

        house_no_str = binding.houseNoEtx.getText().toString();
        society_name_str = binding.societyNameEtx.getText().toString();
        street_address_str = binding.locationEtx.getText().toString();

        if (house_no_str.equalsIgnoreCase("") || house_no_str.isEmpty() || house_no_str.equals(null)) {
            Toast.makeText(ChangeAddressActivity.this, "Please enter House no. / Flat no. / Floor / Building", Toast.LENGTH_SHORT).show();
            return;
        } else {

            addAddressHashmap.put("house_no", house_no_str);
            addAddressHashmap.put("society_name", society_name_str);
            addAddress();
            //binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue_color));

            //etLocationD.setText(house_no_str + ", " + society_name_str + ", " + street_address_str);

        }
    }

    public void addAddress() {

        progressDialog.show();
        addAddressHashmap.put("addresstype", address_radio_txt);
        Log.e("Live_ADDRESS", "" + address_radio_txt);

        Log.e("addAddressHashmap", "" + addAddressHashmap.toString());
        new HttpsRequest(Consts.UPDATE_ADD_ADDRESS, addAddressHashmap, ChangeAddressActivity.this).stringPost("TAG", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    startActivity(new Intent(ChangeAddressActivity.this,BaseActivity.class));
                    finish();
                    Log.e("ADD_ADDRESS_API", "" + response.toString());
                } else {

                }
            }
        });
    }

    private void findPlace() {

        Intent intentt = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setCountry("IN")
                .build(ChangeAddressActivity.this);

        startActivityForResult(intentt, AUTOCOMPLETE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            if (requestCode == AUTOCOMPLETE_REQUEST_CODE){

                Log.e("PLACE_APU", "called");
                Place place = Autocomplete.getPlaceFromIntent(data);

                final_user_address = place.getAddress();
                landmark_name = place.getName();


                lats = place.getLatLng().latitude;
                longs = place.getLatLng().longitude;


                addAddressHashmap.put("street_address", landmark_name + final_user_address);
                addAddressHashmap.put(Consts.LATITUDE, String.valueOf(lats));
                addAddressHashmap.put(Consts.LONGITUDE, String.valueOf(longs));

                binding.locationEtx.setText(landmark_name + ", " + place.getAddress());

            }
        }
    }
}