package com.example.seminarhall.LogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.MainActivity;
import com.example.seminarhall.R;
import com.example.seminarhall.homePage.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Type;
import java.util.HashMap;

public class NewUser extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewUser";
    TextView roll, department, Type;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Intent intent = getIntent();
        map = (HashMap<String, String>) intent.getSerializableExtra("HashMapK");

        /*******/
        FirebaseUser user = (FirebaseUser)intent.getExtras().get("user");
        for (String x : map.values()) {
            Log.d(TAG, "Map"+x);
        }
        /*****/
        if (map == null) {
            intent = new Intent(NewUser.this, MainActivity.class);
            startActivity(intent);
        }
        UpdateUI(FirebaseAuth.getInstance().getCurrentUser());
        setUpViews();
        findViewById(R.id.signUpButton).setOnClickListener(this);

    }

    private void setUpViews() {
        roll = (EditText) findViewById(R.id.Roll);
        department = (EditText) findViewById(R.id.department);
        Type = (EditText) findViewById(R.id.Type);
    }

    private void UpdateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent;
            intent = new Intent(NewUser.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signUpButton) {
            addToDb();
        }
    }

    private void addToDb() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String fullName = map.get("Name");
        String rollNumber=roll.getText().toString().trim();
        String dept=department.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                        else
                        {
                            Toast.makeText(NewUser.this, "An Error Occured!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        CollectionReference ref = FirebaseFirestore.getInstance().collection("Main/Users/Students");
        map.put("rollNumber", rollNumber);
        map.put("Department", dept);
        ref.document(user.getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(NewUser.this, MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
                Toast.makeText(NewUser.this, "An Error Ocurred, Try agian later", Toast.LENGTH_LONG).show();
            }
        });
    }
}
