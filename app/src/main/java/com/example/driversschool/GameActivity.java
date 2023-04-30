package com.example.driversschool;

import android.content.Context;
import android.graphics.Bitmap;
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
        float accX = sensorEvent.values[0];
        float accY = sensorEvent.values[1];
        float accZ = sensorEvent.values[2];

       // Log.d("acc", "X: " + String.format("%.2f", accX) + " Y: " + String.format("%.2f", accY) + " Z: " + String.format("%.2f", accZ));
       // Log.d("acc", "X: " + String.format("%.2f", accX) + " Y: " + String.format("%.2f", accY) + " Z: " + String.format("%.2f", accZ));
        //filtering away large movements (low-pass)
        if (accX > 9.0) {
            accX = Float.valueOf(9);
        }

        if (accX < -9) {
            accX = Float.valueOf(-9);
        }

        if (accY > 9.0) { //turning should only be +- 6
            accY = Float.valueOf(6);
        }

        if (accY < -9) {
            accY = Float.valueOf(-6);
        }
        if (accZ > 9.0) {
            accZ = Float.valueOf(9);
        }
        if (accZ < -9) {
            accZ = Float.valueOf(-9);
        }
        this.accData = sensorEvent.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getAccData() {
        return this.accData;
    }

    public static void RotateBitmap(Bitmap source, double angle, int w, int h) {
        //Matrix matrix = new Matrix();
        //matrix.preRotate((float) angle, w/2, h/2);

    }

}