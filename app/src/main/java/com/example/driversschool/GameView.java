package com.example.driversschool;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private Boolean isPlaying;
    private Background background;
    private Car player;
    private int screenX, screenY;
    private Float screenRatioX, screenRatioY;
    private Paint paint;

    private GameActivity activity;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.screenX = screenX;
        this.screenY = screenY;
        this.activity = activity;


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

        Log.d("acc", "X: " + String.format("%.2f", activity.getAccData()[0]) + " Y: " + String.format("%.2f", activity.getAccData()[1]) + " Z: " + String.format("%.2f", activity.getAccData()[2]));
        //Log.d("Update", "Update function"); //Works

    }

    private void draw() {

        //Log.d("Draw ", "Draw method")

        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, 0, 0, paint);

            canvas.drawBitmap(player.getCar(), 100, 100, paint);

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




}
