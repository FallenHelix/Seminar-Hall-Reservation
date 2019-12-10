package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private Button Register;
    private EditText email;
    private EditText pass;
    private EditText Cpass;
    private EditText info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register = (Button) findViewById(R.id.rgs);
        email=(EditText)findViewById(R.id.userN);
        pass = (EditText) findViewById(R.id.pass);
        Cpass=(EditText) findViewById(R.id.pass2);
        info =(EditText) findViewById(R.id.Txt);
        info.setText("Email Used for OTP");

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                CheckRegistration(email.getText().toString(),pass.getText().toString(),Cpass.getText().toString());
            }
        });
    }
    protected void CheckRegistration(String email,String pass,String Cpass)
    {
        if(!checkEmail(email)) {
            //Wrong Email
            info.setText("Wrong Email!! ");
        } else if (!pass.equals(Cpass)) {
            info.setText("Passwords Don't Match");

            //password doesn't Match
        } else {
            Intent in =new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(in);
            //all GOOD? call next activity;
        }
    }
    private boolean checkEmail(String email)
    {
        return true;
    }
}

