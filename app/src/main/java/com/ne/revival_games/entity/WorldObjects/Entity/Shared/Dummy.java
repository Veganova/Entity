package com.ne.revival_games.entity.WorldObjects.Entity.Shared;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.Effect;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.EffectType;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/27/2017.
 */

public class Dummy extends ConditionalDestructible {
    private EffectType effectType;

    public Dummy(Vector2 location, Effect myEffect, MyWorld world, Team team) {
        super(0, 0, 1, true, team);
        this.shape = new ObjCircle(5);
        this.shape.getBuilder(true, world).setXY(location.x, location.y).init();
        world.objectDatabase.put(this.shape.body, this);
        this.isCollisionAuthority = true;
        this.effectType = myEffect.effectType;
        this.addEffect(myEffect);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit,  double damage){
//        super.onCollision(contact, componentHit, 0);

        Effect activeEffect = zoneToEffect.get(componentHit);
        if (activeEffect != null) {
            activeEffect.apply(contact);
        }

        return false;
    }


    @Override
    protected boolean deathCondition() {
        return (this.effects.get(effectType) == null);
    }

    @Override
    public void draw(Canvas canvas) {
        for(Effect effect : effects.values()) {
            effect.draw(canvas);
        }
    }
}
