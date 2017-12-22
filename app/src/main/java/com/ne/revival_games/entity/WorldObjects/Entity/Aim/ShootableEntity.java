package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Sounds;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 8/29/2017.
 */

public abstract class ShootableEntity extends Entity implements Shootable {

    // default
    private double shootingSpeed = 30;

    public ShootableEntity(double direction, double speed, int health, boolean invulnerable, Team team) {
        super(direction, speed, health, invulnerable, team, DEFAULT_FRICTION);
    }

    public ShootableEntity(double direction, double speed, int health, boolean invulnerable, Team team, double frictionCoefficient) {
        super(direction, speed, health, invulnerable, team, frictionCoefficient);
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

        Sounds.getInstance().playSound(Sounds.SOUND_TYPE.SHOOT);


//        this.projectile.returnCustomizedCopy(this.projectile, new Vector2(x,y), angle, 30, this.world, team);
//
//        this.shape.body.setAsleep(false);
//        this.sleepUntil = System.currentTimeMillis() + this.projectile.getSleepTime();
    }

    public void setShootingSpeed(double speed) {
        this.shootingSpeed = speed;
    }

    public abstract double getShootingDistance();

    public abstract Entity getNewBodyToShoot(double x, double y, double angle);
}
