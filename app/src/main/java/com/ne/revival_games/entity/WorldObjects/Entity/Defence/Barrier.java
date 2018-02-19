package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Untargetable;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;


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


    public Barrier(double x, double y, double angle, MyWorld world, Team team, String tag) {
        super(angle, 0, team, tag + "barrier");
        double width = MySettings.getNum(team.toString(), name_tag + " width");
        this.shape = new ObjRectangle(width,
                MySettings.getNum(team.toString(), name_tag + " height"));
        AShape.InitBuilder builder = this.shape.getBuilder(true, world);
        builder.setBasics(team.toString(), name_tag).setAngle(angle).setXY(x, y).init();

        //this.shape.rotateBody(Math.toRadians(angle));
        world.objectDatabase.put(this.shape.body, this);

//        this.shape.body.setAngularDamping(this.shape.body.getAngularDamping() * 10);
//        this.shape.body.setLinearDamping(this.shape.body.getLinearDamping() * 10);

        this.bar = new ActiveBar(this, (float)(0.8f/200 * width));
        this.bar.setPathType(ActiveBar.PathType.LINE, width);

        this.targetExceptions.addType(Missile.class, Untargetable.FROM.ALLY);
    }

    @Override
    public void interact() {
        super.interact();
        this.targetExceptions.  toggle();
    }


}

