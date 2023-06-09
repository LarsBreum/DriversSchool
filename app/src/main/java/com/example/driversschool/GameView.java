package com.example.driversschool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private Canvas canvas;
    private Canvas carCanvas;
    private Boolean isPlaying;
    private Background background;
    private Car player;
    private int screenX, screenY;
    private Float screenRatioX, screenRatioY;
    private Paint paint;

    private GameActivity activity;
    Matrix matrix;

    Bitmap leftBlinker;
    private int leftBlinkerX;
    private int leftBlinkerY;
    private int rightBlinkerX;
    private int rightBlinkerY;
    Bitmap rightBlinker;
    int[] blinkerStatus;

    boolean phoneVibrating = false;
    int vibrateTime = 1000;
     private Rect parkedCar;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.screenX = screenX;
        this.screenY = screenY;
        this.activity = activity;
        this.matrix = new Matrix();


        background = new Background(screenX, screenY, getResources());

        /*
        Ful blinker implementering. Borde vara egen klass tycker jag
         */
        leftBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.leftarrow);
        rightBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.rightarrow);
        leftBlinker = Bitmap.createScaledBitmap(leftBlinker, leftBlinker.getWidth()/2, leftBlinker.getHeight()/2, false);
        rightBlinker = Bitmap.createScaledBitmap(rightBlinker, rightBlinker.getWidth()/2, rightBlinker.getHeight()/2, false);
        rightBlinkerX = screenX-rightBlinker.getWidth();
        rightBlinkerY = screenY-rightBlinker.getHeight();
        leftBlinkerX = screenX-leftBlinker.getWidth()*2;
        leftBlinkerY = screenY-leftBlinker.getHeight();
        

        this.paint = new Paint();
        paint.setTextSize(128);
        this.canvas = new Canvas();
        this.carCanvas = new Canvas();
        //paint.setColor(Color.white);

        this.player = new Car(getResources(), getContext(), screenX, screenY);
        this.parkedCar = new Rect(-1773, -3338, -1773-player.getCar().getWidth(), -3338+player.getCar().getHeight());

    }

    @Override
    public void run() {

        while(isPlaying) {
            update();
            draw();
            sleep();
        }

    }

    /* 
     * All movements implemented here
     */ 

    private void update() {

        float[] accData = activity.getAccData();

        Log.d("loc", String.valueOf(background.x) + " " + String.valueOf(background.y));

       double speedY = (Math.cos(Math.toRadians(background.rotation))*(-accData[0]*1.4));
       double speedX = (Math.sin(Math.toRadians(background.rotation))*(-accData[0]*1.4)); //1.1-1.4-1.0



        /*
        Stop for stop sign
         */
        if(background.y < -2830 && background.y > -3050) {
            if(speedY == 0.0) {
                activity.winMp.start();
                activity.startActivity(new Intent(activity, GameCompleted.class));
            }
        } else if(background.y > -2700 && (speedY != 0 || speedX != 0)) {
            activity.loseMp.start();
        }

        //Hitting parked car
        //Log.d("back loc", String.valueOf(minX) + " " + String.valueOf(maxX) + " " + String.valueOf(minY) + " " + String.valueOf(maxY) );
        if((background.x < -1760 && background.y > -3363)) {
            speedX = 0;
            speedY = -3;
            if(!phoneVibrating) {
                activity.vibrator.vibrate(vibrateTime);
                phoneVibrating = true;
                activity.crashMp.start();
            }

        }
        // Driving off the road
        if(background.x < -2000 && !phoneVibrating) { //if you drive off the road
            //Toast.makeText(getContext(), "You drove off the road", Toast.LENGTH_SHORT).show();
            activity.vibrator.vibrate(vibrateTime);
            phoneVibrating = true;
        } else if(background.x > -1000 && !phoneVibrating) {
            activity.vibrator.vibrate(vibrateTime);
            phoneVibrating = true;
        }


        if(vibrateTime > 0) {
            vibrateTime -= 34;
        } else {
            vibrateTime = 5000;
            phoneVibrating = false;
        }

        background.rotation = (int) (background.rotation-accData[1]*0.9)%360;
        player.rotation = (int) (player.rotation-accData[1]*0.9)%360;

        background.y += speedY;
        background.x += speedX;

        // Blinka vänster, set en timer
        if(activity.getBlinkDirection() == 1) {
            Log.d("Blinking Right", String.valueOf(activity.getBlinkDirection()));
            activity.mp.start();
            rightBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.arrowyellowright);
            rightBlinker = Bitmap.createScaledBitmap(rightBlinker, rightBlinker.getWidth()/2, rightBlinker.getHeight()/2, false);
            leftBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.leftarrow);
            leftBlinker = Bitmap.createScaledBitmap(leftBlinker, leftBlinker.getWidth()/2, leftBlinker.getHeight()/2, false);
        }else if(activity.getBlinkDirection() == -1){
            activity.mp.start();
            Log.d("Blinking Left", String.valueOf(activity.getBlinkDirection()));
            leftBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.arrowyellowleft);
            leftBlinker = Bitmap.createScaledBitmap(leftBlinker, leftBlinker.getWidth()/2, leftBlinker.getHeight()/2, false);
            rightBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.rightarrow);
            rightBlinker = Bitmap.createScaledBitmap(rightBlinker, rightBlinker.getWidth()/2, rightBlinker.getHeight()/2, false);
           // Toast.makeText(activity, "Blinking Left", Toast.LENGTH_SHORT).show();
        } else {
            activity.mp.pause();
            leftBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.leftarrow);
            rightBlinker = BitmapFactory.decodeResource(getResources(), R.drawable.rightarrow);
            leftBlinker = Bitmap.createScaledBitmap(leftBlinker, leftBlinker.getWidth()/2, leftBlinker.getHeight()/2, false);
            rightBlinker = Bitmap.createScaledBitmap(rightBlinker, rightBlinker.getWidth()/2, rightBlinker.getHeight()/2, false);
        }
    }

    /*
     * Used for drawing the gamingboard
     */
    private void draw() {

        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, 0, 0, paint);
            canvas.rotate(background.rotation, screenX/2, screenY/2);
            canvas.drawBitmap(background.background, background.x, background.y, paint);

            canvas.drawBitmap(leftBlinker, leftBlinkerX,leftBlinkerY, paint);
            canvas.drawBitmap(rightBlinker, rightBlinkerX,rightBlinkerY,paint);
            getHolder().unlockCanvasAndPost(canvas);

            carCanvas = getHolder().lockCanvas();
            carCanvas.drawBitmap(player.getCar(), (screenX/2)-player.getCar().getWidth()/2, (screenY/2)-player.getCar().getHeight()/2, paint);
            //canvas.drawBitmap(player.getCar(), (screenX/2)-player.getCar().getWidth()/2, (screenY/2)-player.getCar().getHeight()/2 , paint);
            getHolder().unlockCanvasAndPost(carCanvas);

            invalidate();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void resume() {
        activity.carSound.start();
        activity.mp.start();
        activity.mp.pause();
        activity.vibrator.vibrate(1000);
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
