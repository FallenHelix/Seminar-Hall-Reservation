package com.example.seminarhall.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.example.seminarhall.homePage.BookingAdapter;
import com.example.seminarhall.homePage.FragmentActive;
import com.example.seminarhall.homePage.ReceiptAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAdminNew extends Fragment implements ReceiptAdapter.ItemClickListener {
    private static final String TAG = "FragmentAdminNew";
    RecyclerView recyclerView;
    List<ReservedHall> halls;
    FirebaseFirestore db;
    CollectionReference notebookRef;
    ReceiptAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        halls = new ArrayList<>();
        db= FirebaseFirestore.getInstance();
        notebookRef = FirebaseFirestore.getInstance().collection("/Main/Reservation/Active");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        setUpViews(view);
        return view;
    }

    private void setUpViews(View view) {
        recyclerView=(RecyclerView)view.findViewById(R.id.Active_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onStart() {
        super.onStart();
        String id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "onStart: ");
        notebookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                }
                adapter = new ReceiptAdapter(halls);
                recyclerView.setAdapter(adapter);
                adapter.setListener(FragmentAdminNew.this);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You have clicked"+position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: ");
        ReservedHall hall = halls.get(position);
        String id = hall.getReservationId();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Main/Reservation/Active").document(id).delete().addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Deletion Successful");
                    }
                }
        );
        db.collection("Main/Reservation/Closed").document(id).set(hall).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: In Adding to Closed");
            }
        });
        Map<String ,Object> map = new HashMap<>();
        map.put("Status:", true);
        map.put("Accepted Date", Calendar.getInstance().getTime());


        db.collection("Main/Reservation/Closed").document(id).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Merge");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Merge Failed");
            }
        });

    }
}