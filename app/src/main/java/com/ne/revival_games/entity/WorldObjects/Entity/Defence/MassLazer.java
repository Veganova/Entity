package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;

import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
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
    public static int HEAD_DAMAGE = 300;
    public static int TRAIL_DAMAGE = 20;

    private double fired;
    private double lastRecorded = 0;
    private ArrayList<AShape> tail;
    private ArrayList<Vector2> points;
    private Queue<Vector2> pointstoPlace;
    private Vector2 lastPoint;
    private MyWorld world;
    private double trailHeadStart = 30;

    public MassLazer(double x, double y, double direction, MyWorld world){
        super(x, y, MAZER_RADIUS, direction, 10, MAZER_HEALTH, world);
            this.isCollisionAuthority = true;
            fired = System.currentTimeMillis();
            tail = new ArrayList<>();
            points = new ArrayList<>();
            pointstoPlace = new LinkedBlockingQueue<>();
            lastPoint = new Vector2(x/ MyWorld.SCALE, y/MyWorld.SCALE);
            points.add(lastPoint);
            this.world = world;
    }


    @Override
    public void draw(Canvas canvas) {
//        needs to be deleted after x seconds

        if (System.currentTimeMillis() - lastRecorded > 100) {

            //two points point1 and this.world.getCenter
            Vector2 thisPoint = this.shape.body.getWorldCenter();
            System.out.println(thisPoint);
            pointstoPlace.offer(thisPoint);

            lastRecorded = System.currentTimeMillis();

        }

        Paint paint = new Paint();
        paint.setColor(0x00ffff);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) (MAZER_RADIUS / 2 / MyWorld.SCALE));

        Paint paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setAlpha(230);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth((float) (MAZER_RADIUS / 4 / MyWorld.SCALE));

        if(points.size() > 1) {
            for (int x = 0; x < points.size() - 1; x++) {
                Vector2 point1 = points.get(x);
                Vector2 point2 = points.get(x + 1);


                canvas.drawLine((float) point1.x, (float) point1.y, (float) point2.x, (float) point2.y,
                        paint);
                canvas.drawLine((float) point1.x, (float) point1.y, (float) point2.x, (float) point2.y,
                        paint2);
            }
        }
            int end = points.size()-1;

            //TODO: cheat to make it look smooth (there is a slight issue with mazer)!
            canvas.drawLine((float) points.get(end).x, (float) points.get(end).y, (float) this.shape.getX(), (float) this.shape.getY(),
                    paint);

        this.shape.draw(canvas);
//        for (AShape bar : tail) {
//            bar.draw(canvas);
//        }
    }

    public void placeTrail(Vector2 oldPoint, Vector2 newPoint){
        double x = (oldPoint.x + newPoint.x) / 2;
        double y = (oldPoint.y + newPoint.y) / 2;
        double l = Util.getDistance(oldPoint, newPoint);
        double ang = Util.absoluteAngle(oldPoint, newPoint);
        ObjRectangle temp = new ObjRectangle(x, y, l+trailHeadStart, 20, this.world);
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

        while (tail.size() > 20) {
            points.remove(0);
            world.engineWorld.removeBody(tail.get(0).body);
            world.objectDatabase.remove(tail.get(0).body);
            tail.remove(0);
        }

    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        if(contact == this){
            return false;
        }
        return componentHit == this.shape.body;
    }

    @Override
    public double getDamage(Body componentHit){
        if(componentHit == this.shape.body){
            return HEAD_DAMAGE;
        }
        return TRAIL_DAMAGE;
    }
}