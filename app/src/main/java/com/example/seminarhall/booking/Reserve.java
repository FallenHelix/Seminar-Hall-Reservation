package com.example.seminarhall.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.seminarhall.Hall;
import com.example.seminarhall.LogIn.SignIn;
import com.example.seminarhall.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class Reserve extends AppCompatActivity implements FragmentCalendar.OnFragmentInteractionListener{
    private static final String TAG = "Reserve Class";
    private SectionPageAdapter msectionPageAdapter;
    private ViewPager mViewPager;
    private TextView head;
    private Hall selectedHall;
    FragmentTime fragmentTime ;



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Log.d(TAG, "onCreate: ");
        head = (TextView) findViewById(R.id.title);
        setHallName();
        //check user is logged in or not
        UpdateUI(FirebaseAuth.getInstance().getCurrentUser());


        //set up tabs
        msectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setUpViewPager();
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((SectionPageAdapter) mViewPager.getAdapter()).getFragment(position);
                try {
                    FragmentTime t = (FragmentTime) fragment;
                    t.onResume();
                } catch (Exception e) {

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        TextView head=findViewById(R.id.toolbarText);
        head.setText(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void UpdateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
        }
    }

    private void setHallName() {
        Intent intent=getIntent();
        selectedHall = intent.getParcelableExtra("Hall Selected");
        setUpToolbar(selectedHall.getName());
    }



    private void setUpViewPager()
    {
        Log.d(TAG, "setUpViewPager: ");
       fragmentTime = FragmentTime.newInstance("Select Date First");
        fragmentTime.setHall(selectedHall);
        FragmentCalendar c=new FragmentCalendar();
        c.setHall(selectedHall);
        msectionPageAdapter.addFragment(c, "Tab 1");
        msectionPageAdapter.addFragment(fragmentTime, "Tab 2");
        mViewPager.setAdapter(msectionPageAdapter);

    }

    @Override
    public void onFragmentInteraction(List<String> sendBackText) {
        if (sendBackText != null) {
            fragmentTime.updateDate(sendBackText);
        }
    }

    @Override
    public void clash(boolean stat) {
        fragmentTime.setClash(stat);
    }


}