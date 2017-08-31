package com.ne.revival_games.entity.WorldObjects.Entity.Aim;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by Veganova on 8/30/2017.
 */

public interface Shootable {

    void fire();

    void setShootingSpeed(double speed);

    double getShootingDistance();

    Entity getNewBodyToShoot(double x, double y, double angle);
}
