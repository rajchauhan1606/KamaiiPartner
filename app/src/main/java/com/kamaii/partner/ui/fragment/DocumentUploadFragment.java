package com.kamaii.partner.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.ArtistDetailsDTO;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.DocumentDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.OnSpinerItemClick;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;
import com.kamaii.partner.utils.SpinnerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;

public class DocumentUploadFragment extends Fragment {

    private String TAG = DocumentUploadFragment.class.getSimpleName();
    private View view;
    CustomEditText etcarno,etbankname,etBenificiaryName,etaccoutno,etifsc_code,etbranchcode,etrefernamenumber;
    ImageView layaafront,layaaback,layrcfront,layrcback,layinsuarnce,laycarpermit,laypoliceverification,laycarphoto,layownerphoto,laycancelcheque,layselfie,laydrivingfront,laydrivingback,laypancard;
    Button btn_submit;
    private Dialog dialog;
    LinearLayout laycamera,laygalley;
    CustomTextViewBold tvCancel;
    String pathone="",pathtwo="",paththree="",pathfour="",pathfive="",pathsix="",pathseven="",patheight="",pathnine="",pathten="",pathelven="",pathtwelve="",paththirteen="",carmodelnamestring="",carcategorystring="",citystring="",pathfourteen="",
    bookingcarstring="",carno="",rno="";
    String flag="";

    String[] city = { "Ahmedabad", "Amreli","Gandhinagar", "Vadodara", "Rajkot", "Surat", "Bhavanagar", "Lunawada","Godhra","Anand","Banas","Kantha","Bharuch","Dohad","Jamnagar"
            ,"Junagadh","Kachchh","Kheda","Mahesana","Narmada","Navsari","Panch","Mahals","Patan","Porbandar","Sabar","Surendranagar","Tapi","The Dangs","Valsad","Modasa"
            ,"Mumbai","Delhi","Pune","Other City"};
    String[] carcategory = {"Hatchback Car", "Sedan Car", "SUV Car", "Luxury Car"};
    String[] carmodelname = {  "Hyundai Xcent", "Hyundai Accent","Hyundai i10", "Maruti Swift Dzire", "Maruti Alto", "Tata Indica", "Tata Zest",
            "Toyota Innova Crysta","Nissan Sunny",
            "Chevrolet Spark","Honda WRV","Honda City","Mahindra TUV300","Mahindra Bolero","Hyundai EON","Maruti Ertiga","Maruti WagonR","Maruti Eeco","Maruti Celerio",
            "Tata Itos","Chevrolet Tavera","Toyota Etios","Nissan Datsun Go","Honda Amaze","Honda BRV","Mahindra Xylo","Mahindra Marazzo"," Ford Figo Aspire","Rickshaw Bajaj",
    "Bike","Others"};
    Spinner spicity,spicarmodelname,spicarmcategory;
    private HashMap<String, String> params;
    CheckBox checkone,checktwo,checkthree,checkfour;
    private HashMap<String, File> paramsFile;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    ProgressDialog progressDialog;
    File fileone=null,filetwo=null,filethree=null,filefour=null,filefive=null,filesix=null,fileseven=null,fileeight=null,filenine=null,fileten=null,fileeleven=null,filetwelve=null,filethirteen=null,filefourteen=null;
    private Bundle bundle;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<DocumentDTO> documentDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private BaseActivity baseActivity;
    Uri uri;
   public static LinearLayout layall;
    CustomTextViewBold laymsg;
    String edit="0";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    CustomTextViewBold tvcat;
    String category_id="";
    LinearLayout layallrcbook,layalldlicence,laycarnformation,layselectcategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_do_upload, container, false);

        baseActivity.headerNameTV.setText("Document");
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        documentDTOArrayList=new ArrayList<>();
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");


        etcarno=view.findViewById(R.id.etcarno);

        params = new HashMap<>();
        paramsFile = new HashMap<>();

        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        etbankname=view.findViewById(R.id.etbankname);
        etaccoutno=view.findViewById(R.id.etaccoutno);
        etifsc_code=view.findViewById(R.id.etifsc_code);
        etbranchcode=view.findViewById(R.id.etbranchcode);
        etBenificiaryName=view.findViewById(R.id.etBenificiaryName);
        laycancelcheque=view.findViewById(R.id.laycancelcheque);
        etrefernamenumber=view.findViewById(R.id.etrefernamenumber);
        layaafront=view.findViewById(R.id.layaafront);
        layaaback=view.findViewById(R.id.layaaback);
        layrcfront=view.findViewById(R.id.layrcfront);
        layrcback=view.findViewById(R.id.layrcback);
        layinsuarnce=view.findViewById(R.id.layinsuarnce);
        laycarpermit=view.findViewById(R.id.laycarpermit);
        laypoliceverification=view.findViewById(R.id.laypoliceverification);
        layselfie=view.findViewById(R.id.layselfie);
        laycarphoto=view.findViewById(R.id.laycarphoto);
        layownerphoto=view.findViewById(R.id.layownerphoto);
        btn_submit=view.findViewById(R.id.btn_submit);
        checkone=view.findViewById(R.id.checkone);
        checktwo=view.findViewById(R.id.checktwo);
        checkthree=view.findViewById(R.id.checkthree);
        checkfour=view.findViewById(R.id.checkfour);
        laymsg=view.findViewById(R.id.laymsg);
        layall=view.findViewById(R.id.layall);
        laydrivingfront=view.findViewById(R.id.laydrivingfront);
        laydrivingback=view.findViewById(R.id.laydrivingback);
        laypancard=view.findViewById(R.id.laypancard);
        tvcat=view.findViewById(R.id.tvcat);
        layallrcbook=view.findViewById(R.id.layallrcbook);
        layalldlicence=view.findViewById(R.id.layalldlicence);
        laycarnformation=view.findViewById(R.id.laycarnformation);
        layselectcategory=view.findViewById(R.id.layselectcategory);

        getCategory();
        tvcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryDTOS.size() > 0) {
                    spinnerDialogCate.showSpinerDialog();

                } else {
                    Toast.makeText(getActivity(),getResources().getString(R.string.no_cate_found), Toast.LENGTH_SHORT).show();

                }
            }
        });

        spicity  = view.findViewById(R.id.spicity);
        spicarmodelname  = view.findViewById(R.id.spicarmodelname);
        spicarmcategory  = view.findViewById(R.id.spicarmcategory);

        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,city);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spicity.setAdapter(aa);

        spicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citystring=city[position];
                //Toast.makeText(getActivity(),city[position] , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter aaggg = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,carmodelname);
        aaggg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spicarmodelname.setAdapter(aaggg);

        spicarmodelname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carmodelnamestring=carmodelname[position];
               // Toast.makeText(getActivity(),carmodelname[position] , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter aaggghhh = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,carcategory);
        aaggghhh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spicarmcategory.setAdapter(aaggghhh);

        spicarmcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carcategorystring=carcategory[position];

               // Toast.makeText(getActivity(),carcategory[position] , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getArtist();

        dialog = new Dialog(getActivity(),R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dailog_camera_gallery);
        dialog.setCancelable(false);
        laycamera=dialog.findViewById(R.id.laycamera);
        laygalley=dialog.findViewById(R.id.laygalley);
        tvCancel=dialog.findViewById(R.id.tvCancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        laycamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        laygalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        laydrivingfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="12";
                        // dialog.show();

                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="12";

                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }



            }
        });

        laydrivingback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="13";
                        // dialog.show();

                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="13";

                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }



            }
        });

        layaafront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="1";
                       // dialog.show();

                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="1";

                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                   // dialog.show();
                }



            }
        });

        layaaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="2";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="2";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }



            }
        });

        layrcfront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="3";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="3";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });


        layrcback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="4";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="4";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });

        layinsuarnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="5";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="5";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        laycarpermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="6";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="6";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });
        laypoliceverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="7";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="7";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });

        laycarphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="8";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="8";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        layownerphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="9";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="9";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }

            }
        });

        laycancelcheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="10";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="10";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }



            }
        });

        layselfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="11";
                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="11";
                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });


        laypancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(documentDTOArrayList.size()!=0){

                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                        flag="14";
                        // dialog.show();

                        ImagePicker.Companion.with(getActivity())
                                .crop()	    			//Crop image(Optional), Check Customization for more option
                                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    }else {


                    }
                }else {
                    flag="14";

                    ImagePicker.Companion.with(getActivity())
                            .crop()	    			//Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }



            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(documentDTOArrayList.size()!=0)
                {
                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){

                        if(edit.equalsIgnoreCase("1")){

                            layall.setVisibility(View.VISIBLE);
                            layselectcategory.setVisibility(View.VISIBLE);
                            tvcat.setVisibility(View.VISIBLE);
                            laymsg.setVisibility(View.GONE);
                            btn_submit.setText("Submit");
                            edit="0";
                        }else {
                            Click();

                        }



                    }else {
                        Toast.makeText(getActivity(), "Already approved document. Good Luck!", LENGTH_LONG).show();
                    }
                }
                else
                {
                    Click();
                }

            }
        });

        return view;
    }

    public void getCategory() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
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
                        progressDialog.dismiss();

                        spinnerDialogCate = new SpinnerDialog(getActivity(), categoryDTOS, getResources().getString(R.string.select_category));// With 	Animation
                        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, String id, int position) {
                                tvcat.setText(item);
                                category_id=id;

                                layall.setVisibility(View.VISIBLE);
                                if(category_id.equalsIgnoreCase("85") ||category_id.equalsIgnoreCase("74")||category_id.equalsIgnoreCase("126")||category_id.equalsIgnoreCase("127")||category_id.equalsIgnoreCase("110")){
                                    layallrcbook.setVisibility(View.VISIBLE);
                                    layalldlicence.setVisibility(View.VISIBLE);
                                    laycarnformation.setVisibility(View.VISIBLE);
                                }else {
                                    layallrcbook.setVisibility(View.GONE);
                                    layalldlicence.setVisibility(View.GONE);
                                    laycarnformation.setVisibility(View.GONE);

                                }


                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    public void getArtist() {
        progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    try {

                        // ProjectUtils.showToast(getActivity(), msg);
                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        documentDTOArrayList = new ArrayList<>();

                        if(artistDetailsDTO.getDocument()!=null){
                            documentDTOArrayList = artistDetailsDTO.getDocument();
                            if(documentDTOArrayList.size()!=0){

                                if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){
                                    layall.setVisibility(View.GONE);
                                    laymsg.setVisibility(View.VISIBLE);
                                    layselectcategory.setVisibility(View.GONE);
                                    tvcat.setVisibility(View.GONE);
                                    btn_submit.setText("Edit Document");
                                    edit="1";

                                }else {
                                    layall.setVisibility(View.VISIBLE);
                                }

                                etcarno.setText(documentDTOArrayList.get(0).getCar_number());
                                etbankname.setText(documentDTOArrayList.get(0).getBank_name());
                                etBenificiaryName.setText(documentDTOArrayList.get(0).getBenefiacary_name());
                                etaccoutno.setText(documentDTOArrayList.get(0).getAccount_number());
                                etifsc_code.setText(documentDTOArrayList.get(0).getIfsc_code());
                                etbranchcode.setText(documentDTOArrayList.get(0).getBranch());
                                etrefernamenumber.setText(documentDTOArrayList.get(0).getRef_number());


                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getDriving_licence_front())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laydrivingfront);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getDriving_licence_back())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laydrivingback);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getPan_card())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laypancard);
                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getAdhar_card())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layaafront);


                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getAdhar_card_back())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layaaback);


                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getRc_book())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layrcfront);


                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getRc_book_back())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layrcback);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getCar_permit())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laycarpermit);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getCar_insurance())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layinsuarnce);


                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getCancel_cheque_copy())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laycancelcheque);
                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getPolice_verification())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laypoliceverification);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getSelfi_with_car_photo())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(laycarphoto);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getSelfie_photo())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layselfie);

                                Glide.with(getActivity()).
                                        load(documentDTOArrayList.get(0).getOwner_photo())
                                        .placeholder(R.drawable.placeholder)
                                        .dontAnimate()
                                        .error(R.drawable.noimagefound)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(layownerphoto);
                                spicity.setSelection(loop(documentDTOArrayList.get(0).getCity(),city));
                                spicarmodelname.setSelection(loop(documentDTOArrayList.get(0).getModel_name(),carmodelname));
                                spicarmcategory.setSelection(loop(documentDTOArrayList.get(0).getCar_category(),carcategory));



                                if(documentDTOArrayList.get(0).getCar_booking().contains(",")){
                                    String[] temparray= documentDTOArrayList.get(0).getCar_booking().split(",");
                                    for(int i=0;i<temparray.length;i++){

                                        if(temparray[i].equalsIgnoreCase("Daily City")){
                                            checkone.setChecked(true);
                                        }
                                        if(temparray[i].equalsIgnoreCase("Local Rental")){
                                            checktwo.setChecked(true);
                                        }
                                        if(temparray[i].equalsIgnoreCase("Out Station")){
                                            checkthree.setChecked(true);
                                        }
                                        if(temparray[i].equalsIgnoreCase("Prime Car (SUV, 7 Seater)")){
                                            checkfour.setChecked(true);
                                        }
                                    }
                                }else {
                                    if(documentDTOArrayList.get(0).getCar_booking().equalsIgnoreCase("Daily City")){
                                        checkone.setChecked(true);
                                    }
                                    if(documentDTOArrayList.get(0).getCar_booking().equalsIgnoreCase("Local Rental")){
                                        checktwo.setChecked(true);
                                    }
                                    if(documentDTOArrayList.get(0).getCar_booking().equalsIgnoreCase("Out Station")){
                                        checkthree.setChecked(true);
                                    }
                                    if(documentDTOArrayList.get(0).getCar_booking().equalsIgnoreCase("Prime Car (SUV, 7 Seater)")){
                                        checkfour.setChecked(true);
                                    }
                                }

                                if(documentDTOArrayList.size()!=0){
                                    if(documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")){

                                    }else {

                                        layaafront.setEnabled(false);
                                        layaaback.setEnabled(false);
                                        layrcfront.setEnabled(false);
                                        layrcback.setEnabled(false);
                                        layinsuarnce.setEnabled(false);
                                        laycarpermit.setEnabled(false);
                                        laypoliceverification.setEnabled(false);
                                        laycarphoto.setEnabled(false);
                                        layownerphoto.setEnabled(false);
                                        laycancelcheque.setEnabled(false);
                                        layselfie.setEnabled(false);
                                        etrefernamenumber.setEnabled(false);
                                        etbranchcode.setEnabled(false);
                                        etifsc_code.setEnabled(false);
                                        etaccoutno.setEnabled(false);
                                        etBenificiaryName.setEnabled(false);
                                        etbankname.setEnabled(false);
                                        etcarno.setEnabled(false);
                                        btn_submit.setEnabled(false);

                                        checkone.setEnabled(false);
                                        checktwo.setEnabled(false);
                                        checkthree.setEnabled(false);
                                        checkfour.setEnabled(false);
                                        spicity.setEnabled(false);
                                        spicarmcategory.setEnabled(false);
                                        spicarmodelname.setEnabled(false);
                                        Toast.makeText(getActivity(),"Not Allow To Change",LENGTH_LONG).show();
                                    }
                                }else {

                                }
                            }
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }


    private  int loop(String val, String[] array){
        return  new ArrayList<String>(Arrays.asList(array)).indexOf(val);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

             if (requestCode == 23) {


                Uri imageUri = data.getData();

                if(flag.equalsIgnoreCase("1")){

                    layaafront.setImageURI(imageUri);
                    pathone=   imageUri.getPath();
                    // pathone = FileUtility.getPath(getActivity(),imageUri);

                }else if(flag.equalsIgnoreCase("2")){

                    layaaback.setImageURI(imageUri);
                    pathtwo=   imageUri.getPath();
                    //pathtwo = FileUtility.getPath(getActivity(),imageUri);

                }
                else if(flag.equalsIgnoreCase("3")){
                    layrcfront.setImageURI(imageUri);
                    paththree=   imageUri.getPath();
                    // paththree = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("4")){
                    layrcback.setImageURI(imageUri);
                    pathfour=   imageUri.getPath();
                    // pathfour = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("5")){
                    layinsuarnce.setImageURI(imageUri);
                    pathfive=   imageUri.getPath();
                    //  pathfive = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("6")){
                    laycarpermit.setImageURI(imageUri);
                    pathsix=   imageUri.getPath();
                    //  pathsix = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("7")){
                    laypoliceverification.setImageURI(imageUri);
                    pathseven=   imageUri.getPath();
                    //   pathseven = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("8")){
                    laycarphoto.setImageURI(imageUri);
                    patheight=   imageUri.getPath();
                    // patheight = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("9")){
                    layownerphoto.setImageURI(imageUri);
                    pathnine=   imageUri.getPath();
                    //   pathnine = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("10")){
                    laycancelcheque.setImageURI(imageUri);
                    pathten=   imageUri.getPath();
                    // pathten = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("11")){
                    layselfie.setImageURI(imageUri);
                    pathelven=   imageUri.getPath();
                    // pathelven = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("12")){
                    laydrivingfront.setImageURI(imageUri);
                    pathtwelve=   imageUri.getPath();
                    // pathelven = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("13")){
                    laydrivingback.setImageURI(imageUri);
                    paththirteen=   imageUri.getPath();
                    // pathelven = FileUtility.getPath(getActivity(),imageUri);
                }
                else if(flag.equalsIgnoreCase("14")){
                    laypancard.setImageURI(imageUri);
                    pathfourteen=   imageUri.getPath();
                    // pathelven = FileUtility.getPath(getActivity(),imageUri);
                }
            }
        } else {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void updateprofile()
    {
        MultipartBody.Part fileToSendfive=null;
        MultipartBody.Part fileToSendsix=null;
        MultipartBody.Part fileToSendseven=null;
        MultipartBody.Part fileToSendele=null;
        MultipartBody.Part fileToSendfourteen=null;
        MultipartBody.Part fileToSendthree=null;
        MultipartBody.Part fileToSendfour=null;
        MultipartBody.Part fileToSendtwelve=null;
        MultipartBody.Part fileToSendthirteen=null;
        MultipartBody.Part fileToSendeight=null;
        MultipartBody.Part fileToSendnine=null;

        if(!patheight.trim().isEmpty()){
            RequestBody requestBodyeight = RequestBody.create(MediaType.parse("multipart/form-data"), fileeight);
           fileToSendeight = MultipartBody.Part.createFormData("selfi_with_car_photo", fileeight.getName(), requestBodyeight);

        }
        if(!pathnine.trim().isEmpty()){
            RequestBody requestBodynine = RequestBody.create(MediaType.parse("multipart/form-data"), filenine);
          fileToSendnine = MultipartBody.Part.createFormData("owner_photo", filenine.getName(), requestBodynine);
        }

        if(!paththree.trim().isEmpty()){
            RequestBody requestBodyth = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendthree = MultipartBody.Part.createFormData("rc_book", filethree.getName(), requestBodyth);

        }

        if(!pathfour.trim().isEmpty()){
            RequestBody requestBodyfour = RequestBody.create(MediaType.parse("multipart/form-data"), filefour);
            fileToSendfour = MultipartBody.Part.createFormData("rc_book_back", filefour.getName(), requestBodyfour);

        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);


        RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);


        if(!pathfive.trim().isEmpty()){
            RequestBody requestBodyfive = RequestBody.create(MediaType.parse("multipart/form-data"), filefive);
            fileToSendfive = MultipartBody.Part.createFormData("car_insurance", filefive.getName(), requestBodyfive);
        }


        if(!pathsix.trim().isEmpty()){
            RequestBody requestBodysix = RequestBody.create(MediaType.parse("multipart/form-data"), filesix);
           fileToSendsix = MultipartBody.Part.createFormData("car_permit", filesix.getName(), requestBodysix);
        }



        if(!pathseven.trim().isEmpty()){
            RequestBody requestBodyse = RequestBody.create(MediaType.parse("multipart/form-data"), fileseven);
             fileToSendseven = MultipartBody.Part.createFormData("police_verification", fileseven.getName(), requestBodyse);

        }

        if(!pathelven.trim().isEmpty()){
            RequestBody requestBodyele = RequestBody.create(MediaType.parse("multipart/form-data"), fileeleven);
            fileToSendele = MultipartBody.Part.createFormData("selfie_photo", fileeleven.getName(), requestBodyele);

        }

        if(!pathtwelve.trim().isEmpty()){
            RequestBody requestBodytwelve = RequestBody.create(MediaType.parse("multipart/form-data"), filetwelve);
         fileToSendtwelve = MultipartBody.Part.createFormData("driving_licence_front", filetwelve.getName(), requestBodytwelve);

        }

        if(!paththirteen.trim().isEmpty()){
            RequestBody requestBodythirteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethirteen);
            fileToSendthirteen = MultipartBody.Part.createFormData("driving_licence_back", filethirteen.getName(), requestBodythirteen);


        }



       // ProgressRequestBody requestBodyten = new ProgressRequestBody(fileten, this);
       RequestBody requestBodyten = RequestBody.create(MediaType.parse("multipart/form-data"), fileten);
        MultipartBody.Part fileToSendten = MultipartBody.Part.createFormData("cancel_cheque_copy", fileten.getName(), requestBodyten);

      //  ProgressRequestBody requestBodyele = new ProgressRequestBody(fileeleven, this);




        if(!pathfourteen.trim().isEmpty()){
            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filefourteen);
            fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filefourteen.getName(), requestBodyfourteen);
        }

        RequestBody carmodel = RequestBody.create(MediaType.parse("text/plain"),carmodelnamestring);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"),citystring);
        RequestBody bokingcar = RequestBody.create(MediaType.parse("text/plain"),bookingcarstring);
        RequestBody carcategory = RequestBody.create(MediaType.parse("text/plain"),carcategorystring);
        RequestBody rnumber = RequestBody.create(MediaType.parse("text/plain"),rno);
        RequestBody carnooo = RequestBody.create(MediaType.parse("text/plain"),carno);
        RequestBody bankname = RequestBody.create(MediaType.parse("text/plain"),etbankname.getText().toString());
        RequestBody bname = RequestBody.create(MediaType.parse("text/plain"),etBenificiaryName.getText().toString());
        RequestBody accno = RequestBody.create(MediaType.parse("text/plain"),etaccoutno.getText().toString());
        RequestBody ifsccode = RequestBody.create(MediaType.parse("text/plain"),etifsc_code.getText().toString());
        RequestBody bcode = RequestBody.create(MediaType.parse("text/plain"),etbranchcode.getText().toString());
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());
        RequestBody cat_idreq = RequestBody.create(MediaType.parse("text/plain"), category_id);




            progressDialog.show();
            Retrofit retrofit = apiClient.getClient();
            apiRest api = retrofit.create(apiRest.class);
            Call<ResponseBody> callone = api.Update_Step_One(carmodel, city,bokingcar,carcategory,rnumber,carnooo,bankname,bname,accno ,ifsccode,bcode,fileToSendone,fileToSendtwo,fileToSendthree,fileToSendfour,fileToSendfive,fileToSendsix,fileToSendseven,fileToSendeight,fileToSendnine,fileToSendten,userid,fileToSendele,fileToSendtwelve,fileToSendthirteen,fileToSendfourteen,cat_idreq);
            callone.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();

                  //  Log.e("RES", response.message());

                    try {
                        if (response.isSuccessful()) {
                            ResponseBody responseBody = response.body();

                            String s = responseBody.string();
                            JSONObject object = new JSONObject(s);


                            String message = object.getString("message");
                            int sstatus = object.getInt("status");
                            if (sstatus==1)
                            {

                                laymsg.setVisibility(View.VISIBLE);
                                layall.setVisibility(View.GONE);
                                layselectcategory.setVisibility(View.GONE);
                                tvcat.setVisibility(View.GONE);
                                btn_submit.setVisibility(View.GONE);

                                Toast.makeText(getActivity(), message, LENGTH_LONG).show();
                                //getArtist();
                            }
                            else if(sstatus==3){
                                Toast.makeText(getActivity(), message, LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(), message, LENGTH_LONG).show();
                            }
                        }
                        else {
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
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Try again. Server is not responding",
                            LENGTH_LONG).show();
                }
            });

    }


    public  void Click()
    {
        bookingcarstring="";
        if(checkone.isChecked()){
            bookingcarstring=bookingcarstring+checkone.getText().toString();

        }


        if (tvcat.getText().toString().equalsIgnoreCase(getResources().getString(R.string.all_categories))) {
            Toast.makeText(getActivity(),getResources().getString(R.string.val_category), Toast.LENGTH_SHORT).show();
            return;
        }

         if(category_id.equalsIgnoreCase("85") ||category_id.equalsIgnoreCase("74")||category_id.equalsIgnoreCase("126")||category_id.equalsIgnoreCase("127")||category_id.equalsIgnoreCase("110")){
            if(paththree.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Upload Rc Book Front Photo", Toast.LENGTH_LONG).show();
                return;
            }

            else if(pathfour.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Upload Rc Book Back Photo", Toast.LENGTH_LONG).show();
                return;
            }
            else if(pathtwelve.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Upload Driving Licence Front Photo", Toast.LENGTH_LONG).show();
                return;
            }

            else if(paththirteen.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Upload Driving Licence Back Photo", Toast.LENGTH_LONG).show();
                return;
            }
            else if(carmodelnamestring.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Select Car Model", Toast.LENGTH_LONG).show();
                return;
            }
            else if(carcategorystring.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Select Car Category", Toast.LENGTH_LONG).show();
                return;
            }
            else if(patheight.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Upload Selfie With Car Photo", Toast.LENGTH_LONG).show();
                return;
            }
            else if(pathnine.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Upload Owner Photo", Toast.LENGTH_LONG).show();
                return;
            }
            else if(bookingcarstring.equalsIgnoreCase("")){
                Toast.makeText(getActivity(),"Please Select Booking", Toast.LENGTH_LONG).show();
                return;
            }
        }else {


        }

        if(checktwo.isChecked()){
            bookingcarstring=bookingcarstring+","+checktwo.getText().toString();
        }
        if(checkthree.isChecked()){
            bookingcarstring=bookingcarstring+","+checkthree.getText().toString();
        }
        if(checkfour.isChecked()){
            bookingcarstring=bookingcarstring+","+checkfour.getText().toString();
        }
         if(citystring.equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please Select City", Toast.LENGTH_LONG).show();
            return;
        }
       else if(pathone.equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
            return;
        }else if(pathtwo.equalsIgnoreCase("")){
            Toast.makeText(getActivity(),"Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
            return;
        }

        else if(etbankname.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Bank Name", Toast.LENGTH_LONG).show();
            return;
        }
        else if(etBenificiaryName.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Beneficiary Name", Toast.LENGTH_LONG).show();
            return;
        }

        else if(etaccoutno.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Account Number", Toast.LENGTH_LONG).show();
            return;
        }
        else if(etifsc_code.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Please Enter IFSC Code", Toast.LENGTH_LONG).show();
            return;
        }
        else if(etbranchcode.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Please Enter Branch Code", Toast.LENGTH_LONG).show();
            return;
        }
         else if(pathten.equalsIgnoreCase("")){
             Toast.makeText(getActivity(),"Please Upload  Cancel Cheque Photo", Toast.LENGTH_LONG).show();
             return;
         }else {

            fileone = new File(pathone);
            filetwo = new File(pathtwo);
            filethree = new File(paththree);
            filefour = new File(pathfour);
            filefive = new File(pathfive);
            filesix = new File(pathsix);
            fileseven = new File(pathseven);
            fileeight = new File(patheight);
            filenine = new File(pathnine);
            fileten = new File(pathten);
            fileeleven = new File(pathelven);
             filetwelve = new File(pathtwelve);
            filethirteen = new File(paththirteen);
             filefourteen = new File(pathfourteen);

            rno=etrefernamenumber.getText().toString();
            carno=etcarno.getText().toString();
            if (NetworkManager.isConnectToInternet(getActivity())) {
                updateprofile();
            } else {
                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
            }



        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;

    }


}
