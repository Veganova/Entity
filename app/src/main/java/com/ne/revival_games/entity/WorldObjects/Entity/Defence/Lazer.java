package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/2/2017.
 */

public class Lazer extends Projectile {
    public double damage;
    public double lazer_width = 20;         //unscaled width
    public double lazer_length;             //unscaled length
    public double lazer_angle;              //angle in RADIANS
    public double lazer_speed;              //raw speed
    private MyWorld world;


    public Lazer(double width, double direction, double speed,
                 double health, MyWorld world, Team team, boolean addToWorld) {
        super(direction, speed, (int) health, world, team, addToWorld);

        this.world = world;
        this.isCollisionAuthority = true;
        this.lazer_speed = speed;
        this.lazer_width = width/MyWorld.SCALE;
    }

    /**
     * function used to place a rectangular shaped "lazer" object
     * also change this.shape to newly generated body
     *
     * WARNING: object has not been placed in world object database & must be used after this.world
     * is initialized
     */
    public void placeLazer(Vector2 oldPoint, Vector2 newPoint){
        double x = (oldPoint.x + newPoint.x) / 2 * MyWorld.SCALE;
        double y = (oldPoint.y + newPoint.y) / 2 * MyWorld.SCALE;
        double l = Util.getDistance(oldPoint, newPoint);
        lazer_angle = Util.absoluteAngle(oldPoint, newPoint);
        this.shape = new ObjRectangle(l  * MyWorld.SCALE, lazer_width * MyWorld.SCALE);
        this.shape.getBuilder(true, this.world).setXY(x,y).setAngle(Math.toDegrees(lazer_angle)).init();
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        return false;
    }

    @Override
    public Projectile returnCustomizedCopy(Projectile project,
                                           Vector2 location, double direction,
                                           double speed, MyWorld world, Team team) {
        return null;
    }

}
