package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by veganova on 8/5/18.
 */

public class TurretThreeBarrel extends Turret {

    public TurretThreeBarrel(double x, double y, double angle, MyWorld world, Team team) {
        super(x, y, angle, world, team, 3);
    }
}
