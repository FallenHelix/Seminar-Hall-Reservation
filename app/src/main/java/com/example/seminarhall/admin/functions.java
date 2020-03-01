package com.example.seminarhall.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.seminarhall.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class functions extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 9001;

    // Add number views
    private EditText mFirstNumberField;
    private EditText mSecondNumberField;
    private EditText mAddResultField;
    private Button mCalculateButton;

    // Add message views
    private EditText mMessageInputField;
    private EditText mMessageOutputField;
    private Button mAddMessageButton;
    private Button mSignInButton;

    // [START define_functions_instance]
    private FirebaseFunctions mFunctions;
    // [END define_functions_instance]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);

        findViewById(R.id.MakeAdmin).setOnClickListener(this);
        findViewById(R.id.checkAdmin).setOnClickListener(this);

        mFirstNumberField = findViewById(R.id.fieldFirstNumber);
        mSecondNumberField = findViewById(R.id.fieldSecondNumber);
        mAddResultField = findViewById(R.id.fieldAddResult);
        mCalculateButton = findViewById(R.id.buttonCalculate);
        mCalculateButton.setOnClickListener(this);

       findViewById(R.id.sendEmail).setOnClickListener(this);

        // [START initialize_functions_instance]
        mFunctions = FirebaseFunctions.getInstance();
        // [END initialize_functions_instance]
    }


    private Task<String> makeAdmin(FirebaseUser user) {
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

    private Task<String> sendMail()
    {
        String email="shubham.bhakuni@somaiya.edu";
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        return mFunctions.getHttpsCallable("sendMail")
                .call(data).continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        return (String) result.get("operationResult");
                    }
                });
    }
    // [START function_add_numbers]
    private Task<Integer> addNumbers(int a, int b) {
        // Create the arguments to the callable function, which are two integers
        Map<String, Object> data = new HashMap<>();
        data.put("firstNumber", a);
        data.put("secondNumber", b);

        // Call the function and extract the operation from the result
        return mFunctions
                .getHttpsCallable("addNumbers")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, Integer>() {
                    @Override
                    public Integer then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.d(TAG, "then: ");
                        Map<String, Object> result = (Map<String, Object>) task.getResult().getData();
                        Log.d(TAG, "then: ");
                        return (Integer) result.get("operationResult");
                    }
                });
    }
    // [END function_add_numbers]

    // [START function_add_message]
    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }
    // [END function_add_message]

    private void onCalculateClicked() {
        int firstNumber;
        int secondNumber;

        hideKeyboard();

        try {
            firstNumber = Integer.parseInt(mFirstNumberField.getText().toString());
            secondNumber = Integer.parseInt(mSecondNumberField.getText().toString());
        } catch (NumberFormatException e) {
            showSnackbar("Please enter two numbers.");
            return;
    }

    // [START call_add_numbers]
    addNumbers(firstNumber, secondNumber)
                .addOnCompleteListener(new OnCompleteListener<Integer>() {
                    @Override
                    public void onComplete(@NonNull Task<Integer> task) {
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
                            Log.w(TAG, "addNumbers:onFailure", e);
                            showSnackbar("An error occurred.");
                            return;
                            // [END_EXCLUDE]
                        }

                        // [START_EXCLUDE]
                        Integer result = task.getResult();
                        mAddResultField.setText(String.valueOf(result));
                        // [END_EXCLUDE]
                    }
                });
        // [END call_add_numbers]
    }

    private void onAddMessageClicked() {
        String inputMessage = mMessageInputField.getText().toString();

        if (TextUtils.isEmpty(inputMessage)) {
            showSnackbar("Please enter a message.");
            return;
        }

        // [START call_add_message]
        addMessage(inputMessage)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }

                            // [START_EXCLUDE]
                            Log.w(TAG, "addMessage:onFailure", e);
                            showSnackbar("An error occurred.");
                            return;
                            // [END_EXCLUDE]
                        }

                        // [START_EXCLUDE]
                        String result = task.getResult();
                        mMessageOutputField.setText(result);
                        // [END_EXCLUDE]
                    }
                });
        // [END call_add_message]
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
            case R.id.buttonCalculate:
                onCalculateClicked();
                break;
            case R.id.sendEmail:
                sendEmail();
                break;
            case R.id.checkAdmin:
                checkAdmin();
                break;
            case R.id.MakeAdmin:
                MakeUserAdmin();

                break;

        }
    }

    private void checkAdmin() {
        Log.d("checkAdmin", "checkAdmin clicked");
//        Toast.makeText(this, "Make CheckAdmin", Toast.LENGTH_SHORT).show();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult result) {
                Map<String, Object> map=result.getClaims();
//                boolean isAdmin = (boolean) map.get("admin");
//                if (isAdmin) {
//                    // Show admin UI.
//                  Toast.makeText(functions.this,"User is admin",Toast.LENGTH_LONG).show();
//                } else {
//                    // Show regular user UI.
//                    Toast.makeText(functions.this,"User is NOT admin",Toast.LENGTH_LONG).show();
//
//                }
//                String f = (String) result.getClaims().get("admin");

                for (Map.Entry<String,Object> entry : map.entrySet())
                    Log.d("Tags","Key = " + entry.getKey() +
                            ", Value = " + entry.getValue());
            }
        });


    }

    private void MakeUserAdmin() {
        makeAdmin(FirebaseAuth.getInstance().getCurrentUser()).addOnCompleteListener(new OnCompleteListener<String>() {
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

    private void sendEmail()
    {
        Log.d(TAG, "sendEmail: started");
        sendMail().addOnCompleteListener(new OnCompleteListener<String>() {
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
                    Log.d(TAG, "onComplete: failed"+e);
                    return;
                    // [END_EXCLUDE]
                }
                else
                {
                    String end = task.getResult();
                    Log.d(TAG, "onComplete: passed?");
                    Toast.makeText(functions.this,end,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

