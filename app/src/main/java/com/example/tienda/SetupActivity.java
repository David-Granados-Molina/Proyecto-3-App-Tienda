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

public class SetupActivity extends AppCompatActivity {

    private EditText nombre, ciudad, direccion, telefono;
    private Button guardar;
    private String phone = "";
    private CircleImageView imagen;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        dialog = new ProgressDialog(this);
        nombre=(EditText)findViewById(R.id.setup_nombre);
        ciudad=(EditText)findViewById(R.id.setup_ciudad);
        direccion=(EditText)findViewById(R.id.setup_direccion);
        telefono=(EditText)findViewById(R.id.setup_telefono);
        guardar=(Button) findViewById(R.id.setup_boton);
        imagen=(CircleImageView) findViewById(R.id.setup_imagen);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            phone=bundle.getString("phone");
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarInformacion();
            }
        });

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                startActivityForResult(intent,Galery_pick);
            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChild("imagen")){
                        String imagestr = snapshot.child("imagen").getValue().toString();
                        Picasso.get().load(imagestr).placeholder(R.drawable.car).into(imagen);
                    }else{
                        Toast.makeText(SetupActivity.this,"Por favor, seleccione una imagen de perfil.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        Toast.makeText(SetupActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    private void EnviarAlInicio() {
        Intent intent = new Intent(SetupActivity.this, PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}