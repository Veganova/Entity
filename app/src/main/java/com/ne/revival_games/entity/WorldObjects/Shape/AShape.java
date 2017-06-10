package com.ne.revival_games.entity.WorldObjects.Shape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;

/**
 * Created by Veganova on 6/7/2017.
 */
public abstract class AShape implements Shape {

    public Body body;

    AShape() {

    }

    AShape(Convex shape, double x, double y) {
        this.body = new Body();
        body.addFixture(shape);
        body.setMass(MassType.NORMAL);
        body.translate(x, y);
    }

    @Override
    public double getX(){
        return this.body.getWorldCenter().x;
    }

    @Override
    public double getY(){
        return this.body.getWorldCenter().y;
    }

    @Override
    public boolean collided(AShape other){
        System.out.println("Collided: " + this.body.isInContact(other.body));
        return this.body.isInContact(other.body);
    }

    //input coordinates of the world and outputs canvas coordinates
    public static double translateXtoCanvas(double x, double scaleX) {
        return x + scaleX / 2;
    }

    //input coordinates of the world and then outputs canvas coordinates
    public static double translateYtoCanvas(double y, double scaleY) {
        return -1 * (y - scaleY / 2);
    }

    public static double translateXtoWorld(double x, double scaleX) {
        return x - scaleX / 2;
    }

    public static double translateYtoWorld(double y, double scaleY) {
        return -1 * (y - scaleY / 2);
    }
}
