package com.example.seminarhall;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class databaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceHall;
    private List<Hall> halls = new ArrayList<>();

    public databaseHelper() {
        mDatabase=FirebaseDatabase.getInstance();
        mReferenceHall = mDatabase.getReference("Hall");
    }

    public interface DataStatus
    {
        void DataIsLoaded(List<Hall> halls, List<String> keys);
        void DataIsInserted();

        void DataIsUpdated();

        void dataIsDeleted();
    }

    public void Readhalls(final DataStatus dataStatus){
        mReferenceHall.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                halls.clear();
                List<String > keys=     new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Hall hall=keyNode.getValue(Hall.class);
                    halls.add(hall);
                }
                dataStatus.DataIsLoaded(halls,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
