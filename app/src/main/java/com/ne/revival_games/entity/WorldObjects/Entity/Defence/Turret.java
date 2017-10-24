package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimLogic;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.AimableEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 6/11/2017.
 */

public class Turret extends AimableEntity {
    public static int COST = 200;
    public static int MASS = 25;
    public static double LINEAR_DAMPING = 5;
    public static double ANGULAR_DAMPING = 0.09;
    private static double reload = 3000;
    private double lastfired = 0;
    private double range = 800;

    private MyWorld world;
    public List<Barrel> barrels = new ArrayList<>();
    private final int numBarrels;
    private Barrel mainBarrel = null;
    private AShape center;


//    private Projectile myProjectile;
    private List<AShape> components = new ArrayList<AShape>();

//    private AimLogic logic;
//    public boolean aiming = true;


    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world, Team team, int numBarrels){
        super(angle, 0, 80, false, team);
        this.numBarrels = numBarrels;
        this.frictionCoefficent = 60;
        this.world = world;
        initializeTurret(location, world);
        this.logic = new SimpleAim(this, world.objectDatabase, range);

        this.bar = new ActiveBar(this, 0.587f);
    };

    private void addBarrel(Barrel.BarrelType type, Vector2 location, double angle) {
        //Projectile project = new SimpleLazer(new Vector2(0,0), 0, 400, 20, 300, 0, this.world);//
//        Projectile projectile = new Missile(0, 0, Missile.SPEED, 0, world, team, false);

        Barrel b = new Barrel(new EntityLeaf("Missile") {
            @Override
            public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                return new Missile(x, y, angle, 0, world, team);
            }
        }, type, this, world, angle, team, location, this.frictionCoefficent);

        this.barrels.add(b);
        WeldJoint joint = new WeldJoint(b.shape.body,
                this.shape.body, new Vector2(location.x/MyWorld.SCALE, location.y/MyWorld.SCALE));
        world.engineWorld.addJoint(joint);
        // this.world.objectDatabase.put(b.shape.body, this);
        // this.components.add(b.shape);
        // this.shape = new ComplexShape(components, location.x, location.y, world);
    }

    /**
     * Checks through the turrets if the given turret exists. If it does, make it the main turret.
     * @param mainBarrel
     */
    public void setMainBarrel(Barrel mainBarrel) {
        this.mainBarrel = mainBarrel;
    }

    private void initializeTurret(Vector2 location, MyWorld world){
        this.shape = new ObjCircle(30);
        AShape.InitBuilder builder = this.shape.getBuilder(true, world);
        builder.setXY(location.x, location.y)
                .setLinearDamping(LINEAR_DAMPING).setAngularDamping(ANGULAR_DAMPING).init();
        // TODO: 7/11/2017 take off and try
        this.world.objectDatabase.put(this.shape.body, this);
//        this.components.add(shape);

//        this.addBarrel(Barrel.BarrelType.SIDE, location, 0);
        //very weird behavior with angle (each angle is like x2 what it is expected to be)

        if (numBarrels == 1) {
            this.addBarrel(Barrel.BarrelType.SINGLE, new Vector2(location.x, 32 + location.y), 45);
        }
        else if (numBarrels == 2) {
            this.addBarrel(Barrel.BarrelType.SINGLE, new Vector2(-36 + location.x, 30 + location.y), 45);
            this.addBarrel(Barrel.BarrelType.SINGLE, new Vector2(36 + location.x, 30 + location.y), 45);
        }
        else if (numBarrels == 3) {
            this.addBarrel(Barrel.BarrelType.SINGLE, new Vector2(location.x, 32 + location.y), 45);
            this.addBarrel(Barrel.BarrelType.SINGLE, new Vector2(-36 + location.x, 30 + location.y), 45);
            this.addBarrel(Barrel.BarrelType.SINGLE, new Vector2(36 + location.x, 30 + location.y), 45);
        }
//        this.world.engineWorld.addJoint(new WeldJoint(barrels.get(0).shape.body, barrels.get(1).shape.body, location));
//        this.addBarrel(Barrel.BarrelType.SINGLE, location, 270);
//        this.addBarrel(Barrel.BarrelType.SINGLE, location);
          this.setMainBarrel(this.barrels.get(0));

    }

    public void selectNewMainBarrel(Barrel deadBarrel){
        if(deadBarrel == mainBarrel && barrels.size() > 0){
            this.mainBarrel = barrels.get(0);
        }
    }

    @Override
    public void addToPlayer(Player player) {
        super.addToPlayer(player);
        for(Barrel barrel: this.barrels) {
            barrel.addToPlayer(player);
        }
    }


    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        boolean answer = super.onCollision(contact, componentHit, damage);
//        for(Barrel myBarrel: barrels) {
//            if(myBarrel == contact) {
//                return false;
//            }
//        }
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
                for(Barrel barrel : barrels) {
                    barrel.frictionCoefficent = Barrel.DEFAULT_FRICTION;
                }

                while(barrels.size() > 0){
                    barrels.get(0).myTurret = null;
                    barrels.remove(0);
                }
        }
        super.onDeath(world);
    }


    @Override
    public void aim() {
        if (this.mainBarrel != null) {// && this.aiming && !this.isSleeping()) {
            super.aim();
        }
    }

    @Override
    public Entity getPartToAimWith() {
        return mainBarrel;
    }

    @Override
    public void fire() {
//        System.out.println("TURRET IS FIRING!");
        if(System.currentTimeMillis() - lastfired <= reload){
            return;
        }

        for (Barrel barrel: barrels) {
            if(barrel == mainBarrel){
                barrel.fire();
            }
            else {
                barrel.fire();
//                barrel.fire(barrel.shape.body.getTransform().getRotation());
            }
        }

        lastfired = System.currentTimeMillis();
    }

    @Override
    public void changeLogicTo(AimLogic logic) {
        this.logic = logic;
    }

    @Override
    public int getTurnSpeed() {
        return 10;
    }

//    @Override
//    public Vector2 getCenter() {
//        return this.shape.body.getWorldCenter();
//    }

//    @Override
//    public boolean isSleeping() {
//        return mainBarrel != null && this.mainBarrel.isSleeping();
//    }

    @Override
    public void interact() {
        super.interact();
        this.aiming = !this.aiming;
    }


    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);

        for (Barrel barrel: this.barrels) {
            barrel.setColor(color);
        }
    }

    public boolean isInContact(Body body) {
        if (super.isInContact(body)) {
            return true;
        } else {
            for (Barrel barrel: this.barrels) {
                if (barrel.isInContact(body)) {
                    return true;
                }
            }
        }
        return false;
    }
}


