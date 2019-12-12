package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.android.material.textfield.TextInputLayout;

public class Singup extends AppCompatActivity {

    private static View view;
    private static EditText fullName, emailId, mobileNumber,
            password, confirmPassword;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        initViews();
    }


    private void initViews() {
        fullName = (EditText) findViewById(R.id.fullName);
        emailId = (EditText)findViewById(R.id.userEmailId);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signUpButton = (Button) findViewById(R.id.signUpBtn);
        login = (TextView) findViewById(R.id.already_user);
        terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                checkValidation();
            }
        });
     }

    private void checkValidation() {

        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getPassword = password.getText().toString();

        String getConfirmPassword = confirmPassword.getText().toString();

        fullName.setError("Error Here");


        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

        {
            fullName.setError("Error Here");
        }
    }
}
