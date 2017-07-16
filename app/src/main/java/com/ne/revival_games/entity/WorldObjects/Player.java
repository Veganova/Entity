package com.ne.revival_games.entity.WorldObjects;

/**
 * Created by Veganova on 7/7/2017.
 */

import android.content.Context;
import android.graphics.Camera;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 *
 -list of entities
 -Camera object that stores info about how to display the scene (zoom, translation)
 -money
 -has listeners specific to it
 -has its own menu
 -one ghost object - have this ghost class implement the listener as well?
 */
public class Player extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{
    private List<Entity> entities;
    private Camera camera;
    private int money;
    private MyWorld world;
    private Context context;

    int playerNumber;
    Team team;




    private static final float WIDTH = 900;
    private static final float HEIGHT = 1600;
    private float lower = 0;
    private float higher = HEIGHT;
    private Vector2 scales;
    private GestureDetectorCompat mDetector;
    private GhostEntity ghost;

    public Player(int id, Team team, MyWorld world, Vector2 scales, int lower, int higher, MainActivity activity){
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.scales = scales;
        this.higher = higher;
        this.lower = lower;
        this.entities = team.getTeamObjects();
        this.context = activity.getApplicationContext();
        mDetector = new GestureDetectorCompat(context, this);
    }

    private final float SCROLL_THRESHOLD = 10;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private static final long DRAG_START = 200;//milliseconds
    private long timeSinceFirst = 0;
    private boolean holdingGhost = false;
    // 0 - havent moved yet. 1 - moving currently. 2 - already moved (in rotation stage)
    private int moving = 0;
    private Vector2 pullTowards;
    private double previousAngle = 0;
    private double lastDownPress  = 0;
    private double lastMultiPress = 0;
    private boolean waitForMultiTouch = false;

    private double mDownX, mDownY;

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        mDownX = ev.getX() / scales.x;
        mDownY = ev.getY() / scales.y;
        mDownX = mDownX - WIDTH/2;
        mDownY = -1*(mDownY - HEIGHT/2);

        int mask = (ev.getAction() & MotionEvent.ACTION_MASK);

        if( mask == MotionEvent.ACTION_DOWN){
            lastDownPress = System.currentTimeMillis();
        }
        else if( mask == MotionEvent.ACTION_UP || mask == MotionEvent.ACTION_POINTER_UP){
            previousAngle = 0;
        }

            if (ev.getPointerCount() > 1) {
                if (holdingGhost) {
                    //we need this line unfortunately for turret
                    this.ghost.entity.shape.body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
                    //rotate
                    double p1x = ev.getX(0);
                    double p1y = ev.getY(0);
                    double p2x = ev.getX(1);
                    double p2y = ev.getY(1);
                    double direction = 1;
                    double currentAngle = Util.absoluteAngle(new Vector2(p2x, p2y), new Vector2(p1x, p1y));
                    if(previousAngle != 0)
                    this.ghost.setAngle(-1 * (currentAngle - previousAngle), this.ghost.entity.shape.body);
                    previousAngle = currentAngle;
                    lastMultiPress = System.currentTimeMillis();
                    lastDownPress = lastMultiPress;
                }
            } else {
                this.mDetector.onTouchEvent(ev);
            }
        //System.out.println("Player " + playerNumber);
//        mDownX = ev.getX() / scales.x;
//        mDownY = ev.getY() / scales.y;
//        mDownX = mDownX - WIDTH/2;
//        mDownY = -1*(mDownY - HEIGHT/2);
        return true;
    }


    public void update() {
        for (Entity entity: this.team.getTeamObjects()) {
            if (entity instanceof Aimable) {
                ((Aimable) entity).aim();
            }
        }
        if (holdingGhost) {
            Vector2 delta = new Vector2(pullTowards.x - ghost.entity.shape.getX(),
                    pullTowards.y - ghost.entity.shape.getY());

            ghost.setLinearVelocity(10 * delta.x, 10 * delta.y);
//            System.out.println(ghost.entity.shape.body.getFixtureCount());
        }
    }

    public void setGhost(Entities type) {
        if(this.holdingGhost) {
            System.out.println("ALREADY HOLDING A GHOST!");
        } else {
            double x = 0;
            double y = (this.higher + this.lower) / 2;
            this.ghost = GhostFactory.produce(type, x / this.scales.x, y / this.scales.y, 0, world, team);
            this.pullTowards = this.ghost.entity.shape.body.getWorldCenter();
//            System.out.println(ghost.entity);
            this.holdingGhost = true;
            this.previousAngle = 0;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        double mDownX = e2.getX() / scales.x;
        double mDownY = e2.getY() / scales.y;
        mDownX = mDownX - WIDTH/2;
        mDownY = -1*(mDownY - HEIGHT/2);
        System.out.println("");
        if(holdingGhost && lastDownPress + 30 < System.currentTimeMillis()){
            if(e2.getPointerCount() < 2 && e1.getPointerCount() < 2){
                pullTowards= new Vector2(mDownX/world.SCALE, mDownY/ world.SCALE);
            }
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        velocityX = velocityX / maxFlingVelocity;
        velocityY = velocityY /maxFlingVelocity;
        System.out.println(velocityX);
//        velocityY = velocityY / maxFlingVelocity;
        if(holdingGhost && velocityX > 0.5 || velocityY > 0.5) {
            holdingGhost = false;
            this.ghost.removeGhost();
            this.ghost = null;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(lastMultiPress + 200 < System.currentTimeMillis() && e.getPointerCount() < 2
                && holdingGhost && this.ghost.canPlace()){
            ghost.place().addToTeam(team);
            holdingGhost = false;
        } else if (!holdingGhost) {
            Vector2 clickPos = new Vector2(mDownX / world.SCALE, mDownY / world.SCALE);
            for (Entity teamEntity: this.entities) {
                if (teamEntity.shape.body.contains(clickPos)) {
                    teamEntity.interact();
                    break;
                }
            }
        }
        return false;
    }

}
