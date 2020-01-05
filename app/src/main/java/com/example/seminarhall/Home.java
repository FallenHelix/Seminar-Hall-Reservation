package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    List<Artist> artists;

    DatabaseReference databaseArtists;
    private TextView Hall;
    private TextView status;
    private String TAG="Database access";
    private RecyclerView mRecyclerView;
    private FirebaseDatabase FirebaseDatabaseHelper;
    ListView listViewArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Hall = findViewById(R.id.Hall_Name);
//        status = findViewById(R.id.cap);
        databaseArtists = FirebaseDatabase.getInstance().getReference("Artists");
        if (databaseArtists == null) {
            Toast.makeText(this,"NULl Reference",Toast.LENGTH_LONG).show();
        }
        artists = new ArrayList<>();
        listViewArtists = (ListView) findViewById(R.id.listViewArtist);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list
                    artists.add(artist);
                }

                //creating adapter
                artistList artistAdapter = new artistList(Home.this, artists);
                //attaching adapter to the listview
                listViewArtists.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
