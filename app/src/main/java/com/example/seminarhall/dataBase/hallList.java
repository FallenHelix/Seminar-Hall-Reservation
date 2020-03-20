package com.example.seminarhall.dataBase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.seminarhall.Hall;
import com.example.seminarhall.LogIn.MainActivity;
import com.example.seminarhall.R;
import com.example.seminarhall.booking.Reserve;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.api.LogDescriptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class hallList extends AppCompatActivity implements HallListAdapter.ItemClickListener,NavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = "hallList";
    HallListAdapter adapter;
    DatabaseReference databaseReference;
    ArrayList<Hall> halls;
    RecyclerView recyclerHallList;
    private boolean FirstTime=true;
    private DrawerLayout drawer;
    CollectionReference db = FirebaseFirestore.getInstance().collection("halls");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpView();
    }

    private void setUpView()
    {
        recyclerHallList = (RecyclerView)findViewById(R.id.RecView);
        recyclerHallList.setLayoutManager(new LinearLayoutManager(this));
        halls=new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Halls");
        //Side Drawer varibales

        drawer = findViewById(R.id.drawer_layout);
    }



    @Override
    protected void onStart() {
        super.onStart();
        db.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                halls.clear();
                for (QueryDocumentSnapshot x : queryDocumentSnapshots) {
                    Hall hall = x.toObject(Hall.class);
                    hall.setKey(x.getId());
                    halls.add(hall);
                }
                    adapter = new HallListAdapter(hallList.this, halls);
                    recyclerHallList.setAdapter(adapter);
                    start();
                
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");

            }
        });

    }
    private void start()
    {


        adapter.setClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toast.makeText(this,"OnCreate Options Menu",Toast.LENGTH_SHORT).show();
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.hall_menu, menu);


        MenuItem searchItem = menu.findItem(R.id.action_search);
        /////
        Drawable drawable = searchItem.getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.white_greyish));
        searchItem.setIcon(drawable);
        ///////
        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView )searchItem.getActionView();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView .OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    int x = Integer.parseInt(newText);
                } catch (Exception e) {
                    ExceptionFunction();
                    return true;
                }
                if((adapter!=null&& adapter.getItemCount()!=0)||FirstTime!=true) {
                    adapter.getFilter().filter(newText);
                    FirstTime=false;
                }
                return false;
            }
        });
        return true;
    }

    private void ExceptionFunction()
    {
        Toast.makeText(this, "Please Input Only Number",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), Reserve.class);
        intent.putExtra("Hall Selected", halls.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i=item.getItemId();

        if (i == R.id.nav_profile) {
            Intent intent = new Intent(hallList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}

