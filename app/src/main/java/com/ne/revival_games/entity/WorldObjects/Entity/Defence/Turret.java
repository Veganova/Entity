package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.ObjectType;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.AngleJoint;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 6/11/2017.
 */

public class Turret extends Entity implements Aimable {
    public static int COST = 200;
    public static int MASS = 25;
    public static int HEALTH = 80;
    public static ObjectType TYPE = ObjectType.DEFENCE;
    private static double reload = 3000;
    private double lastfired = 0;

    private List<AShape> components = new ArrayList<AShape>();
    private WeldJoint joint;
    private MyWorld world;
    private List<Barrel> bullets = new ArrayList<>();
    private AShape center;

    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world){
        super(location.x, location.y, angle, 0, HEALTH, false);
        initializeTurret(location, world);
        this.world = world;
    };

    public void initializeTurret(Vector2 location, MyWorld world){
        //make sure relative location placement is correct
        components.add(new ObjRectangle(50+location.x, location.y, 100, 20, world));
        components.add(new ObjCircle(location.x, location.y, 50.0, world));
        joint = new WeldJoint(components.get(0).body, components.get(1).body,
        components.get(1).body.getWorldCenter());
        world.engineWorld.addJoint(joint);
        this.shape = new ComplexShape(components);

        // TODO: 6/29/2017 shouldnt this be done in the shape class:
        world.objectDatabase.put(components.get(0).body, this);
        world.objectDatabase.put(components.get(1).body, this);
    }

    @Override
    public void aim(Body body){
        Vector2 center = components.get(1).body.getWorldCenter();
        Vector2 axispoint = new Vector2(center.x + 50/MyWorld.SCALE, center.y);
        Vector2 enemypoint = body.getWorldCenter();
        System.out.println("");
        double angle = (Math.PI*2 + components.get(0).getOrientation()
                + this.components.get(0).body.getTransform().getRotation()) % (Math.PI * 2);

        double getSign = (enemypoint.y - center.y);
        double a = Util.getDistance(center, axispoint);
        double b = Util.getDistance(center, enemypoint);
        double c = Util.getDistance(axispoint, enemypoint);
        double angleTo;

        //should break on 180 degrees but who knows?
        if(getSign == 0){
            if(Math.abs(angle - Math.PI) <= 0.2 && enemypoint.x - center.x < 0){
                angleTo = 0;
            }
            else if(Math.abs(angle) <= 0.2 && enemypoint.x - center.x > 0){
                angleTo = 0;
            }
            else{
                angleTo = Math.PI;
            }

        }
        else{
            angleTo = getSign/Math.abs(getSign)*Math.acos((Math.pow(a, 2) + Math.pow(b, 2)
                    - Math.pow(c, 2)) / (2 * a * b));
            angleTo = (Math.PI *2 + angleTo) % (Math.PI *2);
        }

        double angleDifference = angleTo - angle;
        double counterclockDist = (Math.PI * 2 + angleTo - angle) % (Math.PI*2);

//        System.out.println("Current angle: " + Math.toDegrees(angle));
//        System.out.println("angleTo: " + Math.toDegrees(angleTo));
//        System.out.println("angle difference: " + angleDifference);


        //better find a shortest distance algorithm
        if (Math.abs(angleDifference) <= 0.02){
            fire(angleTo);
        }
        else {
            double increment = 0.15;
            if(Math.abs(angleDifference) <= 0.15){
                increment = 0.09;
            }
            if(Math.abs(angleDifference) <= 0.09){
                increment = 0.07;
            }
            if(Math.abs(angleDifference)<= 0.07){
                increment = 0.05;
            }
            if(Math.abs(angleDifference)<= 0.05){
                increment = 0.03;
            }
            if (counterclockDist <= Math.PI){

                components.get(0).body.rotate(increment, center.x, center.y);
            }
            else{
                components.get(0).body.rotate(-1*increment, center.x, center.y);
            }
        }


    }


    @Override
    public void fire(double angle){
        if(System.currentTimeMillis() - lastfired <= reload ){
            return;
        }
        double magnitude = 100;
        lastfired = System.currentTimeMillis();
        double x, y;
        //magnitude of 90 away
        x = magnitude * Math.cos(angle) + MyWorld.SCALE*components.get(1).body.getWorldCenter().x;
        y = magnitude * Math.sin(angle) + MyWorld.SCALE*components.get(1).body.getWorldCenter().y;
        angle = Math.toDegrees(angle);
        new Missile(x, y, angle, 30, this.world);
    }

    public boolean inContact(Body contact){
        return components.get(0).body.isInContact(contact) || components.get(1).body.isInContact(contact);
    }

}
