package com.example.driversschool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Car {
    int x = 100;
    int y = 100;
    float rotation;
    float wheelAngle;
    int width;
    int height;
    double carAcc;
    double carSpeed;
    Bitmap car;



    public Car (Resources res, Context context) {

        car = BitmapFactory.decodeResource(res, R.drawable.car);
        this.rotate(90);
        this.wheelAngle = 2;
        width = car.getWidth() / 2;
        height = car.getHeight() / 2;
        this.carAcc = 0;
        this.carSpeed = 0;
        this.rotation = 0f;

        car = Bitmap.createScaledBitmap(car, width, height, false);

    }

    Bitmap getCar() {
        return car;
    }

    public void setCarSpeed(float speed) {
        this.carSpeed = 0;
    }

    public void rotate(float phi) {
        this.rotation = this.rotation+1;
    }

    public void draw(Matrix matrix, Canvas canvas, Paint paint) {
        matrix.reset();
        float center[] = calcCenterOfCircle(x,y,wheelAngle);
        Log.d("circle:", Float.valueOf(center[0]) + " " + Float.valueOf(center[1]));

        float centerX = this.x + this.car.getWidth()/2;
        float centerY = this.y + this.car.getHeight()/2;

        matrix.postTranslate(-this.car.getWidth()/2, -this.car.getHeight()/2);
        matrix.postRotate(this.rotation);
        matrix.postTranslate(this.x, this.y);

        canvas.drawBitmap(this.car, matrix, paint);

       // canvas.drawBitmap(this.car, matrix, paint);
    }

    Rect getCollsionShape() {
        return new Rect(x,y,x+width, y+height);
    }

    private float[] calcCenterOfCircle(int x, int y, float angle) {
        double rad = Math.toRadians(angle);
        float[] cordinates = new float[2];

        double slope = -1 / Math.tan(rad);
        double b = y-slope*x;
        float centerXLine = x/2;
        float centerYLine = y/2;

        double newB = centerYLine - slope * centerXLine;
        double centerCircleX = (newB - b) / slope;
        double centerCircleY = (slope * centerCircleX) + newB;

        cordinates[0] = (float) centerCircleX;
        cordinates[1] = (float) centerCircleY;

        return cordinates;

    }

}
