package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

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
    public void aim(Body body, AShape mainBarrel) {
        
        Vector2 centerofRotation = mainBarrel.body.getWorldCenter();
        Vector2 pointonX = new Vector2(centerofRotation.x + 50/ MyWorld.SCALE, centerofRotation.y);
        Vector2 targetPoint = body.getWorldCenter();

        double angle = (Math.PI*2 + mainBarrel.getOrientation()
                + mainBarrel.body.getTransform().getRotation()) % (Math.PI * 2);

        double getSign = (targetPoint.y - centerofRotation.y);
        double a = Util.getDistance(centerofRotation, pointonX);
        double b = Util.getDistance(centerofRotation, targetPoint);
        double c = Util.getDistance(pointonX, targetPoint);
        double angleTo;

        //should break on 180 degrees but who knows?
        if(getSign == 0){
            if(Math.abs(angle - Math.PI) <= 0.2 && targetPoint.x - centerofRotation.x < 0){
                mainBarrel.body.setAsleep(true);
                angleTo = 0;
            }
            else if(Math.abs(angle) <= 0.2 && targetPoint.x - centerofRotation.x > 0){
                mainBarrel.body.setAsleep(true);
                angleTo = 0;
            }
            else{
                angleTo = Math.PI;
            }

        }
        else{
            angleTo = getSign/Math.abs(getSign)*Util.lawofCosines(a,b,c);
            angleTo = (Math.PI *2 + angleTo) % (Math.PI *2);
        }

        double angleDifference = angleTo - angle;
        double counterclockDist = (Math.PI * 2 + angleTo - angle) % (Math.PI*2);

//        System.out.println("Current angle: " + Math.toDegrees(angle));
//        System.out.println("angleTo: " + Math.toDegrees(angleTo));
//        System.out.println("angle difference: " + angleDifference);


        //better find a shortest distance algorithm
        if (Math.abs(angleDifference) <= 0.02){
            this.turret.fire(angleTo);
        }
        else {
            double increment = 0.15;
            if(Math.abs(angleDifference) <= 0.15){
                increment = 0.09;
            }
            if(Math.abs(angleDifference) <= 0.09){
                increment = 0.07;
            }
            if(Math.abs(angleDifference)<= 0.07){
                increment = 0.05;
            }
            if(Math.abs(angleDifference)<= 0.05){
                increment = 0.03;
            }
            if (counterclockDist <= Math.PI){

                mainBarrel.body.rotate(increment, this.turret.getCenter().x, this.turret.getCenter().y);
            }
            else{
                mainBarrel.body.rotate(-1*increment, this.turret.getCenter().x, this.turret.getCenter().y);
            }
        }

    }
}
