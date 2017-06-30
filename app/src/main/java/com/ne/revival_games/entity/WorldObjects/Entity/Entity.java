package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.Shape;

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
    public boolean invisible = false;
    boolean invulnerable;
    boolean ghost = false;

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
        this.x = this.shape.getX();
        this.y = this.shape.getY();
        for (Body body: this.shape.body.getInContactBodies(true)) {
            Entity entityInCollision = world.objectDatabase.get(body);
            if (entityInCollision.TYPE.opposite(this.TYPE)) {
                // subtract health etc.
                this.health -= entityInCollision.health;
                entityInCollision.health -= this.health;

                System.out.println("Collision in ENTITY.JAVA: " + System.currentTimeMillis());
            }
        }
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
        result += "Location: " + this.x + ", " + this.y + "\n";
        result += "Direction: " + this.direction + "\n";
        result += "----------------------------\n";
        return result;
    }

    @Override
    public void onDeath(){};

    @Override
    public boolean onCollision(Entity contact, double damage){
        return true;
    };

}
