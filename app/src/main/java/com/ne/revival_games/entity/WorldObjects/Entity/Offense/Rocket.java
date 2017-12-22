
package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimShootEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.ImmediateAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.ShootableEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.ConditionalDestructible;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 7/27/2017.
 */

public class Rocket extends AimShootEntity {
    public static int HEALTH = 20;
    private double lifeTime = 8000;
    private boolean collided = false;
    protected boolean naturalDeath = false;
    protected boolean primed = false;
    protected double startTime = -1;

    public Rocket(Vector2 location, double angle, double direction, double speed, Team team, MyWorld world) {
        super(direction, speed, team, "rocket", true);
        List<AShape> components = new ArrayList<>();

        ObjRectangle rect = new ObjRectangle(50, 20);
        rect.getBuilder(false, world).setXY(-25, 0).init();
        components.add(rect);

        double [] points1 = {0, 15, 0, -15, 25, 0};
        ObjTriangle tri1 = new ObjTriangle(points1);
        tri1.getBuilder(false, world).setXY(0, 0).init();
        components.add(tri1);

        this.shape = new ComplexShape(components, location.x, location.y, world);
        world.objectDatabase.put(this.shape.body, this);
        this.logic = new ImmediateAim(this, world.objectDatabase, 500);
        this.prime();
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

    public void prime() {
        this.primed = true;
        this.startTime = System.currentTimeMillis();
    }

    protected boolean deathCondition() {
        return collided || (startTime + lifeTime < System.currentTimeMillis() && startTime != -1);
    }

    @Override
    public void onDeath(MyWorld world){
            ExpandingEffect boom = new ExplosiveEffect(0.05, 0.05, 12, 120, 6, this.team, world);
            Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), boom, world, this.team);
            boom.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
            super.onDeath(world);
    }


    @Override
    public Entity getPartToAimWith() {
        return this;
    }

    @Override
    public int getTurnSpeed() {
        return 5;
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
