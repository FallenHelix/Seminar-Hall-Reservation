package com.example.seminarhall.booking;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.seminarhall.R;

import java.util.Date;



public class FragmentTime extends Fragment  {
    private static final String TAG = "FragmentTime";
    public static final String Arg_start_date = "Start Date";
    TextView textView;


    //Fragment listener

    public FragmentTime()
    {

    }

    public static FragmentTime newInstance(String start)
    {

        FragmentTime fragment = new FragmentTime();
        Bundle args = new Bundle();
        args.putString(Arg_start_date,""+start);
        fragment.setArguments(args);
        return fragment;
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
        Button b = view.findViewById(R.id.button);
        textView = view.findViewById(R.id.T1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Button pressed in 2nd' Activity", Toast.LENGTH_SHORT).show();

            }
        });
        textView.setText("Select Date First");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
        String mText=Reserve.mString;
        updateDate(mText);
    }

    private void updateDate(String mText) {
        textView.setText(mText);
    }


}
