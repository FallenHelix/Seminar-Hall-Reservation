package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profile extends AppCompatActivity {

    TextView name,email,branch,roll,mob;
    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpViews();
        putInfo();
    }

    private void putInfo() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        mob.setText(user.getPhoneNumber());
//        mob.setText(user.getMetadata());
        String url=user.getPhotoUrl().toString();
        String u=url.replace("s96-c", "s340-c");

        Glide.with(this).load(u)
                .into(profile);

//        profile.setAdjustViewBounds(true);
    }

    private void setUpViews() {
        name = findViewById(R.id.TextView_name);
        branch = findViewById(R.id.TextView_branch);
        email = findViewById(R.id.TextView_email);
        roll = findViewById(R.id.TextView_roll);
        mob = findViewById(R.id.TextView_contact);
        profile = findViewById(R.id.imageview8);
    }
}
