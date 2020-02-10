package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class hallList extends AppCompatActivity implements HallListAdapter.ItemClickListener,NavigationView.OnNavigationItemSelectedListener
{

    HallListAdapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Hall> halls;
    RecyclerView recyclerHallList;
    private boolean FirstTime=true;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_list);

        setUpView();

    }

    private void setUpView()
    {
        recyclerHallList = (RecyclerView)findViewById(R.id.RecView);
        recyclerHallList.setLayoutManager(new LinearLayoutManager(this));
        halls=new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Halls");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                halls.clear();
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Hall hall = postSnapshot.getValue(Hall.class);
                    halls.add(hall);
                }
                adapter = new HallListAdapter(hallList.this,halls);
                recyclerHallList.setAdapter(adapter);
                start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void start()
    {


        adapter.setClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast.makeText(this,"OnCreate Options Menu",Toast.LENGTH_SHORT).show();
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.hall_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView )searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView .OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    int x = Integer.parseInt(newText);
                } catch (Exception e) {
                    ExceptionFunction();
                    return true;
                }
                if((adapter!=null&& adapter.getItemCount()!=0)||FirstTime!=true) {
                    adapter.getFilter().filter(newText);
                    FirstTime=false;
                }
                return false;
            }
        });
        return true;
    }

    private void ExceptionFunction()
    {
        Toast.makeText(this, "Please Input Only Number",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), Booking.class);
        intent.putExtra("Hall Selected", halls.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i=item.getItemId();

        if (i == R.id.nav_profile) {
            Intent intent = new Intent(hallList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}

