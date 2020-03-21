package com.example.seminarhall.NotificationGroup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("toastMessage");
        Log.d(TAG, "onReceive: "+message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
