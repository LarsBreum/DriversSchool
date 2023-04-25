package com.example.driversschool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Car {
    int x = 0, y = 0, rotation = 0, wheelAngle = 0, width, height;
    Bitmap car;



    public Car (Resources res, Context context) {

        car = BitmapFactory.decodeResource(res, R.drawable.car);
        width = car.getWidth() / 2;
        height = car.getHeight() / 2;

        car = Bitmap.createScaledBitmap(car, width, height, false);

    }

    Bitmap getCar() {
        return car;
    }

    Rect getCollsionShape() {
        return new Rect(x,y,x+width, y+height);
    }

}
