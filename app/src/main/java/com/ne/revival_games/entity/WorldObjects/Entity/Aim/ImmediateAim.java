package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyCollections.Database;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.sql.SQLOutput;
import java.util.Iterator;

/**
 * Created by Veganova on 8/30/2017.
 */

public class ImmediateAim implements AimLogic {
    public static double WIGGLE_ROOM = 0.03;
    private double turnSpeed = 10;

    private final AimableEntity aimEntity;
    private final Database objectDatabase;
    private final double range;
    private Entity enemy;
    private boolean first = false;
    private int lastDirection;

    public ImmediateAim(AimableEntity aimEntity, Database objectDatabase, double range) {
        this.aimEntity = aimEntity;
        this.objectDatabase = objectDatabase;
        this.range = range;
    }

    /**
     * Takes the part to aim with.
     *
     * @param aimWith
     */
    @Override
    public void aim(Entity aimWith) {
        // if currently focused on a dead enemy, forget about it
        if (enemy == null || enemy.health <= 0 || enemy.dead
                || Util.getDistance(this.enemy.shape.body.getWorldCenter(),
                this.aimEntity.shape.body.getWorldCenter()) > this.range) {
            enemy = null;
        }

        // if no enemy, choose one
        if (enemy == null) {
            this.choose();
        }

        // if even after choosing it is still null, no targetable enemy
        if (enemy == null) {
            this.first = false;
            return;
        }

        Body enemyBody = enemy.shape.body;

        Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

        Vector2 targetPoint = enemyBody.getWorldCenter();

        double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

        double angleTo = Math.atan2(targetPoint.y - centerofRotation.y,
                targetPoint.x - centerofRotation.x);
        angleTo = (2 * Math.PI + angleTo) % (2 * Math.PI);

        double angleDifference = (Math.PI * 2 + angle - angleTo) % (Math.PI * 2);
        double counterclockDist = (Math.PI * 2 + angleTo - angle) % (Math.PI * 2);

        int turnCounterClock = -1;               //false

        if (counterclockDist < angleDifference) {
            angleDifference = counterclockDist;
            turnCounterClock = 1;
        }


        this.aimEntity.shape.body.getTransform().setRotation(angleTo);
        this.aimEntity.fire();

//        if (first) {
//            first = false;
//        } else if (turnCounterClock != this.lastDirection){
//            this.turnSpeed /= 2;
//        } else {
//            this.turnSpeed = this.aimEntity.getTurnSpeed();
//        }
//
//        this.lastDirection = turnCounterClock;
//
//        if (Math.abs(angleDifference) <= WIGGLE_ROOM) {
//            this.aimEntity.fire();
//
////            mainBarrel.shape.body.clearAccumulatedTorque();
////            mainBarrel.shape.body.clearAccumulatedForce();
//            aimWith.shape.body.setAngularVelocity(0);
//
//            this.aimEntity.shape.body.setAngularVelocity(0);
//        } else {
//            this.aimEntity.shape.body.setAngularVelocity(turnCounterClock * turnSpeed);
//        }
    }


    @Override
    public void choose() {
        Iterator<Entity> enemies = objectDatabase.getTeamIterator(aimEntity.team.getOpposite());

        Entity possibleChoice = null;

        Vector2 myPosition = this.aimEntity.shape.body.getWorldCenter();
        double distance = this.range;

        while (enemies.hasNext()) {
            Entity enemy = enemies.next();
            double otherDist = Util.getDistance(enemy.shape.body.getWorldCenter(), myPosition);
            if (distance >= otherDist && !enemy.ghost) {
                possibleChoice = enemy;
                distance = otherDist;
            }
        }

        this.first = true;
        this.turnSpeed = this.aimEntity.getTurnSpeed();
        this.enemy = possibleChoice;

    }

    private double incrementFunction(double angleDifference) {
        if (angleDifference == 0) {
            return 0;
        }
        return (0.5 / Math.pow(Math.E, Math.PI)) * Math.pow(Math.E, (angleDifference)) + 0.02;
    }
}
