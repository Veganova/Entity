package com.ne.revival_games.entity.WorldObjects.Players;

import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;

/**
 * Created by veganova on 8/20/18.
 */

public class AddToWorld {
    private double x, y;
    private EntityLeaf type;

    public AddToWorld(EntityLeaf type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public EntityLeaf getType() {
        return type;
    }
}
