package com.example.zeeshan.travelguidepak;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

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
    private FirebaseAuth mAuth;

    private DocumentSnapshot lastVisible;
    private Boolean firstPageLoaded = true;

    public PlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places, container, false);

        // Get the Bundle
        final String cityName = getArguments().getString("cityName");


        listaSitios = new ArrayList<>();
        placesRecycler = view.findViewById(R.id.places_recycler_view);

        mAuth = FirebaseAuth.getInstance();

        placesAdapter = new PlacesAdapter(listaSitios);

        placesRecycler.setLayoutManager(new LinearLayoutManager(container.getContext()));
        placesRecycler.setAdapter(placesAdapter);
        placesRecycler.setHasFixedSize(true);


        if(mAuth.getCurrentUser() != null){
            firebaseFirestore = FirebaseFirestore.getInstance();

            placesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean llegadoFinal = !recyclerView.canScrollVertically(1);

                    if(llegadoFinal){
                        refetch(cityName);
                    }

                }
            });


            Query consulta = firebaseFirestore.collection("Places").whereEqualTo("city", cityName).limit(3);

            consulta.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                // Get the last visible document

                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(e != null){
                        sendToLogin();
                    }

                    if (!documentSnapshots.isEmpty()) { // para asegurarnos que no haga nada si no hay nada en la base de datos.
                        if(firstPageLoaded){
                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() -1);
                            listaSitios.clear();
                        }

                        for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                            if(doc.getType() == DocumentChange.Type.ADDED){

                                String placeId = doc.getDocument().getId();


                                Place place = doc.getDocument().toObject(Place.class).withId(placeId);

                               if(firstPageLoaded){
                                    listaSitios.add(place);
                                } else {
                                    // Pondra los nuevos sitios al principio.
                                   listaSitios.add(0, place);
                               }

                                placesAdapter.notifyDataSetChanged();
                            }
                        }

                        firstPageLoaded = false;

                    }

                }
            });
        }



        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void sendToLogin(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void refetch(String name){

        if(mAuth.getCurrentUser() != null){

            Query nextQuery = firebaseFirestore.collection("Places").whereEqualTo("city", name)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(e != null){
                        sendToLogin();
                    }

                    if(!documentSnapshots.isEmpty()){
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            for(DocumentChange doc: documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String placeId = doc.getDocument().getId();
                                    Place place = doc.getDocument().toObject(Place.class).withId(placeId);
                                    listaSitios.add(place);

                                }
                            }

                        placesAdapter.notifyDataSetChanged();

                    }

            }

        });

        }
    }
}