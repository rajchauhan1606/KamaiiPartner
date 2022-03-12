package com.kamaii.partner.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.fragment.AddReferFragment;
import com.kamaii.partner.ui.models.ContactModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ContactAdapter extends BaseAdapter {

    Context context;
    List<ContactModel> arraylist;
    List<ContactModel> searchBookingsList;
    private String TAG = ContactAdapter.class.getSimpleName();
    int rb_pos = 0;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    String refername, phone_num;
    String sender_name;
    SQLiteDatabase db;
    int pos = -1;

    public ContactAdapter(Context context, List<ContactModel> arraylist, int rb_pos) {
        this.context = context;
        this.arraylist = arraylist;
        searchBookingsList = new ArrayList<>();
        prefrence = SharedPrefrence.getInstance(context);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        searchBookingsList.addAll(arraylist);
        sender_name = userDTO.getName();
        db = context.openOrCreateDatabase("kamaii.db", android.content.Context.MODE_PRIVATE, null);

        this.rb_pos = rb_pos;
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.EMAIL_ID, "");
        parms.put(Consts.NAME, refername);
        parms.put(Consts.MOBILE, phone_num);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        return parms;
    }

    public void addrefer() {
        new HttpsRequest(Consts.Add_REFER, getparm(), context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {


                        //  ProjectUtils.showToast(getActivity(), msg);

                        String phoneNumberWithCountryCode = phone_num;
                        String message = "Dear" + " " + refername + "\n \n" + context.getResources().getString(R.string.wamsgone) + "" + sender_name + "" + context.getResources().getString(R.string.wamsgtwo);

                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                                phoneNumberWithCountryCode, message))));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
              //      ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public void NotifyAll(List<ContactModel> contactList) {
        //  this.productDTOList.clear();
        this.arraylist = contactList;
        notifyDataSetChanged();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylist.clear();
        if (charText.length() == 0) {
            arraylist.addAll(searchBookingsList);
        } else {
            for (ContactModel historyDTO : searchBookingsList) {
                if (historyDTO.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    arraylist.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_contactlist_layout, null);

        CustomTextView name, phone_txt, refer_btn, partner_name, partner_phone;
        RelativeLayout relativeLayout;
        CardView cardView;
        ImageView fav_img;

        name = view.findViewById(R.id.partner_name);
        phone_txt = view.findViewById(R.id.partner_phone);
        refer_btn = view.findViewById(R.id.contactlist_send_btn);
        relativeLayout = view.findViewById(R.id.contactlist_relative_layout);
        cardView = view.findViewById(R.id.contactlist_cardview);
        partner_name = view.findViewById(R.id.partner_name);
        partner_phone = view.findViewById(R.id.partner_phone);
        fav_img = view.findViewById(R.id.favourite_icon);

        name.setText(arraylist.get(position).getName());
        phone_txt.setText(arraylist.get(position).getPhone_number());

        if (rb_pos == 0) {
            view.findViewById(R.id.favourite_icon).setVisibility(View.INVISIBLE);
        } else {
            view.findViewById(R.id.favourite_icon).setVisibility(View.VISIBLE);
        }

        if (arraylist.get(position).getFavourite() == 1) {
            fav_img.setBackground(context.getResources().getDrawable(R.drawable.selected_fav));
        }
        fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "UPDATE con_data SET Fav ='1' WHERE ID =" + arraylist.get(position).getId();
                db.execSQL(query);
                pos = position;
                notifyDataSetChanged();

            }
        });
        if (pos == position) {
            fav_img.setBackground(context.getResources().getDrawable(R.drawable.selected_fav));
        } else {
            fav_img.setBackground(context.getResources().getDrawable(R.drawable.unselected_fav));
        }
        refer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refername = arraylist.get(position).getName();
                phone_num = arraylist.get(position).getPhone_number();

                String query = "UPDATE con_data SET AlreadySent ='1' WHERE ID =" + arraylist.get(position).getId();
                db.execSQL(query);
                cardView.setBackgroundResource(R.drawable.relative_colored_background);
                partner_name.setTextColor(Color.WHITE);
                partner_phone.setTextColor(Color.WHITE);
                refer_btn.setTextColor(Color.BLACK);
                refer_btn.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
                notifyDataSetChanged ();
                if (NetworkManager.isConnectToInternet(context)) {

                    addrefer();
                } else {
              //      ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                }
            }
        });

       // Log.e("ALREADY_LIST", "" + arraylist.get(position).getAlreadysent());
       // Log.e("ID_LIST", "" + arraylist.get(position).getName());

        if (arraylist.get(position).getAlreadysent() == 1) {
            cardView.setBackgroundResource(R.drawable.relative_colored_background);
            partner_name.setTextColor(Color.WHITE);
            partner_phone.setTextColor(Color.WHITE);
            refer_btn.setTextColor(Color.BLACK);
            refer_btn.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
        }

        return view;
    }
}
