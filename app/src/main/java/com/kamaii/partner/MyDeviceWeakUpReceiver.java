package com.kamaii.partner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.kamaii.partner.service.MyService;

import static android.content.Context.ALARM_SERVICE;

public class MyDeviceWeakUpReceiver extends WakefulBroadcastReceiver {
    int interval=2*60*60*1000;
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, MyService.class);
        Intent receiverIntent = new Intent(context, com.kamaii.partner.MyDeviceWeakUpReceiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 11, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+interval,alarmIntent);
        }
        startWakefulService(context, serviceIntent);
    }
}
