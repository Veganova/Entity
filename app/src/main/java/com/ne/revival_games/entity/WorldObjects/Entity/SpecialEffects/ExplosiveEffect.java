package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.collision.narrowphase.Gjk;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/23/2017.
 */

public class ExplosiveEffect extends ExpandingEffect {
    public boolean primed = false;

    /**
     *
     * @param applier
     * @param location
     * @param percentStartSize
     * @param growth_rate
     * @param damage
     * @param max_radius
     * @param explosionPower
     * @param team
     * @param world
     */

    public ExplosiveEffect(Entity applier, Vector2 location, double percentStartSize,
                           double growth_rate, double damage, double max_radius, double explosionPower, Team team, MyWorld world) {
        super(applier, location, percentStartSize, growth_rate, damage, max_radius, explosionPower, team, world, EffectType.EXPLOSIVE);
        this.active = true;
    }

    public ExplosiveEffect(double percentStartSize,
                           double growth_rate, double damage, double max_radius, double explosionPower, Team team,
                           MyWorld world) {
        super(percentStartSize, growth_rate, damage, max_radius, explosionPower, team, world, EffectType.EXPLOSIVE);
        this.active = false;
    }

    public ExplosiveEffect(String name, Team team, MyWorld world) {
        super(name, team, world, EffectType.EXPLOSIVE);
        this.active = false;
    }



    //this needs to corrected somehow
    @Override
    public boolean inRange(Entity other){
        //is this optimizing or not?
        double current_radius = max_radius/MyWorld.SCALE*percent_size;
            Gjk detector = new Gjk();
            for(Convex convex : other.shape.myConvexes){
                if(detector.detect(convex, other.shape.body.getTransform(),
                        new Circle(current_radius), this.zone.body.getTransform())){
                    return true;
                }
            }

            return false;

    }

    @Override
    public void applyEffect(Entity other){
        double distance =
                Util.getDistance(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter());

        double angle =
                Util.absoluteAngle(other.shape.body.getWorldCenter(), this.zone.body.getWorldCenter());

        double magnitude = explosionPower / (distance) / other.shape.body.getMass().getMass();

        other.applyDamage(damage, other);

        other.shape.body.applyForce(new Vector2(-1*magnitude*Math.cos(angle),
                -1*magnitude*Math.max(magnitude, 0) * Math.sin(angle)));

    }



}
