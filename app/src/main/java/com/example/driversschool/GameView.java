package com.example.driversschool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
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

    Bitmap leftBlinker;
    private int leftBlinkerX;
    private int leftBlinkerY;
    private int rightBlinkerX;
    private int rightBlinkerY;
    Bitmap rightBlinker;
    int[] blinkerStatus;


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
        this.blinkerStatus = new int[]{0, 0}; //[0,0] means no blink. [1,0] means left is blinking
        rightBlinkerX = screenX-rightBlinker.getWidth();
        rightBlinkerY = screenY-rightBlinker.getHeight();
        leftBlinkerX = screenX-leftBlinker.getWidth()*2;
        leftBlinkerY = screenY-leftBlinker.getHeight();


        this.paint = new Paint();
        paint.setTextSize(128);
        this.canvas = new Canvas();
        //paint.setColor(Color.white);

        this.player = new Car(getResources(), getContext(), screenX, screenY);

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
        //Log.d("Screen:", String.valueOf(screenX) + " " + String.valueOf(screenY));

        float[] accData = activity.getAccData();
        //movePlayer(accData[0], accData[1], accData[2]);

       double speedY = (Math.cos(Math.toRadians(background.rotation))*(-accData[0]));
       double speedX = (Math.sin(Math.toRadians(background.rotation))*(-accData[0]));

        //int speedY = (int) (accData[0]);
        //int speedX = (int) (accData[0]);

        background.y += speedY;
        background.x += speedX;

        //background.rotation = 45;
        background.rotation = (int) (background.rotation-accData[1])%180;
        //player.rotation = (int) (accData[1]/2);

        Log.d("rotation:", String.valueOf(background.rotation));
        Log.d("acc", String.valueOf(accData[0]));
        Log.d("speed", "x: " + String.valueOf(speedX) + " y: " + String.valueOf(speedY));



        //player.rotate((float) 0.01);
        //Log.d("acc", "X: " + String.format("%.2f", activity.getAccData()[0]) + " Y: " + String.format("%.2f", activity.getAccData()[1]) + " Z: " + String.format("%.2f", activity.getAccData()[2]));
        //Log.d("Update", "Update function"); //Works

    }



    private void draw() {

        //Log.d("Draw ", "Draw method")

        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, 0, 0, paint);
            canvas.drawBitmap(leftBlinker, leftBlinkerX,leftBlinkerY, paint);
            canvas.drawBitmap(rightBlinker, rightBlinkerX,rightBlinkerY,paint);

      //      canvas.drawColor(Color.BLACK);
            canvas.rotate(background.rotation, screenX/2, screenY/2);
            canvas.drawBitmap(background.background, background.x, background.y, paint);
            //player.rotate(matrix, canvas, paint);
            canvas.drawBitmap(player.getCar(), (screenX/2)-player.getCar().getWidth()/2, (screenY/2)-player.getCar().getHeight()/2 , paint);
            //background.draw(matrix, canvas, paint);
            //player.draw(matrix, canvas, paint);

            getHolder().unlockCanvasAndPost(canvas);
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


    private void moveBackground(float xAcc, float yAcc, float zAcc) {
        //background.moveBackground(xAcc, yAcc, zAcc, player.carSpeed, this.matrix);

        /*background.rotation = (int) ((background.rotation+yAcc/2)%360);
        background.rotate(this.canvas);*/
        Log.d("Back", String.valueOf(background.rotation));
    }

    private void movePlayer(Float xAcc, Float yAcc, Float zAcc) {
      //  Log.d("acc Data:", "xAcc: " + String.valueOf(xAcc) + " yAcc: " + String.valueOf(yAcc) + " zAcc: " + String.valueOf(zAcc));
       // player.setWheelAngle(yAcc);
        //player.moveCar(xAcc, yAcc, zAcc);
       // background.x = background.x-10;

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
