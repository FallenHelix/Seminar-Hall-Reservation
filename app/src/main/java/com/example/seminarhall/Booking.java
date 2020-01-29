package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Booking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private Hall currHall;
    private Button calendarButton;
    private TextView hallName;
    InputFilter timeFilter;
    TextView txt1,txt2;
    private String LOG_TAG = "Booking Activity";
    private boolean doneOnce = false;//USE?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate Started");
        setViews();
        setUpFilter();

        Log.d(LOG_TAG,"On Clikc Listner working Initiated");
        databaseReference= FirebaseDatabase.getInstance().getReference("Reservation");
    }

    private void setViews()
    {
        setContentView(R.layout.activity_booking);
        calendarButton= findViewById(R.id.bindCalendar);
        calendarButton.setOnClickListener(this);
        txt1 = (TextView) findViewById(R.id.StartTime);
        txt1.setOnClickListener(this);
        txt2 = (TextView) findViewById(R.id.EndTime);
        txt2.setOnClickListener(this);
        hallName = findViewById(R.id.HallName);

        mAuth = FirebaseAuth.getInstance();

        Intent intent=getIntent();
        currHall = intent.getParcelableExtra("Hall Selected");
        hallName.setText(currHall.getName());
        hallName.setTypeface(null, Typeface.BOLD);
        hallName.setPaintFlags(hallName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        Log.d(LOG_TAG, "Set Views");


    }

    private void setUpFilter() {
        timeFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                if (source.length() == 0) {
                    return null;// deleting, keep original editing
                }
                String result = "";
                result += dest.toString().substring(0, dstart);
                result += source.toString().substring(start, end);
                result += dest.toString().substring(dend, dest.length());

                if (result.length() > 5) {
                    return "";// do not allow this edit
                }
                boolean allowEdit = true;
                char c;
                if (result.length() > 0) {
                    c = result.charAt(0);
                    allowEdit &= (c >= '0' && c <= '2');
                }
                if (result.length() > 1) {
                    c = result.charAt(1);
                    if (result.charAt(0) == '0' || result.charAt(0) == '1')
                        allowEdit &= (c >= '0' && c <= '9');
                    else
                        allowEdit &= (c >= '0' && c <= '3');
                }
                if (result.length() > 2) {
                    c = result.charAt(2);
                    allowEdit &= (c == ':');
                }
                if (result.length() > 3) {
                    c = result.charAt(3);
                    allowEdit &= (c >= '0' && c <= '5');
                }
                if (result.length() > 4) {
                    c = result.charAt(4);
                    allowEdit &= (c >= '0' && c <= '9');
                }
                return allowEdit ? null : "";
            }

        };
//        txt1.setFilters(new InputFilter[]{timeFilter});
//        txt2.setFilters(new InputFilter[]{timeFilter});
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
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

    private void showTimePickerDialog()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c=Calendar.getInstance();
        c.set(year, month, dayOfMonth);

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        calendarButton.setText(date);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        String time = DateFormat.getTimeInstance().format(c.getTime());
        txt1.setText(time);
    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        if (i == R.id.StartTime) {
            showTimePickerDialog();

        } else if (i == R.id.EndTime) {
            showTimePickerDialog();

        } else if (i == R.id.bindCalendar) {
            showDatePickerDialog();
        }
    }
}