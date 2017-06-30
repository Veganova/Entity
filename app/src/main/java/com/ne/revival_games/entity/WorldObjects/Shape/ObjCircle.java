package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

/**
 * Represents the simple shape circle class.
 */
public class ObjCircle extends AShape {

    private Convex circ;
    /**
     * Constructor for the square object.
     *
     * @param x The x of the center of the shape
     * @param y The x of the center of the shape
     * @param r  The radius
     */
    public ObjCircle(double x, double y, double r, MyWorld world) {
        r = r/MyWorld.SCALE;
        this.circ = new Circle(r);
        initValues(this.circ, x, y, DEFAULT_DENSITY, world);
    }

    public ObjCircle(double x, double y, double r, double density, MyWorld world) {
        r = r/MyWorld.SCALE;
        this.circ = new Circle(r);
        initValues(this.circ, x, y, density, world);
    }

    public ObjCircle(double x, double y, double r, double fixtureangle) {
        r = r/MyWorld.SCALE;
        this.circ = new Circle(r);
        initValues(this.circ, x, y, fixtureangle);
    }

    @Override
    public void draw(Canvas canvas) {
        //might have to modify coordinates as needed
        Vector2 coord = this.body.getWorldCenter();
        canvas.drawCircle((float) (coord.x + this.circ.getCenter().x - this.body.getLocalCenter().x),
                        (float) (coord.y + this.circ.getCenter().y - this.body.getLocalCenter().y),
                        (float) this.circ.getRadius(), this.paint);
    }


    @Override
    public double getOrientation(){
        //TODO: Orientation of the circle doesn't exist
        return 0;
    }


    public Convex getShape(){
        return this.circ;
    }

}