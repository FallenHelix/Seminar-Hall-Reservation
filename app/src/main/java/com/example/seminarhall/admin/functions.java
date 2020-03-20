package com.example.seminarhall.admin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.seminarhall.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class functions extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "functions";
    private static final int RC_SIGN_IN = 9001;


    private EditText email;


    // Add message views


    private FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);

        findViewById(R.id.MakeAdmin).setOnClickListener(this);
        findViewById(R.id.checkAdmin).setOnClickListener(this);

        email = findViewById(R.id.email);

       findViewById(R.id.sendEmail).setOnClickListener(this);

        // [START initialize_functions_instance]
        mFunctions = FirebaseFunctions.getInstance();
        setupToolbar();
        // [END initialize_functions_instance]
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private Task<String> makeAdmin(String email) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        return mFunctions.getHttpsCallable("makeAdmin")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        Log.d(TAG, "then: " + result.get("message"));
                        return (String) result.get("message");
                    }
                });
    }



    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MakeAdmin:
                MakeUserAdmin();
                break;

        }
    }

    private void MakeUserAdmin() {
        String mail=email.getText().toString().trim();
        if (mail.length() == 0) {
            Toast.makeText(this, "Enter Email Please", Toast.LENGTH_SHORT).show();
            return;
        }
        makeAdmin(mail).addOnCompleteListener(new OnCompleteListener<String>() {
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
                    showSnackbar("An error occurred.");
                    return;
                    // [END_EXCLUDE]
                }
                else
                {
                    String end = task.getResult();
                    Toast.makeText(functions.this,end,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

