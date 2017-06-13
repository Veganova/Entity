package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 * Created by vishn on 6/11/2017.
 */

public class ComplexShape extends AShape {

    List<AShape> shapes;

    public ComplexShape(List<AShape> fixtures, double x, double y, MyWorld world){
//        //might be better to pass in some kind of data object and then parse it ex. json object
        this.body = new Body();
        for(int z=0; z<fixtures.size(); z++) {
            fixtures.get(z).body = this.body;
            body.addFixture(fixtures.get(z).getShape());
////            fixtures.get(z).translateFixture(fixtures.get(z).getX(), fixtures.get(z).getY());
        }
        shapes = fixtures;
//
        initValues(this.body, x, y, world);
    }

    @Override
    public void draw(Canvas canvas) {
        for(int x=0; x<shapes.size(); x++){
            shapes.get(x).draw(canvas);
        }
    }

    @Override
    public void rotateFixture(double angle, Vector2 location){
      shapes.get(0).rotateFixture(angle, location);
    }

    @Override
    public void translateFixture(double x, double y){
        shapes.get(0).translateFixture(x, y);
    }

    public void rotateFixture(int index, double angle, Vector2 location){
        shapes.get(index).rotateFixture(angle, location);
    }

    public void translateFixture(int index, Vector2 location){
        shapes.get(index).translateFixture(location.x, location.y);
    }
}
