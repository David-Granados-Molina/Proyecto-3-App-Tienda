package com.example.tienda;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Tienda.R;

public class FragmentUno extends Fragment {
    private View fragmento;
    private ImageView camiseta, sudadera, pantalon, chaqueta;

    public FragmentUno() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento = inflater.inflate(R.layout.fragment_uno, container, false);

        camiseta = (ImageView)fragmento.findViewById(R.id.camiseta);
        sudadera = (ImageView)fragmento.findViewById(R.id.sudadera);
        pantalon = (ImageView)fragmento.findViewById(R.id.pantalon);
        chaqueta = (ImageView)fragmento.findViewById(R.id.chaqueta);


        camiseta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "camiseta");
                startActivity(intent);
            }
        });

        sudadera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "sudadera");
                startActivity(intent);
            }
        });

        pantalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "pantalon");
                startActivity(intent);
            }
        });

        chaqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "chaqueta");
                startActivity(intent);
            }
        });

        return fragmento;
    }
}