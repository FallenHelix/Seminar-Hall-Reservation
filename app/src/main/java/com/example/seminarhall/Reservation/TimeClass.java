package com.example.seminarhall.Reservation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seminarhall.R;
import com.example.seminarhall.booking.FragmentTime;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeClass extends Fragment implements View.OnClickListener {
    private static final String TAG = "TimeClass";
    private FragmentTimeListener listener;
    public static final String Arg_start_date = "Start Date";
    private TextView startTime, endTime, selectedDate, itemText;
    private Button itemButton, bookHall;
    private static int id = -1;
    private EditText purpose;
    private String[] listItems;
    private boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private List<String> SelectedDates;

    TimeClass()
    {

    }

    public static FragmentTime newInstance(String start) {

        FragmentTime fragment = new FragmentTime();
        Bundle args = new Bundle();
        args.putString(Arg_start_date, "" + start);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean check=listener.dateClashChecker();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        setUpViews(view);

        return view;
    }

    private void setUpViews(View view) {
        Log.d(TAG, "setUpViews: ");
        selectedDate = (TextView) view.findViewById(R.id.Date);
        startTime = (TextView) view.findViewById(R.id.StartTime);
        endTime = (TextView) view.findViewById(R.id.EndTime);
        itemButton = (Button) view.findViewById(R.id.b1);
        itemText = (TextView) view.findViewById(R.id.items);
        bookHall = (Button) view.findViewById(R.id.button4);
        purpose = view.findViewById(R.id.editText);

        //set On Click Listener;
        selectedDate.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        itemButton.setOnClickListener(this);
        bookHall.setOnClickListener(this);

        //Loading Items From Strings.xml to list
        listItems = getResources().getStringArray(R.array.Services);
        checkItems = new boolean[listItems.length];
    }

    @Override
    public void onClick(View v) {

    }


    public interface FragmentTimeListener {
        boolean dateClashChecker();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentTimeListener) {
            listener=(FragmentTimeListener)context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
