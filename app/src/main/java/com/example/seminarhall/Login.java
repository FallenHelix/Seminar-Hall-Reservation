package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {
    private  Button Login;
    private TextView Registration;
    private EditText EmailId;
    private TextView ResetPasword;
    SignInButton signInButton
    private GoogleSignInClient mGoogleSignInClient;
    private EditText Password;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        ResetPasword = findViewById(R.id.frgp);
        EmailId = findViewById(R.id.userEmailId);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.LogIn);
        Registration = findViewById(R.id.Rgst);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                signIn();
            }
        });

///////////////////////////////////////////////////////////
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        /////////////////////////////////////////////////////////////////


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                                    if (EmailVerification()) {

                                        Toast.makeText(getApplicationContext(),
                                                "Login successful!!",
                                                Toast.LENGTH_LONG)
                                                .show();


                                        // if sign-in is successful
                                        // intent to home activity
                                        Intent intent = new Intent(Login.this,
                                                UserDetails.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        mAuth.signOut();
                                    }                                }

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


    private boolean  EmailVerification()
    {
        final FirebaseUser user=mAuth.getCurrentUser();
        if (!user.isEmailVerified()) {
            user.sendEmailVerification();
            Toast.makeText(this, "Email Verification sent to Email Address", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
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
