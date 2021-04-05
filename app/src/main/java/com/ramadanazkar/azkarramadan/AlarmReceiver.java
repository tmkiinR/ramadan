package com.ramadanazkar.azkarramadan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService;


/**
 * Created by IronCodes on 11.04.2019.
 * Website : http://ironcodes.tech/
 * "All rights reserved ©2014-©2019"
 * Support & Emails : dev@ironcodes.tech , ironcodesdev@gmail.com
 */


public class AlarmReceiver extends BroadcastReceiver/*WakefulBroadcastReceiver*/ {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SchedulingServiceToast.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context, int minutes) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() +
                1000 * 60 * minutes, pi);

    }

}
