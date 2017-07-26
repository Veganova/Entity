package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.geometry.Vector2;


/**
 * Created by vishn on 7/23/2017.
 */

public abstract class ExpandingEffect extends Effect {
    protected double percent_size = 0;
    protected double growth_rate;
    protected double max_radius;
    protected double damage;
    protected double explosionPower = 0;

    public ExpandingEffect(Entity applier, Vector2 location, double percentStartSize, double growth_rate, double damage,
                           double max_radius, double explosionPower, Team team, MyWorld world, EffectType type){
        basicInit(applier, zone, type, world);
        this.damage = damage;
        this.max_radius = max_radius;
        this.percent_size = percentStartSize;
        this.growth_rate = growth_rate;
        this.explosionPower = explosionPower;
        this.zone = new ObjCircle(max_radius);
        this.zone.getBuilder(true, world).setXY(location.x, location.y).init();
        this.damage = damage;
//        this.addEffect(new ExplosiveEffect(this, this.shape, explosionPower, world));
        world.objectDatabase.put(this.zone.body, this.applier);
    }

    @Override
    public void apply(Entity other) {
        if (!canApply(other)) {
            return;
        }

        if(inRange(other)){
            System.out.println("APPLYING!");
           applyEffect(other);
        }

    }

    public abstract boolean inRange(Entity other);
    public abstract void applyEffect(Entity other);


    public void draw(Canvas canvas) {
        ((ObjCircle) this.zone).draw(canvas, this.percent_size*max_radius);
    }

    @Override
    public void update(MyWorld world){
        this.percent_size += growth_rate;
        if(percent_size >= 1) {
            this.applier.removeEffect(this);
        }
    }

}
