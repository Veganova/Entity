package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by Veganova on 6/10/2017.
 */

public class Missile extends Projectile {

    public static double SPEED = 30.0;
    public static int HEALTH = 30;
    public static int RADIUS = 10;

    public Missile(double x, double y, double direction, double speed, MyWorld world) {
        super(x, y, RADIUS, direction, speed, HEALTH, world);
    }

}
