package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class hallList extends AppCompatActivity {

    HallListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_list);
        String[] languages={"Java", "JavaScrip", "C++", "Python", "XML", "adfasd", "adfadfas"};
        RecyclerView recyclerHallList = (RecyclerView)findViewById(R.id.RecView);
        recyclerHallList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HallListAdapter( languages);

        recyclerHallList.setAdapter(adapter);
    }
}

