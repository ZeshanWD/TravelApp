package com.example.zeeshan.travelguidepak;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
    private FirebaseAuth mAuth;

    public PlacesAdapter(List<Place> listaSitios){
        this.lista = listaSitios;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        context = parent.getContext();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);


        final String PlaceId = lista.get(position).PlaceId;
        final String currentUserId = mAuth.getCurrentUser().getUid();


        String descData = lista.get(position).getDescription();
        holder.setDesc(descData);


        String imageUrl = lista.get(position).getImage();
        String thumbnail = lista.get(position).getThumbnai();
        holder.setImage(imageUrl, thumbnail);

        String userId = lista.get(position).getUserId();


        // Contar los Likes
        firebaseFirestore.collection("Places/" + PlaceId + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(e != null){
                    return;
                }

                if(documentSnapshots.isEmpty()){
                    holder.setLikesCount(0);
                } else {
                    int numero = documentSnapshots.size();
                    holder.setLikesCount(numero);

                }
            }
        });



        // Miramos los likes
        firebaseFirestore.collection("Places/" + PlaceId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(e != null){
                    return;
                }

                if(documentSnapshot.exists()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.likeBtn.setImageDrawable(context.getDrawable(R.mipmap.liked_btn));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.likeBtn.setImageDrawable(context.getDrawable(R.mipmap.like_btn));
                    }
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

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singlePlaceIntent = new Intent(context, PlaceActivity.class);
                singlePlaceIntent.putExtra("place", lista.get(position));
                context.startActivity(singlePlaceIntent);
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
        private ConstraintLayout parentLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            likeBtn = mView.findViewById(R.id.like_btn);
            commentBtn = mView.findViewById(R.id.comment_img_btn);
            parentLayout = itemView.findViewById(R.id.parentPlaceLayout);
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

        public void setLikesCount(int count){
            likeBtnCount = mView.findViewById(R.id.like_counter);
            likeBtnCount.setText(count + " Likes");
        }

    }
}