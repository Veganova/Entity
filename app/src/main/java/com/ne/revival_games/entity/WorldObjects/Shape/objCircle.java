package com.ne.revival_games.entity.WorldObjects.Shape;

import android.content.Entity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.GamePanel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.AbstractShape;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Represents the simple shape circle class.
 */
public class ObjCircle extends AShape {

    private double r;

    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param r  The radius
     */
    public ObjCircle(double x, double y, double r) {
        super(new Circle(r), x, y);
        this.r = r;
    }

    @Override
    public void draw(Canvas canvas) {
        //might have to modify coordinates as needed
        Vector2 coord = this.body.getWorldCenter();
        canvas.drawCircle((float) translateXtoCanvas(coord.x, GamePanel.WIDTH),
                        (float) translateYtoCanvas(coord.y, GamePanel.HEIGHT),
                        (float) this.body.getRotationDiscRadius(), new Paint());
    }

}