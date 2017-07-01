package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;

import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by vishn on 6/30/2017.
 */

//TODO: implement this better (maybe actually place rectangle objects in world so collision can handle it)
//don't need an "iswithinburn" radius either, set as invulnerable

public class MassLazer extends Projectile {
    public static int MAZER_RADIUS = 20;
    public static int MAZER_HEALTH = 300;

    private double fired;
    private double lastRecorded = 0;
    private ArrayList<AShape> tail;
    private ArrayList<Vector2> points;
    private Queue<Vector2> pointstoPlace;
    private Vector2 lastPoint;
    private MyWorld world;

    public MassLazer(double x, double y, double direction, MyWorld world){
        super(x, y, MAZER_RADIUS, direction, 30, MAZER_HEALTH, world);
            fired = System.currentTimeMillis();
            tail = new ArrayList<>();
            points = new ArrayList<>();
            pointstoPlace = new LinkedBlockingQueue<>();
            lastPoint = this.shape.body.getWorldCenter();
            points.add(lastPoint);
            this.world = world;
    }


    @Override
    public void draw(Canvas canvas) {
//        needs to be deleted after x seconds

        if (System.currentTimeMillis() - lastRecorded> 100) {
////            if (tail.size() > 20) {
////                AShape shape = tail.get(19);
////                tail.remove(0);
////                points.remove(0);
////                world.engineWorld.removeBody(shape.body);
////                world.objectDatabase.remove(shape.body);
////            }
//
            //two points point1 and this.world.getCenter
            Vector2 thisPoint = this.shape.body.getWorldCenter();
            System.out.println(thisPoint);
            pointstoPlace.offer(thisPoint);

            lastRecorded = System.currentTimeMillis();

        }

        this.shape.draw(canvas);
        for (AShape bar : tail) {
            bar.draw(canvas);
        }
    }

    public void placeTrail(Vector2 oldPoint, Vector2 newPoint){
        System.out.println("PLACING TRAIL");
        double x = (oldPoint.x + newPoint.x) / 2;
        double y = (oldPoint.y + newPoint.y) / 2;
        double l = Util.getDistance(oldPoint, newPoint);
        double ang = Util.absoluteAngle(oldPoint, newPoint);
        ObjRectangle temp = new ObjRectangle(x, y, l, 20, this.world);
        temp.rotateBody(ang);
        temp.body.setAsleep(true);
        this.world.objectDatabase.put(temp.body, this);
        tail.add(temp);
        this.lastPoint = newPoint;
    }

    @Override
    public void update(MyWorld world){
        while(pointstoPlace.size() > 0){
            Vector2 temp = pointstoPlace.poll();
            placeTrail(lastPoint, new Vector2(temp.x*MyWorld.SCALE, temp.y*MyWorld.SCALE));
            points.add(temp);
        }

    }

}