package com.example.seminarhall;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.seminarhall.admin.Admin_Control;
import com.example.seminarhall.UserBookings.MyBookings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;


public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "MessagingService";
    public static final String Channel_1 = "User Notification";
    String default_notification_channel_id = "User Notification";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Map<String, String> map = new HashMap<>();
            map.put("tokenId", user.getUid().trim());
            CollectionReference db = FirebaseFirestore.getInstance().collection("users");
            db.document(user.getUid().trim()).set(map, SetOptions.merge()).addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: ");
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e);
                }
            });
        }

    }

    //If a user is made admin show 1 message or show accepted or rejected

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: ");
        super.onMessageReceived(remoteMessage);
        String status = remoteMessage.getData().get("status");
        if (status.equals("accepted") | status.equals("rejected")) {
            Log.d(TAG, "Status message");
            sendOnChannelStatus(remoteMessage.getData());
        } else if (status.equals("new")) {
            sendOnNewReservation(remoteMessage.getData());
        }
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().get("d"));
    }

    private void sendOnNewReservation(Map<String, String> map) {
        String time = map.get("startTime");
        String startDate = map.get("Date");
        String Username = map.get("userName");
        String title = "New Booking Requested";
        String body = Username + " has requested a new Booking on " + startDate + " starting " + time;

        Intent activityIntent = new Intent(this, Admin_Control.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, "User Notification")
                .setSmallIcon(R.drawable.noti)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body)
                        .setBigContentTitle(title)
                        .setSummaryText("New Booking Request"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);

    }

    private void sendOnChannelStatus(Map<String, String> data) {
        String status = data.get("status");
        int largeIcon;
        String time = data.get("startTime");
        String Date = data.get("Date");
        String Title;
        int color;
        String body = "Your Booking request for " + Date + ", starting: " + time + " has been " + status;
        if (status.equals("accepted")) {
            Log.d(TAG, "sendOnChannelStatus: Accepted");
            Log.d(TAG, "Status: " + status);
            Title = "Your Reservation Request Has been Accepted";
            largeIcon = R.drawable.ic_checkbox;
            color = Color.BLUE;


        } else {
            Log.d(TAG, "sendOnChannelStatus: Rejected");
            Log.d(TAG, "Status: " + status);
            Title = "Your Reservation Request Has been Rejected";
            largeIcon = R.drawable.rejected;
            color = Color.RED;
        }

        Log.d(TAG, "sendOnChannelStatus: ");

        Intent activityIntent = new Intent(this, MyBookings.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, "User Notification")
                .setSmallIcon(largeIcon)
                .setContentTitle(Title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body)
                        .setBigContentTitle(Title)
                        .setSummaryText("Booking Update"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(color)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }

}