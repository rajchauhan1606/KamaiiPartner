package com.kamaii.partner.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kamaii.partner.R;
import com.kamaii.partner.interfacess.ImagePickSetOnclickListner;

import java.io.File;
import java.lang.reflect.Method;


public class SelectBottomDialogFragment extends BottomSheetDialogFragment {
    LinearLayout layout_camera,layout_photo;
    ImagePickSetOnclickListner imageSetOnclickListner;
    ImageView img_close;
    private Context context;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLARY_REQUEST = 1890;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;
    private Uri uri;
    private File filePath;
    private String headertitle;
   /* public static SelectBottomDialogFragment newInstance() {
        return new SelectBottomDialogFragment();
    }*/
   public SelectBottomDialogFragment(Context context, String headertitle, ImagePickSetOnclickListner imageSetOnclickListner)
    {
        this.context=context;
        this.headertitle=headertitle;
        this.imageSetOnclickListner=imageSetOnclickListner;
    }
    public SelectBottomDialogFragment()
    {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       // return inflater.inflate(R.layout.bottom_sheet_dialog, container);
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_two, container, false);
        context=getActivity();
        initView(view);
        setCancelable(false);
        return view;
    }
    private void initView(View view)
    {
        layout_camera=view.findViewById(R.id.layout_camera);
        layout_photo=view.findViewById(R.id.layout_photo);
        img_close=view.findViewById(R.id.img_close);
        layout_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkPermissionForCamera())
                {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    //File file = new File(Environment.getExternalStorageDirectory(), "file" + "app_image" + ".jpg");
                    disableException();
                    uri= Uri.fromFile(file);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });
        layout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkPermissionForExternalStorage())
                {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, GALLARY_REQUEST);
                }
                else
                {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public static void disableException()
    {
        if(Build.VERSION.SDK_INT>=24)
        {
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("TAG", String.valueOf(uri));
            this.imageSetOnclickListner.Camera(uri);
            dismiss();
            //filePath = new File(FileUtility.getPath(context, uri));
            //imagepath.add(filepath);


        } else if (requestCode == GALLARY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Log.d("TAG", String.valueOf(selectedImage));
            this.imageSetOnclickListner.Gallary(selectedImage);
           // filePath = new File(FileUtility.getPath(context, selectedImage));

            dismiss();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    layout_camera.performClick();
                }
                break;
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    layout_photo.performClick();
                }
                break;
        }
    }
    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    public void setOnClickListener(ImagePickSetOnclickListner imageSetOnclickListner)
    {
        this.imageSetOnclickListner=imageSetOnclickListner;
    }
}
