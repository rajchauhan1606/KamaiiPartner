package com.kamaii.partner.ui.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.HomeScreenFragment;
import com.kamaii.partner.LocationService;
import com.kamaii.partner.databinding.AdapterCabBookingsBinding;
import com.kamaii.partner.databinding.DialogRequestForRiderConfirmationBinding;
import com.kamaii.partner.databinding.RequestForRiderDialogLayoutBinding;
import com.kamaii.partner.interfacess.AddPrepTime;
import com.kamaii.partner.interfacess.GetCheckedItem;
import com.kamaii.partner.interfacess.OnReasonSelectedListener;
import com.kamaii.partner.interfacess.SelectOrderForRequest;
import com.kamaii.partner.ui.activity.ViewInvoice;
import com.kamaii.partner.ui.models.CancelReasonModel;
import com.kamaii.partner.ui.models.RequestRiderDialogModel;
import com.kamaii.partner.utils.ExpandableHeightGridView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ncorti.slidetoact.SlideToActView;
import com.kamaii.partner.DTO.ArtistBooking;
import com.kamaii.partner.DTO.ProductDTO;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.network.NetworkManager;
import com.kamaii.partner.service.apiClient;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.fragment.CabBookingsFragment;
import com.kamaii.partner.utils.CustomEditText;
import com.kamaii.partner.utils.CustomTextView;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.kamaii.partner.utils.OnTouch;
import com.kamaii.partner.utils.ProjectUtils;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AdapterCabBookings extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnReasonSelectedListener, SelectOrderForRequest, GetCheckedItem {

    private String TAG = AdapterCabBookings.class.getSimpleName();
    private CabBookingsFragment newBookings;
    private ArrayList<ArtistBooking> artistBookingsList;
    private ArrayList<ArtistBooking> searchBookingsList;
    private UserDTO userDTO;
    private LayoutInflater myInflater;
    private Context context;
    HashMap<String, String> findriderHashmap = new HashMap<>();
    private HashMap<String, String> paramsBookingOp;
    private HashMap<String, String> paramsDecline;
    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager gridLayoutManager1;
    private ArrayList<ProductDTO> productDTOArrayList;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> updatetimeparms = new HashMap<>();
    private HashMap<String, String> cancelParms = new HashMap<>();
    private HashMap<String, String> preparationParams = new HashMap<>();
    private HashMap<String, String> startdeliveryParams = new HashMap<>();
    public static RelativeLayout lldauploadImageLayout;
    private Dialog dialog, dialogcustomservice, dialogdecline, dialogapproxtime, dialogcamera, dialogpriview, dialogfinish, dailograting, dialogThankYou, riderRequestConfirmation;
    private CustomTextViewBold dialog_paytype, customer_name, tvcameraskip, tvdAddreason, tvcamera, tvupload, tvtotalfinish, tvfinishsubmit;
    CustomTextView thankyoudialog_paytype;
    CustomTextViewBold ok_btn;
    LinearLayout tvfinishsubmitrating;
    CircleImageView add_profile_pic;
    String locationstatus = "";
    ImageView pay_done_img, thank_u_dialog_close, tvdCancelreason;
    //ImageView tvcameraskip;
    CustomTextViewBold tvskip, tvupdate;
    CustomEditText etd_reason;
    ExpandableHeightGridView cancell_reason_list;
    CheckBox checkone, checktwo, checkthree, checkfour, checkfive, checksix;
    LinearLayout llDate;
    String reason;
    private SimpleDateFormat sdf1;
    CustomTextView tvapproxDate;
    private Date date;
    ImageView imgclose, tvfinishcancel;
    public static ImageView img_upload;
    private int mHour, mMinute;
    AdapterCartCab adapterCart;
    Boolean isCheck = true;
    public static String cancelreason = "";
    ProgressDialog progressDialog;
    LinearLayout layfinishbackground;
    private RatingBar rbReview;
    private float myrating;
    public static String pay_type = null;
    List<CancelReasonModel> cancelList;
    String rider_status = "";
    boolean thank_you_dialog = false;
    boolean waiting_timer = false;
    int pos123 = -1;
    BaseActivity baseActivity;
    LocationService locationService;
    Dialog addPrepTimeDialog;
    public Intent mServiceIntent;
    ImageView close_ship_dialog;
    RecyclerView add_timer_recyclerview;
    CustomTextViewBold add_time_tvYesPro;
    CustomEditText time_reason_txt;
    List<String> addPrepTimeList = new ArrayList<>();
    private String selected_prep_time = "";
    boolean cancel_booking_flag = false;
    BottomSheetDialog bottomSheetDialog;
    RequestForRiderDialogLayoutBinding requestForRiderBinding;
    DialogRequestForRiderConfirmationBinding riderConfirmationBinding;
    RequestRiderDialogModel dialogModel;
    List<RequestRiderDialogModel> dialogList;
    List<ProductDTO> dialogList2;
    List<String> selectedidList;
    List<String> ConfirmationselectedidList;
    HashMap<String, String> selectedidHashmap;
    boolean multi_start_preparation = false;
    boolean confirmation_for_start = false;
    StringBuilder start_multi_buffer;
    String notification_name = "";
    PartnerConfirmationAdapter adapter;

    public AdapterCabBookings(CabBookingsFragment newBookings, ArrayList<ArtistBooking> artistBookingsList, UserDTO userDTO, LayoutInflater myInflater, BaseActivity baseActivity, String notification_name) {
        this.newBookings = newBookings;
        this.artistBookingsList = artistBookingsList;
        this.searchBookingsList = new ArrayList<ArtistBooking>();
        this.searchBookingsList.addAll(artistBookingsList);
        this.userDTO = userDTO;
        this.baseActivity = baseActivity;
        this.myInflater = myInflater;
        cancelList = new ArrayList<>();
        selectedidList = new ArrayList<>();
        ConfirmationselectedidList = new ArrayList<>();
        context = newBookings.getActivity();
        this.notification_name = notification_name;
        Log.e("confirmation_for_start", "bottom 1");

        if (context != null){


        }
        Log.e("confirmation_for_start", "bottom 2");

        Log.e("confirmation_for_start", "bottom 3");

        Log.e("confirmation_for_start", "bottom 4");

        Log.e("confirmation_for_start", "bottom 5");

        Log.e("confirmation_for_start", "confirmation_for_start 300: 100 : "+artistBookingsList.get(0).getOrder_no2());
       /* locationService = new LocationService(context);
        mServiceIntent = new Intent(context, locationService.getClass());
*/
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        RecyclerView.ViewHolder vh;
        if (myInflater == null) {
            myInflater = LayoutInflater.from(viewGroup.getContext());
        }
        if (viewType == VIEW_ITEM) {
            AdapterCabBookingsBinding binding =
                    DataBindingUtil.inflate(myInflater, R.layout.adapter_cab_bookings, viewGroup, false);
            vh = new MyViewHolder(binding);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_section, viewGroup, false);
            vh = new MyViewHolderSection(v);
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderMain, @SuppressLint("RecyclerView") int pos) {

        requestForRiderBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.request_for_rider_dialog_layout, null, false);

        bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(requestForRiderBinding.getRoot());
        bottomSheetDialog.setCancelable(false);
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
        findriderHashmap.put(Consts.ARTIST_ID, userDTO.getUser_id());
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


        addPrepTimeDialog = new Dialog(context, android.R.style.Theme_Dialog);
        addPrepTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addPrepTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addPrepTimeDialog.setContentView(R.layout.add_timer_layout);


        dialogdecline = new Dialog(context, R.style.Theme_Dialog);
        dialogdecline.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdecline.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogdecline.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogdecline.setContentView(R.layout.dailog_decline);
        dialogdecline.setCancelable(false);

        cancell_reason_list = dialogdecline.findViewById(R.id.cancell_reason_list);

        cancelParms.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());

        getCancellReasons();

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

        dialogThankYou = new Dialog(context, R.style.Theme_Dialog);
        dialogThankYou.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogThankYou.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogThankYou.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogThankYou.setContentView(R.layout.dialog_thankyou);
        dialogThankYou.setCancelable(true);


        dailograting = new Dialog(context, R.style.Theme_Dialog);
        dailograting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailograting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dailograting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dailograting.setContentView(R.layout.dailog_rating);
        dailograting.setCancelable(false);

        rbReview = dailograting.findViewById(R.id.rbReview123);
        customer_name = dailograting.findViewById(R.id.customer_name);

        tvfinishsubmitrating = dailograting.findViewById(R.id.btnSubmit);
        add_profile_pic = dailograting.findViewById(R.id.add_profile_pic);


        Glide.with(context).load(artistBookingsList.get(pos).getUserImage()).placeholder(R.drawable.ic_user_placeholder).into(add_profile_pic);
        dialogpriview = new Dialog(context, R.style.Theme_Dialog);
        dialogpriview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogpriview.setContentView(R.layout.dailog_priview);
        dialogpriview.setCancelable(true);

        final int position = pos;


        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());


        if (holderMain instanceof MyViewHolder) {

            final MyViewHolder holder = (MyViewHolder) holderMain;

            gridLayoutManager = new GridLayoutManager(context, 1);
            productDTOArrayList = new ArrayList<>();


            productDTOArrayList = artistBookingsList.get(position).getProduct();
            locationstatus = artistBookingsList.get(position).getLocation_status();



            if (notification_name.equalsIgnoreCase("100122")) {


                riderRequestConfirmation = new Dialog(context, R.style.Theme_Dialog);
                riderRequestConfirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
                riderRequestConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                riderRequestConfirmation.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                riderRequestConfirmation.setCancelable(false);
                riderConfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_request_for_rider_confirmation, null, false);
                riderRequestConfirmation.setContentView(riderConfirmationBinding.getRoot());

                confirmation_for_start = true;
                ConfirmationselectedidList = new ArrayList<>();
                Log.e("confirmation_for_start", "confirmation_for_start 3");
                Log.e("confirmation_for_start", "confirmation_for_start 3 : 2 booking_fag:- "+artistBookingsList.get(position).getBooking_flag() +" customer_booking_flag:-- "+ artistBookingsList.get(position).getCustomer_booking_flag() +" rider_booking_flag:-- "+ artistBookingsList.get(position).getRider_booking_flag());
                Log.e("confirmation_for_start", "confirmation_for_start 3: 1 : "+artistBookingsList.size());

                adapter = new PartnerConfirmationAdapter(context, artistBookingsList, confirmation_for_start, this);
                riderConfirmationBinding.riderRequestConfirmation.setLayoutManager(new LinearLayoutManager(context));
                riderConfirmationBinding.riderRequestConfirmation.setAdapter(adapter);

                riderConfirmationBinding.ctvbTitle.setText("Start Preparation?");
                riderConfirmationBinding.cancelBookingTxt.setText("Are you sure about starting preparation?");

                riderConfirmationBinding.riderconfirmcancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        riderRequestConfirmation.dismiss();
                        ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                    }
                });

                riderConfirmationBinding.riderconfirmbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("confirmation_for_start", "confirmation_for_start 4");

                        start_multi_buffer = new StringBuilder();
                        for (String s : ConfirmationselectedidList) {
                            // sb.append(s);

                            if (ConfirmationselectedidList.size() >= 1) {

                                start_multi_buffer.append("," + s);
                                Log.e("confirmation_for_start", "confirmation_for_start 5 "+start_multi_buffer.toString());

                            }/*else {
                            sb.append(s);

                        }*/

                            if (checkarss(artistBookingsList.get(position).getProduct().get(0).getCategory_id())) {
                                holder.binding.startnew.setVisibility(View.GONE);
                                bookingstart("2", position, "");
                                holder.binding.dropBtn.performClick();
                            } else {
                                Log.e("confirmation_for_start", "else ma gayu" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                                //     holder.binding.prtTxt.setText("Preparation Time:- ");
                                holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                                //    holder.binding.counter.setVisibility(View.VISIBLE);

                                if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("1")) {
                                    Log.e("rider_order123456", "4" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                    bookingstart("2", position, String.valueOf(sdf1.format(date)));
                                    // holder.binding.requestridernew.setVisibility(View.VISIBLE);
                                } else {
                                    Log.e("rider_order123456", "5" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                    preparationParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());
                                    bookingstart("2", position, String.valueOf(sdf1.format(date)));
                                }
                            }

                        }
                    }
                });
                riderRequestConfirmation.show();

            }else if (notification_name.equalsIgnoreCase("100123")) {

                riderRequestConfirmation = new Dialog(context, R.style.Theme_Dialog);
                riderRequestConfirmation.requestWindowFeature(Window.FEATURE_NO_TITLE);
                riderRequestConfirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                riderRequestConfirmation.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                riderRequestConfirmation.setCancelable(false);
                riderConfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_request_for_rider_confirmation, null, false);
                riderRequestConfirmation.setContentView(riderConfirmationBinding.getRoot());

                riderConfirmationBinding.ctvbTitle.setText("Request for rider?");
                riderConfirmationBinding.cancelBookingTxt.setText("Are you sure about requesting rider?");

                confirmation_for_start = false;
                adapter = new PartnerConfirmationAdapter(context, artistBookingsList, confirmation_for_start, this);
                riderConfirmationBinding.riderRequestConfirmation.setLayoutManager(new LinearLayoutManager(context));
                riderConfirmationBinding.riderRequestConfirmation.setAdapter(adapter);


                riderConfirmationBinding.riderconfirmcancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        riderRequestConfirmation.dismiss();
                        ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                    }
                });

                riderConfirmationBinding.riderconfirmbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CountDownTimer cT = new CountDownTimer(20000, 1000) {

                            public void onTick(long millisUntilFinished) {


                                String v = String.format("%02d", millisUntilFinished / 60000);
                                int va = (int) ((millisUntilFinished % 60000) / 1000);
                                holder.binding.riderWaitingTimeDigits.setText("" + v + ":" + String.format("%02d", va));
                            }

                            public void onFinish() {

                            }
                        };
                        cT.start();

                        StringBuilder sb = new StringBuilder();
                        for (String s : ConfirmationselectedidList) {
                            // sb.append(s);
                            if (ConfirmationselectedidList.size() >= 1) {

                                sb.append("," + s);
                            } else {
                                sb.append(s);

                            }
                        }

                        selectedidHashmap = new HashMap<>();
                        selectedidHashmap.put(Consts.ARTIST_ID, userDTO.getUser_id());
                        selectedidHashmap.put(Consts.BOOKING_ID, sb.toString());

                        new HttpsRequest(Consts.REQUEST_FOR_RIDER_MULTIPLE_API, selectedidHashmap, context).stringPosttwo("REQUESTFORRIDER", new Helper() {
                            @Override
                            public void backResponse(boolean flag, String msg, JSONObject response) {

                                Log.e("REQUESt_confirmation_API", "" + response.toString());
                                if (!flag) {
                                    riderRequestConfirmation.dismiss();
                                    holder.binding.riderWaitingLayout.setVisibility(View.GONE);
                                    holder.binding.noRiderTxt.setVisibility(View.VISIBLE);
                                    holder.binding.noRiderRelative.setVisibility(View.VISIBLE);
                                    ConfirmationselectedidList.clear();
                                    ConfirmationselectedidList = new ArrayList<>();
                                } else {
                                    riderRequestConfirmation.dismiss();

                                    holder.binding.requestridernew.setVisibility(View.GONE);
                                    holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        //newBookings.setAdapterAllBookings();
                        // context.startActivity(new Intent(context, BaseActivity.class));
                        holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                        ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                    }
                });
                riderRequestConfirmation.show();
            }


            if (!artistBookingsList.get(position).getCooking_instruction().equalsIgnoreCase("")
                    || artistBookingsList.get(position).getCooking_instruction().equalsIgnoreCase(" ")
                    || artistBookingsList.get(position).getCooking_instruction().equalsIgnoreCase(null)) {

                holder.binding.cookingInst.setText(artistBookingsList.get(position).getCooking_instruction());
            } else {
                holder.binding.cookingInstructionCard.setVisibility(View.GONE);
            }
            holder.binding.txtcat.setText(artistBookingsList.get(position).getCategory_name());
            holder.binding.preparationTime.setText(artistBookingsList.get(position).getPreparation_time());
            // Log.e("productDtoArraylist", "" + artistBookingsList.toString());
            holder.binding.itemsCount.setText(String.valueOf(productDTOArrayList.size()));
            adapterCart = new AdapterCartCab(newBookings, productDTOArrayList, locationstatus);
            holder.binding.rvCart.setLayoutManager(gridLayoutManager);
            holder.binding.rvCart.setAdapter(adapterCart);
            holder.binding.tvName.setText(artistBookingsList.get(position).getUserName());

            if (!artistBookingsList.get(position).getLunch_dinner().equalsIgnoreCase("2")) {
                holder.binding.lunchDinner.setVisibility(View.VISIBLE);
                holder.binding.lunchDinner.setText(artistBookingsList.get(position).getLunch_dinner());
            } else {
                holder.binding.lunchDinner.setVisibility(View.GONE);
            }
            pay_type = artistBookingsList.get(pos).getPay_type();

            Log.e("Booking_date", "" + artistBookingsList.get(position).getMybooking_date());
            holder.binding.tvDate.setText("Order Date: " + artistBookingsList.get(position).getOrdertime());
            holder.binding.deliveryDate.setText("Delivery Time: " + artistBookingsList.get(position).getDeliverytime());
            holder.binding.newPreparationTimeTxt.setText(artistBookingsList.get(position).getNew_preparation_time());

            if (artistBookingsList.get(position).getDelay_msg().equalsIgnoreCase("") || artistBookingsList.get(position).getDelay_msg().isEmpty()) {
                holder.binding.orderStatusTv.setVisibility(View.GONE);
            } else {
                holder.binding.orderStatusTv.setVisibility(View.VISIBLE);
                holder.binding.orderStatusTv.setText(Html.fromHtml(artistBookingsList.get(position).getDelay_msg()));
            }
            Glide.with(context).
                    load(Consts.IMAGE_URL + artistBookingsList.get(position).getImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .into(holder.binding.ivArtist);

            holder.binding.discountedTotalDigits.setText(Html.fromHtml(artistBookingsList.get(position).getItemtotal()));
            if (artistBookingsList.get(position).getServce_charge_tracker().equalsIgnoreCase("1")) {

                if (artistBookingsList.get(pos).getRider_order().equalsIgnoreCase("1")) {

                    holder.binding.llLocation.setVisibility(View.GONE);
                    holder.binding.tmpView11.setVisibility(View.GONE);
                    holder.binding.llLocationdesti.setVisibility(View.GONE);
                    holder.binding.serviceChargeRelative.setVisibility(View.GONE);
                } else {
                    holder.binding.tmpView11.setVisibility(View.VISIBLE);
                    holder.binding.llLocationdesti.setVisibility(View.VISIBLE);
                    holder.binding.serviceChargeRelative.setVisibility(View.VISIBLE);
                }

                //holder.binding.serviceChargeRelative.setVisibility(View.VISIBLE);
                holder.binding.serviceTxt.setText(artistBookingsList.get(position).getServce_charge_txt());
                if (artistBookingsList.get(position).getServicecharge().equalsIgnoreCase("0.00")) {
                    holder.binding.serviceDigitTxt.setText("Free");
                } else {
                    holder.binding.serviceDigitTxt.setText(Html.fromHtml(artistBookingsList.get(position).getServicecharge()));
                }
            }
            if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("0")) {
                holder.binding.txtptype.setVisibility(View.GONE);
                holder.binding.txtptype.setText("Online Payment");

            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("1")) {
                holder.binding.txtptype.setVisibility(View.GONE);
                holder.binding.txtptype.setText("Cash");

            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("2")) {
                holder.binding.txtptype.setVisibility(View.GONE);
                holder.binding.txtptype.setText("Wallet Payment");

            } else {
                holder.binding.txtptype.setVisibility(View.GONE);
            }

            //  holder.binding.tvdestiLocation.setText(artistBookingsList.get(position).getDestinationaddress());


            holder.binding.imgphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!artistBookingsList.get(position).getRidercontact().equalsIgnoreCase("")) {

                        String mobileno = artistBookingsList.get(position).getRidercontact();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileno));
                        context.startActivity(intent);
                    }

                }
            });

            holder.binding.kamaiihelplinephone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!artistBookingsList.get(position).getHelpline_number().equalsIgnoreCase("")) {

                        String mobileno = artistBookingsList.get(position).getHelpline_number();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + mobileno));
                        context.startActivity(intent);

                    }

                }
            });

            holder.binding.imgphone1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("MOBILE", "" + artistBookingsList.get(position).getPartercontact());
                    if (!artistBookingsList.get(position).getPartercontact().equalsIgnoreCase("")) {

                        String mobileno = artistBookingsList.get(position).getPartercontact();

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

            holder.binding.tvtotal.setText(artistBookingsList.get(position).getPrice());


            Glide.with(context).
                    load(artistBookingsList.get(position).getUserImage())
                    .placeholder(R.drawable.dummyuser_image)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            if (productDTOArrayList.size() != 0) {


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
            }
            holder.binding.layoutMoreLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isCheck) {
                        // holder.binding.tmpView3.setVisibility(View.VISIBLE);
                        holder.binding.tmpView2.setVisibility(View.VISIBLE);
                        holder.binding.laymoreee.setVisibility(View.VISIBLE);
                        isCheck = false;
                    } else {
                        // holder.binding.tmpView3.setVisibility(View.GONE);
                        holder.binding.tmpView2.setVisibility(View.GONE);
                        holder.binding.laymoreee.setVisibility(View.GONE);
                        isCheck = true;
                    }
                }
            });

            requestForRiderBinding.popupstartnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                    start_multi_buffer = new StringBuilder();
                    for (String s : selectedidList) {
                        // sb.append(s);
                        if (selectedidList.size() >= 1) {

                            start_multi_buffer.append("," + s);
                        }/*else {
                            sb.append(s);

                        }*/

                        if (checkarss(artistBookingsList.get(position).getProduct().get(0).getCategory_id())) {
                            holder.binding.startnew.setVisibility(View.GONE);
                            bookingstart("2", position, "");
                            holder.binding.dropBtn.performClick();
                        } else {
                            Log.e("rider_order123456", "3" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                            holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                            //     holder.binding.prtTxt.setText("Preparation Time:- ");
                            holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                            //    holder.binding.counter.setVisibility(View.VISIBLE);

                            if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("1")) {
                                Log.e("rider_order123456", "4" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                bookingstart("2", position, String.valueOf(sdf1.format(date)));
                                // holder.binding.requestridernew.setVisibility(View.VISIBLE);
                            } else {
                                Log.e("rider_order123456", "5" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                holder.binding.startnew.setVisibility(View.GONE);
                                holder.binding.startdelivery.setVisibility(View.VISIBLE);
                                preparationParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());
                                bookingstart("2", position, String.valueOf(sdf1.format(date)));

                            }
                        }

                    }
                }
            });

            requestForRiderBinding.requestRiderDialogBtn.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {


                    CountDownTimer cT = new CountDownTimer(20000, 1000) {

                        public void onTick(long millisUntilFinished) {


                            String v = String.format("%02d", millisUntilFinished / 60000);
                            int va = (int) ((millisUntilFinished % 60000) / 1000);
                            holder.binding.riderWaitingTimeDigits.setText("" + v + ":" + String.format("%02d", va));
                        }

                        public void onFinish() {

                        }
                    };
                    cT.start();

                    StringBuilder sb = new StringBuilder();
                    for (String s : selectedidList) {
                        // sb.append(s);
                        if (selectedidList.size() >= 1) {

                            sb.append("," + s);
                        }/*else {
                            sb.append(s);

                        }*/
                    }

                    selectedidHashmap = new HashMap<>();
                    selectedidHashmap.put(Consts.ARTIST_ID, userDTO.getUser_id());
                    selectedidHashmap.put(Consts.BOOKING_ID, sb.toString());

                    new HttpsRequest(Consts.REQUEST_FOR_RIDER_MULTIPLE_API, selectedidHashmap, context).stringPosttwo("REQUESTFORRIDER", new Helper() {
                        @Override
                        public void backResponse(boolean flag, String msg, JSONObject response) {

                            Log.e("REQUEST_FOR_RIDER_MULTIPLE_API", "" + response.toString());
                            if (!flag) {
                                holder.binding.riderWaitingLayout.setVisibility(View.GONE);
                                holder.binding.noRiderTxt.setVisibility(View.VISIBLE);
                                holder.binding.noRiderRelative.setVisibility(View.VISIBLE);
                                selectedidList.clear();
                                selectedidList = new ArrayList<>();
                            } else {
                                holder.binding.requestridernew.setVisibility(View.GONE);
                                holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    holder.binding.requestridernew.setVisibility(View.GONE);
                    holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                    //newBookings.setAdapterAllBookings();
                    // context.startActivity(new Intent(context, BaseActivity.class));
                    ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                    bottomSheetDialog.dismiss();
                }
            });

            holder.binding.requestridernew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                    findriderHashmap.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());

                    if (artistBookingsList.size() == 1) {

                        CountDownTimer cT = new CountDownTimer(20000, 1000) {

                            public void onTick(long millisUntilFinished) {


                                String v = String.format("%02d", millisUntilFinished / 60000);
                                int va = (int) ((millisUntilFinished % 60000) / 1000);
                                holder.binding.riderWaitingTimeDigits.setText("" + v + ":" + String.format("%02d", va));
                            }

                            public void onFinish() {

                            }
                        };
                        cT.start();

                        //   new HttpsRequest(Consts.REQUEST_FOR_RIDER_API, findriderHashmap, context).stringPosttwo(TAG, new Helper() {
                        new HttpsRequest(Consts.REQUEST_FOR_RIDER_MULTIPLE_API, findriderHashmap, context).stringPosttwo(TAG, new Helper() {
                            @Override
                            public void backResponse(boolean flag, String msg, JSONObject response) {
                                //    Toast.makeText(context, "shivam1", Toast.LENGTH_SHORT).show();
                                if (!flag) {
                                    holder.binding.riderWaitingLayout.setVisibility(View.GONE);
                                    holder.binding.noRiderTxt.setVisibility(View.VISIBLE);
                                    holder.binding.noRiderRelative.setVisibility(View.VISIBLE);
                                } else {
                                    holder.binding.requestridernew.setVisibility(View.GONE);
                                    holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        requestForRiderBinding.requestTvtitle.setText("Requests For Rider");
                        requestForRiderBinding.requestRiderDialogBtn.setVisibility(View.VISIBLE);
                        requestForRiderBinding.popupstartnew.setVisibility(View.GONE);
                        showBottomDialog(multi_start_preparation);
                    }

                }
            });

            holder.binding.startdelivery.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                    startdeliveryParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());
                    //  context.startService(mServiceIntent);

                    startPreparation();
                    //  bookingstart("2", position, String.valueOf(sdf1.format(date)));
                    holder.binding.dropBtn.performClick();

                }
            });

            holder.binding.startnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                    if (NetworkManager.isConnectToInternet(context)) {


                        if (artistBookingsList.size() == 1) {

                            Log.e("multi_start_preparation", "single");
                            if (checkarss(artistBookingsList.get(position).getProduct().get(0).getCategory_id())) {
                                holder.binding.startnew.setVisibility(View.GONE);
                                bookingstart("2", position, "");
                                holder.binding.dropBtn.performClick();
                            } else {
                                Log.e("rider_order123456", "3" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                                //     holder.binding.prtTxt.setText("Preparation Time:- ");
                                holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                                //    holder.binding.counter.setVisibility(View.VISIBLE);

                                if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("1")) {
                                    Log.e("rider_order123456", "4" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                    bookingstart("2", position, String.valueOf(sdf1.format(date)));
                                    // holder.binding.requestridernew.setVisibility(View.VISIBLE);
                                } else {
                                    Log.e("rider_order123456", "5" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                    holder.binding.startnew.setVisibility(View.GONE);
                                    holder.binding.startdelivery.setVisibility(View.VISIBLE);
                                    preparationParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());
                                    bookingstart("2", position, String.valueOf(sdf1.format(date)));

                                }
                            }
                        } else {
                            multi_start_preparation = true;
                            Log.e("multi_start_preparation", "multiple");
                            requestForRiderBinding.requestTvtitle.setText("Start Preparation");
                            requestForRiderBinding.requestRiderDialogBtn.setVisibility(View.GONE);
                            requestForRiderBinding.popupstartnew.setVisibility(View.VISIBLE);
                            showBottomDialog(multi_start_preparation);
                        }


                    } else {
                        //         ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                    }

                }
            });

            holder.binding.llarrival.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arival(position);
                    holder.binding.llarrival.setVisibility(View.GONE);
                    holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                    //     holder.binding.prtTxt.setText("Preparation Time:- ");
                    holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                    //       holder.binding.counter.setVisibility(View.VISIBLE);

                    holder.binding.prepRelative.setVisibility(View.VISIBLE);
                    holder.binding.txtarivaltimer.setVisibility(View.VISIBLE);
                    holder.binding.startnew.setVisibility(View.VISIBLE);


                    CountDownTimer cT = new CountDownTimer(300000, 1000) {

                        public void onTick(long millisUntilFinished) {


                            String v = String.format("%02d", millisUntilFinished / 60000);
                            int va = (int) ((millisUntilFinished % 60000) / 1000);
                            holder.binding.preparationTime.setText("" + v + ":" + String.format("%02d", va));
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

                            holder.binding.preparationTime.setText(holder.binding.chronometer.getText().toString());
                        }
                    };
                    cT.start();

                    holder.binding.llarrival.setEnabled(false);

                }
            });

            holder.binding.orderCount.setText(String.valueOf(position + 1));
            //   if (artistBookingsList.get(position).getOrderslotno().equalsIgnoreCase("1")) {
            holder.binding.tmpView2.setBackgroundColor(context.getResources().getColor(R.color.green_slide_color));
            holder.binding.tmpView1.setBackgroundColor(context.getResources().getColor(R.color.green_slide_color));
            holder.binding.tmpView123.setBackgroundColor(context.getResources().getColor(R.color.green_slide_color));
            holder.binding.preparationLayout.setBackgroundColor(context.getResources().getColor(R.color.green_slide_color));
            holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.drawable.slot_1_bg));
            holder.binding.lunchDinner.setBackgroundTintList(context.getResources().getColorStateList(R.color.green_slide_color));

            /*} else if (artistBookingsList.get(position).getOrderslotno().equalsIgnoreCase("2")) {
                holder.binding.tmpView2.setBackgroundColor(context.getResources().getColor(R.color.mapbox_blue));
                holder.binding.tmpView1.setBackgroundColor(context.getResources().getColor(R.color.mapbox_blue));
                holder.binding.tmpView123.setBackgroundColor(context.getResources().getColor(R.color.mapbox_blue));
                holder.binding.preparationLayout.setBackgroundColor(context.getResources().getColor(R.color.mapbox_blue));
                holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.drawable.slot_2_bg));
                holder.binding.lunchDinner.setBackgroundTintList(context.getResources().getColorStateList(R.color.mapbox_blue));

            } else if (artistBookingsList.get(position).getOrderslotno().equalsIgnoreCase("3")) {
                holder.binding.tmpView2.setBackgroundColor(context.getResources().getColor(R.color.pink_color));
                holder.binding.tmpView1.setBackgroundColor(context.getResources().getColor(R.color.pink_color));
                holder.binding.tmpView123.setBackgroundColor(context.getResources().getColor(R.color.pink_color));
                holder.binding.preparationLayout.setBackgroundColor(context.getResources().getColor(R.color.pink_color));
                holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.drawable.slot_3_bg));
                holder.binding.lunchDinner.setBackgroundTintList(context.getResources().getColorStateList(R.color.pink_color));

            } else if (artistBookingsList.get(position).getOrderslotno().equalsIgnoreCase("0")) {
                holder.binding.tmpView2.setBackgroundColor(context.getResources().getColor(R.color.yellow_color));
                holder.binding.tmpView1.setBackgroundColor(context.getResources().getColor(R.color.yellow_color));
                holder.binding.tmpView123.setBackgroundColor(context.getResources().getColor(R.color.yellow_color));
                holder.binding.preparationLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow_color));
                holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.drawable.slot_advance_bg));
                holder.binding.lunchDinner.setBackgroundTintList(context.getResources().getColorStateList(R.color.yellow_color));

            }*/

            if (artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("0")
                    || artistBookingsList.get(position).getBooking_type().equalsIgnoreCase("3")) {

                if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("0")) {
                    //  Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();

                    // holder.binding.layinquiry.setVisibility(View.VISIBLE);
                    holder.binding.layaccpet.setVisibility(View.GONE);
                    holder.binding.tvnavigation.setVisibility(View.GONE);
                    holder.binding.layfinish.setVisibility(View.GONE);
                    holder.binding.tvnavigationfinish.setVisibility(View.GONE);
                    holder.binding.txtptype.setVisibility(View.GONE);
                    // holder.binding.cardpaymnet.setBackground(context.getResources().getDrawable(R.drawable.button_line));
                    //                    holder.binding.cardfinish.setBackground(context.getResources().getDrawable(R.drawable.button_line));
                    //holder.binding.cardaccept.setBackground(context.getResources().getDrawable(R.drawable.button_line));
                    // holder.binding.cardinquiry.setBackground(context.getResources().getDrawable(R.drawable.button_line_blue));

                    holder.binding.imgone.setImageResource(R.drawable.bone);


                    holder.binding.imgtwo.setImageResource(R.drawable.blacktwo);


                    holder.binding.imgthree.setImageResource(R.drawable.blackthree);


                    holder.binding.imgfour.setImageResource(R.drawable.blackfour);


                    holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                    holder.binding.counter.setIsShowingTextDesc(true);
                    holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                    holder.binding.counter.setTextColor(Color.WHITE);

                    holder.binding.counter.setListener(new Counter.Listener() {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                        }

                        @Override
                        public void onTick(long days, long hours, long minutes, long seconds) {
                            Log.d(TAG, "onTick" + days + "D " +
                                    hours + "H " +
                                    minutes + "M " +
                                    seconds + "S ");

                            String newtime = "";

                            if (days == 00 && hours == 00) {
                                newtime =
                                        minutes + "M " +
                                                seconds + "S ";
                            } else if (days == 00 && hours != 00) {
                                //   Log.e("DATE_FORMAT", "second");

                                newtime =
                                        hours + "H " + minutes + "M " + seconds + "S ";
                            } else {
                            }
                            holder.binding.preparationTime.setText(newtime);

                        }
                    });

                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("1")
                        && artistBookingsList.get(pos).getRider_order().equalsIgnoreCase("0")) {

                    if (artistBookingsList.get(pos).getProduct().size() != 0) {


                        if (checkarss(artistBookingsList.get(pos).getProduct().get(0).getCategory_id())) {
                            //    holder.binding.cardData.setBackgroundColor(R.color.light_blue);
                            holder.binding.llarrival.setVisibility(View.VISIBLE);
                            holder.binding.prepRelative.setVisibility(View.INVISIBLE);
                            holder.binding.preparationTitle.setText("Waiting time: ");
                            holder.binding.layaccpet.setVisibility(View.VISIBLE);
                            holder.binding.pickupBtnDupli.setVisibility(View.GONE);
                            holder.binding.pickupBtn.setVisibility(View.VISIBLE);
                            holder.binding.layfinish.setVisibility(View.GONE);
                            holder.binding.typeLl.setVisibility(View.GONE);
                            holder.binding.tvnavigationfinish.setVisibility(View.GONE);
                            holder.binding.txtptype.setVisibility(View.GONE);
                            holder.binding.preparationTime.setText("5:00");

                        } else {

                            if (artistBookingsList.get(pos).getPreparation_status().equalsIgnoreCase("1")) {
                                //holder.binding.startdelivery.setVisibility(View.VISIBLE);
                            } else {
                                holder.binding.startnew.setVisibility(View.VISIBLE);
                                holder.binding.startnew.setText("Start Preparation");
                            }
                            Log.e("rider_order123456", "rider no order nathi" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                            holder.binding.layaccpet.setVisibility(View.VISIBLE);
                            holder.binding.tvnavigation.setVisibility(View.VISIBLE);
                            holder.binding.layfinish.setVisibility(View.GONE);
                            holder.binding.duplicatePickupTxt.setText("Delivery Location");
                            holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                            // holder.binding.prtTxt.setText("Preparation Time:- ");

                            holder.binding.tvnavigationfinish.setVisibility(View.GONE);
                            holder.binding.txtptype.setVisibility(View.GONE);
                            holder.binding.preparationLayout.setVisibility(View.GONE);

                            SimpleDateFormat format = new SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                            Date date = null;
                            try {
                                date = format.parse(artistBookingsList.get(position).getPreparation_time());
                                holder.binding.counter.setDate(date);//countdown starts

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                            holder.binding.counter.setIsShowingTextDesc(true);

                            holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                            holder.binding.counter.setTextColor(Color.WHITE);

                            holder.binding.counter.setListener(new Counter.Listener() {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                                }

                                @Override
                                public void onTick(long days, long hours, long minutes, long seconds) {
                                    Log.d(TAG, "onTick" + days + "D " +
                                            hours + "H " +
                                            minutes + "M " +
                                            seconds + "S ");

                                    String newtime = "";

                                    if (days == 00 && hours == 00) {
                                        //      Log.e("DATE_FORMAT", "first");
                                        newtime =
                                                minutes + "M " +
                                                        seconds + "S ";
                                    } else if (days == 00 && hours != 00) {
                                        //           Log.e("DATE_FORMAT", "second");

                                        newtime =
                                                hours + "H " + minutes + "M " + seconds + "S ";
                                    } else {
                                    }
                                    holder.binding.preparationTime.setText(newtime);

                                }
                            });
                        }

                        holder.binding.imgone.setImageResource(R.drawable.gone);


                        holder.binding.imgtwo.setImageResource(R.drawable.btwo);


                        holder.binding.imgthree.setImageResource(R.drawable.blackthree);


                        holder.binding.imgfour.setImageResource(R.drawable.blackfour);
                    }

                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("1")
                        && artistBookingsList.get(position).getRider_flag().equalsIgnoreCase("0")) {

                    Log.e("rider_order123456", "101" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                    holder.binding.layaccpet.setVisibility(View.GONE);
                    holder.binding.tvnavigation.setVisibility(View.GONE);
                    holder.binding.startnew.setVisibility(View.VISIBLE);
                    holder.binding.startnew.setText("Start Preparation");
                    holder.binding.layfinish.setVisibility(View.GONE);
                    holder.binding.llLocation.setVisibility(View.GONE);
                    holder.binding.llLocationdesti.setVisibility(View.GONE);
                    holder.binding.tmpView11.setVisibility(View.GONE);
                    holder.binding.duplicatePickupTxt.setText("Delivery Location");
                    holder.binding.tmpView2.setVisibility(View.GONE);
                    holder.binding.dropBtn.setVisibility(View.GONE);
                    holder.binding.prepRelative.setVisibility(View.VISIBLE);
                    holder.binding.riderWaitingLayout.setVisibility(View.GONE);
                    holder.binding.tvnavigationfinish.setVisibility(View.GONE);
                    holder.binding.txtptype.setVisibility(View.GONE);
                    holder.binding.preparationLayout.setVisibility(View.VISIBLE);

                    /*if (holder.binding.requestridernew.getVisibility()==View.VISIBLE){
                        holder.binding.requestridernew.setVisibility(View.GONE);
                    }*/
                    if (artistBookingsList.get(position).getGetdeliveryflag().equalsIgnoreCase("1")) {

                        holder.binding.prtTxt.setVisibility(View.GONE);
                        holder.binding.counter.setVisibility(View.GONE);
                        holder.binding.arrivalTimeDigit.setVisibility(View.VISIBLE);

                        holder.binding.startnew.setEnabled(false);
                        holder.binding.startnew.setClickable(false);
                    } else {

                        //       holder.binding.prtTxt.setVisibility(View.VISIBLE);
                        //   holder.binding.counter.setVisibility(View.VISIBLE);
                        holder.binding.arrivalTimeDigit.setVisibility(View.GONE);

                        holder.binding.startnew.setEnabled(true);
                        holder.binding.startnew.setClickable(true);
                    }

                    if (artistBookingsList.get(position).getCheck_artist_preparation().equalsIgnoreCase("0")) {

                        //  holder.binding.addPreparationTime.setVisibility(View.VISIBLE);

                    } else {
                        //   holder.binding.addPreparationTime.setVisibility(View.GONE);
                    }

                    SimpleDateFormat format12 = new SimpleDateFormat(
                            "HH:mm:ss", Locale.US);
                    format12.setTimeZone(TimeZone.getTimeZone("UTC"));

                    try {
                    //    Date d = format12.parse(artistBookingsList.get(position).getPreparation_time());
                     //   holder.binding.arrivalTimeDigit.setText(d.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  //  Log.e("PREPARATION_TIME", "1" + artistBookingsList.get(position).getPreparation_time() + "Position:-- " + position);

                    holder.binding.imgone.setImageResource(R.drawable.gone);


                    holder.binding.imgtwo.setImageResource(R.drawable.btwo);


                    holder.binding.imgthree.setImageResource(R.drawable.blackthree);


                    holder.binding.imgfour.setImageResource(R.drawable.blackfour);

                    SimpleDateFormat format = new SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                    Date date = null;
                    try {
                        date = format.parse(artistBookingsList.get(position).getPreparation_time());
                        holder.binding.counter.setDate(date);//countdown starts

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //or use:
                    Log.e("PREPARATION_TIME", " preparation time :-- " + artistBookingsList.get(position).getPreparation_time());

                    holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                    holder.binding.counter.setIsShowingTextDesc(true);

                    holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                    holder.binding.counter.setTextColor(Color.WHITE);
                    holder.binding.counter.setTextSize(25);

                    holder.binding.counter.setListener(new Counter.Listener() {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                        }

                        @Override
                        public void onTick(long days, long hours, long minutes, long seconds) {
                            Log.d(TAG, "onTick" + days + "D " +
                                    hours + "H " +
                                    minutes + "M " +
                                    seconds + "S ");
                            String newtime = "";

                            if (days != 00 && hours != 00) {
                                newtime = days + "D " +
                                        hours + "H " +
                                        minutes + "M " +
                                        seconds + "S ";
                            } else if (days == 00 && hours != 00) {
                                newtime =
                                        hours + "H " + minutes + "M " + seconds + "S ";
                            } else if (days == 00 && hours == 00) {
                                newtime =
                                        minutes + "M " +
                                                seconds + "S ";
                            } else {
                                newtime = days + "D " +
                                        hours + "H " +
                                        minutes + "M " +
                                        seconds + "S ";
                            }
                            holder.binding.preparationTime.setText(newtime);

                        }
                    });

                  /*  holder.binding.addPreparationTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            close_ship_dialog = addPrepTimeDialog.findViewById(R.id.add_time_close_ship_dialog);
                            time_reason_txt = addPrepTimeDialog.findViewById(R.id.time_reason_txt);
                            add_timer_recyclerview = addPrepTimeDialog.findViewById(R.id.add_timer_recyclerview);
                            add_time_tvYesPro = addPrepTimeDialog.findViewById(R.id.add_time_tvYesPro);

                            close_ship_dialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addPrepTimeDialog.dismiss();
                                }
                            });

                            addPrepTimeList = artistBookingsList.get(position).getAdd_time_array();
                            gridLayoutManager1 = new GridLayoutManager(context, 3);

                            AddPreparationTimeAdapter preparationTimeAdapter = new AddPreparationTimeAdapter(context, addPrepTimeList, new AddPrepTime() {
                                @Override
                                public void getTime(String time) {
                                    selected_prep_time = time;

                                }
                            });
                            add_timer_recyclerview.setLayoutManager(gridLayoutManager1);
                            add_timer_recyclerview.setAdapter(preparationTimeAdapter);

                            add_time_tvYesPro.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    updatetimeparms.put("reason", time_reason_txt.getText().toString());
                                    updatetimeparms.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());
                                    updatetimeparms.put("update_preparation_time", selected_prep_time);


                                    Log.e("selected_prep_time", "" + updatetimeparms.toString());

                                    new HttpsRequest(Consts.UPDATE_PREPARATION_TIME_API, updatetimeparms, context).stringPost("update_time", new Helper() {
                                        @Override
                                        public void backResponse(boolean flag, String msg, JSONObject response) {

                                            if (flag) {

                                                holder.binding.startnew.setEnabled(false);
                                                holder.binding.startnew.setClickable(false);
                                                addPrepTimeDialog.dismiss();
                                                holder.binding.prtTxt.setVisibility(View.GONE);
                                                holder.binding.counter.setVisibility(View.GONE);
                                                holder.binding.arrivalTimeDigit.setVisibility(View.VISIBLE);
                                                holder.binding.addPreparationTime.setVisibility(View.GONE);
                                                //    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                newBookings.getBookings();
                                            } else {
                                                //            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            });

                            addPrepTimeDialog.show();
                        }
                    });

*/
                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("1")
                        && artistBookingsList.get(position).getRider_flag().equalsIgnoreCase("1")) {

                    if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("0")) {

                        Log.e("rider_order123456", "102" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                        Log.e("PREPARATION_TIME", "2" + artistBookingsList.get(position).getPreparation_time() + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                        holder.binding.layaccpet.setVisibility(View.VISIBLE);
                        holder.binding.cabRequestRelative.setVisibility(View.VISIBLE);
                        holder.binding.llTime.setVisibility(View.GONE);
                        holder.binding.startnew.setVisibility(View.VISIBLE);
                        holder.binding.riderDetails.setVisibility(View.VISIBLE);
                        holder.binding.riderRelative.setVisibility(View.VISIBLE);
                        holder.binding.preparationLayout.setVisibility(View.GONE);
                        holder.binding.tvnavigation.setVisibility(View.VISIBLE);
                        holder.binding.layfinish.setVisibility(View.VISIBLE);
                        holder.binding.tvnavigationfinish.setVisibility(View.GONE);
                        holder.binding.txtptype.setVisibility(View.GONE);
                        holder.binding.riderNameTxt.setText(artistBookingsList.get(position).getRider_name());

                        holder.binding.imgone.setImageResource(R.drawable.gone);


                        holder.binding.imgtwo.setImageResource(R.drawable.btwo);


                        holder.binding.imgthree.setImageResource(R.drawable.blackthree);


                        holder.binding.imgfour.setImageResource(R.drawable.blackfour);

                        CountDownTimer cT2 = new CountDownTimer(Long.parseLong(artistBookingsList.get(position).getRider_arrival_time()), 1000) {

                            public void onTick(long millisUntilFinished) {


                                int seconds = (int) (millisUntilFinished / 1000) % 60;
                                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                                String newtime = hours + ":" + minutes + ":" + seconds;


                                String v = String.format("%02d", millisUntilFinished / 60000);
                                int va = (int) ((millisUntilFinished % 60000) / 1000);
                                int hr = ((va % 60000) / 1000);
                                holder.binding.arrivalTimeDigit.setText("" + hours + ":" + minutes + ":" + String.format("%02d", seconds));

                                if (newtime.equals("0:0:0")) {
                                    holder.binding.arrivalTimeDigit.setText("00:00:00");
                                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                    holder.binding.arrivalTimeDigit.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                                    holder.binding.arrivalTimeDigit.setText("0" + hours + ":0" + minutes + ":" + seconds);
                                } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                    holder.binding.arrivalTimeDigit.setText("0" + hours + ":" + minutes + ":0" + seconds);
                                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                    holder.binding.arrivalTimeDigit.setText(hours + ":0" + minutes + ":0" + seconds);
                                } else if (String.valueOf(hours).length() == 1) {
                                    holder.binding.arrivalTimeDigit.setText("0" + hours + ":" + minutes + ":" + seconds);
                                } else if (String.valueOf(minutes).length() == 1) {
                                    holder.binding.arrivalTimeDigit.setText(hours + ":0" + minutes + ":" + seconds);
                                } else if (String.valueOf(seconds).length() == 1) {
                                    holder.binding.arrivalTimeDigit.setText(hours + ":" + minutes + ":0" + seconds);
                                } else {
                                    holder.binding.arrivalTimeDigit.setText(hours + ":" + minutes + ":" + seconds);
                                }
                            }

                            public void onFinish() {
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

                                holder.binding.arrivalTimeDigit.setText(holder.binding.chronometer.getText().toString());
                            }
                        };
                        cT2.start();

                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                        Date date = null;
                        try {
                            date = format.parse(artistBookingsList.get(position).getPreparation_time());
                            holder.binding.counter.setDate(date);//countdown starts

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                        holder.binding.counter.setIsShowingTextDesc(true);

                        holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                        holder.binding.counter.setTextColor(Color.WHITE);

                        holder.binding.counter.setListener(new Counter.Listener() {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                            }

                            @Override
                            public void onTick(long days, long hours, long minutes, long seconds) {
                                Log.d(TAG, "onTick" + days + "D " +
                                        hours + "H " +
                                        minutes + "M " +
                                        seconds + "S ");

                                String newtime = "";

                                if (days == 00 && hours == 00) {
                                    newtime =
                                            minutes + "M " +
                                                    seconds + "S ";
                                } else if (days == 00 && hours != 00) {

                                    newtime =
                                            hours + "H " + minutes + "M " + seconds + "S ";
                                } else {
                                }
                                holder.binding.preparationTime.setText(newtime);

                            }
                        });

                    } else {
                        Log.e("rider_order123456", "103" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                        Log.e("PREPARATION_TIME", "3" + artistBookingsList.get(position).getPreparation_time() + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                        holder.binding.layaccpet.setVisibility(View.GONE);
                        holder.binding.tvnavigation.setVisibility(View.GONE);
                        holder.binding.layinquiry.setVisibility(View.GONE);
                        holder.binding.dropBtn.setVisibility(View.GONE);
                        holder.binding.prepRelative.setVisibility(View.VISIBLE);
                        holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                        //  holder.binding.counter.setVisibility(View.VISIBLE);
                        //    holder.binding.prtTxt.setText("Preparation Time:- ");
                        holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                        //     holder.binding.counter.setVisibility(View.VISIBLE);

                        holder.binding.finishnew.setVisibility(View.VISIBLE);
                        //holder.binding.prepRelative.setVisibility(View.INVISIBLE);
                        // holder.binding.dropBtn.setVisibility(View.GONE);
                        holder.binding.llTime.setVisibility(View.GONE);
                        // holder.binding.tvnavigationfinish.setVisibility(View.VISIBLE);
                        holder.binding.txtptype.setVisibility(View.GONE);

                        SimpleDateFormat format = new SimpleDateFormat(
                                "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                        Date date = null;
                        try {
                            date = format.parse(artistBookingsList.get(position).getPreparation_time());
                            holder.binding.counter.setDate(date);//countdown starts

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                        holder.binding.counter.setIsShowingTextDesc(true);

                        holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                        holder.binding.counter.setTextColor(Color.WHITE);

                        holder.binding.counter.setListener(new Counter.Listener() {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                            }

                            @Override
                            public void onTick(long days, long hours, long minutes, long seconds) {
                                Log.d(TAG, "onTick" + days + "D " +
                                        hours + "H " +
                                        minutes + "M " +
                                        seconds + "S ");

                                String newtime = "";

                                if (days == 00 && hours == 00) {
                                    newtime =
                                            minutes + "M " +
                                                    seconds + "S ";
                                } else if (days == 00 && hours != 00) {

                                    newtime =
                                            hours + "H " + minutes + "M " + seconds + "S ";
                                } else {
                                }
                                holder.binding.preparationTime.setText(newtime);

                            }
                        });
                    }
                } else if (artistBookingsList.get(position).getBooking_flag().equalsIgnoreCase("3")) {

                    if (artistBookingsList.get(pos).getProduct().size() != 0) {

                        if (checkarss(artistBookingsList.get(pos).getProduct().get(0).getCategory_id())) {

                            holder.binding.preparationTitle.setText("Working time: ");
                            holder.binding.layaccpet.setVisibility(View.GONE);
                            holder.binding.tvnavigation.setVisibility(View.GONE);
                            holder.binding.layinquiry.setVisibility(View.GONE);
                            holder.binding.pickupBtn.setVisibility(View.GONE);
                            holder.binding.pickupBtnDupli.setVisibility(View.GONE);
                            holder.binding.dropBtn.setVisibility(View.VISIBLE);
                            holder.binding.llCancel.setVisibility(View.GONE);
                            holder.binding.llCancelduplicate.setVisibility(View.VISIBLE);
                            holder.binding.prepRelative.setVisibility(View.VISIBLE);
                            holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                            //      holder.binding.prtTxt.setText("Preparation Time:- ");
                            holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                            //   holder.binding.counter.setVisibility(View.VISIBLE);
                            holder.binding.chronometer.setVisibility(View.VISIBLE);
                            holder.binding.preparationTime.setVisibility(View.GONE);
                            holder.binding.startnew.setVisibility(View.GONE);
                            holder.binding.typeLl.setVisibility(View.GONE);
                            holder.binding.finishnew.setVisibility(View.VISIBLE);
                            holder.binding.layfinish.setVisibility(View.GONE);
                            holder.binding.preparationTitle.setTextColor(context.getResources().getColor(R.color.green));
                            Chronometer simpleChronometer = (Chronometer) holder.binding.chronometer;

                            simpleChronometer.start();
                        } else {
                            if (artistBookingsList.get(position).getRider_order().equals("1") &&
                                    artistBookingsList.get(position).getRider_flag().equalsIgnoreCase("1")) {
                                Log.e("rider_order123456", "301" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                Log.e("PREPARATION_TIME", "4" + artistBookingsList.get(position).getPreparation_time() + "Position:-- " + position);

                                //           holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.color.white));
                                holder.binding.layaccpet.setVisibility(View.GONE);
                                holder.binding.tvnavigation.setVisibility(View.GONE);
                                holder.binding.layinquiry.setVisibility(View.GONE);
                                holder.binding.dropBtn.setVisibility(View.GONE);
                                holder.binding.riderDetails.setVisibility(View.VISIBLE);
                                holder.binding.riderRelative.setVisibility(View.VISIBLE);
                                // holder.binding.cabRequestRelative.setVisibility(View.GONE);
                                holder.binding.prepRelative.setVisibility(View.VISIBLE);
                                holder.binding.startnew.setVisibility(View.GONE);
                                holder.binding.callLinear.setVisibility(View.GONE);
                                holder.binding.callLinearDisable.setVisibility(View.VISIBLE);
                                holder.binding.requestridernew.setVisibility(View.GONE);
                                holder.binding.linearL.setVisibility(View.GONE);
                                holder.binding.llLocation.setVisibility(View.GONE);
                                holder.binding.llLocationdesti.setVisibility(View.GONE);
                                holder.binding.tmpView11.setVisibility(View.GONE);
                                holder.binding.tmpView2.setVisibility(View.GONE);
                                holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                                holder.binding.riderNameTxt.setText(artistBookingsList.get(position).getRider_name());

                                holder.binding.llTime.setVisibility(View.GONE);
                                holder.binding.txtptype.setVisibility(View.GONE);
                                holder.binding.counter.setVisibility(View.GONE);
                                holder.binding.arrivalTimeDigit.setVisibility(View.VISIBLE);
                                holder.binding.preparationLayout.setVisibility(View.GONE);
                                //        holder.binding.prtTxt.setText("Arrival Time:- ");
                                CountDownTimer cT2 = new CountDownTimer(Long.parseLong(artistBookingsList.get(position).getRider_arrival_time()), 1000) {

                                    public void onTick(long millisUntilFinished) {


                                        int seconds = (int) (millisUntilFinished / 1000) % 60;
                                        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                                        int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                                        String newtime = hours + ":" + minutes + ":" + seconds;


                                        String v = String.format("%02d", millisUntilFinished / 60000);
                                        int va = (int) ((millisUntilFinished % 60000) / 1000);
                                        int hr = ((va % 60000) / 1000);
                                        holder.binding.arrivalTimeDigit.setText("" + hours + ":" + minutes + ":" + String.format("%02d", seconds));

                                        if (newtime.equals("0:0:0")) {
                                            holder.binding.arrivalTimeDigit.setText("00:00:00");
                                        } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                            holder.binding.arrivalTimeDigit.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                                        } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                                            holder.binding.arrivalTimeDigit.setText("0" + hours + ":0" + minutes + ":" + seconds);
                                        } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                            holder.binding.arrivalTimeDigit.setText("0" + hours + ":" + minutes + ":0" + seconds);
                                        } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                                            holder.binding.arrivalTimeDigit.setText(hours + ":0" + minutes + ":0" + seconds);
                                        } else if (String.valueOf(hours).length() == 1) {
                                            holder.binding.arrivalTimeDigit.setText("0" + hours + ":" + minutes + ":" + seconds);
                                        } else if (String.valueOf(minutes).length() == 1) {
                                            holder.binding.arrivalTimeDigit.setText(hours + ":0" + minutes + ":" + seconds);
                                        } else if (String.valueOf(seconds).length() == 1) {
                                            holder.binding.arrivalTimeDigit.setText(hours + ":" + minutes + ":0" + seconds);
                                        } else {
                                            holder.binding.arrivalTimeDigit.setText(hours + ":" + minutes + ":" + seconds);
                                        }
                                    }

                                    public void onFinish() {
                                        SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");
                                        Date dt = null;
                                        int min = 0;
                                        int sec = 0;
                                        if (artistBookingsList.get(position).getRider_arrival_time().equalsIgnoreCase("0")) {
                                            try {
                                                dt = sdf.parse("0.1");
                                                sdf = new SimpleDateFormat("HH:mm:ss");
                                                min = dt.getHours() * 60 + dt.getMinutes();
                                                sec = dt.getSeconds();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            try {
                                                dt = sdf.parse(artistBookingsList.get(position).getRider_arrival_time());
                                                sdf = new SimpleDateFormat("HH:mm:ss");
                                                min = dt.getHours() * 60 + dt.getMinutes();
                                                sec = dt.getSeconds();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        holder.binding.riderArrivalChronometer.setBase(SystemClock.elapsedRealtime() - (min * 60000 + sec * 1000));
                                        holder.binding.riderArrivalChronometer.start();

                                        holder.binding.arrivalTimeDigit.setText(holder.binding.riderArrivalChronometer.getText().toString());
                                    }
                                };
                                cT2.start();
                            } else if (artistBookingsList.get(position).getRider_order().equals("1") &&
                                    artistBookingsList.get(position).getRider_flag().equalsIgnoreCase("3")) {

                                Log.e("rider_order123456", "302" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                Log.e("PREPARATION_TIME", "7" + artistBookingsList.get(position).getPreparation_time() + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                if (artistBookingsList.get(position).getPickup_status().equalsIgnoreCase("0")) {

                                    thank_you_dialog = true;
                                    tvtotalfinish = dialogThankYou.findViewById(R.id.tvtotalfinish);
                                    pay_done_img = dialogThankYou.findViewById(R.id.pay_done_img);
                                    dialog_paytype = dialogThankYou.findViewById(R.id.dialog_paytype);
                                    layfinishbackground = dialogThankYou.findViewById(R.id.layfinishbackground);

                                    ok_btn = dialogThankYou.findViewById(R.id.okbtn);
                                    thankyoudialog_paytype = dialogThankYou.findViewById(R.id.thankyoudialog_paytype);
                                    thank_u_dialog_close = dialogThankYou.findViewById(R.id.thank_tvfinishcancel);

                                    thankyoudialog_paytype.setText(Html.fromHtml("Your order is picked up by <b color=\\\"#22cb4a\\\"><font color='#22cb4a'>" +
                                            artistBookingsList.get(position).getRider_name() + "</font></b> <br>and<br>will be delivered to <b><font color='#22cb4a'>" + artistBookingsList.get(position).getUserName() + "</font></b>"));

                                    if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("0")) {

                                        dialog_paytype.setText("Onlile Payment");
                                        pay_done_img.setImageResource(R.drawable.ic_payment_done);

                                    } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("1")) {
                                        dialog_paytype.setText("Cash Payment");


                                    } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("2")) {
                                        dialog_paytype.setText("Wallet Payment");
                                        pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                    }

                                    tvtotalfinish.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getActuall_collect()));
                                    Log.e("rider_order123456", " pickup_status " + artistBookingsList.get(position).getPickup_status() + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                    ok_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.e("ok_btn", "btn clicked");
                                            dialogThankYou.dismiss();
                                            preparationParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());
                                            updatePickupStatus();
                                        }
                                    });

                                    thank_u_dialog_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogThankYou.dismiss();
                                            booking("3", position);
                                        }
                                    });
//                                    dialogThankYou.show();

                                } else {
                                    dialogThankYou.dismiss();

                                    thank_you_dialog = false;
                                }

                            } else if (artistBookingsList.get(position).getRider_order().equals("1") &&
                                    artistBookingsList.get(position).getRider_flag().equalsIgnoreCase("0")) {
                                Log.e("rider_order123456", "1" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                Log.e("PREPARATION_TIME", "22" + artistBookingsList.get(position).getPreparation_time() + "Position:-- " + position);

                                //             holder.binding.cardRelativeMain.setBackground(context.getResources().getDrawable(R.color.white));
                                holder.binding.layaccpet.setVisibility(View.GONE);
                                holder.binding.tvnavigation.setVisibility(View.GONE);
                                holder.binding.layinquiry.setVisibility(View.GONE);
                                holder.binding.dropBtn.setVisibility(View.GONE);
                                holder.binding.linearL.setVisibility(View.GONE);
                                holder.binding.llLocation.setVisibility(View.GONE);
                                holder.binding.llLocationdesti.setVisibility(View.GONE);
                                holder.binding.tmpView11.setVisibility(View.GONE);
                                holder.binding.duplicatePickupTxt.setText("Delivery Location");
                                holder.binding.cabRequestRelative.setVisibility(View.VISIBLE);
                                holder.binding.prepRelative.setVisibility(View.VISIBLE);
                                holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                                //       holder.binding.prtTxt.setText("Preparation Time:- ");
                                holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                                //  holder.binding.counter.setVisibility(View.VISIBLE);
                                holder.binding.addPreparationTime.setVisibility(View.GONE);
                                holder.binding.startnew.setVisibility(View.GONE);

                                if (artistBookingsList.get(position).getRequest_for_rider_status().equalsIgnoreCase("1")) {
                                    holder.binding.requestridernew.setVisibility(View.GONE);
                                    holder.binding.riderWaitingLayout.setVisibility(View.VISIBLE);
                                } else {

                                    if (artistBookingsList.get(position).getOrder_disable().equalsIgnoreCase("1")) {
                                        holder.binding.requestAcceptBtRiderLayout.setEnabled(false);
                                        Log.e("linearLayout", "ascas");

                                        holder.binding.requestAcceptBtRiderLayout.setClickable(false);
                                        holder.binding.requestridernew.setVisibility(View.GONE);
                                        holder.binding.requestridernew1.setEnabled(false);
                                        holder.binding.requestridernew1.setClickable(false);
                                        holder.binding.requestridernew1.setVisibility(View.VISIBLE);
                                    } else {
                                        Log.e("linearLayout", "ascas adas");

                                        holder.binding.requestAcceptBtRiderLayout.setEnabled(true);
                                        holder.binding.requestAcceptBtRiderLayout.setClickable(true);
                                        holder.binding.requestridernew.setVisibility(View.VISIBLE);
                                        holder.binding.requestridernew1.setVisibility(View.GONE);
                                    }
                                }
                                holder.binding.finishnew.setVisibility(View.GONE);
                                holder.binding.llTime.setVisibility(View.GONE);
                                holder.binding.txtptype.setVisibility(View.GONE);


                                SimpleDateFormat format = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                                Date date = null;
                                try {
                                    date = format.parse(artistBookingsList.get(position).getPreparation_time());
                                    holder.binding.counter.setDate(date);//countdown starts

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                                holder.binding.counter.setIsShowingTextDesc(true);
                                holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                                holder.binding.counter.setTextColor(Color.WHITE);
                                holder.binding.counter.setListener(new Counter.Listener() {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }

                                    @Override
                                    public void onTick(long days, long hours, long minutes, long seconds) {

                                        String newtime = "";

                                        if (days == 00 && hours == 00) {
                                            newtime =
                                                    minutes + "M " +
                                                            seconds + "S ";
                                        } else if (days == 00 && hours != 00) {

                                            newtime =
                                                    hours + "H " + minutes + "M " + seconds + "S ";
                                        } else {
                                        }
                                        holder.binding.preparationTime.setText(newtime);

                                    }
                                });

                            } else {
                                Log.e("rider_order123456", "22" + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);
                                Log.e("PREPARATION_TIME", "23" + artistBookingsList.get(position).getPreparation_time() + " orderid :-- " + artistBookingsList.get(position).getId() + " position " + position);

                                if (artistBookingsList.get(position).getPreparation_status().equalsIgnoreCase("1")) {

                                    holder.binding.startdelivery.setVisibility(View.GONE);
                                    holder.binding.finishnew.setVisibility(View.VISIBLE);
                                    holder.binding.finishnew.setText("Delivered");
                                } else {
                                    holder.binding.startdelivery.setVisibility(View.VISIBLE);
                                }
                                holder.binding.layaccpet.setVisibility(View.GONE);
                                holder.binding.preparationTitle.setText("Working time: ");
                                holder.binding.tvnavigation.setVisibility(View.GONE);
                                holder.binding.layinquiry.setVisibility(View.GONE);
                                holder.binding.pickupBtn.setVisibility(View.GONE);
                                holder.binding.pickupBtnDupli.setVisibility(View.GONE);
                                holder.binding.dropBtn.setVisibility(View.VISIBLE);
                                holder.binding.dropBtnTxt.setText("Delivery Location");
                                holder.binding.llCancel.setVisibility(View.GONE);
                                holder.binding.llCancelduplicate.setVisibility(View.VISIBLE);
                                holder.binding.startnew.setVisibility(View.GONE);
                                holder.binding.prepRelative.setVisibility(View.VISIBLE);
                                holder.binding.preparationLayout.setVisibility(View.VISIBLE);
                                //     holder.binding.prtTxt.setText("Preparation Time:- ");
                                holder.binding.arrivalTimeDigit.setVisibility(View.GONE);
                                holder.binding.preparationTime.setVisibility(View.GONE);
                                //        holder.binding.counter.setVisibility(View.VISIBLE);

                                holder.binding.chronometer.setVisibility(View.GONE);
                                holder.binding.llTime.setVisibility(View.GONE);

                                holder.binding.layfinish.setVisibility(View.VISIBLE);
                                holder.binding.txtptype.setVisibility(View.GONE);

                                Chronometer simpleChronometer = (Chronometer) holder.binding.chronometer;

                                simpleChronometer.start();
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

                                Date date = null;
                                try {
                                    date = format.parse(artistBookingsList.get(position).getPreparation_time());
                                    holder.binding.counter.setDate(date);//countdown starts

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                                holder.binding.counter.setIsShowingTextDesc(true);
                                holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                                holder.binding.counter.setTextColor(Color.WHITE);

                                holder.binding.counter.setListener(new Counter.Listener() {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        Log.d(TAG, "onTick: Counter - " + millisUntilFinished);
                                    }

                                    @Override
                                    public void onTick(long days, long hours, long minutes, long seconds) {
                                        Log.d(TAG, "onTick" + days + "D " +
                                                hours + "H " +
                                                minutes + "M " +
                                                seconds + "S ");

                                        String newtime = "";

                                        if (days == 00 && hours == 00) {
                                            newtime =
                                                    minutes + "M> " +
                                                            seconds + "S> ";
                                        } else if (days == 00 && hours != 00) {

                                            newtime =
                                                    hours + "H< " + minutes + "M< " + seconds + "S< ";
                                        } else {
                                        }
                                    }
                                });
                            }


                        }


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
            }

            if (artistBookingsList.get(position).getPickup_status().equalsIgnoreCase("1")) {

                dialogThankYou.dismiss();
            }

            holder.binding.dropBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkarss(artistBookingsList.get(pos).getProduct().get(0).getCategory_id())) {
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getDestinationaddress());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);
                    } else {
                        if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("0")) {
                            Log.e("dest12345", "   1   " + artistBookingsList.get(position).getDestinationaddress2());

                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getDestinationaddress2());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);
                        } else {
                            Log.e("dest12345", "   2   " + artistBookingsList.get(position).getDestinationaddress2());
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getDestinationaddress2());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);
                        }
                    }


                }
            });


            holder.binding.pickupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + artistBookingsList.get(position).getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);

                }
            });

            preparationParams.put(Consts.BOOKING_ID, artistBookingsList.get(position).getId());


            holder.binding.llAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkManager.isConnectToInternet(context)) {
                        booking("1", position);
                    } else {
                        //        ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                    }
                }
            });
            holder.binding.llDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //     etd_reason = dialogdecline.findViewById(R.id.etd_reason);
                    tvdCancelreason = dialogdecline.findViewById(R.id.tvdCancelreason);
                    tvdAddreason = dialogdecline.findViewById(R.id.tvdAddreason);
                    cancell_reason_list = dialogdecline.findViewById(R.id.cancell_reason_list);
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


                            if (reason.equalsIgnoreCase("")) {
                                ProjectUtils.showToast(context, "Please Enter Reason");
                            } else {

                                newBookings.autodecline(artistBookingsList.get(position).getId());
                                dialogdecline.dismiss();
                            }


                        }
                    });


                }
            });


            holder.binding.llCancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   holder.binding.llarrival.setVisibility(View.GONE);
                    //  etd_reason = dialogdecline.findViewById(R.id.etd_reason);
                    tvdCancelreason = dialogdecline.findViewById(R.id.tvdCancelreason);
                    tvdAddreason = dialogdecline.findViewById(R.id.tvdAddreason);
                    dialogdecline.show();

                    tvdCancelreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

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

            holder.binding.llCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvdCancelreason = dialogdecline.findViewById(R.id.tvdCancelreason);
                    cancell_reason_list = dialogdecline.findViewById(R.id.cancell_reason_list);
                    tvdAddreason = dialogdecline.findViewById(R.id.tvdAddreason);
                    dialogdecline.show();

                    tvdCancelreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            cancelreason = "";
                            dialogdecline.dismiss();
                            holder.binding.llCancel.setVisibility(View.VISIBLE);
                            holder.binding.llCancelduplicate.setVisibility(View.GONE);
                        }
                    });

                    tvdAddreason.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.e("cancelreason", "" + cancelreason);
                            if (cancelreason.equalsIgnoreCase("")) {
                                ProjectUtils.showToast(context, "Please Enter Reason");
                            } else {

                                holder.binding.llCancel.setVisibility(View.GONE);
                                holder.binding.llCancelduplicate.setVisibility(View.VISIBLE);
                                decline(position, cancelreason);
                                //  decline(position, cancelreason);
                                dialogdecline.dismiss();
                            }


                        }
                    });

                }
            });

            holder.binding.finishnew.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                    Log.e("atchual_amount", "" + artistBookingsList.get(position).getActuall_collect());
                    if (artistBookingsList.get(position).getRider_order().equalsIgnoreCase("1")) {

                        booking("3", position);

                    } else {

                        if (checkarss(artistBookingsList.get(position).getProduct().get(0).getCategory_id())) {

                            tvtotalfinish = dialogfinish.findViewById(R.id.tvtotalfinish);
                            pay_done_img = dialogfinish.findViewById(R.id.pay_done_img);
                            dialog_paytype = dialogfinish.findViewById(R.id.dialog_paytype);
                            tvfinishsubmit = dialogfinish.findViewById(R.id.tvfinishsubmit);
                            tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                            layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);

                            tvtotalfinish.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getActuall_collect()));

                            if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("0")) {

                                dialog_paytype.setText("Onlile Payment");
                                tvtotalfinish.setVisibility(View.GONE);
                                pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                //     layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                                layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.BLUE_TXT));
                                tvfinishsubmit.setText("Okay");

                            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("1")) {
                                dialog_paytype.setText("Cash Payment");
                                layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.green));
                                tvfinishsubmit.setText("Collect");


                            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("2")) {
                                dialog_paytype.setText("Wallet Payment");
                                tvtotalfinish.setVisibility(View.GONE);
                                pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                tvfinishsubmit.setText("Okay");
                                tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.BLUE_TXT));
                            }
                            dialogfinish.show();


                            // holder.tvPrices.setText(Html.fromHtml("&#x20B9;"+t));


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
                                        customer_name.setText(artistBookingsList.get(position).getUserName());
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
                                                holder.binding.finishnew.setVisibility(View.GONE);
                                                booking("3", position);
                                                //newBookings.getEarning();
                                            }
                                        });


                                    } else {
                                        //           ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                    }
                                    dialogfinish.dismiss();
                                }
                            });
                        } else {

                            tvtotalfinish = dialogfinish.findViewById(R.id.tvtotalfinish);
                            pay_done_img = dialogfinish.findViewById(R.id.pay_done_img);
                            dialog_paytype = dialogfinish.findViewById(R.id.dialog_paytype);
                            tvfinishsubmit = dialogfinish.findViewById(R.id.tvfinishsubmit);
                            tvfinishcancel = dialogfinish.findViewById(R.id.tvfinishcancel);
                            layfinishbackground = dialogfinish.findViewById(R.id.layfinishbackground);

                            if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("0")) {

                                dialog_paytype.setText("Onlile Payment");
                                //   tvtotalfinish.setVisibility(View.GONE);
                                pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                //     layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.chat_out));
                                layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.BLUE_TXT));
                                tvfinishsubmit.setText("Okay");

                            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("1")) {
                                dialog_paytype.setText("Cash Payment");
                                layfinishbackground.setBackgroundColor(context.getResources().getColor(R.color.green));
                                tvfinishsubmit.setText("Collect");


                            } else if (artistBookingsList.get(position).getPay_type().equalsIgnoreCase("2")) {
                                dialog_paytype.setText("Wallet Payment");
                                //   tvtotalfinish.setVisibility(View.GONE);
                                pay_done_img.setImageResource(R.drawable.ic_payment_done);
                                layfinishbackground.setBackground(context.getResources().getDrawable(R.drawable.online_finish_bg));
                                tvfinishsubmit.setText("Okay");
                                tvfinishsubmit.setTextColor(context.getResources().getColor(R.color.BLUE_TXT));
                            }
                            dialogfinish.show();

                            tvtotalfinish.setText(Html.fromHtml("&#x20B9;" + artistBookingsList.get(position).getActuall_collect()));
                            // holder.tvPrices.setText(Html.fromHtml("&#x20B9;"+t));


                            tvfinishcancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogfinish.dismiss();
                                }
                            });

                            tvfinishsubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogfinish.dismiss();

                                    tvcameraskip = dialogcamera.findViewById(R.id.tvcameraskip);
                                    tvcamera = dialogcamera.findViewById(R.id.tvcamera);
                                    tvupload = dialogcamera.findViewById(R.id.tvupload);
                                    img_upload = dialogcamera.findViewById(R.id.img_upload);
                                    lldauploadImageLayout = dialogcamera.findViewById(R.id.uploadImageLayout);
                                    dialogcamera.show();

                                    tvcameraskip.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String cid = artistBookingsList.get(position).getProduct().get(0).getCategory_id();
                                            paramsBookingOp.put(Consts.CATEGORY_ID, cid);

                                            if (NetworkManager.isConnectToInternet(context)) {
                                                Log.e("service", "SERVICE CAlled 1");
                                                //          context.stopService(mServiceIntent);
                                                booking("3", position);


                                            } else {
                                                ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                            }
                                            dialogcamera.dismiss();
                                        }
                                    });

                                    tvupload.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            newBookings.opencamrea();

                                            //  lldauploadImageLayout.setVisibility(View.GONE);
                                        }
                                    });
                                    tvcamera.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            String cid = artistBookingsList.get(position).getProduct().get(0).getCategory_id();
                                            paramsBookingOp.put(Consts.CATEGORY_ID, cid);


                                            if (img_upload.getVisibility() == View.VISIBLE) {

                                                if (NetworkManager.isConnectToInternet(context)) {
                                                    Log.e("service", "SERVICE CAlled 2");
                                                    //           context.stopService(mServiceIntent);
                                                    bookingupload("3", position);

                                                } else {
                                                    ProjectUtils.showToast(context, context.getResources().getString(R.string.internet_concation));
                                                }
                                            } else {
                                                Toast.makeText(context, "Please Upload Photo", Toast.LENGTH_LONG).show();


                                            }


                                        }
                                    });

                                }
                            });
                        }

                    }
                }
            });


        } else {
            MyViewHolderSection view = (MyViewHolderSection) holderMain;
            view.tvSection.setText(artistBookingsList.get(position).getSection_name());
        }

    }

    private void showBottomDialog(boolean multi_start_preparation) {


        /*new HttpsRequest(Consts.MULTI_REQUEST_FOR_RIDER_API, findriderHashmap, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //    Toast.makeText(context, "shivam1", Toast.LENGTH_SHORT).show();
                if (flag) {
                    Log.e("REQUEST_RIDER", "" + response.toString());
                    try {
                        dialogList = new ArrayList<>();
                        Log.e("data-----", "" + response.getJSONArray("data").toString());
                        dialogList = new Gson().fromJson(response.getJSONArray("data").toString(), new TypeToken<List<RequestRiderDialogModel>>() {
                        }.getType());

                        setAdapter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

        selectedidList = new ArrayList<>();
        setAdapter();

        requestForRiderBinding.requestRiderDialogClose.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //notifyDataSetChanged();
                newBookings.setAdapterAllBookings();
                //context.startActivity(new Intent(context, BaseActivity.class));
            }
        });


        bottomSheetDialog.show();
    }

    private void setAdapter() {

        RequestForRiderDialogAdapter adapter = new RequestForRiderDialogAdapter(context, artistBookingsList, multi_start_preparation, this);
        requestForRiderBinding.riderRequestDialogRv.setLayoutManager(new LinearLayoutManager(context));
        requestForRiderBinding.riderRequestDialogRv.setAdapter(adapter);
    }

    private void updatePickupStatus() {


        new HttpsRequest(Consts.UPDATE_PICKUP_STATUS, preparationParams, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                    Log.e("response_res", "" + response.toString());
                } else {

                }
            }
        });
    }

    private void startPreparation() {

        Log.e("response_res", "preparation called");

        new HttpsRequest(Consts.START_PREPARATION, startdeliveryParams, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    Log.e("rider_order123456", "preparation_response" + response.toString());
                } else {

                }
            }
        });
    }


    public void booking(String req, int pos) {

        paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.USER_ID, artistBookingsList.get(pos).getUser_id());
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    newBookings.getBookingscopy("3");
                    newBookings.getEarning();

                } else {
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
                    // ProjectUtils.showToast(context, msg);
                    newBookings.getBookingscopy("3");


                } else {
                }


            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return artistBookingsList.get(position).isSection() ? VIEW_SECTION : VIEW_ITEM;
        // return position;
    }

    @Override
    public int getItemCount() {
        return artistBookingsList.size();
    }

    @Override
    public void onReasonSelected(int position, String reason) {

    }

    @Override
    public void getOrders(int position, List<String> id) {

        selectedidList = id;


        Log.e("selectedidList", "" + selectedidList.toString());
    }

    @Override
    public void getCheckedItems(int position, List<String> id) {
        ConfirmationselectedidList = id;
        Log.e("confirmation_for_start", "confirmation_for_start 6: "+ConfirmationselectedidList.toString());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterCabBookingsBinding binding;

        public MyViewHolder(AdapterCabBookingsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public void bookingstart(String req, int pos, String datetime) {
        if (multi_start_preparation) {
            paramsBookingOp.put(Consts.BOOKING_ID, start_multi_buffer.toString());
            Log.e("multi_start_preparation", "multiple 1");

        } else if(confirmation_for_start){
            Log.e("confirmation_for_start", "confirmation_for_start 1");

            paramsBookingOp.put(Consts.BOOKING_ID, start_multi_buffer.toString());

        }else {
            Log.e("multi_start_preparation", "multiple 2");

            paramsBookingOp.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        }
        paramsBookingOp.put(Consts.REQUEST, req);
        paramsBookingOp.put(Consts.APPROXTIME, datetime);
        paramsBookingOp.put(Consts.USER_ID, artistBookingsList.get(pos).getUser_id());
        progressDialog.show();
        new HttpsRequest(Consts.BOOKING_OPERATION_API, paramsBookingOp, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    Log.e("confirmation_for_start", "confirmation_for_start 2");

                    //  ProjectUtils.showToast(context, msg);
                    progressDialog.dismiss();
                    if (multi_start_preparation) {
                        Log.e("multi_start_preparation", "multiple 3");

                        selectedidList.clear();
                        selectedidList = new ArrayList<>();
                        ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                        bottomSheetDialog.dismiss();

                    }else if(confirmation_for_start){
                        Log.e("confirmation_for_start", "confirmation_for_start 3");

                        ConfirmationselectedidList.clear();
                        ConfirmationselectedidList = new ArrayList<>();
                        riderRequestConfirmation.dismiss();
                        ((BaseActivity) context).loadHomeFragment(new CabBookingsFragment(), "cab");
                    }
                    Log.e("multi_start_preparation", "multiple 4");

                    newBookings.getBookingscopy("3");


                } else {
                    // ProjectUtils.showToast(context, msg);
                }


            }
        });

    }

    public void autodecline(int pos, String declineby, String reason1) {

        HashMap paramsDecline = new HashMap<>();
        paramsDecline.put(Consts.USER_ID, userDTO.getUser_id());
        paramsDecline.put(Consts.BOOKING_ID, artistBookingsList.get(pos).getId());
        Log.e("declineby1234", "" + declineby);
        // Toast.makeText(context, "decline :---" + declineby, Toast.LENGTH_LONG).show();
        String decl = declineby;
        paramsDecline.put("jjjj", decl);
        paramsDecline.put("decline_by", decl);
        paramsDecline.put("passvalue", "0");
        paramsDecline.put(Consts.DECLINE_REASON, reason1);
        paramsDecline.put("lat", artistBookingsList.get(pos).getC_latitude());
        paramsDecline.put("lang", artistBookingsList.get(pos).getC_longitude());
        paramsDecline.put("cat_id", artistBookingsList.get(pos).getProduct().get(0).getCategory_id());
        paramsDecline.put("sub_id", artistBookingsList.get(pos).getProduct().get(0).getSub_category_id());
        paramsDecline.put("third_id", artistBookingsList.get(pos).getProduct().get(0).getSublevel_category());

        // progressDialog.show();
        new HttpsRequest(Consts.DECLINE_BOOKING_API2, paramsDecline, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //  progressDialog.dismiss();
                //  dialog_book.dismiss();
                if (flag) {

                    Log.e("declineby1234", "" + response.toString());
                    //    ProjectUtils.showToast(context, msg);


                    newBookings.getBookingscopy("3");

                } else {
                    // ProjectUtils.showToast(context, msg);
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

        Log.e("CANCEL_RESPONSE", "" + paramsDecline.toString());


        new HttpsRequest(Consts.DECLINE_BOOKING_ON_BTN_CLICK2, paramsDecline, context).stringPosttwo(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                //Log.e("CANCEL_RESPONSE", "" + flag + " msg " + msg);
                progressDialog.dismiss();
                if (flag) {
                    //  ProjectUtils.showToast(context, msg);

                    Log.e("CANCEL_RESPONSE", "" + flag + " msg " + msg);
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
                        Log.e("CANCEL_RESPONSE_error", "" + e.toString());
                    }


                    newBookings.getBookingscopy("3");

                } else {
                }


            }
        });
    }

    public void getCancellReasons() {

        cancelParms.put(Consts.ARTIST_ID, userDTO.getUser_id());

        cancelParms.put("role", "1");
        Log.e("cancelreason", "params " + cancelParms.toString());

        new HttpsRequest(Consts.GET_CANCEL_REASON_API, cancelParms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

                    Log.e("cancelreason", "" + response.toString());
                    Type getpetDTO = new TypeToken<List<CancelReasonModel>>() {
                    }.getType();
                    try {
                        cancelList = (ArrayList<CancelReasonModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        ReasonAdapter reasonAdapter = new ReasonAdapter(context, cancelList, new OnReasonSelectedListener() {
                            @Override
                            public void onReasonSelected(int position, String reason) {

                                cancelreason = reason;
                                Log.e("cancelreason", "" + cancelreason);
                            }
                        });
                        cancell_reason_list.setAdapter(reasonAdapter);
                        cancell_reason_list.setExpanded(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
