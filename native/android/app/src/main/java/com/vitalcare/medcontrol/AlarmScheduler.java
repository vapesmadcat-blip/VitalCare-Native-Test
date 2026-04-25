package com.vitalcare.medcontrol;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmScheduler {
    public static void schedule(Context context, int id, long timeMillis, String med, String dose) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarmId", id);
        intent.putExtra("med", med == null ? "Medicamento" : med);
        intent.putExtra("dose", dose == null ? "" : dose);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent showIntent = new Intent(context, AlarmActivity.class);
        showIntent.putExtra("med", med == null ? "Medicamento" : med);
        showIntent.putExtra("dose", dose == null ? "" : dose);
        showIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent showPendingIntent = PendingIntent.getActivity(
                context,
                id + 100000,
                showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(timeMillis, showPendingIntent);
        alarmManager.setAlarmClock(info, alarmIntent);
    }

    public static void cancel(Context context, int id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) alarmManager.cancel(alarmIntent);
    }
}
