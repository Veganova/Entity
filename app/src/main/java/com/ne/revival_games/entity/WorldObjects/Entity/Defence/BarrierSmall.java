package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by veganova on 7/31/18.
 */

public class BarrierSmall extends Barrier {

    /**
     * Instantiates a smaller barrier. The regular super constructor will end up using BarrierSmall's settings.json values.
     */
    public BarrierSmall(double x, double y, double angle, MyWorld world, Team team) {
        super(x, y, angle, world, team);
    }
}
