package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.Query;
import com.ne.revival_games.entity.WorldObjects.Sounds;

import org.dyn4j.geometry.Vector2;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 8/30/2017.
 */

public abstract class AimShootEntity extends AimableEntity implements Shootable {

    // default
    private double shootingSpeed = 30, acceleration = 0;
    private boolean isThrust = false;

    public AimShootEntity(double direction, double speed, Team team, boolean isThrust) {
        super(direction, speed, team);
        this.isThrust = isThrust;
        this.acceleration = MySettings.getNum(team.toString(), new Query(this.getName(),  "acceleration"));
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
        if(isThrust) {
            angle = (Math.PI * 2 + this.shape.getOrientation()
                    + this.shape.body.getTransform().getRotation()) % (Math.PI * 2);
            this.shape.body.applyForce(new Vector2(acceleration*Math.cos(angle),
                    acceleration*Math.sin(angle)));
        }
        else {
            this.setVelocity(shootingSpeed, angle);
        }

        Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.SHOOT);



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
