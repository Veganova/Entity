package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.ConditionalDestructible;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 9/5/2017.
 */

public class Mine extends ConditionalDestructible{
    boolean contacted = false;

    public Mine(Vector2 location, double angle, double health, Team team, MyWorld world, String tag){
        super(0, 0, team, tag + "mine");
        this.shape = new ObjCircle(MySettings.getNum(team.toString(), name_tag + " radius"));
        this.shape.getBuilder(true, world).setXY(location.x, location.y)
                .setBasics(team.toString(), name_tag)
                .init();
        world.objectDatabase.put(this.shape.body, this);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        contacted = super.onCollision(contact, componentHit, damage);

        if(primed && contact.team.opposite(this.team)) {
           contacted = true;
        }

        return true;
    }

    @Override
    public void onDeath(MyWorld world){
        ExpandingEffect boom = new ExplosiveEffect(name_tag, this.team, world);
        Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), boom, world, this.team);
        boom.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
        super.onDeath(world);
    }


    @Override
    protected boolean deathCondition() {
        return contacted;
    }
}
