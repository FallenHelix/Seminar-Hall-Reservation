package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfilePage extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        image = findViewById(R.id.userImgae);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        String url = null;
        if (user.getPhotoUrl() != null) {
            url = user.getPhotoUrl().toString();
        }
        if (url != null) {
            url = url.replace("s96-c", "s340-c");
            Picasso.get()
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(image);
        }
    }
}
