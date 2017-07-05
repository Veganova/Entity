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

    //private Convex circ;
    /**
     * Constructor for the square object. Create an init builder and finish the job.
     * @param r  The radius
     */
    public ObjCircle(double r) {
        r = r/MyWorld.SCALE;
        this.convex = new Circle(r);
    }

//    private ObjCircle(double x, double y, double r, double density, MyWorld world) {
//        r = r/MyWorld.SCALE;
//        this.circ = new Circle(r);
//        initValues(this.circ, x, y, density, world);
//    }
//
//    private ObjCircle(double x, double y, double r, double fixtureangle) {
//        r = r/MyWorld.SCALE;
//        this.circ = new Circle(r);
//        initValues(this.circ, x, y, fixtureangle);
//    }

//    public InitBuilder putInWorld(int x, int y, MyWorld world) {
//        return new InitBuilder(world)
//    }

    @Override
    public void draw(Canvas canvas) {
        //might have to modify coordinates as needed
        Vector2 coord = this.body.getWorldCenter();
        canvas.drawCircle((float) (coord.x + this.convex.getCenter().x - this.body.getLocalCenter().x),
                        (float) (coord.y + this.convex.getCenter().y - this.body.getLocalCenter().y),
                        (float) this.convex.getRadius(), this.paint);
    }


    @Override
    public double getOrientation(){
        //TODO: Orientation of the circle doesn't exist
        return 0;
    }


    public Convex getShape(){
        return this.convex;
    }


    public static class ObjCircleBuilder {

        private double x = 0, y =0, r = 10;


        ObjCircleBuilder setX(double x) {
            this.x = x;
            return this;
        }
        ObjCircleBuilder setY(double x) {
            this.y = x;
            return this;
        }
        ObjCircleBuilder setR(double x) {
            this.r = x;
            return this;
        }

        // Builder b = CircleBuilder
        // myCricle = b.setX(10).setY(1).setR(1).build();

    }

}