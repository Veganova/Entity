package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.joint.RevoluteJoint;
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
    private double cooldown;

    /**
     * Says whether this effect is on or not.
     */
    protected boolean status = true;
    protected boolean onCooldown = false;
    private boolean draw = false;

    /**
     *  @param applier
     * @param zone
     * @param type
     * @param world
     */
    void basicInit(Entity applier, AShape zone, EffectType type, MyWorld world) {
        this.applier = applier;
        this.effectType = type;
        this.world = world;
        this.zone = zone;
        this.cooldown = 0.0;
    }

    /**
     *  @param applier
     * @param zone
     * @param type
     * @param jointDisplacement
     * @param world
     */
    void aoeJoint(Entity applier, AShape zone, EffectType type,
                  Vector2 jointDisplacement, MyWorld world){
        basicInit(applier, zone, type, world);

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
        return this.status && this.cooldown != getMaxCooldown()
                && other != applier && !other.targetExceptions.isContactDisallowedWith(this);
    }

    public void toggleDraw() {draw = !draw;}

    public void draw(Canvas canvas) {if (draw && status) zone.draw(canvas);}

    /**
     * Gets called every frame.
     *
     * @param world
     */
    @OverridingMethodsMustInvokeSuper
    public void update(MyWorld world) {
        // logic only for effects/abilities that have a capped duration of usage (and then a cooldown time for coming back up for usage)
        if (this.getMaxCooldown() != 0) {
            // is active and the effect has an activation duration
            if (this.status && this.getMaxActiveTime() != 0) {
//            System.out.println("cooldown - " + cooldown);
                // when it runs for Max_active_time, it will reach maxcooldown

                if (this.cooldown < this.getMaxCooldown()) {
                    this.cooldown += getMaxCooldown() / (getMaxActiveTime() * 1.0);
                }
                // the ability has reached the max cooldown (has been used for as long as it can be)
                else {
                    this.disable();
                    this.cooldown = getMaxCooldown();//set cooldown to max
                }
            } else {
                if (this.cooldown >= 0) {
                    this.cooldown -= 1;
                }

                // double arithmetic safety
                if (this.cooldown < 0) {
                    this.cooldown = 0;
                }
            }
        }

    }

    public void onRemove(){
        this.world.engineWorld.removeBody(this.zone.body);
    }

    public void setColor(int color) {
        this.zone.setColor(color);
    }

    /**
     * @return  Returns the cooldown in frames of that effect.
     */
    public abstract int getMaxCooldown();

    /**
     * @return Returns the active time in frames of that effect.
     */
    abstract public int getMaxActiveTime();

    public boolean getStatus() {
        return status;
    }

    public double getCooldown() {
        return cooldown;
    }
}
