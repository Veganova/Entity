package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.GravityEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 6/29/2017.
 */

public class Nexus extends Entity {
    public static int HEALTH = 200;
    public static int MASS;
    public static int COST;
    private List<AShape> components;
    private static int centerDensity = 200;
    public static double LINEAR_DAMPING = 2;
    public static double ANGULAR_DAMPING = 0.25;

    private GravityEffect gravEffect;

    public Nexus(double x, double y, double angle, MyWorld world, Team team) {
        super(angle, 0, HEALTH, false, team, DEFAULT_FRICTION);
        frictionCoefficent = 90;
        components = new ArrayList<>();

        ObjRectangle rect = new ObjRectangle(120, 120);
        rect.getBuilder(false, world).setXY(0, 0).setDensity(centerDensity).
                setLinearDamping(ANGULAR_DAMPING).setAngularDamping(ANGULAR_DAMPING).init();

        components.add(rect);

        double [] points1 = {0, 40, 0, -40, 40, 0};
        ObjTriangle tri1 = new ObjTriangle(points1);
        tri1.getBuilder(false, world).setXY(50, 0).init();
        components.add(tri1);
        
        double [] points2 = {0, -40, 0, 40, -40, 0};
        ObjTriangle tri2 = new ObjTriangle(points2);
        tri2.getBuilder(false, world).setXY(-50, 0).init();
        components.add(tri2);       
        
        double [] points3 = {-40, 0, 40, 0, 0, 40};
        ObjTriangle tri3 = new ObjTriangle(points3);
        tri3.getBuilder(false, world).setXY(0, 50).init();
        components.add(tri3);
        
        double [] points4 = {40, 0,-40, 0,0, -40};
        ObjTriangle tri4 = new ObjTriangle(points4);
        tri4.getBuilder(false, world).setXY(0, -50).init();
        components.add(tri4);

        this.shape = new ComplexShape(components, x, y, world);
        world.objectDatabase.put(this.shape.body, this);
        this.team = team;
        gravEffect = new GravityEffect(this, 1000, 20, new Vector2(0,0), world);
        this.addEffect(gravEffect);

        this.bar = new ActiveBar(this, 1f);
//        this.bar.setPathType(ActiveBar.PathType.LINE, 200);
    }

    @Override
    public boolean update(MyWorld world){
//        System.out.println(this.shape.body.getWorldCenter().x*MyWorld.SCALE + " " +
//                this.shape.body.getWorldCenter().y*MyWorld.SCALE);
        return super.update(world);
    }

    @Override
    public void interact() {
        super.interact();
        this.gravEffect.toggle();
    }

    boolean gameRunning = true;
    @Override
    public void onDeath(MyWorld world) {
        super.onDeath(world);

        if (gameRunning) {
            world.gameOver();
            gameRunning = false;
        }
    }
}
