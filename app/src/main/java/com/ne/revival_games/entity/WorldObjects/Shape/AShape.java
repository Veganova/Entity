package com.ne.revival_games.entity.WorldObjects.Shape;

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

    public Body body;
    /**
    * RADIANS
     */
    double angle;
    Convex convex;

    AShape() {

    }

    protected void initValues(Convex shape, double x, double y, MyWorld world) {
        x = x/MyWorld.SCALE;
        y = y/MyWorld.SCALE;
        this.body = new Body();
        this.convex = shape;
        //this part is only relevant to fixtures...
        body.addFixture(shape, 0.005D, 0.2D, 2.0D);
        body.setMass(MassType.NORMAL);

        body.translate(x, y);
        world.engineWorld.addBody(this.body);
        this.angle = 0;
    }

    protected void initValues(Convex shape, double x, double y, double fixtureangle) {
        x = x/MyWorld.SCALE;
        y = y/MyWorld.SCALE;
        this.convex = shape;
        this.angle = Math.toRadians(fixtureangle);
        this.rotateFixture(this.angle, new Vector2(0,0));
        this.translateFixture(x ,y);
    }



    protected void initValues(Body body, double x, double y, MyWorld world) {
        x = x/MyWorld.SCALE;
        y = y/MyWorld.SCALE;
        this.body = body;
        body.setMass(MassType.NORMAL);
        body.translate(x, y);
        this.angle = 0;
        world.engineWorld.addBody(this.body);
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
     * In radians (not degrees)
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
