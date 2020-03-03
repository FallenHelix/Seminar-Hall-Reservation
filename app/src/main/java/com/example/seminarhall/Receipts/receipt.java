package com.example.seminarhall.Receipts;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.example.seminarhall.homePage.BookingAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class receipt extends AppCompatActivity {
    private static final String TAG = "receipt";
    RecyclerView recyclerView;
    ReceiptAdapter adapter;
    List<ReservedHall> halls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        setViews();
    }

    private void setViews() {
        recyclerView = (RecyclerView) findViewById(R.id.receipt_rec_view);
        halls = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        CollectionReference col = FirebaseFirestore.getInstance().collection("/Main/Reservation/Active");
        String id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        col.whereEqualTo("userId",id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                halls.clear();
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }
                if(queryDocumentSnapshots!=null)
                    for (QueryDocumentSnapshot query : queryDocumentSnapshots) {
                        ReservedHall hall=query.toObject(ReservedHall.class);
                        hall.setReservationId(query.getId());
                        halls.add(hall);
                        Log.d(TAG, "onEvent: ");
                    }
                adapter = new ReceiptAdapter(halls);
                recyclerView.setAdapter(adapter);
            }
        });

    }
}
