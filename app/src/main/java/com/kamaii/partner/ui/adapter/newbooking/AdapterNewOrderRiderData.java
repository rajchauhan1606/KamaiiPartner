package com.kamaii.partner.ui.adapter.newbooking;

import static android.util.Log.e;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.partner.R;
import com.kamaii.partner.databinding.NewOrderRiderLayoutBinding;
import com.kamaii.partner.ui.activity.BaseActivity;
import com.kamaii.partner.ui.adapter.RiderMultiIdOrderAdapter;
import com.kamaii.partner.ui.models.newbooking.NewCartAdapter;
import com.kamaii.partner.ui.models.newbooking.RiderArray;
import com.tsuryo.androidcountdown.Counter;
import com.tsuryo.androidcountdown.TimeUnits;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterNewOrderRiderData extends RecyclerView.Adapter<AdapterNewOrderRiderData.RiderDataViewHolder> {

    Context context;
    List<RiderArray> arrayList;
  //  String endtime="";
    public AdapterNewOrderRiderData(Context context, List<RiderArray> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
//        this.endtime = endtime;
    }

    @NonNull
    @Override
    public RiderDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewOrderRiderLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.new_order_rider_layout,parent,false);
        return new RiderDataViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RiderDataViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.newOrderRiderName.setText(arrayList.get(position).getRiderName());


        e("PREPARATION_TIME", " end time :-- " +arrayList.get(position).getEndTime());

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        e("tracker", "1");
        Date date = null;
        try {
            e("tracker", "2");

            date = format.parse(arrayList.get(position).getEndTime());
            holder.binding.riderCounter.setDate(date);//countdown starts
            e("tracker", "3");


        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.binding.counter.setDate(artistBookingsList.get(position).getPreparation_time()); //countdown starts

        holder.binding.newRiderCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + arrayList.get(position).getRiderMobileNo()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((BaseActivity)context),new String[]{Manifest.permission.CALL_PHONE},100);
                    return;
                }
                context.startActivity(callIntent);

            }
        });
        holder.binding.riderCounter.setIsShowingTextDesc(true);
        holder.binding.riderCounter.setTextSize(35);
        holder.binding.riderCounter.setMaxTimeUnit(TimeUnits.DAY);
        holder.binding.riderCounter.setTextColor(context.getResources().getColor(R.color.pink_color));
        e("tracker", "4");

        holder.binding.riderCounter.setListener(new Counter.Listener() {
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

              //  holder.binding.riderCounter.setText(newtime);

            }
        });
        
        //holder.binding.orderId.setText(arrayList.get(position).get());
        holder.binding.riderOrderRv.setLayoutManager(new LinearLayoutManager(context));
        RiderMultiIdOrderAdapter cartAdapter = new RiderMultiIdOrderAdapter(context,arrayList.get(position).getOrders());
        holder.binding.riderOrderRv.setAdapter(cartAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class RiderDataViewHolder extends RecyclerView.ViewHolder {
        NewOrderRiderLayoutBinding binding;
        public RiderDataViewHolder(@NonNull NewOrderRiderLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
