package com.example.zeeshan.travelguidepak;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CitiesRecyclerAdapter extends RecyclerView.Adapter<CitiesRecyclerAdapter.ViewHolder> {

    private List<City> lista;
    private Context context;
    private City city;

    public CitiesRecyclerAdapter(List<City> listaSitios){
        this.lista = listaSitios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        String nameData = lista.get(position).getName();
        holder.setName(nameData);

        String image = lista.get(position).getImage();
        holder.setImage(image);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                City citySelected = lista.get(position);
                Intent detailsIntent = new Intent(context, CityDetailsActivity.class);
                detailsIntent.putExtra("city", citySelected);
                context.startActivity(detailsIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        private View mView;
        private ImageView imageView;
        private ConstraintLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        private void setName(String name){
            nameView = mView.findViewById(R.id.city_name);
            nameView.setText(name);
        }

        private void setImage(String imageUrl){
            imageView = mView.findViewById(R.id.city_image);
            Glide.with(context).load(imageUrl).into(imageView);
        }
    }

}
