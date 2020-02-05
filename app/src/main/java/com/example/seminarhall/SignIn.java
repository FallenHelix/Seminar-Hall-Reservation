package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.SigningInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignIn extends AppCompatActivity implements View.OnClickListener{

    private static int RC_SIGN_IN=1;
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText emailInput,passwordInput;


    private TextView register,resetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setupViews();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();
        updateUi(user);
    }

    private void updateUi(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(this, UserDetails.class);
            Toast.makeText(this, "Please LogOut First!", Toast.LENGTH_LONG);
            startActivity(intent);
        }
    }

    protected void setupViews()
    {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.LogIn).setOnClickListener(this);
        register = findViewById(R.id.Rgst);
        resetPassword = findViewById(R.id.frgp);
        emailInput = findViewById(R.id.EmailInput);
        passwordInput = findViewById(R.id.passwordInput);
        findViewById(R.id.frgp).setOnClickListener(this);
        findViewById(R.id.Rgst).setOnClickListener(this);
        progressbar = findViewById(R.id.progressBar);
        register.setText(Html.fromHtml(register.getText().toString()));
        resetPassword.setText(Html.fromHtml(resetPassword.getText().toString()));

        //hide progress bar
        progressbar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if (i == R.id.btn_back) {
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            startActivity(intent);
        } else if (i == R.id.frgp) {
            Intent intent = new Intent(SignIn.this, reset.class);
            startActivity(intent);
        } else if (i == R.id.Rgst) {
            Intent intent = new Intent(SignIn.this, Singup.class);
            startActivity(intent);
        } else if (i == R.id.LogIn) {
            SignInUser();

        } else if (i == R.id.sign_in_button) {
            signIn();

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
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
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressBar();
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

                            progressbar.setVisibility(View.INVISIBLE);

                            Toast.makeText(SignIn.this,"Authentication Successful",Toast.LENGTH_LONG).show();
                            //updateUI(user);
                            Intent intent = new Intent(SignIn.this, UserDetails.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.INVISIBLE);

                            Toast.makeText(SignIn.this,"Authentication Failed",Toast.LENGTH_LONG).show();


                            //  updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }


    private void signIn() {
        progressbar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        progressbar.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    private void SignInUser()
    {
        String email=emailInput.getText().toString().trim();
        String password=passwordInput.getText().toString().trim();

        if(confirmInput())
            progressbar.setVisibility(View.VISIBLE);
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
                                        Intent intent = new Intent(SignIn.this,
                                                UserDetails.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        mAuth.signOut();
                                        progressbar.setVisibility(View.INVISIBLE);
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
        String email =  emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
            return false;
        } else {
            emailInput.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String password = passwordInput.getText().toString().trim();

        if (password.isEmpty()) {
            passwordInput.setError("Field can't be empty");
            return false;
        } else{
            passwordInput.setError(null);
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


}
