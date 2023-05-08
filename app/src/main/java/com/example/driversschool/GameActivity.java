package com.example.driversschool;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private GameView gameView;
    private RelativeLayout containerRL;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float[] accData;
    private float lowPass;
    private float highPass;
    private int blinkDirection;
    public MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        gameView = new GameView(this, screenWidth, screenHeight);
        setContentView(gameView);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.accData = new float[3];
        this.lowPass = 9f;
        this.highPass = 1f;

        this.blinkDirection = 0;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.mp = MediaPlayer.create(this, R.raw.blinker);

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
        //filtering away small movements (high-pass). Meaning we have a bandpass

        if(accX < highPass && accX > -highPass) {
            accX = 0; // high-pass
        } else {
            if (accX > lowPass) {
                accX = Float.valueOf(9);
            }
            if(accX < -lowPass) {
                accX = -Float.valueOf(9);
            }
        }

        if(accY < highPass && accY > -highPass) {
            accY = 0; // high-pass
        } else {
            if (accY > lowPass) {
                accY = Float.valueOf(9);
            }
            if(accY < -lowPass) {
                accY = -Float.valueOf(9);
            }
        }

        if(accZ < highPass && accZ > -highPass) {
            accX = 0; // high-pass
        } else {
            if (accZ > lowPass) {
                accZ = Float.valueOf(9);
            }
            if(accZ < -lowPass) {
                accZ = -Float.valueOf(9);
            }
        }

        this.accData[0] = accX;
        this.accData[1] = accY;
        this.accData[2] = accZ;
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

    public int getBlinkDirection() {
        return blinkDirection;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (blinkDirection == 1) {
            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
                blinkDirection = 0;
                return true;
            }
            else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
                blinkDirection = -1;
                return true;
            }

        } else if (blinkDirection == -1) {
            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
                blinkDirection = 1;
                return true;

            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                blinkDirection = 0;
                return true;
            }
        } else {
            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                blinkDirection = 1;
                return true;

            } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                blinkDirection = -1;
                return true;
            }
        }
        return true; //stops the default event
    }

}