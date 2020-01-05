package com.example.seminarhall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddArtist extends AppCompatActivity {
    EditText EditTextName;
    EditText EditTextGenre;
    Button addToDb;
    DatabaseReference databaseArtists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);
        EditTextGenre = findViewById(R.id.a_genre);
        EditTextName = findViewById(R.id.a_Name);
        addToDb = findViewById(R.id.Add_a);
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });

    }

    private void addArtist() {
        //getting the values to save
        String name = EditTextName.getText().toString().trim();
        String genre = EditTextGenre.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            Artist artist = new Artist(id, name, genre);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting edittext to blank again
            EditTextGenre.setText("");
            EditTextName.setText("");


            //displaying a success toast
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
