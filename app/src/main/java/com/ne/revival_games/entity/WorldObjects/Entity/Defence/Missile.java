package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

/**
 * Created by Veganova on 6/10/2017.
 */

public class Missile extends Entity {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    public static int RADIUS = 10;
    public static double LINEAR_DAMPING = 0;
    public static double ANGULAR_DAMPING = 0;
    public static double lifeTime = 15000;



    public Missile(double x, double y, double direction, double speed,
                   MyWorld world, Team team, String tag) {
        super(direction, speed, team, tag + "missile");
        shape = new ObjCircle(RADIUS);
        shape.getBuilder(true, world).setXY(x, y)
                .setBasics(team.toString(), name_tag)
                .init();
//        shape.body.setBullet(true);
        world.objectDatabase.put(this.shape.body, this);
        this.prime();
    }


    @Override
    protected boolean deathCondition() {
        boolean val = this.startTime + this.lifeTime <= System.currentTimeMillis() || this.shape.body.getLinearVelocity().getMagnitude() < 2;
        System.out.println("DEAD: " + val);

        return val;
    }
}
