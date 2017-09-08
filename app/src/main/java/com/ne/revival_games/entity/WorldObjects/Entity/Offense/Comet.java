package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimShootEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimableEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.ImmediateAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Missile;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/10/2017.
 */

public class Comet extends AimShootEntity {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    public static int RADIUS = 10;
    private double range = 1000;

    public Comet(double x, double y, double direction, double speed, MyWorld world, Team team) {
        super(direction, speed, HEALTH, false, team);

        shape = new ObjCircle(RADIUS);
        shape.getBuilder(true, world).setXY(x, y).init();
//        shape.body.setBullet(true);
        world.objectDatabase.put(this.shape.body, this);
        this.logic = new ImmediateAim(this, world.objectDatabase, range);//new SeekerAim(this, world.objectDatabase, range, false);
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
    public Entity getPartToAimWith() {
        return this;
    }

    @Override
    public void fire() {
        super.fire();
        this.aiming = false;
    }

    @Override
    public int getTurnSpeed() {
        return 20;
    }

    @Override
    public double getShootingDistance() {
        return 0;
    }

    @Override
    public Entity getNewBodyToShoot(double x, double y, double angle) {
        return this;
    }
}
