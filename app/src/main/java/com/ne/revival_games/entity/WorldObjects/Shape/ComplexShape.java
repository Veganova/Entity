package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Shape.Shape;

import java.util.List;

/**
 * Represents a complex shape that is made of other shapes.
 */
public class ComplexShape implements Shape {

    List<Shape> subparts;

    // TODO: 6/7/2017 how to store the relative manner in which the shapes are stored
    @Override
    public void draw(Canvas canvas, int curX, int curY, int offsetX, int offsetY) {
        for (Shape shape: this.subparts) {
            shape.draw(canvas, curX, curY, offsetX, offsetY);
        }
    }

    @Override
    public boolean collided(Shape other) {
        // TODO: 6/7/2017
        return false;
    }
}
