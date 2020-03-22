package com.example.seminarhall.NotificationGroup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationClass extends Application {
    //Class makes a defualt notification channel for every user
    public static final String CHANNEL_1_ID = "User Notification";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Default Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel1);
        }
    }
}