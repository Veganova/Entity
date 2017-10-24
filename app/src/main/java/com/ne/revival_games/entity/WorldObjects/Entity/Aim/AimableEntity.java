package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 8/29/2017.
 */

public abstract class AimableEntity extends Entity implements Aimable {

    protected AimLogic logic;
    protected boolean aiming = true;

    public AimableEntity(double direction, double speed, int health, boolean invulnerable, Team team) {
        super(direction, speed, health, invulnerable, team, DEFAULT_FRICTION);
    }
//
//    public AimableEntity(double direction, double speed, int health, boolean invulnerable, Team team, AimLogic logic) {
//        super(direction, speed, health, invulnerable, team);
//        this.logic = logic;
//    }

    @Override
    public boolean update(MyWorld world) {
        if(super.update(world) && this.logic != null) {
            this.aim();
            return true;
        }

        return false;
    }

    @OverridingMethodsMustInvokeSuper
    public void aim() {
        if (aiming) {
            this.logic.aim(this.getPartToAimWith());
        }
    }

    public abstract Entity getPartToAimWith();

    // doesn't need an angle, use the bodies heading to shoot
    public abstract void fire();

    public void changeLogicTo(AimLogic logic) {
        this.logic = logic;
    }

    public abstract int getTurnSpeed();
}
