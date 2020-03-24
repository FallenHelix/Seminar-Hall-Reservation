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
import com.example.seminarhall.dataBase.addHall;
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
    TextView roll,Name,mobile;
    Spinner department,userType;
    HashMap<String, Object> map;
    private FirebaseFunctions mFunctions;
    private String branchString,userTypeString;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        user=FirebaseAuth.getInstance().getCurrentUser();
        UpdateUI(FirebaseAuth.getInstance().getCurrentUser());
        setUpViews();
        findViewById(R.id.AddUserDetails).setOnClickListener(this);
    }


    private void setUpViews() {
        Name = findViewById(R.id.User_Name);
        roll = (EditText) findViewById(R.id.Roll);
        department = (Spinner) findViewById(R.id.Branch);
        mobile = findViewById(R.id.mobile);
        mFunctions = FirebaseFunctions.getInstance();
        userType = findViewById(R.id.Usertype);
        roll.setEnabled(false);
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

    private boolean checkFields(String name,String roll,String mobile) {
        if (!branchSelected()) {
            return false;
        }
        else if ( name.length()<2) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            Name.setError("Enter Name");
            return false;
        } else if (this.roll.isEnabled() &&roll.length()<6) {
            this.roll.setError("Enter Roll Number");
            Toast.makeText(this, "Enter Roll Number!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mobile.length() < 10) {
            this.mobile.setError("Enter Mobile Number!");
            Toast.makeText(this, "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        int i = v.getId();
        if (i == R.id.AddUserDetails) {
            addToDb();
        }
    }

    private void addToDb() {
        if (user != null) {

            String fullName = Name.getText().toString().trim();

            String rollNumber = roll.getText().toString().trim();
            String mob = mobile.getText().toString().trim();
            if (checkFields(fullName, rollNumber, mob)) {
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
                                } else {
                                    Toast.makeText(NewUser.this, "An Error Occured!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                map = new HashMap<>();
                CollectionReference ref = FirebaseFirestore.getInstance().collection("users");
                map.put("userName", fullName);
                map.put("rollNumber", rollNumber);
                map.put("Department", branchString);
                map.put("userType", userTypeString);
                map.put("newUser", false);
                map.put("mobile", mob);
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
                        Log.d(TAG, "onFailure: " + e);
                        Toast.makeText(NewUser.this, "An Error Ocurred, Try agian later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please Fill Details", Toast.LENGTH_SHORT).show();
    }

    //need to add Validation fields Here

    private boolean branchSelected() {
        if (branchString == null || branchString.equals("Select Department")) {
            Log.d(TAG, "branchSelected: None");
            Toast.makeText(NewUser.this,"Select Branch",Toast.LENGTH_SHORT).show();
            return false;

        } else if (userType==null||userTypeString.equals("Select User Type")) {

            Log.d(TAG, "UserType: None");
            Toast.makeText(NewUser.this,"Select User Type",Toast.LENGTH_SHORT).show();
            return false;

        } else {
            Log.d(TAG, "branchSelected: " + branchString);
            Log.d(TAG, "User Type"+userTypeString);
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.Usertype){

            userTypeString = parent.getItemAtPosition(position).toString().trim();
            Toast.makeText(this, userTypeString, Toast.LENGTH_SHORT).show();
            if (userTypeString.compareTo("Student")==0) {
                roll.setEnabled(true);
            }
            else
            {
                roll.setText("");
                roll.setEnabled(false);
            }
        } else if (parent.getId()== R.id.Branch) {
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
