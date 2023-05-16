package com.example.driversschool;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GameCompleted extends AppCompatActivity {
    private Button button;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        this.button = (Button) findViewById(R.id.levelButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameCompleted.this, Level.class));
            }

        });

    }
}
