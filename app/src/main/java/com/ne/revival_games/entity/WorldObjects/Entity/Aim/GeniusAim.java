package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

/**
 * Created by vishn on 12/27/2017.
 */

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.FrameTime;
import com.ne.revival_games.entity.WorldObjects.MyCollections.Database;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.Iterator;

/**
 * Created by Veganova on 8/30/2017.
 */

public class GeniusAim extends SimpleAim {
    public static double WIGGLE_ROOM = 0.1;
    private double turnSpeed = 10;
    private Entity enemy;
    private boolean first = false;
    private int lastDirection;
    private double projectileSpeed = 0;
    private double waitTime = 0;
    private double getToAngle = 0;
    public static double angleCheck = 0.05;
    private State state = State.not_executing;
    public double execTime = Double.MAX_VALUE;
    protected Vector2 tryPoint;

    public GeniusAim(AimableEntity aimEntity, Database objectDatabase, double range, double projectileSpeed) {
        super(aimEntity, objectDatabase, range, false);
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Takes the part to aim with.
     *
     * @param aimWith
     */
    @Override
    public void aim(Entity aimWith) {

        if (enemy == null || enemy.health <= 0 || enemy.dead
                || Util.getDistance(this.enemy.shape.body.getWorldCenter(),
                this.aimEntity.shape.body.getWorldCenter()) > this.range) {
            enemy = null;
        }

        // if no enemy, choose one
        if (enemy == null) {
            this.choose();
            this.state = State.not_executing;
        }

        // if even after choosing it is still null, no targetable enemy
        if (enemy == null) {
            return;
        }

        Paint paint =  new Paint();
        paint.setColor(Color.YELLOW);

        enemy.shape.paint = paint;

        if(this.state == State.executing) {

            //turning
            turnToward(aimWith);
            this.state = State.waiting_callback;

            //waiting, then fire
             FrameTime.getReference().addCallBackAtDeltaFrames((long) waitTime,
                     new Runnable() {
                         @Override
                         public void run() {
                             aimEntity.fire();
                             state = State.not_executing;
                         }
                     });
        }
        else if(state == State.waiting_callback) {
            turnToward(aimWith);
        }
        else {


            double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                    + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

            calculateTrajectory(aimWith, angle);

            this.state = State.executing;
        }





    }

    private void turnToward(Entity aimWith) {
        Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

        Vector2 targetPoint = tryPoint;

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

        if (first) {
            first = false;
        } else if (turnCounterClock != this.lastDirection){
            this.turnSpeed /= 2;
        } else {
            this.turnSpeed = this.aimEntity.getTurnSpeed();
        }

        this.lastDirection = turnCounterClock;

        if (Math.abs(angleDifference) <= WIGGLE_ROOM) {

            this.aimEntity.freezeAngularForces();
            //(angle + angleTo)/2 % (2*Math.PI)
//            System.out.println("ANGLETO: " + angleTo + " " + aimWith.shape.body.getTransform().getRotation());
            if(!Util.nearValue(aimWith.shape.body.getTransform().getRotation(), angleTo, 0.001)) {
                aimWith.rotateEntity(angleTo);
                this.aimEntity.freezeAngularForces();
            }
        } else {
            this.aimEntity.shape.body.setAngularVelocity(turnCounterClock * turnSpeed);
        }
    }

    private Vector2 calculateTrajectory(Entity aimWith, double currentAngle) {
        //pair


        //timeStep
        double timeStep = 0.05;

//        //position to rotate to
//        double rotationTo = 0;
//
//        double thetaTargetCurrent = Util.absoluteAngle(this.aimEntity.shape.body.getWorldCenter(),
//                enemy.shape.body.getWorldCenter());

        tryPoint = enemy.shape.body.getWorldCenter().copy()
                .add(enemy.shape.body.getLinearVelocity().copy().multiply(timeStep));

//        //timeStep * sign
//        double increment = Math.signum(Util.absoluteAngle(this.aimEntity.shape.body.getWorldCenter(),
//                   tryPoint)) * angleCheck;
//
//
//        boolean seemsPossible = true;

        double timeToGuessedAngle = 0, timeProjectiletoGuessPoint = 0, timeMyMissiletoPoint = 0;
        for(int i = 0; i < 1000; ++i) {
            Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

           double toTarget = Math.atan2(tryPoint.y - centerofRotation.y, tryPoint.x - centerofRotation.x);


            //timeTo that angle
            timeToGuessedAngle = Math.abs(toTarget - currentAngle) % Math.PI / (turnSpeed);

            //target linear velocity
            Vector2 targetVelocity = enemy.shape.body.getLinearVelocity();
            //target current position
            Vector2 curpos = enemy.shape.body.getWorldCenter();

             timeProjectiletoGuessPoint = (tryPoint.distance(curpos) / targetVelocity.getMagnitude());
             timeMyMissiletoPoint = (tryPoint.distance(centerofRotation)-2/5) / (projectileSpeed);

//            System.out.println(getToAngle);


            if(timeMyMissiletoPoint + timeToGuessedAngle < timeProjectiletoGuessPoint) {
                waitTime = Math.abs(timeMyMissiletoPoint + timeToGuessedAngle - timeProjectiletoGuessPoint);
                getToAngle = toTarget;

                break;
            }

            tryPoint.add(enemy.shape.body.getLinearVelocity().copy().multiply(timeStep));

        }

        System.out.println("turn:" + timeToGuessedAngle + "fire: " + timeMyMissiletoPoint + "target: " + timeProjectiletoGuessPoint);

//        if(aimEntity instanceof Turret) {
//            Turret myTurret = (Turret) aimEntity;
//            ObjCircle target = new ObjCircle(15);
////            System.out.println("TRY-POINT:" + tryPoint.x + " " + tryPoint.y);
//            target.getBuilder(true, myTurret.world).setXY(tryPoint.x*MyWorld.SCALE, tryPoint.y*MyWorld.SCALE).init();
//            myTurret.drawme.add(target);
//        }


        return new Vector2(0,0);
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

    private enum State {
           executing, waiting_callback, not_executing
    }
}


