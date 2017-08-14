package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/1/2017.
 */
public class SimpleLazer extends Lazer {
    public double lifetime = 500, bornTime;

    public SimpleLazer(Vector2 start, Vector2 end, double width,
                       double health, double speed, MyWorld world, Team team, boolean addToWorld) {
        super(width, 0, speed, health, world, team, addToWorld);
        start = new Vector2(start.x/ MyWorld.SCALE, start.y/MyWorld.SCALE);
        end = new Vector2(end.x/ MyWorld.SCALE, end.y/MyWorld.SCALE);
        this.lazer_length = Util.getDistance(start, end);

        if(addToWorld){
            placeLazer(start, end);
            world.objectDatabase.put(this.shape.body, this);
            this.setVelocity(speed);
        }

        this.direction = Math.toDegrees(lazer_angle);
        bornTime = System.currentTimeMillis();
        this.barrel_sleepTime = lifetime;
        this.ghost = true;
    }

    public SimpleLazer(Vector2 start, double angle, double length,
                       double width, double health, double speed, MyWorld world, Team team, boolean addToWorld) {
        super(width, 0, speed, health, world, team, addToWorld);
        start = new Vector2(start.x/ MyWorld.SCALE, start.y/MyWorld.SCALE);
        length = length/MyWorld.SCALE;
        Vector2 end = new Vector2(start.x+length*Math.cos(angle), start.y+length*Math.sin(angle));
        this.lazer_length = Util.getDistance(start, end);

        if(addToWorld){
            placeLazer(start, end);
            world.objectDatabase.put(this.shape.body, this);
            this.setVelocity(speed);
        }

        this.direction = Math.toDegrees(lazer_angle);
        bornTime = System.currentTimeMillis();
        this.barrel_sleepTime = lifetime;
        this.ghost = true;

    }


    @Override
    public Projectile returnCustomizedCopy(Projectile project, Vector2 location, double direction,
                                           double speed, MyWorld world, Team team) {
        if(project.getClass() == this.getClass()){
            SimpleLazer lazer = (SimpleLazer) project;
            return new SimpleLazer(location, Math.toRadians(direction),
                    lazer.lazer_length*MyWorld.SCALE, lazer.lazer_width*MyWorld.SCALE,
                    lazer.health, lazer.lazer_speed, world, team, true);

        }
        return null;
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        return false;
    }


    @Override
    public boolean update(MyWorld world){

        if(lifetime!=0 && this.bornTime + lifetime < System.currentTimeMillis()){
            world.objectDatabase.remove(this.shape.body);
        }

        return true;
    }

    /**
     * draw function only works with rectangle / hit boxes
     *
     * @param canvas
     */
//    @Override
//    public void draw(Canvas canvas){
//        ObjRectangle rectangle = (ObjRectangle) this.shape;
//
//        canvas.drawLine((float) point1.x,(float)  point1.y, (float) point2.x, (float) point2.y, new Paint());
//    }

}
