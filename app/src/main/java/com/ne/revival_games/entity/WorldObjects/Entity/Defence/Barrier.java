package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.util.Pair;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
import com.ne.revival_games.entity.WorldObjects.Entity.Aim.Shootable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Untargetable;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;


/**
 * Players can place this to protect
 */
public class Barrier extends Entity {

    public static int COST = 100;
    public static int MASS = 20;
    public static int HEALTH = 100;
//    public static Team team = Team.OFFENSE;

    public static double WIDTH = 200;
    public static double HEIGHT = 30;


    public Barrier(double x, double y, double angle, MyWorld world, Team team) {
        super(angle, 0, HEALTH, false, team);
        this.shape = new ObjRectangle(WIDTH, HEIGHT);
        AShape.InitBuilder builder = this.shape.getBuilder(true, world);
        builder.setAngle(angle).setXY(x, y).init();

        //this.shape.rotateBody(Math.toRadians(angle));
        world.objectDatabase.put(this.shape.body, this);

//        this.shape.body.setAngularDamping(this.shape.body.getAngularDamping() * 10);
//        this.shape.body.setLinearDamping(this.shape.body.getLinearDamping() * 10);

        this.bar = new ActiveBar(this, 0.8f);
        this.bar.setPathType(ActiveBar.PathType.LINE, 200);

        this.targetExceptions.addType(Missile.class, Untargetable.FROM.ALLY);
    }

    @Override
    public void interact() {
        super.interact();
        this.targetExceptions.toggle();
    }


}

