package com.ne.revival_games.entity.WorldObjects.Entity.Shared;

import android.graphics.Color;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

/**
 * Created by Veganova on 7/9/2017.
 */
public class CustomEntity extends Entity {

    public CustomEntity(AShape shape, double speed,
                        int health, boolean invulnerable, Team team, MyWorld world) {
        super(0, speed, health, invulnerable, team);
        this.shape = shape;
//        this.shape.setColor(Color.MAGENTA);
        world.objectDatabase.put(this.shape.body, this);
    }

    public CustomEntity(AShape shape, double speed,
                        int health, boolean invulnerable, Player player, MyWorld world) {
        super(0, speed, health, invulnerable, player.team);
        this.shape = shape;
        world.objectDatabase.put(this.shape.body, this, player);
    }

}
