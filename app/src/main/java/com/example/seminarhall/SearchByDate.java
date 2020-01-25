package com.example.seminarhall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SearchByDate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private TextView textView;
    private Hall currHall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_date);
        textView = findViewById(R.id.dateText);
        mAuth = FirebaseAuth.getInstance();
//        textView.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        currHall = intent.getParcelableExtra("Hall Selected");
        textView.setText(currHall.getName());

        findViewById(R.id.bindCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        checkUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Reservation");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void checkUser()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date=dayOfMonth+"/"+month+"/"+year;
        textView.setVisibility(View.VISIBLE);
        textView.setText(date);
    }
}
