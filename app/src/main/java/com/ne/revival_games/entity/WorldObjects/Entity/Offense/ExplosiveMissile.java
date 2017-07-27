package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.ConditionalDestructible;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/27/2017.
 */

public class ExplosiveMissile extends ConditionalDestructible {
    public static int HEALTH = 20;
    private double lifeTime = 5000;
    private boolean collided = false;
    private boolean primed = false;

    public ExplosiveMissile(Vector2 location, double angle, double direction, double speed, Team team, MyWorld world) {
        super(direction, speed, HEALTH, false, team);
        this.shape = new ObjRectangle(50, 20);
        this.shape.getBuilder(true, world).setXY(location.x, location.y).setAngle(angle).init();
        world.objectDatabase.put(this.shape.body, this);
        this.prime();
    }

    @Override
    public boolean update(MyWorld world){
        return super.update(world);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        this.collided = true;
        return super.onCollision(contact, componentHit, damage);
    }

    @Override
    protected boolean deathCondition() {
        return collided || (startTime + lifeTime < System.currentTimeMillis() && startTime != -1);
    }

    @Override
    public void onDeath(MyWorld world){
            ExpandingEffect boom = new ExplosiveEffect(0.05, 0.01, 10, 500, 9, this.team, world);
            Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), boom, world, this.team);
            boom.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
            super.onDeath(world);
    }

}
