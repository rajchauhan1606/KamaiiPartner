package com.kamaii.partner.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.adapter.BirthdayCardSliderAdapter;
import com.kamaii.partner.ui.adapter.BusinessCardSliderAdapter;
import com.kamaii.partner.ui.models.BusinessCardSliderModel;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.ImageCompression;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class BirthdayCardActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = BirthdayCardActivity.class.getSimpleName();
    ViewPager promotionpager;
    DotsIndicator dotsIndicator;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private ArrayList<BusinessCardSliderModel> tvideoModelArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    public static boolean card_flag = false;
    private HashMap<String, String> parmsCategory = new HashMap<>();
    private ArrayList<CategoryDTO> categoryDTOS = new ArrayList<>();
    TextInputEditText customEditText;
    RelativeLayout birthday_cardmain;
    LinearLayout applay_btn;
    String pathOfImage = "";
    ImageCompression imageCompression;
    File file;
    CircleImageView customer_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_card);

        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(this);
        promotionpager = findViewById(R.id.birthdaypager);
        applay_btn = findViewById(R.id.applay_btn);
        customEditText = findViewById(R.id.custoer_name_etx);
        birthday_cardmain = findViewById(R.id.birthday_cardmain);
        customer_image = findViewById(R.id.birthday_preview_image);
        dotsIndicator = findViewById(R.id.dots22);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());

        findViewById(R.id.applay_btn).setOnClickListener(this);
        findViewById(R.id.birthday_preview_image).setOnClickListener(this);
        findViewById(R.id.birthdaycardllBack).setOnClickListener(this);
        findViewById(R.id.share_birthdaycard).setOnClickListener(this);

        getCategory();


    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }//create bitmap from view and returns it

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Business-Card");
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCategory() {


        progressDialog.show();
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        Log.e("getAllCaegory123", "" + response.toString());
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        gettvideo();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }

    public void gettvideo() {
        Log.e("Vider", "1");
        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_BIRTHDAY_CARD_IMAGES, parms, BirthdayCardActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                progressDialog.dismiss();
                if (flag) {

                    try {
                        Log.e("SliderRES", "" + response.toString());
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<BusinessCardSliderModel>>() {
                        }.getType();

                        card_flag = true;
                        tvideoModelArrayList = (ArrayList<BusinessCardSliderModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);

                        dotsIndicator.attachViewPager(promotionpager);

                        /*Timer timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });


    }

    public void loadAdapter(String s, String pathOfImage) {

        promotionpager.setAdapter(new BirthdayCardSliderAdapter(BirthdayCardActivity.this, tvideoModelArrayList, userDTO, categoryDTOS, s,pathOfImage));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.birthday_preview_image:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(23);
                break;

            case R.id.birthdaycardllBack:
                onBackPressed();
                break;

            case R.id.share_birthdaycard:
                File file = saveBitMap(BirthdayCardActivity.this, birthday_cardmain);
                break;

            case R.id.applay_btn:
                String str = customEditText.getText().toString();
                loadAdapter(str,pathOfImage);
                findViewById(R.id.share_birthdaycard).setEnabled(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 23) {
                Uri picUri = data.getData();

                pathOfImage = picUri.getPath();
                imageCompression = new ImageCompression(BirthdayCardActivity.this);
                imageCompression.execute(pathOfImage);
                imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                    @Override
                    public void processFinish(String imagePath) {
                        try {
                            file = new File(imagePath);


                            Glide.with(BirthdayCardActivity.this).load("file://" + imagePath)
                                    .thumbnail(0.5f)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(customer_image);

                            if (NetworkManager.isConnectToInternet(BirthdayCardActivity.this)) {
                                //updateProfileSelf();
                            } else {
                                ProjectUtils.showToast(BirthdayCardActivity.this, getResources().getString(R.string.internet_concation));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            try {
                BirthdayCardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (promotionpager.getCurrentItem() < tvideoModelArrayList.size() - 1) {
                            promotionpager.setCurrentItem(promotionpager.getCurrentItem() + 1);
                        } else {
                            promotionpager.setCurrentItem(0);
                        }
                    }
                });
            } catch (Exception e) {

            }

        }
    }
}