package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 7/12/2017.
 */
public abstract class Effect {
    public EffectType effectType;
    public AShape zone;
    public MyWorld world;
    public Entity applier;
    protected boolean status = true;
    private boolean draw = false;
    // Number of frames that this effect will take place 40fps
    private int COOLDOWN_MAX;
    private int cooldown;

    /**
     *
     * @param applier
     * @param zone
     * @param type
     * @param world
     * @param cooldown  cooldown in seconds
     */
    void basicInit(Entity applier, AShape zone, EffectType type, MyWorld world, double cooldown) {
        this.applier = applier;
        this.effectType = type;
        this.world = world;
        this.zone = zone;

        this.COOLDOWN_MAX = (int)Math.ceil(cooldown * MyWorld.FPS);
        this.cooldown = 0;//starts off at no cooldown
    }

    /**
     *
     * @param applier
     * @param zone
     * @param type
     * @param jointDisplacement
     * @param world
     * @param cooldown cooldown in seconds
     */
    void aoeJoint(Entity applier, AShape zone, EffectType type,
                  Vector2 jointDisplacement, MyWorld world, double cooldown){
        basicInit(applier, zone, type, world, cooldown);

        Paint p = zone.getPaint();
        p.setStyle(Paint.Style.STROKE);
        p.setPathEffect( new DashPathEffect(new float[]{1f, .8f} , 0 ));
        p.setStrokeCap( Paint.Cap.ROUND );
        p.setStrokeWidth(0.08f);
//        zone.getPaint(p);
        //TODO: there may be an error relating to initialize prior to joint adding
        this.zone.getBuilder(true, world).setXY(applier.shape.getX()* MyWorld.SCALE + jointDisplacement.x,
                applier.shape.getY()*MyWorld.SCALE + jointDisplacement.y).init();
        world.engineWorld.addJoint(new RevoluteJoint(applier.shape.body, zone.body, zone.body.getWorldCenter()));
    }

    /**
     *
     *
     * @param other
     * @return      Whether the effect was applied or not
     */
    @OverridingMethodsMustInvokeSuper
    public boolean apply(Entity other) {
        return this.canApply(other);
    }


    public void disable() {
        status = false;
    }

    public void enable() {
        status = true;
    }

    public void toggle() {
        status = !status;
    }

    //TODO: use engine possibly to avoid checking if effect is hitting applier
    private boolean canApply(Entity other) {
        return this.status && other != applier && !other.targetExceptions.isContactDisallowedWith(this);
    }

    public void toggleDraw() {draw = !draw;}

    public void draw(Canvas canvas) {if (draw && status) zone.draw(canvas);}

    /**
     * Gets called every frame.
     *
     * @param world
     */
    public void update(MyWorld world) {}

    public void onRemove(){
        this.world.engineWorld.removeBody(this.zone.body);
    }

    public void setColor(int color) {
        this.zone.setColor(color);
    }
}
