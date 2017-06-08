package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

/**
 * Represents the simple shape circle class.
 */
public class Triangle extends AShape {


    Triangle (int x, int y, int s) {
        Body equTri = new Body();
        equTri.addFixture(Geometry.createEquilateralTriangle(s));
        equTri.setMass(MassType.NORMAL);
        equTri.translate(x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO: 6/7/2017
    }

}