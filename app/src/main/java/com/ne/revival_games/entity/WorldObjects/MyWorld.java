package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.WorldObject;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Veganova on 6/7/2017.
 */

public class MyWorld {

    public HashMap<Body, Entity> objectDatabase;

    /** The scale 45 pixels per meter */
    public static final double SCALE = 1.0;

    /** The conversion factor from nano to base */
    public static final double NANO_TO_BASE = 1.0e9;

    /** The canvas to draw to */
    protected Canvas canvas;

    /** The dynamics engine */
    protected World engineWorld;

    /** Whether the example is stopped or not */
    protected boolean stopped;

    /** The time stamp for the last iteration */
    protected long last;

    List<Entity> entities;
    public ObjCircle circ, circ2;
    public ObjRectangle rect;

    /**
     * default constructor for MyWorld (calls initialize engineWorld, can vary based off game type, etc.)
     * TODO: include data or list of objects or entities to be initialized
     *
     */
    public MyWorld(Canvas canvas){
//        Paint paint =  new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle((float) 50.0, (float) 50.0, (float) 30.0, paint);
        initializeWorld();
    };
    /**
     * Creates game objects and adds them to the engineWorld.
     * <p>
     * Basically the same shapes from the Shapes test in
     * the TestBed.
     */
    protected void initializeWorld() {
        // create the engineWorld
        this.objectDatabase = new HashMap<>();
        this.engineWorld = new World();
        this.engineWorld.setGravity(new Vector2(0.0, 0.0));
        System.out.println("Initialized WORLD");
        // create all your bodies/joints
        circ = new ObjCircle(-300.0, -300.0, 50.0);
        this.engineWorld.addBody(circ.body);
        rect = new ObjRectangle(0, 0, 500, 320);
        rect.rotate(15);
        circ2 = new ObjCircle(0.0, 500.0, 30.0);
        this.engineWorld.addBody(rect.body);
        this.engineWorld.addBody(circ2.body);
    }

    //need a way to add an object (check what kind of object it is, etc.)
    //alternative is to add a new object on creation in MainThread
    /**
     * The method calling the necessary methods to update
     * the game, graphics, and poll for input.
     */
    public void objectUpdate() {
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = diff / NANO_TO_BASE;
        // update the engineWorld with the elapsed time
        this.engineWorld.update(elapsedTime);
        rect.rotate(0.1);

    }

    /**
     * Cycles through entity list and draws onto canvas
     *
     * @param canvas the canvas onto which the entities will be drawn
     */
    public void drawObjects(Canvas canvas){
        circ.draw(canvas);

        rect.draw(canvas);
        if (rect.collided(circ2)) {
            Paint p = new Paint();
            p.setColor(Color.YELLOW);
            circ2.draw(canvas, p);
        } else {
            circ2.draw(canvas);
        }
    }

    public static long yuck = 0;

    public static void log(String s) {
        if (System.currentTimeMillis() - yuck >= 1000) {
            yuck = System.currentTimeMillis();
            System.out.println(s);
        }
    }
}
