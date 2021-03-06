package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;

/**
 * Created by vishn on 9/5/2017.
 */

public class Mine extends Entity {
    boolean contacted = false;

    public Mine(double x, double y, double angle, Team team, MyWorld world) {
        super(angle, 0, team);
        this.shape = new ObjCircle(MySettings.getEntityNum(team.toString(), new Query(getName(),"radius"), true));
        this.shape.getBuilder(true, world).setXY(x, y)
                .setBasics(team.toString(), getName())
                .init();
        world.objectDatabase.put(this.shape.body, this);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        contacted = super.onCollision(contact, componentHit, damage);

        if(this.primed)
        contacted = true;

        return true;
    }

    @Override
    public void onDeath(MyWorld world) {
        ExpandingEffect boom = new ExplosiveEffect(this.team, world, this);
        Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), boom, world, this.team);
        boom.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
        super.onDeath(world);
    }


    @Override
    protected boolean deathCondition() {
        return contacted;
    }
}
