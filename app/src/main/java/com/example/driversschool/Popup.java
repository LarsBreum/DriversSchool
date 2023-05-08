package com.example.driversschool;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Popup  extends AppCompatActivity {
    private Button cross;
    private MediaPlayer mp;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        this.mp = MediaPlayer.create(Popup.this, R.raw.instruktion);
        this.cross = (Button) findViewById(R.id.cross);


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                startActivity(new Intent(Popup.this, GameActivity.class));
            }

        });


    }
}