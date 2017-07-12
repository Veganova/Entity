package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/12/2017.
 */
public class SlowEffect extends Effect {
    private double slowFactor;

    /**
     * joins a given shape to the applier at a given displacement from the center using a weldjoint
     *
     * @param applier
     * @param zone uninitialized shape, unplaced (but set angles and other properties)
     * @param jointDisplacement displacement from the center point of the applier to join at
     * @param slowFactor
     * @param world
     */

    public SlowEffect(Entity applier, AShape zone, Vector2 jointDisplacement,
                      double slowFactor, MyWorld world) {
        aoeJoint(applier, zone, effectType.SLOW, jointDisplacement, world);
        this.slowFactor = slowFactor;
    }

    @Override
    public void apply(Entity other) {
        if (applier.team.opposite(other.team)) {
            Vector2 vel  = other.shape.body.getLinearVelocity();
            other.shape.body.setLinearVelocity(vel.x / slowFactor, vel.y / slowFactor);
        }
    }
}
