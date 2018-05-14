package com.example.zeeshan.travelguidepak;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesFragment extends Fragment {

    private List<Place> listaSitios;
    private RecyclerView placesRecycler;
    private FirebaseFirestore firebaseFirestore;
    private PlacesAdapter placesAdapter;

    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the Bundle
        String cityName = getArguments().getString("cityName");



        View view = inflater.inflate(R.layout.fragment_places, container, false);

        listaSitios = new ArrayList<>();

        placesRecycler = view.findViewById(R.id.places_recycler_view);

        placesAdapter = new PlacesAdapter(listaSitios);

        placesRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        placesRecycler.setAdapter(placesAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();

        //firebaseFirestore.collection("Places").whereEqualTo("city",'Lahore').



        firebaseFirestore.collection("Places").whereEqualTo("city", cityName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        Place place = doc.getDocument().toObject(Place.class);
                        listaSitios.add(place);

                        placesAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
