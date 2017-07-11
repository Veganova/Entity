package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.Joint;

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

        int num = 0;
        for (Joint joint: this.entity.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());
            ent1.shape.setPaint(Paint.Style.STROKE);
            ent2.shape.setPaint(Paint.Style.STROKE);
            ent1.ghost = true;
            ent1.invulnerable = true;
            ent2.ghost = true;
            ent2.invulnerable = true;
            world.ghosts.put(ent1, this);
            world.ghosts.put(ent2, this);
            num++;
        }
        System.out.println("NUM JOINTS - " + num);
        System.out.println(entity);

        this.entity.invulnerable = true;
    }

    public boolean canPlace() {
        //this.entity.shape.body.b
        return this.placeable;
    }

    public Entity place() {
        this.entity.shape.setPaint(Paint.Style.FILL);
        this.entity.ghost = false;
        for (Joint joint: this.entity.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());
            ent1.shape.setPaint(Paint.Style.FILL);
            ent2.shape.setPaint(Paint.Style.FILL);

            ent1.ghost = false;
            ent1.invulnerable = false;
            ent2.ghost = false;
            ent2.invulnerable = false;
            world.ghosts.remove(ent1);
            world.ghosts.remove(ent2);
        }
        this.entity.invulnerable = false;
        Entity toPlace = this.entity;
        this.entity = null;
        world.ghosts.remove(toPlace);
        this.placeable = false;
        return toPlace;
    }

    // TODO: 7/7/2017 dont need to always call this method - can start calling it when the listeners find a collision with ghost
    public void isColliding() {
        //System.out.println(placeable);
        if (this.entity == null) {
            return;
        }
        for (Body body: world.engineWorld.getBodies()) {
            if (!world.objectDatabase.get(body).ghost) {
                if (this.entity.shape.body.isInContact(body)) {
                    // the ghost object is colliding with another non-ghost object

//                    entity.shape.setColor(CONTACTING);
//                    entity.shape.setPaint(Paint.Style.FILL);
//
//                    for (Joint joint: this.entity.shape.body.getJoints()) {
//                        Entity ent1 = world.objectDatabase.get(joint.getBody1());
//                        Entity ent2 = world.objectDatabase.get(joint.getBody2());
//                        ent1.shape.setColor(CONTACTING);
//                        ent1.shape.setPaint(Paint.Style.FILL);
//                        ent2.shape.setColor(CONTACTING);
//                        ent2.shape.setPaint(Paint.Style.FILL);
//                    }
                    entity.setColor(CONTACTING, world);
                    entity.setPaint(Paint.Style.FILL, world);

                    this.placeable = false;
                    return;
                }
            }
        }
        this.placeable = true;
        entity.setColor(PLACEABLE, world);
        entity.setPaint(Paint.Style.STROKE, world);

    }
}
