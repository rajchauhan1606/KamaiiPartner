package com.kamaii.partner.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.maps.model.LatLng;
import com.kamaii.partner.R;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.atan;

public class MapUtils
{

    public static ArrayList<LatLng> getListOfLocations()
    {
        ArrayList<LatLng> locationList =new ArrayList<LatLng>();
        locationList.add(new LatLng(22.291542, 70.801882));
        locationList.add(new LatLng(22.304967, 70.803772));
        locationList.add(new LatLng(22.294754, 70.791120));
        locationList.add(new LatLng(22.282312, 70.799111));
        locationList.add(new LatLng(22.244549, 70.799808));
        locationList.add(new LatLng(22.274052, 70.756964));
        locationList.add(new LatLng(22.256770, 70.693912));
        locationList.add(new LatLng(22.234540, 70.651931));
        locationList.add(new LatLng(22.192126, 70.656842));
        locationList.add(new LatLng(22.102387, 70.628765));
      //  locationList.add(new LatLng(21.964306, 70.664006));
        return locationList;
    }
    public static ArrayList<LatLng> getPinListOfLocations(double sourceLatitude, double sourceLongitude, double destinationLatitude, double destinationLongitude)
    {
        ArrayList<LatLng> locationList =new ArrayList<LatLng>();
        locationList.add(new LatLng(sourceLatitude, sourceLongitude));
        locationList.add(new LatLng(destinationLatitude, destinationLongitude));

        //  locationList.add(new LatLng(21.964306, 70.664006));
        return locationList;
    }
    public static Bitmap getCarBitmap(Context context, int imageType)
    {
        //1= mini ,2=sedan,3=suv
        Bitmap bitmap=null;
        if(imageType==1)
        {
             bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_mini);
        }
        else if(imageType==2)
        {
             bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_sedan);
        }
        else if(imageType==3)
        {
             bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_suv);
        }
        else if(imageType==4)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_rikshaw);
        }
        else if(imageType==5)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.marker_bike);
        }
        else if(imageType==6)
        {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.other);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.other);
        }
        return Bitmap.createScaledBitmap(bitmap, 85, 85, false);
    }
    public static Bitmap getOriginDestinationMarkerBitmap()
    {
        int height = 20;
        int width = 20;
        Bitmap bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(width, height, 100, paint);
        return bitmap;
    }

    public static float getRotation(LatLng start, LatLng end)
    {
        Double latDifference = abs(start.latitude - end.latitude);
        Double lngDifference = abs(start.longitude - end.longitude);
        float rotation = -1F;
        if(start.latitude < end.latitude && start.longitude < end.longitude)
        {
            rotation = (float) Math.toDegrees(atan(lngDifference / latDifference));
        }
        else if(start.latitude >= end.latitude && start.longitude < end.longitude)
        {
            rotation = (float) (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90);
        }
        else if(start.latitude >= end.latitude && start.longitude >= end.longitude)
        {
            rotation = (float) (Math.toDegrees(atan(lngDifference / latDifference)) + 180);
        }
        else if(start.latitude < end.latitude && start.longitude >= end.longitude)
        {
            rotation = (float) (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270);
        }

        return rotation;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
