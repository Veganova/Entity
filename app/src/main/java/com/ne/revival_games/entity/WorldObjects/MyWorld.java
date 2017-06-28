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
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.contact.ContactListener;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Veganova on 6/7/2017.
 */

public class MyWorld {

    public HashMap<Body, Entity> objectDatabase;
    public ArrayList<Body> bodiestodelete;
    public ArrayList<double []> coords;

    /** The scale 45 pixels per meter */
    public static final double SCALE = 50.0;

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
    public ObjTriangle tri;
    public ObjCircle circ;
    public Entity barrier;
    public Turret turret;
    public ComplexShape complex;
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
        this.bodiestodelete = new ArrayList<>();
        this.engineWorld = new World();


        this.engineWorld.setGravity(new Vector2(0, 0));
//        CollisionListener skip = new CollisionController(this);
//        ContactListener contact = new ContactController(this);
//        this.engineWorld.addListener(skip);
//        this.engineWorld.addListener(contact);

//        barrier = new Barrier(300, 400, 0, this);
//        Entity bullet = new Bullet(0, 0, 50, 60, this);
        turret = new Turret(new Vector2(-200, 100), 30, this);
//        rect = new Barrier(300, 300, 0, this);

        circ = new ObjCircle(0, 150, 10, this);
        coords = new ArrayList<double[]>();
        List<AShape> objects = new ArrayList<AShape>();
        double [] points = {0, 100, -100, -100, 100, -100};
        tri = new ObjTriangle(150, 150, points, this);
//        objects.add(new ObjCircle(0, 0, 50, 0));
//        testlocation(-500, 500, -500, 500, 10, tri);
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
        turret.aim(tri.body);
        this.engineWorld.update(elapsedTime);

        for(Body body: bodiestodelete){
            objectDatabase.get(body).onDeath();
            objectDatabase.remove(body);
            engineWorld.removeBody(body);
            bodiestodelete.remove(body);
        }

        for (Entity entity : objectDatabase.values()) {
//            entity.update(this);
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
            if(!entity.invisible)
            entity.draw(canvas);
        }
        tri.draw(canvas);
        circ.draw(canvas);

        Paint pent = new Paint();
        pent.setColor(Color.RED);
        for(double [] kek : coords){
            canvas.drawCircle((float) kek[0], (float) kek[1], (float) (5/SCALE), pent);
        }

    }

    public void testlocation(double x1, double x2, double y1, double y2, double grain, AShape shape){
        x1 = x1/SCALE;
        x2 = x2/SCALE;
        y1 = y1/SCALE;
        y2 = y2/SCALE;
        grain = grain/SCALE;
        for(double x=x1; x<x2; x+=grain){
            for(double y=y1; y<y2; y+=grain){
                if(shape.body.contains(new Vector2(x,y))){
                    double [] hi = {x, y};
                    coords.add(hi);
                }
            }
        }
    }
}

