package com.example.seminarhall;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class Booking extends AppCompatActivity implements HorizontalAdapter.ItemClickListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    DatabaseReference databaseReference;
    private Hall currHall;
    private Button reserve;
    TextView txt1,txt2,hallName,itemSelected;
    private String TAG = "Booking Activity";
    private int currentId;
    private ArrayList<String> dates;
    private ArrayList<String> days;
    HorizontalAdapter adapter;

    //var for multiple choice
    private Button mainList;
    String[] listItems;
    boolean[] checkItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getApplicationContext(), "You Have clicked"+position, Toast.LENGTH_SHORT).show();

        adapter.setSelected(position);
        adapter.notifyDataSetChanged();
    }

    private enum weekDays {Sunday,Monday,Tuesday,Wednesday,Thursday,Friday, Saturday}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Started");
        setViews();
        listItems = getResources().getStringArray(R.array.Services);
        checkItems = new boolean[listItems.length];



        Log.d(TAG,"On Clikc Listner working Initiated");
        databaseReference= FirebaseDatabase.getInstance().getReference("Reservation");
    }

    private void multiChoiceDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Booking.this);
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
                        item = item + ",";
                    }
                }

                itemSelected.setText(item);
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
                itemSelected.setText("");

            }
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();

    }


    private void setViews()
    {
        setContentView(R.layout.activity_booking);

        //setting up Views;
        reserve = findViewById(R.id.button2);
        txt1 = (TextView) findViewById(R.id.StartTime);
        txt2 = (TextView) findViewById(R.id.EndTime);
        hallName = findViewById(R.id.HallName);
        itemSelected = findViewById(R.id.items);

        //onCLickListener
        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);
        reserve.setOnClickListener(this);
        findViewById(R.id.b1).setOnClickListener(this);
        setUpRecyclerView();
        //getting Selected hall Details
        Intent intent=getIntent();
        currHall = intent.getParcelableExtra("Hall Selected");
        hallName.setText(currHall.getName());
        hallName.setTypeface(null, Typeface.BOLD);
        hallName.setPaintFlags(hallName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        Log.d(TAG, "Set Views");


    }

    private void setUpRecyclerView() {
        Log.d(TAG, "SetupRecyclerViews");

        days = new ArrayList<>();
        dates = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HorizontalAdapter(this, dates, days);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener( this);
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
            days.add(""+ weekDays.values()[(temp-1)]);
            c=Calendar.getInstance();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }
    private void checkUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, user.getUid(), Toast.LENGTH_SHORT).show();
        }
    }



    private void showTimePickerDialog()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        String time = DateFormat.getTimeInstance().format(c.getTime());

        switch (currentId) {
            case R.id.StartTime:
                txt1.setText(time);
                break;
            case R.id.EndTime:
                txt2.setText(time);
        }

    }

    @Override
    public void onClick(View v) {
        int i=v.getId();
        currentId=i;
        if (i == R.id.StartTime) {
            showTimePickerDialog();

        } else if (i == R.id.EndTime) {
            showTimePickerDialog();
        } else if (i == R.id.button2) {
            if(!mainCheck())
            {
                Toast.makeText(this, "Pleasea Enter Purpose!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                reserveHall();
            }
        } else if (i == R.id.b1) {
            multiChoiceDialog();
        }
    }

    private boolean mainCheck()
    {
        EditText text = findViewById(R.id.editText);
        if (text.getText().toString().trim().length() == 0) {
            return false;
        }
        else return true;
    }

    private void reserveHall()
    {


        EditText text = findViewById(R.id.editText);
        String purpose=text.getText().toString().trim();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Reserved");
//        String id = databaseReference.push().getKey();
//
        FirebaseFirestore db;
        ReservedHall reservedHall = new ReservedHall(currHall.getKey(), "Date Needed", txt1.getText().toString().trim(),
                txt2.getText().toString().trim(), user.getUid(),purpose);
//        databaseReference.child(id).setValue(reservedHall);


          db=FirebaseFirestore.getInstance();
//        CollectionReference notebookRef = db.collection("Reservation");
//        notebookRef.document("In_Progress").collection("progress").add(reservedHall);

         CollectionReference notebookRef = db.collection("Reservation");
        notebookRef.document("In_Progress")
                .collection("Upcoming").add(reservedHall);

        Toast.makeText(this,"Done reservation",Toast.LENGTH_SHORT).show();
    }
}