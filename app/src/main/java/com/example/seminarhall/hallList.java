package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class hallList extends AppCompatActivity {

    HallListAdapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Hall> halls;
    RecyclerView recyclerHallList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_list);
        String[] languages={"Java", "JavaScrip", "C++", "Python", "XML", "adfasd", "adfadfas"};
        recyclerHallList = (RecyclerView)findViewById(R.id.RecView);
        recyclerHallList.setLayoutManager(new LinearLayoutManager(this));


        databaseReference = FirebaseDatabase.getInstance().getReference("Halls");
        if (databaseReference == null) {
            Toast.makeText(this,"Null Database",Toast.LENGTH_LONG).show();
        }
        halls=new ArrayList<>();


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
}

