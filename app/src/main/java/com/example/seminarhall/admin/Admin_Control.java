package com.example.seminarhall.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.seminarhall.R;
import com.example.seminarhall.homePage.BookingsPageAdapter;
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
        Toolbar toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Control");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        adapter.addFragment(new FragmentAdminNew(), "Admin Active");
        adapter.addFragment(new FragmentAdminClosed(), "Admin Closed");
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
