package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExplosiveEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/23/2017.
 */

public class ExpandingCircle extends Entity{
    private double percent_size = 0;
    private double growth_rate;
    private double max_radius;
    private double damage;
    private double explosionPower = 0;

    public ExpandingCircle(Vector2 location, double percentStartSize, double growth_rate, double damage,
                           double max_radius, double explosionPower, Team team, MyWorld world){
        super(0, 0, 0, true, team);
        this.damage = damage;
        this.max_radius = max_radius;
        this.percent_size = percentStartSize;
        this.growth_rate = growth_rate;
        this.explosionPower = explosionPower;
        this.isCollisionAuthority = true;
        this.shape = new ObjCircle(max_radius);
        this.shape.getBuilder(true, world).setXY(location.x, location.y).init();
        this.addEffect(new ExplosiveEffect(this, this.shape, explosionPower, world));
        world.objectDatabase.put(this.shape.body, this);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage){
        if(Util.getDistance(contact.shape.body.getWorldCenter(), this.shape.body.getWorldCenter())
                < (this.percent_size * max_radius / MyWorld.SCALE)){
            super.onCollision(contact, componentHit, this.health);
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        ((ObjCircle)this.shape).draw(canvas, (max_radius*percent_size));
    }

    @Override
    public void update(MyWorld world){
        this.percent_size += growth_rate;
        if(percent_size >= 1) {
            world.bodiestodelete.add(this.shape.body);
        }
    }

}
