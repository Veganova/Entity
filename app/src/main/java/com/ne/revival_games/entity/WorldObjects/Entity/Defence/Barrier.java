package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.ObjectType;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;


/**
 * Players can place this to protect
 */
public class Barrier extends Entity {

    public static int COST = 100;
    public static int MASS = 20;
    public static int HEALTH = 100;
//    public static ObjectType TYPE = ObjectType.OFFENSE;


    public Barrier(double x, double y, double angle, MyWorld world) {
        super(x, y, angle, 0, HEALTH, false);
        this.shape = new ObjRectangle(x, y, 200, 30, world);
        this.shape.rotateBody(Math.toRadians(angle));
        world.objectDatabase.put(this.shape.body, this);
        TYPE = ObjectType.OFFENSE;
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        if(contact.TYPE == ObjectType.DEFENCE){
            this.health -= damage;
            if(this.health <= 0){
                this.invisible = true;
                return false;
            }
        }

        return true;
    }


}
