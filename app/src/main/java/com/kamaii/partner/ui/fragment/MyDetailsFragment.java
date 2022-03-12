package com.kamaii.partner.ui.fragment;

import static android.app.Activity.RESULT_OK;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentMyDetailsBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.ChangeAddressActivity;
import com.kamaii.partner.ui.activity.EditPersnoalInfo;
import com.kamaii.partner.ui.adapter.AddressBottomSheetAdapter;
import com.kamaii.partner.ui.models.newbooking.ArtistProfileDtoNew;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MyDetailsFragment extends Fragment implements View.OnClickListener {

    BottomSheetDialog addressBottomSheet;
    CustomTextView no_rider_found_txt, change_address_btn;
    CustomTextViewBold address_submit;
    CustomEditText location_etx, house_no_etx, society_name_etx;
    String house_no_str = "", society_name_str = "", street_address_str = "";
    LinearLayout customAddress;
    LinearLayout address_lay_dest_location;
    RelativeLayout address_relative;
    int address_adapter_counter = 0;
    HashMap<String, String> addAddressHashmap = new HashMap<>();
    RadioGroup add_addressradioGroup;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    public static int AUTOCOMPLETE_REQUEST_CODE = 10;
    List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);
    private static final String API_KEY = "AIzaSyCIpO2XuPQr8HDOg92EU0pRkP0G9rKvFoY";
    ArtistProfileNewFragment artistProfileNewFragment;
    public static String final_user_address = "";
    String landmark_name = "";
    double lats = 0.0;
    double longs = 0.0;
    BaseActivity baseActivity;

    String address_radio_txt = "Home";
    FusedLocationProviderClient fusedLocationProviderClient;
    double live_latitude = 0.0;
    double live_longitude = 0.0;
    String live_address_str = "";
    private ArtistProfileDtoNew artistDetailsDTO;
    private HashMap<String, String> parms = new HashMap<>();
    FragmentMyDetailsBinding binding;
    private Bundle bundle;
    ProgressDialog progressDialog;
    private HashMap<String, String> params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        addressBottomSheet = new BottomSheetDialog(getContext());
        addressBottomSheet.setContentView(R.layout.activity_address_bottom_dialog);
        change_address_btn = addressBottomSheet.findViewById(R.id.change_address_btn);
        location_etx = addressBottomSheet.findViewById(R.id.location_etx);
        house_no_etx = addressBottomSheet.findViewById(R.id.house_no_etx);
        society_name_etx = addressBottomSheet.findViewById(R.id.society_name_etx);
        customAddress = addressBottomSheet.findViewById(R.id.customAddress);
        add_addressradioGroup = addressBottomSheet.findViewById(R.id.add_addressradioGroup);
        address_submit = addressBottomSheet.findViewById(R.id.address_submit);
        address_relative = addressBottomSheet.findViewById(R.id.address_relative);
        address_lay_dest_location = addressBottomSheet.findViewById(R.id.address_lay_dest_location);
        addAddressHashmap.put(Consts.USER_ID, userDTO.getUser_id());
        Places.initialize(getContext(), API_KEY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        bundle = this.getArguments();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        artistDetailsDTO = (ArtistProfileDtoNew) bundle.getSerializable(Consts.ARTIST_DTO);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_details, container, false);
        binding.mydetailChangeAddressBtn.setOnClickListener(this);
        binding.updateProfile.setOnClickListener(this);

        artistProfileNewFragment = ((ArtistProfileNewFragment)MyDetailsFragment.this.getParentFragment());

        return binding.getRoot();
    }

    private void getMyLocation() {

        Log.e("Live_ADDRESS", "getmylocation called");

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            location_etx.setText(live_address_str);
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
    public void onResume() {
        super.onResume();

        showDataFirst();
    }


    private void showDataFirst() {

        binding.fullName.setText(artistDetailsDTO.getName());
        if (!artistDetailsDTO.getLandmark().equalsIgnoreCase("")) {

            binding.landmarkName.setText(Html.fromHtml("<b>Landmark : </b>")+artistDetailsDTO.getLandmark());
        } else {
            binding.landmarkName.setVisibility(View.GONE);
        }

        if (!artistDetailsDTO.getHouseNo().equalsIgnoreCase("")) {

            binding.blockNo.setText(Html.fromHtml("<b>Block No : </b>")+artistDetailsDTO.getHouseNo());
        } else {
            binding.blockNo.setVisibility(View.GONE);
        }

        binding.tvLocation.setText(Html.fromHtml("<b>Address : </b>")+artistDetailsDTO.getStretaddress());


        binding.mobileNo.setText(artistDetailsDTO.getMobile());
        binding.emailTxt.setText(artistDetailsDTO.getEmail());

        if (artistDetailsDTO.getGender().equalsIgnoreCase("0")) {
            binding.rbGenderM.setChecked(false);
            binding.rbGenderF.setChecked(true);
            binding.rbGenderO.setChecked(false);
        } else if (artistDetailsDTO.getGender().equalsIgnoreCase("1")) {
            binding.rbGenderM.setChecked(true);
            binding.rbGenderF.setChecked(false);
            binding.rbGenderO.setChecked(false);
        } else {
            binding.rbGenderM.setChecked(false);
            binding.rbGenderF.setChecked(false);
            binding.rbGenderO.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mydetail_change_address_btn:
                startActivity(new Intent(getActivity(), ChangeAddressActivity.class));
                //showAddressDialog();
                break;
            case R.id.update_profile:
                submitPersonalProfile();
                //showAddressDialog();
                break;
        }
    }

    public void submitPersonalProfile() {
        // Toast.makeText(mContext, ""+user_profile_uri_str, Toast.LENGTH_SHORT).show();
        //


        if (NetworkManager.isConnectToInternet(getContext())) {



            if (binding.fullName.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Please enter your name!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.emailTxt.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Please enter your email id!!", Toast.LENGTH_LONG).show();
                return;
            }else if (!ProjectUtils.isEmailValid(binding.emailTxt.getText().toString())) {
                Toast.makeText(getContext(), "Please enter valid email id!!", Toast.LENGTH_LONG).show();
                return;
            }
            else {

                params = new HashMap<>();
                params.put(Consts.ARTIST_ID, userDTO.getUser_id());
                params.put(Consts.NAME, ProjectUtils.getEditTextValue(binding.fullName));
                params.put(Consts.EMAIL, ProjectUtils.getEditTextValue(binding.emailTxt));

                if (binding.rbGenderF.isChecked()) {
                    params.put(Consts.GENDER, "female");
                } else if (binding.rbGenderM.isChecked()) {
                    params.put(Consts.GENDER, "male");
                } else {
                    params.put(Consts.GENDER, "others");
                }
                uploadData();

            }


        } else {
            ProjectUtils.showToast(getContext(), getResources().getString(R.string.internet_concation));
        }

    }

    private void uploadData() {
        progressDialog.show();
        new HttpsRequest(Consts.UPDATE_ARTIST_PROFILE_DATA,params,getContext()).stringPosttwo(Consts.UPDATE_ARTIST_PROFILE_DATA, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
               progressDialog.dismiss();
                if (flag){
                    Log.e("UPDATE_ARTIST res",""+response.toString());
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    baseActivity.loadHomeFragment(new ArtistProfileNewFragment(),"new");

                }else {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;

    }
}