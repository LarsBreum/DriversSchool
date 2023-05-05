package com.example.driversschool;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Level  extends AppCompatActivity {
    private NavController navController;
    private Button easy;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        this.navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        this.easy = (Button)findViewById(R.id.easy);


    }
}
