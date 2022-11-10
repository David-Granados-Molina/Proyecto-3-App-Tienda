package com.example.tienda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentTres extends Fragment {

    private View fragmento;
    private EditText nombre, email, telefono;
    private Button guardar, cerrarSesion, vistaUsuario;
    private CircleImageView imagen;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_pick = 1;
    private StorageReference UserImagenPerfil;

    public FragmentTres() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento = inflater.inflate(R.layout.fragment_tres, container, false);

        auth = FirebaseAuth.getInstance();
        Log.e("admin:", String.valueOf(auth));
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog = new ProgressDialog(getContext());
        UserImagenPerfil = FirebaseStorage.getInstance().getReference().child("Perfil");
        nombre=(EditText)fragmento.findViewById(R.id.perfila_nombre);
        email=(EditText)fragmento.findViewById(R.id.perfila_email);
        telefono=(EditText)fragmento.findViewById(R.id.perfila_telefono);
        guardar=(Button)fragmento.findViewById(R.id.perfila_boton);
        vistaUsuario=(Button)fragmento.findViewById(R.id.perfila_boton3);
        cerrarSesion=(Button)fragmento.findViewById(R.id.perfila_boton2);
        imagen=(CircleImageView)fragmento.findViewById(R.id.perfila_imagen);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()&&snapshot.hasChild("imagen")){
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String emails = snapshot.child("email").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    String imagens = snapshot.child("imagen").getValue().toString();

                    Picasso.get()
                            .load(imagens)
                            .placeholder(R.drawable.welcome)
                            .into(imagen);

                    nombre.setText(nombres);
                    email.setText(emails);
                    telefono.setText(telefonos);
                }else if (snapshot.exists()){
                    String nombres = snapshot.child("nombre").getValue().toString();
                    String emails = snapshot.child("email").getValue().toString();
                    String telefonos = snapshot.child("telefono").getValue().toString();
                    nombre.setText(nombres);
                    email.setText(emails);
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

        vistaUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PrincipalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return fragmento;
    }
    private void GuardarInformacion() {
        String nombres = nombre.getText().toString().toUpperCase();
        String emails = email.getText().toString();
        String phones = telefono.getText().toString();

        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(getContext(),"Ingrese el nombre",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(emails)){
            Toast.makeText(getContext(),"Ingrese el email",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)){
            Toast.makeText(getContext(),"Ingrese el telefono",Toast.LENGTH_SHORT).show();
        }else{
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor, espera...");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            HashMap map = new HashMap();
            map.put("nombre",nombres);
            map.put("email",emails);
            map.put("telefono",phones);
            map.put("uid",CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        EnviarAlInicio();
                    }else{
                        String mensaje = task.getException().toString();
                        Toast.makeText(getContext(), "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    private void EnviarAlInicio() {
        Intent intent = new Intent(getContext(), AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}