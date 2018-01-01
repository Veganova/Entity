package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/12/2017.
 */
public class SlowEffect extends Effect {
    private double slowFactor;
    public static double percentRealRadius = 0.95;

    public static int COOLDOWN = 5;
    /**
     * joins a given shape to the applier at a given displacement from the center using a weldjoint
     *
     * @param applier
     * @param zone              uninitialized shape, unplaced (but set angles and other properties)
     * @param jointDisplacement displacement from the center point of the applier to join at
     * @param slowFactor
     * @param world
     */

    public SlowEffect(Entity applier, AShape zone, Vector2 jointDisplacement,
                      double slowFactor, MyWorld world) {
        aoeJoint(applier, zone, EffectType.SLOW, jointDisplacement, world);
        this.slowFactor = slowFactor;
        world.objectDatabase.put(this.zone.body, applier);
    }

    @Override
    public void update(MyWorld world) {
        super.update(world);

        System.out.println(status);
    }

    @Override
    public boolean apply(Entity other) {
        if (super.apply(other) && (applier.team.opposite(other.team))) {

            if(Util.getDistance(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter())
                    < this.zone.body.getRotationDiscRadius()*percentRealRadius) {
                other.resetVelocity();
                Vector2 vel = other.shape.body.getLinearVelocity();
                other.setTempVelocity(vel.x / slowFactor, vel.y / slowFactor);
//                System.out.println(other.shape.body.getLinearVelocity().x + " " + other.shape.body.getLinearVelocity().y);
            }
            else {
                other.resetVelocity();
            }

            return true;
        }
        return false;
    }

    @Override
    public int getMaxCooldown() {
        return 5 * MyWorld.FPS;
    }

    @Override
    public int getMaxActiveTime() {
        return 3 * MyWorld.FPS;
    }
}
