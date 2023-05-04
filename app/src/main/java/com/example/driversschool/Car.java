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
    int x = 500;
    int y = 500;
    float rotation;
    float wheelAngle;
    int width;
    int height;
    double carAcc;
    double carSpeedX;
    double carSpeedY;
    double carSpeed;
    Bitmap car;



    public Car (Resources res, Context context) {

        car = BitmapFactory.decodeResource(res, R.drawable.car);
        //this.rotate(90);
        this.wheelAngle = 2;
        width = car.getWidth() / 2;
        height = car.getHeight() / 2;
        this.carAcc = 0;
        this.carSpeedX = 0f;
        this.carSpeedY = 0f;
        this.carSpeed = 0f;
        this.rotation = 0f;

        car = Bitmap.createScaledBitmap(car, width, height, false);

    }

    Bitmap getCar() {
        return car;
    }

    /*public void rotate(float phi) {
        this.rotation = this.rotation + calcAngleSpeed(this.carSpeed, this.wheelAngle);
    }*/

    public void draw(Matrix matrix, Canvas canvas, Paint paint) {
        // rotate around own axis: https://stackoverflow.com/questions/27004655/drawbitmap-how-can-you-set-coordinates-and-use-a-matrix
        matrix.reset();

        float centerX = this.x + this.car.getWidth()/2;
        float centerY = this.y + this.car.getHeight()/10;

       // Log.d("msg:", "X: " + this.x + " Y: " + this.y);

        matrix.postTranslate(-this.car.getWidth()/2, -this.car.getHeight()/2);
        matrix.postRotate(this.rotation); //this.rotation+calcAngleSpeed()
       // Log.d("Cords", String.valueOf(this.x) + " " + String.valueOf((this.y)));
        matrix.postTranslate(this.x, this.y);

        canvas.drawBitmap(this.car, matrix, paint);
    }

    Rect getCollsionShape() {
        return new Rect(x,y,x+width, y+height);
    }
    public void moveCar(float xAcc, float yAcc, float zAcc) {
        this.setCarSpeed(xAcc);
        this.setWheelAngle(yAcc);
        //have to use the car's speed and rotation to figure out the new coordinates
        //this.setRotation();

        this.x = this.x + (int)carSpeedX;
        //this.y = this.y + (int)carSpeedY;
    }

    private void setCarSpeed(float xAcc) {
        Log.d("xAcc:", String.valueOf(xAcc) + " rotation: " + String.valueOf(rotation));
        this.carSpeedX = xAcc*Math.sin(-this.rotation);
        this.carSpeedY = xAcc*Math.cos(-this.rotation);
        this.carSpeed = Math.sqrt(Math.exp(this.carSpeedX) + Math.exp(this.carSpeedY));
        if(this.carSpeed < 1.5) {
            this.carSpeed = 0;
        }
        //Log.d("carSpeedX", String.valueOf(carSpeedX) + " carSpeedY:" + String.valueOf(carSpeedY) + " carSpeed: " + String.valueOf(carSpeed));
    }

    /**
     * How fast the car should change direction
     * @return
     */
    private float calcAngleSpeed() {
        return (float) carSpeed*(wheelAngle);
    }

    /*private void setRotation() {
        float angleSpeed = calcAngleSpeed();
        if(angleSpeed != 0) {
            this.rotation = (Math.abs(this.rotation+angleSpeed)%360);
        } else {
            this.rotation = this.rotation;
        }

       // Log.d("rot", String.valueOf(Float.valueOf(this.rotation)));
    }*/

    private void setWheelAngle(Float yAcc) {
        this.wheelAngle = yAcc;
    }
}
