package com.example.seminarhall.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.seminarhall.LogIn.SignIn;
import com.example.seminarhall.R;
import com.example.seminarhall.booking.FragmentCalendar;
import com.example.seminarhall.booking.FragmentTime;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyBookings extends AppCompatActivity {
    private static final String TAG = "MyBookings";
    private BookingsPageAdapter adapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        Log.d(TAG, "onCreate: Started");

        getSupportActionBar().setTitle("My Bookings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpViews();
        UpdateUI(FirebaseAuth.getInstance().getCurrentUser());
    }

    private void setUpViews() {
        adapter = new BookingsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setUpViewPager();
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setUpViewPager() {
        Log.d(TAG, "setUpViewPager: ");
        adapter.addFragment(new FragmentActive(), "Tab 1");
        adapter.addFragment(new FragmentClosed(), "Tab 2");
        mViewPager.setAdapter(adapter);
    }


    private void UpdateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
        }
    }


}
