package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

/**
 * A interface representing different shapes.
 */
public interface Shape {

    double getX();

    double getY();

    void draw(Canvas canvas);

    boolean collided(AShape other);

    Convex getShape();

    double getOrientation();

    /**
     * In degrees (not radians)
     *
     * @param degrees above
     */
    void rotateBody(double degrees);

    void rotateFixture(double radians, Vector2 location);

    void translateFixture(double x, double y);


}
