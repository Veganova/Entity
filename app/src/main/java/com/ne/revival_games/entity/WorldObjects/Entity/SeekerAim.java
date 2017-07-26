package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 * Created by vishn on 7/26/2017.
 */

public class SeekerAim implements AimLogic {
    private Entity enemy;
    private Aimable mainBody;
    private double range;

    public SeekerAim(Aimable mainBody, double range) {
        this.mainBody = mainBody;
        this.range = range/ MyWorld.SCALE;
    }

    private Team getTeam() {
        return ((Entity)this.mainBody).team;
    }

    @Override
    public void aim(Aimable mainBarrel) {
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

        mainBody.fire(angleTo);
    }

    @Override
    public void choose() {
        Team team = getTeam();
        List<Entity> enemies = team.getOpposite().getTeamObjects();
        if(enemies.size() > 0){
            Vector2 myPosition = ((Entity)this.mainBody).shape.body.getWorldCenter();
            int index = 0;
            double distance = Util.getDistance(enemies.get(0).shape.body.getWorldCenter(), myPosition);
            for(int x = 1; x < enemies.size(); x++){
                double otherDist = Util.getDistance(enemies.get(x).shape.body.getWorldCenter(), myPosition);
                if(distance > otherDist){
                    index = x;
                    distance = otherDist;
                }
            }
            if(distance < this.range) {
                this.enemy = enemies.get(index);
                return;
            }
        }
        this.enemy = null;
    }
}
