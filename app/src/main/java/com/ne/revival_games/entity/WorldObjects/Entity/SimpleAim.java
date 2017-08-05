package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.sql.SQLOutput;
import java.util.List;

/**
 * Created by Veganova on 6/29/2017.
 */
public class SimpleAim implements AimLogic {

    // Doesnt have to be a turret
    private Aimable turret;
    private Entity enemy;
    private double range;


    public SimpleAim(Aimable turret, double range) {
        this.turret = turret;
        this.range = range/MyWorld.SCALE;
    }

    private Team getTeam() {
        return ((Entity)this.turret).team;
    }

    @Override
    public void aim(Aimable barrel) {
        // if currently focused on a dead enemy, forget about it
        if (enemy == null || enemy.health <= 0 || enemy.team == getTeam()
                || Util.getDistance(this.enemy.shape.body.getWorldCenter(),
                ((Entity)this.turret).shape.body.getWorldCenter()) > this.range) {
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
        if(enemies.size() > 0) {
//            Vector2 myPosition = ((Entity)this.turret).shape.body.getWorldCenter();
//            int index = 0;
//            double distance = Util.getDistance(enemies.get(0).shape.body.getWorldCenter(), myPosition);
//            for(int x = 1; x < enemies.size(); x++){
//                double otherDist = Util.getDistance(enemies.get(x).shape.body.getWorldCenter(), myPosition);
//                if(distance > otherDist){
//                    index = x;
//                    distance = otherDist;
//                }
//            }
//            if(distance < this.range) {
//                this.enemy = enemies.get(index);
//                return;
//            }
            enemy = enemies.get(0);
            return;
        }
        this.enemy = null;

    }
}

