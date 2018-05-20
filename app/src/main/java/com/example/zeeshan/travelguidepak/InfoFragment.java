package com.example.zeeshan.travelguidepak;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    private ImageView cityImage;
    private TextView cityName;
    private TextView cityDesc;

    private City city;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        cityImage = (ImageView) view.findViewById(R.id.info_city_image);
        cityName = (TextView) view.findViewById(R.id.info_city_name);
        cityDesc = (TextView) view.findViewById(R.id.info_city_desc);

        // Get the Bundle
        if(getArguments() != null){
            city = (City) getArguments().getSerializable("city");
        }

        // display Info

        Glide.with(container.getContext()).load(city.getImage()).into(cityImage);

        cityName.setText(city.getName());




         return view;
    }

}
