package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/10/2017.
 */

public class Comet extends Projectile implements Aimable {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    public static int RADIUS = 10;
    private AimLogic logic;

    public Comet(double x, double y, double direction, double speed, MyWorld world, Team team) {
        super(x, y, RADIUS, direction, speed, HEALTH, world, team, true);
        this.team = team;
        this.logic = new SimpleAim(this, team);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
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
                                           double speed, MyWorld world, Team team){
        return new Comet(location.x, location.y, direction, speed, world, team);
    }

    @Override
    public void aim() {
        this.logic.aim(this);
    }

    @Override
    public void fire(double angle) {
        //get the heading
        double x = Math.cos(this.shape.body.getTransform().getRotation()) * 10;
        double y = Math.sin(this.shape.body.getTransform().getRotation()) * 10;
        this.shape.body.applyForce(new Vector2(x, y));
    }

    @Override
    public Vector2 getCenter() {
        return this.shape.body.getWorldCenter();
    }

    @Override
    public void changeLogicTo(AimLogic logic) {
        this.logic = logic;
    }

    @Override
    public boolean isSleeping() {
        return false;
    }
}
