package com.example.driversschool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Car {

    int x = 0, y, width, height;
    Bitmap car;

    Car (Resources res) {
        car = BitmapFactory.decodeResource(res, R.drawable.car);
        width = car.getWidth();
        height = car.getHeight();

        car = Bitmap.createScaledBitmap(car, width, height, false);

    }

    Bitmap getCar() {
        return car;
    }

    Rect getCollsionShape() {
        return new Rect(x,y,x+width, y+height);
    }

}
