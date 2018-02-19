package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Veganova on 6/7/2017.
 */
public abstract class AShape implements Shape {

    private static final float STROKE_WIDTH = 0.1f;
    public Body body;
    public List<Convex> myConvexes = new ArrayList<>();

    /**
    * RADIANS
     */
    protected double angle;
    public Convex convex;
    public Paint paint;

    AShape() {
        this.paint = new Paint();
        this.paint.setColor(Color.GREEN);
        this.paint.setAntiAlias(true);
    }


    //a + b = c
    //b = c - a
    public void setBodyPosition(Vector2 pos, Vector2 curCenter) {
        Vector2 newHeading = pos.difference(curCenter);
        body.translate(newHeading.x, newHeading.y);
    }

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
        private double density = 0.5D, friction = 0.2D, restitution = 0.5D;
        private double linearDamping = Body.DEFAULT_LINEAR_DAMPING;
        private double angularDamping = Body.DEFAULT_ANGULAR_DAMPING;
        private double angle = 0.0D;
        private boolean premadeBody = false;
        private MassType type = MassType.NORMAL;
        private Mass mass;

        private InitBuilder(boolean body, MyWorld world, Convex shape) {
            this.isBody = body;
            this.world = world;
            myConvexes.add(convex);
            convex = shape;
        }

        private InitBuilder(MyWorld world, Body premade) {
            setBody(premade);
            this.world = world;
        }


        public InitBuilder setXY(double x, double y) {
            this.x = x / MyWorld.SCALE;
            this.y = y / MyWorld.SCALE;
            return this;
        }

        public InitBuilder setLinearDamping(double dampingfactor) {
            this.linearDamping = dampingfactor;
            return this;
        }

        public InitBuilder setAngularDamping(double dampingfactor) {
            this.angularDamping = dampingfactor;
            return this;
        }

        public void init() {

            // dealing with a part, not a whole body

            // dealing with a part, not a whole body (fixture)

            if (!isBody && !premadeBody) {
                rotateFixture(this.angle, new Vector2(0,0));
                translateFixture(x, y);

                HashMap<String, Double> valueMap = new HashMap<>();
                valueMap.put("friction", friction);
                valueMap.put("density", density);
                valueMap.put("restitution", restitution);
                convex.setUserData(valueMap);

//                world.engineWorld.addBody(body);
                // exit since the below deals with building a body
                return;
            }
            // 1st aoeJoint (ex. complex shape)
            else if (!premadeBody) {
                translateFixture(-1 * convex.getCenter().x, -1 * convex.getCenter().y);

                body = new Body();
                rotateBody(this.angle);
                body.addFixture(convex, density, friction, restitution);
            }
            else{
                rotateBody(this.angle);
            }
            // premade body

            body.setAutoSleepingEnabled(false);
            body.rotate(angle);

            if (mass !=null) {
                body.setMass(mass);
            } else {
                body.setMass(type);
            }
            setBodyPosition(new Vector2(x, y), body.getWorldCenter());
            // TODO: 7/4/2017 could be adding several times..
            body.setLinearDamping(linearDamping);
            body.setAngularDamping(angularDamping);
            world.engineWorld.addBody(body);

        }

        public InitBuilder setAngle(double fixtureangle) {
            this.angle = Math.toRadians(fixtureangle);
            return this;
        }

        public InitBuilder setBasics(String team, String name) {
            this.setAngularDamping(MySettings.getNum(team, name + " angular_damping"));
            this.setLinearDamping(MySettings.getNum(team, name + " linear_damping"));
            this.setDensity(MySettings.getNum(team, name + " density"));
            this.setRestitution(MySettings.getNum(team, name + " restitution"));
            this.setFriction(MySettings.getNum(team, name + " friction"));
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

        public InitBuilder setMassType(double mass) {
            this.mass = new Mass(new Vector2(x, y), mass, 1);
            return this;
        }

        public InitBuilder setMassType(MassType type) {
            this.type = type;
            return this;
        }

        public void initFixture(BodyFixture fixture){
            fixture.setRestitution(restitution);
            fixture.setDensity(density);
            fixture.setFriction(friction);
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
    public Paint getPaint() {
        return this.paint;
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
