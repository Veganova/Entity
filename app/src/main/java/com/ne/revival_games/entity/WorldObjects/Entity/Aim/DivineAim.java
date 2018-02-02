package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SimpleAim;
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
 * Created by vishn on 1/4/2018.
 */

//TODO: get shooting distance somehow
public class DivineAim extends SimpleAim {

/**
 * Created by vishn on 12/27/2017.
 */


        public static double WIGGLE_ROOM = 0.1;
        private double turnSpeed = 10;
        private Entity enemy;
        private double projectileSpeed = 0;
        private double waitTime = 0;
        private boolean first = true;
        private State state = State.not_executing;
        protected Vector2 tryPoint;

        public DivineAim(AimableEntity aimEntity, Database objectDatabase, double range, double projectileSpeed) {
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

            if (enemy == null || enemy.health <= 0 || enemy.dead
                    || Util.getDistance(this.enemy.shape.body.getWorldCenter(),
                    this.aimEntity.shape.body.getWorldCenter()) > this.range) {
                enemy = null;
            }

            // if no enemy, choose one
            if (enemy == null) {
                this.choose();
                this.first = true;
                this.state = State.not_executing;
            }

            // if even after choosing it is still null, no targetable enemy
            if (enemy == null) {
                return;
            }

            Paint paint =  new Paint();
            paint.setColor(Color.YELLOW);

            enemy.shape.paint = paint;

            //get out of this state asap
            if(this.state == State.not_executing){


                this.state = State.executing;
                double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                        + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

                //tryPoint is calculated here
                calculateTrajectory(aimWith, angle);
            }

            if(this.state == State.waiting_callback) {
                if(aimWith != null)
                    turnToward(aimWith);
            }
            else if(this.state == State.executing) {

                //turning
                this.state = State.waiting_callback;
                turnToward(aimWith);
                    //waiting, then fire
                    FrameTime.addCallBackAtDeltaFrames((long) waitTime,
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (aimEntity != null && !aimEntity.dead) {
                                        if (enemy != null && !enemy.dead) {
                                            turnToward(aimWith);
                                            aimEntity.fire();

                                            state = State.not_executing;
                                        }
                                    }
                                }
                            });
                }
            }
//        }



        private void turnToward(Entity aimWith) {
            if(aimWith == null || tryPoint == null) return;

            Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

            double angle = (Math.PI * 2 + aimWith.shape.getOrientation()
                    + aimWith.shape.body.getTransform().getRotation()) % (Math.PI * 2);

            double angleTo = Math.atan2(tryPoint.y - centerofRotation.y,
                    tryPoint.x - centerofRotation.x);

            angleTo = (2 * Math.PI + angleTo) % (2 * Math.PI);

            this.aimEntity.rotate(angleTo - angle);
            this.aimEntity.freezeAngularForces();
        }

        private void calculateTrajectory(Entity aimWith, double currentAngle) {

            //timeStep (seconds)
            double timeStep = 0.05;

            //point we're guessing
            tryPoint = enemy.shape.body.getWorldCenter().copy()
                    .add(enemy.shape.body.getLinearVelocity().copy().multiply(timeStep));


            double timeToGuessedAngle = 0, timeProjectiletoGuessPoint = 0, timeMyMissiletoPoint = 0;
            for(int i = 0; i < 5000; ++i) {
                Vector2 centerofRotation = aimWith.shape.body.getWorldCenter();

                double toTarget = Math.atan2(tryPoint.y - centerofRotation.y, tryPoint.x - centerofRotation.x);


                //timeTo that angle
                timeToGuessedAngle = 0;

                //target linear velocity
                Vector2 targetVelocity = enemy.shape.body.getLinearVelocity();
                //target current position
                Vector2 curpos = enemy.shape.body.getWorldCenter();

                timeProjectiletoGuessPoint = (tryPoint.distance(curpos) / targetVelocity.getMagnitude());
                timeMyMissiletoPoint = (tryPoint.distance(centerofRotation)-6/5) / (projectileSpeed);

//            System.out.println(getToAngle);


                if(timeMyMissiletoPoint + timeToGuessedAngle < timeProjectiletoGuessPoint) {
                    waitTime = Math.abs(timeMyMissiletoPoint + timeToGuessedAngle - timeProjectiletoGuessPoint);
                    waitTime *= 40;
                    waitTime = Math.ceil(waitTime - 0.5);
//                getToAngle = toTarget;

                    return;
                }

                tryPoint.add(enemy.shape.body.getLinearVelocity().copy().multiply(timeStep));

            }

            tryPoint = enemy.shape.body.getWorldCenter().copy()
                    .add(enemy.shape.body.getLinearVelocity().copy().multiply(timeStep));
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

