package com.ne.revival_games.entity.WorldObjects.Players;

/**
 * Created by Veganova on 7/7/2017.
 */

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Point;
import android.support.v4.view.GestureDetectorCompat;
import android.text.Layout;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ne.revival_games.entity.CameraController;
import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.Menu;
import com.ne.revival_games.entity.WorldObjects.Entity.Aimable;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * -list of entities
 * -Camera object that stores info about how to display the scene (setCenter, translation)
 * -money
 * -has listeners specific to it
 * -has its own menu
 * -one ghost object - have this ghost class implement the listener as well?
 */
public abstract class Player extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    protected List<Entity> entities;
    public CameraController camera;
    protected MyWorld world;
    protected Context context;
    protected JSONObject addToWorld = null;

    int playerNumber;
    Team team;
    protected float WIDTH = 900;
    protected float HEIGHT = 1600;
    protected float VIEW_WIDTH = 900;
    protected float VIEW_HEIGHT = 1600;
    protected Vector2 scales;
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector scaleGestureDetector;
    protected GhostEntity ghost;
    protected boolean oldScroll = false;
    protected Vector2 initialTranslate = new Vector2(0, 0);

    public Player(int id, Team team, MyWorld world, GamePanel gamePanel, MainActivity activity, boolean addListenertoPanel) {
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.scales = gamePanel.scales;
        System.out.println(gamePanel.scales);
        this.camera = gamePanel.camera;
        if (addListenertoPanel) {
            gamePanel.addPlayerListener(this);
        }
        this.entities = team.getTeamObjects();
        this.context = activity.getApplicationContext();
        this.WIDTH = activity.MAP_WIDTH;
        this.HEIGHT = activity.MAP_HEIGHT;
        this.VIEW_HEIGHT = gamePanel.getHeight();
        this.VIEW_WIDTH = gamePanel.getWidth();
        this.mDetector = new GestureDetectorCompat(activity.getApplicationContext(), this);
        this.mDetector.setIsLongpressEnabled(false);
        this.scaleGestureDetector = new ScaleGestureDetector(gamePanel.getContext(), new ScaleListener());
    }

    protected boolean holdingGhost = false;
    protected Vector2 pullTowards;
    protected double previousAngle = 0;
    protected double lastDownPress = 0;
    protected double lastMultiPress = 0;
    protected double mDownX, mDownY;


    public abstract void update();

    @Override
    public abstract boolean onTouch(View view, MotionEvent ev);

    @Override
    public abstract boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

    @Override
    public abstract boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);

    @Override
    public abstract boolean onDoubleTap(MotionEvent e);

    @Override
    public abstract boolean onSingleTapUp(MotionEvent e);


    public void setGhost(Entities type) {
        if (this.holdingGhost) {
            System.out.println("ALREADY HOLDING A GHOST!");
        } else {
            //center of the screen
            this.holdingGhost = true;
//            this.ghost = GhostFactory.produce(type, -1 * camera.translateXY.x * MyWorld.SCALE,
//                    -1 * camera.translateXY.y * MyWorld.SCALE, 0, world, team);
            addToWorld = new JSONObject();
            try{
                addToWorld.put("x", -1*camera.translateXY.x * MyWorld.SCALE);
                addToWorld.put("y", -1*camera.translateXY.y * MyWorld.SCALE);
                addToWorld.put("type", type.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }

            if(holdingGhost && this.ghost != null){
                this.pullTowards = this.ghost.entity.shape.body.getWorldCenter();
            }
            this.previousAngle = 0;
        }
    }

    public class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            double scaleFactor = scaleGestureDetector.getScaleFactor();
            scaleFactor = ((float) ((int) (scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
            camera.relativeZoom(scaleFactor, scaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            lastDownPress = System.currentTimeMillis();
        }
    }

    public Menu getMenu() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        if (this.playerNumber == 1) {
            return new Menu(context, this, 0);
        } else if (this.playerNumber == 2) {
            return new Menu(context, this, height - 400);
        } else {
            return new Menu(context, this, 0);
        }
    }
}
