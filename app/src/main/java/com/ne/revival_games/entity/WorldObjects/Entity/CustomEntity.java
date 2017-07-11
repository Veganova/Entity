package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Color;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

/**
 * Created by Veganova on 7/9/2017.
 */

public class CustomEntity extends Entity {

    public CustomEntity(AShape shape, double speed,
                        int health, boolean invulnerable, Team team, MyWorld world) {
        super(0, speed, health, invulnerable, team);
        this.shape = shape;
        this.shape.setColor(Color.MAGENTA);
        world.objectDatabase.put(this.shape.body, this);
    }
}
