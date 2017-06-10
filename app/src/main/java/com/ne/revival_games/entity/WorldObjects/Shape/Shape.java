package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;

/**
 * A interface representing different shapes.
 */
public interface Shape {

    double getX();

    double getY();

    void draw(Canvas canvas);

    boolean collided(AShape other);

}
