package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Interface that represents all engineWorld objects
 */
public interface WorldObject {

//    /**
//     * Updates the engineWorld object using its state values.
//     */
//    void update(MyWorld world);

    /**
     * Draw this engineWorld object onto the given canvas.
     *
     * @param canvas the canvas onto which the image will be drawn
     */
    void draw(Canvas canvas);

    void setVelocity(double speed);

    void onDeath();

    /**
     *
     * @param contact
     * @param damage
     * @return true if collision should continue, false if collision should end
     */
    boolean onCollision(Entity contact, double damage);

}
