package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/7/2017.
 */
public abstract class AShape implements Shape {

    private static final float STROKE_WIDTH = 0.1f;
    public static final double DEFAULT_DENSITY = 0.5D;
    public Body body;

    /**
    * RADIANS
     */
    private int test = 68;
    protected double angle;
    protected Convex convex;
    protected Paint paint;

    AShape() {
        this.paint = new Paint();
    }

    //a + b = c
    //b = c - a
    private void setBodyPosition(Vector2 pos, Vector2 curCenter) {
        Vector2 newHeading = pos.difference(curCenter);
        body.translate(newHeading.x, newHeading.y);
    }

//    protected void initValues(Convex shape, double x, double y, double density, MyWorld world) {
//        x = x/MyWorld.SCALE;
//        y = y/MyWorld.SCALE;
//        this.body = new Body();
//        this.convex = shape;
//        this.translateFixture(-1*convex.getCenter().x, -1*convex.getCenter().y);
//
//        body.addFixture(shape, density, 0.2D, 2.0D);
//        body.setMass(MassType.NORMAL);
//        //System.out.println(x + " "  + y);
//       // body.translateToOrigin();
//
//        setBodyPosition(new Vector2(x, y), convex.getCenter());
//        world.engineWorld.addBody(this.body);
//        this.angle = 0;
//    }
//
//    protected void initValues(Convex shape, double x, double y, double fixtureangle) {
//        x = x/MyWorld.SCALE;
//        y = y/MyWorld.SCALE;
//
//        this.convex = shape;
//        this.angle = Math.toRadians(fixtureangle);
//        this.rotateFixture(this.angle, new Vector2(0,0));
////        this.translateFixture(-1*convex.getCenter().x, -1*convex.getCenter().y);
//        translateFixture(x, y);
//    }
//
//    protected void initValues(Body body, double x, double y, MyWorld world) {
//        x = x/MyWorld.SCALE;
//        y = y/MyWorld.SCALE;
//
//        this.body = body;
//        body.setMass(MassType.NORMAL);
//
//
//        setBodyPosition(new Vector2(x, y), this.body.getWorldCenter());
//        this.angle = 0;
//        world.engineWorld.addBody(this.body);
//    }

    public InitBuilder getBuilder(boolean body, MyWorld world) {
        return new InitBuilder(body, world, convex);
    }

    public InitBuilder getBuilder(MyWorld world, Body premade) {
        return new InitBuilder(world, premade);
    }


    public class InitBuilder {

        private boolean isBody;
        private MyWorld world;
        private double x = 0, y = 0;

        // fixture values
        private double fx = 0, fy = 0;
        private double density = 0.5D, friction = 0.2D, restitution = 2.0D;
        private double angle = 0.0D;
        private boolean premadeBody = false;
        private MassType type = MassType.NORMAL;

        InitBuilder(boolean body, MyWorld world, Convex shape) {
            this.isBody = body;
            this.world = world;
            convex = shape;
        }

        InitBuilder(MyWorld world, Body premade) {
            setBody(premade);
            this.world = world;
        }

        /**
         * Do this last
         *
         * @param x
         * @param y
         * @return
         */
        public InitBuilder setXY(double x, double y) {
            this.x = x / MyWorld.SCALE;
            this.y = y / MyWorld.SCALE;
            return this;
        }

        public void init() {
            // dealing with a part, not a whole body
            if (!isBody && !premadeBody) {
                rotateFixture(this.angle, new Vector2(0,0));
                translateFixture(x, y);
                // exit since the below deals with building a body
                return;
            }
            // 1st init
            else if (!premadeBody) {
                translateFixture(-1 * convex.getCenter().x, -1 * convex.getCenter().y);
                rotateFixture(this.angle, new Vector2(0,0));
                body = new Body();
                body.addFixture(convex, density, friction, restitution);
            }
            // premade body

            body.setMass(type);
            setBodyPosition(new Vector2(x, y), body.getWorldCenter());
            // TODO: 7/4/2017 could be adding several times.. 
            world.engineWorld.addBody(body);

        }

        public InitBuilder setAngle(double fixtureangle) {
            this.angle = Math.toRadians(fixtureangle);
            return this;
        }

        public InitBuilder setConvex(Convex shape) {
            convex = shape;
            if (isBody) {
                body = new Body();
                translateFixture(-1 * convex.getCenter().x, -1 * convex.getCenter().y);
            }
            return this;
        }

        public InitBuilder setBody(Body abody) {
            body = abody;
            this.premadeBody = true;
            return this;
        }

        public InitBuilder setDensity(double density) {
            this.density = density;
            return this;
        }

        public InitBuilder setRestitution(double restitution) {
            this.restitution = restitution;
            return this;
        }
        public InitBuilder setFriction(double friction) {
            this.friction = friction;
            return this;
        }

        public InitBuilder setMassType(MassType type) {
            this.type = type;
            return this;
        }


    }


    @Override
    public void setPaint(Paint.Style style) {
        if (this.paint != null) {
            this.paint.setStyle(style);
            if (style == Paint.Style.STROKE) {
                this.paint.setStrokeWidth(STROKE_WIDTH);
            }
        }
    }

    @Override
    public void setColor(int color) {
        this.paint.setColor(color);
    }


    @Override
    public double getX() {
        return this.body.getWorldCenter().x;
    }

    @Override
    public double getY() {
        return this.body.getWorldCenter().y;
    }

    @Override
    public boolean collided(AShape other) {
//        System.out.println("Collided: " + this.body.isInContact(other.body));
        return this.body.isInContact(other.body);
    }

    @Override
    /**
     * In radians (not degrees).
     *
     * @param radians above
     */
    public void rotateBody(double radians) {
        this.angle += radians;
        this.body.rotate(radians, this.getX(), this.getY());
    }

    //fixture related functions
    @Override
    public Convex getShape(){
        return this.convex;
    }

    @Override
    public void rotateFixture(double radians, Vector2 location){
        this.convex.rotate(radians, location.x, location.y);
    }

    @Override
    public void translateFixture(double x, double y){
        this.convex.translate(x, y);
    }
}
