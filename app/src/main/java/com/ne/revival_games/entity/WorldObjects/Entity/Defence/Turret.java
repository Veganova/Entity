package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.ObjectType;
import com.ne.revival_games.entity.WorldObjects.Entity.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.Shape;

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

    private MyWorld world;
    private List<Barrel> barrels = new ArrayList<>();
    private Barrel mainBarrel = null;
    private AShape center;
    private AimLogic logic;

    private List<AShape> components = new ArrayList<AShape>();

    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world){
        super(location.x, location.y, angle, 0, HEALTH, false);
        this.world = world;
        initializeTurret(location, world);
        this.logic = new SimpleAim(this);
    };

    public void addBarrel(Barrel.BarrelType type) {
        System.out.println("ADDING BARREL");
        Barrel b = new Barrel(type, new Vector2(center.getX(), center.getY()), world, 3);
        this.barrels.add(b);
        RevoluteJoint joint = new RevoluteJoint(b.shape.body, this.center.body, this.getCenter());

        this.components.add(b.shape);
        world.engineWorld.addJoint(joint);

        this.shape = new ComplexShape(components);
        System.out.println("ADDED BARREL");
    }

    /**
     * Checks through the turrets if the given turret exists. If it does, make it the main turret.
     * @param mainBarrel
     */
    public void setMainBarrel(Barrel mainBarrel) {
        this.mainBarrel = mainBarrel;
        System.out.println("MAIN BARREL SET");
//        for (Barrel barrel: barrels) {
//            if (barrel == mainBarrel) {
//
//            }
//        }
    }

    private void initializeTurret(Vector2 location, MyWorld world){

        this.center = new ObjRectangle(location.x, location.y, 50, 50, world);
        this.world.objectDatabase.put(this.center.body, this);

        this.addBarrel(Barrel.BarrelType.SIDE);
        this.setMainBarrel(this.barrels.get(0));

        //this.addBarrel(Barrel.BarrelType.SINGLE);

       // stupidshit(location);
    }

    @Override
    public void aim(Body body) {
        if (this.mainBarrel != null) {
            logic.aim(body, mainBarrel.shape);
        }
        //logic.aim(body, this.barrels.get(1).shape);
    }

    @Override
    public void fire(double angle){
        if(System.currentTimeMillis() - lastfired <= reload ){
            return;
        }

        for (Barrel barrel: barrels) {
            barrel.fire();
        }

        lastfired = System.currentTimeMillis();
    }

    @Override
    public Vector2 getCenter() {
        return this.center.body.getWorldCenter();
    }

    @Override
    public void changeLogicTo(AimLogic logic) {
        this.logic = logic;
    }

    public boolean inContact(Body contact){
        return this.barrels.get(0).shape.body.isInContact(contact) || center.body.isInContact(contact);
    }

    @Override
    public void draw(Canvas canvas){
        this.center.draw(canvas);
    }

}
