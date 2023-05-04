package com.example.driversschool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Background {

    int x, y;
    int rotation;
    private int screenX;
    private int screenY;

    //road is from 1750 to 3000 px
    Bitmap background;

    Background (int screenX, int screenY, Resources res) {
        this.screenX = screenX;
        this.screenY = screenY;

        this.rotation = 0;
        this.x = 0;
        this.y = 0;

        background = BitmapFactory.decodeResource(res, R.drawable.level1);
        background = Bitmap.createScaledBitmap(background, background.getWidth()/2, background.getHeight()/2, false);

    }

    public void rotate(Matrix matrix, float angle) {
        this.rotation = (this.rotation - (int)angle)%360;
        matrix.setRotate(this.rotation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(this.background, 0,0, this.background.getWidth(), this.background.getHeight(),matrix,true);
        Bitmap orgBitmap = this.background;
        this.background = rotatedBitmap;
        orgBitmap.recycle();
    }

    public void draw(Matrix matrix, Canvas canvas, Paint paint) {
        // rotate around own axis: https://stackoverflow.com/questions/27004655/drawbitmap-how-can-you-set-coordinates-and-use-a-matrix
        matrix.reset();

        // Log.d("msg:", "X: " + this.x + " Y: " + this.y);

        matrix.postRotate(this.rotation); //this.rotation+calcAngleSpeed()
        // Log.d("Cords", String.valueOf(this.x) + " " + String.valueOf((this.y)));
        matrix.postTranslate(this.x+screenX/2, this.y+screenY/2);

        canvas.drawBitmap(this.background, matrix, paint);

        // canvas.drawBitmap(this.car, matrix, paint);
    }


    public void moveBackground(float xAcc, float yAcc, float zAcc, double carSpeed, Matrix matrix) {
        this.rotation = this.rotation - (int)yAcc/2;
       // this.rotate(matrix);
        this.y += (int) xAcc;
    }
}
