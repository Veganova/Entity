package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;
import android.graphics.Color;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 * Created by Veganova on 6/7/2017.
 */

public class MyWorld {

    /** The scale 45 pixels per meter */
    public static final double SCALE = 1.0;

    /** The conversion factor from nano to base */
    public static final double NANO_TO_BASE = 1.0e9;

    /** The canvas to draw to */
    protected Canvas canvas;

    /** The dynamics engine */
    protected World world;

    /** Wether the example is stopped or not */
    protected boolean stopped;

    /** The time stamp for the last iteration */
    protected long last;

    List<Entity> entities;


    // TODO: 6/8/2017 addEntity(), constructors
    /**
     * Creates game objects and adds them to the world.
     * <p>
     * Basically the same shapes from the Shapes test in
     * the TestBed.
     */
    protected void initializeWorld() {
        // create the world
        this.world = new World();

        // create all your bodies/joints

        // create the floor
        Rectangle floorRect = new Rectangle(15.0, 1.0);
        Body floor = new Body();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);
        // move the floor down a bit
        floor.translate(-5.0, -4.0);
        this.world.addBody(floor);

        // create a triangle object
        Triangle triShape = new Triangle(
                new Vector2(0.0, 0.5),
                new Vector2(-0.5, -0.5),
                new Vector2(0.5, -0.5));
        Body triangle = new Body();
        triangle.addFixture(triShape);
        triangle.setMass(MassType.NORMAL);
        triangle.translate(-1.0, 2.0);
        // test having a velocity
        triangle.getLinearVelocity().set(5.0, 0.0);
        this.world.addBody(triangle);

        // create a circle
        Circle cirShape = new Circle(0.05);
        Body circle = new Body();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(-8.0, 2.0);
        // test adding some force
        //circle.applyForce(new Vector2(-100.0, 1.0));
        // set some linear damping to simulate rolling friction
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);
    }

    /**
     * The method calling the necessary methods to update
     * the game, graphics, and poll for input.
     */
    protected void objectUpdate() {
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = diff / NANO_TO_BASE;
        // update the world with the elapsed time
        this.world.update(elapsedTime);
    }
}
