package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.ConditionalDestructible;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.GravityEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.SlowEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Untargetable;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/2/2017.
 */

public class Lazer extends ConditionalDestructible {
    public double damage;
    public double lazer_width = 0;         //unscaled width
    public double lazer_angle = 0;              //angle in RADIANS
    public double lazer_length = 0;
    private double lifeTime, start;
    private MyWorld world;


    public Lazer(double x, double y, double angle, MyWorld world, Team team, String tag) {
        super(angle, 0, team, tag + "lazer");
        this.world = world;
        this.invulnerable = true;
        this.isCollisionAuthority = true;
        this.lazer_length = MySettings.getNum(team.toString(), name_tag + " lazer length");
        this.lazer_width = MySettings.getNum(team.toString(), name_tag + " lazer width");
        this.start = System.currentTimeMillis();
        this.lifeTime = MySettings.getNum(team.toString(), name_tag + " lazer lifetime");
        this.damage = MySettings.getNum(team.toString(), name_tag + " lazer damage");
        this.lazer_angle = Math.toRadians(angle);
        Vector2 newPoint = new Vector2(x + lazer_length*Math.cos(lazer_angle),
                y + lazer_length*Math.sin(lazer_angle));

        double mid_x = x + lazer_length*Math.cos(lazer_angle) / 2;
        double mid_y = y + lazer_length*Math.sin(lazer_angle)/ 2;
        this.shape = new ObjRectangle(lazer_length, lazer_width);
        this.shape.getBuilder(true, this.world).setXY(x + lazer_length*Math.cos(lazer_angle) / 2,
                y + lazer_length*Math.sin(lazer_angle)/ 2).init();
        this.shape.rotateBody(lazer_angle);

        this.setVelocity(MySettings.getNum(team.toString(), name_tag + " lazer speed"));
        world.objectDatabase.put(this.shape.body, this);
        this.targetExceptions.addType(GravityEffect.class, Untargetable.FROM.ALL);
        this.targetExceptions.addType(SlowEffect.class, Untargetable.FROM.ALL);
        this.targetExceptions.addType(ExplosiveEffect.class, Untargetable.FROM.ALL);
    }

//      /**
//     * function used to place a rectangular shaped "lazer" object
//     * also change this.shape to newly generated body
//     *
//     * WARNING: object has not been placed in world object database & must be used after this.world
//     * is initialized
//     */
//    public void placeLazer(Vector2 oldPoint, Vector2 newPoint){
//        double x = (oldPoint.x + newPoint.x) / 2;
//        double y = (oldPoint.y + newPoint.y) / 2;
//        this.shape = new ObjRectangle(lazer_length, lazer_width);
//        this.shape.getBuilder(true, this.world).setXY(x,y).init();
//        this.shape.rotateBody(lazer_angle);
//    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        super.onCollision(contact, componentHit, damage);

        return false;
    }


    @Override
    protected boolean deathCondition() {
        return  (lifeTime != -1 && startTime + lifeTime < System.currentTimeMillis());
    }
}
