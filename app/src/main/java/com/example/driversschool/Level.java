package com.example.driversschool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Level  extends AppCompatActivity {

    private Button easy;
    private ImageButton imageButton2;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        this.easy = (Button)findViewById(R.id.easy);
        this.imageButton2 = (ImageButton)findViewById(R.id.imageButton2);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Level.this, Popup.class));
            }

        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Level.this, Profil.class));
            }

        });}
}