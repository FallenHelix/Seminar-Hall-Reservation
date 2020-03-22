package com.example.seminarhall;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
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
//        var payload = {
//                data:{
//            d:"Payload Loaded",
//                    status:"new",
//                    "startTime":Time,
//                    "Date": Date,
//                    "userName:":userName
//        }};

        String time = map.get("startTime");
        String startDate = map.get("Date");
        String Username = map.get("userName");
        String title = "New Booking Requested";
        String body= Username+" has requested a new Booking on "+startDate+ " starting "+time;

        Notification notification = new NotificationCompat.Builder(this, "User Notification")
                .setSmallIcon(R.drawable.ic_bug)
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
            Log.d(TAG, "Status: "+status);
            Title = "Your Reservation Request Has been Accepted";
            largeIcon = R.drawable.ic_checkbox;
            color = Color.BLUE;


        } else {
            Log.d(TAG, "sendOnChannelStatus: Rejected");
            Log.d(TAG, "Status: "+status);
            Title = "Your Reservation Request Has been Rejected";
            largeIcon = R.drawable.rejected;
            color = Color.RED;
        }

        Log.d(TAG, "sendOnChannelStatus: ");
//        Notification notification = new NotificationCompat.Builder(this, Channel_1)
//                .setSmallIcon(largeIcon)
//                .setContentTitle(Title)
//                .setContentText(body)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .setColor(color)
//                .setAutoCancel(true)
//                .setOnlyAlertOnce(true)
//                .build();
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
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
    }

    private void showUserNotification(String title, String Body) {
        Log.d(TAG, "showUserNotification: ");
        Notification notification = new NotificationCompat.Builder(this, Channel_1)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(Body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notification);
        Log.d(TAG, "showUserNotification: End");
    }

//    public void showNotification(String title, String Body) {
//        Log.d(TAG, "showNotification: ");
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
//                "My Notification")
//                .setContentTitle(title).setSmallIcon(R.drawable.ic_checkbox)
//                .setAutoCancel(true)
//                .setContentText(Body+"Show notification");
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(999,builder.build());
//    }

    public void sendOnChannel1(String title, String message) {

        Log.d(TAG, "sendOnChannel1: ");
//        Intent activityIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this,
//                0, activityIntent, 0);
//
//        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
////        broadcastIntent.putExtra("toastMessage", message);
//        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
//                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.thumb);
        largeIcon = Bitmap.createScaledBitmap(largeIcon, 144, 144, true);
        Bitmap acc = BitmapFactory.decodeResource(getResources(), R.drawable.ic_checkbox);

        Notification notification = new NotificationCompat.Builder(this, Channel_1)
                .setSmallIcon(R.drawable.ic_checkbox)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(acc)
                        .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        notificationManager.notify(1, notification);
    }

}
