package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.util.FileUtil;
import com.google.gson.reflect.TypeToken;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BirthdayCardActivity;
import com.kamaii.partner.ui.activity.BusinessCardActivity;
import com.kamaii.partner.ui.activity.ContactListActivity;
import com.kamaii.partner.ui.activity.GalleryActivity;
import com.kamaii.partner.ui.activity.MySqliteHelper;
import com.kamaii.partner.ui.models.ContactModel;
import com.mapbox.android.core.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;

import static android.app.Activity.RESULT_OK;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;


public class PromotionFragment extends Fragment {
    private String TAG = PromotionFragment.class.getSimpleName();
    private BaseActivity baseActivity;
    MySqliteHelper mySqliteHelper = new MySqliteHelper();
    SQLiteDatabase db;
    //  private HashMap<String, String> paramsGetMsg = new HashMap<>();
    ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> params = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.promotion));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivEditPersonal.setVisibility(View.GONE);

        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        String str = userDTO.getName().trim();
        params.put(Consts.ARTIST_PROFILE_LINK, "https://kamaii.in/" + str.trim() + "/" + userDTO.getUser_id());
        //paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());

        view.findViewById(R.id.share_profile_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookArtist2();
            }
        });

        view.findViewById(R.id.share_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GalleryActivity.class));
            }
        });


        view.findViewById(R.id.birth_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(baseActivity, "Coming Soon...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), BirthdayCardActivity.class));

            }
        });
        view.findViewById(R.id.share_services_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(baseActivity, "Coming soon...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), BusinessCardActivity.class));
            }
        });
        return view;

    }

    public void bookArtist2() {


        //Log.e("UID_QTY", "5->" + quentity);
        Log.e("HASHMAP", "5->" + params.toString());

        new HttpsRequest(Consts.GET_SHARED_PROFILE_MSG_API, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    //ProjectUtils.showToast(Booking.this, msg);
                    try {
                        //  userBookingList = new ArrayList<>();
                        // Type getpetDTO = new TypeToken<List<CartModel>>() {
                        // }.getType();
                        // JSONArray jsonArray = response.getJSONArray("data");
                        JSONObject jsonObject = response;
                        String status = jsonObject.getString("status");
                        String success_msg = jsonObject.getString("message");
                        String data_msg = jsonObject.getString("data");


                        //  Log.e("success_msg", "" + success_msg);
                        //  Log.d("data_msg", "" + data_msg);


                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = data_msg;
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Log.d(TAG, "backResponse: " + msg);
                }
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}