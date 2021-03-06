package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;
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
    protected boolean active = false;
    protected double explosionPower = 0;
    protected double startTime;

    public static int COOLDOWN = 0;

    public ExpandingEffect(Entity applier, Vector2 location, double percentStartSize, double growth_rate, double damage,
                           double max_radius, double explosionPower, Team team, MyWorld world, EffectType type){
        basicInit(applier, zone, type, world);
        this.active = true;
        this.damage = damage;
        this.max_radius = max_radius;
        this.percent_size = percentStartSize;
        this.growth_rate = growth_rate;
        this.explosionPower = explosionPower;
        this.zone = new ObjCircle(max_radius);
        this.zone.getBuilder(true, world).setXY(location.x, location.y).init();
        this.damage = damage;
        world.objectDatabase.put(this.zone.body, this.applier);
        this.startTime = System.currentTimeMillis();
    }

    public ExpandingEffect(double percentStartSize, double growth_rate, double damage,
                           double max_radius, double explosionPower, Team team, MyWorld world, EffectType type){
        basicInit(null, zone, type, world);
        this.active = false;
        this.damage = damage;
        this.max_radius = max_radius;
        this.percent_size = percentStartSize;
        this.growth_rate = growth_rate;
        this.explosionPower = explosionPower;
        this.zone = new ObjCircle(max_radius);
    }

    public ExpandingEffect(Team team, MyWorld world, EffectType type, Entity parent) {
        basicInit(null, zone, type, world, parent);
        this.active = false;
        this.percent_size = MySettings.getEntityNum(team.toString(), new Query(this.getName(), "percent_start"), true);
        this.growth_rate = MySettings.getEntityNum(team.toString(), new Query(this.getName(),"growth_rate"), true);
        this.damage = MySettings.getEntityNum(team.toString(), new Query(this.getName(),"damage"), true);
        this.max_radius = MySettings.getEntityNum(team.toString(), new Query(this.getName(),"max_radius"), true);
        this.explosionPower = MySettings.getEntityNum(team.toString(), new Query(this.getName(),"push_back"), true);
        this.zone = new ObjCircle(max_radius);
    }

    public void addToWorld(Vector2 location, Entity applier, MyWorld world) {
        this.active = true;
        this.applier = applier;
        this.zone.getBuilder(true, world).setXY(location.x, location.y).init();
        this.applier.zoneToEffect.put(this.zone.body, this);
        world.objectDatabase.put(this.zone.body, this.applier);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean apply(Entity other) {
        if (super.apply(other)) {

            if (inRange(other)) {
                applyEffect(other);
            }
            return true;
        }
        return false;

    }

    public abstract boolean inRange(Entity other);
    public abstract void applyEffect(Entity other);


    public void draw(Canvas canvas) {
        if(!active){
            return;
        }
        ((ObjCircle) this.zone).draw(canvas, this.percent_size*max_radius);
    }

    @Override
    public void update(MyWorld world){
        super.update(world);
        if(!active){
            return;
        }

        this.percent_size += growth_rate;

        if(percent_size >= 1) {
            this.applier.removeEffect(this);
        }
    }

}
