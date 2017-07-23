package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;


import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

/**
 * Created by vishn on 7/23/2017.
 */

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/12/2017.
 */

public class ExplosiveEffect extends Effect{
    private double explosivePower;

    public ExplosiveEffect(Entity applier, AShape zone, double explosivePower, MyWorld world){
        basicInit(applier, zone, effectType.EXPLOSIVE);
        this.explosivePower = explosivePower;
    }


    @Override
    public void apply(Entity other) {
        if (!canApply(other)) {
            return;
        }

        double distance =
                Util.getDistance(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter());

        double angle =
                Util.absoluteAngle(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter());

        double magnitude = explosivePower / (distance) / other.shape.body.getMass().getMass();


        other.shape.body.applyForce(new Vector2(-1*magnitude*Math.cos(angle),
                -1*magnitude*Math.max(magnitude, 0) * Math.sin(angle)));

    }

}
