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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class FragmentTime extends Fragment implements View.OnClickListener,TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "FragmentTime";
    public static final String Arg_start_date = "Start Date";
    private TextView startTime, endTime,selectedDate,itemText;
    private Button itemButton,bookHall;
    private static int id=-1;
    private EditText purpose;

    //Items for multiple Choice
    private Button mainList;
    String[] listItems;
    boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();


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
        String mText=Reserve.mString;
        updateDate(mText);
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

    private void reserveHall()
    {

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Reserved");
//        String id = databaseReference.push().getKey();
//
        FirebaseFirestore db;
        Hall currHall=Reserve.getHall();
        ReservedHall reservedHall = new ReservedHall(currHall.getKey(), selectedDate.getText().toString().trim(), startTime.getText().toString().trim(),
                endTime.getText().toString().trim(), user.getUid(),purpose.getText().toString().trim());
//        databaseReference.child(id).setValue(reservedHall);


        db=FirebaseFirestore.getInstance();
//        CollectionReference notebookRef = db.collection("Reservation");
//        notebookRef.document("In_Progress").collection("progress").add(reservedHall);

        CollectionReference notebookRef = db.collection("Reservation");
        notebookRef.document("In_Progress")
                .collection("Upcoming").add(reservedHall);


        Toast.makeText(getActivity(),"Done reservation",Toast.LENGTH_SHORT).show();
    }
    private boolean mainCheck()
    {

        if (itemText.getText().toString().trim().length() == 0) {
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
                startTime.setText(time);
                break;
            case R.id.EndTime:
                endTime.setText(time);
        }
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
