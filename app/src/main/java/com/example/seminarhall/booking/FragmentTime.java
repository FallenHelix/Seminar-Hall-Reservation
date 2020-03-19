package com.example.seminarhall.booking;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.seminarhall.Hall;
import com.example.seminarhall.R;
import com.example.seminarhall.ReservedHall;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentTime extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener  {
    private static final String TAG = "FragmentTime";
    public static final String Arg_start_date = "Start Date";
    private TextView startTime, endTime, selectedDate, itemText;
    private Button itemButton, bookHall;
    private static int id = -1;
    private EditText purpose;
    private Hall currHall;

    private int firstHour = 1000, firstMinute = 1000, secondHour = 1000, secondMinute = 1000;
    //Items for multiple Choice
    private Button mainList;
    private String[] listItems;
    private boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private List<String> SelectedDates;
    int stat = -1; //need to check for single dates

    //variables for clash check for single date
    List<Double> sTime;
    List<Double> eTime;
    double startHour=-1, endHour=-1;
    List<Integer> expandedList = new ArrayList<>();

    //Fragment listener
    //boolean for clash
    boolean clash;

    public FragmentTime() {

    }

    private boolean TimeCheck() {
        Log.d(TAG, "TimeCheck: ");
        if (expandedList.contains(startHour) || expandedList.contains(endHour)) {
            Log.d(TAG, "TimeCheck: error");

            return false;
        } else
            return true;

    }


    private void DateCheck() {
        final List<Integer> startTime = new ArrayList<>();
        final List<Integer> endTime = new ArrayList<>();
        Log.d(TAG, "Selected" + SelectedDates);
//        Log.d(TAG, "SingleDates" + singleDates);


        String status = FragmentCalendar.stat(SelectedDates.get(0));
        String id = FragmentCalendar.getSingleId(SelectedDates.get(0));
        Log.d(TAG, "Key " + id);
        Log.d(TAG, "Status: " + status);

        CollectionReference doc = FirebaseFirestore.getInstance().collection("Main");


        doc.whereArrayContains("days", SelectedDates.get(0)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot x : queryDocumentSnapshots) {
                    startTime.add((int)(long) ( x.get("startHour")));
                    endTime.add((int)(long)  (x.get("endHour")));
                }
                Log.d(TAG, "Start Time" + startTime);
                Log.d(TAG, "End Time"+endTime);
                expand(startTime, endTime);
            }
        });
    }

    private boolean expandedListReady=false;

    public void expand(List<Integer> startTime, List<Integer> endTime) {

        for (int i = 0; i < startTime.size(); i++) {
            for (int j = startTime.get(i) + 1; j <= endTime.get(i)-1; j++) {
                expandedList.add(j);
            }
        }
        itemText.setText("");

        Log.d(TAG, "expand list: "+expandedList);
        for (int i : expandedList) {
            itemText.append("-" + i);
        }
        expandedListReady=true;
    }


    public static FragmentTime newInstance(String start) {

        FragmentTime fragment = new FragmentTime();
        Bundle args = new Bundle();
        args.putString(Arg_start_date, "" + start);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clash=false;
        Log.d(TAG, "onCreate: ");
        Calendar c = Calendar.getInstance();
        String d = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        Log.d(TAG, "onCreate: " + d);
        sTime = new ArrayList<>();
        eTime = new ArrayList<>();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        setUpViews(view);
        return view;
    }

    //after getting


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");

        if (clash)
            getClashTimes();
    }


    private void getClashTimes() {
        sTime.clear();
        eTime.clear();
        Log.d(TAG, "getClashTimes: ");
        CollectionReference db1 = FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        CollectionReference db2 = FirebaseFirestore.getInstance().collection("Main/Reservation/Closed");
        db1.whereGreaterThan("endHour", 10)
                .whereEqualTo("hallId",currHall.getKey())
                .orderBy("endHour", Query.Direction.DESCENDING).get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
                            sTime.add((double) x.getDouble("startHour"));
                            eTime.add((double) x.getDouble("endHour"));
                        }
                        Log.d(TAG, "startHour" + sTime);
                        Log.d(TAG, "end Time " + eTime);
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
            }
        });
//        Task t2=db2.whereGreaterThan("endTime", 10)
//                .whereEqualTo("hallId",currHall.getKey())
//                .orderBy("endTime", Query.Direction.DESCENDING).get();



    }

    private boolean filterTime() {
        List<Double> t1 = new ArrayList<>(sTime);
        List<Double> t2= new ArrayList<>(eTime);
        //initial filter
        List<Integer> popList = new ArrayList<>();
        for (int i = 0; i < t1.size(); i++) {
            if (startHour > eTime.get(i)) {
                popList.add(i);
            }
        }
        for (int i : popList) {
            t1.remove(i);
            t2.remove(i);
        }

        for (int i = 0; i < sTime.size(); i++) {
            if ((endHour)>sTime.get(i)) {
                if (startHour+0.01 > eTime.get(i)) {
                    return true;
                }
                else
                {
                    Log.d(TAG, "filterTime: "+sTime.get(i)+" - "+eTime.get(i));
                    Log.d(TAG, "INput TIme"+startHour+" - "+endHour);
                    return false;//startHour is less
                }
//                if (endHour < eTime.get(i)) {//db has SuperSet of time i.e database has bigger time slots reserved
//                    return false;
//                } else if (endHour < eTime.get(i)) {
//                    return false;
//                }
            }
        }
        Log.d(TAG, "INput TIme"+startHour+" - "+endHour);
        return  true;
    }

    public void updateDate(List<String> s) {
        SelectedDates = new ArrayList<>(s); //get all the selected dates

        selectedDate.setText(s.get(0) + " - " + s.get(s.size() - 1));
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        int i = v.getId();
        id = i;
        if (i == R.id.StartTime) {
            showTimePickerDialog();

        } else if (i == R.id.EndTime) {
            showTimePickerDialog();
        } else if (i == R.id.b1) {
            multiChoiceDialog();
        } else if (i == R.id.button4) {

//            if (!mainCheck()) {
//                Toast.makeText(getContext(), "Pleasea Enter Purpose!!", Toast.LENGTH_SHORT).show();
//            } else {
//                reserveHall();
//            }
            if (filterTime()) {
                Toast.makeText(getContext(), "Valid Time bro", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getContext(),"Invalid Time bro",Toast.LENGTH_LONG).show();
        }

    }

    private void reserveHall() //adding to database
    {

        if (!TimeCheck()) {
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db;
        ReservedHall hall = new ReservedHall(currHall.getKey(), SelectedDates, startTime.getText().toString().trim(),
                endTime.getText().toString().trim(), user.getUid(), purpose.getText().toString().trim());
        hall.setStartHour(startHour);
        hall.setEndHour(endHour);
        if (hall.getStartDate() == null || hall.getEndDate() == null) {
            Toast.makeText(getContext(), "Error Occurred Try again later", Toast.LENGTH_SHORT).show();
            return;
        }

        db = FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Main/Reservation/Active");
        ref.add(hall).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "New request for Reservation has been created wih id: \n" +
                        documentReference.getId(), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Exception Occurred", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }


    private boolean mainCheck() {

        if (purpose.getText().toString().trim().length() == 0) {
            return false;
        } else return true;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        int hour;
        switch (id) {
            case R.id.StartTime:
                firstHour = hourOfDay;
                firstMinute = minute;
                if (checkValidTime()) {
                    startTime.setText(time);
                    startHour = hourOfDay+((double)minute/100);
                }
                break;
            case R.id.EndTime:
                secondHour = hourOfDay;
                secondMinute = minute;
                if (checkValidTime()) {
                    endTime.setText(time);
                    endHour = hourOfDay+((double)minute/100);
                }
        }
    }

    private boolean checkValidTime() {
        Log.d(TAG, "checkValidTime: ");
        if (SelectedDates.size() < 2 && firstMinute != -1 && secondMinute != -1) {
            if (firstHour > secondHour) {
                Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                return false;

            } else if (firstHour == secondHour) {
                if (firstMinute >= secondMinute) {
                    Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    System.out.println("valid 3");
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void temp()
    {

        CollectionReference db = FirebaseFirestore.getInstance().collection("Main/Reservation/Active");
        db.whereGreaterThan("endTime", 10)
                .whereEqualTo("hallId",currHall.getKey())
                .orderBy("endTime", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

    private void multiChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Selected Items");
        builder.setMultiChoiceItems(listItems, checkItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    //if already added then remove
                    if (!mUserItems.contains(which)) {
                        mUserItems.add(which);
                    } else {
                        mUserItems.remove(which);
                    }
                }
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    item = item + listItems[mUserItems.get(i)];

                    if (i != mUserItems.size() - 1) {
                        item = item + "\n";
                    }
                }

                itemText.setText(item);
            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkItems.length; i++)
                    checkItems[i] = false;
                mUserItems.clear();
                itemText.setText("");

            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public void setHall(Hall h)
    {
        currHall=h;
    }

    public void setClash(boolean stat) {
        clash=stat;
    }

    public void openDialog() {
        DialogBox exampleDialog = new DialogBox();
        exampleDialog.show(getChildFragmentManager(), "example dialog");
    }
}
