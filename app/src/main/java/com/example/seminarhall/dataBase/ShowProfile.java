package com.example.seminarhall.dataBase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ShowProfile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ShowProfile";

    String id;

    TextView name, email, branch, roll, mob,userType,bookings;
    ImageView profilePic;
    FirebaseUser user;
    Map<String, Object> map = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        user= FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
        setUpViews();
        setIntent();
    }



    private void setUpViews() {
        Log.d(TAG, "setUpViews: ");
        name = findViewById(R.id.TextView_name);
        branch = findViewById(R.id.TextView_branch);
        email = findViewById(R.id.TextView_email);
        roll = findViewById(R.id.TextView_roll);
        mob = findViewById(R.id.TextView_contact);
        profilePic = findViewById(R.id.userImgae);
        userType = findViewById(R.id.TextViewUserType);
        bookings = findViewById(R.id.TextViewBookings);

        findViewById(R.id.backButton).setOnClickListener(this);
        Log.d(TAG, "setUpViews: 2");
    }


    private void setIntent() {
        Intent intent = getIntent();
        id = intent.getExtras().getString("userId");
        loadData();
    }

    private void loadData() {
        DocumentReference db = FirebaseFirestore.getInstance().document("users/" + id);
        db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot!=null)
                map.putAll(documentSnapshot.getData());
                String photoUrl = (String) map.get("picUrl");
                if (photoUrl != null || photoUrl.length() > 5) {
                    loadPhoto(photoUrl);
                }
                putInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowProfile.this, "An Error Occured!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e);
                finish();
            }
        });
    }

    private void loadPhoto(String url) {
        if (url != null) {
            url = url.replace("s96-c", "s340-c");
            Picasso.get()
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(profilePic);
        }
    }

    private void putInfo() {
        try {
            name.setText((String) map.get("userName"));
            email.setText((String) map.get("email"));
            userType.setText((String) map.get("userType"));
            roll.setText((String) map.get("rollNumber"));
            branch.setText((String) map.get("Department"));
            mob.setText((String) map.get("mobile"));
            bookings.setText(String.valueOf((int) map.get("bookings")));

        } catch (Exception e) {
            Log.d(TAG, "putInfo: "+e);
        }
    }


    private void updateUI(FirebaseUser user) {
        Log.d(TAG, "updateUI: ");
        if (user == null) {
            finish();
            Log.d(TAG, "updateUI: 3");
        }
        Log.d(TAG, "updateUI: 2");
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        switch (i) {
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }
}
