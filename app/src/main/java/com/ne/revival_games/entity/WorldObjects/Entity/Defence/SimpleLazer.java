package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/1/2017.
 */

public class SimpleLazer extends Lazer {
    public double lifetime = 500, bornTime;

    public SimpleLazer(Vector2 start, Vector2 end, double width, double health, double speed, MyWorld world) {
        super(width, 0, speed, health, world);
        start = new Vector2(start.x/ MyWorld.SCALE, start.y/MyWorld.SCALE);
        end = new Vector2(end.x/ MyWorld.SCALE, end.y/MyWorld.SCALE);

        this.lazer_length = Util.getDistance(start, end);
        placeLazer(start, end);
        this.direction = lazer_angle;
        world.objectDatabase.put(this.shape.body, this);
        this.setVelocity(speed);
        bornTime = System.currentTimeMillis();
    }

    public SimpleLazer(Vector2 start, double angle, double length, double width, double health, double speed, MyWorld world) {
        super(width, 0, speed, health, world);
        start = new Vector2(start.x/ MyWorld.SCALE, start.y/MyWorld.SCALE);
        length = length/MyWorld.SCALE;
        Vector2 end = new Vector2(start.x+length*Math.cos(angle), start.y+length*Math.sin(angle));
        this.lazer_length = Util.getDistance(start, end);
        placeLazer(start, end);
        this.direction = lazer_angle;
        world.objectDatabase.put(this.shape.body, this);
        this.setVelocity(speed);
        bornTime = System.currentTimeMillis();
    }


    @Override
    public Projectile returnCustomizedCopy(Vector2 location, double direction, double speed, MyWorld world) {
        Vector2 endPoint = new Vector2(this.lazer_length*Math.cos(this.lazer_angle),
                this.lazer_length * Math.sin(this.lazer_angle));
        return new SimpleLazer(location, endPoint, this.lazer_width, this.health, this.lazer_speed, world);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        return false;
    }


    @Override
    public void update(MyWorld world){
        if(this.bornTime + lifetime < System.currentTimeMillis())
        world.bodiestodelete.add(this.shape.body);
    }




}
