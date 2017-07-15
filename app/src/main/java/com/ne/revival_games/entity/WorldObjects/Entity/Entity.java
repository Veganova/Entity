package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effector;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.EffectType;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.Joint;

import java.util.HashMap;

/**
 * Represents the common behaviors that are shared by all engineWorld objects
 */
public class Entity implements Effector {

    public int COST;
    public int MAX_HEALTH;
    public Team team;

    public AShape shape;
    protected double direction;
    protected double speed;
    public int health;
    public boolean isCollisionAuthority = false;
    public boolean invisible = false;
    boolean invulnerable;
    public boolean ghost = false;
    public boolean untargetable = false;

    public double frictionCoefficent = 1;
    public HashMap<EffectType, Effect> effects;
    public HashMap<Body, Effect> zoneToEffect;
    protected ActiveBar bar;

    // TODO: 7/5/2017 some fields here are not needed 
    public Entity(double direction, double speed, int health, boolean invulnerable, Team team) {
        this.direction = direction;
        this.speed = speed;
        this.health = health;
        this.MAX_HEALTH = health;
        this.invulnerable = invulnerable;
        this.team = team;
        this.effects = new HashMap<>();
        this.zoneToEffect = new HashMap<>();
    }

    public void update(MyWorld world) {

         //if anything has collided, reduce health - this can be put in Entity
    }


    public void draw(Canvas canvas) {
        this.shape.draw(canvas);
        if (this.bar != null) {
            this.bar.draw(canvas);
        }
    }

    public void interact() {
        if (this.bar != null) {
            this.bar.toggle();
        }
    }

    public void setVelocity(double speed) {
            this.shape.body.setLinearVelocity(speed * Math.cos(Math.toRadians(this.direction)),
                speed * Math.sin(Math.toRadians(this.direction)));
    }

    public String toString() {
        String result = "";
        result += "Team: " + this.team + "\n";
        result += "Type: " + this.getClass() + "\n";
        result += "Health: " + this.health + "\n";
       // result += "Location: " + this.shape.body.getWorldCenter().toString() + "\n";
        result += "Direction: " + this.direction + "\n";
        result += "----------------------------\n";
        return result;
    }

    public void onDeath(MyWorld world){
        for(Effect effect: effects.values()){
            world.objectDatabase.remove(effect);
            world.engineWorld.removeBody(effect.zone.body);
        }
        this.team.remove(this);
    }

    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        if (this.untargetable) {
            return false;
        }
        if(componentHit == null)
            return false;

        Effect activeEffect = zoneToEffect.get(componentHit);
        if (activeEffect != null) {
            activeEffect.apply(contact);
            return false;
        }

        if(contact.team.opposite(this.team)) {
            this.health -= damage;
            if (this.health <= 0) {
                this.invisible = true;
                return false;
            }
        }

        return true;
    }

    public void addToTeam(Team team) {
        if (team != null) {
            this.team.remove(this);
            this.team = team;
            this.team.add(this);
        }
    }

    public double getDamage(Body componentHit){
        if (this.team == Team.NEUTRAL)
            return 0;
        return this.health;
    }

    /**
     * Handles the joints as well.
     *
     * @param style
     */
    public void setPaint(Paint.Style style, MyWorld world) {
        this.shape.setPaint(style);
        for (Joint joint: this.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());
            if(ent1 != null && ent2 != null){
                ent1.shape.setPaint(style);
                ent2.shape.setPaint(style);
            }
        }
    }

    /**
     * Handles the joints as well.
     *
     * @param color
     */
    public void setColor(int color, MyWorld world) {
        this.shape.setColor(color);
        for (Joint joint: this.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());
            if(ent1 != null && ent2 !=null){
                ent1.shape.setColor(color);
                ent2.shape.setColor(color);
            }

        }
    }

    @Override
    public boolean isEffect(Body effectBody) {
        return this.zoneToEffect.containsKey(effectBody);
    }

    @Override
    public void addEffect(Effect effect) {
        this.removeEffect(effect);
        this.effects.put(effect.effectType, effect);
        this.zoneToEffect.put(effect.zone.body, effect);
    }

    @Override
    public void removeEffect(Effect effect) {
        this.zoneToEffect.remove(effect.zone.body);
        this.effects.remove(effect.effectType);
    }

    public void disableAllEffects() {
        for (Effect effect: this.effects.values()) {
            effect.disable();
        }
    }

    public void enableAllEffects() {
        for (Effect effect: this.effects.values()) {
            effect.enable();
        }
    }

}
