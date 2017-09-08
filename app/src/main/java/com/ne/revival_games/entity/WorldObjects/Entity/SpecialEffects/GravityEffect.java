package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/12/2017.
 */

public class GravityEffect extends Effect{
    private double gravityValue;

    /**
     * joins a given shape to the applier at a given displacement from the center using a weldjoint
     *
     * @param applier
     * @param zone uninitialized shape, unplaced (but set angles and other properties)
     * @param jointDisplacement displacement from the center point of the applier to join at
     * @param gravityValue
     * @param world
     */
    public GravityEffect(Entity applier, AShape zone, double gravityValue,
                         Vector2 jointDisplacement, MyWorld world){
        aoeJoint(applier, zone, effectType.GRAVITY, jointDisplacement, world);
        this.gravityValue = gravityValue;
    }

    /**
     * joins a given shape to the applier at a given displacement from the center using a weldjoint
     *
     * @param applier
     * @param radius
     * @param jointDisplacement displacement from the center point of the applier to join at
     * @param gravityValue
     * @param world
     */
    public GravityEffect(Entity applier, double radius, double gravityValue,
                         Vector2 jointDisplacement, MyWorld world) {
        aoeJoint(applier, new ObjCircle(radius), effectType.GRAVITY, jointDisplacement, world);
        this.gravityValue = gravityValue;
        world.objectDatabase.put(this.zone.body, applier);
    }

    @Override
    public boolean apply(Entity other) {
//        if (!canApply(other)) {
//            return;
//        }
        if (super.apply(other)) {

            double distance =
                    Util.getDistance(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter());

            double angle =
                    Util.absoluteAngle(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter());

            double magnitude = gravityValue
                    * this.zone.body.getMass().getMass()
                    * other.shape.body.getMass().getMass()
                    / (Math.pow(distance, 2));


            if (Util.nearValue(other.shape.body.getLinearVelocity().x, 0, 0.001)
                    && Util.nearValue(other.shape.body.getLinearVelocity().y, 0, 0.001)) {
                if (this.applier.shape.body.getMass().getMass() * MyWorld.staticFriction > magnitude) {
                    return false;
                }
                magnitude -= this.applier.shape.body.getMass().getMass() * other.frictionCoefficent * MyWorld.staticFriction;
            } else {
                magnitude -= this.applier.shape.body.getMass().getMass() * other.frictionCoefficent * MyWorld.kineticFriction;
            }

            magnitude = Math.max(magnitude, 0) * 0.01;


            other.shape.body.applyForce(new Vector2(magnitude * Math.cos(angle),
                    magnitude * Math.max(magnitude, 0) * Math.sin(angle)));
            return true;
        }
        return false;

    }

}
