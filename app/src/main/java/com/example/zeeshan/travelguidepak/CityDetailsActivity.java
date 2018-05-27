package com.example.zeeshan.travelguidepak;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class CityDetailsActivity extends AppCompatActivity {

    private Toolbar cityToolbar;
    private FloatingActionButton addPlaceButton;
    private City city;
    private BottomNavigationView cityBottomNav;

    private InfoFragment infoFragment;
    private PlacesFragment placesFragment;
    private MapFragment mapFragment;
    private FirebaseAuth mAuth;


    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);

        // Getting the City
        Intent intent =  getIntent();
        if(intent != null){ // When Coming from MainActivity.
            city = (City) getIntent().getSerializableExtra("city");
        }

        cityBottomNav = findViewById(R.id.cityBottomNavbar);

        cityToolbar = findViewById(R.id.city_toolbar);

        setSupportActionBar(cityToolbar);
        getSupportActionBar().setTitle(city.getName());


            // Fragments
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("city", city);
            infoFragment = new InfoFragment();
            infoFragment.setArguments(bundle1);



            Bundle bundle2 = new Bundle();
            bundle2.putString("cityName", city.getName());
            placesFragment = new PlacesFragment();
            placesFragment.setArguments(bundle2);


            Bundle bundle3 = new Bundle();
            bundle3.putSerializable("city", city);
            mapFragment = new MapFragment();
            mapFragment.setArguments(bundle3);


            fragmentChanging(infoFragment); // for the default

            addPlaceButton = findViewById(R.id.add_place_btn);

            addPlaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newPlaceIntent = new Intent(CityDetailsActivity.this, AddPlaceActivity.class);
                    newPlaceIntent.putExtra("city", city);
                    startActivity(newPlaceIntent);
                }
            });

            cityBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){

                        case R.id.bottom_info_btn:
                            fragmentChanging(infoFragment);
                            return true;

                        case R.id.bottom_places_btn:
                            fragmentChanging(placesFragment);
                            return true;

                        case R.id.bottom_map_btn:
                            if(isServicesAvailable()){
                                fragmentChanging(mapFragment);
                            }
                            return true;

                        default:
                            return false;

                    }
                }
            });

    }

    public boolean isServicesAvailable(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CityDetailsActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Toast.makeText(CityDetailsActivity.this, "User can load map", Toast.LENGTH_LONG).show();
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            // error can be resolved(some version error)
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(CityDetailsActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(CityDetailsActivity.this, "User can't load Map", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    // Forma de Cambiar los fragmentos, unos por otros.
    private void fragmentChanging(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_wrapper, fragment);
        fragmentTransaction.commit();
    }
}
