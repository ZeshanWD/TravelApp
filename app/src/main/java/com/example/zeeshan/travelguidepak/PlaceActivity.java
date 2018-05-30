package com.example.zeeshan.travelguidepak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PlaceActivity extends AppCompatActivity {

    private Toolbar placeToolbar;
    private ImageView placeImage;
    private TextView placeDesc;


    private Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Intent intent = getIntent();
        if(intent != null){
            place = (Place) intent.getSerializableExtra("place");
        }

        placeToolbar = findViewById(R.id.place_toolbar);

        setSupportActionBar(placeToolbar);
        getSupportActionBar().setTitle(place.getTitle());


        placeImage = (ImageView) findViewById(R.id.place_image);
        placeDesc = (TextView) findViewById(R.id.place_desc);

        placeDesc.setText(place.getDescription());

        Glide.with(this).load(place.getImage()).into(placeImage);


    }
}
