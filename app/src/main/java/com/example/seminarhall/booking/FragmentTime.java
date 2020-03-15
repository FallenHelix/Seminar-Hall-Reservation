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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.timessquare.CalendarPickerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FragmentTime extends Fragment implements View.OnClickListener,TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "FragmentTime";
    public static final String Arg_start_date = "Start Date";
    private TextView startTime, endTime,selectedDate,itemText;
    private Button itemButton,bookHall;
    private static int id=-1;
    private EditText purpose;
    Date sCalendar=null,eCalendar=null;

    //Items for multiple Choice
    private Button mainList;
    String[] listItems;
    boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    private List<String> SelectedDates;


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
        Calendar c=Calendar.getInstance();
        String d = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());
        Log.d(TAG, "onCreate: "+d);
    }

    private void setUpViews(View view) {
        Log.d(TAG, "setUpViews: ");
        selectedDate = (TextView) view.findViewById(R.id.Date);
        startTime = (TextView) view.findViewById(R.id.StartTime);
        endTime=(TextView) view.findViewById(R.id.EndTime);
        itemButton = (Button) view.findViewById(R.id.b1);
        itemText = (TextView) view.findViewById(R.id.items);
        bookHall=(Button)view.findViewById(R.id.button4) ;
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
        SelectedDates = Reserve.getSelectedDates();
        if(SelectedDates.size()!=0) //for empty case
        updateDate(SelectedDates.get(0));
    }

    private void updateDate(String mText) {
        selectedDate.setText(mText);
    }

    private void showTimePickerDialog()
    {


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
        int i=v.getId();
        id=i;
        if (i == R.id.StartTime) {
            showTimePickerDialog();

        } else if (i == R.id.EndTime) {
            showTimePickerDialog();
        }
        else if (i == R.id.b1) {
            multiChoiceDialog();
        }
        else if (i == R.id.button4) {

            if(!mainCheck())
            {
                Toast.makeText(getContext(), "Pleasea Enter Purpose!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                reserveHall();
            }
        }

    }

    private void reserveHall() //adding to database
    {

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db;
        Hall currHall=Reserve.getHall();
        ReservedHall hall = new ReservedHall(currHall.getKey(), SelectedDates, startTime.getText().toString().trim(),
                endTime.getText().toString().trim(), user.getUid(), purpose.getText().toString().trim());

        if (hall.getStartDate() == null||hall.getEndDate()==null) {
            Toast.makeText(getContext(), "Error Occurred Try again later", Toast.LENGTH_SHORT).show();
            return;
        }

        db=FirebaseFirestore.getInstance();
        CollectionReference ref = db.collection("Main/Reservation/Active");
        ref.add(hall).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"New request for Reservation has been created wih id: \n"+
                        documentReference.getId(),Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Exception Occurred",Toast.LENGTH_LONG).show();
                Log.d(TAG, "onFailure: " + e);
            }
        });
    }


    private boolean mainCheck()
    {

        if (purpose.getText().toString().trim().length() == 0) {
            return false;
        }
        else return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        switch (id) {
            case R.id.StartTime:
                    sCalendar = c.getTime();
                    startTime.setText(time);
                    checkValidTime();

                break;
            case R.id.EndTime:
                c.add(Calendar.DATE,SelectedDates.size()-1);
                    eCalendar=c.getTime();
                    endTime.setText(time);
                    checkValidTime();
        }
    }

    private void checkValidTime() {

    }

    private void multiChoiceDialog()
    {
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
                String item="";
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
                    checkItems[i]=false;
                mUserItems.clear();
                itemText.setText("");

            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }


}
