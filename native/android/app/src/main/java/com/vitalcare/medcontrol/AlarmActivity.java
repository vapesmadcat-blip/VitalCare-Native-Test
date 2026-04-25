package com.vitalcare.medcontrol;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AlarmActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        String med = getIntent().getStringExtra("med");
        String dose = getIntent().getStringExtra("dose");
        if (med == null || med.trim().isEmpty()) med = "Medicamento";
        if (dose == null) dose = "";

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(42, 42, 42, 42);
        layout.setBackgroundColor(Color.rgb(7, 17, 31));

        TextView title = new TextView(this);
        title.setText("💊 Hora da dose");
        title.setTextSize(30f);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        TextView msg = new TextView(this);
        msg.setText((med + " " + dose).trim());
        msg.setTextSize(22f);
        msg.setTextColor(Color.WHITE);
        msg.setGravity(Gravity.CENTER);
        msg.setPadding(0, 30, 0, 30);

        Button btn = new Button(this);
        btn.setText("OK, registrar no app");
        btn.setTextSize(18f);
        btn.setOnClickListener(v -> {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.cancelAll();
            finish();
        });

        layout.addView(title);
        layout.addView(msg);
        layout.addView(btn);
        setContentView(layout);
    }
}
