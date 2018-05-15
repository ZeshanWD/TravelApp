package com.example.zeeshan.travelguidepak;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> lista;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private User user;
    private TextView placeDate;
    private TextView placeUsername;
    private CircleImageView placeUserImage;

    public PlacesAdapter(List<Place> listaSitios){
        this.lista = listaSitios;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        String descData = lista.get(position).getDescription();
        holder.setDesc(descData);

        String imageUrl = lista.get(position).getImage();
        holder.setImage(imageUrl);

        String userId = lista.get(position).getUserId();
        //User username = getUser(userId);
        //holder.setUsername(username.getName());

        // User Query
        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String username = task.getResult().getString("name");
                    String image = task.getResult().getString("image");

                    holder.setUserData(username, image);

                } else {

                }
            }
        });





        long milSec = lista.get(position).getTimestamp().getTime();
        String date = DateFormat.format("MM/dd/yyyy", new Date(milSec)).toString();
        holder.setDate(date);
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

            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.defaultplaceimage);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(imageUrl).into(placeImage);
        }

        private void setUsername(String name){
            username = mView.findViewById(R.id.place_username);
            username.setText(name);
        }

        private void setDate(String date){
            placeDate = mView.findViewById(R.id.created_date);
            placeDate.setText(date);

        }

        private void setUserData(String name, String image){
            placeUsername = mView.findViewById(R.id.place_username);
            placeUserImage = mView.findViewById(R.id.place_userimage);
            placeUsername.setText(name);

            // para al cargar no se descuadre la imagen.
            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.mipmap.ic_launcher_round);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(image).into(placeUserImage);
        }
    }
}
