package com.example.seminarhall.dataBase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.seminarhall.Hall;
import com.example.seminarhall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

public class addHall extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "addHall";

    EditText editTextHallName;
    EditText editTextHallSize;
    Button addToDb;
    DatabaseReference databaseReference;
    Spinner branch;
    private String branchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        branchString = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);
        editTextHallName = findViewById(R.id.Hall_Name);
        editTextHallSize = findViewById(R.id.Hall_Size);
        addToDb = findViewById(R.id.Add_a);
        branch = findViewById(R.id.HallDepartment);

        databaseReference = FirebaseDatabase.getInstance().getReference("Halls");

        setspinner();
        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHall();
            }
        });
    }


    private void setspinner() {
        Log.d(TAG, "setspinner: ");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Departments, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        branch.setAdapter(adapter);


        branch.setOnItemSelectedListener(this);
    }

    private void addHall() {

        String hallName = editTextHallName.getText().toString().trim();
        String size_string = editTextHallSize.getText().toString().trim();
        int size = -1;
        try {
            size = Integer.parseInt(size_string);
        } catch (Exception e) {
            Toast.makeText(addHall.this, "Enter Size", Toast.LENGTH_SHORT).show();
            return;
        }
        if(hallName.length()==0)
        {
            Toast.makeText(this, "Hall Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (size == 0) {
            Toast.makeText(this, "Hall Size should be Greater than 0", Toast.LENGTH_SHORT).show();
        }


        //checking if the value is provided
        if (!TextUtils.isEmpty(hallName) && branchSelected()) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
//            String id = databaseReference.push().getKey();
            addHallToDb(hallName, size);
            //creating an Hall Object
//            Hall hall = new Hall(hallName, size, id, branchString);
//
//
//            //Saving to database
//            databaseReference.child(id).setValue(hall);
//
//            //setting edittext to blank again
//            editTextHallName.setText("");
//            editTextHallSize.setText("");
//
//
//            //displaying a success toast
//            Toast.makeText(this, "Hall Added to Database", Toast.LENGTH_LONG).show();
//        } else {
//            //if the value is not given displaying a toast
//
//            Toast.makeText(this, "Please Correct Values ", Toast.LENGTH_LONG).show();
        }
    }

    private void addHallToDb(final String hallName, int size) {
        String id =hallName.toLowerCase();
        CollectionReference db = FirebaseFirestore.getInstance().collection("halls");
        Hall hall = new Hall(hallName, size, branchString);

        db.document(id).set(hall,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(addHall.this, "Hall Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addHall.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }

    private boolean branchSelected() {
        if (branchString == null || branchString == "Select Department") {
            return false;
        } else
            return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: ");
        branchString = parent.getItemAtPosition(position).toString().trim();
        Toast.makeText(this, branchString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");
        branchString = parent.getItemAtPosition(0).toString().trim();

    }

}
