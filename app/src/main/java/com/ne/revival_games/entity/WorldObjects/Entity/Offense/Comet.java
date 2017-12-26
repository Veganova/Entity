package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimShootEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimableEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.ImmediateAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Missile;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/10/2017.
 */

public class Comet extends Entity {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    private double range = 1000;
    public double radius;

    public Comet(double x, double y, double direction, double speed, MyWorld world, Team team, String tag) {
        super(direction, speed, team, tag + "comet");

        radius = MySettings.getNum(team.toString(), name_tag + " radius");
        shape = new ObjCircle(radius);
        shape.getBuilder(true, world).setXY(x, y)
                .setBasics(team.toString(), name_tag)
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
        String tag = name_tag;

        double lower_health =
                MySettings.getNum(team.toString(), tag + " health lower");
        double upper_health =
                MySettings.getNum(team.toString(), tag + " health upper");
        double size_lower =
                MySettings.getNum(team.toString(), tag + " radius lower");
        double size_upper =
                MySettings.getNum(team.toString(), tag + " radius upper");
        double speed_upper = MySettings.getNum(team.toString(), tag + " speed upper");
        double speed_lower = MySettings.getNum(team.toString(), tag + " speed lower");

        double ratio = (myComet.radius - size_lower) / (size_upper - size_lower);

        myComet.health = (int) (ratio * (upper_health-lower_health) + lower_health);

        double magnitude;
        magnitude = ratio * (speed_upper - speed_lower) + speed_lower;
        magnitude = speed_upper + speed_lower - magnitude;


        ghost.setInitialVelocity(magnitude, angle);
    }

//    @Override
//    public Entity getPartToAimWith() {
//        return this;
//    }
//
//    @Override
//    public void fire() {
//        super.fire();
//        this.aiming = false;
//    }
//
//    @Override
//    public double getTurnSpeed() {
//        return 20;
//    }
//
//    @Override
//    public double getShootingDistance() {
//        return 0;
//    }
//
//    @Override
//    public Entity getNewBodyToShoot(double x, double y, double angle) {
//        return this;
//    }
}
