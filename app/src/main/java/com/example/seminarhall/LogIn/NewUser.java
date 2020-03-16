package com.example.seminarhall.LogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.R;
import com.example.seminarhall.homePage.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.functions.FirebaseFunctions;

import java.util.HashMap;

public class NewUser extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "NewUser";
    TextView roll,Name;
    Spinner department,userType;
    HashMap<String, Object> map;
    private FirebaseFunctions mFunctions;
    private String branchString,userTypeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
//        UpdateUI(FirebaseAuth.getInstance().getCurrentUser());
        setUpViews();
        findViewById(R.id.signUpButton).setOnClickListener(this);

    }


    private void setUpViews() {
        Name = findViewById(R.id.nameInput);
        roll = (EditText) findViewById(R.id.Roll);
        department = (Spinner) findViewById(R.id.department);
        mFunctions = FirebaseFunctions.getInstance();
        userType = findViewById(R.id.Usertype);
        setUpSpinner();

    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Departments, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.UserType,
                android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        userType.setAdapter(adapter1);
        department.setAdapter(adapter);
        userType.setOnItemSelectedListener(this);
        department.setOnItemSelectedListener(this);
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
        String fullName = Name.getText().toString().trim();
        String rollNumber=roll.getText().toString().trim();
//        String dept=department.getText().toString().trim();

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
        map = new HashMap<>();
        CollectionReference ref = FirebaseFirestore.getInstance().collection("users");
        map.put("userName",fullName);
        map.put("rollNumber", rollNumber);
        map.put("Department", branchString);
        map.put("User Type:", userTypeString);
        map.put("newUser", false);
        ref.document(user.getUid()).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: ");
                Intent intent = new Intent(NewUser.this, UserDetails.class);
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


    //need to add Validation fields Here



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.Usertype){
            Toast.makeText(this, "User Type Selected", Toast.LENGTH_SHORT).show();
            userTypeString=parent.getItemAtPosition(position).toString().trim();
        } else if (parent.getId()== R.id.department) {
            branchString=parent.getItemAtPosition(position).toString().trim();
            Toast.makeText(this,branchString,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        branchString=parent.getItemAtPosition(0).toString().trim();
        userTypeString=parent.getItemAtPosition(0).toString().trim();

    }
}
