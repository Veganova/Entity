package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.CustomEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.MassLazer;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;

import com.ne.revival_games.entity.WorldObjects.Entity.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactListener;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.MassType;
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
    public ArrayList<Body> bodiestodelete;
    public ArrayList<double []> coords;
    private List<Player> players;
    public HashMap<Entity, GhostEntity> ghosts;

    /**static friction */
    public static double staticFriction = 20;

    /**kinetic friction */
    public static double kineticFriction = 10;

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


    CustomEntity up, down, left, right;

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

    public void addPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Creates game objects and adds them to the engineWorld.
     * <p>
     * Basically the same shapes from the Shapes test in
     * the TestBed.
     */
    protected void initializeWorld() {
        // create the engineWorld
        coords = new ArrayList<double[]>();
        this.objectDatabase = new HashMap<>();
        this.ghosts = new HashMap<>();
        this.bodiestodelete = new ArrayList<>();
        this.engineWorld = new World();
        this.players = new ArrayList<>();

        this.engineWorld.setGravity(new Vector2(0, 0));
        CollisionListener skip = new CollisionController(this);
        ContactListener contact = new ContactController(this);
        this.engineWorld.addListener(skip);
        this.engineWorld.addListener(contact);

//        bam = new MassLazer(-200, -200, 30, this);
//        this.ghostNexus = new GhostEntity(nex);

//        circ = new ObjCircle(0, 150, 10, this);
//        List<AShape> objects = new ArrayList<AShape>();



//        double [] points = {0, 100, -100, -100, 100, -100};
//        tri = new ObjTriangle(points);
//        tri.getBuilder(true, this).setXY(-100, -100).aoeJoint();

//        objects.add(new ObjCircle(0, 0, 50, 0));
//        testlocation(-500, 500, -500, 500, 10, tri);

        //MAKE THE STADIUM - TEMPORARY SO THAT THINGS DOING GO FLYING OUT
        ObjRectangle up = new ObjRectangle(800, 20);
        up.getBuilder(true, this).setXY(0, 750).setMassType(MassType.INFINITE).setRestitution(0).init();
        this.up = new CustomEntity(up, 0, 100, true, Team.NEUTRAL, this);

        ObjRectangle down = new ObjRectangle(800, 20);
        down.getBuilder(true, this).setXY(0, -750).setMassType(MassType.INFINITE).setRestitution(0).init();
        this.down = new CustomEntity(down, 0, 100, true, Team.NEUTRAL, this);

        ObjRectangle left = new ObjRectangle(20, 1350);
        left.getBuilder(true, this).setXY(-400, 0).setMassType(MassType.INFINITE).setRestitution(0).init();
        this.left = new CustomEntity(left, 0, 100, true, Team.NEUTRAL, this);

        ObjRectangle right = new ObjRectangle(20, 1350);
        right.getBuilder(true, this).setXY(400, 0).setMassType(MassType.INFINITE).setRestitution(0).init();
        this.right = new CustomEntity(right, 0, 100, true, Team.NEUTRAL, this);

//        Turret turret = new Turret(new Vector2(0, 100), 0, this, Team.DEFENCE);
//        Team.DEFENCE.add(turret);
    }

    //need a way to add an object (check what kind of object it is, etc.)
    //alternative is to add a new object on creation in MainThread
    /**
     * The method calling the necessary methods to update
     * the game, graphics, and poll for input.
     */
    public void objectUpdate() {
        // TODO: 7/7/2017 this check can probably be done when the user clicks the "place" command
        for (GhostEntity ghost :this.ghosts.values()) {
            ghost.isColliding();
        }

        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = diff / NANO_TO_BASE;
        // update the engineWorld with the elapsed time

        for (Player player : this.players) {
            player.update();
        }

        for(Entity ent: this.objectDatabase.values()){
            ent.update(this);
        }



        for (int i = 0; i < bodiestodelete.size(); i += 1) {
            Body body = bodiestodelete.get(i);
            Entity toDelete = objectDatabase.get(body);
            if(toDelete != null){
                toDelete.onDeath(this);
                objectDatabase.remove(body);
            }
            engineWorld.removeBody(body);
            bodiestodelete.remove(body);
        }

        this.engineWorld.update(elapsedTime);
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
        // up.draw(canvas);
//        tri.draw(canvas);
//        circ.draw(canvas);
//
//        if (ghost.canPlace())
//            ghost.entity.shape.setPaint(Paint.Style.STROKE);

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

