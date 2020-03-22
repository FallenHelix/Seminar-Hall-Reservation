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
    Context context;
//    NotificationManager manager;

    setUpNotification(Context context,FirebaseUser user) {
        this.context=context;
        if (user != null) {
            this.user = user;
        }
//        this.manager=m;
        setUpGeneralNotification();
    }

//    "eujZ52c7SBejzmyH412o-O:APA91bHvbiSpZAMrNiztXbjWObUsnevO_EUK-x36xyzsT3UA8UXyPMYEOhS8UEODR2MUmh-WKuMmHrCub0DeubbAq1qIXngRDx7PsOVwISDSDF36LsYkH-XYbjdL4f_2ytNu-d3XCNUX"
    
    public void setFirebaseAdminNotification()
    {
        Log.d(TAG, "setFirebaseAdminNotification: ");
        FirebaseMessaging.getInstance().subscribeToTopic("admin")
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

    private void setUpGeneralNotification()
    {
       
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
