package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;

/**
 * Represents the simple shape square.
 */
public class Rectangle extends AShape {


    Rectangle(int x, int y, int l, int w) {
        org.dyn4j.geometry.Rectangle rectShape = new org.dyn4j.geometry.Rectangle(l, w);
        Body rectangle = new Body();
        rectangle.addFixture(rectShape);
        rectangle.setMass(MassType.NORMAL);
        rectangle.translate(0.0, 2.0);
        rectangle.getLinearVelocity().set(x, y);
    }


    @Override
    public void draw(Canvas canvas) {
        // TODO: 6/7/2017
    }

}
