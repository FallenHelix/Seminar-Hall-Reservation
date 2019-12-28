package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class Home extends AppCompatActivity {

    private TextView Hall;
    private TextView status;
    private String TAG="Database access";
    private RecyclerView mRecyclerView;
    private FirebaseDatabase FirebaseDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Hall = findViewById(R.id.Hall_Name);
        status = findViewById(R.id.cap);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_books);
         new databaseHelper().Readhalls(new databaseHelper.DataStatus() {
             @Override
             public void DataIsLoaded(List<com.example.seminarhall.Hall> halls, List<String> keys) {
                 new RecyclerView_Config().setConfig(mRecyclerView, Home.this,halls,keys);
             }

             @Override
             public void DataIsInserted() {

             }

             @Override
             public void DataIsUpdated() {

             }

             @Override
             public void dataIsDeleted() {

             }
         });

    }
}
