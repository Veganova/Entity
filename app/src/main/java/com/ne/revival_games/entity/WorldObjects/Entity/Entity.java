package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.Joint;

/**
 * Represents the common behaviors that are shared by all engineWorld objects
 */
public abstract class Entity implements WorldObject {

    public  int COST;

    public Team TYPE;

    public AShape shape;
    protected double direction;
    protected double speed;
    public int health;
    public boolean isCollisionAuthority = false;
    public boolean invisible = false;
    boolean invulnerable;
    public boolean ghost = false;

    // TODO: 7/5/2017 some fields here are not needed 
    public Entity(double direction, double speed, int health, boolean invulnerable, Team team) {
        this.direction = direction;
        this.speed = speed;
        this.health = health;
        this.invulnerable = invulnerable;
        this.TYPE = team;
    }

    @Override
    public void update(MyWorld world) {

         //if anything has collided, reduce health - this can be put in Entity
    }


    @Override
    public void draw(Canvas canvas) {
        this.shape.draw(canvas);
    }

    @Override
    public void setVelocity(double speed) {
            this.shape.body.setLinearVelocity(speed * Math.cos(Math.toRadians(this.direction)),
                speed * Math.sin(Math.toRadians(this.direction)));
    }

    @Override
    public String toString() {
        String result = "";
        result += "Team: " + this.TYPE + "\n";
        result += "Type: " + this.getClass() + "\n";
        result += "Health: " + this.health + "\n";
       // result += "Location: " + this.shape.body.getWorldCenter().toString() + "\n";
        result += "Direction: " + this.direction + "\n";
        result += "----------------------------\n";
        return result;
    }

    @Override
    public void onDeath(){
        this.TYPE.remove(this);
    };

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        return true;
    };

    @Override
    public double getDamage(Body componentHit){
        if (this.TYPE == Team.NEUTRAL)
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
            ent1.shape.setPaint(style);
            ent2.shape.setPaint(style);
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
            ent1.shape.setColor(color);
            ent2.shape.setColor(color);
        }
    }

}
