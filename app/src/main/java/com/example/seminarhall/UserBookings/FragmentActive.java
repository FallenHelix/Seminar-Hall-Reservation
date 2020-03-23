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

public class FragmentActive extends Fragment implements ReceiptAdapter.ItemClickListener{
    private static final String TAG = "FragmentActive";
    RecyclerView recyclerView;
    List<ReservedHall> halls;
    FirebaseFirestore db;
    CollectionReference notebookRef;
    ReceiptAdapter adapter;
    public List<Integer> status; //1: New, 2:approved, 3: declined
    private FirebaseUser user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        user=FirebaseAuth.getInstance().getCurrentUser();
        halls = new ArrayList<>();
        status = new ArrayList<>();
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
        if (user != null) {


            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Log.d(TAG, "onStart: ");
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
                                adapter.setListener(FragmentActive.this);
                            }
                        }
                    });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You have clicked" + position, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onItemClick: ");
        Intent intent = new Intent(getContext(), ReceiptUser.class);
        intent.putExtra("key",halls.get(position).getReservationId());
        intent.putExtra("stat", "Active");
        startActivity(intent);
    }
}