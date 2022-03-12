package com.kamaii.partner.ui.fragment;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.FragmentDocumentTabBinding;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BankDocument;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.BasicInfoActivity;
import com.kamaii.partner.ui.activity.CheckSignInActivity;
import com.kamaii.partner.ui.models.newbooking.ArtistProfileDtoNew;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DocumentTabFragment extends Fragment implements View.OnClickListener {

    FragmentDocumentTabBinding binding;
    String flag = "";
    File fileone = null, filetwo = null, filethree = null, filefour = null;
    String pathone = "", pathtwo = "", paththree = "", pathfour = "";
    ProgressDialog progressDialog;
    BaseActivity baseActivity;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private Bundle bundle;
    private ArtistProfileDtoNew artistDetailsDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_document_tab, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        bundle = this.getArguments();
        artistDetailsDTO = (ArtistProfileDtoNew) bundle.getSerializable(Consts.ARTIST_DTO);
        //prefrence.setIntValue("tab", 1);

        showData();


        return binding.getRoot();
    }

    private void showData() {

        if (!artistDetailsDTO.getAddharCard().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(getContext()).load(Uri.parse(artistDetailsDTO.getAddharCard())).into(binding.layaafront);
        }
        if (!artistDetailsDTO.getAddharCardBack().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(getContext()).load(artistDetailsDTO.getAddharCardBack()).into(binding.layaaback);
        }
        if (!artistDetailsDTO.getPanCard().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(getContext()).load(artistDetailsDTO.getPanCard()).into(binding.laypancard);
        }
        if (!artistDetailsDTO.getCheque().equalsIgnoreCase("https://kamaii.in/app/")) {
            Glide.with(getContext()).load(artistDetailsDTO.getCheque()).into(binding.laycancelcheque);
        }

        if (artistDetailsDTO.getTracker().equalsIgnoreCase("0")) {
            binding.btnLayout.setVisibility(View.VISIBLE);
            binding.btnSubmit.setOnClickListener(this);
            binding.layaafront.setOnClickListener(this);
            binding.layaaback.setOnClickListener(this);
            binding.laypancard.setOnClickListener(this);
            binding.laycancelcheque.setOnClickListener(this);
        } else {
            binding.btnLayout.setVisibility(View.GONE);
        }


        pathone = artistDetailsDTO.getAddharCard();
        pathtwo = artistDetailsDTO.getAddharCardBack();
        paththree = artistDetailsDTO.getPanCard();
        pathfour = artistDetailsDTO.getCheque();
    }

    public void submitPersonalProfile() {
        // Toast.makeText(mContext, ""+user_profile_uri_str, Toast.LENGTH_SHORT).show();
        //


        if (NetworkManager.isConnectToInternet(getContext())) {


            if (pathone.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(getContext(), "Please Upload Aadhar Card Front Photo", Toast.LENGTH_LONG).show();
                return;
            } else if (pathtwo.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(getContext(), "Please Upload Aadhar Card Back Photo", Toast.LENGTH_LONG).show();
                return;
            } else if (pathfour.equalsIgnoreCase("https://kamaii.in/app/")) {
                Toast.makeText(getContext(), "Please Upload Cancel Cheque Photo", Toast.LENGTH_LONG).show();
                return;
            } else if (paththree.equalsIgnoreCase("https://kamaii.in/app/")) {
                paththree = "";
            } else {

                filethree = new File(paththree);
                fileone = new File(pathone);
                filetwo = new File(pathtwo);
                filefour = new File(pathfour);


                uploadImages();

            }


        } else {
            ProjectUtils.showToast(getContext(), getResources().getString(R.string.internet_concation));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 23) {

                Log.e("PLACE_API", "PLACE API CALLED 3");


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
                    Log.e("IMAGE123456", " pathOfImage " + pathfour);
                    // pathelven = FileUtility.getPath(BasicInfoActivity.this,picUri);
                }


            }
        }

    }

    public void uploadImages() {

        Log.e("dhaval tag ttt", " updateprofile 2 called");
        Log.e("dhaval tag ttt", " updateprofile " + pathone + " pathtwo " + pathtwo + " paththree " + paththree + " pathfour " + pathfour);

        MultipartBody.Part fileToSendone = null;
        MultipartBody.Part fileToSendtwo = null;
        MultipartBody.Part cancelChequepart = null;

        if (!pathone.equalsIgnoreCase("https://kamaii.in/app/")) {
            if (!pathone.equalsIgnoreCase(artistDetailsDTO.getAddharCard())) {
                Log.e("dhaval tag ttt", " 1 ");

                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileone);
                fileToSendone = MultipartBody.Part.createFormData("adhar_card", fileone.getName(), requestBody);
            }
        }
        if (!pathtwo.equalsIgnoreCase("https://kamaii.in/app/")) {
            if (!pathtwo.equalsIgnoreCase(artistDetailsDTO.getAddharCardBack())) {
                Log.e("dhaval tag ttt", " 2 ");

                RequestBody requestBodyt = RequestBody.create(MediaType.parse("multipart/form-data"), filetwo);
                fileToSendtwo = MultipartBody.Part.createFormData("adhar_card_back", filetwo.getName(), requestBodyt);
            }
        }
        if (!pathfour.equalsIgnoreCase("https://kamaii.in/app/")) {
            if (!pathfour.equalsIgnoreCase(artistDetailsDTO.getCheque())) {
                Log.e("dhaval tag ttt", " 3 ");

                RequestBody requestBodycheck = RequestBody.create(MediaType.parse("multipart/form-data"), filefour);
                cancelChequepart = MultipartBody.Part.createFormData("cancel_cheque", filefour.getName(), requestBodycheck);
            }
        }

        MultipartBody.Part fileToSendfourteen = null;


        if (!paththree.trim().isEmpty()) {
            if (!paththree.equalsIgnoreCase("https://kamaii.in/app/")) {
                if (!paththree.equalsIgnoreCase(artistDetailsDTO.getPanCard())) {
                    Log.e("dhaval tag ttt", " 3 ");

                    RequestBody requestBodyfourteen = RequestBody.create(MediaType.parse("multipart/form-data"), filethree);
                    fileToSendfourteen = MultipartBody.Part.createFormData("pan_card", filethree.getName(), requestBodyfourteen);
                }
            }


        }

        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userDTO.getUser_id());

        progressDialog.show();
        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.Add_PERSONAL_DOC2(fileToSendone, fileToSendtwo, fileToSendfourteen, cancelChequepart, userid);
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
                            Toast.makeText(getContext(), message, LENGTH_LONG).show();
                            baseActivity.loadHomeFragment(new ArtistProfileNewFragment(), "new");
                          /*  if (bank_status.equalsIgnoreCase("0")) {

                                startActivity(new Intent(baseActivity, BankDocument.class));
                                finish();
                            } else {

                                onBackPressed();
                                finish();
                            }
*/

                        } else if (sstatus == 3) {
                            Intent intent1 = new Intent(baseActivity, CheckSignInActivity.class);
                            startActivity(intent1);
                            getActivity().finish();
                            Toast.makeText(getContext(), message, LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), message, LENGTH_LONG).show();
                        }
                    } else {

                        Log.e("DOCUMENT_ERROR", "" + response.errorBody().string());
                        Toast.makeText(getContext(), "Try again. Server is not responding" + "999",
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

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layaafront:
                flag = "1";

                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.layaaback:
                flag = "2";

                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.laypancard:
                flag = "3";

                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;
            case R.id.laycancelcheque:
                flag = "4";

                ImagePicker.Companion.with(getActivity())
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;

            case R.id.btnSubmit:
                submitPersonalProfile();
                break;
        }
    }
}