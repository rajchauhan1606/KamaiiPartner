package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.kamaii.partner.DTO.DocumentDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.fragment.Wallet;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.widget.Toast.LENGTH_LONG;

public class BankDocument extends AppCompatActivity {
    Button btn_submit;
    private HashMap<String, String> params;
    ProgressDialog progressDialog;
    private HashMap<String, File> paramsFile;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    private HashMap<String, String> parms = new HashMap<>();
    private ArrayList<DocumentDTO> documentDTOArrayList;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    CustomEditText etbankname, etBenificiaryName, etaccoutno, etifsc_code, etbranchcode;
    ImageView laycancelcheque;
    String edit = "0";
    String pathone = "", pathtwo = "", paththree = "", pathfour = "", pathfive = "", pathsix = "", pathseven = "", patheight = "", pathnine = "", pathten = "", pathelven = "", pathtwelve = "", paththirteen = "", carmodelnamestring = "", carcategorystring = "", citystring = "", pathfourteen = "";
    File fileten = null;
    String flag = "";
    LinearLayout llBack;
    boolean from_wallet = false;
    BaseActivity baseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_document);
        prefrence = SharedPrefrence.getInstance(BankDocument.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        documentDTOArrayList = new ArrayList<>();
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        progressDialog = new ProgressDialog(BankDocument.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        params = new HashMap<>();
        paramsFile = new HashMap<>();

        baseActivity = new BaseActivity();
        from_wallet = getIntent().getBooleanExtra("from_wallet", false);
        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        etbankname = findViewById(R.id.etbankname);
        etaccoutno = findViewById(R.id.etaccoutno);
        etifsc_code = findViewById(R.id.etifsc_code);
        etbranchcode = findViewById(R.id.etbranchcode);
        etBenificiaryName = findViewById(R.id.etBenificiaryName);
        laycancelcheque = findViewById(R.id.laycancelcheque);
        btn_submit = findViewById(R.id.btn_submit);

        llBack = findViewById(R.id.llBack);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();


            }
        });

        laycancelcheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentDTOArrayList.size() != 0) {

                    if (documentDTOArrayList.get(0).getStatus().equalsIgnoreCase("0")) {
                        flag = "10";
                        ImagePicker.Companion.with(BankDocument.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)
                                //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(23);
                    } else {


                    }
                } else {
                    flag = "10";
                    ImagePicker.Companion.with(BankDocument.this)
                            .crop()                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start(23);
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
                        Toast.makeText(BankDocument.this, "Already approved document. Good Luck!", LENGTH_LONG).show();
                    }
                } else {
                    Click();
                }

            }
        });
    }

    public void Click() {

        if (etbankname.getText().toString().trim().isEmpty()) {
            Toast.makeText(BankDocument.this, "Please Enter Bank Name", Toast.LENGTH_LONG).show();
            return;
        } else if (etBenificiaryName.getText().toString().trim().isEmpty()) {
            Toast.makeText(BankDocument.this, "Please Enter Beneficiary Name", Toast.LENGTH_LONG).show();
            return;
        } else if (etaccoutno.getText().toString().trim().isEmpty()) {
            Toast.makeText(BankDocument.this, "Please Enter Account Number", Toast.LENGTH_LONG).show();
            return;
        } else if (etifsc_code.getText().toString().trim().isEmpty()) {
            Toast.makeText(BankDocument.this, "Please Enter IFSC Code", Toast.LENGTH_LONG).show();
            return;
        } else if (etbranchcode.getText().toString().trim().isEmpty()) {
            Toast.makeText(BankDocument.this, "Please Enter Branch Code", Toast.LENGTH_LONG).show();
            return;
        } else if (pathten.equalsIgnoreCase("")) {
            Toast.makeText(BankDocument.this, "Please Upload  Cancel Cheque Photo", Toast.LENGTH_LONG).show();
            return;
        } else {

            fileten = new File(pathten);


            if (NetworkManager.isConnectToInternet(BankDocument.this)) {
                updateprofile();
            } else {
                ProjectUtils.showToast(BankDocument.this, getResources().getString(R.string.internet_concation));
            }


        }
    }

    public void updateprofile() {
        // ProgressRequestBody requestBodyten = new ProgressRequestBody(fileten, this);
        RequestBody requestBodyten = RequestBody.create(MediaType.parse("multipart/form-data"), fileten);
        MultipartBody.Part fileToSendten = MultipartBody.Part.createFormData("cancel_cheque_copy", fileten.getName(), requestBodyten);

        //  ProgressRequestBody requestBodyele = new ProgressRequestBody(fileeleven, this);


        RequestBody bankname = RequestBody.create(MediaType.parse("text/plain"), etbankname.getText().toString());
        RequestBody bname = RequestBody.create(MediaType.parse("text/plain"), etBenificiaryName.getText().toString());
        RequestBody accno = RequestBody.create(MediaType.parse("text/plain"), etaccoutno.getText().toString());
        RequestBody ifsccode = RequestBody.create(MediaType.parse("text/plain"), etifsc_code.getText().toString());
        RequestBody bcode = RequestBody.create(MediaType.parse("text/plain"), etbranchcode.getText().toString());
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());


        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.Add_bank_details(bankname, bname, accno, ifsccode, bcode, fileToSendten, userid);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                //   Log.e("RES", response.message());

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");
                        if (sstatus == 1) {
                            // Toast.makeText(BankDocument.this, message, LENGTH_LONG).show();
                            //getArtist();


                           // Intent intent1 = new Intent(BankDocument.this, DocumentUploadTwoActivity.class).putExtra("from_wallet",from_wallet);
                           // startActivity(intent1);
                            onBackPressed();
                            finish();
                            overridePendingTransition(R.anim.stay, R.anim.slide_down);


                        } else if (sstatus == 3) {
                            // Toast.makeText(BankDocument.this, message, LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(BankDocument.this, message, LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(BankDocument.this, "Try again. Server is not responding",
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
                Toast.makeText(BankDocument.this, "Try again. Server is not responding",
                        LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 23) {


                Uri imageUri = data.getData();
                if (flag.equalsIgnoreCase("10")) {
                    laycancelcheque.setImageURI(imageUri);
                    pathten = imageUri.getPath();
                    // pathten = FileUtility.getPath(getActivity(),imageUri);
                }
            }
        } else {

        }
    }

}