package com.kamaii.partner.ui.adapter.newbooking;

import static android.util.Log.e;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.partner.DTO.UserDTO;
import com.kamaii.partner.R;
import com.kamaii.partner.databinding.DialogStartPreparationBinding;
import com.kamaii.partner.databinding.DialogThankyouBinding;
import com.kamaii.partner.databinding.MyOrderNewRvLayoutBinding;
import com.kamaii.partner.databinding.SlotDataLayoutBinding;
import com.kamaii.partner.https.HttpsRequest;
import com.kamaii.partner.interfacess.Consts;
import com.kamaii.partner.interfacess.Helper;
import com.kamaii.partner.interfacess.apiRest;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.SlotDataAdapter;
import com.kamaii.partner.ui.fragment.CabBookingsFragment;
import com.kamaii.partner.ui.fragment.MyOrders;
import com.kamaii.partner.ui.models.SlotDataModel;
import com.kamaii.partner.ui.models.newbooking.NewCartAdapter;
import com.kamaii.partner.ui.models.newbooking.NewOrders;
import com.kamaii.partner.utils.CustomTextViewBold;
import com.ncorti.slidetoact.SlideToActView;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderNewAdapter extends RecyclerView.Adapter<OrderNewAdapter.OrdernewViewHolder> {

    Context context;
    List<NewOrders> orderList;
    ProgressDialog progressDialog;
    HashMap<String, String> selectedidHashmap;
    private UserDTO userDTO;
    Dialog dialogPreparation, orderfinishdialog;
    DialogStartPreparationBinding dialogStartPreparationBinding;
    List<SlotDataModel> slotDataList;
    SlotDataLayoutBinding binding11;
    DialogThankyouBinding dialogFinishBinding;
    private HashMap<String, String> preparationParams = new HashMap<>();
    MediaPlayer mMediaPlayer;
    public OrderNewAdapter(Context context, List<NewOrders> orderList, UserDTO userDTO, String notification_name, String messagebody) {
        this.context = context;
        this.orderList = orderList;
        this.userDTO = userDTO;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        dialogPreparation = new Dialog(context);
        orderfinishdialog = new Dialog(context);
    }

    @NonNull
    @Override
    public OrdernewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyOrderNewRvLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_order_new_rv_layout, parent, false);
        return new OrdernewViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull OrdernewViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (orderList.get(position).getAppFlag().toString().equalsIgnoreCase("1")) {

            dialogStartPreparationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_start_preparation, null, false);
            dialogPreparation.setContentView(dialogStartPreparationBinding.getRoot());


            holder.binding.newOrderFirstCard.setVisibility(View.VISIBLE);
            holder.binding.newOrderRiderCard.setVisibility(View.GONE);
            holder.binding.totalPaymentFirst.setText(Html.fromHtml("&#8377;" + orderList.get(position).getOrderPayment()));
            holder.binding.cartRvFirst.setLayoutManager(new LinearLayoutManager(context));
            NewCartAdapter adapter = new NewCartAdapter(context, orderList.get(position).getProductsArray());
            holder.binding.cartRvFirst.setAdapter(adapter);
            holder.binding.newOrderStartPreparation.setVisibility(View.VISIBLE);
            holder.binding.newOrderRequestForRider.setVisibility(View.GONE);
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.green_border_bg));

            holder.binding.newOrderStartPreparation.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                    getStartPreparation(orderList.get(position).getOrderId(), "2", position);

                }
            });

            holder.binding.infoFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSlotDetails(position);
                }
            });

            if (orderList.get(position).getEnd_time_tracker().equalsIgnoreCase("0")) {
                holder.binding.counter.setVisibility(View.VISIBLE);

                Log.e("endtime", "" + orderList.get(position).getEndTime());
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                e("tracker", "1");
                Date date = null;
                try {
                    e("tracker", "2");

                    //   date = format.parse("2022-02-12T18:00:00");
                    date = format.parse(orderList.get(position).getEndTime());
                    holder.binding.counter.setDate(date);//countdown starts
                    e("tracker", "3");


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                holder.binding.counter.setIsShowingTextDesc(true);
                holder.binding.counter.setTextSize(35);
                holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                holder.binding.counter.setTextColor(context.getResources().getColor(R.color.green));
                e("tracker", "4");

                holder.binding.counter.setListener(new Counter.Listener() {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        e("TAG", "onTick: Counter - " + millisUntilFinished);

                        if (millisUntilFinished == 0) {
                            holder.binding.counter.setVisibility(View.GONE);
                            holder.binding.preparationTimeFirst.setVisibility(View.VISIBLE);
                        }
                        e("tracker", "5");

                    }

                    @Override
                    public void onTick(long days, long hours, long minutes, long seconds) {
                        //     Log.e("tracker", "6");

               /*     Log.e("TAG", "onTick" + days + "D " +
                            hours + "H " +
                            minutes + "M " +
                            seconds + "S ");*/

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
                        //      Log.e("tracker", "7" + newtime);

                        holder.binding.preparationTimeFirst.setText(newtime);

                    }
                });

            }

            else {
                holder.binding.counter.setVisibility(View.GONE);
                holder.binding.orderDelayTimer.setVisibility(View.VISIBLE);
               // holder.binding.orderDelayTimer.setText(orderList.get(position).getEnd_time_diff());

                int stoppedMilliseconds = 0;

                String chronoText = orderList.get(position).getEnd_time_diff();
                String array[] = chronoText.split(":");
                if (array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                            + Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                            + Integer.parseInt(array[1]) * 60 * 1000
                            + Integer.parseInt(array[2]) * 1000;
                }

                holder.binding.orderDelayTimer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                holder.binding.orderDelayTimer.start();
                holder.binding.orderStatusFirst.setText("Preparing order delayed by");
                holder.binding.preparationTimeFirst.setVisibility(View.VISIBLE);
                holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.red_border_bg));
                holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_red_bg));
                holder.binding.infoFirst.setImageTintList(context.getResources().getColorStateList(R.color.red));

            }


        }

        else if (orderList.get(position).getAppFlag().toString().equalsIgnoreCase("2")) {

            dialogStartPreparationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_start_preparation, null, false);
            dialogPreparation.setContentView(dialogStartPreparationBinding.getRoot());

            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.pink_border_bg));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_pink_bg));

            holder.binding.newOrderFirstCard.setVisibility(View.VISIBLE);
            holder.binding.newOrderStartPreparation.setVisibility(View.GONE);
            holder.binding.newOrderRiderCard.setVisibility(View.GONE);
            holder.binding.newOrderRequestForRider.setVisibility(View.VISIBLE);
            holder.binding.infoFirst.setImageTintList(context.getResources().getColorStateList(R.color.pink_color));
            holder.binding.totalPaymentFirst.setText(Html.fromHtml("&#8377;" + orderList.get(position).getOrderPayment()));
            holder.binding.cartRvFirst.setLayoutManager(new LinearLayoutManager(context));
            NewCartAdapter adapter = new NewCartAdapter(context, orderList.get(position).getProductsArray());
            holder.binding.cartRvFirst.setAdapter(adapter);
            e("PREPARATION_TIME", " end time :-- " + orderList.get(position).getEndTime());

            holder.binding.infoFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSlotDetails(position);
                }
            });
            holder.binding.newOrderRequestForRider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                    getRequestForrider(orderList.get(position).getOrderId(), position);
                }
            });

            if (orderList.get(position).getEnd_time_tracker().equalsIgnoreCase("0")) {
                holder.binding.counter.setVisibility(View.VISIBLE);

                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                e("tracker", "1");
                Date date = null;
                try {
                    e("tracker", "2");

                    date = format.parse(orderList.get(position).getEndTime());
                    holder.binding.counter.setDate(date);//countdown starts
                    e("tracker", "3");


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

                holder.binding.counter.setIsShowingTextDesc(true);
                holder.binding.counter.setTextSize(35);
                holder.binding.counter.setMaxTimeUnit(TimeUnits.DAY);
                holder.binding.counter.setTextColor(context.getResources().getColor(R.color.pink_color));
                e("tracker", "4");

                holder.binding.counter.setListener(new Counter.Listener() {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        //     Log.e("TAG", "onTick: Counter - " + millisUntilFinished);
                        //    Log.e("tracker", "5");

                    }

                    @Override
                    public void onTick(long days, long hours, long minutes, long seconds) {
                        //  Log.e("tracker", "6");

//                 //   Log.e("TAG", "onTick" + days + "D " +
//                            hours + "H " +
//                            minutes + "M " +
//                            seconds + "S ");

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
                        //  Log.e("tracker", "7" + newtime);

                        holder.binding.preparationTimeFirst.setText(newtime);

                    }
                });

            }

            else {

                holder.binding.counter.setVisibility(View.GONE);
                holder.binding.orderDelayTimer.setVisibility(View.VISIBLE);
                int stoppedMilliseconds = 0;

                String chronoText = orderList.get(position).getEnd_time_diff();
                String array[] = chronoText.split(":");
                if (array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                            + Integer.parseInt(array[1]) * 1000;
                } else if (array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                            + Integer.parseInt(array[1]) * 60 * 1000
                            + Integer.parseInt(array[2]) * 1000;
                }

                holder.binding.orderDelayTimer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                holder.binding.orderDelayTimer.start();

                holder.binding.orderStatusFirst.setText("Preparing order");
                holder.binding.preparationTimeFirst.setVisibility(View.VISIBLE);
                holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.red_border_bg));
                holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_red_bg));

                holder.binding.infoFirst.setImageTintList(context.getResources().getColorStateList(R.color.red));

            }

        } else if (orderList.get(position).getAppFlag().toString().equalsIgnoreCase("3")) {
            holder.binding.covidTxt.setText(orderList.get(position).getCovid_msg());
            holder.binding.newOrderFirstCard.setVisibility(View.GONE);
            holder.binding.newOrderRiderCard.setVisibility(View.VISIBLE);

            holder.binding.orderRiderRv.setLayoutManager(new LinearLayoutManager(context));
            AdapterNewOrderRiderData newOrderRiderData = new AdapterNewOrderRiderData(context, orderList.get(position).getRiderArray());
            holder.binding.orderRiderRv.setAdapter(newOrderRiderData);


        } else if (orderList.get(position).getAppFlag().toString().equalsIgnoreCase("4")) {

            holder.binding.clock123First.setImageResource(R.drawable.ic_waiting_timer);
            holder.binding.orderStatusFirst.setText("Waiting for rider");
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_yellow_bg));
            holder.binding.newOrderFirstCard.setVisibility(View.VISIBLE);
            holder.binding.newOrderStartPreparation.setVisibility(View.GONE);
            holder.binding.newOrderRiderCard.setVisibility(View.GONE);
            holder.binding.newOrderRequestForRider.setVisibility(View.GONE);
            holder.binding.newOrderCancelRequestForRider.setVisibility(View.VISIBLE);

            holder.binding.newOrderCancelRequestForRider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                    cancelRiderRequest(position);
                }
            });
            holder.binding.infoFirst.setVisibility(View.GONE);
            holder.binding.counter.setVisibility(View.GONE);
            holder.binding.totalPaymentFirst.setText(Html.fromHtml("&#8377;" + orderList.get(position).getOrderPayment()));
            holder.binding.cartRvFirst.setLayoutManager(new LinearLayoutManager(context));
            NewCartAdapter adapter = new NewCartAdapter(context, orderList.get(position).getProductsArray());
            holder.binding.cartRvFirst.setAdapter(adapter);
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.yello_border_bg));


        } else if (orderList.get(position).getAppFlag().toString().equalsIgnoreCase("5")) {

            dialogFinishBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_thankyou, null, false);
            orderfinishdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            orderfinishdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            orderfinishdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            orderfinishdialog.setContentView(dialogFinishBinding.getRoot());
            orderfinishdialog.setCancelable(true);

            orderfinishdialog.setContentView(dialogFinishBinding.getRoot());
            dialogFinishBinding.tvtotalfinish.setText(Html.fromHtml("&#8377;" + orderList.get(position).getOrderPayment()));
            dialogFinishBinding.totalPaymentFirst.setText(Html.fromHtml("&#8377;" + orderList.get(position).getOrderPayment()));
            dialogFinishBinding.riderName.setText(orderList.get(position).getRider_name());
            dialogFinishBinding.cartRvFirst.setLayoutManager(new LinearLayoutManager(context));
            NewCartAdapter adapter = new NewCartAdapter(context, orderList.get(position).getProductsArray());
            dialogFinishBinding.cartRvFirst.setAdapter(adapter);


            dialogFinishBinding.okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderfinishdialog.dismiss();
                    updatePickupStatus(position);
                }
            });

            orderfinishdialog.show();
        } else {
            holder.binding.newOrderFirstCard.setVisibility(View.GONE);
            holder.binding.newOrderRiderCard.setVisibility(View.GONE);

        }

    }

    private void cancelRiderRequest(int position) {

        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL3)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiRest apiRest = retrofit.create(apiRest.class);
        Call<ResponseBody> call = apiRest.cancel_rider_all_rider_request(userDTO.getUser_id(), orderList.get(position).getOrderId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {

                        ResponseBody responseBody = response.body();
                        String res = responseBody.string();

                        Log.e("cancel_rider_ res", "" + res);
                        JSONObject object = new JSONObject(res);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {
                            progressDialog.dismiss();
                            ((BaseActivity) context).loadHomeFragment(new MyOrders(), "cab");

                        }
                        else {
                            progressDialog.dismiss();
                        }
                    } else {
                        Log.e("error", "" + response.errorBody().string());
                        progressDialog.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    private void updatePickupStatus(int pos) {

        preparationParams.put(Consts.BOOKING_ID, orderList.get(pos).getOrderId());

        new HttpsRequest(Consts.UPDATE_PICKUP_STATUS, preparationParams, context).stringPosttwo(Consts.UPDATE_PICKUP_STATUS, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                if (flag) {
                    orderfinishdialog.dismiss();
                    ((BaseActivity) context).loadHomeFragment(new MyOrders(), "cab");
                    Log.e("response_res", "" + response.toString());
                } else {

                }
            }
        });
    }

    private void getStartPreparation(String b_id, String req, int pos) {

        e("getStartPreparatparams", " b_id " + b_id + " req: " + req);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRest api = retrofit.create(apiRest.class);
        Call<ResponseBody> callone = api.booking_operation(orderList.get(pos).getOrderId(), req, userDTO.getUser_id());
        callone.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();

                        String s = responseBody.string();

                        e("getOrders", "" + s);
                        JSONObject object = new JSONObject(s);

                        String message = object.getString("message");
                        int sstatus = object.getInt("status");

                        if (sstatus == 1) {

                            try {
                                dialogPreparation.dismiss();

                                ((BaseActivity) context).loadHomeFragment(new MyOrders(), "cab");

//                                orderArrayList = new ArrayList<>();
//
//                                Type getpetDTO = new TypeToken<List<NewOrders>>() {
//                                }.getType();
//                                orderArrayList = (ArrayList<NewOrders>)new Gson().fromJson(object.getJSONArray("data").toString(), getpetDTO);
//
//                                Log.e("orderArrayList", " 2 " + orderArrayList.size());
//
//
//                                setOrderAdapter();
//                                showData();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            progressDialog.dismiss();
                            dialogPreparation.dismiss();

                            // showData();
                            // binding.idPBLoading.setVisibility(View.GONE);
                        }


                    } else {
                        progressDialog.dismiss();
                        dialogPreparation.dismiss();


                       /* Toast.makeText(getActivity(), "Please try again later",
                                LENGTH_LONG).show();*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();

              /*  Toast.makeText(getActivity(), "Try again. Server is not responding",
                        LENGTH_LONG).show();*/


            }
        });


    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(Context c, int rid) {
        stop();

        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
    }

    private void getRequestForrider(String b_id, int pos) {

        progressDialog.show();
        selectedidHashmap = new HashMap<>();
        selectedidHashmap.put(Consts.ARTIST_ID, userDTO.getUser_id());
        selectedidHashmap.put(Consts.BOOKING_ID, orderList.get(pos).getOrderId());

        new HttpsRequest(Consts.REQUEST_FOR_RIDER_MULTIPLE_API, selectedidHashmap, context).stringPosttwo("REQUESTFORRIDER", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                e("REQUEST_FOR_RIDER_API", "" + response.toString());
                if (flag) {
                    play(context,R.raw.request_for_rider);
                    ((BaseActivity) context).loadHomeFragment(new MyOrders(), "cab");

                } else {
                    ((BaseActivity) context).loadHomeFragment(new MyOrders(), "cab");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public void getSlotDetails(int pos) {

        progressDialog.show();
        HashMap<String, String> slotMap = new HashMap<>();
        slotMap.put("order_ids", orderList.get(pos).getOrderId());
        //  slotMap.put("Order_id", artistBookingsList.get(artistBookingsList.size() - 1).getNew_order_slot_key());
        e("orderSummeryCount", " clicked " + slotMap.toString());
        new HttpsRequest(Consts.GET_SLOT_DETAILS_API, slotMap, context).stringPostthree("Tag", new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                progressDialog.dismiss();
                if (flag) {
                    e("SLOT_DATA", "" + response.toString());

                    try {

                        Type getType = new TypeToken<List<SlotDataModel>>() {
                        }.getType();

                        slotDataList = new Gson().fromJson(response.getJSONArray("data").toString(), getType);

                        e("slotDataList", "" + slotDataList.size());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Dialog alertDialog = new Dialog(context);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    binding11 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slot_data_layout, null, false);
                    alertDialog.setContentView(binding11.getRoot());
//                            alertDialog.setCancelable(false);

                    CustomTextViewBold dialog_title = alertDialog.findViewById(R.id.slot_title);
                    ImageView close_slot_dialog = alertDialog.findViewById(R.id.close_slot_dialog);

                    dialog_title.setText("Order Details");
                    close_slot_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    RecyclerView slotdata_recyclerview = alertDialog.findViewById(R.id.slotdata_recyclerview);
                    SlotDataAdapter slotDataAdapter = new SlotDataAdapter(context, slotDataList);
                    slotdata_recyclerview.setLayoutManager(new LinearLayoutManager(context));
                    slotdata_recyclerview.setAdapter(slotDataAdapter);

                    alertDialog.show();
                }
            }
        });

    }

    private void showPreparationDialog(int pos, String flag) {


        dialogStartPreparationBinding.cartPrepqration.setLayoutManager(new LinearLayoutManager(context));
        AdapterPreparationCart cart = new AdapterPreparationCart(context, orderList.get(pos).getProductsArray());
        dialogStartPreparationBinding.cartPrepqration.setAdapter(cart);
        if (flag.equalsIgnoreCase("2")) {
            dialogStartPreparationBinding.orderStatusFirst.setText("Request For Rider");
            dialogStartPreparationBinding.timerRelativeFirst.setBackground(context.getResources().getDrawable(R.color.light_pink_bg));

            dialogStartPreparationBinding.preparationDialogYes.setBackgroundTintList(context.getResources().getColorStateList(R.color.pink_color));
            //dialogStartPreparationBinding.preparationDialogCancel.setBackground(context.getResources().getDrawable(R.drawable.));
        }
        dialogStartPreparationBinding.preparationDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPreparation.dismiss();
            }
        });
        dialogStartPreparationBinding.preparationDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartPreparation("", "2", pos);

            }
        });

        Log.e("endtime", "" + orderList.get(pos).getEndTime());
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        e("tracker", "1");
        Date date = null;
        try {
            e("tracker", "2");

            //   date = format.parse("2022-02-12T18:00:00");
            date = format.parse(orderList.get(pos).getEndTime());
            dialogStartPreparationBinding.dialogCounter.setDate(date);//countdown starts
            e("tracker", "3");


        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

        dialogStartPreparationBinding.dialogCounter.setIsShowingTextDesc(true);
        dialogStartPreparationBinding.dialogCounter.setTextSize(35);
        dialogStartPreparationBinding.dialogCounter.setMaxTimeUnit(TimeUnits.DAY);
        dialogStartPreparationBinding.dialogCounter.setTextColor(context.getResources().getColor(R.color.green));
        e("tracker", "4");

        dialogStartPreparationBinding.dialogCounter.setListener(new Counter.Listener() {
            @Override
            public void onTick(long millisUntilFinished) {
                e("TAG", "onTick: Counter - " + millisUntilFinished);

                if (millisUntilFinished == 0) {
                    dialogStartPreparationBinding.dialogCounter.setVisibility(View.GONE);
                    dialogStartPreparationBinding.preparationTimeFirst.setVisibility(View.VISIBLE);
                }
                e("tracker", "5");

            }

            @Override
            public void onTick(long days, long hours, long minutes, long seconds) {
                //     Log.e("tracker", "6");

               /*     Log.e("TAG", "onTick" + days + "D " +
                            hours + "H " +
                            minutes + "M " +
                            seconds + "S ");*/

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
                //      Log.e("tracker", "7" + newtime);

                //   holder.binding.preparationTimeFirst.setText(newtime);

            }
        });

        dialogPreparation.show();
    }

    class OrdernewViewHolder extends RecyclerView.ViewHolder {
        MyOrderNewRvLayoutBinding binding;

        public OrdernewViewHolder(@NonNull MyOrderNewRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
