package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Calendar;

public class HCalendar extends AppCompatActivity {

    private static String TAG="HCalendar";


    //variable
    private ArrayList<String> dates;
    private ArrayList<String> days;
    private enum nameOfDay{Sunday,Monday,Tuesday,Wednesday,Thursday,Friday, Saturday,}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcalendar);
        setUpRecyclerView();

        //setUPView
        getTime();
    }

    private void getTime() {
        Calendar c=Calendar.getInstance();
        int temp;
        for (int i = 0; i < 8; i++) {
            c.add(Calendar.DATE,i);
            temp = c.get(Calendar.DAY_OF_MONTH);
            dates.add("" + temp);
            temp = c.get(Calendar.DAY_OF_WEEK);

//            days.add("" + nameOfDays.values()[temp]);
            days.add(""+nameOfDay.values()[(temp-1)]);
            c=Calendar.getInstance();
        }
    }

    private void setUpRecyclerView() {
        Log.d(TAG, "SetupRecyclerViews");

        days = new ArrayList<>();
        dates = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        HorizontalAdapter adapter = new HorizontalAdapter(this, dates, days);
        recyclerView.setAdapter(adapter);
    }
}
