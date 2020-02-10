package com.example.seminarhall;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class SeeRev extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Reservation");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_rev);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);

        getData();
    }



    protected void getData()
    {
        CollectionReference x = notebookRef.document("In_Progress").collection("Upcoming");

        x.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                    String data="";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ReservedHall hall = documentSnapshot.toObject(ReservedHall.class);
                    hall.setReservationId(documentSnapshot.getId());

                    String documentId = hall.reservationId;
                    String title = hall.getHallId();
                    String description = hall.getPurpose();

                    data += "ID: " + documentId
                            + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
                }
                textViewData.setText(data);

            }
        });

//        x.document("CQFgRzFN0zcXrcaN47Ti").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                ReservedHall hall = documentSnapshot.toObject(ReservedHall.class);
//                hall.setReservationId(documentSnapshot.getId());
//
//                String documentId = hall.reservationId;
//                String title = hall.getHallId();
//                String description = hall.getPurpose();
//                String data="";
//
//                data += "ID: " + documentId
//                        + "\nTitle: " + title + "\nDescription: " + description + "\n\n";
//                textViewData.setText(data);
//            }
//        });
    }

}