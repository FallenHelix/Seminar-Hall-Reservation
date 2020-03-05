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
import com.example.seminarhall.admin.functions;
import com.example.seminarhall.homePage.UserDetails;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class NewUser extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewUser";
    TextView roll, department, Type;
    HashMap<String, String> map;
    private FirebaseFunctions mFunctions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        UpdateUI(FirebaseAuth.getInstance().getCurrentUser());
        setUpViews();
        findViewById(R.id.signUpButton).setOnClickListener(this);

    }

    private Task<String> newUser(FirebaseUser user) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        return mFunctions.getHttpsCallable("makeAdmin")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        Log.d(TAG, "then: "+result.get("message"));
                        return (String)result.get("message");
                    }
                });
    }

    public void afterInsert()
    {
        Log.d(TAG, "afterInsert: ");
        newUser(FirebaseAuth.getInstance().getCurrentUser()).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    if (e instanceof FirebaseFunctionsException) {
                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;

                        // Function error code, will be INTERNAL if the failure
                        // was not handled properly in the function call.
                        FirebaseFunctionsException.Code code = ffe.getCode();

                        // Arbitrary error details passed back from the function,
                        // usually a Map<String, Object>.
                        Object details = ffe.getDetails();
                    }

                    // [START_EXCLUDE]
                    Log.w(TAG, "Make Admin Failed", e);
                    return;
                    // [END_EXCLUDE]
                }
                else
                {
                    String end = task.getResult();
                    Toast.makeText(NewUser.this,end,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewUser.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setUpViews() {
        roll = (EditText) findViewById(R.id.Roll);
        department = (EditText) findViewById(R.id.department);
        Type = (EditText) findViewById(R.id.Type);
        mFunctions = FirebaseFunctions.getInstance();

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
        String fullName = user.getDisplayName();
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
        map = new HashMap<>();
        CollectionReference ref = FirebaseFirestore.getInstance().collection("Main/Users/Students");
        map.put("userName",fullName);
        map.put("rollNumber", rollNumber);
        map.put("Department", dept);
        ref.document(user.getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                afterInsert();

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
