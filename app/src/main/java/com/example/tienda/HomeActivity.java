package com.example.tienda;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Tienda.R;

public class HomeActivity extends AppCompatActivity {

    private Button botonusuario;
    private VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        botonusuario = (Button)findViewById(R.id.botonusuario);
        video = (VideoView) findViewById(R.id.videobackground);

        String path = "android.resource://com.example.tienda/" + R.raw.video;
        Uri uri = Uri.parse(path);
        video.setVideoURI(uri);
        video.start();

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        botonusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,PrincipalActivity.class);
                startActivity(intent);
            }
        });
    }
}