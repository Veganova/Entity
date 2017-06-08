package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Shape.Shape;

import org.dyn4j.dynamics.Body;

import java.util.List;

/**
 * Represents a complex shape that is made of other shapes.
 */
public class ComplexShape extends AShape {

    //Given body is a composition of many Bodies..
    ComplexShape(Body body) {
        this.body = body;
    }

    // TODO: 6/7/2017 how to store the relative manner in which the shapes are stored
    @Override
    public void draw(Canvas canvas) {
//        for (Shape shape: this.body.parts) {
//            shape.draw(canvas, curX, curY, offsetX, offsetY);
//        }
    }

}
