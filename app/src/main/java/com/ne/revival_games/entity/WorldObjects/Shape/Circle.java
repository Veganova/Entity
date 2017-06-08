package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Represents the simple shape circle class.
 */
public class Circle extends AShape {

    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param r  The radius
     */
    Circle (int x, int y, int r) {
        org.dyn4j.geometry.Circle cirShape = new org.dyn4j.geometry.Circle(r);
        Body body = new Body();
        body.addFixture(cirShape);
        body.setMass(MassType.NORMAL);
        body.translate(x, y);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawCircle(this.getX(), this.getY(),
                this.body.getRotationDiscRadius(), new Paint());
        // TODO: 6/7/2017
    }

}