package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Created by Veganova on 8/30/2017.
 */

public interface Aimable {

    void aim();

    Entity getPartToAimWith();

    // doesn't need an angle, use the bodies heading to shoot
    void fire();

    void changeLogicTo(AimLogic logic);

}
