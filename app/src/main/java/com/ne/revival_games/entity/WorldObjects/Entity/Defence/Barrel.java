package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public class Barrel extends Entity implements Aimable {

    public enum BarrelType {
        SINGLE, LAZER, SIDE, SPIRIT_BOMB
    }

    private MyWorld world;
    private double magnitude;
    private Projectile projectile;
    private double sleepUntil = 0;

    public Barrel(Projectile projectile, BarrelType type,
                  Vector2 location, MyWorld world, double angle, Team team) {
        super(0, 0, 0, false, team);
        initBarrel(type, location, world, angle);
        this.world = world;
        this.projectile = projectile;
        this.world.objectDatabase.put(this.shape.body, this);
    }

    private void initBarrel(BarrelType type, Vector2 location, MyWorld world, double angle) {
        switch (type) {
            case SINGLE:
                //magnitude needs to be specified here
                this.shape = new ObjRectangle(120, 20);
                AShape.InitBuilder builderSingle = this.shape.getBuilder(true, world);
                builderSingle.setXY(location.x, location.y).init();
                magnitude = 150;
                break;
            case SIDE:
                this.shape = new ObjRectangle(120, 20);
                AShape.InitBuilder builderSide = this.shape.getBuilder(true, world);
                builderSide.setXY(50 + (location.x), 100 + (location.y)).setAngle(Math.toDegrees(angle)).init();

                magnitude = 50;

//                this.shape.rotateBody(angle);
//                this.shape.body.getTransform().rotate(1);
                break;
        }
        this.health = 100;
    }



    @Override
    public void draw(Canvas canvas){
        this.shape.draw(canvas);
    }

    @Override
    public void aim() {
        // done in the turret that controls this
    }

    @Override
    public void fire(double angle) {
        //double angle = shape.body.getTransform().getRotation();
        double x, y;
        //magnitude of 90 away
        x = magnitude * Math.cos(angle) + MyWorld.SCALE*shape.body.getWorldCenter().x;
        y = magnitude * Math.sin(angle) + MyWorld.SCALE*shape.body.getWorldCenter().y;
        angle = Math.toDegrees(angle);

        this.projectile.returnCustomizedCopy(this.projectile, new Vector2(x,y), angle, 30, this.world, TYPE);

        this.shape.body.setAsleep(false);
        this.sleepUntil = System.currentTimeMillis() + 0;
    }

    @Override
    public Vector2 getCenter() {
        return this.shape.body.getWorldCenter();
    }

    @Override
    public void changeLogicTo(AimLogic logic) {
        // logic in the turret that controls this
    }

    @Override
    public boolean isSleeping() {
        return false;
    }


}
