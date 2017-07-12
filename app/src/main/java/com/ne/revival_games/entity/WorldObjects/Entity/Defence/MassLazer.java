package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;

import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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


    public MassLazer(double x, double y, double direction, MyWorld world, Team team, boolean addToWorld){
        super(direction, 10, MAZER_HEALTH, world, team, addToWorld);
            this.isCollisionAuthority = true;
        if(addToWorld) {
            fired = System.currentTimeMillis();
            tail = new ArrayList<>();
            points = new ArrayList<>();
            pointstoPlace = new LinkedList<>();
            lastPoint = new Vector2(x / MyWorld.SCALE, y / MyWorld.SCALE);
            points.add(lastPoint);
            this.world = world;
            this.team = team;

//        initializeHead(x, y);
            this.shape = new ObjCircle(30);
            this.shape.getBuilder(true, world).setXY(x, y).init();

            this.world.objectDatabase.put(this.shape.body, this);
            this.shape.body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
            this.setVelocity(20);
        }

    }

    public void initializeHead(double x, double y){
//        List<AShape> components = new ArrayList<>();
//        components.add(new ObjRectangle(0, 0, MAZER_RADIUS, MAZER_RADIUS, 0));
//        double [] points = { 0, MAZER_RADIUS,MAZER_RADIUS, 0, MAZER_RADIUS*1.5, MAZER_RADIUS*1.5};
//        components.add(new ObjTriangle(-MAZER_RADIUS/2, -MAZER_RADIUS/2, points, 0 ));
//        this.shape = new ComplexShape(components, x, y, this.world);
//        this.world.objectDatabase.put(this.shape.body, this);
    }


    @Override
    public void draw(Canvas canvas) {
//        needs to be deleted after x seconds

        if (System.currentTimeMillis() - lastRecorded > 50) {

            //two points point1 and this.world.getCenter
            Vector2 thisPoint = this.shape.body.getWorldCenter();
            pointstoPlace.offer(thisPoint);

            lastRecorded = System.currentTimeMillis();

        }

        Paint paint = new Paint();
        paint.setColor(0x00ffff);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) (MAZER_RADIUS / 2 / MyWorld.SCALE));

//        Paint paint2 = new Paint();
//        paint2.setColor(Color.RED);
//        paint2.setAlpha(230);
//        paint2.setStyle(Paint.Style.STROKE);
//        paint2.setStrokeWidth((float) (MAZER_RADIUS / 4 / MyWorld.SCALE));
//
        if(points.size() > 1) {
            for (int x = 0; x < points.size() - 1; x++) {
                Vector2 point1 = points.get(x);
                Vector2 point2 = points.get(x + 1);


                canvas.drawLine((float) point1.x, (float) point1.y, (float) point2.x, (float) point2.y,
                        paint);
//                canvas.drawLine((float) point1.x, (float) point1.y, (float) point2.x, (float) point2.y,
//                        paint2);
            }
        }
            int end = points.size()-1;

//            //TODO: cheat to make it look smooth (there is a slight issue with mazer)!
            canvas.drawLine((float) points.get(end).x, (float) points.get(end).y, (float) this.shape.getX(), (float) this.shape.getY(),
                    paint);

        this.shape.draw(canvas);
    }

    public void placeTrail(Vector2 oldPoint, Vector2 newPoint){
        double x = (oldPoint.x + newPoint.x) / 2;
        double y = (oldPoint.y + newPoint.y) / 2;
        double l = Util.getDistance(oldPoint, newPoint)  * MyWorld.SCALE;
        double ang = Util.absoluteAngle(oldPoint, newPoint);
        ObjRectangle rect = new ObjRectangle(l+trailHeadStart, 20);
        rect.getBuilder(true, world).setAngle(ang).setXY(x * MyWorld.SCALE, y * MyWorld.SCALE).init();
        rect.body.setAsleep(true);
        this.world.objectDatabase.put(rect.body, this);
        tail.add(rect);
        this.lastPoint = newPoint;
    }

    @Override
    public void update(MyWorld world){
        //can optimize here (ex. don't place if point has no hope or too many points)
        while(pointstoPlace.size() > 0){
            Vector2 temp = pointstoPlace.poll();
            if(Util.getDistance(temp, lastPoint) > 5/ MyWorld.SCALE) {
                placeTrail(lastPoint, temp);
                points.add(temp);
            }
        }


        while (tail.size() > 10) {
            points.remove(0);
            this.world.bodiestodelete.add(tail.get(0).body);
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


    @Override
    public Projectile returnCustomizedCopy(Projectile project, Vector2 location,
                                           double direction, double speed, MyWorld world, Team team){
        return new MassLazer(location.x, location.y, direction, world, team, true);

    }
}