package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;

/**
 * Created by Veganova on 6/30/2017.
 */

public class GhostEntity {
    public Entity entity;
    private MyWorld world;
    private static int CONTACTING = Color.RED;
    private static int PLACEABLE = Color.GREEN;
    private boolean placeable = true;
    private Mass previousType;

    public GhostEntity(Entity entity, MyWorld world) {
        this.entity = entity;
        this.world = world;

        this.entity.shape.setPaint(Paint.Style.STROKE);
        this.entity.ghost = true;

        //we need this line unfortunately for turret TODO: change it back on place!
        previousType = this.entity.shape.body.getMass();
        this.entity.shape.body.setMass(MassType.FIXED_ANGULAR_VELOCITY);

        int num = 0;
        for (Joint joint : this.entity.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());

            if (ent1 != null && ent2 != null) {
                ent1.shape.setPaint(Paint.Style.STROKE);
                ent2.shape.setPaint(Paint.Style.STROKE);
                ent1.ghost = true;
                ent1.invulnerable = true;
                ent2.ghost = true;
                ent2.invulnerable = true;
                world.ghosts.put(ent1, this);
                world.ghosts.put(ent2, this);
            }

            num++;
        }
        System.out.println(entity.simpleString() + ": NUM JOINTS - " + num);
//        System.out.println(entity);

        this.entity.invulnerable = true;
        this.entity.disableAllEffects();
    }

    public boolean canPlace() {
        //this.entity.shape.body.b
        return this.placeable;
    }

    private Team team;
    private Player player;

    public void place(Team team) {
        if (this.placeable) {
            this.wantToPlace = true;
            this.team = team;
        } else {
            throw new RuntimeException("Cannot place ghost " + entity.simpleString() + " here!");
        }
    }

    public void place(Player player) {
        if (this.placeable) {
            this.wantToPlace = true;
            this.player = player;
        } else {
            throw new RuntimeException("Cannot place ghost " + entity.simpleString() + " here!");
        }
    }


    /**
     * place(Player) will be called before this. Assumes that placeable is true
     * @return
     */
    private void placeReal() {
        // this check is not necessary, but if incorrect usage then throws an error (below)
        this.entity.shape.setPaint(Paint.Style.FILL);
        this.entity.ghost = false;
        this.clearForces(this.entity);

        this.entity.shape.body.setMass(previousType);


        for (Joint joint : this.entity.shape.body.getJoints()) {
            Entity ent1 = world.objectDatabase.get(joint.getBody1());
            Entity ent2 = world.objectDatabase.get(joint.getBody2());
            if (ent1 != null && ent2 != null) {
                ent1.shape.setPaint(Paint.Style.FILL);
                ent2.shape.setPaint(Paint.Style.FILL);
                ent1.ghost = false;
                ent1.invulnerable = false;
                ent2.ghost = false;
                ent2.invulnerable = false;
                world.ghosts.remove(ent1);
                world.ghosts.remove(ent2);
            }
        }
        this.entity.invulnerable = false;
//        Entity toPlace = this.entity;

        this.entity.enableAllEffects();
        if (this.player != null) {
            this.entity.addToPlayer(this.player);
            this.player = null;
        } else if (this.team != null) {
            this.team.applyTeamColor(entity);
            this.team = null;
        }

//        this.team.add(this.entity);
        world.ghosts.remove(this.entity);

        this.entity = null;
        this.placeable = false;
        this.wantToPlace = false;
        // TODO: 7/11/2017 might want to place the entity created into the team lists right here
//        return toPlace;
    }

    private boolean wantToPlace = false;

    // TODO: 7/7/2017 dont need to always call this method - can start calling it when the listeners find a collision with ghost
    public void isColliding() {
        if (this.entity == null) {
            return;
        }

        try {
            for (Body body : world.engineWorld.getBodies()) {
                if (world.objectDatabase.get(body) != null && !world.objectDatabase.get(body).ghost) {
                    if (entity.isInContact(body)) {//shape.body.isInContact(body)) {
                        // the ghost object is colliding with another non-ghost object

                        entity.setColor(CONTACTING);
                        entity.setPaint(Paint.Style.FILL, world);

                        this.placeable = false;
                        return;
                    }
                }
            }
        } catch (NullPointerException e) {
            // gets rid of a concurrency bug - the entity was being set to null as this
            // function enters the loop below
            e.printStackTrace();
        }
        if (this.wantToPlace) {
            this.placeReal();
        } else {
//            this.placeable = true;
            entity.setColor(PLACEABLE);
            entity.setPaint(Paint.Style.STROKE, world);
        }
        this.placeable = true;


    }


    public void setLinearVelocity(double x, double y) {
        clearForces(this.entity);

        this.entity.shape.body.setLinearVelocity(x, y);

        for (Joint joint : this.entity.shape.body.getJoints()) {
            joint.getBody1().setLinearVelocity(x, y);
            joint.getBody2().setLinearVelocity(x, y);
        }
    }

    //will break if x connects to y connects to z connects to x cases
    public void setAngle(double angleChange, Body caller) {
        this.entity.shape.body.getTransform().setRotation(
                this.entity.shape.body.getTransform().getRotation() + angleChange);
        clearForces(this.entity);
//        this.entity.shape.body.setAngularVelocity(angleChange);

    }

    public void clearForces(Entity ent) {
        ent.shape.body.setAsleep(true);
        ent.shape.body.setAsleep(false);
        for (Joint joint : ent.shape.body.getJoints()) {
            joint.getBody1().setAsleep(true);
            joint.getBody1().setAsleep(false);
            joint.getBody2().setAsleep(true);
            joint.getBody2().setAsleep(false);
        }
    }

    public void removeGhost() {
        if (this.entity != null && this.entity.shape != null) {
            for (Joint joint : this.entity.shape.body.getJoints()) {
                if (joint.getBody1() != this.entity.shape.body) {
                    this.world.objectDatabase.remove(joint.getBody1());
                    this.world.engineWorld.removeBody(joint.getBody1());
                } else {
                    this.world.objectDatabase.remove(joint.getBody2());
                    this.world.engineWorld.removeBody(joint.getBody2());
                }

            }
            this.world.objectDatabase.remove(this.entity.shape.body);
            this.world.engineWorld.removeBody(this.entity.shape.body);
            this.entity = null;
        }
    }
}
