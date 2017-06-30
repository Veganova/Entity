package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.Shape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public interface Aimable {

    void aim(Body body);

    void fire(double angle);

    Vector2 getCenter();

    void changeLogicTo(AimLogic logic);
}
