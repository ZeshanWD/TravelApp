package com.example.zeeshan.travelguidepak;

/**
 * Created by zeeshan on 28/10/2017.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zeeshan on 24/10/2017.
 */

public class CitiesAdapter extends BaseAdapter {

    // Declare Variables
    private Context context;
    private int layout;
    private List<City> list;

    public CitiesAdapter(Context context, int layout, List<City> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public City getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * En este caso vamos a implementar el patron ViewHolder
         * para mejorar el rendimiento.
         */

        View row = convertView;

        if(convertView == null){
            /**
             * Va acceder aqui solo la primera vez, ya que va a ser nulo.
             * Cuando acceda aqui inflamos la vista y adjuntamos las referencias del layout
             * en una nueva instancia de nuestro ViewHolde, y la insertamos dentro del converview
             * para reciclar su uso.
             */
            Log.w("TraviApp", "PASANDO PASANDO");
            row = LayoutInflater.from(parent.getContext()).inflate(layout, null);

            TextView name = (TextView) row.findViewById(R.id.tv_name);
            ImageView imagen = (ImageView) row.findViewById(R.id.city_image);


            ViewHolder holder = new ViewHolder(name, imagen);
            row.setTag(holder);


        } else {
            /**
             * Obtenemos la referencia que anteriormente pusimos dentro del convertview
             * Y asi, recilamos su uso sin necesidad de buscar de nuevo.
             */
            row = convertView;
        }

        // cogo el ViewHolder con el metodo getTag, ya que antes metimos con el setTag()
        ViewHolder holder = (ViewHolder) row.getTag();

        Log.w("TraviApp", "COGIENDO EL HOLDER");

        final City cityActual = getItem(position);

        holder.nombre.setText(cityActual.getName());
        holder.imagen.setImageResource(cityActual.getImage());

        return row;
    }

    static class ViewHolder{
        private TextView nombre;
        private ImageView imagen;

        ViewHolder(TextView nombre, ImageView imagen){
            this.nombre = nombre;
            this.imagen = imagen;
        }
    }
}
