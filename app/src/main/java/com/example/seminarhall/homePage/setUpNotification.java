package com.example.seminarhall.homePage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import static androidx.core.content.ContextCompat.getSystemService;

public class setUpNotification extends Application {
    private static final String TAG = "setUpNotification";
    FirebaseUser user;
    private String userNotification="User Notification";
    Context context;
//    NotificationManager manager;

    setUpNotification(Context context,FirebaseUser user) {
        this.context=context;
        if (user != null) {
            this.user = user;
        }
//        this.manager=m;
        setUPgeneralNotification();
    }

    private void setUPgeneralNotification()
    {
        if (!isNotificationChannelEnabled(context,userNotification)) {
            createNotificationChannelUser();
        }
        setUserNotification();
    }

    private void setUserNotification()
    {
        Log.d(TAG, "setUserNotification: ");
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "SuccessFull";
                        Log.d(TAG, "onComplete: Successful ");
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                            Log.d(TAG, "onComplete: Failed");
                        }
                    }
                });
    }


    private void createNotificationChannelUser() {
        Log.d(TAG, "createNotificationChannelUser: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    userNotification,
                    "User Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Personal Channel");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel1);

        }
    }

    private boolean isNotificationChannelEnabled(Context context,@Nullable String channelId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!TextUtils.isEmpty(channelId)) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = manager.getNotificationChannel(channelId);
                if (channel == null) {
                    return false;
                }
                return channel.getImportance() != NotificationManager.IMPORTANCE_NONE;
            }
            return false;
        } else {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
    }
}
