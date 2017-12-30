package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

/**
 * Created by vishn on 12/27/2017.
 */

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyCollections.Database;

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

        if(this.state == State.executing) {

            //turning
            turnToward(aimWith);

            //waiting
            if(waitTime >= System.currentTimeMillis()) {
                System.out.println(waitTime);
            }
            //fire
            else {
                this.aimEntity.fire();
                this.state = State.not_executing;
            }
        }
        else {


            double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                    + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

            calculateTrajectory(angle);

            this.state = State.executing;
        }





    }

    private void turnToward(Entity aimWith) {
        double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

        double angleDifference = (Math.PI * 2 + angle - getToAngle) % (Math.PI * 2);
        double counterclockDist = (Math.PI * 2 + getToAngle - angle) % (Math.PI * 2);

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
            //(angle + getToAngle)/2 % (2*Math.PI)
            if(!Util.nearValue(aimWith.shape.body.getTransform().getRotation(), getToAngle, 0.001)) {
                aimWith.rotateEntity(getToAngle);
                this.aimEntity.freezeAngularForces();
            }
        } else {
            this.aimEntity.shape.body.setAngularVelocity(turnCounterClock * turnSpeed);
        }
    }

    private Vector2 calculateTrajectory(double currentAngle) {
        //pair


        //timeStep
        double timeStep = 0.05;

        //position to rotate to
        double rotationTo = 0;


        double thetaTargetCurrent = Util.absoluteAngle(this.aimEntity.shape.body.getWorldCenter(),
                enemy.shape.body.getWorldCenter());

        Vector2 tryPoint = enemy.shape.body.getWorldCenter()
                .add(enemy.shape.body.getLinearVelocity()).multiply(timeStep);

        //timeStep * sign
        double increment = Math.signum(Util.absoluteAngle(this.aimEntity.shape.body.getWorldCenter(),
                   tryPoint)) * angleCheck;


        boolean seemsPossible = true;

        for(int i = 0; i < 1000; ++i) {
            double toTarget = Util.absoluteAngle(this.aimEntity.shape.body.getWorldCenter(), tryPoint);

            //timeTo that angle
            double timeToGuessedAngle = Math.abs(toTarget - currentAngle) % Math.PI / (turnSpeed);

            //target linear velocity
            Vector2 targetVelocity = enemy.shape.body.getLinearVelocity();
            //target current position
            Vector2 curpos = enemy.shape.body.getWorldCenter();

            //y = getLinearVelocity.y * (t) + current _ y
            //x = getLinearVelocity.x * (t) + current_x, t = (x - current_x) / linearvelocity x
            double slope_target =  targetVelocity.y / targetVelocity.x;
            double start =  curpos.y - (slope_target * curpos.x);

            //TODO: other_start is not accurate since the thing is fired a bit away
            double other_slope = this.projectileSpeed*(Math.tan(toTarget));
            double other_start = 0;

            Vector2 guessPoint = Util.intersectionBetweenTwoLines(new Vector2(slope_target, start),
                    new Vector2(other_slope, other_start));

//            System.out.println("LINE ONE: " + slope_target + "x " + start);
//            System.out.println("LINE TWO: " + other_slope + "x " + other_start);


            double timeProjectiletoGuessPoint = (guessPoint.x - curpos.x) / targetVelocity.x;
            double timeMyMissiletoPoint = (guessPoint.x - 30*Math.cos(toTarget))
                    / (projectileSpeed*Math.cos(toTarget));

            System.out.println(timeProjectiletoGuessPoint + " " + timeMyMissiletoPoint);
            if(timeMyMissiletoPoint + timeToGuessedAngle < timeMyMissiletoPoint) {
                waitTime = timeMyMissiletoPoint + timeToGuessedAngle - timeProjectiletoGuessPoint;
                getToAngle = toTarget;

                break;
            }



            tryPoint = tryPoint.add(aimEntity.shape.body.getLinearVelocity().multiply(timeStep));

        }



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
           executing, not_executing
    }
}


