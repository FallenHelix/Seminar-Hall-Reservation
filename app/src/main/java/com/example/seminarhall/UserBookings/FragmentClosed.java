package com.example.seminarhall.UserBookings;

import android.content.Intent;
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
import com.example.seminarhall.ReceiptUser;
import com.example.seminarhall.ReservedHall;
import com.example.seminarhall.dataBase.ReceiptAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentClosed extends Fragment implements ReceiptAdapter.ItemClickListener {
    private static final String TAG = "FragmentClosed";
    RecyclerView recyclerView;
    List<ReservedHall> halls;
    ReceiptAdapter adapter;
    List<Integer> status = new ArrayList<>();
    CollectionReference notebookRef = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed");
//    CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/Rejected");

    private FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        halls = new ArrayList<>();
        user=FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        setUpViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (user != null) {


            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            notebookRef.whereEqualTo("userId", id)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            status.clear();
                            halls.clear();
                            if (e != null) {
                                System.err.println("Listen failed: " + e);
                                return;
                            }
                            if (queryDocumentSnapshots != null) {
                                Log.d(TAG, "Size: " + queryDocumentSnapshots.size());

                                for (QueryDocumentSnapshot query : queryDocumentSnapshots) {
                                    ReservedHall hall = query.toObject(ReservedHall.class);
                                    hall.setReservationId(query.getId());
                                    Integer temp = query.get("Status") == null ? 0 : (Boolean) query.get("Status") == true ? 1 : 2;
                                    Log.d(TAG, "Temp: " + temp);
                                    Log.d(TAG, "Status: " + query.get("Status"));
                                    status.add(temp);
                                    halls.add(hall);
                                }
                                adapter = new ReceiptAdapter(getContext(), halls, status);
                                recyclerView.setAdapter(adapter);
                                adapter.setListener(FragmentClosed.this);
                            }
                        }
                    });
        }


    }

    private void setUpViews(View v) {
        recyclerView=(RecyclerView)v.findViewById(R.id.Active_recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You have clicked"+position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: ");
        Intent intent = new Intent(getContext(), ReceiptUser.class);
        intent.putExtra("key",halls.get(position).getReservationId());
        intent.putExtra("stat", "Closed");
        startActivity(intent);

    }
}
