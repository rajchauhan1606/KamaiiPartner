package com.kamaii.partner.ui.activity;

import static android.widget.Toast.LENGTH_LONG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityBankWalletBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.fragment.ArtistProfileNewFragment;
import com.kamaii.partner.ui.models.newbooking.ArtistProfileDtoNew;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BankWalletActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityBankWalletBinding binding;
    private HashMap<String, String> parms = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    String flag = "";
    private ArtistProfileDtoNew artistDetailsDTO;
    ProgressDialog progressDialog;
    File fileone = null, filetwo = null, filethree = null, filefour = null;
    String pathone = "", pathtwo = "", paththree = "", pathfour = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BankWalletActivity.this, R.layout.activity_bank_wallet);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");

        getArtist();
        binding.btnSubmit.setOnClickListener(this);
        binding.llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                if (artistDetailsDTO.getCheck_bank_tracker().equalsIgnoreCase("1")){
                    Log.e("tracker","1");
                    submitAllData();
                }
                else if (artistDetailsDTO.getCheck_bank_tracker().equalsIgnoreCase("2")) {
                    Log.e("tracker","2");

                    submitWithoutBankData();
                } else if (artistDetailsDTO.getCheck_bank_tracker().equalsIgnoreCase("3")){
                    Log.e("tracker","3");

                    submitBankData();
                }
                break;
            case R.id.layaafront:
                flag = "1";

                ImagePicker.Companion.with(BankWalletActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.layaaback:
                flag = "2";

                ImagePicker.Companion.with(BankWalletActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.laypancard:
                flag = "3";

                ImagePicker.Companion.with(BankWalletActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.laycancelcheque:
                flag = "4";

                ImagePicker.Companion.with(BankWalletActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;

        }
    }

    private void submitWithoutBankData() {

        if (NetworkManager.isConnectToInternet(BankWalletActivity.this)) {




            if (pathone.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(this, "Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
                return;
            } else if (pathtwo.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(BankWalletActivity.this, "Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
                return;
            } else {
                fileone = new File(pathone);
                filetwo = new File(pathtwo);
                filethree = new File(paththree);
             //   filefour = new File(pathfour);

                uploadDocument();

            }


        } else {
            ProjectUtils.showToast(BankWalletActivity.this, getResources().getString(R.string.internet_concation));
        }


    }


    public void showDataFirst() {


        if (artistDetailsDTO.getCheck_bank_tracker().equalsIgnoreCase("2")) {

            binding.cancelChequeCard.setVisibility(View.INVISIBLE);
            binding.bankDetailsRelative.setVisibility(View.GONE);
            binding.aadharcardLinear.setVisibility(View.VISIBLE);
            binding.pancardCard.setVisibility(View.VISIBLE);
        } else if (artistDetailsDTO.getCheck_bank_tracker().equalsIgnoreCase("3")) {
            binding.cancelChequeCard.setVisibility(View.VISIBLE);
            binding.bankDetailsRelative.setVisibility(View.VISIBLE);
            binding.aadharcardLinear.setVisibility(View.GONE);
            binding.pancardCard.setVisibility(View.GONE);
        }


        //   binding.etBenificiaryName.setText(artistDetailsDTO.getName());
        binding.etBenificiaryName.setText(artistDetailsDTO.getUser_bank_name());
        binding.etbankname.setText(artistDetailsDTO.getBank_name());
        binding.etaccoutno.setText(artistDetailsDTO.getAccount_number());
        binding.etifscCode.setText(artistDetailsDTO.getIfsc_code());


        if (!artistDetailsDTO.getAddharCard().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(this).load(Uri.parse(artistDetailsDTO.getAddharCard())).into(binding.layaafront);
        }
        if (!artistDetailsDTO.getAddharCardBack().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(this).load(artistDetailsDTO.getAddharCardBack()).into(binding.layaaback);
        }
        if (!artistDetailsDTO.getPanCard().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(this).load(artistDetailsDTO.getPanCard()).into(binding.laypancard);
        }
        if (!artistDetailsDTO.getCheque().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(this).load(artistDetailsDTO.getCheque()).into(binding.laycancelcheque);
        }

        if (artistDetailsDTO.getTracker().equalsIgnoreCase("0")) {
            //    binding.btnLayout.setVisibility(View.VISIBLE);
            binding.btnSubmit.setOnClickListener(this);
            binding.layaafront.setOnClickListener(this);
            binding.layaaback.setOnClickListener(this);
            binding.laypancard.setOnClickListener(this);
            binding.laycancelcheque.setOnClickListener(this);
        } else {
            // binding.btnLayout.setVisibility(View.GONE);
        }


        pathone = artistDetailsDTO.getAddharCard();
        pathtwo = artistDetailsDTO.getAddharCardBack();
        paththree = artistDetailsDTO.getPanCard();
        pathfour = artistDetailsDTO.getCheque();

    }


    private void submitBankData() {

        if (NetworkManager.isConnectToInternet(this)) {



            if (binding.etBenificiaryName.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your name!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.etbankname.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your bank name!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.etaccoutno.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your account number!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.confirmAccoutno.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please confirm your account number!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.etifscCode.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your bank ifsc code!!", Toast.LENGTH_LONG).show();
                return;
            } else if (!binding.etaccoutno.getText().toString().equalsIgnoreCase(binding.confirmAccoutno.getText().toString())) {
                Toast.makeText(this, "Account number and Confirm account number not match!!", Toast.LENGTH_LONG).show();
                return;
            }  else if (pathfour.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(this, "Please Upload Cancel Cheque Photo", Toast.LENGTH_LONG).show();
                return;
            } else {

             //   fileone = new File(pathone);
             //   filetwo = new File(pathtwo);
            //    filethree = new File(paththree);
                filefour = new File(pathfour);

                uploadBankData();

            }


        } else {
            ProjectUtils.showToast(this, getResources().getString(R.string.internet_concation));
        }
    }

    private void submitAllData() {

        if (NetworkManager.isConnectToInternet(this)) {




            if (binding.etBenificiaryName.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your name!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.etbankname.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your bank name!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.etaccoutno.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your account number!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.confirmAccoutno.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please confirm your account number!!", Toast.LENGTH_LONG).show();
                return;
            } else if (binding.etifscCode.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter your bank ifsc code!!", Toast.LENGTH_LONG).show();
                return;
            } else if (!binding.etaccoutno.getText().toString().equalsIgnoreCase(binding.confirmAccoutno.getText().toString())) {
                Toast.makeText(this, "Account number and Confirm account number not match!!", Toast.LENGTH_LONG).show();
                return;
            } else if (pathone.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(this, "Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
                return;
            } else if (pathtwo.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(this, "Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
                return;
            } else if (pathfour.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(this, "Please Upload Cancel Cheque Photo", Toast.LENGTH_LONG).show();
                return;
            }  else {

                fileone = new File(pathone);
                filetwo = new File(pathtwo);
                filethree = new File(paththree);
                filefour = new File(pathfour);
                uploadAllData();

            }


        } else {
            ProjectUtils.showToast(this, getResources().getString(R.string.internet_concation));
        }
    }

    public void uploadBankData() {

        Log.e("tracker", " upload bank data");

   /*     RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);
*/
        Log.e("tracker", " upload bank data 2");

     /*   RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);
    */    Log.e("tracker", " upload bank data 3");

        RequestBody requestBodycheck = RequestBody.create(MediaType.parse("multipart/form-data"), filefour);
        MultipartBody.Part cancelChequepart = MultipartBody.Part.createFormData("cancel_cheque", filefour.getName(), requestBodycheck);

        Log.e("tracker", " upload bank data 4");

        MultipartBody.Part fileToSendfourteen = null;

     /*   if (!paththree.equalsIgnoreCase("https://kamaii.in/app/")) {
            Log.e("tracker","path three");

            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
        }*/
        Log.e("tracker", " upload bank data 5");

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), binding.etBenificiaryName.getText().toString());
        RequestBody account_number = RequestBody.create(MediaType.parse("text/plain"), binding.etaccoutno.getText().toString());
        RequestBody ifsc_code = RequestBody.create(MediaType.parse("text/plain"), binding.etifscCode.getText().toString());
        RequestBody bank_name = RequestBody.create(MediaType.parse("text/plain"), binding.etbankname.getText().toString());

        progressDialog.show();
        Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.update_bank_data(null, null, fileToSendfourteen, cancelChequepart, userid, name, account_number, ifsc_code, bank_name);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        Log.e("tracker", " upload bank data 6");

                        Log.e("WEBBYKNIGHT", "" + s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {

                            onBackPressed();
                            finish();
                            // baseActivity.loadHomeFragment(new ArtistProfileNewFragment(), "new");
                          /*  if (bank_status.equalsIgnoreCase("0")) {

                                startActivity(new Intent(baseActivity, BankDocument.class));
                                finish();
                            } else {

                                onBackPressed();
                                finish();
                            }
*/

                        } else if (sstatus == 3) {
                            Intent intent1 = new Intent(BankWalletActivity.this, CheckSignInActivity.class);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(BankWalletActivity.this, message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BankWalletActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {

                        Log.e("DOCUMENT_ERROR", "" + response.errorBody().string());
                        Toast.makeText(BankWalletActivity.this, "Try again. Server is not responding" + "999",
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

                Log.e("failure", "" + t.toString());

                /*Toast.makeText(BasicInfoActivity.this, "Server Did Not Responding and Try again" + t,
                        LENGTH_LONG).show();*/
            }
        });

    }

    public void uploadAllData() {

        Log.e("tracker", " upload bank data");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);

        Log.e("tracker", " upload bank data 2");

        RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);
        Log.e("tracker", " upload bank data 3");

        RequestBody requestBodycheck = RequestBody.create(MediaType.parse("multipart/form-data"), filefour);
        MultipartBody.Part cancelChequepart = MultipartBody.Part.createFormData("cancel_cheque", filefour.getName(), requestBodycheck);

        Log.e("tracker", " upload bank data 4");

        MultipartBody.Part fileToSendfourteen = null;

        if (!paththree.equalsIgnoreCase("https://kamaii.in/app/")) {
            Log.e("tracker","path three");

            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
        }
        Log.e("tracker", " upload bank data 5");

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), binding.etBenificiaryName.getText().toString());
        RequestBody account_number = RequestBody.create(MediaType.parse("text/plain"), binding.etaccoutno.getText().toString());
        RequestBody ifsc_code = RequestBody.create(MediaType.parse("text/plain"), binding.etifscCode.getText().toString());
        RequestBody bank_name = RequestBody.create(MediaType.parse("text/plain"), binding.etbankname.getText().toString());

        progressDialog.show();
        Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.update_bank_data(fileToSendone, fileToSendtwo, fileToSendfourteen, cancelChequepart, userid, name, account_number, ifsc_code, bank_name);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);
                        Log.e("tracker", " upload bank data 6");

                        Log.e("WEBBYKNIGHT", "" + s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {

                            onBackPressed();
                            finish();
                            // baseActivity.loadHomeFragment(new ArtistProfileNewFragment(), "new");
                          /*  if (bank_status.equalsIgnoreCase("0")) {

                                startActivity(new Intent(baseActivity, BankDocument.class));
                                finish();
                            } else {

                                onBackPressed();
                                finish();
                            }
*/

                        } else if (sstatus == 3) {
                            Intent intent1 = new Intent(BankWalletActivity.this, CheckSignInActivity.class);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(BankWalletActivity.this, message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BankWalletActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {

                        Log.e("DOCUMENT_ERROR", "" + response.errorBody().string());
                        Toast.makeText(BankWalletActivity.this, "Try again. Server is not responding" + "999",
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

                Log.e("failure", "" + t.toString());

                /*Toast.makeText(BasicInfoActivity.this, "Server Did Not Responding and Try again" + t,
                        LENGTH_LONG).show();*/
            }
        });

    }

    public void getArtist() {
        //    progressDialog.show();
        new HttpsRequest(Consts.GET_ARTIST_PROFILE_DATA_API, parms, this).stringPosttwo(Consts.GET_ARTIST_PROFILE_DATA_API, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    try {

                        Log.e("GET_ARTIST_PROFILE", "" + response.toString());
                        //    progressDialog.dismiss();
                        // ProjectUtils.showToast(BankWalletActivity.this, msg);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 23) {


                Uri picUri = data.getData();

                if (flag.equalsIgnoreCase("1")) {

                    binding.layaafront.setImageURI(picUri);
                    pathone = picUri.getPath();

                    // pathone = FileUtility.getPath(BasicInfoActivity.this,picUri);

                } else if (flag.equalsIgnoreCase("2")) {

                    binding.layaaback.setImageURI(picUri);
                    pathtwo = picUri.getPath();
                    //pathtwo = FileUtility.getPath(BasicInfoActivity.this,picUri);

                } else if (flag.equalsIgnoreCase("3")) {
                    binding.laypancard.setImageURI(picUri);
                    paththree = picUri.getPath();
                    // pathelven = FileUtility.getPath(BasicInfoActivity.this,picUri);
                } else if (flag.equalsIgnoreCase("4")) {
                    binding.laycancelcheque.setImageURI(picUri);
                    pathfour = picUri.getPath();
                    // pathelven = FileUtility.getPath(BasicInfoActivity.this,picUri);
                }


            }
        }

    }

    public void uploadDocument() {

        Log.e("tracker ", " upload document 2 called");

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
        MultipartBody.Part fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);


        RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
        MultipartBody.Part fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);

        MultipartBody.Part fileToSendfourteen = null;

        if (!paththree.equalsIgnoreCase("https://kamaii.in/app/")) {
            RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
            fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
        }

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.Add_PERSONAL_DOC2(fileToSendone, fileToSendtwo, fileToSendfourteen, null, userid);
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();
                        JSONObject object = new JSONObject(s);

                        Log.e("WEBBYKNIGHT", "" + s);


                        String message = object.getString("message");
                        int sstatus = object.getInt("status");


                        if (sstatus == 1) {
                            onBackPressed();
                            finish();
                            //  baseActivity.loadHomeFragment(new ArtistProfileNewFragment(), "new");
                          /*  if (bank_status.equalsIgnoreCase("0")) {

                                startActivity(new Intent(baseActivity, BankDocument.class));
                                finish();
                            } else {

                                onBackPressed();
                                finish();
                            }
*/

                        } else if (sstatus == 3) {
                            Intent intent1 = new Intent(BankWalletActivity.this, CheckSignInActivity.class);
                            startActivity(intent1);
                            finish();
                            Toast.makeText(BankWalletActivity.this, message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BankWalletActivity.this, message, LENGTH_LONG).show();
                        }
                    } else {

                        Log.e("DOCUMENT_ERROR", "" + response.errorBody().string());
                        Toast.makeText(BankWalletActivity.this, "Try again. Server is not responding" + "999",
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

                Log.e("failure", "" + t.toString());

                /*Toast.makeText(BasicInfoActivity.this, "Server Did Not Responding and Try again" + t,
                        LENGTH_LONG).show();*/
            }
        });

    }
}