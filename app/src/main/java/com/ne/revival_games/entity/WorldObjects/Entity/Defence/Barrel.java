package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.support.annotation.NonNull;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.ShootableEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public class Barrel extends ShootableEntity {


    public enum BarrelType {
        SINGLE, LAZER, SIDE, SPIRIT_BOMB
    }

    private MyWorld world;
    private double magnitude;
    private EntityLeaf toShoot;
    public Turret myTurret = null;
    private double sleepUntil = 0;

    public Barrel(EntityLeaf toShoot, BarrelType type,
                  @NonNull Turret turret, MyWorld world, double angle, Team team, Vector2 location) {
        super(0, 0, 70, false, team);
        initBarrel(type, location, world, angle);
        this.world = world;
        this.toShoot = toShoot;
        this.world.objectDatabase.put(this.shape.body, this);

        this.myTurret = turret;

    }

    private void initBarrel(BarrelType type, Vector2 location, MyWorld world, double angle) {
        switch (type) {
            case SINGLE:
                //magnitude needs to be specified here
                this.shape = new ObjRectangle(90, 20);
                AShape.InitBuilder builderSingle = this.shape.getBuilder(true, world);
                builderSingle.setXY(location.x, location.y).setAngle(angle).init();
                magnitude = 0;
                break;
            case SIDE:
                this.shape = new ObjRectangle(120, 20);
                AShape.InitBuilder builderSide = this.shape.getBuilder(true, world);
                builderSide.setXY(50 + (location.x), 100 + (location.y)).setAngle(angle).init();

                magnitude = 50;

//                this.shape.rotateBody(angle);
//                this.shape.body.getTransform().rotate(1);
                break;
        }

//        this.bar = new ActiveBar(this);
//        this.bar.setPathType(LINE, 120);
    }


    @Override
    public double getShootingDistance() {
        return magnitude;
    }

    @Override
    public Entity getNewBodyToShoot(double x, double y, double angle) {
        return toShoot.produce(x, y, angle, world, this.team);
    }

//    @Override
//    public void fire(double angle) {
//        //double angle = shape.body.getTransform().getRotation();
//        double x, y;
//        //magnitude of 90 away
//        x = magnitude * Math.cos(angle) + MyWorld.SCALE*shape.body.getWorldCenter().x;
//        y = magnitude * Math.sin(angle) + MyWorld.SCALE*shape.body.getWorldCenter().y;
//        angle = Math.toDegrees(angle);
//
//        this.projectile.returnCustomizedCopy(this.projectile, new Vector2(x,y), angle, 30, this.world, team);
//
//        this.shape.body.setAsleep(false);
//        this.sleepUntil = System.currentTimeMillis() + this.projectile.getSleepTime();
//    }

//    @Override
//    public Vector2 getCenter() {
//        return this.shape.body.getWorldCenter();
//    }

//    @Override
//    public void changeLogicTo(AimLogic logic) {
//        // logic in the turret that controls this
//    }
//
//    @Override
//    public boolean isSleeping() {
//        return sleepUntil > System.currentTimeMillis();
//    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        // TODO: 8/27/2017 see if this not there breaks anything
//        if(contact instanceof Barrel) {
//            if(((Barrel) contact).myTurret == this.myTurret && this.myTurret != null){
//                return false;
//            }
//        }

//        if(this.myTurret == contact) {
//            return false;
//        }
        System.out.println("COLLIDING barrel");

        return super.onCollision(contact, componentHit, damage);
    }

    @Override
    public void onDeath(MyWorld world){
           if(this.myTurret != null){
               myTurret.barrels.remove(this);
               myTurret.selectNewMainBarrel(this);
           }
           this.myTurret = null;
        super.onDeath(world);
    }
}
