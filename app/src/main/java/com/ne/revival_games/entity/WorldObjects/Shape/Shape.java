package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;

/**
 * A interface representing different shapes.
 */
public interface Shape {
    int getX();
    int getY();
    /**
     * Draws the given shape onto the given canvas.
     *
     * @param canvas    The canvas onto which the this shape will be drawn
     * @param curX      The x coordinate of the entity which this shape is part of
     * @param curY      The y coordinate of the entity which this shape is part of
     * @param offsetX   The x-offset from the center
     * @param offsetY   The y-offset from the center
     */
    void draw(Canvas canvas, int curX, int curY, int offsetX, int  offsetY);

    /**
     * Reports whether this shape has collided with the other.
     *
     * @param other The other shape that will be checked if it has collided with this shape
     * @return  Returns true if this shape is collided (intersecting) with the other
     */
    boolean collided(Shape other);

    boolean isWithin(double curX, double curY);

}
