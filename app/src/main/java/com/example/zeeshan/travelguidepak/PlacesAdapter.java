package com.example.zeeshan.travelguidepak;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> lista;
    public PlacesAdapter(List<Place> listaSitios){
        this.lista = listaSitios;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String descData = lista.get(position).getDescription();
        holder.setDesc(descData);

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setDesc(String desc){
            descView = mView.findViewById(R.id.place_desc);
            descView.setText(desc);
        }
    }
}
