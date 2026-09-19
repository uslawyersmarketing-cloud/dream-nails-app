package com.dreamnails.salon;

import android.app.*;
import android.content.*;
import android.os.Build;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReminderReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context c, Intent i) {
        String channel="dream_nails_reminders";
        NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=26) nm.createNotificationChannel(new NotificationChannel(channel,"Appointment reminders",NotificationManager.IMPORTANCE_DEFAULT));
        String customer=i.getStringExtra("customer"); String service=i.getStringExtra("service"); long t=i.getLongExtra("time",0);
        String when=new SimpleDateFormat("dd MMM, h:mm a",Locale.getDefault()).format(new Date(t));
        Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(c,channel):new Notification.Builder(c);
        b.setSmallIcon(android.R.drawable.ic_popup_reminder).setContentTitle("Dream Nails appointment tomorrow")
         .setContentText(customer+" • "+service+" • "+when).setAutoCancel(true);
        nm.notify((int)(System.currentTimeMillis()%100000),b.build());
    }
}
