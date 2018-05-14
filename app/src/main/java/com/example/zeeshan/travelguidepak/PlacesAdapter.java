package com.example.zeeshan.travelguidepak;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> lista;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private User user;
    public PlacesAdapter(List<Place> listaSitios){
        this.lista = listaSitios;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String descData = lista.get(position).getDescription();
        holder.setDesc(descData);

        String imageUrl = lista.get(position).getImage();
        holder.setImage(imageUrl);

        String userId = lista.get(position).getUserId();
        User username = getUser(userId);
        holder.setUsername(username.getName());


    }

    private User getUser(String userId){
        firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               user = documentSnapshot.toObject(User.class);
            }
        });
        return user;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;
        private ImageView placeImage;
        private TextView username;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setDesc(String desc){
            descView = mView.findViewById(R.id.place_desc);
            descView.setText(desc);
        }

        private void setImage(String imageUrl){
            placeImage = mView.findViewById(R.id.place_image);
            Glide.with(context).load(imageUrl).into(placeImage);
        }

        private void setUsername(String name){
            username = mView.findViewById(R.id.place_username);
            username.setText(name);
        }
    }
}
