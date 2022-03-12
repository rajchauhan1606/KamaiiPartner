package com.kamaii.partner.ui.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.AdapterCabBookingsBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.fragment.BookingWithRiderFragment;
import com.kamaii.partner.ui.fragment.CabBookingsFragment;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.OnTouch;
import com.kamaii.partner.utils.ProjectUtils;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterBookingWithRider extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = AdapterBookingWithRider.class.getSimpleName();
    private BookingWithRiderFragment newBookings;
    private ArrayList<ArtistBooking> artistBookingsList;
    private ArrayList<ArtistBooking> searchBookingsList;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private Context context;
    private HashMap<String, String> paramsBookingOp;
    private HashMap<String, String> paramsDecline;
    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<ProductDTO> productDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private Dialog dialog, dialogcustomservice, dialogdecline, dialogapproxtime, dialogcamera, dialogpriview, dialogfinish, dailograting;
    private CustomTextViewBold tvdCancelreason, tvdAddreason, tvcameraskip, tvcamera, tvupload, tvtotalfinish, tvfinishcancel, tvfinishsubmit, tvfinishsubmitrating;
    String locationstatus = "";
    CustomTextViewBold tvskip, tvupdate;
    CustomEditText etd_reason;
    CheckBox checkone, checktwo, checkthree, checkfour, checkfive, checksix;
    LinearLayout llDate;
    String reason;
    private SimpleDateFormat sdf1;
    CustomTextView tvapproxDate;
    private Date date;
    ImageView imgclose;
    public static ImageView img_upload;
    private int mHour, mMinute;
    AdapterCartCab adapterCart;
    Boolean isCheck = true;
    String cancelreason = "";
    ProgressDialog progressDialog;
    LinearLayout layfinishbackground;
    private RatingBar rbReview;
    private float myrating;

    public AdapterBookingWithRider(BookingWithRiderFragment newBookings, ArrayList<ArtistBooking> artistBookingsList, UserDTO userDTO, LayoutInflater myInflater) {
        this.newBookings = newBookings;
        this.artistBookingsList = artistBookingsList;
        this.searchBookingsList = new ArrayList<ArtistBooking>();
        this.searchBookingsList.addAll(artistBookingsList);
        this.userDTO = userDTO;
        this.myInflater = myInflater;
        context = newBookings.getActivity();
     //   Log.e("ABCD", "" + "sdkhgcbhadsbvjhadsn");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (myInflater == null) {
            myInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == VIEW_ITEM) {
            AdapterCabBookingsBinding binding =
                    DataBindingUtil.inflate(myInflater, R.layout.adapter_cab_bookings, parent, false);
            vh = new MyViewHolder(binding);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
            vh = new AdapterCabBookings.MyViewHolderSection(v);
        }
        return vh;
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, int pos) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        date = new Date();
        paramsBookingOp = new HashMap<>();
        dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dailog_add_services);
        dialog.setCancelable(false);

        dialogcustomservice = new Dialog(context, R.style.Theme_Dialog);
        dialogcustomservice.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcustomservice.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialogcustomservice.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogcustomservice.setContentView(R.layout.dailog_add_custom_services);
        dialogcustomservice.setCancelable(false);

        dialogapproxtime = new Dialog(context, R.style.Theme_Dialog);
        dialogapproxtime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogapproxtime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialogapproxtime.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogapproxtime.setContentView(R.layout.dailog_approxtime);
        dialogapproxtime.setCancelable(false);


        dialogdecline = new Dialog(context, R.style.Theme_Dialog);
        dialogdecline.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdecline.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogdecline.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogdecline.setContentView(R.layout.dailog_decline);
        dialogdecline.setCancelable(false);


        dialogcamera = new Dialog(context, R.style.Theme_Dialog);
        dialogcamera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogcamera.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogcamera.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogcamera.setContentView(R.layout.dailog_camera);
        dialogcamera.setCancelable(false);


        dialogfinish = new Dialog(context, R.style.Theme_Dialog);
        dialogfinish.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogfinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogfinish.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogfinish.setContentView(R.layout.dailog_finish);
        dialogfinish.setCancelable(false);


        dailograting = new Dialog(context, R.style.Theme_Dialog);
        dailograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailograting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailograting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dailograting.setContentView(R.layout.dailog_rating);
        dailograting.setCancelable(false);

        rbReview = dailograting.findViewById(R.id.rbReview);

        tvfinishsubmitrating = dailograting.findViewById(R.id.tvfinishsubmitrating);

        dialogpriview = new Dialog(context, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_priview);
        dialogpriview.setCancelable(true);


        final int position = pos;


        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());


        if (holderMain instanceof AdapterCabBookings.MyViewHolder) {

            final AdapterCabBookings.MyViewHolder holder = (AdapterCabBookings.MyViewHolder) holderMain;

            gridLayoutManager = new GridLayoutManager(context, 1);
            productDTOArrayList = new ArrayList<>();


            productDTOArrayList = artistBookingsList.get(position).getProduct();
            locationstatus = artistBookingsList.get(position).getLocation_status();

            holder.binding.txtcat.setText(artistBookingsList.get(position).getCategory_name());

            adapterCart = new AdapterCartCab(newBookings, productDTOArrayList, locationstatus);
            holder.binding.rvCart.setLayoutManager(gridLayoutManager);
            holder.binding.rvCart.setAdapter(adapterCart);

            holder.binding.tvName.setText(artistBookingsList.get(position).getUserName());

            holder.binding.tvDate.setText(ProjectUtils.changeDateFormate1(artistBookingsList.get(position).getBooking_date()) + " " + artistBookingsList.get(position).getBooking_time());


            Glide.with(context).
                    load(Consts.IMAGE_URL + artistBookingsList.get(position).getImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .into(holder.binding.ivArtist);


            if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("0")) {
                holder.binding.txtptype.setVisibility(View.VISIBLE);
                holder.binding.txtptype.setText("Online Payment");

            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("1")) {
                holder.binding.txtptype.setVisibility(View.VISIBLE);
                holder.binding.txtptype.setText("Cash");

            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("2")) {
                holder.binding.txtptype.setVisibility(View.VISIBLE);
                holder.binding.txtptype.setText("Wallet Payment");

            } else {
                holder.binding.txtptype.setVisibility(View.GONE);
            }

            //  holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getDestinationaddress());


            holder.binding.imgphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!artistBookingsList.get(position).getUserMobile().equalsIgnoreCase("")) {

                        String mobileno = artistBookingsList.get(position).getUserMobile();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileno));
                        context.startActivity(intent);
                    }

                }
            });

            holder.binding.llLocationdesti.setVisibility(View.VISIBLE);
            if (!artistBookingsList.get(position).getDestinationaddress().equalsIgnoreCase("")) {

                holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getDestinationaddress());
            } else {
                holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getAddress());

            }

            holder.binding.tvLocation.setText(artistBookingsList.get(position).getAddress());

            holder.binding.tvtotal.setText(artistBookingsList.get(position).getPrice() + " " + "Rs");


            Glide.with(context).
                    load(artistBookingsList.get(position).getUserImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            if (checkarss(productDTOArrayList.get(0).getCategory_id())) {

                if (productDTOArrayList.get(0).getSub_category_id().equalsIgnoreCase("453") || productDTOArrayList.get(0).getSub_category_id().equalsIgnoreCase("242") || productDTOArrayList.get(0).getSub_category_id().equalsIgnoreCase("455")) {
                    holder.binding.tvroundtrip.setVisibility(View.GONE);
                } else {

                    if (productDTOArrayList.get(0).getRoundtrip().equalsIgnoreCase("1")) {
                        holder.binding.tvroundtrip.setVisibility(View.VISIBLE);
                        holder.binding.tvroundtrip.setText("Round Trip");
                    } else if (productDTOArrayList.get(0).getRoundtrip().equalsIgnoreCase("0")) {
                        holder.binding.tvroundtrip.setVisibility(View.VISIBLE);
                        holder.binding.tvroundtrip.setText("One Way");
                    } else {
                        holder.binding.tvroundtrip.setVisibility(View.GONE);
                    }

                }

            } else {

                if (locationstatus.equalsIgnoreCase("1")) {
                    holder.binding.tvroundtrip.setVisibility(View.VISIBLE);
                    holder.binding.tvroundtrip.setText("Delivery");
                } else if (locationstatus.equalsIgnoreCase("0")) {
                    holder.binding.tvroundtrip.setVisibility(View.VISIBLE);
                    holder.binding.tvroundtrip.setText("Pick Up");
                } else {
                    holder.binding.tvroundtrip.setVisibility(View.GONE);
                }

            }


            holder.binding.txtmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck) {
                        holder.binding.laymoreee.setVisibility(View.VISIBLE);
                        isCheck = false;
                    } else {
                        holder.binding.laymoreee.setVisibility(View.GONE);
                        isCheck = true;
                    }
                }
            });


            holder.binding.startnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                    if (artistBookingsList.get(position).getPrice().equalsIgnoreCase("0")) {
                        Toast.makeText(context, "Please Select At Least One Service", Toast.LENGTH_LONG).show();
                    } else {
                        if (NetworkManager.isConnectToInternet(context)) {


                            if (checkarss(artistBookingsList.get(position).getProduct().get(0).getCategory_id())) {
                                holder.binding.startnew.setVisibility(View.GONE);
                                bookingstart("2", position, "");
                            } else {
                                llDate = dialogapproxtime.findViewById(R.id.llDate);
                                tvskip = dialogapproxtime.findViewById(R.id.tvskip);
                                tvupdate = dialogapproxtime.findViewById(R.id.tvupdate);
                                tvapproxDate = dialogapproxtime.findViewById(R.id.tvapproxDate);
                                imgclose = dialogapproxtime.findViewById(R.id.imgclose);
                                dialogapproxtime.show();


                                imgclose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogapproxtime.dismiss();

                                    }
                                });

                                tvapproxDate.setText(sdf1.format(date).toString().toUpperCase());
                                tvskip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //    booking("2", position);
                                        bookingstart("2", position, "");
                                        dialogapproxtime.dismiss();
                                    }
                                });
                                tvupdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bookingstart("2", position, String.valueOf(sdf1.format(date)));
                                        dialogapproxtime.dismiss();
                                    }
                                });
                                llDate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        final Calendar c = Calendar.getInstance();

                                        final DatePickerDialog dpd = new DatePickerDialog(context,
                                                new DatePickerDialog.OnDateSetListener() {

                                                    @Override
                                                    public void onDateSet(DatePicker view, final int year,
                                                                          final int monthOfYear, final int dayOfMonth) {


                                                        final Calendar c = Calendar.getInstance();
                                                        mHour = c.get(Calendar.HOUR_OF_DAY);
                                                        mMinute = c.get(Calendar.MINUTE);


                                                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                                                new TimePickerDialog.OnTimeSetListener() {

                                                                    @Override
                                                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                          int minute) {


                                                                        c.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                                                                        String dateString = sdf1.format(c.getTime());
                                                                        tvapproxDate.setText(dateString);
                                                                        date = c.getTime();

                                                                        //    Toast.makeText(context,String.valueOf(sdf1.format(date)),Toast.LENGTH_LONG).show();
                                                                        // Toast.makeText(context,String.valueOf(timeZone.format(date)),Toast.LENGTH_LONG).show();

                                                                    }
                                                                }, mHour, mMinute, false);
                                                        timePickerDialog.show();
                                                    }
                                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                                        dpd.show();


                                    }
                                });
                            }


                            //
                        } else {
                       //     ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                        }
                    }
                }
            });

            holder.binding.llarrival.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arival(position);
                    holder.binding.txtarivaltimer.setVisibility(View.VISIBLE);
                    holder.binding.startnew.setVisibility(View.VISIBLE);


                    CountDownTimer cT = new CountDownTimer(300000, 1000) {

                        public void onTick(long millisUntilFinished) {


                            String v = String.format("%02d", millisUntilFinished / 60000);
                            int va = (int) ((millisUntilFinished % 60000) / 1000);
                            holder.binding.txtarivaltimer.setText("" + v + ":" + String.format("%02d", va));
                        }

                        public void onFinish() {
                            //   holder.binding.txtarivaltimer.setVisibility(View.GONE);
                            SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");
                            Date dt = null;

                            if (artistBookingsList.get(position).getWorking_min().equalsIgnoreCase("0")) {
                                try {
                                    dt = sdf.parse("0.1");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                try {
                                    dt = sdf.parse(artistBookingsList.get(position).getWorking_min());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            sdf = new SimpleDateFormat("HH:mm:ss");
                            int min = dt.getHours() * 60 + dt.getMinutes();
                            int sec = dt.getSeconds();
                            holder.binding.chronometer.setBase(SystemClock.elapsedRealtime() - (min * 60000 + sec * 1000));
                            holder.binding.chronometer.start();

                            holder.binding.txtarivaltimer.setText(holder.binding.chronometer.getText().toString());
                        }
                    };
                    cT.start();

                    holder.binding.llarrival.setEnabled(false);

                }
            });

            if (artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("0") || artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("3")) {

                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("0")) {

                    // holder.binding.layinquiry.setVisibility(View.VISIBLE);
                    holder.binding.layaccpet.setVisibility(View.GONE);
                    holder.binding.tvnavigation.setVisibility(View.VISIBLE);
                    holder.binding.layfinish.setVisibility(View.VISIBLE);
                    holder.binding.tvnavigationfinish.setVisibility(View.VISIBLE);
                    holder.binding.txtptype.setVisibility(View.GONE);
                    holder.binding.cardpaymnet.setBackground(context.getResources().getDrawable(R.drawable.button_line));
//                    holder.binding.cardfinish.setBackground(context.getResources().getDrawable(R.drawable.button_line));
                    holder.binding.cardaccept.setBackground(context.getResources().getDrawable(R.drawable.button_line));
                    holder.binding.cardinquiry.setBackground(context.getResources().getDrawable(R.drawable.button_line_blue));

                    holder.binding.imgone.setImageResource(R.drawable.bone);

                    holder.binding.imgtwo.setImageResource(R.drawable.blacktwo);

                    holder.binding.imgthree.setImageResource(R.drawable.blackthree);

                    holder.binding.imgfour.setImageResource(R.drawable.blackfour);

                }
                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("1")) {
                    holder.binding.layaccpet.setVisibility(View.VISIBLE);
                    holder.binding.tvnavigation.setVisibility(View.VISIBLE);
                    holder.binding.layfinish.setVisibility(View.VISIBLE);
                    holder.binding.tvnavigationfinish.setVisibility(View.VISIBLE);
                    holder.binding.txtptype.setVisibility(View.GONE);


                    holder.binding.cardpaymnet.setBackground(context.getResources().getDrawable(R.drawable.button_line));
//                    holder.binding.cardfinish.setBackground(context.getResources().getDrawable(R.drawable.button_line));
                    holder.binding.cardaccept.setBackground(context.getResources().getDrawable(R.drawable.button_line_blue));
                    holder.binding.cardinquiry.setBackground(context.getResources().getDrawable(R.drawable.buttonroundlightgreen));

                    holder.binding.imgone.setImageResource(R.drawable.gone);


                    holder.binding.imgtwo.setImageResource(R.drawable.btwo);


                    holder.binding.imgthree.setImageResource(R.drawable.blackthree);


                    holder.binding.imgfour.setImageResource(R.drawable.blackfour);


                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("3")) {
                    holder.binding.layaccpet.setVisibility(View.GONE);
                    holder.binding.tvnavigation.setVisibility(View.VISIBLE);
                    holder.binding.layinquiry.setVisibility(View.GONE);
                    holder.binding.layfinish.setVisibility(View.VISIBLE);
                    holder.binding.tvnavigationfinish.setVisibility(View.VISIBLE);
                    holder.binding.txtptype.setVisibility(View.VISIBLE);


                    holder.binding.cardpaymnet.setBackground(context.getResources().getDrawable(R.drawable.button_line));
//                    holder.binding.cardfinish.setBackground(context.getResources().getDrawable(R.drawable.button_line_blue));
                    holder.binding.cardinquiry.setBackground(context.getResources().getDrawable(R.drawable.buttonroundlightgreen));
                    holder.binding.cardaccept.setBackground(context.getResources().getDrawable(R.drawable.buttonroundlightgreen));

                    holder.binding.imgone.setImageResource(R.drawable.gone);


                    holder.binding.imgtwo.setImageResource(R.drawable.gtwo);


                    holder.binding.imgthree.setImageResource(R.drawable.bthree);


                    holder.binding.imgfour.setImageResource(R.drawable.blackfour);

                    SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

                    try {
                        Date dt;

                        if (artistBookingsList.get(position).getWorking_min().equalsIgnoreCase("0")) {
                            dt = sdf.parse("0.1");

                        } else {
                            dt = sdf.parse(artistBookingsList.get(position).getWorking_min());

                        }
                        sdf = new SimpleDateFormat("HH:mm:ss");
                        System.out.println(sdf.format(dt));
                        int min = dt.getHours() * 60 + dt.getMinutes();
                        int sec = dt.getSeconds();
                        holder.binding.chronometer.setBase(SystemClock.elapsedRealtime() - (min * 60000 + sec * 1000));
                        holder.binding.chronometer.start();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }

            holder.binding.tvnavigationfinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getDestinationaddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);
                }
            });


            holder.binding.tvnavigation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);

                }
            });
            holder.binding.llAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkManager.isConnectToInternet(context)) {
                        booking("1", position);
                    } else {
                   //     ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                    }
                }
            });
            holder.binding.llDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                //    etd_reason = dialogdecline.findViewById(R.id.etd_reason);
                    tvdCancelreason = dialogdecline.findViewById(R.id.tvdCancelreason);
                    tvdAddreason = dialogdecline.findViewById(R.id.tvdAddreason);
                    checkone = dialogdecline.findViewById(R.id.checkone);
                    checktwo = dialogdecline.findViewById(R.id.checktwo);
                    checkthree = dialogdecline.findViewById(R.id.checkthree);
                    checkfour = dialogdecline.findViewById(R.id.checkfour);
                    dialogdecline.show();

                    tvdCancelreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogdecline.dismiss();
                        }
                    });
                    tvdAddreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //  reason= etd_reason.getText().toString();


                            if (reason.equalsIgnoreCase("")) {
                                ProjectUtils.showToast(context, "Please Enter Reason");
                            } else {

                                decline(position, reason);
                                dialogdecline.dismiss();
                            }


                        }
                    });


                }
            });


            holder.binding.llCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   holder.binding.llarrival.setVisibility(View.GONE);
                 //   etd_reason = dialogdecline.findViewById(R.id.etd_reason);
                    tvdCancelreason = dialogdecline.findViewById(R.id.tvdCancelreason);
                    tvdAddreason = dialogdecline.findViewById(R.id.tvdAddreason);
                    dialogdecline.show();


                    checkone = dialogdecline.findViewById(R.id.checkone);
                    checktwo = dialogdecline.findViewById(R.id.checktwo);
                    checkthree = dialogdecline.findViewById(R.id.checkthree);
                    checkfour = dialogdecline.findViewById(R.id.checkfour);
                 //   checkfive = dialogdecline.findViewById(R.id.checkfive);
                 //   checksix = dialogdecline.findViewById(R.id.checksix);

                    checkone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkone.setChecked(true);
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checkfour.setChecked(false);
                            checksix.setChecked(false);
                            cancelreason = "Car Problem";
                        }
                    });

                    checktwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkone.setChecked(false);
                            checktwo.setChecked(true);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            cancelreason = "Fuel Problem";
                        }
                    });


                    checkthree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkone.setChecked(false);
                            checktwo.setChecked(false);
                            checkthree.setChecked(true);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            cancelreason = "Customer Problem";
                        }
                    });

                    checkfour.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkone.setChecked(false);
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(true);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            cancelreason = "Language Problem";
                        }
                    });


                    checkfive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkone.setChecked(false);
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(true);
                            checksix.setChecked(false);
                            cancelreason = "Many Customer";
                        }
                    });


                    checksix.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkone.setChecked(false);
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(true);
                            cancelreason = "My Reason Not Listed";
                        }
                    });
                    tvdCancelreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            checkone.setChecked(false);
                            checktwo.setChecked(false);
                            checkthree.setChecked(false);
                            checkfour.setChecked(false);
                            checkfive.setChecked(false);
                            checksix.setChecked(false);
                            cancelreason = "";
                            dialogdecline.dismiss();
                        }
                    });
                    tvdAddreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (cancelreason.equalsIgnoreCase("")) {
                                ProjectUtils.showToast(context, "Please Enter Reason");
                            } else {

                                decline(position, cancelreason);
                                dialogdecline.dismiss();
                            }


                        }
                    });

                }
            });

            holder.binding.finishnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                    if (checkarss(artistBookingsList.get(position).getProduct().get(0).getCategory_id())) {
                        tvtotalfinish = dialogfinish.findViewById(R.id.tvtotalfinish);
                        tvfinishsubmit = dialogfinish.findViewById(R.id.tvfinishsubmit);
                        tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                        layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);


                        if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("0")) {
                            layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                            tvfinishsubmit.setText("Okay");

                        } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("1")) {
                            layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.green));
                            tvfinishsubmit.setText("Collect");


                        } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("2")) {
                            layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                            tvfinishsubmit.setText("Okay");


                        }
                        dialogfinish.show();


                        tvtotalfinish.setText(artistBookingsList.get(position).getPrice());


                        tvfinishcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogfinish.dismiss();
                            }
                        });


                        tvfinishsubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String cid = artistBookingsList.get(position).getProduct().get(0).getCategory_id();
                                paramsBookingOp.put(Consts.CATEGORY_ID, cid);
                                if (NetworkManager.isConnectToInternet(context)) {
                                    dailograting.show();
                                    rbReview.setRating(5);
                                    rbReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                            myrating = ratingBar.getRating();
                                        }
                                    });


                                    tvfinishsubmitrating.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dailograting.dismiss();
                                            holder.binding.finishnew.setVisibility(View.VISIBLE);
                                            booking("3", position);
                                        }
                                    });


                                } else {
                                   // ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                }
                                dialogfinish.dismiss();
                            }
                        });
                    } else {
                        tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
                        tvcamera = dialogcamera.findViewById(R.id.tvcamera);
                        tvupload = dialogcamera.findViewById(R.id.tvupload);
                        img_upload = dialogcamera.findViewById(R.id.img_upload);
                        dialogcamera.show();

                        img_upload.setVisibility(View.GONE);

                        tvcameraskip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String cid = artistBookingsList.get(position).getProduct().get(0).getCategory_id();
                                paramsBookingOp.put(Consts.CATEGORY_ID, cid);

                                if (NetworkManager.isConnectToInternet(context)) {
                                  booking("3", position);


                                } else {
                                 //   ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                }
                                dialogcamera.dismiss();
                            }
                        });

                        tvupload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                newBookings.opencamrea();


                            }
                        });
                        tvcamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                String cid = artistBookingsList.get(position).getProduct().get(0).getCategory_id();
                                paramsBookingOp.put(Consts.CATEGORY_ID, cid);


                                if (img_upload.getVisibility() == View.VISIBLE) {

                                    if (NetworkManager.isConnectToInternet(context)) {
                                        bookingupload("3", position);


                                    } else {
                                   //     ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                    }
                                } else {
                                    Toast.makeText(context, "Please Upload Photo", Toast.LENGTH_LONG).show();


                                }


                            }
                        });
                    }
                }
            });


        } else {
            AdapterCabBookings.MyViewHolderSection view = (AdapterCabBookings.MyViewHolderSection) holderMain;
            view.tvSection.setText(artistBookingsList.get(position).getSection_name());
        }
    }

    OnTouch onTouch = new OnTouch() {
        @Override
        public void removeBorder() {

        }
    };

    public void booking(String req, int pos) {

        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, artistBookingsList.get(pos).getUser_id());
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                 //   ProjectUtils.showToast(context, msg);

                    newBookings.getBookingscopy("3");


                } else {
                 //   ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public void bookingupload(String req, int pos) {
        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, artistBookingsList.get(pos).getUser_id());
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, newBookings.paramsFile, context).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    dialogcamera.dismiss();
                 //   ProjectUtils.showToast(context, msg);
                    newBookings.getBookingscopy("3");


                } else {
               //     ProjectUtils.showToast(context, msg);
                }


            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return artistBookingsList.get(position).isSection() ? VIEW_SECTION : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        return artistBookingsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCabBookingsBinding binding;

        public MyViewHolder(AdapterCabBookingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public void bookingstart(String req, int pos, String datetime) {

        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.APPROXTIME, datetime);
        paramsBookingOp.put(Consts.USER_ID, artistBookingsList.get(pos).getUser_id());
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                //    ProjectUtils.showToast(context, msg);
                    progressDialog.dismiss();
                    newBookings.getBookingscopy("3");


                } else {
                 //   ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public void decline(int pos, String reason) {
        progressDialog.show();
        paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsDecline.put(Consts.DECLINE_BY, "1");
        paramsDecline.put(Consts.DECLINE_REASON, reason);
        paramsDecline.put("cat_id", artistBookingsList.get(pos).getProduct().get(0).getCategory_id());
        paramsDecline.put("sub_id", artistBookingsList.get(pos).getProduct().get(0).getSub_category_id());
        paramsDecline.put("third_id", artistBookingsList.get(pos).getProduct().get(0).getSublevel_category());
        paramsDecline.put("lat", artistBookingsList.get(pos).getC_latitude());
        paramsDecline.put("lang", artistBookingsList.get(pos).getC_longitude());
        paramsDecline.put("passvalue", "1");


        new HttpsRequest(Consts.DECLINE_BOOKING_ON_BTN_CLICK, paramsDecline, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                 //   ProjectUtils.showToast(context, msg);

                    try {
                        MediaPlayer mediaPlayer = new MediaPlayer();

                        AssetFileDescriptor descriptor = context.getAssets().openFd("cancel.mpeg");
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        mediaPlayer.prepare();
                        mediaPlayer.setVolume(1f, 1f);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    newBookings.getBookingscopy("3");

                } else {
                 //   ProjectUtils.showToast(context, msg);
                }


            }
        });
    }

    public static class MyViewHolderSection extends RecyclerView.ViewHolder {
        public CustomTextView tvSection;

        public MyViewHolderSection(View view) {
            super(view);
            tvSection = view.findViewById(R.id.tvSection);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        artistBookingsList.clear();
        if (charText.length() == 0) {
            artistBookingsList.addAll(searchBookingsList);
        } else {
            for (ArtistBooking historyDTO : searchBookingsList) {
                if (historyDTO.getUserName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    artistBookingsList.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }


    public void arival(int pos) {


        Retrofit retrofit = apiClient.getClient();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.arival(userDTO.getUser_id(), artistBookingsList.get(pos).getId());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });


    }
}
