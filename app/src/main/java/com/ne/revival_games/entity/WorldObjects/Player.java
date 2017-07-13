package com.ne.revival_games.entity.WorldObjects;

/**
 * Created by Veganova on 7/7/2017.
 */

import android.graphics.Camera;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;

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
public class Player implements View.OnTouchListener {
    private List<Entity> entities;
    private Camera camera;
    private int money;
    private MyWorld world;

    int playerNumber;
    Team team;




    private static final float WIDTH = 900;
    private static final float HEIGHT = 1600;
    private float lower = 0;
    private float higher = HEIGHT;
    private Vector2 scales;

    private GhostEntity ghost;

    public Player(int id, Team team, MyWorld world, Vector2 scales, int lower, int higher) {
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.scales = scales;
        this.higher = higher;
        this.lower = lower;
    }

    private double mDownX, mDownY;
    private final float SCROLL_THRESHOLD = 10;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private static final long DRAG_START = 200;//milliseconds
    private long timeSinceFirst = 0;
    private boolean holdingGhost = false;
    // 0 - havent moved yet. 1 - moving currently. 2 - already moved (in rotation stage)
    private int moving = 0;
    private Vector2 pullTowards;

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        //System.out.println("Player " + playerNumber);
        mDownX = ev.getX() / scales.x;
        mDownY = ev.getY() / scales.y;
        mDownX = mDownX - WIDTH/2;
        mDownY = -1*(mDownY - HEIGHT/2);

        if (holdingGhost && moving == 1) {
            pullTowards= new Vector2(mDownX/world.SCALE, mDownY/ world.SCALE);
        }
        else if (holdingGhost && moving == 2) {
            // Dragging - change ghost angle
            double angle = Util.absoluteAngle(this.ghost.entity.shape.body.getWorldCenter(),
                    new Vector2(mDownX, mDownY));
            ghost.entity.shape.body.getTransform().setRotation((angle + 2*Math.PI) % (2*Math.PI));
        }
        if (!holdingGhost && mDownY < lower || mDownY > higher) {
            // out of bounds
            return false;
        }

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (holdingGhost && moving == 0) {
                    moving = 1;
                    pullTowards= new Vector2(mDownX/world.SCALE, mDownY/ world.SCALE);
                }
                break;
           // case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (moving == 1) {
                    moving = 2;
                    this.ghost.entity.shape.body.setLinearVelocity(0, 0);
                    this.ghost.entity.shape.body.setAngularVelocity(0);
                    this.ghost.entity.shape.body.clearAccumulatedForce();

                    this.ghost.entity.shape.body.clearAccumulatedTorque();
                }
                else if (holdingGhost && moving == 2) {

                    // if a ghost is being held by the player
                    if (ghost.canPlace()) {
                        this.team.add(ghost.place());

                        holdingGhost = false;
                        moving = 0;
                    }
                }
                break;
            default:
                break;

        }
        return true;
    }


    public void update() {
        for (Entity entity: this.team.getTeamObjects()) {
            if (entity instanceof Aimable) {
                ((Aimable) entity).aim();
            }
        }
        if (holdingGhost && moving == 1) {
            Vector2 delta = new Vector2(pullTowards.x - ghost.entity.shape.getX(),
                    pullTowards.y - ghost.entity.shape.getY());
            ghost.entity.shape.body.setLinearVelocity(10 * delta.x, 10 * delta.y);
            System.out.println(ghost.entity.shape.body.getFixtureCount());
        }
    }

    public void setGhost(Entities type) {
        if(this.holdingGhost) {
            System.out.println("ALREADY HOLDING A GHOST!");
        } else {
            double x = 0;
            double y = (this.higher + this.lower) / 2;
            this.ghost = GhostFactory.produce(type, x / this.scales.x, y / this.scales.y, 0, world, team);
            System.out.println(ghost.entity);
            this.holdingGhost = true;
            this.moving = 0;
        }
    }

}
