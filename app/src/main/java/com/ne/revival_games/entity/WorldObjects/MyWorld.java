package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Shared.CustomEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;

import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Launcher;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.contact.ContactListener;
import org.dyn4j.geometry.Vector2;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Veganova on 6/7/2017.
 */

public class MyWorld {

    public Database objectDatabase;
//    public HashMap<GhostEntity, Team> entitiestoAdd = new HashMap<>();
//    public ArrayList<Body> bodiestodelete;
    public ArrayList<double []> coords;
    protected List<Player> players;
    public List<JSONObject> addtoWorld;
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

    /**
     * Allows checking if any of the bodies are out of bounds (and preforms deletion on them).
     */
    private Boundary bounds;

    CustomEntity up, down, left, right;

    /**
     * All updatable objects.
     */
    private List<Updatable> updatables;

    /**
     * default constructor for MyWorld (calls initialize engineWorld, can vary based off game type, etc.)
     *
     */
    public MyWorld(){
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
        this.objectDatabase = new Database ();
        this.ghosts = new HashMap<>();
//        this.bodiestodelete = new ArrayList<>();
        this.engineWorld = new World();
        this.players = new ArrayList<>();
        this.updatables = new ArrayList<>();
        this.updatables = Collections.synchronizedList(updatables);

        this.engineWorld.setGravity(new Vector2(0, 0));
        CollisionListener skip = new CollisionController(this);
        ContactListener contact = new ContactController(this);
        StepController step = new StepController();
        bounds = new Boundary(2000, this);
        this.engineWorld.addListener(skip);
        this.engineWorld.addListener(contact);
        this.engineWorld.addListener(step);
//        ExpandingCircle hi = new ExpandingCircle(new Vector2(0,0), 0.2, 0.01, 10, 500, 50, Team.NEUTRAL, this);
        Settings settings = new Settings();
//        settings.setAngularTolerance(50*settings.getAngularTolerance());
        settings.setLinearTolerance(150*settings.getLinearTolerance());
//        settings.setMaximumTranslation(settings.getMaximumTranslation() / 5.0);
        this.engineWorld.setSettings(settings);

//        bam = new MassLazer(-200, -200, 30, this);
//        this.ghostNexus = new GhostEntity(nex);

//        circ = new ObjCircle(0, 150, 10, this);
//        List<AShape> objects = new ArrayList<AShape>();



//        double [] points = {0, 100, -100, -100, 100, -100};
//        tri = new ObjTriangle(points);
//        tri.getBuilder(true, this).setXY(-100, -100).aoeJoint();

//        objects.add(new ObjCircle(0, 0, 50, 0));
//        testlocation(-500, 500, -500, 500, 10, tri);

//        ObjCircle hi = new ObjCircle(20);
//        hi.getBuilder(true, this).setXY(100, 100).setRestitution(0).init();
//        CustomEntity wow = new CustomEntity(hi, 0, 100, true, Team.NEUTRAL, this);
//        wow.addEffect( new EMP(wow, new Vector2(100,100), 0.1, 0.01, 10, 500, 200, wow.team, this, 6000));

        //MAKE THE STADIUM - TEMPORARY SO THAT THINGS DOING GO FLYING OUT
//        ObjRectangle up = new ObjRectangle(800, 20);
//        up.getBuilder(true, this).setXY(0, 750).setMassType(MassType.INFINITE).setRestitution(0).init();
//        this.up = new CustomEntity(up, 0, 100, true, Team.NEUTRAL, this);
//
//        ObjRectangle down = new ObjRectangle(800, 20);
//        down.getBuilder(true, this).setXY(0, -750).setMassType(MassType.INFINITE).setRestitution(0).init();
//        this.down = new CustomEntity(down, 0, 100, true, Team.NEUTRAL, this);
//
//        ObjRectangle left = new ObjRectangle(20, 1350);
//        left.getBuilder(true, this).setXY(-400, 0).setMassType(MassType.INFINITE).setRestitution(0).init();
//        this.left = new CustomEntity(left, 0, 100, true, Team.NEUTRAL, this);
//
//        ObjRectangle right = new ObjRectangle(20, 1350);
//        right.getBuilder(true, this).setXY(400, 0).setMassType(MassType.INFINITE).setRestitution(0).init();
//        this.right = new CustomEntity(right, 0, 100, true, Team.NEUTRAL, this);

//        new ShockwaveCanister(new Vector2(-100, -100), 0, 0, 0, Team.DEFENCE, this);

//        new ExplosiveMissile(new Vector2(-100, -100), 0, 0, 0, Team.DEFENCE, this);
//        Turret turret = new Turret(new Vector2(0, 100), 0, this, Team.DEFENCE);
//        Team.DEFENCE.add(turret);
//
//        Turret turret2 = new Turret(new Vector2(300, 300), 0, this, Team.OFFENSE);
//        Team.OFFENSE.add(turret2);

//        launcher = new Launcher(2000, 2000, this, Team.OFFENSE);
    }

    private Launcher launcher;
    //need a way to add an object (check what kind of object it is, etc.)
    //alternative is to add a new object on creation in MainThread
    /**
     * The method calling the necessary methods to update
     * the game, graphics, and poll for input.
     */
    public void objectUpdate() {
        // TODO: 7/7/2017 this check can probably be done when the user clicks the "place" command
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
    }

    /**
     * Cycles through entity list and draws onto canvas
     *
     * @param canvas the canvas onto which the entities will be drawn
     */
    public void drawObjects(Canvas canvas){
        this.bounds.draw(canvas);
//        launcher.draw(canvas);

        for (Entity toDraw: this.objectDatabase.values()) {
            if (!toDraw.ghost)
                toDraw.draw(canvas);
        }

        // Draw ghosts last so that they don't get overlapped by other objects. Copy to be safe.
        List<Entity> ghosts = new ArrayList<>(this.ghosts.keySet());
        for (Entity ghost: ghosts) {
            ghost.draw(canvas);
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

    public void addUpdatable(Updatable updatable) {
        this.updatables.add(updatable);
    }

    public boolean removeUpdatable(Updatable updatable) {
        return this.updatables.remove(updatable);
    }


    private class StepController extends StepAdapter {

        @Override
        public void begin(Step step, World world) {
            // deals with concurrent modification error by creating all the projectile objects
            // (things that are shot) before the database loop
            // WHY NOT CALL WORLD.UPDATE HERE.. UNECESSARY INFORMATION SHARING earth.objectUpdate()

            MyWorld earth = MyWorld.this;


            ArrayList<GhostEntity> ghosts = new ArrayList<>(earth.ghosts.values());

            for (int x = 0; x < ghosts.size(); x++) {
                ghosts.get(x).isColliding();
            }

            for (Player player : earth.players) {
                player.update();
            }

//        ArrayList<Entity> updater = new ArrayList<>(earth.objectDatabase.values());
//
//        for (int x = 0; x < updater.size(); x++){
//            earth.bounds.checkOutside(updater.get(x));
//            if(!updater.get(x).ghost)
//                updater.get(x).update(earth);
//        }



//            MyDeque values = earth.objectDatabase.valuesFast();
//            System.out.println("NUM OBJECTS - " + values.size());
//
//            long time = System.nanoTime();
//            Collection<Entity> x = earth.objectDatabase.values();
//            for (Entity entity:x) {
//                String s = entity.toString();
//            }
//            System.out.println("TIME DIF - " + (System.nanoTime() - time));

            for (Updatable updatable: earth.updatables) {
                updatable.update();
            }


            for (Entity entity:
//                    values) {
                earth.objectDatabase.values()) {
                earth.bounds.checkOutside(entity);
                if (!entity.ghost) {
                    entity.update(earth);
                }
            }
            earth.objectDatabase.addPendingAdditions();

        }

        public void updatePerformed(Step step, World world) {

        }

        @Override
        public void end(Step step, World world) {
            MyWorld earth = MyWorld.this;
//        for (int i = 0; i < earth.bodiestodelete.size(); i += 1) {
//            Body body = earth.bodiestodelete.get(i);
//            Entity toDelete = earth.objectDatabase.get(body);
//            if(toDelete != null){
//                if(toDelete.team != null)
//                    toDelete.team.getTeamObjects().remove(toDelete);
//                toDelete.onDeath(earth);
//                earth.objectDatabase.remove(body);
//            }
//            earth.engineWorld.removeBody(body);
//            earth.bodiestodelete.remove(body);
//        }
            earth.objectDatabase.removePendingDeletions(earth);
//        System.out.println("KEYSET - " + earth.objectDatabase.keySet().size());
        }


    }
}

