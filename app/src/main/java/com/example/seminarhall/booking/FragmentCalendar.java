package com.example.seminarhall.booking;

import android.content.Context;
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
    String SelectedDate;
    private OnFragmentInteractionListener mListener;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        setUpCalendar(view);
        return view;
    }

    private void setUpCalendar(View view) {
        calendarPickerView = view.findViewById(R.id.calendar);
        Date today = new Date();
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, 5);
        calendarPickerView.init(today, c.getTime()).withSelectedDate(today).inMode(CalendarPickerView.SelectionMode.SINGLE);
        calendarPickerView.setOnDateSelectedListener(this);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.sf_display_medium);

//        calendarPickerView.setDateTypeface(typeface);
        calendarPickerView.setTypeface(typeface);
    }


    @Override
    public void onDateSelected(Date date) {
        SelectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        Toast.makeText(getActivity(), SelectedDate, Toast.LENGTH_LONG).show();
        sendBack(SelectedDate);
//        updateable.update();

    }

    @Override
    public void onDateUnselected(Date date) {

    }

    public void sendBack(String sendBackText) {
        if (mListener != null) {
            mListener.onFragmentInteraction(sendBackText);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String sendBackText);

    }

}
