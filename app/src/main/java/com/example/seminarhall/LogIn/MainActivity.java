package com.example.seminarhall.LogIn;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.seminarhall.R;
import com.example.seminarhall.homePage.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private Button Continue;
    private FirebaseAuth mAuth;
    TextView newUserTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setUPNotification();
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

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);

                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
    }




    @Override
    public void onClick(View v) {
        int i = newUserTextView.getId();
        if (i == R.id.TextRegister) {
            Intent intent = new Intent(MainActivity.this, Signup.class);
            startActivity(intent);
        }

    }
}

