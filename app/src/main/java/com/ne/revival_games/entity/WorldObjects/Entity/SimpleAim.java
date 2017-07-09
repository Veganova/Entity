package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */
public class SimpleAim implements AimLogic {

    // Doesnt have to be a turret
    private Aimable turret;

    public SimpleAim(Aimable turret) {
        this.turret = turret;
    }

    @Override
    public void aim(Body body, Barrel mainBarrel) {

        if(mainBarrel.isSleeping()){
            return;
        }

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
            mainBarrel.shape.body.setAsleep(true);
            return;
        }

        //better find a shortest distance algorithm
        mainBarrel.shape.body.rotate(turnCounterClock * incrementFunction(Math.abs(angleDifference)),
                this.turret.getCenter().x, this.turret.getCenter().y);

    }

    private double incrementFunction(double angleDifference){
        if(angleDifference == 0){
            return 0;
        }
        return  (0.5 / Math.pow(Math.E, Math.PI)) * Math.pow(Math.E, (angleDifference)) + 0.02;
    }
}

