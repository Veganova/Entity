package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public interface Aimable {

    void aim();

    void fire(double angle);

    Vector2 getCenter();

    void changeLogicTo(AimLogic logic);

    boolean isSleeping();
}
