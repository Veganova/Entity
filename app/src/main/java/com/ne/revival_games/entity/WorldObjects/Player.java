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
    private boolean moving = false;

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        //System.out.println("Player " + playerNumber);
        mDownX = ev.getX() / scales.x;
        mDownY = ev.getY() / scales.y;
        mDownX = mDownX - WIDTH/2;
        mDownY = -1*(mDownY - HEIGHT/2);

        if (holdingGhost && moving) {
            Vector2 newPos = new Vector2(mDownX/world.SCALE, mDownY/ world.SCALE);
            ghost.entity.shape.setBodyPosition(newPos, ghost.entity.shape.body.getWorldCenter());
        }
        else if (holdingGhost && (System.currentTimeMillis() - timeSinceFirst) > DRAG_START) {
            // Dragging - change ghost angle
            double angle = Util.absoluteAngle(this.ghost.entity.shape.body.getWorldCenter(),
                    new Vector2(mDownX, mDownY));
//                    Math.atan2(mDownY - ghost.entity.shape.getY(),
//                    mDownX - ghost.entity.shape.getX());
            //System.out.println(Math.toDegrees(angle));
            ghost.entity.shape.body.getTransform().setRotation((angle + 2*Math.PI) % (2*Math.PI));

        } else {

        }

        //System.out.println(mDownY + " " + mDownX);
        if (!holdingGhost && mDownY < lower || mDownY > higher) {
            // out of bounds
            return false;
        }

        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("ACTION DOWN");
                if (!holdingGhost) {
                    // First time holding down (no entity possessed yet)
                    holdingGhost = true;
                    timeSinceFirst = System.currentTimeMillis();
                    Entities randomEntity = Entities.random();
                    //System.out.println(randomEntity);
                    ghost = GhostFactory.produce(randomEntity, mDownX, mDownY, 0, world, team);
                    moving = true;
                }
                break;
           // case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (moving) {
                    moving = false;
                }
                else if (holdingGhost) {
                    System.out.println("ACTION UP");

                    // if a ghost is being held by the player
                    if (ghost.canPlace()) {
                        this.team.add(ghost.place());

                        holdingGhost = false;
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }



    /*
    User input handling
     */
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        double canvasX = event.getX() / scales.x;
//        double canvasY = event.getY()  / scales.y;
//
//        // Only allow clicks on the player's half - remove later
//        //System.out.println("click at (" + canvasX + ", " + canvasY + ")");
//        if (canvasY < lower || canvasY > higher) {
//            // out of bounds
//            return false;
//        }
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            long clickTime = System.currentTimeMillis();
//            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
//                onDoubleClick(event);
//            } else {
//                onSingleClick(event);
//            }
//            lastClickTime = clickTime;
//            return true;
//        }
//        return false;
//    }

    //@Override
//    public boolean onTouch(View view, MotionEvent ev) {
//        mDownX = ev.getX() / scales.x;
//        mDownY = ev.getY() / scales.x;
//        if (mDownY < lower || mDownY > higher) {
//            // out of bounds
//            return false;
//        }
//
//        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("ACTION DOWN");
//                if (!holdingGhost) {
//                    // First time holding down (no entity possessed yet)
//                    holdingGhost = true;
//                    Entities randomEntity = Entities.random();
//                    ghost = GhostFactory.produce(randomEntity, mDownX, mDownY, 0, world, team);
//                }
//                break;
//           // case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (holdingGhost) {
//                    // if a ghost is being held by the player
//                    if (ghost.canPlace()) {
//                        ghost.place();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    public void update() {
        for (Entity entity: this.team.getTeamObjects()) {
            if (entity instanceof Aimable) {
                ((Aimable) entity).aim();
            }
        }
    }

}
