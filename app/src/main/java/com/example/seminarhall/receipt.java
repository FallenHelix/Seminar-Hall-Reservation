package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seminarhall.booking.Reserve;
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

    TextView startDate,startTime,endDate,endTime,purpose;
    TextView bookingId,Room,Duration;
    ReservedHall hall=null;
    Integer status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        setUpViews();
        setUpIntent();
    }

    private void setUpIntent() {
        Log.d(TAG, "setUpIntent: ");
        Intent intent=getIntent();
        String key=(String)intent.getExtras().get("key");
        String stat = (String) intent.getExtras().get("stat");
        status = (Integer) intent.getExtras().get("status");
        CollectionReference db= FirebaseFirestore.getInstance().collection("Main/Reservation/"+stat);
        db.document(key).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hall = documentSnapshot.toObject(ReservedHall.class);
                hall.setReservationId(documentSnapshot.getId());
                fillData();
            }
        });
    }

    private void fillData()
    {
        Log.d(TAG, "fillData: ");
        bookingId.append(hall.getReservationId());
        startTime.append(hall.getStartTime());
        endTime.append(hall.getEndTime());
        endDate.append(hall.getDays().get(hall.getNoOfDays() - 1));
        startDate.append(hall.getDays().get(0));
        purpose.setText(hall.getPurpose());

    }

    public void setUpViews()
    {
        Log.d(TAG, "setUpViews: ");
        startDate = findViewById(R.id.TextStartDate);
        endDate = findViewById(R.id.TextEndDate);
        endTime = findViewById(R.id.TextEndTime);
        startTime = findViewById(R.id.TextStartTime);
        Duration = findViewById(R.id.TextDuration);
        bookingId = findViewById(R.id.TextBookingId);
        Room = findViewById(R.id.TextRoom);
        purpose = findViewById(R.id.TextViewPurpose);
        findViewById(R.id.ButtonReject).setOnClickListener(this);
        findViewById(R.id.ButtonAccept).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ButtonAccept) {
            if(hall!=null)
            accept();
        } else if (i == R.id.ButtonReject) {
            if(hall!=null)
            reject();
        }
    }

    private void reject() {
        Log.d(TAG, "reject: ");
        WriteBatch batch=FirebaseFirestore.getInstance().batch();

        String id = hall.getReservationId();
        CollectionReference db=FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        DocumentReference doc1=db.document(id);
        DocumentReference doc2=FirebaseFirestore.getInstance().collection("Main/Reservation/Closed").document(id);
        batch.delete(doc1);
        batch.set(doc2,hall,SetOptions.merge());
        Map<String ,Object> map = new HashMap<>();
        map.put("Status", false);
        map.put("D_Date", Calendar.getInstance().getTime());
        map.put("Admin_Id", FirebaseAuth.getInstance().getCurrentUser().getUid());
//        batch.update(doc2, map);
        batch.set(doc2, map, SetOptions.merge());
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(receipt.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(receipt.this, "Error Encountered", Toast.LENGTH_SHORT).show();
            }
        });
//        doc2.update(map).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(receipt.this, ""+e, Toast.LENGTH_SHORT).show();
//            }
//
//        });
    }
    private void accept()
    {
        Log.d(TAG, "accept: ");
        WriteBatch batch=FirebaseFirestore.getInstance().batch();

        String id = hall.getReservationId();
        CollectionReference db=FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        DocumentReference doc1=db.document(id);
        DocumentReference doc2=FirebaseFirestore.getInstance().collection("Main/Reservation/Closed").document(id);
        batch.delete(doc1);
        Map<String ,Object> map = new HashMap<>();
        map.put("Status", true);
        map.put("D_Date", Calendar.getInstance().getTime());
        map.put("Admin_Id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        batch.set(doc2,hall,SetOptions.merge());
        batch.set(doc2,map, SetOptions.merge());


        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(receipt.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(receipt.this, "Error Encountered"+e, Toast.LENGTH_SHORT).show();
            }
        });
//        doc2.update(map).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(receipt.this, "Failure"+e, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}
