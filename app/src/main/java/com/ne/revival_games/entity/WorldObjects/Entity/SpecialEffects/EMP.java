package com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.collision.narrowphase.Gjk;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/23/2017.
 */

public class EMP extends ExpandingEffect {
    private double pulse_width = 50;
    private double duration = 2000;

    public EMP(Entity applier, Vector2 location, double percentStartSize,
               double growth_rate, double damage, double max_radius, double explosionPower, Team team,
               MyWorld world, double duration) {
        super(applier, location, percentStartSize, growth_rate, damage, max_radius, explosionPower, team, world, EffectType.SHOCKWAVE);
        this.duration = duration;
    }

    public EMP(double percentStartSize,
               double growth_rate, double damage, double max_radius, double explosionPower, Team team,
               MyWorld world, double duration) {
        super(percentStartSize, growth_rate, damage, max_radius, explosionPower, team, world, EffectType.SHOCKWAVE);
        this.duration = duration;
    }

    public EMP(String name, Team team, MyWorld world) {
        super(name + " emp_effect", team, world, EffectType.SHOCKWAVE);
        this.duration = MySettings.getNum(team.toString(), name + " emp_effect duration");
    }

    @Override
    public boolean inRange(Entity other) {
        //TODO: will break for complex shapes / look crappy since we use shape.convex
        double current_radius1 = max_radius/MyWorld.SCALE * percent_size;
        double current_radius2 = (max_radius - pulse_width) / MyWorld.SCALE * percent_size;

        Gjk detector = new Gjk();
        //iterates over individual shapes in complex shape
        if(other.shape instanceof ComplexShape) {
            boolean inrange = false;
            for(AShape shape : ((ComplexShape) other.shape).shapes) {
                if(detector.detect(shape.convex, shape.body.getTransform(), new Circle(current_radius1),
                        this.zone.body.getTransform())
                        && !detector.detect(shape.convex, shape.body.getTransform(),
                        new Circle(current_radius2), this.zone.body.getTransform())) {
                    return true;
                }
            }

            return false;
        }
        else  {
            //checks if within the two circles
            return detector.detect(other.shape.convex, other.shape.body.getTransform(), new Circle(current_radius1),
                    this.zone.body.getTransform())
                    && !detector.detect(other.shape.convex, other.shape.body.getTransform(),
                    new Circle(current_radius2), this.zone.body.getTransform());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.zone.setPaint(Paint.Style.STROKE);
        ((ObjCircle) this.zone).draw(canvas, this.percent_size*max_radius);
    }

    @Override
    public double getMaxCooldown() {
        return 0;
    }

    @Override
    public double getMaxActiveTime() {
        return 0;
    }

    @Override
    protected double getStartupCost() {
        return 0;
    }

    @Override
    public void applyEffect(Entity other) {
        other.isActive = false;
        //TODO: this should apply a 'pause effect'
        other.isDisabledUntil = System.currentTimeMillis() + duration;
    }
}
