package com.example.seminarhall.booking;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.seminarhall.R;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentCalendar extends Fragment implements CalendarPickerView.OnDateSelectedListener{

    private static final String TAG = "FragmentCalendar";
    CalendarPickerView calendarPickerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        setUpCalendar(view);
//        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Date date) {
//
//            }
//
//            @Override
//            public void onDateUnselected(Date date) {
//
//            }
//        });
        return view;
    }

    private void setUpCalendar(View view) {
        calendarPickerView = view.findViewById(R.id.calendar);
        Date today = new Date();
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, 5);
        calendarPickerView.init(today, c.getTime()).withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.RANGE);
        calendarPickerView.setOnDateSelectedListener(this);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.sf_display_medium);

//        calendarPickerView.setDateTypeface(typeface);
        calendarPickerView.setTypeface(typeface);
    }


    @Override
    public void onDateSelected(Date date) {
        String SelectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        Toast.makeText(getActivity(), SelectedDate, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDateUnselected(Date date) {

    }

}
