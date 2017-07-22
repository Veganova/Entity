package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 * Created by Veganova on 6/29/2017.
 */
public class SimpleAim implements AimLogic {

    // Doesnt have to be a turret
    private Aimable turret;
    private Entity enemy;


    public SimpleAim(Aimable turret) {
        this.turret = turret;
    }

    private Team getTeam() {
        return ((Entity)this.turret).team;
    }

    @Override
    public void aim(Aimable barrel) {
        // if currently focused on a dead enemy, forget about it
        if (enemy == null || enemy.health <= 0 || enemy.team == getTeam()) {
            enemy = null;
        }

        // if no enemy, choose one
        if (enemy == null) {
            this.choose();
        }

        // if even after choosing it is still null, no targetable enemy
        if (enemy == null) {
            return;
        }

        Body body = enemy.shape.body;
        Entity mainBarrel = (Entity) barrel;
        Vector2 centerofRotation = mainBarrel.shape.body.getWorldCenter();

        Vector2 targetPoint = body.getWorldCenter();

        double angle = (Math.PI * 2 + mainBarrel.shape.getOrientation()
                + mainBarrel.shape.body.getTransform().getRotation()) % (Math.PI * 2);

        double angleTo = Math.atan2(targetPoint.y - centerofRotation.y,
                targetPoint.x - centerofRotation.x);
        angleTo = (2 * Math.PI + angleTo) % (2 * Math.PI);

        double angleDifference = (Math.PI * 2 + angle - angleTo) % (Math.PI * 2);
        double counterclockDist = (Math.PI * 2 + angleTo - angle) % (Math.PI * 2);
        double turnCounterClock = -1;               //false

        if (counterclockDist < angleDifference) {
            angleDifference = counterclockDist;
            turnCounterClock = 1;
        }

        if (Math.abs(angleDifference) <= 0.03) {
            this.turret.fire(angleTo);
//            mainBarrel.shape.body.clearAccumulatedTorque();
//            mainBarrel.shape.body.clearAccumulatedForce();
            mainBarrel.shape.body.setAngularVelocity(0);
            if(mainBarrel instanceof Barrel){
                Barrel thisBarrel = ((Barrel) mainBarrel);
                if(thisBarrel.myTurret == null || thisBarrel.myTurret.shape == null){
                    return;
                }
                thisBarrel.myTurret.setMotion(0);
                return;
            }
            mainBarrel.shape.body.setAngularVelocity(0);
        }


        //better find a shortest distance algorithm
        if(mainBarrel instanceof Barrel){
            Barrel thisBarrel = ((Barrel) mainBarrel);
            if(thisBarrel.myTurret == null || thisBarrel.myTurret.shape == null){
                return;
            }
            thisBarrel.myTurret.setMotion(turnCounterClock * 10);
//            thisBarrel.shape.body.setAngularVelocity(turnCounterClock * 10);
            return;
        }
            mainBarrel.shape.body.setAngularVelocity(turnCounterClock *10);
        // System.out.println("ANGLE - " + mainBarrel.shape.body.getTransform().getRotation());
        //new Turret(new Vector2(-200, 100), 30, this);

    }

    private double incrementFunction(double angleDifference){
        if(angleDifference == 0){
            return 0;
        }
        return  (0.5 / Math.pow(Math.E, Math.PI)) * Math.pow(Math.E, (angleDifference)) + 0.02;
    }

    @Override
    public void choose() {
        Team team = getTeam();
        List<Entity> enemies = team.getOpposite().getTeamObjects();
        if (enemies.size() > 0) {
            this.enemy = enemies.get(0);
        }
    }
}

