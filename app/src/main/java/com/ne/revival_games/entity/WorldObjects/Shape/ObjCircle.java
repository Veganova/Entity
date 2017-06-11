package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.AbstractShape;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Represents the simple shape circle class.
 */
public class ObjCircle extends AShape {

    private Circle circ;
    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param r  The radius
     */
    public ObjCircle(double x, double y, double r, MyWorld world) {
        this.circ = new Circle(r);
        initValues(this.circ, x, y, world);
    }

    @Override
    public void draw(Canvas canvas) {
        //might have to modify coordinates as needed
        Vector2 coord = this.body.getWorldCenter();
        canvas.drawCircle((float) coord.x,
                        (float) coord.y,
                        (float) this.circ.getRadius(), new Paint());
    }


    public void draw(Canvas canvas, Paint paint) {
        //might have to modify coordinates as needed
        Vector2 coord = this.body.getWorldCenter();
        canvas.drawCircle((float) coord.x,
                (float) coord.y,
                (float) this.circ.getRadius(), paint);
    }



    @Override
    public void rotate(double degrees) {
        return;
    }

}