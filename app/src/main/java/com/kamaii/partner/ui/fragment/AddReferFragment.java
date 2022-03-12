package com.kamaii.partner.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.activity.ContactListActivity;
import com.kamaii.partner.ui.models.ContactModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.ProjectUtils;
import com.webknight.filemanager.Constant;
import com.webknight.filemanager.activity.NormalFilePickActivity;
import com.webknight.filemanager.filter.entity.NormalFile;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;

import static android.app.Activity.RESULT_OK;

public class AddReferFragment extends Fragment {

    private View view;
    private BaseActivity baseActivity;
    LinearLayout btnenter;
    CustomEditText edtmobileno, edtfname, edtemail;
    RelativeLayout RRsncbar;
    private String TAG = AddReferFragment.class.getSimpleName();
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    //ProgressDialog progressDialog;
    SQLiteDatabase db;
    //  private HashMap<String, String> paramsGetMsg = new HashMap<>();
    ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();
    private HashMap<String, String> params = new HashMap<>();
    String name = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_addrefer, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.addrefer));
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);
        baseActivity.editImage.setVisibility(View.GONE);
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        btnenter = view.findViewById(R.id.btnenter);
        edtmobileno = view.findViewById(R.id.edtmobileno);
        edtfname = view.findViewById(R.id.edtfname);
        edtemail = view.findViewById(R.id.edtemail);
        RRsncbar = view.findViewById(R.id.RRsncbar);
        db = getActivity().openOrCreateDatabase("kamaii.db", android.content.Context.MODE_PRIVATE, null);
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS con_data ("
                    + "ID INTEGER primary key AUTOINCREMENT,"
                    + "Name TEXT,"
                    + "Phone TEXT,"
                    + "Fav INTEGER,"
                    + "AlreadySent INTEGER);";

            db.execSQL(CREATE_TABLE_CONTAIN);
        } catch (Exception ignored) {
           // Log.e("ERROR", "Error in creating table");
        }

        name = userDTO.getName();

        // Toast.makeText(getActivity(),name,Toast.LENGTH_SHORT).show();

        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!ValidateMobile()) {
                    return;
                } else if (!validation(edtfname, getResources().getString(R.string.val_name))) {
                    return;
                } else {
                    if (NetworkManager.isConnectToInternet(getActivity())) {

                        addrefer();
                    } else {
                        ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                    }
                }

            }
        });

        view.findViewById(R.id.upload_contacts_linear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(baseActivity, "Coming Soon.....", Toast.LENGTH_SHORT).show();
                //callSqlite();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                    StringBuilder builder = new StringBuilder();
                    for (NormalFile file : list) {
                        String path = file.getPath();
                        builder.append(path);
                    }
                  //  Log.e("KALAOO", "" + builder.toString());
                    File file = new File("", builder.toString());

                    List<VCard> vcard;
                    try {
                        vcard = Ezvcard.parse(file).all();
                        System.out.println("asd " + vcard);
                        for (VCard vCard : vcard) {
                            List<Telephone> telePhoneNumbers = vCard.getTelephoneNumbers();
                            List<FormattedName> nameListing = vCard.getFormattedNames();

                            for (int i = 0; i < telePhoneNumbers.size(); i++) {
                                System.out.println(nameListing.get(0).getValue() + " -> " + telePhoneNumbers.get(i).getText());
                                String sql = "INSERT INTO con_data(Name, Phone, Fav, AlreadySent) VALUES('" + nameListing.get(0).getValue().replaceAll("[^a-zA-Z0-9]", " ") + "','" + telePhoneNumbers.get(i).getText() + "',0,0)";
                                db.execSQL(sql);

                                if (i == (telePhoneNumbers.size() - 1)) {
                                    //this is the last iteration of for loop
                                    //progressDialog.dismiss();
                                    getActivity().startActivity(new Intent(getActivity(), ContactListActivity.class));
                                }
                            }
                        }

                        Cursor cc = db.rawQuery("SELECT * FROM con_data", null);

                        if (cc != null) {
                            cc.moveToFirst();
                            getActivity().startManagingCursor(cc);

                            for (int i = 0; i < cc.getCount(); i++) {
                                contactModelArrayList.add(new ContactModel(cc.getInt(0), cc.getString(1), cc.getString(2), cc.getInt(3), cc.getInt(4)));
                                cc.moveToNext();
                            }
                        }

                    } catch (IOException e1) {
                        e1.printStackTrace();
                        System.out.println("asd " + e1);
                    }
//                    mTvResult.setText(builder.toString());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 111) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                callSqlite();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;

    }

    public void callSqlite() {

        //progressDialog = new ProgressDialog(getActivity());
        //progressDialog.setMessage("Please Wait...");
        //progressDialog.show();
//                db = getActivity().openOrCreateDatabase("DATABASE", android.content.Context.MODE_PRIVATE, null);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        } else {


                new UploadContacts().execute();
//                    callSqlite();
        }
    }


    public void addrefer() {
        new HttpsRequest(Consts.Add_REFER, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {


                        //  ProjectUtils.showToast(getActivity(), msg);

                        String phoneNumberWithCountryCode = "+91" + edtmobileno.getText().toString();
                        String message = "Dear" + " " + edtfname.getText().toString() + "\n \n" + getResources().getString(R.string.wamsgone) + "" + name + "" + getResources().getString(R.string.wamsgtwo);

                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                                phoneNumberWithCountryCode, message))));

                        edtemail.setText("");
                        edtfname.setText("");
                        edtmobileno.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL_ID, "");
        parms.put(Consts.NAME, ProjectUtils.getEditTextValue(edtfname));
        parms.put(Consts.MOBILE, ProjectUtils.getEditTextValue(edtmobileno));
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public boolean ValidateMobile() {
        if (!ProjectUtils.isPhoneNumberValid(edtmobileno.getText().toString().trim())) {
            String msg = getResources().getString(R.string.val_mobile);
            Snackbar snackbar = Snackbar.make(RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }

    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            Snackbar snackbar = Snackbar.make(RRsncbar, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
            return false;
        } else {
            return true;
        }
    }

    class UploadContacts extends AsyncTask<String,String,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Cursor cc = db.rawQuery("SELECT * FROM con_data", null);
                if (cc != null) {
                    cc.moveToFirst();
                    getActivity().startManagingCursor(cc);

                    if (cc.getCount() < 1) {
                        Intent intent4 = new Intent(getActivity(), NormalFilePickActivity.class);
                        intent4.putExtra(Constant.MAX_NUMBER, 1);
                        intent4.putExtra(com.webknight.filemanager.activity.BaseActivity.IS_NEED_FOLDER_LIST, false);
                        intent4.putExtra(NormalFilePickActivity.SUFFIX,
                                new String[]{"vcf"});
                        startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
                        //Toast.makeText(getActivity(), "khali", Toast.LENGTH_SHORT).show();
                      //  Log.e("khali",""+"khali");
                    } else {
                        getActivity().startActivity(new Intent(getActivity(), ContactListActivity.class));
                       // Toast.makeText(getActivity(), "bharelu", Toast.LENGTH_SHORT).show();
                        //Log.e("khali",""+"bharelu");

                    }
                }
            }catch (Exception e){
            //    Log.e("ERROR",""+e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }
}
