package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 8/29/2017.
 */

public abstract class ShootableEntity extends Entity implements Shootable {

    // default
    private double shootingSpeed = 30;

    public ShootableEntity(double direction, double speed, Team team, String name) {
        super(direction, speed, team, name);
    }

    @OverridingMethodsMustInvokeSuper
    public void fire() {
        //double angle = shape.body.getTransform().getRotation();
        double x, y, angle;
        double magnitude = getShootingDistance();

        //magnitude of 90 away
        angle = this.shape.body.getTransform().getRotation();
        x = magnitude * Math.cos(angle) + MyWorld.SCALE*shape.body.getWorldCenter().x;
        y = magnitude * Math.sin(angle) + MyWorld.SCALE*shape.body.getWorldCenter().y;

        Entity shooting = getNewBodyToShoot(x, y, Math.toDegrees(angle));
        shooting.setVelocity(shootingSpeed);
        shooting.addToPlayer(this.player);
    }

    public void setShootingSpeed(double speed) {
        this.shootingSpeed = speed;
    }

    public abstract double getShootingDistance();

    public abstract Entity getNewBodyToShoot(double x, double y, double angle);
}
