package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

/**
 * Represents the simple shape equilateral triangle
 */
public class Triangle extends AShape {

    private double s;
    
    Triangle (int x, int y, int s) {
        initValues(Geometry.createEquilateralTriangle(s), x, y);
        this.s = s;
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO: 6/9/2017  
    }

    @Override
    public void rotate(double degrees) {

    }

}