package com.example.seminarhall;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

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

public class ProfilePage extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "ProfilePage";

    TextView name, email, branch, roll, mob,userType,bookings;
    ImageView profilePic;
    FirebaseUser user;
    Map<String, Object> map = new HashMap<String, Object>();
    ImageView settings;
    float roation=0;


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
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.settings) {
            roation+=30;
            settings.setRotation(roation);
            showPopup(v);
        }
    }

    public void showPopup(View v) {
//        PopupMenu popup = new PopupMenu(this, v,18,20);
        PopupMenu popup = new PopupMenu(this, v, Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pop_profile_settings);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 clicked", Toast.LENGTH_SHORT).show();
                //Open Edit settins pages
                return true;
            default:
                return false;
        }
    }
}
