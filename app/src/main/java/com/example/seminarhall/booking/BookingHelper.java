package com.example.seminarhall.booking;

import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingHelper  {
    private static final String TAG = "BookingHelper";
    List<String> singleDates;
    CalendarPickerView calendarPickerView;
    List<Date> bookedDates=new ArrayList<>();
    Context context;

    BookingHelper()
    {
        singleDates=new ArrayList<>();
    }
    public void setInfo(List<String> list) {
        singleDates.addAll(list);
        toDatesArray();

    }


    public List<CalendarCellDecorator> setCellDecorator() {
        CalendarCellDecorator decorator = new CalendarCellDecorator() {
            @Override
            public void decorate(CalendarCellView cellView, Date date) {
                Log.d(TAG, "decorate: Function Called");
                if(bookedDates.contains(date)) {
                    Log.d(TAG, "Clash");
//                    Log.d(TAG, "decorate: " + date);
                    cellView.setBackgroundColor(Color.MAGENTA);
                    cellView.setSelectable(false);
                    return;
                }
            }
        };
        List<CalendarCellDecorator> decoratorList = new ArrayList<>();
        decoratorList.add(decorator);

        return decoratorList;
    }


    private void func()
    {
        double t=  3.14;
        CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        db.whereGreaterThan("endTime", t)
                .whereLessThan("startTime",t)
                .orderBy("bookingDate", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
                    Log.d(TAG, "onSuccess: " + x);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
            }
        });
    }


    public void toDatesArray() {
        Log.d(TAG, "toDatesArray: "+singleDates.size());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        for (int i = 0; i < singleDates.size(); i++) {
            Date t = null;
            Log.d(TAG, "SIngle date"+singleDates.get(i));
            try {
                t = sdf.parse(singleDates.get(i));
                bookedDates.add(t);
            } catch (ParseException e) {
                Log.d(TAG, "Exception In Dates");
                return;
            }
        }
        for (Date x : bookedDates) {
            Log.d(TAG, x.toString());
        }

//        setCellDecorator();
    }



}
