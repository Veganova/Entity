package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 6/29/2017.
 */
public interface AimLogic {
    // needs to have a shootable!

    void aim(Entity aimWith);

    void choose();

}
