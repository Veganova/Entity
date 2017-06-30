package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public class Barrel {

    public enum BarrelType {
        SINGLE, LAZER, SIDE, SPIRIT_BOMB
    }

    AShape shape;
    private MyWorld world;
    Barrel(BarrelType type, Vector2 location, MyWorld world, double angle) {
        initBarrel(type, location, world, angle);
        this.world = world;
    }

    private void initBarrel(BarrelType type, Vector2 location, MyWorld world, double angle) {
        switch (type) {
            case SINGLE:
                this.shape = new ObjRectangle(50 + location.x, location.y, 100, 20, world);
            case SIDE:
                this.shape = new ObjRectangle(50 + location.x, location.y, 300, 20, world);
                this.shape.rotateBody(angle);
        }

    }

    protected void fire() {
        double magnitude = 100;
        double angle = shape.body.getTransform().getRotation();
        double x, y;
        //magnitude of 90 away
        x = magnitude * Math.cos(angle) + MyWorld.SCALE*shape.body.getWorldCenter().x;
        y = magnitude * Math.sin(angle) + MyWorld.SCALE*shape.body.getWorldCenter().y;
        angle = Math.toDegrees(angle);
        new Missile(x, y, angle, 30, this.world);
    }

}
