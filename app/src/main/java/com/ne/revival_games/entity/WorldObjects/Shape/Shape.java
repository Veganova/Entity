package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

import java.util.Iterator;

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

    void setPaint(Paint.Style style);

    void setColor(int color);

    Paint getPaint();
}
