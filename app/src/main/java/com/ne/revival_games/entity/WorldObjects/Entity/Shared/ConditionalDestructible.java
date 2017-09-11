package com.ne.revival_games.entity.WorldObjects.Entity.Shared;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

/**
 * Created by vishn on 7/27/2017.
 */

public abstract class ConditionalDestructible extends Entity {
    protected boolean naturalDeath = false;
    protected boolean primed = false;
    protected double startTime = -1;

    public ConditionalDestructible(double direction, double speed, int health, boolean invulnerable, Team team) {
        super(direction, speed, health, invulnerable, team);
    }

    @Override
    public boolean update(MyWorld world){
        
        if(deathCondition() && this.primed) {
            this.invisible = true;
            world.objectDatabase.remove(this.shape.body);
        }
        else {
            super.update(world);
        }

        return true;
    }

    @Override
    public void prime(){
        this.primed = true;
        this.startTime = System.currentTimeMillis();
    }

    protected abstract boolean deathCondition();


}
