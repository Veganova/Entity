package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Bullet;
import com.ne.revival_games.entity.WorldObjects.Entity.WorldObject;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Rectangle;
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
    public World engineWorld;
    /** Whether the example is stopped or not */
    protected boolean stopped;

    /** The time stamp for the last iteration */
    protected long last;

    //List<Entity> entities;

    public ObjRectangle rect;
    public Entity barrier;
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
        barrier = new Barrier(300, 400, 0, this);
        Entity bullet = new Bullet(0, 0, 50, 60, this);
        rect = new ObjRectangle(700, 700, 800, 20, this);

        //this.engineWorld.addListener(new SkipDeadObjects(this));
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
        for (Entity entity : objectDatabase.values()) {
            entity.draw(canvas);
        }
        rect.draw(canvas);
//        Entity ent1 = (Entity)objectDatabase.values().toArray()[0];
//        Entity ent2 = (Entity)objectDatabase.values().toArray()[1];
//        System.out.println(ent1.shape.collided(ent2.shape));
    }

}

class SkipDeadObjects implements CollisionListener {

    MyWorld world;

    SkipDeadObjects(MyWorld world) {
        this.world = world;
    }

    @Override
    public boolean collision(Body body, BodyFixture bodyFixture,
                             Body body1, BodyFixture bodyFixture1) {

        Entity ent1 = world.objectDatabase.get(body);
        Entity ent2 = world.objectDatabase.get(body1);
        System.out.println();
        System.out.println(ent1 + " " + ent1.health);
        System.out.println(ent2 + " " + ent2.health);
        return (ent1.health <= 0 || ent2.health <= 0);
    }

    @Override
    public boolean collision(Body body, BodyFixture bodyFixture, Body body1,
                             BodyFixture bodyFixture1, Penetration penetration) {
        return this.collision(body, bodyFixture, body1, bodyFixture1);
    }

    @Override
    public boolean collision(Body body, BodyFixture bodyFixture, Body body1,
                             BodyFixture bodyFixture1, Manifold manifold) {
        return this.collision(body, bodyFixture, body1, bodyFixture1);
    }

    @Override
    public boolean collision(ContactConstraint contactConstraint) {
        return true;
    }
}