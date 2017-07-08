package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/10/2017.
 */

public class Missile extends Projectile {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    public static int RADIUS = 10;

    public Missile(double x, double y, double direction, double speed, MyWorld world) {
        super(x, y, RADIUS, direction, speed, HEALTH, world);
        TYPE = Team.DEFENCE;
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        if(contact.TYPE == Team.OFFENSE){
            this.health = 0;
            this.invisible = true;
            return false;
        }

        return true;
    }

    @Override
    public Projectile returnCustomizedCopy(Projectile project, Vector2 location, double direction, double speed, MyWorld world){
        return new Missile(location.x, location.y, direction, speed, world);
    }

}
