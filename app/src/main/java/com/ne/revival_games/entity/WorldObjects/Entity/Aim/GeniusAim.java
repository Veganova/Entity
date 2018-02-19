package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

/**
 * Created by vishn on 12/27/2017.
 */

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Pair;
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

//TODO: get shooting distance somehow
public class GeniusAim extends SimpleAim {
    public static double WIGGLE_ROOM = 0.1;
    private double turnSpeed = 10;
    private Entity enemy;
    private double projectileSpeed = 0;
    private boolean approaching = false;
    private double timeAhead = 0.05;
    private double waitTime = 0;
    private State state = State.not_executing;
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
    public void aim(final Entity aimWith) {
        System.out.println(this.aimEntity.shape.body.getAngularVelocity());
        if (enemy == null || enemy.health <= 0 || enemy.dead
                || Util.getDistance(this.enemy.shape.body.getWorldCenter(),
                this.aimEntity.shape.body.getWorldCenter()) > this.range) {
            enemy = null;
        }

        // if no enemy, choose one
        if (enemy == null) {
            this.choose();
            timeAhead = 0.05;
            this.state = State.not_executing;
        }

        // if even after choosing it is still null, no targetable enemy
        if (enemy == null) {
            return;
        }

        Paint paint =  new Paint();
        paint.setColor(Color.YELLOW);

        enemy.shape.paint = paint;

        turnToward(aimWith);

        if(isShootingViable(aimWith)) {
            aimEntity.fire();
        }
    }

    private void turnToward(Entity aimWith) {

        Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

        Vector2 targetPoint = enemy.shape.body.getWorldCenter().copy()
                .add(enemy.shape.body.getLinearVelocity().copy().multiply(timeAhead));

        approaching = targetPoint.distance(centerofRotation)
                > enemy.shape.body.getWorldCenter().distance(centerofRotation);


        double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

        double angleTo = Math.atan2(targetPoint.y - centerofRotation.y,
                targetPoint.x - centerofRotation.x);

        angleTo = (2 * Math.PI + angleTo) % (2 * Math.PI);

//        aimWith.rotateEntity(angleTo);
        this.aimEntity.freezeAngularForces();

        double angleDifference = (Math.PI * 2 + angle - angleTo) % (Math.PI * 2);
        double counterclockDist = (Math.PI * 2 + angleTo - angle) % (Math.PI * 2);

        int turnCounterClock = -1;               //false

        if (counterclockDist < angleDifference) {
            angleDifference = counterclockDist;
            turnCounterClock = 1;
        }


        if (Math.abs(angleDifference) <= WIGGLE_ROOM) {

            this.aimEntity.freezeAngularForces();
            //(angle + angleTo)/2 % (2*Math.PI)
            if(!Util.nearValue(aimWith.shape.body.getTransform().getRotation(), angleTo, 0.001)) {
                this.aimEntity.rotate(angleTo);
                this.aimEntity.freezeAngularForces();
            }
        } else {
            this.aimEntity.setAngularVelocity(turnCounterClock * turnSpeed);
        }
    }

    public boolean isShootingViable(Entity aimWith) {

        //center of turret
        Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

        //current angle
        double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);


        //
        Vector2 targetVelocity =  enemy.shape.body.getLinearVelocity();

        double projSlope = Math.tan(angle);
        double targetSlope = targetVelocity.y/targetVelocity.x;


        Vector2 tryPoint = Util.intersectionBetweenTwoLines(projSlope, centerofRotation,
                targetSlope, enemy.shape.body.getWorldCenter());

        //checks to see if backwards along the line
        double futureOrCurrentTarg = (tryPoint.x - enemy.shape.body.getWorldCenter().x) / targetVelocity.x;
        double futureOrCurrentProj = (tryPoint.x - centerofRotation.x) / (projectileSpeed*Math.cos(angle));


        //if time is negative then return false
        if(futureOrCurrentTarg < 0 || futureOrCurrentProj < 0) {
            return false;
        }


        if(Util.nearValue(targetSlope, projSlope, 0.025)) {
            return true;
        }

        double timeProjToPoint = (tryPoint.distance(centerofRotation) - 6/5) / projectileSpeed;
        double timeTargToPoint = tryPoint.distance(enemy.shape.body.getWorldCenter()) /
                        targetVelocity.getMagnitude();

        if(Math.abs(timeProjToPoint - timeTargToPoint) > 0.03) {
            timeAhead += 0.0225;
        }
        if(timeAhead > 0.02 || approaching) {
            timeAhead -= 0.02;
        }


        return Util.nearValue(timeProjToPoint, timeTargToPoint, 0.01);
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

        this.turnSpeed = this.aimEntity.getTurnSpeed();
        this.enemy = possibleChoice;

    }

    private enum State {
           executing, waiting_callback, not_executing
    }
}







