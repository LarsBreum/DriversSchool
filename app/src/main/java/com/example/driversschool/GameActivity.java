package com.example.driversschool;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    private RelativeLayout containerRL;

    private SensorManager sensorManager;
    private Sensor sensor;

    private float[] accData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameView = new GameView(this, point.x, point.y);
        setContentView(gameView);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.accData = new float[3];

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        gameView.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        gameView.resume();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Float accX = sensorEvent.values[0];
        Float accY = sensorEvent.values[1];
        Float accZ = sensorEvent.values[2];
       // Log.d("acc", "X: " + String.format("%.2f", accX) + " Y: " + String.format("%.2f", accY) + " Z: " + String.format("%.2f", accZ));

        this.accData = sensorEvent.values;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getAccData() {
        return this.accData;
    }

}