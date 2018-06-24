package com.example.zeeshan.travelguidepak;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class PlaceCommentAdapter extends RecyclerView.Adapter<PlaceCommentAdapter.ViewHolder> {

    public List<Comment> commentsList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;

    public PlaceCommentAdapter(List<Comment> commentsList){

        this.commentsList = commentsList;

    }

    @Override
    public PlaceCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new PlaceCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceCommentAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        firebaseFirestore = FirebaseFirestore.getInstance();

        String commentMessage = commentsList.get(position).getMessage();
        holder.setCommentMessage(commentMessage);

        String userId = commentsList.get(position).getUserId();

        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    String username = task.getResult().getString("name");
                    String image = task.getResult().getString("image");

                    holder.setUserData(username, image);

                } else {

                    // Error Handling
                    Toast.makeText(context, "Error while retrieving user info " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });



    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private TextView comment_username;
        private ImageView user_image;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCommentMessage(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }

        private void setUserData(String name, String image){
            comment_username = mView.findViewById(R.id.comment_username);
            user_image = mView.findViewById(R.id.user_image);
            comment_username.setText(name);

            // para al cargar no se descuadre la imagen.
            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.mipmap.default_image);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(image).into(user_image);
        }
    }

}