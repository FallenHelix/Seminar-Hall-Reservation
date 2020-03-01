package com.example.seminarhall.booking;

import android.content.Intent;
import android.graphics.Paint;
import com.example.seminarhall.Hall;
import android.graphics.Typeface;
import android.os.Bundle;

import com.example.seminarhall.LogIn.SignIn;
import com.example.seminarhall.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Reserve extends AppCompatActivity implements FragmentCalendar.OnFragmentInteractionListener{
    private static final String TAG = "Reserve Class";
    private SectionPageAdapter msectionPageAdapter;
    private ViewPager mViewPager;
    private static List<String> mString;
    private TextView head;
    private static Hall selectedHall;

    public static List<String> getSelectedDates()
    {
        return mString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);

        Log.d(TAG, "onCreate: ");
        mString = new ArrayList<>();
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

    private void UpdateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), SignIn.class);
            startActivity(intent);
        }
    }

    private void setHallName() {
        Intent intent=getIntent();
        selectedHall = intent.getParcelableExtra("Hall Selected");
        head.setText(selectedHall.getName());
        head.setPaintFlags(head.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }

    public static Hall getHall()
    {
        return selectedHall;
    }



    private void setUpViewPager()
    {
        Log.d(TAG, "setUpViewPager: ");
        FragmentTime fragmentTime = FragmentTime.newInstance("Select Date First");
        msectionPageAdapter.addFragment(new FragmentCalendar(), "Tab 1");
        msectionPageAdapter.addFragment(fragmentTime, "Tab 2");
        mViewPager.setAdapter(msectionPageAdapter);

    }

    @Override
    public void onFragmentInteraction(List<String> sendBackText) {
        if (sendBackText != null) {
            mString=sendBackText;
//        msectionPageAdapter.notifyDataSetChanged();
        }
    }


}