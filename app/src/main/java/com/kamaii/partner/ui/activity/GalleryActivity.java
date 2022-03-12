package com.kamaii.partner.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.GalleryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityGalleryBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.adapter.BusinessCardSliderAdapter;
import com.kamaii.partner.ui.adapter.GalleryActivityAdapter;
import com.kamaii.partner.ui.models.BusinessCardSliderModel;

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

public class GalleryActivity extends AppCompatActivity {

    private String TAG = GalleryActivity.class.getSimpleName();
    RecyclerView promotionpager;
    DotsIndicator dotsIndicator;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    public static boolean card_flag = false;
    private ArrayList<GalleryDTO> GalleryImages = new ArrayList<>();
    RelativeLayout business_cardmain;
    ActivityGalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        prefrence = SharedPrefrence.getInstance(this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        progressDialog = new ProgressDialog(this);
        promotionpager = findViewById(R.id.rvGallery);
        business_cardmain = findViewById(R.id.business_cardmain);
        dotsIndicator = findViewById(R.id.dots2);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        findViewById(R.id.businesscardllBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        gettvideo();

    }

    public boolean getPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
            } else {

                return true;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            File file = saveBitMap(GalleryActivity.this, business_cardmain);    //which view you want to pass that view as parameter
        } else {

            findViewById(R.id.share_businesscard).performClick();

        }
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            Log.e("TRACKER12345", "1");
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.e("TRACKER12345", "2");
            Log.i("Tag", "Can't create directory to save the image");
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
        Log.e("TRACKER12345", "3");

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
                    Log.e("TRACKER12345", "5");
                    Log.e("TRACKER12345", "" + uri.toString());

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sharingIntent.setType("image/jpeg");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Business-Card");
                    // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gettvideo() {
        Log.e("Vider", "1");
        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ARTIST_GALLERY_API, parms, GalleryActivity.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {


                progressDialog.dismiss();
                if (flag) {

                    try {
                        Log.e("GallerySliderRES", "" + response.toString());
                        GalleryImages = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<GalleryDTO>>() {
                        }.getType();

                        GalleryImages = (ArrayList<GalleryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(GalleryActivity.this, 2);
                        promotionpager.setLayoutManager(gridLayoutManager);
                        promotionpager.setAdapter(new GalleryActivityAdapter(GalleryActivity.this, GalleryImages, userDTO));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("Vider", "4");

                }
            }
        });


    }

}