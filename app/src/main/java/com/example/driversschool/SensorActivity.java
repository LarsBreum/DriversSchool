package com.example.driversschool;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driversschool.databinding.ActivitySensorBinding;

import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * We can use this activity for the actual game //Lars
 */
public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    // Blinkers
    private int blinkDirection;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private ImageView leftArrowBlink;
    private ImageView rightArrowBlink;
    private ObjectAnimator pulseLeftArrowBlink;
    private ObjectAnimator pulseRightArrowBlink;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;

    private SensorManager sensorManager;
    private Sensor rotationSensor;

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivitySensorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mVisible = true;

        //Blinkers
        this.blinkDirection = 0;
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        leftArrowBlink = findViewById(R.id.leftArrowBlink);
        rightArrowBlink = findViewById(R.id.rightArrowBlink);


        leftArrow.setVisibility(View.VISIBLE);
        rightArrow.setVisibility(View.VISIBLE);
        leftArrowBlink.setVisibility(View.GONE);
        rightArrowBlink.setVisibility(View.GONE);
        /*
        pulseLeftArrowBlink = ObjectAnimator.ofFloat(leftArrowBlink,View.ALPHA, 03f );
        pulseRightArrowBlink = ObjectAnimator.ofFloat(rightArrowBlink,View.ALPHA, 03f );
        pulseLeftArrowBlink.setDuration(500);
        pulseRightArrowBlink.setDuration(500);
        pulseLeftArrowBlink.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseRightArrowBlink.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseLeftArrowBlink.setRepeatCount(ObjectAnimator.INFINITE);
        pulseRightArrowBlink.setRepeatCount(ObjectAnimator.INFINITE);
        pulseLeftArrowBlink.setRepeatMode(ObjectAnimator.REVERSE);
        pulseRightArrowBlink.setRepeatMode(ObjectAnimator.REVERSE);
         */


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        //get sensors
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d("sensor", String.valueOf(this.rotationSensor));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().show(
                    WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        } else {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Float rotX = sensorEvent.values[0];
        Float rotY = sensorEvent.values[1];
        Float rotZ = sensorEvent.values[2];
        Log.d("RotationX", "X: " + String.format("%.2f", rotX) + " Y: " + String.format("%.2f", rotY) + " Z: " + String.format("%.2f", rotZ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (blinkDirection == 1) {
            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
                //Toast.makeText(this, "Not Blinking", Toast.LENGTH_SHORT).show();
                leftArrowBlink.setVisibility(View.GONE);
               // pulseLeftArrowBlink.start();
                return true;
            }
            else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
               // Toast.makeText(this, "Blinking Left", Toast.LENGTH_SHORT).show();
               leftArrowBlink.setVisibility(View.VISIBLE);
                blinkDirection = -1;
                //pulseLeftArrowBlink.end();
                return true;
            }
        } else if (blinkDirection == -1) {
            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
                //Toast.makeText(this, "Blinking right", Toast.LENGTH_SHORT).show();
                rightArrowBlink.setVisibility(View.VISIBLE);
                //pulseRightArrowBlink.start();
                blinkDirection = 1;
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
               //Toast.makeText(this, "Not Blinking", Toast.LENGTH_SHORT).show();
                rightArrowBlink.setVisibility(View.GONE);
                //pulseLeftArrowBlink.end();
                blinkDirection = 0;
                return true;
            }
        } else {
            if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                // Toast.makeText(this, "Blinking right", Toast.LENGTH_SHORT).show();
                rightArrowBlink.setVisibility(View.VISIBLE);
                //pulseRightArrowBlink.start();
                blinkDirection = 1;
                return true;
            } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                //Toast.makeText(this, "Blinking left", Toast.LENGTH_SHORT).show();
                leftArrowBlink.setVisibility(View.VISIBLE);
                //pulseRightArrowBlink.end();
                blinkDirection = -1;
                return true;
            }
        }
        return true; //stops the default event

    }
}