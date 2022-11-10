package com.example.tienda;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Tienda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfirmarordenActivity extends AppCompatActivity {
    private EditText nombre, correo, direccion, telefono;
    private Button confirmar;
    private String totalPago = "";
    private FirebaseAuth auth;
    private String CurrentUserId;
    private String regx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmarorden);

        totalPago = getIntent().getStringExtra("Total");
        Toast.makeText(this, "Total a pagar: €" + totalPago, Toast.LENGTH_SHORT).show();

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();

        nombre = (EditText) findViewById(R.id.final_nombre);
        correo = (EditText) findViewById(R.id.final_correo);
        direccion = (EditText) findViewById(R.id.final_direccion);
        telefono = (EditText) findViewById(R.id.final_telefono);
        
        confirmar = (Button)findViewById(R.id.final_boton_confirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarDatos();
            }
        });

    }

    private void VerificarDatos() {

        Pattern pattern = Pattern.compile(regx);
        Matcher mather = pattern.matcher(correo.getText().toString());

        if(TextUtils.isEmpty(nombre.getText().toString())){
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(direccion.getText().toString())){
            Toast.makeText(this, "Ingrese una direccion", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(correo.getText().toString())){
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(telefono.getText().toString())){
            Toast.makeText(this, "Ingrese un telefono", Toast.LENGTH_SHORT).show();
        }else if (telefono.getText().toString().length()<=8 || telefono.getText().toString().length()>=10){
            Toast.makeText(this,"Ingrese un telefono valido",Toast.LENGTH_SHORT).show();
        }else if (mather.find() == false){
            Toast.makeText(this,"El email ingresado es inválido",Toast.LENGTH_SHORT).show();
        }else{
            ConfirmarOrden();
        }

    }

    private void ConfirmarOrden() {
        final String CurrentTime, CurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = dateFormat1.format(calendar.getTime());

        final DatabaseReference OrdenesRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserId);

        HashMap<String,Object> map = new HashMap<>();
        map.put("total",totalPago);
        map.put("nombre",nombre.getText().toString());
        map.put("direccion",direccion.getText().toString());
        map.put("telefono",telefono.getText().toString());
        map.put("correo",correo.getText().toString());
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("estado","No Enviado");

        OrdenesRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Carrito")
                            .child("Usuario Compra").child(CurrentUserId).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmarordenActivity.this, "Tu orden fue tomada con exito", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmarordenActivity.this, PrincipalActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }
}























