package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

/**
 * Represents the common behaviors that are shared by all world objects
 */
public abstract class Entity implements WorldObject {

    int x;
    int y;
    int mass;
    AShape shape;
    double direction;
    int speed;
    int health;
    boolean invulnerable;

}
