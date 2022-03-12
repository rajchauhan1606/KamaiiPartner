package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
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
import com.kamaii.partner.ui.fragment.DocumentUploadFragment;
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

import static android.widget.Toast.LENGTH_LONG;

public class PersonalDocumentation extends AppCompatActivity {
    private String TAG = DocumentUploadFragment.class.getSimpleName();
    ImageView layaafront, layaaback, laypancard;
    Button btn_submit;
    private Dialog dialog;
    LinearLayout laycamera, laygalley;
    CustomTextViewBold tvCancel;
    String pathone = "", pathtwo = "", paththree = "";
    String flag = "";

    private HashMap<String, String> params;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    ProgressDialog progressDialog;
    File fileone = null, filetwo = null, filethree = null;
    private Bundle bundle;
    private ArtistDetailsDTO artistDetailsDTO;
    private ArrayList<DocumentDTO> documentDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private BaseActivity baseActivity;
    Uri uri;
    CustomTextViewBold laymsg;
    String edit = "0";
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    private SpinnerDialog spinnerDialogCate;
    CustomTextViewBold tvcat;
    String category_id = "";
    LinearLayout llBack;
    boolean from_wallet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_documentation);

        prefrence = SharedPrefrence.getInstance(PersonalDocumentation.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        documentDTOArrayList = new ArrayList<>();
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog = new ProgressDialog(PersonalDocumentation.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        params = new HashMap<>();

        from_wallet = getIntent().getBooleanExtra("from_wallet",false);

        Log.e("from_wallet"," personal "+from_wallet);

        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        layaafront = findViewById(R.id.layaafront);
        layaaback = findViewById(R.id.layaaback);
        btn_submit = findViewById(R.id.btn_submit);
        laypancard = findViewById(R.id.laypancard);

        dialog = new Dialog(PersonalDocumentation.this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dailog_camera_gallery);
        dialog.setCancelable(false);
        laycamera = dialog.findViewById(R.id.laycamera);
        laygalley = dialog.findViewById(R.id.laygalley);
        tvCancel = dialog.findViewById(R.id.tvCancel);

        llBack = findViewById(R.id.llBack);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PersonalDocumentation.this, DocumentUploadTwoActivity.class).putExtra("from_wallet",from_wallet);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

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

        layaafront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "1";
                        // dialog.show();

                        ImagePicker.Companion.with(PersonalDocumentation.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "1";

                    ImagePicker.Companion.with(PersonalDocumentation.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }


            }
        });

        layaaback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "2";
                        ImagePicker.Companion.with(PersonalDocumentation.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "2";
                    ImagePicker.Companion.with(PersonalDocumentation.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                }


            }
        });

        laypancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "14";
                        // dialog.show();

                        ImagePicker.Companion.with(PersonalDocumentation.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "14";

                    ImagePicker.Companion.with(PersonalDocumentation.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
                    // dialog.show();
                }


            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (documentDTOArrayList.size() != 0) {
                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {

                        if (edit.equalsIgnoreCase("1")) {
                            edit = "0";
                        } else {
                            Click();
                        }
                    } else {
                        Toast.makeText(PersonalDocumentation.this, "Already approved document. Good Luck!", LENGTH_LONG).show();
                    }
                } else {
                    Click();
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 23) {


                Uri imageUri = data.getData();

                if (flag.equalsIgnoreCase("1")) {

                    layaafront.setImageURI(imageUri);
                    pathone = imageUri.getPath();
                    // pathone = FileUtility.getPath(PersonalDocumentation.this,imageUri);

                } else if (flag.equalsIgnoreCase("2")) {

                    layaaback.setImageURI(imageUri);
                    pathtwo = imageUri.getPath();
                    //pathtwo = FileUtility.getPath(PersonalDocumentation.this,imageUri);

                } else if (flag.equalsIgnoreCase("14")) {
                    laypancard.setImageURI(imageUri);
                    paththree = imageUri.getPath();
                    // pathelven = FileUtility.getPath(PersonalDocumentation.this,imageUri);
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

    public void updateprofile() {

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);


        RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);

        MultipartBody.Part fileToSendfourteen=null;

        if (!paththree.trim().isEmpty()) {
            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
             fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
        }

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.Add_PERSONAL_DOC(fileToSendone, fileToSendtwo, fileToSendfourteen, userid);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                Log.e("dhaval tag ttt", response.message());

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        Log.e("WEBBYKNIGHT", "" + s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        if (sstatus == 1) {
                            Toast.makeText(PersonalDocumentation.this, message, LENGTH_LONG).show();
                            //getArtist();
                            Intent intent1 = new Intent(PersonalDocumentation.this, DocumentUploadTwoActivity.class).putExtra("from_wallet",from_wallet);
                            startActivity(intent1);
                            finish();
                            overridePendingTransition(R.anim.stay, R.anim.slide_down);
                        } else if (sstatus == 3) {
                            Toast.makeText(PersonalDocumentation.this, message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PersonalDocumentation.this, message, LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(PersonalDocumentation.this, "Try again. Server is not responding"+"999",
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
                Toast.makeText(PersonalDocumentation.this, "Server Did Not Responding and Try again"+t,
                        LENGTH_LONG).show();
            }
        });

    }


    public void Click() {
        if (pathone.equalsIgnoreCase("")) {
            Toast.makeText(PersonalDocumentation.this, "Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
            return;
        } else if (pathtwo.equalsIgnoreCase("")) {
            Toast.makeText(PersonalDocumentation.this, "Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
            return;
        } else {

            fileone = new File(pathone);
            filetwo = new File(pathtwo);
            filethree = new File(paththree);

            if (NetworkManager.isConnectToInternet(PersonalDocumentation.this)) {
                updateprofile();
            } else {
                ProjectUtils.showToast(PersonalDocumentation.this, getResources().getString(R.string.internet_concation));
            }


        }
    }
}
