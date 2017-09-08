package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.CustomEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.SlowEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.joint.WeldJoint;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veganova on 9/1/2017.
 */

public class Totem extends Entity {

    public static int HEALTH = 50;
    public static int STUB_HEALTH = 30;

    private static int DISTANCE = 90;


    private Effect slow;


    public Totem(double x, double y, double angle, MyWorld world, Team team, int numbars) {
        super(angle, 0, HEALTH, false, team);

        this.shape = new ObjCircle(30);
        AShape.InitBuilder builder = this.shape.getBuilder(true, world);
        builder.setAngle(angle).setXY(x, y).init();

        //this.shape.rotateBody(Math.toRadians(angle));
        world.objectDatabase.put(this.shape.body, this);

        initBars(x, y, world, numbars);
//        this.bar = new ActiveBar(this, 0.587f);


        this.slow = new SlowEffect(this, new ObjCircle(250), new Vector2(0, 0), 1.2, world);
        this.slow.toggleDraw();
        this.addEffect(this.slow);

        this.bar = new ActiveBar(this, 1f);
        this.bar.setPathType(ActiveBar.PathType.FILLED_CIRCLE, 0.6);
    }



    @Override
    public void interact() {
        super.interact();
        this.slow.toggle();
    }

    private static double ANGULAR_SPEED = 2.5;

    @Override
    public boolean update(MyWorld world) {
        double dTetha = this.shape.body.getAngularVelocity();
//        System.out.println(dTetha);
        // ccw
        if (dTetha > 0) {
            normalize(dTetha, ANGULAR_SPEED);
        } else {
            normalize(dTetha, -ANGULAR_SPEED);
        }

        return super.update(world);
    }

    private void normalize(double dTetha, double targetSpeed) {
        if (!doubleEqual(dTetha, targetSpeed)) {
            if (dTetha > targetSpeed) {
                this.shape.body.applyTorque(-10);
            } else {
                this.shape.body.applyTorque(10);
            }
        }
    }

    private boolean doubleEqual(double dTetha, double angularSpeed) {
        return Math.abs(dTetha - angularSpeed) < 0.001;
    }


    private List<Entity> bars = new ArrayList<>();

    private void initBars(double x, double y, MyWorld world, int numBars) {

        double delta = 2 * Math.PI / numBars;

        for (double theta = 0; theta < 2 * Math.PI; theta += delta) {

            double barX = DISTANCE * Math.cos(theta) + x;
            double barY = DISTANCE * Math.sin(theta) + y;

            AShape bar = new ObjRectangle(20, 100);
            AShape.InitBuilder builder = bar.getBuilder(true, world);
            builder.setAngle(Math.toDegrees(theta * 2)).setXY(barX, barY).init();

            this.bars.add(new CustomEntity(bar, 0, STUB_HEALTH, false, team, world));


            WeldJoint joint = new WeldJoint(this.shape.body, bar.body, this.shape.body.getWorldCenter());

            joint.setCollisionAllowed(true);
            joint.setFrequency(8);
            joint.setDampingRatio(0.8);

            world.engineWorld.addJoint(joint);
        }
    }

    @Override
    public void addToPlayer(Player player) {
        super.addToPlayer(player);
        for (Entity bar : this.bars) {
            bar.addToPlayer(player);
        }
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);

        for (Entity bar : this.bars) {
            bar.setColor(color);
        }
    }

    public boolean isInContact(Body body) {
        if (super.isInContact(body)) {
            return true;
        } else {
            for (Entity bar : this.bars) {
                if (bar.isInContact(body)) {
                    return true;
                }
            }
        }
        return false;
    }
}
