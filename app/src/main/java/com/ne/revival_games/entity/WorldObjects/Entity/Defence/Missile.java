package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/10/2017.
 */

public class Missile extends Entity {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    public static int RADIUS = 10;


    public Missile(double x, double y, double direction, double speed,
                   MyWorld world, Team team) {
        super(direction, speed, HEALTH, false, team);
        shape = new ObjCircle(RADIUS);
        shape.getBuilder(true, world).setXY(x, y).init();
//        shape.body.setBullet(true);
        world.objectDatabase.put(this.shape.body, this);
    }


//    @Override
//    public boolean onCollision(Entity contact, Body componentHit, double damage) {
//        boolean regular = super.onCollision(contact, componentHit, damage);
//
//        if (!regular || this.dead) {
//            return false;
//        }
//
//        if (contact.team.opposite(this.team)) {
//            this.applyDamage(damage, contact);
//            return false;
//        }
//
//        return true;
//    }


//    @Override
//    public double applyDamage(double damage, Entity from) {
//        this.dead = true;
//        this.invisible = true;
//        this.health = 0;
//        return super.applyDamage(damage, from);
//    }

}
