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
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.RevoluteJoint;
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
    public static Team TYPE = Team.DEFENCE;
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
        //Projectile project = new SimpleLazer(new Vector2(0,0), 0, 400, 20, 300, 0, this.world);//
        Projectile projectile = new Missile(0, 0, Missile.SPEED, 0, world);
        Barrel b = new Barrel(projectile, type, new Vector2(center.getX(), center.getY()), world, 3);
        this.barrels.add(b);
        RevoluteJoint joint = new RevoluteJoint(b.shape.body, this.center.body, this.getCenter());

        this.components.add(b.shape);
        world.engineWorld.addJoint(joint);

        this.shape = new ComplexShape(components);
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

        this.world.objectDatabase.put(this.center.body, this);
        this.components.add(center);
        this.shape = new ComplexShape(components);

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
