package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.LogIn.MainActivity;
import com.example.seminarhall.homePage.profile;
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

public class ProfilePage extends AppCompatActivity {

    private static final String TAG = "ProfilePage";

    TextView name, email, branch, roll, mob,userType,bookings;
    ImageView profilePic;
    FirebaseUser user;
    Map<String, Object> map = new HashMap<String, Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        user= FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
        setUpViews();

    }

    private void updateUI(FirebaseUser user) {
        Log.d(TAG, "updateUI: ");
        if (user == null) {
//            Intent intent = new Intent(ProfilePage.this, MainActivity.class);
//            startActivity(intent);
            finish();
            Log.d(TAG, "updateUI: 3");
        }
        Log.d(TAG, "updateUI: 2");
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPhoto();
        String id=user.getUid();
        DocumentReference db = FirebaseFirestore.getInstance().document("users/"+id);
        db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                map.putAll(documentSnapshot.getData());
                Log.d(TAG, "onSuccess: "+map.size());
                putInfo();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfilePage.this,"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void putInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name.setText((String) map.get("userName"));
        email.setText(user.getEmail());
        roll.setText((String) map.get("rollNumber"));
        branch.setText((String) map.get("Department"));
//        mob.setText((String) map.get("User Type:"));
//        bookings.setText();
        userType.setText((String)map.get("UserType"));
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
        Log.d(TAG, "setUpViews: 2");
    }

    private void loadPhoto() {
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
                    .into(profilePic);
        }
    }
}
