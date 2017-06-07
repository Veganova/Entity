package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;

/**
 * Represents the simple shape circle class.
 */
public class Circle implements Shape {

    int x;
    int y;
    int r;

    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param r  The radius
     */
    Circle (int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public int getX(){
        return this.x;
    }

    @Override
    public int getY(){
        return this.y;
    }

    @Override
    public void draw(Canvas canvas, int curX, int curY, int offsetX, int offsetY) {
        // TODO: 6/7/2017
    }

    @Override
    public boolean collided(Shape other){
        double vectX = other.getX() - this.x;
        double vectY = other.getY() - this.y;
        double scale = Math.pow(Math.pow(vectX,2) + Math.pow(vectY,2), 0.5);
        vectX = vectX/scale;
        vectY = vectY/scale;
        return isWithin(this.x + vectX*this.r, this.y + vectY*this.r);
    }

    @Override
    public boolean isWithin(double curX, double curY) {
        return (Math.pow(Math.pow(curX - this.x, 2) + Math.pow(curY - this.y, 2), 0.5) <= this.r);
    }
}