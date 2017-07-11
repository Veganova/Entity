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
//    public static Team TYPE = Team.OFFENSE;


    public Barrier(double x, double y, double angle, MyWorld world, Team team) {
        super(angle, 0, HEALTH, false, team);
        this.shape = new ObjRectangle(200, 30);
        AShape.InitBuilder builder = this.shape.getBuilder(true, world);
        builder.setAngle(angle).setXY(x, y).init();

        //this.shape.rotateBody(Math.toRadians(angle));
        world.objectDatabase.put(this.shape.body, this);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        if(contact.TYPE.opposite(this.TYPE)){
            this.health -= damage;
            if(this.health <= 0){
                this.invisible = true;
                return false;
            }
        }

        return true;
    }


}
