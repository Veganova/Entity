package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

/**
 * Represents the simple shape square.
 */
public class ObjRectangle extends AShape {
    private double l;
    private double w;

    ObjRectangle(int x, int y, int l, int w) {
        Rectangle rectShape = new Rectangle(l, w);
        Body rectangle = new Body();
        rectangle.addFixture(rectShape);
        rectangle.setMass(MassType.NORMAL);
        rectangle.translate(0.0, 2.0);
        rectangle.getLinearVelocity().set(x, y);
        this.l = l;
        this.w = w;
    }


    @Override
    public void draw(Canvas canvas, double [] scale) {
        //left, top, right, bottom
        double bottom = translateYtoCanvas(this.getY() - 0.5*this.l, scale[1]);
        double left = translateXtoCanvas(this.getX()-0.5*this.w, scale[0]);
        double right = translateXtoCanvas(this.getX()+0.5*this.w, scale[0]);
        double top = translateYtoCanvas(this.getY() + 0.5*this.l, scale[1]);

        Rect rectangle = new Rect((int) left, (int) top, (int) right, (int) bottom);
        canvas.drawRect(rectangle, new Paint());
    }
}
