package com.ne.revival_games.entity.WorldObjects.Shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vishn on 6/11/2017.
 */

public class ComplexShape extends AShape {

    List<AShape> shapes;
    boolean isComplexBody;

    /**
     * constructor for a fixture-based complex body
     *
     * @param fixtures list of fixture, body-less shapes passed in a a list
     * @param x x-coordinate of center location of the body
     * @param y y-coordinate of the center location of the body
     * @param world
     */
    public ComplexShape(List<AShape> fixtures, double x, double y, MyWorld world){
       //might be better to pass in some kind of data object and then parse it ex. json object
        this.body = new Body();
        for(int z=0; z<fixtures.size(); z++) {
            fixtures.get(z).body = this.body;
            body.addFixture(fixtures.get(z).getShape());
        }

        for(BodyFixture fixture: this.body.getFixtures()){
            HashMap<String, Double> values = (HashMap<String, Double>)fixture.getShape().getUserData();

            fixture.setRestitution(values.get("restitution"));
            fixture.setFriction(values.get("friction"));
            fixture.setDensity(values.get("density"));
        }

        shapes = fixtures;
        this.getBuilder(world, body).setXY(x, y).init();
        isComplexBody = false;
    }


    /**
     * constructor for a complex body which must be created externally to the function using joints
     *
     * @param jointBodies list of shapes that are already joined in the appropriate fashion
     */
    public ComplexShape(List<AShape> jointBodies){
        //might be better to pass in some kind of data object and then parse it ex. json object
        shapes = jointBodies;
        isComplexBody = true;
    }

    @Override
    public void draw(Canvas canvas) {
        for(int x=0; x<shapes.size(); x++){
            shapes.get(x).draw(canvas);
        }
    }

    @Override
    public void setPaint(Paint.Style style) {
        for (Shape shape: this.shapes) {
            shape.setPaint(style);
        }
    }

    @Override
    public void setColor(int color) {
        for (Shape shape: this.shapes) {
            shape.setColor(color);
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

    public void rotateAllBodies(double angle){
        if(isComplexBody){
            for (int x = 0; x < shapes.size(); x++){
                shapes.get(x).rotateBody(angle);
            }
            return;
        }
        System.out.println("Warning-- this shape is not a complex body");
    }

    @Override
    public double getOrientation(){

        return 0;
    }
}
