package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class hallList extends AppCompatActivity implements HallListAdapter.ItemClickListener{

    HallListAdapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Hall> halls;
    RecyclerView recyclerHallList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_list);

        setUpRecyclerView();
        Toast.makeText(this,"Started",Toast.LENGTH_LONG).show();
    }

    private void setUpRecyclerView()
    {
        recyclerHallList = (RecyclerView)findViewById(R.id.RecView);
        recyclerHallList.setLayoutManager(new LinearLayoutManager(this));

        halls=new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Halls");
        adapter = new HallListAdapter(hallList.this,halls);
        recyclerHallList.setAdapter(adapter);
    }

    private void dislay(List<Hall> h) {

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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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



                adapter.getFilter().filter(newText);


                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this,"You Click: "+ position,Toast.LENGTH_LONG).show();
    }
}

