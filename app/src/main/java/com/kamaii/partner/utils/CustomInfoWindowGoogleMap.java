package com.kamaii.partner.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.kamaii.partner.R;
import com.kamaii.partner.ui.models.InfoWindowData;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.custom_info_window, null);
        TextView km_tv = view.findViewById(R.id.km);
        TextView time_tv = view.findViewById(R.id.time);
        try
        {
            InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
            km_tv.setText(infoWindowData.getKm());
            time_tv.setText(infoWindowData.getTime());
        }
        catch (Exception e)
        {
        }
       view.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               return false;
           }
       });
        return view;
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
