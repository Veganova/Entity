package com.ne.revival_games.entity.WorldObjects.Entity;

import android.os.Looper;

/**
 * Created by Veganova on 9/8/2017.
 */

public class IntAnimator {


    private final double delta;
    private double lastHealth;
    private int targetHealth;

    public IntAnimator(int lastHealth, int targetHealth, double seconds) {
        this.lastHealth = lastHealth;
        this.targetHealth = targetHealth;
        // fps
        this.delta = (targetHealth - lastHealth) / (40 * seconds);
    }

    public int update() {
        if ( (this.lastHealth - this.targetHealth) < 0.01) {
            return targetHealth;
        }
        this.lastHealth += delta;
        return (int)lastHealth;
    }
}
