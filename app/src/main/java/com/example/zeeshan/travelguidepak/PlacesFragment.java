package com.example.zeeshan.travelguidepak;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment {

    private RecyclerView placesRecycler;

    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places, container, false);

        placesRecycler = getActivity().findViewById(R.id.places_recycler_view);

        // Inflate the layout for this fragment
        return view;
    }

}
