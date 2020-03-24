package com.example.seminarhall.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.example.seminarhall.dataBase.ShowProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class receipt extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "receipt";

    TextView startDate, startTime, endDate, endTime, purpose;
    TextView bookingId, Room, Duration;
    ReservedHall hall = null;
    Integer status;

    TextView hallRoom,UserId;
    TextView hallSize;
    TextView hallBranch,hallName,hallBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        setUpViews();
        setUpIntent();
        setUpToolbar();
    }

    private void setUpToolbar() {

        Toolbar toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUpViews() {
        Log.d(TAG, "setUpViews: ");
        startDate = findViewById(R.id.TextStartDate);
        endDate = findViewById(R.id.TextEndDate);
        endTime = findViewById(R.id.TextEndTime);
        startTime = findViewById(R.id.TextStartTime);
        Duration = findViewById(R.id.TextDuration);
        bookingId = findViewById(R.id.TextBookingId);
        Room = findViewById(R.id.TextRoom);
        purpose = findViewById(R.id.TextViewPurpose);
        //Setting up Hall Details
        hallRoom = (TextView) findViewById(R.id.HallRoom);
        hallSize = (TextView) findViewById(R.id.HallSize);
        hallBranch = findViewById(R.id.HallDepartment);
        hallName = findViewById(R.id.HallName);
        hallBuilding = findViewById(R.id.HallBuilding);

        findViewById(R.id.ButtonReject).setOnClickListener(this);
        findViewById(R.id.ButtonAccept).setOnClickListener(this);
        findViewById(R.id.hell).setOnClickListener(this);
    }


    private void setUpIntent() {
        Log.d(TAG, "setUpIntent: ");
        Intent intent = getIntent();
        String key = (String) intent.getExtras().get("key");
        String stat = (String) intent.getExtras().get("stat");
        if (stat.compareTo("Closed") == 0) {
            Button accept = findViewById(R.id.ButtonAccept);
            Button reject = findViewById(R.id.ButtonReject);
            accept.setEnabled(false);
            reject.setEnabled(false);
            accept.setVisibility(View.INVISIBLE);
            reject.setVisibility(View.INVISIBLE);
        }
        status = (Integer) intent.getExtras().get("status");

        CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/" + stat);
        db.document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hall = documentSnapshot.toObject(ReservedHall.class);
                if (hall != null) {
                    hall.setReservationId(documentSnapshot.getId());
                    fillData();
                }
            }
        });
    }


    private void fillData() {
        Log.d(TAG, "fillData: ");
        bookingId.append(hall.getReservationId());
        startTime.setText(hall.getStartTime());
        endTime.setText(hall.getEndTime());
        endDate.setText(hall.getDays().get(hall.getNoOfDays() - 1));
        startDate.setText(hall.getDays().get(0));
        purpose.setText(hall.getPurpose());
    }


    @Override
    public void onClick(View v) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(200);

        int i = v.getId();
        if (i == R.id.ButtonAccept) {
            if (hall != null)
                accept();
        } else if (i == R.id.ButtonReject) {
            if (hall != null)
                reject();
        } else if (i == R.id.hell) {

            Intent intent = new Intent(receipt.this, ShowProfile.class);
            intent.putExtra("userId", hall.getUserId());
            startActivity(intent);
        }
    }

    private void reject() {
        findViewById(R.id.ButtonReject).setEnabled(false);
        Log.d(TAG, "reject:");
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        String id = hall.getReservationId();
        CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        DocumentReference doc1 = db.document(id);
        DocumentReference doc2 = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed").document(id);
        batch.delete(doc1);
        batch.set(doc2, hall, SetOptions.merge());
        Map<String, Object> map = new HashMap<>();
        map.put("Status", false);
        map.put("D_Date", Calendar.getInstance().getTime());
        map.put("Admin_Id", FirebaseAuth.getInstance().getCurrentUser().getUid());
//        batch.update(doc2, map);
        batch.set(doc2, map, SetOptions.merge());
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(receipt.this, "Booking Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(receipt.this, "Error Encountered", Toast.LENGTH_SHORT).show();
                findViewById(R.id.ButtonReject).setEnabled(true);
            }
        });
    }

    private void accept() {
        findViewById(R.id.ButtonAccept).setEnabled(false);
        Log.d(TAG, "accept: ");
        WriteBatch batch = FirebaseFirestore.getInstance().batch();

        String id = hall.getReservationId();
        CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        DocumentReference doc1 = db.document(id);
        DocumentReference doc2 = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed").document(id);
        batch.delete(doc1);
        Map<String, Object> map = new HashMap<>();
        map.put("Status", true);
        map.put("D_Date", Calendar.getInstance().getTime());
        map.put("Admin_Id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        batch.set(doc2, hall, SetOptions.merge());
        batch.set(doc2, map, SetOptions.merge());


        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(receipt.this, "Booking updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(receipt.this, "Error Encountered" + e, Toast.LENGTH_SHORT).show();
                findViewById(R.id.ButtonAccept).setEnabled(true);
            }
        });

    }

}
