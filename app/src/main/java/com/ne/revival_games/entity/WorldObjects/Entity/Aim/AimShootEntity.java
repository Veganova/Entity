package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import android.media.MediaPlayer;

import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Sounds;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 8/30/2017.
 */

public abstract class AimShootEntity extends AimableEntity implements Shootable {

    // default
    private double shootingSpeed = 30;

    public AimShootEntity(double direction, double speed, int health, boolean invulnerable, Team team) {
        super(direction, speed, health, invulnerable, team);
    }

    @OverridingMethodsMustInvokeSuper
    public void fire() {
        //double angle = shape.body.getTransform().getRotation();
        double x, y, angle;
        double magnitude = getShootingDistance();
        //magnitude of 90 away
        angle = this.shape.body.getTransform().getRotation();
//        x = magnitude * Math.cos(angle) + MyWorld.SCALE*shape.body.getWorldCenter().x;
//        y = magnitude * Math.sin(angle) + MyWorld.SCALE*shape.body.getWorldCenter().y;
//
//        System.out.println("SHOOTING AT - " + Math.toDegrees(angle));
//        Entity e = getNewBodyToShoot(x, y, 0);

//        this.shape.body.rotate(angle, shape.body.getWorldCenter().x, shape.body.getWorldCenter().y);
//        this.direction = Math.toDegrees(angle);
        this.setVelocity(shootingSpeed, angle);

        Sounds.getInstance().playSound(Sounds.SOUND_TYPE.SHOOT);



//        this.projectile.returnCustomizedCopy(this.projectile, new Vector2(x,y), angle, 30, this.world, team);
//
//        this.shape.body.setAsleep(false);
//        this.sleepUntil = System.currentTimeMillis() + this.projectile.getSleepTime();
    }


    @Override
    public void setShootingSpeed(double speed) {
        this.shootingSpeed = speed;
    }

    public abstract double getShootingDistance();

    public abstract Entity getNewBodyToShoot(double x, double y, double angle);

}
