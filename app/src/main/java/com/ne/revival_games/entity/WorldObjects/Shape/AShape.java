package com.ne.revival_games.entity.WorldObjects.Shape;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;

/**
 * Created by Veganova on 6/7/2017.
 */
public abstract class AShape implements Shape {

    public Body body;

    /**
    * RADIANS
     */
    double angle;

    AShape() {

    }

    protected void initValues(Convex shape, double x, double y, MyWorld world) {
        this.body = new Body();
        body.addFixture(shape, 1.0D, 0.2D, 2.0D);
        body.setMass(MassType.NORMAL);

        body.translate(x, y);
        world.engineWorld.addBody(this.body);
        this.angle = 0;
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
        System.out.println("Collided: " + this.body.isInContact(other.body));
        return this.body.isInContact(other.body);
    }

}
