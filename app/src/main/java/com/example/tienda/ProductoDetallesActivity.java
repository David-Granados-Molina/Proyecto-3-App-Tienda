package com.example.tienda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Tienda.R;
import com.example.tienda.Modal.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductoDetallesActivity extends AppCompatActivity {

    private Button agregarCarrito, sumar, restar;
    private ImageView productoImagen;
    private TextView productoPrecio, productoDescripcion, productoNombre, productoCantidad;
    private TextView productoTalla;
    private String productoID = "", estado = "Normal", CurrentUserID;
    private FirebaseAuth auth;
    private int contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalles);

        productoID = getIntent().getStringExtra("pid");

        agregarCarrito = (Button) findViewById(R.id.boton_siguiente_detalles);
        sumar = (Button) findViewById(R.id.sumar);
        restar = (Button) findViewById(R.id.restar);
        productoCantidad=(TextView) findViewById(R.id.producto_cantidad_detalles);
        productoImagen = (ImageView) findViewById(R.id.producto_imagen_detalles);
        productoPrecio = (TextView) findViewById(R.id.producto_precio_detalles);
        productoDescripcion = (TextView) findViewById(R.id.producto_descripcion_detalles);
        productoNombre = (TextView) findViewById(R.id.producto_nombre_detalles);
        productoTalla = (TextView) findViewById(R.id.producto_talla_detalles);

        ObtenerDatosProducto(productoID);

        auth = FirebaseAuth.getInstance();
        CurrentUserID = auth.getCurrentUser().getUid();

        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado.equals("Pedido") || estado.equals("Enviado")){
                    Log.e("pedidoOenviado","ha entrado");
                    Toast.makeText(ProductoDetallesActivity.this,"Esperando a que el primer pedido se finalice...", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("AgregarAlalista","ha entrado");
                    AgregarAlaLista();
                }
            }
        });

        sumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador++;
                productoCantidad.setText(""+contador);
            }
        });

        restar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contador<=1){
                    contador=1;
                }else{
                    contador--;
                    productoCantidad.setText(""+contador);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadoOrden();
    }

    private void AgregarAlaLista() {
        String CurrentTime, CurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat data = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate = data.format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = time.format(calendar.getTime());

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        final HashMap<String, Object> map = new HashMap<>();

        map.put("pid",productoID);
        map.put("nombre",productoNombre.getText().toString());
        map.put("precio",productoPrecio.getText().toString());
        map.put("cantidad",productoCantidad.getText().toString());
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("descuento","");
        map.put("talla",productoTalla.getText());

        CartListRef.child("Usuario Compra").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    CartListRef.child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductoDetallesActivity.this,"Agregando...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductoDetallesActivity.this, PrincipalActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    private void ObtenerDatosProducto(String productoID) {
        DatabaseReference ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");
        ProductoRef.child(productoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Productos productos = snapshot.getValue(Productos.class);
                    productoNombre.setText(productos.getNombre());
                    productoDescripcion.setText(productos.getDescripcion());
                    productoPrecio.setText(productos.getPrecio());
                    productoTalla.setText(productos.getTalla());
                    Picasso.get().load(productos.getImagen()).into(productoImagen);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void VerificarEstadoOrden() {
        DatabaseReference OrdenRef;
        OrdenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserID);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String envioEstado = snapshot.child("estado").getValue().toString();
                    if (envioEstado.equals("Enviado")){
                        estado = "Enviado";
                        Log.e("enviado",envioEstado);
                    }else if (envioEstado.equals("No Enviado")){
                        estado = "Pedido";
                        Log.e("noenviado",envioEstado);
                        Log.e("estado",estado);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}





















