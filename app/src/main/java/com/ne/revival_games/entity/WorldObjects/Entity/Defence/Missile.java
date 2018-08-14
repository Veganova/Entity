package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.FrameTime;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

/**
 * Created by Veganova on 6/10/2017.
 */

public class Missile extends Entity {
    public static double lifeTime = 320;

    public Missile(double x, double y, double angle, double speed, MyWorld world, Team team, Entity parent) {
        super(angle, speed, team, parent);
        shape = new ObjCircle(MySettings.getEntityNum(team.toString(), new Query(getName(), "radius"), true));
        shape.getBuilder(true, world).setXY(x, y)
                .setDensity(MySettings.getEntityNum(team.toString(), new Query(getName(), "density"), true))
                .setBasics(team.toString(), new Query(this.getName()))
                .init();
//        shape.body.setBullet(true);
        world.objectDatabase.put(this.shape.body, this);
        this.prime();
    }


    @Override
    protected boolean deathCondition() {
        boolean val = this.startTime + this.lifeTime < FrameTime.getTime()
                || this.shape.body.getLinearVelocity().getMagnitude() < 2;

        return val;
    }
}
