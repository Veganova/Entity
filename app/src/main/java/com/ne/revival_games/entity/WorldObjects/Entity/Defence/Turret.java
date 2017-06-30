package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

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

    private WeldJoint joint;
    private MyWorld world;
    private List<Barrel> barrels = new ArrayList<>();
    private AShape center;
    private AimLogic logic;
    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world){
        super(location.x, location.y, angle, 0, HEALTH, false);
        initializeTurret(location, world);
        this.world = world;
        this.logic = new SimpleAim(this);
    };

    private void initializeTurret(Vector2 location, MyWorld world){
        //make sure relative location placement is correct
        this.barrels.add(new Barrel(Barrel.BarrelType.SIDE, location, world, 1));
       // this.barrels.add(new Barrel(Barrel.BarrelType.SINGLE, location, world));


        this.center = new ObjCircle(location.x, location.y, 50.0, world);

        List<AShape> components = new ArrayList<AShape>();
        components.add(barrels.get(0).shape);
        components.add(this.center);



        joint = new WeldJoint(components.get(0).body, components.get(1).body,
                                components.get(1).body.getWorldCenter());

        world.engineWorld.addJoint(joint);
        this.shape = new ComplexShape(components);

        // TODO: 6/29/2017 can this be done in the shape class:
        world.objectDatabase.put(components.get(0).body, this);
        world.objectDatabase.put(components.get(1).body, this);
    }

    @Override
    public void aim(Body body){
        logic.aim(body, this.barrels.get(0).shape);
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

}
