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
import com.ne.revival_games.entity.CustomViews.MenuList;
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
    private final GamePanel gamePanel;
    private final MainActivity activity;
    private Screen screen;
//    protected Iterator<Entity> entities;
    public CameraController camera;
    protected MyWorld world;
    protected Context context;
    protected AddToWorld addToWorld;

    int playerNumber;
    public Team team;
    protected float WIDTH = 900;
    protected float HEIGHT = 1600;
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
    protected boolean firstGhostHold = false;

    public Player(int id, Team team, MyWorld world, Screen screen, MainActivity activity, boolean addListenertoPanel) {
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.screen = screen;
        this.activity = activity;
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
        this.mDetector = new GestureDetectorCompat(activity.getApplicationContext(), this);
        this.mDetector.setIsLongpressEnabled(false);
        this.scaleGestureDetector = new ScaleGestureDetector(gamePanel.getContext(), new ScaleListener());
        this.gamePanel = gamePanel;
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


    public void setGhost(EntityLeaf toProduce, float creationX, float creationY, MotionEvent e) {
        if (this.holdingGhost) {
            System.out.println("ALREADY HOLDING A GHOST!");
        } else {
            //center of the screen
            this.holdingGhost = true;
            creationY = this.gamePanel.getHeight() - creationY;
            double cameraX = -1*camera.getCenter().x * MyWorld.SCALE;
            double cameraY = -1*camera.getCenter().y * MyWorld.SCALE;
            double distCenterX = (creationX - this.gamePanel.getWidth() / 2);
            double distCenterY = (creationY - this.gamePanel.getHeight() / 2);

            System.out.println("CREATED: " + creationX + " " + creationY + " | " + this.WIDTH + ", " + this.HEIGHT + " | " + this.gamePanel.getWidth() + " " + this.gamePanel.getHeight());
//            System.out.println("CAMERA : " + camera.getCenter() + " " + distCenterX + " " + distCenterY);// camera.translateXY.x + ", " + camera.translateXY.y);
            addToWorld = new AddToWorld(toProduce, distCenterX + cameraX, distCenterY + cameraY);
//            this.onTouch(activity.view, e);
            this.pullTowards = new Vector2(0, 0);
            this.firstGhostHold = true;
//            PERHAPS NEED TO INVOKE THIS ON THE VIEW THREAD:
//            - calls onscroll once but not getting called repeatedly
            gamePanel.dispatchTouchEvent(e);
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

    /**
     * On next touch make poppist.stopAndDispatch = false;
     */
    public abstract void setStopAndDispatch(MenuList.Poppist stopAndDispatch);

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

    public GamePanel getGamePanel() {
        return gamePanel;
    }

}
