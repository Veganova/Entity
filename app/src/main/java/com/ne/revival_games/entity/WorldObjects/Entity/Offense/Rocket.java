
package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimShootEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.FrameTime;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import org.dyn4j.dynamics.Body;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 7/27/2017.
 */

public class Rocket extends AimShootEntity {
    public static int HEALTH = 20;
    private long lifeTime = 320;
    private boolean collided = false;

    public Rocket(double x, double y, double angle, double speed, Team team, MyWorld world) {
        super(angle, speed, team, true);
        List<AShape> components = new ArrayList<>();

        ObjRectangle rect = new ObjRectangle(50, 20);
        rect.getBuilder(false, world).setXY(-25, 0).init();
        components.add(rect);

        double [] points1 = {0, 15, 0, -15, 25, 0};
        ObjTriangle tri1 = new ObjTriangle(points1);
        tri1.getBuilder(false, world).setXY(0, 0).init();
        components.add(tri1);

        this.shape = new ComplexShape(components, x, y, world);
        world.objectDatabase.put(this.shape.body, this);
        this.logic = new SimpleAim(this, world.objectDatabase, 1000, true);
    }

    @Override
    public boolean update(MyWorld world) {
        if (deathCondition() && this.primed) {
            this.invisible = true;
            world.objectDatabase.remove(this.shape.body);
        } else {
            super.update(world);
        }

        return true;
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        this.collided = true;
        return super.onCollision(contact, componentHit, damage);
    }


    protected boolean deathCondition() {
        return collided || (startTime + lifeTime < FrameTime.getTime());
    }

    @Override
    public void onDeath(MyWorld world){
            ExpandingEffect boom = new ExplosiveEffect(this.team, world, this);
            Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), boom, world, this.team);
            boom.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
            super.onDeath(world);
    }


    @Override
    public Entity getPartToAimWith() {
        return this;
    }

    @Override
    public double getTurnSpeed() {
        return 10;
    }

    @Override
    public double getShootingDistance() {
        return 0;
    }

    @Override
    public Entity getNewBodyToShoot(double x, double y, double angle) {
        return this;
    }
}
