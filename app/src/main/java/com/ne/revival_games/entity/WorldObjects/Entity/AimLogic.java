package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;

/**
 * Created by Veganova on 6/29/2017.
 */
public interface AimLogic {

    public void aim(Aimable mainBarrel);

    public void choose();

}
