package com.example.seminarhall.booking;

import android.os.Bundle;

import com.example.seminarhall.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;


public class Reserve extends AppCompatActivity {
    private static final String TAG = "Reserve Class";
    private SectionPageAdapter msectionPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Log.d(TAG, "onCreate: ");

        msectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setUpViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void setUpViewPager(ViewPager viewPager)
    {
        Log.d(TAG, "setUpViewPager: ");
        msectionPageAdapter.addFragment(new FragmentCalendar(), "Tab 1");
        msectionPageAdapter.addFragment(new FragmentTime(), "Tab 2");
        msectionPageAdapter.addFragment(new FragmentFinal(), "Tab 2");
        viewPager.setAdapter(msectionPageAdapter);
    }
}