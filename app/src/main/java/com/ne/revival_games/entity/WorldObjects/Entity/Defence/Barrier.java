package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

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
    public static ObjectType TYPE = ObjectType.DEFENCE;

    private ObjRectangle rectangle;


    public Barrier(double x, double y, double angle) {
        super(x, y, MASS, angle, 0, HEALTH, false);
        this.rectangle = new ObjRectangle(x, y, 200, 30);
    }

    @Override
    public void update(MyWorld world) {

        for (Body body: rectangle.body.getInContactBodies(true)) {
            if (world.objectDatabase.get(body).TYPE == ObjectType.OFFENSE) {
                // subtract health etc.
            }
        }
        this.rectangle.body.setUserData(1);
        // if anything has collided, reduce health - this can be put in Entity
        System.out.println();
    }

    @Override
    public void draw(Canvas canvas) {
        rectangle.draw(canvas);
    }

}
