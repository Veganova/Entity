package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public class Barrel extends Entity{

    public enum BarrelType {
        SINGLE, LAZER, SIDE, SPIRIT_BOMB
    }

    AShape shape;
    private MyWorld world;
    private double magnitude;
    private Projectile projectile;

    Barrel(Projectile projectile, BarrelType type, Vector2 location, MyWorld world, double angle) {
        super(0, 0, 0, 0, 0, false);
        initBarrel(type, location, world, angle);
        this.world = world;
        this.projectile = projectile;
        this.world.objectDatabase.put(this.shape.body, this);
    }

    private void initBarrel(BarrelType type, Vector2 location, MyWorld world, double angle) {
        switch (type) {
            case SINGLE:
                //magnitude needs to be specified here
                this.shape = new ObjRectangle(location.x, location.y, 120, 20, world);
                magnitude = 150;
                break;
            case SIDE:
                this.shape = new ObjRectangle(50 + location.x, 100+location.y, 120, 20, world);
                magnitude = 50;

                this.shape.rotateBody(angle);
                this.shape.body.getTransform().rotate(1);
                break;
        }

    }

    protected void fire() {
        double angle = shape.body.getTransform().getRotation();
        double x, y;
        //magnitude of 90 away
        x = magnitude * Math.cos(angle) + MyWorld.SCALE*shape.body.getWorldCenter().x;
        y = magnitude * Math.sin(angle) + MyWorld.SCALE*shape.body.getWorldCenter().y;
        angle = Math.toDegrees(angle);
        this.projectile.returnCustomizedCopy(this.projectile, new Vector2(x,y), angle, 30, this.world);
        this.shape.body.setAsleep(false);
    }

    @Override
    public void draw(Canvas canvas){
        this.shape.draw(canvas);
    }


}
