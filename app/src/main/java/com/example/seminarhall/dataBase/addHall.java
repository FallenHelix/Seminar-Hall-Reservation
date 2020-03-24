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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class addHall extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "addHall";

    EditText editTextHallName;
    EditText editTextHallSize,floor;
    Button addToDb;
    DatabaseReference databaseReference;
    Spinner branch, buildingType;
    private String branchString,HallBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        branchString = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hall);
        editTextHallName = findViewById(R.id.HallName);
        editTextHallSize = findViewById(R.id.HallSize);
        addToDb = findViewById(R.id.Add_a);
        floor = findViewById(R.id.HallFloor);
        branch = findViewById(R.id.HallDepartment);
        buildingType = findViewById(R.id.HallBuilding);

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
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.BuildingType, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        branch.setAdapter(adapter);
        buildingType.setAdapter(adapter2);
        buildingType.setOnItemSelectedListener(this);

        branch.setOnItemSelectedListener(this);
    }

    private void addHall() {

        String hallName = editTextHallName.getText().toString().trim().toUpperCase();
        String size_string = editTextHallSize.getText().toString().trim();
        String f=floor.getText().toString().trim().toUpperCase();
        int size = -1;
        try {
            size = Integer.parseInt(size_string);
        } catch (Exception e) {
            Toast.makeText(addHall.this, "Enter Size", Toast.LENGTH_SHORT).show();
            editTextHallSize.setText("");
            editTextHallSize.setError("Enter Size");

            return;
        }
        if(TextUtils.isEmpty(f))
        {
            floor.setError("Required");
            return;
        } else if (size == 0) {
            Toast.makeText(this, "Hall Size should be Greater than 0", Toast.LENGTH_SHORT).show();
        }

        if (!TextUtils.isEmpty(hallName) && branchSelected()) {
            addHallToDb(hallName, size,f);

        }
    }

    private void addHallToDb(final String hallName, int size,String f) {
        String id =hallName.toLowerCase();
        CollectionReference db = FirebaseFirestore.getInstance().collection("halls");
        Hall hall = new Hall(hallName, size, branchString,f,HallBuilding);

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
        if (branchString == null || branchString.equals("Select Department")) {
            Log.d(TAG, "branchSelected: None");
            Toast.makeText(addHall.this,"Select Branch",Toast.LENGTH_SHORT).show();
            return false;

        } else if (HallBuilding == null | HallBuilding.equals("Select Building")) {
            Log.d(TAG, "branchSelected: None");
            Toast.makeText(addHall.this,"Select Building",Toast.LENGTH_SHORT).show();
            return false;

        } else {
            Log.d(TAG, "branchSelected: " + branchString);
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: ");
        if (parent.getId() == R.id.HallDepartment) {

        branchString = parent.getItemAtPosition(position).toString().trim();
        Toast.makeText(this, branchString, Toast.LENGTH_SHORT).show();
        } else if (parent.getId() == R.id.HallBuilding) {
            HallBuilding = parent.getItemAtPosition(position).toString().trim();
            Toast.makeText(this, HallBuilding, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");
        branchString = parent.getItemAtPosition(0).toString().trim();
        HallBuilding = parent.getItemAtPosition(0).toString().trim();
    }

}
