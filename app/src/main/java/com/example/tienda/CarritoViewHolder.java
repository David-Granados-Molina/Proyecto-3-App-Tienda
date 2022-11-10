package com.example.tienda;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Tienda.R;

public class CarritoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView carProductoNombre, carProductoCantidad, carProductoPrecio, carProductoTalla;
    private ItemClickListener itemClickListener;

    public CarritoViewHolder(@NonNull View itemView) {
        super(itemView);
        carProductoNombre = itemView.findViewById(R.id.car_producto_nombre);
        carProductoCantidad = itemView.findViewById(R.id.car_producto_cantidad);
        carProductoPrecio = itemView.findViewById(R.id.car_producto_precio);
        carProductoTalla = itemView.findViewById(R.id.car_producto_talla);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
