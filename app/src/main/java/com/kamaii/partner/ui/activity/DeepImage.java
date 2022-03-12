package com.kamaii.partner.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.ui.adapter.AdapterGallery;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DeepImage extends AppCompatActivity implements View.OnClickListener {
  //  Toolbar tooling;
    RelativeLayout deepRelative;
    String imaging, imaging_id, user_id;
    ImageView deepImage;
    private Context context;
    private String TAG = DeepImage.class.getSimpleName();
    private HashMap<String, String> parms = new HashMap<>();
    private DialogInterface dialog_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_image);
       // tooling = (Toolbar) findViewById(R.id.tooling);
        deepRelative = (RelativeLayout) findViewById(R.id.deepRelative);
        deepImage = (ImageView) findViewById(R.id.deepImage);
       // setSupportActionBar(tooling);
//        tooling.setTitleTextColor(Color.WHITE);
        //getSupportActionBar().setTitle("My Business Profile");
        imaging_id = getIntent().getStringExtra("imaging_id");
        imaging = getIntent().getStringExtra("imaging");
        user_id = getIntent().getStringExtra("user_id");

        findViewById(R.id.gallerycardllBack).setOnClickListener(this);
        findViewById(R.id.action_shareing).setOnClickListener(this);
        Glide
                .with(DeepImage.this)
                .load(imaging)
                .placeholder(R.drawable.dummyuser_image)
                .into(deepImage);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            File file = saveBitMap(DeepImage.this, deepImage);    //which view you want to pass that view as parameter
        } else {

            findViewById(R.id.deepRelative).performClick();

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
        scanGallery(DeepImage.this, pictureFile.getAbsolutePath());
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

    public void deleteDialog() {
        try {
            new AlertDialog.Builder(DeepImage.this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(DeepImage.this.getResources().getString(R.string.delete_gallery))
                    .setMessage(DeepImage.this.getResources().getString(R.string.delete_gallery_msg))
                    .setCancelable(false)
                    .setPositiveButton(DeepImage.this.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            deleteGallery();

                        }
                    })
                    .setNegativeButton(DeepImage.this.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGallery() {
        new HttpsRequest(Consts.DELETE_GALLERY_API, parms, DeepImage.this).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    onBackPressed();
                } else {
                    ProjectUtils.showLong(DeepImage.this, msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.gallerycardllBack:
                onBackPressed();
                break;

            case R.id.action_shareing:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1011);
                    } else {

                        File file = saveBitMap(DeepImage.this, deepRelative);    //which view you want to pass that view as parameter
                        if (file != null) {
                            Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                        } else {
                            Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                        }
                    }
                }
                break;

        }
    }
}