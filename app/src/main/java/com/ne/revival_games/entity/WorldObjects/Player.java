package com.ne.revival_games.entity.WorldObjects;

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

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.List;

/**
 *
 -list of entities
 -Camera object that stores info about how to display the scene (setCenter, translation)
 -money
 -has listeners specific to it
 -has its own menu
 -one ghost object - have this ghost class implement the listener as well?
 */
public class Player extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{
    private List<Entity> entities;
    public CameraController camera;
    private MyWorld world;
    private Context context;

    int playerNumber;
    Team team;
    private float WIDTH = 900;
    private float HEIGHT = 1600;
    private float VIEW_WIDTH = 900;
    private float VIEW_HEIGHT = 1600;
    private Vector2 scales;
    private GestureDetectorCompat mDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private GhostEntity ghost;
    private boolean oldScroll = false;
    private Vector2 initialTranslate = new Vector2(0,0);

    public Player(int id, Team team, MyWorld world, GamePanel gamePanel, MainActivity activity, boolean addListenertoPanel){
        this.playerNumber = id;
        this.team = team;
        this.world = world;
        this.scales = gamePanel.scales;
        System.out.println(gamePanel.scales);
        this.camera = gamePanel.camera;
        if(addListenertoPanel){
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

    private boolean holdingGhost = false;
    private Vector2 pullTowards;
    private double previousAngle = 0;
    private double lastDownPress  = 0;
    private double lastMultiPress = 0;
    private double mDownX, mDownY;

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        mDownX = ev.getX() / scales.x;   //scales coordinates 0 to map width
        mDownY = ev.getY() / scales.y;   //scales coordinates 0 to map height
        mDownX = (mDownX - WIDTH/2);     //centers x
        mDownY = -1*(mDownY - HEIGHT/2); //centers y
        mDownX /= camera.zoomXY.x;
        mDownY /= camera.zoomXY.y;
        mDownX -= camera.translateXY.x*MyWorld.SCALE;
        mDownY -= camera.translateXY.y*MyWorld.SCALE;


        int mask = (ev.getAction() & MotionEvent.ACTION_MASK);

        if( mask == MotionEvent.ACTION_DOWN){
            lastDownPress = System.currentTimeMillis();
        }
        else if( mask == MotionEvent.ACTION_UP || mask == MotionEvent.ACTION_POINTER_UP){
            previousAngle = 0;
        }

            if (ev.getPointerCount() > 1) {
                if (holdingGhost) {
                    //we need this line unfortunately for turret TODO: change it back on place!
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
                else{
                    this.scaleGestureDetector.onTouchEvent(ev);
                }
            } else {
                this.mDetector.onTouchEvent(ev);
            }
        return false;
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
            //center of the screen
            this.ghost = GhostFactory.produce(type, -1*camera.translateXY.x*MyWorld.SCALE,
                    -1*camera.translateXY.y*MyWorld.SCALE, 0, world, team);
            this.pullTowards = this.ghost.entity.shape.body.getWorldCenter();
            this.holdingGhost = true;
            this.previousAngle = 0;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        System.out.println("SCROLLING");
        double scrollSpeed = 3000 / camera.zoomXY.x;
        double marginDetection = 30;
        double mDownX = e2.getX() / scales.x;
        double mDownY = e2.getY() / scales.y;
        mDownX = mDownX - WIDTH/2;
        mDownY = -1*(mDownY - HEIGHT/2);
        double realDownx = mDownX;
        double realDowny = mDownY;
        mDownX /= camera.zoomXY.x;
        mDownY /= camera.zoomXY.y;
        mDownX -= camera.translateXY.x*MyWorld.SCALE;
        mDownY -= camera.translateXY.y*MyWorld.SCALE;

        if(holdingGhost){
            if(lastDownPress + 30 < System.currentTimeMillis()){
                if(e2.getPointerCount() < 2 && e1.getPointerCount() < 2){
                    pullTowards= new Vector2(mDownX/world.SCALE, mDownY/ world.SCALE);
                }
            }
            if(camera.nearEdge(e2.getX(), e2.getY(), marginDetection)) {
                camera.relativeMove(realDownx, realDowny);
            }
            return false;
        }
        else if(lastDownPress + 30 < System.currentTimeMillis()){
            camera.relativeMove(scrollSpeed*distanceX/MyWorld.SCALE, -1*scrollSpeed*distanceY/MyWorld.SCALE);
        }


        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        velocityX = velocityX / maxFlingVelocity;
        velocityY = velocityY /maxFlingVelocity;

        if(holdingGhost && velocityX > 0.5 || velocityY > 0.5) {
            holdingGhost = false;
            this.ghost.removeGhost();
            this.ghost = null;
        }
        else if(!holdingGhost) {
            if(velocityX > 0.3 || velocityY > 0.3) {

            }
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

    public class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            double scaleFactor = scaleGestureDetector.getScaleFactor();
            scaleFactor = ((float)((int)(scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
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
            return new Menu(context, this, height - 300);
        } else {
            return new Menu(context, this, 0);
        }
    }
}
