package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

/**
 * Represents the common behaviors that are shared by all engineWorld objects
 */
public abstract class Entity implements WorldObject {

    public static int COST;
    public static int MASS;
    public static int HEALTH;
    public static ObjectType TYPE;

    double x;
    double y;
    int mass;
    AShape shape;
    double direction;
    int speed;
    int health;
    boolean invulnerable;

    public Entity(double x, double y, int mass, double direction, int speed, int health, boolean invulnerable) {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.direction = direction;
        this.speed = speed;
        this.health = health;
        this.invulnerable = invulnerable;
    }

}
