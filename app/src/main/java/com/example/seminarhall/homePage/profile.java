package com.example.seminarhall.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class profile extends AppCompatActivity {

    TextView name, email, branch, roll, mob;
    ImageView profilePic;
    Map<String, Object> map = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpViews();
        putInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id=user.getUid();
        DocumentReference db = FirebaseFirestore.getInstance().document("users/"+id);
      db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
          @Override
          public void onSuccess(DocumentSnapshot documentSnapshot) {
              map.putAll(documentSnapshot.getData());

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Toast.makeText(profile.this,"Error Occured",Toast.LENGTH_SHORT).show();
          }
      });
    }

    private void putInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        name.setText((String)map.get("userName"));
        email.setText(user.getEmail());
        mob.setText(user.getPhoneNumber());
//        mob.setText(user.getMetadata());
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

    private void setUpViews() {
        name = findViewById(R.id.TextView_name);
        branch = findViewById(R.id.TextView_branch);
        email = findViewById(R.id.TextView_email);
        roll = findViewById(R.id.TextView_roll);
        mob = findViewById(R.id.TextView_contact);
        profilePic = findViewById(R.id.Profile_photo);
    }
}
