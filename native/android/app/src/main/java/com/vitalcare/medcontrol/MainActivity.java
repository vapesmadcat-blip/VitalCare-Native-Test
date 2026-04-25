package com.vitalcare.medcontrol;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private static final int REQ_POST_NOTIFICATIONS = 9901;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNotificationPermissionIfNeeded();

        WebView webView = getBridge().getWebView();
        webView.addJavascriptInterface(new JSBridge(this), "Android");
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_POST_NOTIFICATIONS);
            }
        }
    }

    public static class JSBridge {
        private final MainActivity activity;

        JSBridge(MainActivity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void requestNotificationPermission() {
            activity.runOnUiThread(activity::requestNotificationPermissionIfNeeded);
        }

        @JavascriptInterface
        public void scheduleAlarm(String idText, String timeMillisText, String med, String dose) {
            try {
                int id = Integer.parseInt(idText);
                long timeMillis = Long.parseLong(timeMillisText);
                AlarmScheduler.schedule(activity.getApplicationContext(), id, timeMillis, med, dose);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void cancelAlarm(String idText) {
            try {
                int id = Integer.parseInt(idText);
                AlarmScheduler.cancel(activity.getApplicationContext(), id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
