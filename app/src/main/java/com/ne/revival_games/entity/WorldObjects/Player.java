package com.ne.revival_games.entity.WorldObjects;

/**
 * Created by Veganova on 7/7/2017.
 */

import android.graphics.Camera;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;

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

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    private long lastClickTime = 0;

    private static final float WIDTH = 900;
    private static final float HEIGHT = 1600;
    private float lower = 0;
    private float higher = HEIGHT;
    private Vector2 scales;


    public Player(int id, Team team, MyWorld world, Vector2 scales, int lower, int higher) {
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.scales = scales;
        this.higher = higher;
        this.lower = lower;
    }







    /*
    User input handling
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        double canvasX = event.getX() / scales.x;
        double canvasY = event.getY()  / scales.y;

        // Only allow clicks on the player's half - remove later
        //System.out.println("click at (" + canvasX + ", " + canvasY + ")");
        if (canvasY < lower || canvasY > higher) {
            // out of bounds
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(event);
            } else {
                onSingleClick(event);
            }
            lastClickTime = clickTime;
            return true;
        }
        return false;
    }
//    private float mDownX;
//    private float mDownY;
//    private final float SCROLL_THRESHOLD = 10;
//    private boolean isOnClick;
//    @Override
//    public boolean onTouch(View view, MotionEvent ev) {
//        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("ACTION DOWN");
//                mDownX = ev.getX();
//                mDownY = ev.getY();
//                isOnClick = true;
//                break;
//           // case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                if (isOnClick) {
//                    System.out.println("REGULAR CLICK?");
//                    //TODO onClick code
//                }
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    private void onSingleClick(MotionEvent event) {
        double canvasX = event.getX() / scales.x;
        double canvasY = event.getY()  / scales.y;
        if (world.ghost.entity != null) {
            world.ghost.entity.shape.body.translateToOrigin();
            world.ghost.entity.shape.body.translate((canvasX - WIDTH / 2) / MyWorld.SCALE,
                    -1 * (canvasY - HEIGHT / 2) / MyWorld.SCALE);
        } else {
            world.nex.shape.body.translateToOrigin();
            world.nex.shape.body.translate((canvasX - WIDTH / 2) / MyWorld.SCALE,
                    -1 * (canvasY - HEIGHT / 2) / MyWorld.SCALE);
        }
        System.out.println(world.ghost.canPlace());
    }

    private void onDoubleClick(MotionEvent event) {
        System.out.println("DOUBLE");
        if (world.ghost.canPlace()) {
            System.out.println("PLACED");
            world.nex = world.ghost.place();
        }
    }
}
