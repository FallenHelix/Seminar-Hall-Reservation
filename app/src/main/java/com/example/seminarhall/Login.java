package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    private  Button Login;
    private TextView Registration;
    private EditText EmailId;
    private EditText Password;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        EmailId = findViewById(R.id.userEmailId);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.LogIn);
        Registration = findViewById(R.id.Rgst);

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                goRegister();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmInput()) {
                    SignInUser();
                } else {
                    //Toast.makeText(this, "End ", Toast.LENGTH_LONG);

                }
            }
        });
    }


    private void SignInUser()
    {
        String email=EmailId.getText().toString().trim();
        String password=Password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();


                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent= new Intent(Login.this,
                                            UserDetails.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Login failed!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                }
                            }
                        });

        Toast.makeText(this, "End of Function", Toast.LENGTH_LONG);
    }


    private boolean validateEmail() {
        String emailInput = EmailId.getText().toString().trim();

        if (emailInput.isEmpty()) {
            EmailId.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            EmailId.setError("Please enter a valid email address");
            return false;
        } else {
            EmailId.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = Password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            Password.setError("Field can't be empty");
            return false;
        } else {
            Password.setError(null);
            return true;
        }
    }


    public boolean confirmInput() {
        if (!validateEmail() | !validatePassword()) {
            Toast.makeText(this, "Check Function Failed", Toast.LENGTH_LONG);

            return false;
        } else
        {

            Toast.makeText(this, "Check Function is Successful", Toast.LENGTH_LONG);

        return true;
        }
    }

    private void goRegister() {
        Intent intent = new Intent(Login.this, Singup.class);
        Toast.makeText(this, "Please Input Details",Toast.LENGTH_SHORT);
        startActivity(intent);
    }
}
