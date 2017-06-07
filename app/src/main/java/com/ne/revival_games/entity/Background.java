package com.ne.revival_games.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Background class for the background image.
 */
public class Background {

    private Bitmap image;
    private int x, y;

    /**
     * constructor for the background class.
     *
     * @param res (a bitmap image)
     */
    public Background(Bitmap res){
        image = res;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

}
