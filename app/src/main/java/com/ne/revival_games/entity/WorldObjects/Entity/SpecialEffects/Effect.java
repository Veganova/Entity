package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/12/2017.
 */
public abstract class Effect {
    public EffectType effectType;
    public AShape zone;
    public MyWorld world;
    protected Entity applier;
    protected boolean status = true;

    public void basicInit(Entity applier, AShape zone, EffectType type, MyWorld world) {
        this.applier = applier;
        this.effectType = type;
        this.world = world;
        this.zone = zone;
    }

    public void aoeJoint(Entity applier, AShape zone, EffectType type,
                         Vector2 jointDisplacement, MyWorld world){
        this.applier = applier;
        this.effectType = type;
        this.world = world;
        this.zone = zone;
        //TODO: there may be an error relating to initialize prior to joint adding
        this.zone.getBuilder(true, world).setXY(applier.shape.getX()* MyWorld.SCALE + jointDisplacement.x,
                applier.shape.getY()*MyWorld.SCALE + jointDisplacement.y).init();
        world.engineWorld.addJoint(new WeldJoint(zone.body, applier.shape.body, zone.body.getWorldCenter()));
    }

    public abstract void apply(Entity other);


    public void disable() {
        status = false;
    }

    public void enable() {
        status = true;
    }

    public void toggle() {
        status = !status;
    }

    protected boolean canApply(Entity other) {
        return this.status && other != applier;
    }

    public void draw(Canvas canvas) {}

    public void update(MyWorld world) {}

    public void onRemove(){
        this.world.engineWorld.removeBody(this.zone.body);
    }
}
