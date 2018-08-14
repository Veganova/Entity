package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.EMPEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.FrameTime;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

/**
 * Created by vishn on 7/27/2017.
 */

public class ShockwaveCanister extends Entity {
    public static int HEALTH = 20;
    private long lifeTime;
    boolean naturalDeath = false;

    public ShockwaveCanister(double x, double y, double angle, Team team, MyWorld world) {
        super(angle, 0, team);
        this.shape = new ObjRectangle(50, 20);
        this.lifeTime = (long) MySettings.getEntityNum(team.toString(), new Query(this.getName(), "lifetime"), true);
        this.shape.getBuilder(true, world).setXY(x, y).setAngle(angle).init();
        world.objectDatabase.put(this.shape.body, this);

    }


    @Override
    protected boolean deathCondition() {
        if(this.health > 0){
            this.naturalDeath = true;
        }

        return this.primed && (startTime+lifeTime) < FrameTime.getTime();
    }

    @Override
    public void onDeath(MyWorld world){
        if(this.naturalDeath) {
            ExpandingEffect emp = new EMPEffect(this.team, world, this);
            Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), emp, world, Team.NEUTRAL);
            emp.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
        }
        super.onDeath(world);
    }
}
