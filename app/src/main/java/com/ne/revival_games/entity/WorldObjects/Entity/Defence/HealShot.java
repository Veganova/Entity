package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;

/**
 * Created by vishn on 1/9/2018.
 */

public class HealShot extends Missile {
    //TODO: "heal_missile"

    public HealShot(double x, double y, double angle, double speed, MyWorld world, Team team, Entity parent) {
        super(x, y, angle, speed, world, team, parent);
    }

    @Override
    public boolean onCollision(Entity contact, Body componentHit, double damage) {
        if(contact.team.opposite(this.team)) {
            return super.onCollision(contact, componentHit, damage);
        }
        else {
            contact.applyDamage(-1*this.getDamage(componentHit), this);
            this.dead = true;
            return false;
        }
    }
}
