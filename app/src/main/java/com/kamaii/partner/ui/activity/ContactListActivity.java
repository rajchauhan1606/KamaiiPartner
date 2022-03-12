package com.kamaii.partner.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kamaii.partner.R;
import com.kamaii.partner.service.GorgasAPI;
import com.kamaii.partner.ui.adapter.ContactAdapter;
import com.kamaii.partner.ui.fragment.AddReferFragment;
import com.kamaii.partner.ui.fragment.ReferCustomerFragment;
import com.kamaii.partner.ui.models.ContactModel;
import com.kamaii.partner.utils.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    ContactAdapter contactAdapter;
    RadioButton rb;
    RadioGroup radioGroup;
    RadioButton radio_favourate, radio_contact;
    List<ContactModel> contactList;
    List<ContactModel> favContactList;
    ExpandableHeightGridView recyclerView;
    int radiopos = 1;
    public int rb_pos = 1;
    SQLiteDatabase db;
    EditText svSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactListActivity.this, BaseActivity.class));
            }
        });
        Cursor cc = db.rawQuery("SELECT * FROM con_data WHERE Fav ='0'", null);
       // Toast.makeText(ContactListActivity.this, "" + cc.getCount(), Toast.LENGTH_SHORT).show();
        if (cc != null) {
            cc.moveToFirst();
            startManagingCursor(cc);

            for (int i = 0; i < cc.getCount(); i++) {
                contactList.add(new ContactModel(cc.getInt(0), cc.getString(1), cc.getString(2), cc.getInt(3), cc.getInt(4)));
                cc.moveToNext();
            }
        }
      //  Log.e("CONTACTLIST", "" + contactList.toString());

        loadAdapter(contactList, rb_pos);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb) {
                    if (rb.getText().toString().equalsIgnoreCase("favourite")) {
                        rb_pos = 0;
                       // Toast.makeText(ContactListActivity.this, "favourite", Toast.LENGTH_SHORT).show();
                        favContactList.clear();

                        Cursor cc = db.rawQuery("SELECT * FROM con_data WHERE Fav ='1'", null);
                        //Toast.makeText(ContactListActivity.this, "" + cc.getCount(), Toast.LENGTH_SHORT).show();
                        if (cc != null) {
                            cc.moveToFirst();
                            startManagingCursor(cc);

                            for (int i = 0; i < cc.getCount(); i++) {
                                favContactList.add(new ContactModel(cc.getInt(0), cc.getString(1), cc.getString(2), cc.getInt(3), cc.getInt(4)));
                                cc.moveToNext();
                            }
                        }
                        if (favContactList.size() == 0) {
                            findViewById(R.id.contact_scroll).setVisibility(View.GONE);
                            findViewById(R.id.no_contacts_txt).setVisibility(View.VISIBLE);
                        } else {
                            if (findViewById(R.id.contact_scroll).getVisibility() == View.GONE) {
                                findViewById(R.id.contact_scroll).setVisibility(View.VISIBLE);
                                findViewById(R.id.no_contacts_txt).setVisibility(View.GONE);
                            }
                            loadAdapter(favContactList, rb_pos);
                        }
                      //  Log.e("FAV_CONTACTLIST", "" + favContactList.toString());
                        radio_favourate.setBackground(getResources().getDrawable(R.drawable.radio_btn_selectedleft));
                        radio_contact.setBackground(getResources().getDrawable(R.drawable.radio_btn_unselected_right));
                        radio_contact.setTextColor(getResources().getColor(R.color.black));
                        radio_favourate.setTextColor(getResources().getColor(R.color.white));
                        contactAdapter.NotifyAll(favContactList);

                    } else {
                        //Toast.makeText(ContactListActivity.this, "Cnt", Toast.LENGTH_SHORT).show();
                        rb_pos = 1;
                        contactList.clear();
                        Cursor cc = db.rawQuery("SELECT * FROM con_data WHERE Fav ='0'", null);
                        //Toast.makeText(ContactListActivity.this, "" + cc.getCount(), Toast.LENGTH_SHORT).show();
                        if (cc != null) {
                            cc.moveToFirst();
                            startManagingCursor(cc);

                            for (int i = 0; i < cc.getCount(); i++) {
                                contactList.add(new ContactModel(cc.getInt(0), cc.getString(1), cc.getString(2), cc.getInt(3), cc.getInt(4)));
                                cc.moveToNext();
                            }
                        }
                       // Log.e("CONTACTLIST", "" + contactList.toString());

                        if (contactList.size() == 0) {
                            findViewById(R.id.contact_scroll).setVisibility(View.GONE);
                            findViewById(R.id.no_contacts_txt).setVisibility(View.VISIBLE);
                        } else {
                            if (findViewById(R.id.contact_scroll).getVisibility() == View.GONE) {
                                findViewById(R.id.contact_scroll).setVisibility(View.VISIBLE);
                                findViewById(R.id.no_contacts_txt).setVisibility(View.GONE);
                            }
                            loadAdapter(contactList, rb_pos);
                        }
                        radio_contact.setBackground(getResources().getDrawable(R.drawable.radio_btn_selected));
                        radio_favourate.setBackground(getResources().getDrawable(R.drawable.radio_btn_unselected));
                        radio_favourate.setTextColor(getResources().getColor(R.color.black));
                        radio_contact.setTextColor(getResources().getColor(R.color.white));
                        contactAdapter.NotifyAll(contactList);

                    }
                }
            }
        });

        svSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    contactAdapter.filter(charSequence.toString());

                } else {


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void init() {

        radioGroup = findViewById(R.id.contactlist_radiogroup);
        svSearch = findViewById(R.id.contact_list_svSearch);
        radio_favourate = findViewById(R.id.radio_favourate);
        radio_contact = findViewById(R.id.radio_contact);
        recyclerView = findViewById(R.id.rv_contactlist);
        contactList = new ArrayList<>();
        favContactList = new ArrayList<>();
        db = this.openOrCreateDatabase("kamaii.db", android.content.Context.MODE_PRIVATE, null);
    }


    public void loadAdapter(List<ContactModel> array, int rb_pos) {

        contactAdapter = new ContactAdapter(ContactListActivity.this, array, rb_pos);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setExpanded(true);
        recyclerView.setAdapter(contactAdapter);
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(ContactListActivity.this, BaseActivity.class));
        finish();
    }

    public void loadHomeFragment(final Fragment fragment) {

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
               /* fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);*/
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

    }
}