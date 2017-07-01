package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;

/**
 * Represents the common behaviors that are shared by all engineWorld objects
 */
public abstract class Entity implements WorldObject {

    public  int COST;
    public int MASS;
    public  int HEALTH;
    public ObjectType TYPE;

    double x;
    double y;
    public AShape shape;
    protected double direction;
    protected double speed;
    public int health;
    public boolean isCollisionAuthority = false;
    public boolean invisible = false;
    boolean invulnerable;
    public boolean ghost = false;

    public Entity(double x, double y, double direction,
                  double speed, int health, boolean invulnerable) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.health = health;
        this.invulnerable = invulnerable;
    }

    // damage from speed;
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
        result += "Type: " + this.getClass() + "\n";
        result += "Health: " + this.health + "\n";
        result += "Location: " + this.x + ", " + this.y + "\n";
        result += "Direction: " + this.direction + "\n";
        result += "----------------------------\n";
        return result;
    }

    @Override
    public void onDeath(){};

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        return true;
    };

    @Override
    public double getDamage(Body componentHit){
        return this.health;
    }

}
