package com.example.seminarhall.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.seminarhall.R;
import com.example.seminarhall.homePage.BookingsPageAdapter;
import com.example.seminarhall.homePage.FragmentActive;
import com.example.seminarhall.homePage.FragmentClosed;
import com.example.seminarhall.homePage.UserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Map;

public class Admin_Control extends AppCompatActivity {
    private static final String TAG = "Admin_Control";
    private BookingsPageAdapter adapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__control);
        setUpViews();
        updateUI(FirebaseAuth.getInstance().getCurrentUser());
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
        adapter.addFragment(new FragmentAdminNew(), "Tab 1");
        adapter.addFragment(new FragmentClosed(), "Tab 2");
        mViewPager.setAdapter(adapter);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null)
        {
            currentUser.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult getTokenResult) {
                    Map<String, Object> map=getTokenResult.getClaims();

                    boolean isAdmin= map.get("admin") != null && (boolean) map.get("admin");
                    if (!isAdmin) {
                        Intent intent = new Intent(Admin_Control.this, UserDetails.class);
                        startActivity(intent);

                    }
                }
            });
        } else if (currentUser == null) {
            Intent intent = new Intent(Admin_Control.this, UserDetails.class);
            startActivity(intent);
        }
    }
}
