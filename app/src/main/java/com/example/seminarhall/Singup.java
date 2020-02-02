package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.android.material.textfield.TextInputLayout;

public class Singup extends AppCompatActivity {

    //private static View view;
    private FirebaseAuth mAuth;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private static EditText fullName, emailId, mobileNumber,
            password, confirmPassword;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    //private static ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        initViews();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }


    private void initViews() {
        fullName = (EditText) findViewById(R.id.nameInput);
        emailId = (EditText)findViewById(R.id.emailInput);
        mobileNumber = (EditText) findViewById(R.id.mobileInput);
        password = (EditText)findViewById(R.id.passwordInput);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordInput);
        signUpButton = (Button) findViewById(R.id.signUpButton);




        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                checkValidation();
                if (confirmInput()) {
                    register();
                }

            }
        });
     }

    private void checkValidation() {

        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getPassword = password.getText().toString();

        String getConfirmPassword = confirmPassword.getText().toString();


        if(getFullName.length()==0)
        fullName.setError("Error Here");


         else if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

        {
            fullName.setError("Error 2");
        }
        else
            fullName.setError(null);
    }


    private boolean validateEmail() {
        String emailInput = emailId.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailId.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailId.setError("Please enter a valid email address");
            return false;
        } else if (!emailInput.substring(emailInput.indexOf("@") + 1).equals("somaiya.edu")) {
            emailId.setError("Only Somaiya email address Allowed");
            return false;
        } else {
            emailId.setError(null);
            return true;
        }
    }
    private boolean validateUsername() {
        String usernameInput = fullName.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            fullName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            fullName.setError("Username too long");
            return false;
        } else {
            fullName.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();
        String cpassword=confirmPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else if (!passwordInput.equals(cpassword)) {
            confirmPassword.setError("Passwords didn't match");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public boolean confirmInput() {
        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return false;
        }
        else
            return true;


//        String input = "Email: " + emailId.getText().toString();
//        input += "\n";
//        input += "Username: " + fullName.getText().toString();
//        input += "\n";
//        input += "Password: " + password.getText().toString();
//
        //Toast.makeText(this, "Sucess", Toast.LENGTH_SHORT).show();

    }

    private void register() {
        String email=emailId.getText().toString().trim();
        String passwordInput=password.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, passwordInput).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!, Please verify account",
                                    Toast.LENGTH_SHORT).show();

                            // hide the progress bar

                            // if the user created intent to login activity
                            Intent intent= new Intent(Singup.this,
                                    Login.class);
                            startActivity(intent);
                        }
                        else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                        }
                    }
                });
    }



}
