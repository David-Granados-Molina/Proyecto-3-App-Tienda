package com.example.tienda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Tienda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {

    private EditText nombre, ciudad, direccion, telefono;
    private Button guardar;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_pick = 1;
    private StorageReference UserImagenPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        dialog = new ProgressDialog(this);
        UserImagenPerfil = FirebaseStorage.getInstance().getReference().child("Perfil");
        nombre=(EditText)findViewById(R.id.perfil_nombre);
        ciudad=(EditText)findViewById(R.id.perfil_ciudad);
        direccion=(EditText)findViewById(R.id.perfil_direccion);
        telefono=(EditText)findViewById(R.id.perfil_telefono);
        guardar=(Button) findViewById(R.id.perfil_boton);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String direccions = snapshot.child("direccion").getValue().toString();
                    String ciudads = snapshot.child("ciudad").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();

                    nombre.setText(nombres);
                    direccion.setText(direccions);
                    ciudad.setText(ciudads);
                    telefono.setText(telefonos);
                }else if (snapshot.exists()){
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String direccions = snapshot.child("direccion").getValue().toString();
                    String ciudads = snapshot.child("ciudad").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    nombre.setText(nombres);
                    direccion.setText(direccions);
                    ciudad.setText(ciudads);
                    telefono.setText(telefonos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarInformacion();
            }
        });
    }



    private void GuardarInformacion() {
        String nombres = nombre.getText().toString().toUpperCase();
        String direccions = direccion.getText().toString();
        String ciudads = ciudad.getText().toString();
        String phones = telefono.getText().toString();

        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(this,"Ingrese el nombre",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(direccions)){
            Toast.makeText(this,"Ingrese la direccion",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(ciudads)){
            Toast.makeText(this,"Ingrese la ciudad",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)){
            Toast.makeText(this,"Ingrese el telefono",Toast.LENGTH_SHORT).show();
        }else if (phones.length()<=8 || phones.length()>=10){
            Toast.makeText(this,"Ingrese un telefono valido",Toast.LENGTH_SHORT).show();
        }else{
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor, espera...");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            HashMap map = new HashMap();
            map.put("nombre",nombres);
            map.put("direccion",direccions);
            map.put("ciudad",ciudads);
            map.put("telefono",phones);
            map.put("uid",CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        EnviarAlInicio();
                    }else{
                        String mensaje = task.getException().toString();
                        Toast.makeText(PerfilActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    private void EnviarAlInicio() {
        Intent intent = new Intent(PerfilActivity.this, PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}