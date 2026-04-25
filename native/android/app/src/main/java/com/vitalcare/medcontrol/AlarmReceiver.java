package com.vitalcare.medcontrol;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "vitalcare_alarm_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String med = intent.getStringExtra("med");
        String dose = intent.getStringExtra("dose");
        if (med == null) med = "Medicamento";
        if (dose == null) dose = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarmes VitalCare", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null);
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }

        Intent fullIntent = new Intent(context, AlarmActivity.class);
        fullIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fullIntent.putExtra("med", med);
        fullIntent.putExtra("dose", dose);

        PendingIntent fullPi = PendingIntent.getActivity(
                context,
                (int)(System.currentTimeMillis() % 100000),
                fullIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("💊 Hora da dose")
                .setContentText(med + " " + dose)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVibrate(new long[]{0,1000,500,1000,500,1000})
                .setFullScreenIntent(fullPi, true)
                .setContentIntent(fullPi)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < 33 || ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(context).notify((int)(System.currentTimeMillis() % 100000), builder.build());
        }

        context.startActivity(fullIntent);
    }
}
