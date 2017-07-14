package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
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
        this.shape = new ObjRectangle(200, 30);
        AShape.InitBuilder builder = this.shape.getBuilder(true, world);
        builder.setAngle(angle).setXY(x, y).init();

        //this.shape.rotateBody(Math.toRadians(angle));
        world.objectDatabase.put(this.shape.body, this);
    }

}
