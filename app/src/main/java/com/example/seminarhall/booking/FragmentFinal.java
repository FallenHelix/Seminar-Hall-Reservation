package com.example.seminarhall.booking;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.example.seminarhall.homePage.FragmentActive;
import com.example.seminarhall.homePage.ReceiptAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentFinal extends Fragment implements ReceiptAdapter.ItemClickListener {
    private static final String TAG = "FragmentFinal";
    RecyclerView recyclerView;
    List<ReservedHall> halls;
    CollectionReference notebookRef;
    ReceiptAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        halls = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        setUpViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
//        String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        notebookRef = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed");
//        notebookRef.whereEqualTo("userId",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                halls.clear();
//                if (e != null) {
//                    System.err.println("Listen failed: " + e);
//                    return;
//                }
//                if(queryDocumentSnapshots!=null)
//                    for (QueryDocumentSnapshot query : queryDocumentSnapshots) {
//                        ReservedHall hall=query.toObject(ReservedHall.class);
//                        hall.setReservationId(query.getId());
////                    hall.setBookingDate(Calendar.getInstance().getTime());
//                        hall.setBookingDate(query.getTimestamp("bookingDate").toDate());
//                        halls.add(hall);
//                        Log.d(TAG, "onEvent: ");
//                    }
//                adapter = new ReceiptAdapter(halls);
//                recyclerView.setAdapter(adapter);
//                adapter.setListener(FragmentFinal.this);
//            }
//        });

    }

    private void setUpViews(View v) {
        recyclerView=(RecyclerView)v.findViewById(R.id.Active_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You have clicked"+position, Toast.LENGTH_SHORT).show();

    }
}
