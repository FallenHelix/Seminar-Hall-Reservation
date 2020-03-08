package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.seminarhall.booking.Reserve;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class receipt extends AppCompatActivity {
    private static final String TAG = "receipt";

    TextView startDate,startTime,endDate,endTime,purpose;
    TextView bookingId,Room,Duration;
    ReservedHall hall;
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
    }

}
