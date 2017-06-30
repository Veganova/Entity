package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;

import java.util.Iterator;

import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;

/**
 * Created by vishn on 6/30/2017.
 */

//TODO: implement this better (maybe actually place rectangle objects in world so collision can handle it)
//don't need an "iswithinburn" radius either, set as invulnerable

public class MassLazer extends Projectile {
    public static int MAZER_RADIUS = 2;
    public static int MAZER_HEALTH = 300;

    private double fired;
    private double lastRecorded = 0;
    private ArrayList<AShape> tail;
    private ArrayList<Vector2> points;
    private Vector2 lastPoint;
    private MyWorld world;

    public MassLazer(double x, double y, double direction, MyWorld world){
            super(x, y, MAZER_RADIUS, direction, 100, MAZER_HEALTH, world);
            fired = System.currentTimeMillis();
            tail = new ArrayList<>();
            points = new ArrayList<>();
            lastPoint = this.shape.body.getWorldCenter();
            points.add(lastPoint);
            this.world = world;
    }

//    private boolean withinBurnRadius(Vector2 location){
//        double widthDetection = MAZER_RADIUS;
//        Iterator<Vector2> iterator = queue.iterator();
//
//        Vector2 point1 = iterator.next();
//        for(int x=0; x<queue.size()-1; x++){
//            Vector2 point2 = iterator.next();
//
//            //may be overly precise
//            double sumDistance = Util.getDistance(point2, location) + Util.getDistance(point1, location);
//            double lineLength = Util.getDistance(point1, point2);
//            if(lineLength > sumDistance - widthDetection || lineLength < sumDistance + widthDetection){
//                return false;
//            }
//            if(Util.distanceFromLine(point1, point2, location) <= widthDetection){
//                return true;
//            }
//
//            point1 = point2;
//        }
//
//        return false;
//    }

    @Override
    public void draw(Canvas canvas){
        //needs to be deleted after x seconds

        if(lastRecorded - System.currentTimeMillis() > 500){
            if(tail.size() > 20){
                AShape shape = tail.get(19);
                tail.remove(0);
                points.remove(0);
                world.engineWorld.removeBody(shape.body);
                world.objectDatabase.remove(shape.body);
            }
            //two points lastPoint and this.world.getCenter
            Vector2 thisPoint = this.shape.body.getWorldCenter();
            double x = (lastPoint.x + thisPoint.x)/2;
            double y = (lastPoint.y + thisPoint.y)/2;
            double l = Util.getDistance(lastPoint, thisPoint);
            double ang = Util.absoluteAngle(lastPoint, thisPoint);
            tail.add(new ObjRectangle(x, y, MAZER_RADIUS, l,  ang));
            tail.get(19).body.setAsleep(true);
            lastPoint = thisPoint;
        }



        this.shape.draw(canvas);

        if(points.size() > 1)
        for(int x=0; x<points.size()-1; x++){
            Vector2 point1 = points.get(x);
            Vector2 point2 = points.get(x+1);

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(MAZER_RADIUS);

            canvas.drawLine((float) point1.x, (float) point1.y, (float) point2.x, (float) point2.y,
                    new Paint());
        }

    }

//    @Override
//    public void draw(Canvas canvas){
//        //needs to be deleted after x seconds
//
//        if(lastRecorded - System.currentTimeMillis() > 500){
//            if(queue.size() > 20){
//                queue.remove();
//            }
//            queue.add(this.shape.body.getWorldCenter());
//        }
//        this.shape.draw(canvas);
//        Iterator<Vector2> iterator = queue.iterator();
//
//        Vector2 point1 = iterator.next();
//        for(int x=0; x<queue.size()-1; x++){
//            Vector2 point2 = iterator.next();
//
//            Paint paint = new Paint();
//            paint.setColor(Color.RED);
//            paint.setStrokeWidth(MAZER_RADIUS);
//
//            canvas.drawLine((float) point1.x, (float) point1.y, (float) point2.x, (float) point2.y,
//                    new Paint());
//        }
//    }


}