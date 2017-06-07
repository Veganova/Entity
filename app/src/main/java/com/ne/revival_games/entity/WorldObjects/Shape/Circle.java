package com.ne.revival_games.entity.WorldObjects.Shape;

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
    public void draw(Canvas canvas, int curX, int curY, int offsetX, int offsetY) {
        // TODO: 6/7/2017
    }

    @Override
    public boolean collided(Shape other) {
        // TODO: 6/7/2017
        return false;
    }
}