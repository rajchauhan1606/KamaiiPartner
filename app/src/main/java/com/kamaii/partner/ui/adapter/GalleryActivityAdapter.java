package com.kamaii.partner.ui.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kamaii.partner.DTO.CategoryDTO;
import com.kamaii.partner.DTO.GalleryDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.ui.activity.GalleryActivity;
import com.kamaii.partner.ui.models.BusinessCardSliderModel;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ImagePreviewerUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivityAdapter extends RecyclerView.Adapter<GalleryActivityAdapter.GalleryViewHolder> {

    private ArrayList<GalleryDTO> images;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<CategoryDTO> categoryDTOS;
    UserDTO userDTO;

    ImageView myImage;
    ListView imagelistview;
    CustomTextViewBold b_name;
    CustomTextViewBold partner_name;
    CustomTextViewBold business_services_name;

    public GalleryActivityAdapter(Context context, ArrayList<GalleryDTO> images, UserDTO userDTO) {
        this.context = context;
        this.images = images;
        this.userDTO = userDTO;
        // this.categoryDTOS = categoryDTOS;
        inflater = LayoutInflater.from(context);

        Log.e("Sliderimagesize1234", "" + images.size());
    }


    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myImageLayout = inflater.inflate(R.layout.gallery_slide4, parent, false);

        return new GalleryViewHolder(myImageLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {

        Glide.with(context)
                .load(images.get(position).getImage())
                .placeholder(R.drawable.dafault_product)
                .into(holder.myImage);

        holder.myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                show(context, holder.myImage, images.get(position).getImage());
            }
        });
        // myImage.setImageResource(Integer.parseInt(images.get(position).getImage()));


    }

    @SuppressLint("ClickableViewAccessibility")
    public void show(Context context, ImageView source, String s) {
        BitmapDrawable background = ImagePreviewerUtils.getBlurredScreenDrawable(context, source.getRootView());

        View dialogView = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.previewer_image);
        CustomTextViewBold ic_close_imaging = (CustomTextViewBold) dialogView.findViewById(R.id.ic_close_imaging);
        CustomTextViewBold sharing_image = (CustomTextViewBold) dialogView.findViewById(R.id.sharing_image);

        Glide
                .with(context)
                .load(s)
                .placeholder(R.drawable.dummyuser_image)
                .into(imageView);


        final Dialog dialog = new Dialog(context, R.style.ImagePreviewerTheme);
        dialog.getWindow().setBackgroundDrawable(background);
        dialog.setContentView(dialogView);
        dialog.setCancelable(true);
        dialog.show();
        ic_close_imaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                dialog.dismiss();
            }
        });
        sharing_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "asas", Toast.LENGTH_SHORT).show();

                if (((GalleryActivity)context).getPermission()){

                    File file = saveBitMap(context, imageView);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                    } else {
                        Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                    }
                }

            }
        });
        source.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (dialog.isShowing()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    int action = event.getActionMasked();
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        dialog.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
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
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Gallery");
                    // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    class GalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView myImage;
        ListView imagelistview;
        CustomTextViewBold b_name;
        CustomTextViewBold partner_name;
        CustomTextViewBold business_services_name;

        public GalleryViewHolder(@NonNull View itemView) {
            super(itemView);


            myImage = itemView.findViewById(R.id.image2);
            imagelistview = itemView.findViewById(R.id.slide2_listview);
            b_name = itemView.findViewById(R.id.b_name);
            partner_name = itemView.findViewById(R.id.business_card_partner_name);
            business_services_name = itemView.findViewById(R.id.business_services_name);
        }
    }

}



