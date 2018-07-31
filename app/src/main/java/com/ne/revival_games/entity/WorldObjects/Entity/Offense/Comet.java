package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 7/10/2017.
 */

public class Comet extends Entity {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    private double range = 1000;
    public double radius;

    public Comet(double x, double y, double direction, double speed, MyWorld world, Team team) {
        super(direction, speed, team);

        radius = MySettings.getNum(team.toString(), new Query(this.getName(), "radius"));
        shape = new ObjCircle(radius);
        shape.getBuilder(true, world).setXY(x, y)
                .setBasics(team.toString(), this.getName())
                .init();
        world.objectDatabase.put(this.shape.body, this);
//        this.logic = new ImmediateAim(this,
//                world.objectDatabase, range);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        Entities.NEXUS.getDefaultLeaf();
        boolean regular = super.onCollision(contact, componentHit, damage);

        if (!regular || this.dead) {
            return false;
        }

        if (contact.team.opposite(this.team)) {
            this.applyDamage(damage, contact);
            return false;
        }

        return true;
    }

    @Override
    public double applyDamage(double damage, Entity from) {
        this.dead = true;
        this.invisible = true;
        this.health = 0;
        return super.applyDamage(damage, from);
    }

    @Override
    public void normalizeBot(GhostEntity ghost, double angle) {
        Comet myComet = this;

        double lower_health = MySettings.getNum(team.toString(), new Query(getName(), "health", "lower"));
        double upper_health = MySettings.getNum(team.toString(), new Query(getName(), "health", "upper"));
        double size_lower   = MySettings.getNum(team.toString(), new Query(getName(), "radius", "lower"));
        double size_upper   = MySettings.getNum(team.toString(), new Query(getName(), "radius", "upper"));
        double speed_upper  = MySettings.getNum(team.toString(), new Query(getName(), "speed", "upper"));
        double speed_lower  = MySettings.getNum(team.toString(), new Query(getName(), "speed" , "lower"));

        double ratio = (myComet.radius - size_lower) / (size_upper - size_lower);

        myComet.health = (int) (ratio * (upper_health-lower_health) + lower_health);

        double magnitude;
        magnitude = ratio * (speed_upper - speed_lower) + speed_lower;
        magnitude = speed_upper + speed_lower - magnitude;

        ghost.setInitialVelocity(magnitude, angle);
    }
}
