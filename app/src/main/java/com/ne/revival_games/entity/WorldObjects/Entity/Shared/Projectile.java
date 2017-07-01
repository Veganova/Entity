package com.ne.revival_games.entity.WorldObjects.Entity.Shared;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

/**
 * Created by Veganova on 6/10/2017.
 */

public abstract class Projectile extends Entity {

    public static double MAX_RADIUS = 250;


    public Projectile(double x, double y, int r, double direction, double speed,
                      int health, MyWorld world) {
        super(x, y, direction, speed, health, false);
        shape = new ObjCircle(x, y, (double) r, world);
        world.objectDatabase.put(this.shape.body, this);
        this.setVelocity(this.speed);
    }

    public Projectile(double direction, double speed, int health, MyWorld world) {
        super(0, 0, direction, speed, health, false);
    }

//    public Projectile duplicate() {
//
//    }
//
//    public Projectile(Projectile projectile){
//        super(projectile.shape.getX(), projectile.shape.getY(), 0, 0, 0, false);
//        shape = new ObjCircle(projectile.shape.getX(), projectile.shape.getY(), (double) projectile.s, world);
//    }
}
