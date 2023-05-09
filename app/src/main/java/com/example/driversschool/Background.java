package com.example.driversschool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
        this.x = -1750;
        this.y = -4000;

        background = BitmapFactory.decodeResource(res, R.drawable.level1);
        //background = Bitmap.createScaledBitmap(background, background.getWidth()/2, background.getHeight()/2, false);

    }

    public void rotate(Canvas canvas) {

        int centerX = this.background.getWidth()/2;
        int centerY = this.background.getHeight()/2;

        canvas.rotate((float)this.rotation, centerX, centerY);
    }


}
