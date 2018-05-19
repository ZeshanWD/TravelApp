package com.example.zeeshan.travelguidepak;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private List<City> cityList;
    private CitiesRecyclerAdapter recyclerAdapter;
    private FirebaseFirestore firebaseFirestore;
    private String currentUserId;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mainToolbar = findViewById(R.id.main_toolbar);

        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Top Destinations");

        cityList = new ArrayList<City>();
        cityList = getAllCities();

        recyclerView = findViewById(R.id.cityRecycler);

        recyclerAdapter = new CitiesRecyclerAdapter(cityList);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser(); // el usuario que esta logueado.
        if(currentUser == null){
            sendToLogin();
        } else { // si esta logueado, miramos si ha rellenado su informacion.
            currentUserId = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().exists()){ // miramos si ha acabado con el setup o no(Eziste su info o no)

                            Intent intentSetup = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(intentSetup);
                            finish();

                        }
                    }
                }
            });
        }
    }

    private List<City> getAllCities(){
        City lahore = new City("Lahore", R.drawable.lahore);
        City islamabad = new City("Islamabad", R.drawable.islamabad);
        City karachi = new City("Karachi", R.drawable.karachi);
        City multan = new City("Multan", R.drawable.multan);
        City peshawar = new City("Peshawar", R.drawable.peshawar);
        City rawalpindi = new City("Rawalpindi", R.drawable.rawalpindi);
        List<City> lista = new ArrayList<City>();

        lista.add(lahore);
        lista.add(islamabad);
        lista.add(karachi);
        lista.add(multan);
        lista.add(peshawar);
        lista.add(rawalpindi);

        return lista;
    }



    private void sendToLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // para que el usuario no vuelva atras.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout_btn:

                logout();

                return true;
            case R.id.action_settings_btn:

                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);

                return true;

            default:
                return false;
        }

    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();
    }
}
