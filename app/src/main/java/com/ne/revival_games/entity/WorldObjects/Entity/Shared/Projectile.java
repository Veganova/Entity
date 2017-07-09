package com.ne.revival_games.entity.WorldObjects.Entity.Shared;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/10/2017.
 */

public abstract class Projectile extends Entity {

    public static double MAX_RADIUS = 250;
    public double barrel_sleepTime = 0;
    private boolean inWorld;


    public Projectile(boolean addToWorld, double x, double y, int r, double direction, double speed,
                      int health, MyWorld world) {
        super(x, y, direction, speed, health, false);
        this.inWorld = addToWorld;
        shape = new ObjCircle(r);
        if(addToWorld) {
            shape.getBuilder(true, world).setXY(x, y).init();

//        shape.body.setBullet(true);

            world.objectDatabase.put(this.shape.body, this);
            this.setVelocity(this.speed);
        }
        else {
            shape.getBuilder(false, world).setXY(x,y).init();
        }
    }


    public Projectile(boolean addedToWorld, double direction, double speed, int health, MyWorld world) {
        super(0, 0, direction, speed, health, false);
        this.inWorld = addedToWorld;
    }

    abstract public Projectile returnCustomizedCopy(Projectile project, Vector2 location,
                                                    double direction, double speed, MyWorld world);

    public double getSleepTime(){
        return this.barrel_sleepTime;
    }

}
