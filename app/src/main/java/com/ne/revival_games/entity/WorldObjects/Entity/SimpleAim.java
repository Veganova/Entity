package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 * Created by Veganova on 6/29/2017.
 */
public class SimpleAim implements AimLogic {

    // Doesnt have to be a turret
    private Aimable turret;
    private Team team;
    private Entity enemy;


    public SimpleAim(Aimable turret, Team team) {
        this.turret = turret;
        this.team = team;
    }

    @Override

    public void aim(Aimable barrel) {

        if(barrel.isSleeping()){
            return;
        }
        if (enemy == null || enemy.health <= 0) {
            this.choose();
        }
        Body body = enemy.shape.body;
        Entity mainBarrel = (Entity)barrel;
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

        if (Math.abs(angleDifference) <= 0.02) {
            this.turret.fire(angleTo);
//            mainBarrel.shape.body.setAsleep(true);
//            mainBarrel.shape.body.setAngularVelocity(0);
            return;
        }

        //better find a shortest distance algorithm
        mainBarrel.shape.body.setAngularVelocity(turnCounterClock *10);
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
        List<Entity> enemies = this.team.getOpposite().getTeamObjects();
        if (enemies.size() > 0) {
            this.enemy = enemies.get(0);
        }
    }
}

