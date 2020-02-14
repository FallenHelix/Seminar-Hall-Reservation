package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{
    private TextView Email;
    private TextView U_id;
    FirebaseAuth mAuth;
    private DrawerLayout drawer;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        setView();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        updateUi(user);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }



    private void setView() {
        findViewById(R.id.SearchBydate).setOnClickListener(this);
        findViewById(R.id.sign_out).setOnClickListener(this);
        findViewById(R.id.See_data).setOnClickListener(this);
        findViewById(R.id.Save_data).setOnClickListener(this);
        Email = findViewById(R.id.Eml);
        U_id = findViewById(R.id.U_id);

        //Setting up Navigation Bar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //setting up side drawer email and profile pic
        String name,email;
        name=mAuth.getCurrentUser().getDisplayName();
        email=mAuth.getCurrentUser().getEmail();
        String url=mAuth.getCurrentUser().getPhotoUrl().toString();
        url=url.replace("s96-c", "s384-c");
        //get the text views
        View headerView = navigationView.getHeaderView(0);
        ImageView navPic= (ImageView) headerView.findViewById(R.id.Profile_photo);
        TextView nav_email = (TextView) headerView.findViewById(R.id.User_email);
        TextView nav_name = (TextView) headerView.findViewById(R.id.User_Name);

        Glide.with(this).load(url).into(navPic);
        nav_email.setText(email);
        nav_name.setText(name);
        navPic.setAdjustViewBounds(true);

        return;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //updateUI(null);
                    }
                });
        Intent intent = new Intent(UserDetails.this, SignIn.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();

        if (i == R.id.SearchBydate) {
            Intent intent = new Intent(UserDetails.this, functions.class);
            startActivity(intent);
        } else if (i == R.id.See_data) {
            Intent intent = new Intent(UserDetails.this, hallList.class);
            startActivity(intent);
        } else if (i == R.id.Save_data) {
            Intent intent = new Intent(UserDetails.this, addHall.class);
            startActivity(intent);
        } else if (i == R.id.sign_out) {
            signOut();
        }
    }

    private void updateUi(FirebaseUser user) {
        if (user != null) {
            //String name=user.getDisplayName();
            String email = user.getEmail();
            //String password=user.getUid();
            String password=user.getDisplayName();
            Email.setText(email);
            U_id.setText(password);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "User Not Logged IN",Toast.LENGTH_LONG);
        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i=item.getItemId();
        if (i == R.id.nav_profile) {
            Intent intent = new Intent(UserDetails.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void checkAuthority()
    {
        FirebaseUser user=mAuth.getCurrentUser();
        user.getDisplayName();
    }


}
