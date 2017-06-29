package com.ne.revival_games.entity.WorldObjects.Entity;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 6/29/2017.
 */

public interface Aimable {

    void aim(Body body);

    void fire(double angle);
}
