package com.ne.revival_games.entity.WorldObjects.Players;

/**
 * Created by Veganova on 7/7/2017.
 */

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.ne.revival_games.entity.CameraController;
import com.ne.revival_games.entity.CustomViews.MoneyView;
import com.ne.revival_games.entity.CustomViews.Screen;
import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.CustomViews.Menu;
import com.ne.revival_games.entity.TouchListeners.GestureCallback;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;
import org.json.JSONObject;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * -list of entities
 * -Camera object that stores info about how to display the scene (setCenter, translation)
 * -money
 * -has listeners specific to it
 * -has its own menu
 * -one ghost object - have this ghost class implement the listener as well?
 */
public abstract class Player extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private Screen screen;
//    protected Iterator<Entity> entities;
    public CameraController camera;
    protected MyWorld world;
    protected Context context;
    protected JSONObject addToWorld = null;

    int playerNumber;
    public Team team;
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
    protected double lastScale = 1;

    private double money;
    // money per tick
    private double mpt;
    private GestureCallback onScaleListener;
    private GestureCallback onMoveListener;
    private Menu menu;
    private MoneyView moneyView;
    protected GestureCallback onGhostPlace;

    public Player(int id, Team team, MyWorld world, Screen screen, MainActivity activity, boolean addListenertoPanel) {
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.screen = screen;

        GamePanel gamePanel = screen.getGamePanel();
        this.scales = gamePanel.scales;
//        System.out.println(gamePanel.scales);
        this.camera = gamePanel.camera;
        if (addListenertoPanel) {
            gamePanel.addPlayerListener(this);
        }
//        this.entities = world.objectDatabase.getTeamIterator(team);//team.getTeamObjects();
        this.context = activity.getApplicationContext();
        this.WIDTH = activity.MAP_WIDTH;
        this.HEIGHT = activity.MAP_HEIGHT;
        this.VIEW_HEIGHT = gamePanel.getHeight();
        this.VIEW_WIDTH = gamePanel.getWidth();
        this.mDetector = new GestureDetectorCompat(activity.getApplicationContext(), this);
        this.mDetector.setIsLongpressEnabled(false);
        this.scaleGestureDetector = new ScaleGestureDetector(gamePanel.getContext(), new ScaleListener());

        this.money = 0.0;
        this.mpt = 0.0;
    }

    protected boolean holdingGhost = false;
    protected Vector2 pullTowards;
    protected double previousAngle = 0;
    protected double lastDownPress = 0;
    protected double lastMultiPress = 0;
    protected double mDownX, mDownY;

    @OverridingMethodsMustInvokeSuper
    public void update() {
        this.money += this.mpt;
    }

    public double getMoney() {
        return money;
    }

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


    public void setGhost(EntityLeaf toProduce, float x, float y) {
        if (this.holdingGhost) {
            System.out.println("ALREADY HOLDING A GHOST!");
        } else {
            //center of the screen
            this.holdingGhost = true;
//            this.ghost = GhostFactory.produce(type, -1 * camera.translateXY.x * MyWorld.SCALE,
//                    -1 * camera.translateXY.y * MyWorld.SCALE, 0, world, team);
            addToWorld = new JSONObject();
            try{
                addToWorld.put("x", x -1*camera.translateXY.x * MyWorld.SCALE);
                addToWorld.put("y", y -1*camera.translateXY.y * MyWorld.SCALE);
                addToWorld.put("type", toProduce);
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

    public void addMoney(double damage) {
        this.money += damage;
    }


    public void addOnScaleListener(GestureCallback callback) {
        this.onScaleListener = callback;
    }

    public void addOnMoveListener(GestureCallback callback) {
        this.onMoveListener = callback;
    }

    public void callScaleListener() {
        if (this.onScaleListener != null) {
            this.onScaleListener.apply();
        }
    }

    public void callMoveListener() {
        if (this.onMoveListener != null) {
            this.onMoveListener.apply();
        }
    }

    public class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        protected double refX = 0;
        protected double refY = 0;
        protected double absoluteX = 0;
        protected double absoluteY = 0;
        protected double initialZoom = 0;

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            double scaleFactor = scaleGestureDetector.getScaleFactor();
            scaleFactor = ((float) ((int) (scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
            //WIDTH, HEIGHT

            //record absolute position of zoom
            //snap screen to focus of zoom
//          //move screen appropriately by absolute coordinates based on current screen size

            mDownX = scaleGestureDetector.getFocusX() / scales.x;   //scales coordinates 0 to map width
            mDownY = scaleGestureDetector.getFocusY() / scales.y;   //scales coordinates 0 to map height
            mDownX = (mDownX - WIDTH / 2);     //centers x
            mDownY = -1 * (mDownY - HEIGHT / 2); //centers y
            mDownX /= camera.zoomXY.x;
            mDownY /= camera.zoomXY.y;
            mDownX -= camera.translateXY.x * MyWorld.SCALE;
            mDownY -= camera.translateXY.y * MyWorld.SCALE;
//            System.out.println(mDownX + " " + mDownY);
            camera.relativeZoom(scaleFactor, scaleFactor);
            camera.move(-1* refX, -1* refY);
            camera.relativeMove(-1*absoluteX/camera.zoomXY.x*MyWorld.SCALE, -1*absoluteY/camera.zoomXY.y*MyWorld.SCALE);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            mDownX = scaleGestureDetector.getFocusX() / scales.x;   //scales coordinates 0 to map width
            mDownY = scaleGestureDetector.getFocusY() / scales.y;   //scales coordinates 0 to map height
            mDownX = (mDownX - WIDTH / 2);     //centers x
            mDownY = -1 * (mDownY - HEIGHT / 2); //centers y
            absoluteX = mDownX;
            absoluteY = mDownY;
            mDownX /= camera.zoomXY.x;
            mDownY /= camera.zoomXY.y;
            mDownX -= camera.translateXY.x * MyWorld.SCALE;
            mDownY -= camera.translateXY.y * MyWorld.SCALE;

            refX = mDownX;
            refY = mDownY;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            lastDownPress = System.currentTimeMillis();
            Player.this.callScaleListener();
        }
    }

    public void addMenu(int parentHeight, float SCREEN_WIDTH, boolean startHidden) {
        this.menu = new Menu(context, parentHeight, this, Gravity.BOTTOM, SCREEN_WIDTH, startHidden);
        this.screen.addView(menu);
    }


    public void addMoneyView(MainActivity activity, float SCREEN_WIDTH) {
        this.moneyView = new MoneyView(activity, context, world, this, SCREEN_WIDTH);
        this.screen.addView(moneyView);
    }

    public boolean readytoPlace(MotionEvent e){
        return this.ghost != null && lastMultiPress + 200 < System.currentTimeMillis()
                && e.getPointerCount() < 2 && ghost.entity.isFlingable()
                && holdingGhost && this.ghost.canPlace();
    }


    public void setOnGhostPlace(GestureCallback onGhostPlace) {
        this.onGhostPlace = onGhostPlace;
    }


    public Menu getMenu() {
        return this.menu;
    }

    public MoneyView getMoneyView() {
        return this.moneyView;
    }



}
