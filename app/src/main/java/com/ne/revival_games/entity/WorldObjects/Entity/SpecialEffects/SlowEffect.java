package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 7/12/2017.
 */
public class SlowEffect implements Effect {

    private Entity applier;
    private double slowFactor;

    public SlowEffect(Entity applier, double slowFactor) {
        this.applier = applier;
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
