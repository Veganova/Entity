package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by veganova on 7/10/18.
 */

public class TurretOneBarrel extends Turret {

    public TurretOneBarrel(double x, double y, double angle, MyWorld world, Team team) {
        super(x, y, angle, world, team, 1);
    }
}
