package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDetails extends AppCompatActivity {
    private TextView Email;
    private TextView U_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setView();

        if (user != null) {
            //String name=user.getDisplayName();
            String email = user.getEmail();
            String password=user.getUid();
            Email.setText(email);
            U_id.setText(password);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        else
        {
            Toast.makeText(this, "User Not Logged IN",Toast.LENGTH_LONG);
        }
    }

    private void setView() {
        Email = findViewById(R.id.Eml);
        U_id = findViewById(R.id.U_id);
        return;
    }
}
