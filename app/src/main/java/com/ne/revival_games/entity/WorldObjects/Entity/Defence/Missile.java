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


    public Missile(double x, double y, double direction, double speed,
                   MyWorld world, Team team, boolean addToWorld) {
        super(x, y, RADIUS, direction, speed, HEALTH, world, team, addToWorld);
        this.team = team;
    }


    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        boolean regular = super.onCollision(contact, componentHit, damage);

        if (!regular) {
            return false;
        }

        if(contact.team.opposite(this.team)){
            this.health = 0;
            this.invisible = true;
            return false;
        }

        return true;
    }

    @Override
    public Projectile returnCustomizedCopy(Projectile project,
                                           Vector2 location, double direction,
                                           double speed, MyWorld world, Team team) {
        return new Missile(location.x, location.y, direction, speed, world, team, true);
    }

}
