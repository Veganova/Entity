package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Bullet;
import com.ne.revival_games.entity.WorldObjects.Entity.WorldObject;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
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
    public World engineWorld;
    /** Whether the example is stopped or not */
    protected boolean stopped;

    /** The time stamp for the last iteration */
    protected long last;

    //List<Entity> entities;

    public ObjRectangle rect, rect2;
    public ComplexShape complex;
    public Entity barrier;
    public Turret turret;
    /**
     * default constructor for MyWorld (calls initialize engineWorld, can vary based off game type, etc.)
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


        this.engineWorld.setGravity(new Vector2(0, 0));
        CollisionListener skip = new CollisionController(this);
//        this.engineWorld.addListener(skip);
        barrier = new Barrier(300, 400, 0, this);
        Entity bullet = new Bullet(0, 0, 50, 60, this);
        turret = new Turret(new Vector2(-200, 100), 30, this);
        rect = new ObjRectangle(700, 700, 20, 20, this);
//        List<AShape> objects = new ArrayList<AShape>();
//        rect2 = new ObjRectangle(50, 0, 100, 20, 0);
//        rect = new ObjRectangle(-10, -10, 10, 10, this);
//        objects.add(rect2);
//        objects.add(new ObjCircle(0, 0, 50, 0));
//        rect2.rotateFixture(2*Math.PI/3, new Vector2(0, 0));
//        System.out.println("x,y:" + rect2.getShape().getCenter());
//        complex = new ComplexShape(objects, 300, 420, this);

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
        turret.aim(rect.body);
        this.engineWorld.update(elapsedTime);

        for (Entity entity : objectDatabase.values()) {
            entity.update(this);
        }


    }

    /**
     * Cycles through entity list and draws onto canvas
     *
     * @param canvas the canvas onto which the entities will be drawn
     */
    public void drawObjects(Canvas canvas){
        //this might draw multiple of the same entities
        for (Entity entity : objectDatabase.values()) {
            entity.draw(canvas);
        }
//        complex.draw(canvas);
        rect.draw(canvas);
//        Entity ent1 = (Entity)objectDatabase.values().toArray()[0];
//        Entity ent2 = (Entity)objectDatabase.values().toArray()[1];
//        System.out.println(ent1.shape.collided(ent2.shape));
    }

}
