package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Projectile;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by Veganova on 6/10/2017.
 */

public class Bullet extends Projectile {

    public static double MIN_SPEED = 1500;

    public Bullet(double x, double y, int r, double direction, MyWorld world) {
        super(x, y, Math.min(r, (int)MAX_RADIUS), direction, getSpeed(r), getHealth(r), world);
    }

    private static double getSpeed(int r) {
        return Math.pow(Math.E, -r) + MIN_SPEED;
    }

    private static int getHealth(int r) {
        return (int)Math.PI * r * r;
    }
}
