package com.example.galaxybowlingcentera;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.galaxybowlingcentera.Activityk.KezdolapActivity;
import com.example.galaxybowlingcentera.Activityk.ProfilActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.TimeZone;

public class NotificationHandler {
    private static final String CHANNEL_ID = "bowling_notification_channel";

    private NotificationManager mManager;
    private Context mContext;

    private FirebaseUser felhasznalo = FirebaseAuth.getInstance().getCurrentUser();


    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Bowling Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.setLightColor(Color.WHITE);
        channel.setDescription("Értesítés a Galaxy Bowling Center-től.");
        this.mManager.createNotificationChannel(channel);
    }

    public void sendNotification(String uzenet) {
        Intent intent;
        if (felhasznalo.isAnonymous()) {
            intent = new Intent(mContext, KezdolapActivity.class);
        } else {
            intent = new Intent(mContext, ProfilActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Galaxy Bowling Center")
                .setContentText(uzenet)
                .setSmallIcon(R.drawable.palyafoglalas_icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(uzenet))
                .setContentIntent(pendingIntent);

        this.mManager.notify(0, builder.build());
    }

    public void sendscheduleNotificationNap(String uzenet, int foglalasEv, int foglalasHonap, int foglalasNap) {
        Calendar foglalasCalendar = Calendar.getInstance();
        foglalasCalendar.set(foglalasEv, foglalasHonap, foglalasNap);
        foglalasCalendar.add(Calendar.DAY_OF_MONTH, -1);
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Budapest");
        Calendar ma = Calendar.getInstance(timeZone);
        long kesleltetes = foglalasCalendar.getTimeInMillis()-ma.getTimeInMillis();

//        Log.d(LOG_TAG,kesleltetes+" "+ma.getTimeInMillis()+" "+foglalasCalendar.getTimeInMillis()
//                +" "+ma.get(Calendar.HOUR_OF_DAY)+":"+ma.get(Calendar.MINUTE)
//                +" "+foglalasCalendar.get(Calendar.HOUR_OF_DAY)+":"+foglalasCalendar.get(Calendar.MINUTE));

        if (kesleltetes < 0) {
            kesleltetes = 0;
        }
        sendScheduledNotification(uzenet, 1, kesleltetes);
    }

    public void sendScheduledNotificationOra(String uzenet, int foglalasEv, int foglalasHonap, int foglalasNap, int foglalasOra) {
        Calendar foglalasCalendar = Calendar.getInstance();
        foglalasCalendar.set(foglalasEv, foglalasHonap, foglalasNap);
        foglalasCalendar.set(Calendar.HOUR_OF_DAY, foglalasOra+1);
        foglalasCalendar.set(Calendar.MINUTE, 0);
        foglalasCalendar.set(Calendar.SECOND, 0);
        foglalasCalendar.add(Calendar.HOUR_OF_DAY, -1);
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Budapest");
        Calendar ma = Calendar.getInstance(timeZone);
        ma.set(Calendar.MINUTE, 0);
        ma.set(Calendar.SECOND, 0);
        long kesleltetes = foglalasCalendar.getTimeInMillis()-ma.getTimeInMillis();

//        Log.d(LOG_TAG,kesleltetes+" "+ma.getTimeInMillis()+" "+foglalasCalendar.getTimeInMillis()
//                +" "+foglalasOra
//                +" "+ma.get(Calendar.HOUR_OF_DAY)+":"+ma.get(Calendar.MINUTE)
//                +" "+foglalasCalendar.get(Calendar.HOUR_OF_DAY)+":"+foglalasCalendar.get(Calendar.MINUTE));

        if (kesleltetes < 0) {
            kesleltetes = 0;
        }
        sendScheduledNotification(uzenet, 2, kesleltetes);
    }

    private void sendScheduledNotification(String uzenet, int notificationId, long kesleltetes) {
        Intent intent;
        if (felhasznalo.isAnonymous()) {
            intent = new Intent(mContext, KezdolapActivity.class);
        } else {
            intent = new Intent(mContext, ProfilActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, kesleltetes, pendingIntent);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Galaxy Bowling Center")
                .setContentText(uzenet)
                .setSmallIcon(R.drawable.palyafoglalas_icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(uzenet))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        this.mManager.notify(notificationId, builder.build());
    }
}
