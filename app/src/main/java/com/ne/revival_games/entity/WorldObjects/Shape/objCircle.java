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
public class objCircle extends AShape {

    private double r;
    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param r  The radius
     */
    public objCircle(double x, double y, double r) {
        org.dyn4j.geometry.Circle cirShape = new org.dyn4j.geometry.Circle(r);
        this.body = new Body();
        body.addFixture(cirShape);
        body.setMass(MassType.NORMAL);
        body.translate(x, y);
        this.r = r;
    }

    @Override
    public void draw(Canvas canvas, double [] scale) {
        //might have to modify coordinates as needed
        canvas.drawCircle((float) translateXtoCanvas(this.body.getWorldCenter().x, scale[0]),
                (float) translateYtoCanvas(this.body.getWorldCenter().y, scale[1]),
                        (float) this.body.getRotationDiscRadius(), new Paint());
    }

}