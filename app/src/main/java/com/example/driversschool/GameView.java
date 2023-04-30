package com.example.driversschool;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private Canvas canvas;
    private Boolean isPlaying;
    private Background background;
    private Car player;
    private int screenX, screenY;
    private Float screenRatioX, screenRatioY;
    private Paint paint;

    private GameActivity activity;
    Matrix matrix;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.screenX = screenX;
        this.screenY = screenY;
        this.activity = activity;
        this.matrix = new Matrix();

        background = new Background(screenX, screenY, getResources());

        this.paint = new Paint();
        paint.setTextSize(128);
        //paint.setColor(Color.white);

        this.player = new Car(getResources(), getContext());

    }

    @Override
    public void run() {

        while(isPlaying) {
            update();
            draw();
            sleep();
        }

    }

    private void update() {

        float[] accData = activity.getAccData();
       // movePlayer(accData[0], accData[1], accData[2]);
        player.rotate((float) 0.01);
        //Log.d("acc", "X: " + String.format("%.2f", activity.getAccData()[0]) + " Y: " + String.format("%.2f", activity.getAccData()[1]) + " Z: " + String.format("%.2f", activity.getAccData()[2]));
        //Log.d("Update", "Update function"); //Works



    }

    private void draw() {

        //Log.d("Draw ", "Draw method")

        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, 0, 0, paint);

            player.draw(matrix, canvas, paint);

           // canvas.drawBitmap(player.getCar(), player.x, player.y, paint);

            getHolder().unlockCanvasAndPost(canvas);
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

    private void movePlayer(Float xAcc, Float yAcc, Float zAcc) {

        player.carSpeed = xAcc;
        //player.y = (int) ((int) player.y + xAcc); //turn on for moving
        /*
        Turning makes sense for yAcc +-6
         */

        if(xAcc > 8.5) {
            //reversing
            //Log.d("movePlayer:", "reverse");

        } else if(xAcc < 5) {
            //forward
            //Log.d("movePlayer:", "forward");
        } else {
            //standing still
          //  Log.d("movePlayer:", "no acc");
        }
    }




}
