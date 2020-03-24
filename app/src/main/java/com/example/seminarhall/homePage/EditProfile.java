package com.example.seminarhall.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seminarhall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener {
    private static final String TAG = "EditProfile";
    
    //View Components
    EditText name,roll,mobile;
    Spinner brach,user;
    String branchType,userType;
    FirebaseUser mUser;
    Map<String,String> map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        map = new HashMap<>();
        setUpViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserDetails();
    }

    private void loadUserDetails() {
        CollectionReference db = FirebaseFirestore.getInstance().collection("users");
        db.document(mUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText((String)documentSnapshot.get("userName"));
                String r = (String) documentSnapshot.get("rollNumber");
                String mob = (String) documentSnapshot.get("mobile");

                if (r != null) {
                    if (!roll.isEnabled()) {
                        roll.setEnabled(true);
                        roll.setText(r);
                    } else {
                        roll.setEnabled(true);
                    }
                }
                if (mob != null) {
                    mobile.setText(mob);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");
                Toast.makeText(EditProfile.this, "Error Loading Details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setUpViews() {
        Log.d(TAG, "setUpViews: ");
        name = findViewById(R.id.User_Name);
        roll = findViewById(R.id.Roll);
        mobile = findViewById(R.id.mobile);
        user = findViewById(R.id.Usertype);
        brach = findViewById(R.id.Branch);
        findViewById(R.id.backButton).setOnClickListener(this);
        findViewById(R.id.AddUserDetails).setOnClickListener(this);
        //Disable roll
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
        user.setAdapter(adapter1);
        brach.setAdapter(adapter);
        user.setOnItemSelectedListener(this);
        brach.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        int i=v.getId();
        switch (i)
        {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.AddUserDetails:
               updateDetailsUser();
        }
        
    }

    private boolean checkFields(String name,String roll,String mobile) {
        if (!branchSelected()) {
            return false;
        }
        Log.d(TAG, "checkFields: ");
        if ( name == null) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (this.roll.isEnabled() &&roll.length()<6) {
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

    private void updateDetailsUser() {
        String id=mUser.getUid();
        String userName=name.getText().toString().trim();
        String userRoll=roll.getText().toString().trim();
        String userMobile=mobile.getText().toString().trim();
        if (!checkFields(userName, userRoll,userMobile)||id==null) {
            return;
        }
        map.put("userName",userName);
        if(roll.isEnabled())
            map.put("rollNumber", userRoll);
        else
            map.put("rollNumber", null);
        map.put("Department", branchType);
        map.put("UserType", userType);
        map.put("mobile", userMobile);

        CollectionReference db = FirebaseFirestore.getInstance().collection("users");
        db.document(id).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: ");
                Intent intent = new Intent(EditProfile.this, UserDetails.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
                Toast.makeText(EditProfile.this, "An Error Ocurred, Try agian later", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean branchSelected() {
        if (branchType == null || branchType.equals("Select Department")) {
            Log.d(TAG, "branchSelected: None");
            Toast.makeText(EditProfile.this,"Select Branch",Toast.LENGTH_SHORT).show();
            return false;

        } else if (userType==null||userType.equals("Select User Type")) {

            Log.d(TAG, "UserType: None");
            Toast.makeText(EditProfile.this,"Select User Type",Toast.LENGTH_SHORT).show();
            return false;

        } else {
            Log.d(TAG, "branchSelected: " + branchType);
            return true;
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.Usertype){
            Log.d(TAG, "User: "+userType);
            userType=parent.getItemAtPosition(position).toString().trim();
            if (userType.compareTo("Student")==0) {
                roll.setEnabled(true);
            }
            else
            {
                roll.setText("");
                roll.setEnabled(false);
            }
        } else if (parent.getId()== R.id.Branch) {
            branchType=parent.getItemAtPosition(position).toString().trim();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        branchType=parent.getItemAtPosition(0).toString().trim();
        userType=parent.getItemAtPosition(0).toString().trim();

    }
}
