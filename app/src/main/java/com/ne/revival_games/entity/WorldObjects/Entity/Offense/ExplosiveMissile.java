package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SeekerAim;
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

public class ExplosiveMissile extends ConditionalDestructible implements Aimable {
    public static int HEALTH = 20;
    private double lifeTime = 8000;
    private boolean collided = false;
    private AimLogic logic;
    private boolean primed = false;

    public ExplosiveMissile(Vector2 location, double angle, double direction, double speed, Team team, MyWorld world) {
        super(direction, speed, HEALTH, false, team);
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
        this.logic = new SeekerAim(this, world.objectDatabase, 900, true);
        this.prime();
    }

    @Override
    public boolean update(MyWorld world){
        this.aim();
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
            ExpandingEffect boom = new ExplosiveEffect(0.05, 0.05, 12, 120, 6, this.team, world);
            Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), boom, world, this.team);
            boom.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
            super.onDeath(world);
    }

    @Override
    public void aim() {
        this.logic.aim(this);
    }

    @Override
    public void fire(double angle) {
        if(Util.nearValue(angle, 0, 0.01)) {
            this.shape.body.setAngularVelocity(0);
        }

        double delta = Math.signum(angle)*Math.min(Math.abs(angle), 0.1);

        this.shape.body.getTransform().setRotation(this.shape.body.getTransform().getRotation()+ delta);
        double x = Math.cos(this.shape.body.getTransform().getRotation()) * 10;
        double y = Math.sin(this.shape.body.getTransform().getRotation()) * 10;
        this.shape.body.applyForce(new Vector2(x, y));
    }

    @Override
    public Vector2 getCenter() {
        return this.shape.body.getWorldCenter();
    }

    @Override
    public void changeLogicTo(AimLogic logic) {
        this.logic = logic;
    }

    @Override
    public boolean isSleeping() {
        return this.isActive;
    }
}
