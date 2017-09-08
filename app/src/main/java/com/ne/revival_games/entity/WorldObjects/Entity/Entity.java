package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effector;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.EffectType;
import com.ne.revival_games.entity.WorldObjects.MyCollections.MyDeque;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.Joint;

import java.util.HashMap;

import javax.annotation.OverridingMethodsMustInvokeSuper;

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
    public boolean invulnerable;
    protected Player player;
    public boolean isActive = true;
    public double isDisabledUntil = 0;
    public boolean ghost = false;
    public Untargetable targetExceptions;
    public boolean untargetable = false;
    public boolean dead = false;

    public double frictionCoefficent = 1;
    public HashMap<EffectType, Effect> effects;
    public HashMap<Body, Effect> zoneToEffect;
    protected ActiveBar bar;

    private MyDeque.Node node;

    // TODO: 7/5/2017 some fields here are not needed 
    public Entity(double direction, double speed, int health, boolean invulnerable, Team team) {
        this.direction = direction;
        this.speed = speed;
        this.health = health;
        this.MAX_HEALTH = health;
        this.invulnerable = invulnerable;
        this.player = null;
        this.team = team;
        this.effects = new HashMap<>();
        this.zoneToEffect = new HashMap<>();
        this.targetExceptions = new Untargetable(this);
    }

    public MyDeque.Node getNode() {
        return node;
    }

    public void setNode(MyDeque.Node node) {
        this.node = node;
    }

    /**
     * Method is called every game loop on all entities
     *
     * @param world
     * @return true if the update occurred successfully, false if entity is disabled
     */
    public boolean update(MyWorld world) {
        if (this.dead) {
            world.objectDatabase.remove(this.shape.body);
            return true;
        }

        for (Effect effect : effects.values()) {
            effect.update(world);
        }

        if (isDisabledUntil < System.currentTimeMillis() && isDisabledUntil != -1) {
            this.isActive = true;
        }

        if (this.isActive) {
            return true;
        }

        return false;
    }


    public void draw(Canvas canvas) {
        System.out.println(this.simpleString());

        if (!this.invisible) {
//            for (Effect effect : effects.values()) {
//                effect.draw(canvas);
//            }

            this.shape.draw(canvas);



            if (this.bar != null) {
                this.bar.draw(canvas);
            }
        }
    }

    public void drawEffect(Canvas canvas) {
        for (Effect effect : effects.values()) {
            effect.draw(canvas);
        }
    }

    public void interact() {
        if (this.bar != null) {
            this.bar.toggle();
        }
    }

    public void setVelocity(double speed) {
//        double direction = shape.body.getTransform().getRotation();
        this.shape.body.setLinearVelocity(speed * Math.cos(Math.toRadians(direction)),
                speed * Math.sin(Math.toRadians(direction)));
    }

    public void setVelocity(double speed, double angle) {
//        double direction = shape.body.getTransform().getRotation();
        this.shape.body.setLinearVelocity(speed * Math.cos(angle), speed * Math.sin(angle));
    }


    public String simpleString() {
        return this.getClass().getSimpleName();
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


    public void onDeath(MyWorld world) {
        if (this.lastHit != null && this.lastHit.player != null) {
            // if the last hit entity part is owned by a player
            double earned = this.MAX_HEALTH;
//            System.out.println("ADDING MONEY" + earned);
            lastHit.player.addMoney(earned);
        }

        for (Effect effect : effects.values()) {
            this.removeEffect(effect);
//            world.objectDatabase.remove(effect.zone.body);
            // dont think next line does anything
//            world.objectDatabase.remove(effect);
//            world.engineWorld.removeBody(effect.zone.body);
        }

        this.dead = true;
    }

    private Entity lastHit;

    @OverridingMethodsMustInvokeSuper
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
//        System.out.println("contact type " + contact.getClass().getSimpleName());

        if ((this.untargetable && this.targetExceptions.isContactDisallowedWith(contact) )
                || (contact.untargetable && contact.targetExceptions.isContactDisallowedWith(this))) {
            return false;
//            System.out.println("CONTACT PROHIBITED");
        }
//
//        if (contact.team.opposite(this.team)) {
//            this.lastHit = contact;
//        }

        if (this.dead) {
            return false;
        }

        if (componentHit == null)
            return false;

        Effect activeEffect = zoneToEffect.get(componentHit);
        if (activeEffect != null) {
            activeEffect.apply(contact);
            return false;
        }

        if (contact.team.opposite(this.team)) {
            this.lastHit = contact;
            applyDamage(damage, contact);
        }

        return !this.dead;
    }

//    public void addToTeam(Team team) {
//        if (team != null) {
//            this.team.remove(this);
//            this.team = team;
//            this.team.add(this);
//        }
//    }

    public void addToPlayer(Player player) {
//        this.addToTeam(player.team);
        this.player = player;
        this.team.applyTeamColor(this);
    }

    public double getDamage(Body componentHit) {
        if (this.team == Team.NEUTRAL)
            return 0;
        return this.health;
    }

    public double applyDamage(double damage, Entity from) {

        if (this.untargetable && this.targetExceptions.isContactDisallowedWith(from)) {
            return 0;
        }

        if (!this.ghost && !this.invulnerable) {
            this.health -= damage;

            if (this.health <= 0) {
                this.invisible = true;
                this.dead = true;
                return 0;
            }
            return damage;
        }
        return 0;
    }

    public boolean isInContact(Body body) {
        return shape.body.isInContact(body);
    }

    /**
     * Override this if the object (like turret) and has subparts to be colored.
     *
     * @param color
     */
    public void setColor(int color) {
        this.shape.setColor(color);
        for (Effect effect: this.effects.values()) {
            effect.setColor(color);
        }
    }

    /**
     * Handles the joints as well.
     *
     * @param style
     */
    public void setPaint(Paint.Style style, MyWorld world) {
        this.shape.setPaint(style);
        for (Joint joint : this.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());
            if (ent1 != null && ent2 != null) {
                ent1.shape.setPaint(style);
                ent2.shape.setPaint(style);
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
        Effect thisEffect = this.zoneToEffect.remove(effect.zone.body);

        if (thisEffect != null) {
            thisEffect.onRemove();
        }

        this.effects.remove(effect.effectType);
    }

    public void disableAllEffects() {
        for (Effect effect : this.effects.values()) {
            effect.disable();
        }
    }

    public void enableAllEffects() {
        for (Effect effect : this.effects.values()) {
            effect.enable();
        }
    }

}
