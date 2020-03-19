package com.example.seminarhall.LogIn;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.os.Build;
import android.os.Bundle;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.ProfilePage;
import com.example.seminarhall.R;
import com.example.seminarhall.booking.Reserve;
import com.example.seminarhall.homePage.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button Continue;
    private FirebaseAuth mAuth;
    TextView newUserTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUPNotification();
        newUserTextView = findViewById(R.id.TextRegister);
        mAuth = FirebaseAuth.getInstance();
        newUserTextView.setText(Html.fromHtml(newUserTextView.getText().toString()));
        newUserTextView.setVisibility(View.VISIBLE);
        newUserTextView.setOnClickListener(this);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
             Intent intent = new Intent(MainActivity.this, UserDetails.class);
            startActivity(intent);
        }


        Continue = findViewById(R.id.button);
        Button temp = findViewById(R.id.button11);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
//                Intent intent = new Intent(MainActivity.this, Temp.class);

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


    }

    private void setUPNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotification", "MyNotification"
                            , NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "SuccessFull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onClick(View v) {
        int i = newUserTextView.getId();
        if (i == R.id.TextRegister) {
            Intent intent = new Intent(MainActivity.this, Singup.class);
            startActivity(intent);
        }

    }
}

