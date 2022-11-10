package com.example.tienda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupadminActivity extends AppCompatActivity {

    private EditText nombre, email, telefono;
    private Button guardar, cerrarSesion;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_pick = 1;
    private String regx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupadmin);

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog = new ProgressDialog(this);
        nombre=(EditText)findViewById(R.id.asetup_nombre);
        email=(EditText)findViewById(R.id.asetup_email);
        telefono=(EditText)findViewById(R.id.asetup_telefono);
        guardar=(Button) findViewById(R.id.asetup_boton);
        Bundle bundle=getIntent().getExtras();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarInformacion();
            }
        });
    }


    private void GuardarInformacion() {
        String nombres = nombre.getText().toString().toUpperCase();
        String emails = email.getText().toString();
        String phones = telefono.getText().toString();

        Pattern pattern = Pattern.compile(regx);
        Matcher mather = pattern.matcher(email.getText().toString());
        Log.e("wer", String.valueOf(mather));

        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(this,"Ingrese el nombre",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(emails)){
            Toast.makeText(this,"Ingrese el email",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)){
            Toast.makeText(this,"Ingrese el telefono",Toast.LENGTH_SHORT).show();
        }else if (phones.length()<=8 || phones.length()>=10){
            Toast.makeText(this,"Ingrese un telefono valido",Toast.LENGTH_SHORT).show();
        }else if (mather.find() == false){
            Toast.makeText(this,"El email ingresado es inválido",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SetupadminActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    private void EnviarAlInicio() {
        Intent intent = new Intent(SetupadminActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
