package com.ne.revival_games.entity.WorldObjects.Shape;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 6/7/2017.
 */
public abstract class AShape implements Shape {

    Body body;

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
        return this.body.isInContact(other.body);
    }

}
