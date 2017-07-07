package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 6/30/2017.
 */

public class GhostEntity {
    public Entity entity;
    private MyWorld world;
    private static int CONTACTING = Color.RED;
    private static int PLACEABLE = Color.GREEN;
    private boolean placeable = true;

    public GhostEntity(Entity entity, MyWorld world) {
        this.entity = entity;
        this.world = world;

        this.entity.shape.setPaint(Paint.Style.STROKE);
        this.entity.ghost = true;
        this.entity.invulnerable = true;
    }

    public boolean canPlace() {
        //this.entity.shape.body.b
        return this.placeable;
    }

    public Entity place() {
        System.out.println("Born: " + this.entity);
        this.entity.shape.setPaint(Paint.Style.FILL);
        this.entity.ghost = false;
        this.entity.invulnerable = false;
        Entity toPlace = this.entity;
        this.entity = null;
        world.ghosts.remove(toPlace);
        this.placeable = false;
        return toPlace;
    }

    // TODO: 7/7/2017 dont need to always call this method - can start calling it when the listeners find a collision with ghost
    public void isColliding() {
        if (this.entity == null) {
            return;
        }
        for (Body body: world.engineWorld.getBodies()) {
            if (!world.objectDatabase.get(body).ghost) {
                if (this.entity.shape.body.isInContact(body)) {
                    // the ghost object is colliding with another non-ghost object
                    entity.shape.setColor(CONTACTING);
                    entity.shape.setPaint(Paint.Style.FILL);
                    this.placeable = false;
                    return;
                }
            }
        }
        this.placeable = true;
        entity.shape.setColor(PLACEABLE);
        entity.shape.setPaint(Paint.Style.STROKE);

    }
}
