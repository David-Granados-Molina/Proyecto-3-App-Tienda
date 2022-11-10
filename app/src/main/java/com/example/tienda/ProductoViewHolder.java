package com.example.tienda;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Tienda.R;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productoNom, productoDescrip, productoPrecio, productoCantidad, productoTalla;
    public ImageView productoImagen;
    public ItemClickListener listener;
    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);
        productoNom = (TextView) itemView.findViewById(R.id.producto_nombre);
        productoPrecio = (TextView) itemView.findViewById(R.id.producto_precio);
        productoImagen = (ImageView) itemView.findViewById(R.id.producto_imagen);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
