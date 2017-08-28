package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import android.provider.ContactsContract;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyCollections.Database;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.Iterator;
import java.util.List;


/**
 * Created by vishn on 7/26/2017.
 */

public class SeekerAim implements AimLogic {
    private Entity enemy;
    private Aimable mainBody;
    private final Database data;
    private double range;
    public boolean orientable = false;

    public SeekerAim(Aimable mainBody, Database data, double range, boolean orientable) {
        this.mainBody = mainBody;
        this.data = data;
        this.range = range/ MyWorld.SCALE;
        this.orientable = orientable;
    }

    private Team getTeam() {
        return ((Entity)this.mainBody).team;
    }

    @Override
    public void aim(Aimable missile) {
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
        Entity myTurret = (Entity) mainBody;

        Vector2 centerofRotation = myTurret.shape.body.getWorldCenter();
        Vector2 targetPoint = body.getWorldCenter();

        double angleTo = Math.atan2(targetPoint.y - centerofRotation.y,
                targetPoint.x - centerofRotation.x);

        if(!orientable){
            mainBody.fire(angleTo);
        }
        else {
            Entity thisMissile = ((Entity) missile);
            double angle = (Math.PI * 2 + thisMissile.shape.getOrientation()
                    + thisMissile.shape.body.getTransform().getRotation()) % (Math.PI * 2);

            double angleDifference = (Math.PI * 2 + angle - angleTo) % (Math.PI * 2);
            double counterclockDist = (Math.PI * 2 + angleTo - angle) % (Math.PI * 2);
            double turnCounterClock = -1;               //false


            if (counterclockDist < angleDifference) {
                angleDifference = counterclockDist;
                turnCounterClock = 1;
            }

            mainBody.fire(turnCounterClock*angleDifference/4);
        }

    }

    private double incrementFunction(double angleDifference){
        if(Util.nearValue(angleDifference, 0, 0.01)){
            return 0;
        }
        return  (0.5 / Math.pow(Math.E, Math.PI)) * Math.pow(Math.E, (angleDifference));
    }

    @Override
    public void choose() {
        Team team = getTeam();
//        List<Entity> enemies = team.getOpposite().getTeamObjects();
        Iterator<Entity> enemies = data.getTeamIterator(team);
        Entity possibleChoice = null;
        // checks if there is a single element
        if(enemies.hasNext()){
            Vector2 myPosition = ((Entity)this.mainBody).shape.body.getWorldCenter();
            double distance = this.range + 1;//Util.getDistance(enemies.get(0).shape.body.getWorldCenter(), myPosition);

            while(enemies.hasNext()) {
                Entity enemy = enemies.next();
                double otherDist = Util.getDistance(enemy.shape.body.getWorldCenter(), myPosition);
                if(distance > otherDist){
                    possibleChoice = enemy;
                    distance = otherDist;
                }
            }
//            if(distance < this.range) {
//                this.enemy = possibleChoice;
//                return;
//            }
        }
        this.enemy = possibleChoice;
    }
}
