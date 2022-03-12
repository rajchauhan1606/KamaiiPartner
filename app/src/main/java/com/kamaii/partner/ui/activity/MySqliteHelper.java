package com.kamaii.partner.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.partner.R;
import com.kamaii.partner.ui.models.ContactModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;

public class MySqliteHelper extends AppCompatActivity {

    SQLiteDatabase db;
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> phone_no = new ArrayList<>();
    ArrayList<ContactModel> contactList;

    // ListView listView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listView=findViewById(R.id.list_n);

        contactList = new ArrayList<>();
        db = openOrCreateDatabase("kamaii.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS con_data ("
                    + "ID INTEGER primary key AUTOINCREMENT,"
                    + "Name TEXT,"
                    + "Phone TEXT,"
                    + "Fav INTEGER);";

            db.execSQL(CREATE_TABLE_CONTAIN);
        } catch (Exception e) {

        }

        File sdcard = Environment.getExternalStorageDirectory();

    }

    public void getFile(String file1) {

        File file = new File(file1);

        List<VCard> vcard;
        try {
            vcard = Ezvcard.parse(file).all();
            System.out.println("vafdfd " + vcard);
            for (VCard vCard : vcard) {
                List<Telephone> telePhoneNumbers = vCard.getTelephoneNumbers();
                List<FormattedName> nameListing = vCard.getFormattedNames();

                for (int i = 0; i < telePhoneNumbers.size(); i++) {
                    System.out.println(nameListing.get(0).getValue() + " -> " + telePhoneNumbers.get(i).getText());
                    String sql = "INSERT INTO con_data(Name, Phone, Fav) VALUES('" + nameListing.get(0).getValue().replaceAll("[^a-zA-Z0-9]", " ") + "','" + telePhoneNumbers.get(i).getText() + "',0)";
                    db.execSQL(sql);
                }
            }

            Cursor cc = db.rawQuery("SELECT * FROM con_data", null);

            if (cc != null) {
                cc.moveToFirst();
                startManagingCursor(cc);
                for (int i = 0; i < cc.getCount(); i++) {
                    name.add(cc.getString(1));
                    phone_no.add(cc.getString(2));
                    cc.moveToNext();
                }
            }


        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("asd " + e1);
        }
    }

    public void openWhatsapp() {

        try {


            //  ProjectUtils.showToast(getActivity(), msg);

            String phoneNumberWithCountryCode = "";


            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                    phoneNumberWithCountryCode, "hello"))));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}