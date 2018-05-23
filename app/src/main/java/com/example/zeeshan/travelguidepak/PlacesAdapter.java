package com.example.zeeshan.travelguidepak;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> lista;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private User user;
    private FirebaseAuth mAuth;
    private TextView placeDate;
    private TextView placeUsername;
    private CircleImageView placeUserImage;
    private ImageView placeCommentBtn;

    public PlacesAdapter(List<Place> listaSitios){
        this.lista = listaSitios;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        context = parent.getContext();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        String descData = lista.get(position).getDescription();
        holder.setDesc(descData);

        final String currentUserId = mAuth.getCurrentUser().getUid();
        final String PlaceId = lista.get(position).PlaceId;

        String imageUrl = lista.get(position).getImage();
        String thumbnail = lista.get(position).getThumbnai();
        holder.setImage(imageUrl, thumbnail);

        String userId = lista.get(position).getUserId();

        // User Query
        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String username = task.getResult().getString("name");
                    String image = task.getResult().getString("image");

                    holder.setUserData(username, image);

                } else {

                    // Error Handling

                }
            }
        });



        try {

            long milSec = lista.get(position).getTimestamp().getTime();
            String date = DateFormat.format("MM/dd/yyyy", new Date(milSec)).toString();
            holder.setDate(date);

        }catch (Exception e){
            Toast.makeText(context, "Got ERROR ON DATE : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        // Contar los Likes
        firebaseFirestore.collection("Places").document(PlaceId).collection("Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){
                    int numero = documentSnapshots.size();
                    holder.setLikesCount(numero);

                } else {
                    holder.setLikesCount(0);
                }
            }
        });



        // Miramos los likes
        firebaseFirestore.collection("Places").document(PlaceId).collection("Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.mipmap.liked_btn));
                } else {
                    holder.likeBtn.setImageDrawable(context.getDrawable(R.mipmap.like_btn));
                }

            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Primero vamos a mirar si ya esta likeado.
                firebaseFirestore.collection("Places/" + PlaceId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){ // No existe el Like
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Places").document(PlaceId).collection("Likes").document(currentUserId).set(likesMap);
                        } else { // si existe el Like to Eliminamos
                            firebaseFirestore.collection("Places").document(PlaceId).collection("Likes").document(currentUserId).delete();
                        }

                    }
                });


            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaceCommentsActivity.class);
                intent.putExtra("placeId", PlaceId);
                context.startActivity(intent);
            }
        });

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
        private ImageView likeBtn;
        private TextView likeBtnCount;
        private ImageView commentBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            likeBtn = mView.findViewById(R.id.like_btn);
            commentBtn = mView.findViewById(R.id.comment_img_btn);
        }

        private void setDesc(String desc){
            descView = mView.findViewById(R.id.place_desc);
            descView.setText(desc);
        }

        private void setImage(String imageUrl, String thumbnail){
            placeImage = mView.findViewById(R.id.city_image);

            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.defaultplaceimage);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(imageUrl).thumbnail(Glide.with(context).load(thumbnail)).into(placeImage);
        }

        private void setUsername(String name){
            username = mView.findViewById(R.id.place_username);
            username.setText(name);
        }

        private void setDate(String date){
            placeDate = mView.findViewById(R.id.created_date);
            placeDate.setText(date);

        }

        public void setLikesCount(int count){
            likeBtnCount = mView.findViewById(R.id.like_counter);
            likeBtnCount.setText(count + " Likes");
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