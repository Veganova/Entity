package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.WeldJoint;
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
    private static double reload = 3000;
    private double lastfired = 0;

    private MyWorld world;
    private List<Barrel> barrels = new ArrayList<>();
    private Barrel mainBarrel = null;
    private AShape center;
    private AimLogic logic;

    private List<AShape> components = new ArrayList<AShape>();

    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world, Team team){

        super(angle, 0, HEALTH, false, team);
        this.frictionCoefficent = 3;
        this.world = world;
        initializeTurret(location, world);
        this.logic = new SimpleAim(this, this.team);
    };

    private void addBarrel(Barrel.BarrelType type, Vector2 location) {
        //Projectile project = new SimpleLazer(new Vector2(0,0), 0, 400, 20, 300, 0, this.world);//
        Projectile projectile = new Missile(-10000, -10000, Missile.SPEED, 0, world, team, false);
        Barrel b = new Barrel(projectile, type, location, world, 3, team);

        this.barrels.add(b);
        WeldJoint joint = new WeldJoint(b.shape.body, this.center.body, location);
        world.engineWorld.addJoint(joint);
        this.components.add(b.shape);

        //this.shape = new ComplexShape(components, location.x, location.y, world);
        //this.world.objectDatabase.put(b.shape.body, this);

    }

    /**
     * Checks through the turrets if the given turret exists. If it does, make it the main turret.
     * @param mainBarrel
     */
    public void setMainBarrel(Barrel mainBarrel) {
        this.mainBarrel = mainBarrel;
//        for (Barrel barrel: barrels) {
//            if (barrel == mainBarrel) {
//
//            }
//        }
    }

    private void initializeTurret(Vector2 location, MyWorld world){

        this.center = new ObjRectangle(50, 50);
        AShape.InitBuilder builder = this.center.getBuilder(true, world);
        builder.setXY(location.x, location.y).init();
        this.shape = this.center;
        // TODO: 7/11/2017 take off and try
        this.world.objectDatabase.put(this.shape.body, this);
        this.components.add(shape);
        //this.shape = center;
        //this.shape = new ComplexShape(components);

        this.addBarrel(Barrel.BarrelType.SIDE, location);
        this.setMainBarrel(this.barrels.get(0));

        //this.addBarrel(Barrel.BarrelType.SINGLE);

    }

    @Override
    public void aim() {
        if (this.mainBarrel != null) {
            logic.aim(mainBarrel);
        }
        //logic.aim(body, this.barrels.get(1).shape);
    }

    @Override
    public void fire(double angle){
        if(System.currentTimeMillis() - lastfired <= reload ){
            return;
        }

        for (Barrel barrel: barrels) {
            barrel.fire(angle);
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

    @Override
    public boolean isSleeping() {
        return false;
    }

    public boolean inContact(Body contact){
        return this.barrels.get(0).shape.body.isInContact(contact) || center.body.isInContact(contact);
    }

    @Override
    public void draw(Canvas canvas){
        this.center.draw(canvas);
    }

}
