package com.example.driversschool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = -1500, y = 0;

    //road is from 1750 to 3000 px
    Bitmap background;

    Background (int screenX, int screenY, Resources res) {


        background = BitmapFactory.decodeResource(res, R.drawable.level1);
        background = Bitmap.createScaledBitmap(background, background.getWidth()/4, background.getHeight()/4, false);

    }

}
