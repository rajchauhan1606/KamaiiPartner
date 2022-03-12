package com.kamaii.partner.ui.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.HistoryDTO;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.Glob;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.ActivityViewInvoiceBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.preferences.SharedPrefrence;
import com.kamaii.partner.ui.adapter.AdapterInvoiceService;
import com.kamaii.partner.ui.fragment.PaidFrag;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.ProjectUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewInvoice extends AppCompatActivity {

    private ActivityViewInvoiceBinding binding;
    private HistoryDTO historyDTO;
    private Context mContext;
    // private LinearLayout llPdf;
    private Bitmap bitmap;
    double totalamount = 0, artistamount = 0, commisionamount = 0;
    private GridLayoutManager gridLayoutManager;
    AdapterInvoiceService adapterInvoiceService;
    private ArrayList<ProductDTO> productDTOArrayList;
    private String booking_id;
    private String TAG = PaidFrag.class.getSimpleName();
    private ArrayList<HistoryDTO> historyDTOList;
    private UserDTO userDTO;
    private SharedPrefrence prefrence;
    CustomTextViewBold servicecharge;
    String s_charge = "";
    RelativeLayout rlClick;
    CardView cardData;
    String targetPdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_invoice);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_invoice);
        mContext = ViewInvoice.this;
        // llPdf=findViewById(R.id.llPdf);
        prefrence = SharedPrefrence.getInstance(mContext);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        servicecharge = findViewById(R.id.service_digit_txt1);
        rlClick = findViewById(R.id.rlClick);
        cardData = findViewById(R.id.cardData);
        if (getIntent().hasExtra(Consts.BOOKING_ID)) {
            booking_id = getIntent().getStringExtra(Consts.BOOKING_ID);
        }
        getHistroy();
    }

    public void setUiAction() {

        if (historyDTOList.get(0).getIsmap().equalsIgnoreCase("1")) {
            binding.llLocation.setVisibility(View.VISIBLE);
            binding.lldestiLocation.setVisibility(View.VISIBLE);
            binding.view1.setVisibility(View.VISIBLE);
        } else {
            binding.llLocation.setVisibility(View.GONE);
            binding.lldestiLocation.setVisibility(View.GONE);
            binding.view1.setVisibility(View.GONE);
        }

        binding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(mContext).
                load(historyDTO.getArtistImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivProfile);
        gridLayoutManager = new GridLayoutManager(ViewInvoice.this, 1);
        productDTOArrayList = historyDTO.getProduct();
        adapterInvoiceService = new AdapterInvoiceService(mContext, productDTOArrayList, "0");
        binding.rvCart.setLayoutManager(gridLayoutManager);
        binding.rvCart.setAdapter(adapterInvoiceService);

        binding.tvDate.setText(ProjectUtils.changeDateFormate1(historyDTO.getBooking_date()) + " " + historyDTO.getBooking_time());
        binding.tvInvoiceId.setText(historyDTO.getInvoice_id());
        binding.tvName.setText(ProjectUtils.getFirstLetterCapital(historyDTO.getArtistName()));
        binding.tvServiceType.setText(historyDTO.getCategoryName());
        // binding.tvWork.setText(historyDTO.getProduct());
        binding.tvPrice.setText(Html.fromHtml("&#x20B9;" + historyDTO.getArtist_amount()));
        binding.tvSubtotal.setText(Html.fromHtml("&#x20B9;" + historyDTO.getItemtotal()));
        //binding.tvservicetotal.setText(historyDTO.getTotal_amount() +" "+"Rs");

        if (!historyDTO.getAddress().equalsIgnoreCase("")) {
            binding.etAddressSelectsource.setVisibility(View.VISIBLE);
            binding.etAddressSelectsource.setText(historyDTO.getAddress());
        }

        if (!historyDTO.getDestinationaddress().equalsIgnoreCase("")) {
            binding.etAddressSelectdesti.setVisibility(View.VISIBLE);
            binding.etAddressSelectdesti.setText(historyDTO.getDestinationaddress());
        } else {
            binding.etAddressSelectdesti.setVisibility(View.VISIBLE);
            binding.etAddressSelectdesti.setText(historyDTO.getAddress());
        }

        if (historyDTO.getPayment_type().equalsIgnoreCase("0")) {
            binding.tvptype.setText("Online Payment");
        } else if (historyDTO.getPayment_type().equalsIgnoreCase("1")) {
            binding.tvptype.setText("Cash Payment");
        } else if (historyDTO.getPayment_type().equalsIgnoreCase("2")) {
            binding.tvptype.setText("Wallet Payment");
        }
        totalamount = Double.parseDouble(historyDTO.getTotal_amount());
        artistamount = Double.parseDouble(historyDTO.getArtist_amount());

        try {
            commisionamount = totalamount - artistamount;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }

        DecimalFormat newFormat = new DecimalFormat("##.##");
        String mainprice = String.valueOf(newFormat.format(commisionamount));
        // binding.tvDiscount.setText(mainprice+" "+"Rs");

        binding.tvTotal.setText(Html.fromHtml("&#x20B9;" + historyDTO.getNetpay()));
        s_charge = historyDTO.getServicecharge();


        if (productDTOArrayList.get(0).getSub_category_id().equals("242") || productDTOArrayList.get(0).getSub_category_id().equals("434")) {
            binding.serviceTxt.setText("Driver Allowance");
            binding.serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
        } else if (productDTOArrayList.get(0).getSub_category_id().equals("453")) {
            binding.serviceTxt.setVisibility(View.GONE);
            binding.serviceDigitTxt1.setVisibility(View.GONE);
        } else {

            if (historyDTOList.get(0).getRider_order().equalsIgnoreCase("1")) {
                binding.serviceChargeRelative.setVisibility(View.GONE);
            } else {
                if (s_charge.equals("0")) {
                    binding.serviceDigitTxt1.setText("Free");
                } else {
                    binding.serviceDigitTxt1.setText(Html.fromHtml("&#x20B9;" + historyDTO.getServicecharge()));
                }
            }

        }

        try {
            //   binding.tvInvoiceDate.setText(ProjectUtils.convertTimestampDateToTime(ProjectUtils.correctTimestamp(Long.parseLong(historyDTO.getCreated_at()))));
            //  binding.tvInvoiceDate1.setText(ProjectUtils.convertTimestampDateToTime(ProjectUtils.correctTimestamp(Long.parseLong(historyDTO.getCreated_at()))));

        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnPdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
              //  Toast.makeText(mContext, "btn clicked", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(ViewInvoice.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ViewInvoice.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {

                    Log.i("SHIVAKASHI", "btn clicked!");

                    File file = saveBitMap(ViewInvoice.this, cardData);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
                    } else {
                        Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
                    }
                }
            }
        });
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpeg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
            Toast.makeText(ViewInvoice.this, "Invoice saved at Pictures/Handcare in your file manager", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        //scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }//create bitmap from view and returns it

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                public void onScanCompleted(String path, Uri uri) {

                    Log.e("GALLERY_URI1234",""+uri);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPdf() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        targetPdf = binding.tvInvoiceId.getText().toString() + ".pdf";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File myDir = new File(Environment.getExternalStorageDirectory().toString(), "/OkyBookPartner");


            if (!myDir.exists()) {
                boolean md = myDir.mkdirs();
                if (!md) {

                }
            }

            final File file = new File(myDir, targetPdf);
            if (file.exists()) {
                file.delete();
            }
            try {
                document.writeTo(new FileOutputStream(file));

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();
        }
        document.close();

        openGeneratedPDF();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openGeneratedPDF() {
        File file = new File(targetPdf);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(ViewInvoice.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            } catch (FileUriExposedException e) {

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           // bitmap = loadBitmapFromView(cardData, cardData.getMeasuredWidth(), cardData.getMeasuredHeight());
           // createPdf();
            File file = saveBitMap(ViewInvoice.this, cardData);    //which view you want to pass that view as parameter
            if (file != null) {
                Log.i("SHIVAKASHI", "Drawing saved to the gallery!");
            } else {
                Log.i("SHIVAKASHI", "Oops! Image could not be saved.");
            }
            //  File file = saveBitMap(ViewInvoice.this, business_cardmain);    //which view you want to pass that view as parameter
        } else {
            Toast.makeText(mContext, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        com.kamaii.partner.Glob.BUBBLE_VALUE = "0";
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.BOOKING_ID, booking_id);
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "1");
        return parms;
    }

    public boolean checkarss(String catid) {

        boolean value = false;
        for (int i = 0; i < BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value = true;
                break;
            } else {
                value = false;
            }

        }
        return value;
    }

    public void getHistroy() {
        ProjectUtils.showProgressDialog(mContext, true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_INVOICE_API2, getparm(), mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        Log.e("response_history", "" + response.toString());
                        historyDTOList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<HistoryDTO>>() {
                        }.getType();
                        historyDTOList = (ArrayList<HistoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        historyDTO = historyDTOList.get(0);
                        setUiAction();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
