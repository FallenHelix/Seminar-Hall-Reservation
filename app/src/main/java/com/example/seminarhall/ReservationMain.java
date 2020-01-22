package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_main);
//        Date today= new Date();
//        Calendar nextYear=Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 1);
//        CalendarView datePicker = findViewById(R.id.calendarView);
//        datePicker.setMinDate(1);
//
//        datePicker.init(today, nextYear.getTime()).withSelectedDate(today);
//
//        datePicker.setons
//        datePicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Date date) {
//                String selectDate= DateFormat.getDateInstance(DateFormat.FULL).format(date);
//                Toast.makeText(ReservationMain.this,selectDate,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDateUnselected(Date date) {
//
//            }
//        });
    }
}
