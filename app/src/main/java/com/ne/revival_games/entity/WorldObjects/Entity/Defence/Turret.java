package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
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
    private static double reload = 3000;
    private double lastfired = 0;
    private double range = 8;

    private MyWorld world;
    public List<Barrel> barrels = new ArrayList<>();
    private Barrel mainBarrel = null;
    private AShape center;
    private AimLogic logic;
    private Projectile myProjectile;

    private List<AShape> components = new ArrayList<AShape>();
    private boolean aiming = true;

    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world, Team team){

        super(angle, 0, 80, false, team);
        this.frictionCoefficent = 3;
        this.world = world;
        initializeTurret(location, world);
        this.logic = new SimpleAim(this);

        this.bar = new ActiveBar(this);
    };

    private void addBarrel(Barrel.BarrelType type, Vector2 location, double angle) {
        //Projectile project = new SimpleLazer(new Vector2(0,0), 0, 400, 20, 300, 0, this.world);//
        Projectile projectile = new Missile(0, 0, Missile.SPEED, 0, world, team, false);
        Barrel b = new Barrel(projectile, type, this, world, angle, team, location);

        this.barrels.add(b);
        WeldJoint joint = new WeldJoint(b.shape.body, this.center.body, location);
        world.engineWorld.addJoint(joint);
//        this.components.add(b.shape);

        //this.shape = new ComplexShape(components, location.x, location.y, world);
        //this.world.objectDatabase.put(b.shape.body, this);

    }

    /**
     * Checks through the turrets if the given turret exists. If it does, make it the main turret.
     * @param mainBarrel
     */
    public void setMainBarrel(Barrel mainBarrel) {
        this.mainBarrel = mainBarrel;
    }

    private void initializeTurret(Vector2 location, MyWorld world){
        this.center = new ObjRectangle(50, 50);
        AShape.InitBuilder builder = this.center.getBuilder(true, world);
        builder.setXY(location.x, location.y).init();
        this.shape = this.center;
        // TODO: 7/11/2017 take off and try
        this.world.objectDatabase.put(this.shape.body, this);
//        this.components.add(shape);

//        this.addBarrel(Barrel.BarrelType.SIDE, location, 0);
        this.addBarrel(Barrel.BarrelType.SINGLE, location, -30);
          this.addBarrel(Barrel.BarrelType.SINGLE, location, 30);
//        this.addBarrel(Barrel.BarrelType.SINGLE, location, 270);
//        this.addBarrel(Barrel.BarrelType.SINGLE, location);
          this.setMainBarrel(this.barrels.get(0));


        //this.addBarrel(Barrel.BarrelType.SINGLE);
    }

    @Override
    public void aim() {
        if (this.mainBarrel != null && this.aiming && !this.isSleeping()) {
            logic.aim(mainBarrel);
        }
        //logic.aim(body, this.barrels.get(1).shape);
    }

    public void selectNewMainBarrel(Barrel deadBarrel){
        if(deadBarrel == mainBarrel){
            this.mainBarrel = barrels.get(0);
        }
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        boolean answer = super.onCollision(contact, componentHit, damage);
        if(this.health <= 0) {
            for(Barrel myBarrel : barrels){
                barrels.remove(myBarrel);
                myBarrel.myTurret = null;
            }
        }
        return answer;
    }

    public void setMotion(double angularVel){
        this.shape.body.setAngularVelocity(angularVel);
//        for(Barrel barrel : barrels) {
//            barrel.shape.body.setAngularVelocity(angularVel);
//        }
    }

    @Override
    public void onDeath(MyWorld world){
        if(this.health <= 0) {
            for(Barrel myBarrel : barrels){
                barrels.remove(myBarrel);
                myBarrel.myTurret = null;
            }
        }
        super.onDeath(world);
    }

    @Override
    public void fire(double angle){
        if(System.currentTimeMillis() - lastfired <= reload ){
            return;
        }

        for (Barrel barrel: barrels) {
            if(barrel == mainBarrel){
                barrel.fire(angle);
            }
            else {
                barrel.fire(barrel.shape.body.getTransform().getRotation());
            }
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
        return mainBarrel != null && this.mainBarrel.isSleeping();
    }

//    public boolean inContact(Body contact){
//        return this.barrels.get(0).shape.body.isInContact(contact) || center.body.isInContact(contact);
//    }


    @Override
    public void addToTeam(Team team) {
        super.addToTeam(team);
        for (Barrel barrel: this.barrels) {
            barrel.addToTeam(team);
        }
    }

    @Override
    public void interact() {
        super.interact();
        this.aiming = !this.aiming;
    }

//    @Override
//    public void draw(Canvas canvas){
//        this.center.draw(canvas);
//    }

}
