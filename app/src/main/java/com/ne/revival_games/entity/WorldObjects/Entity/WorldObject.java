package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Canvas;

/**
 * Interface that represents all world objects
 */
public interface WorldObject {

    /**
     * Updates the world object using its state values.
     */
    void update();

    /**
     * Draw this world object onto the given canvas.
     *
     * @param canvas the canvas onto which the image will be drawn
     */
    void draw(Canvas canvas);

}
