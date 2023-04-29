package com.example.driversschool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Car {
    int x = 100;
    int y = 100;
    double rotation = 0;
    float wheelAngle;
    int width;
    int height;
    double carAcc;
    double carSpeed;
    Bitmap car;



    public Car (Resources res, Context context) {

        car = BitmapFactory.decodeResource(res, R.drawable.car);
        this.rotate(90);
        width = car.getWidth() / 2;
        height = car.getHeight() / 2;
        this.carAcc = 0;
        this.carSpeed = 0;

        car = Bitmap.createScaledBitmap(car, width, height, false);

    }

    Bitmap getCar() {
        return car;
    }

    public void setCarSpeed(float speed) {
        this.carSpeed = 0;
    }

    public void rotate(double phi) {
        this.rotation = this.rotation + phi;
        this.car = GameActivity.RotateBitmap(this.car, phi);
    }

    Rect getCollsionShape() {
        return new Rect(x,y,x+width, y+height);
    }

}
