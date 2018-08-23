package com.ne.revival_games.entity.WorldObjects.Players;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ne.revival_games.entity.CustomViews.MenuList;
import com.ne.revival_games.entity.CustomViews.Screen;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;

import org.dyn4j.geometry.Vector2;

import java.util.Iterator;

/**
 * Created by vishn on 7/27/2017.
 */

public class PlayerDefense extends Player {
    private MenuList.Poppist poppist;

    public PlayerDefense(int id, Team team, MyWorld world, Screen screen, MainActivity activity, boolean addListenertoPanel) {
        super(id, team, world, screen, activity, addListenertoPanel);
    }

    @Override
    public void update() {
        super.update();
        if(this.addToWorld != null)
            try{
//                pullTowards = new Vector2(addToWorld.getX()/MyWorld.SCALE, addToWorld.getY()/MyWorld.SCALE);
                this.ghost = GhostFactory.produce(
                        addToWorld.getType(),
                        addToWorld.getX(),
                        addToWorld.getY(),
                        0,
                        world,
                        team
                );
                this.addToWorld = null;
            }
            catch (Exception e){
                e.printStackTrace();
            }

        if (holdingGhost && ghost != null) {
            System.out.println("pullTowards in update: " + pullTowards);
            Vector2 delta = new Vector2(pullTowards.x - ghost.entity.shape.getX(),
                    pullTowards.y - ghost.entity.shape.getY());
            ghost.setLinearVelocity(10 * delta.x, 10 * delta.y);
        }
    }


    @Override
    public void setStopAndDispatch(MenuList.Poppist stopAndDispatch) {
        // On next touch make poppist.stopAndDispatch = false;
        this.poppist = stopAndDispatch;
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {

        mDownX = ev.getX() / scales.x;   //scales coordinates 0 to map width
        mDownY = ev.getY() / scales.y;   //scales coordinates 0 to map height
        mDownX = (mDownX - WIDTH / 2);     //centers x
        mDownY = -1 * (mDownY - HEIGHT / 2); //centers y
        mDownX /= camera.zoomXY.x;
        mDownY /= camera.zoomXY.y;
        mDownX -= camera.translateXY.x * MyWorld.SCALE;
        mDownY -= camera.translateXY.y * MyWorld.SCALE;

        int mask = (ev.getAction() & MotionEvent.ACTION_MASK);

        if (mask == MotionEvent.ACTION_DOWN) {
            lastDownPress = System.currentTimeMillis();
            if (this.firstGhostHold) {
                System.out.println("ON TOUCH IN PLAYERDEFESE");
                this.mDetector.onTouchEvent(ev);
                return false;
            }
        } else if (mask == MotionEvent.ACTION_UP || mask == MotionEvent.ACTION_POINTER_UP) {
            previousAngle = 0;
            if (this.firstGhostHold) {
                this.firstGhostHold = false;
                this.poppist.setStopAndDispatch(false);
            }
        }

        // two finger touch
        if (ev.getPointerCount() > 1) {
            if (holdingGhost && ghost.entity != null) {

                //rotate
                double p1x = ev.getX(0);
                double p1y = ev.getY(0);
                double p2x = ev.getX(1);
                double p2y = ev.getY(1);
                double currentAngle = Util.absoluteAngle(new Vector2(p2x, p2y), new Vector2(p1x, p1y));
                if (previousAngle != 0)
                    this.ghost.setAngle(-1 * (currentAngle - previousAngle), this.ghost.entity.shape.body);
                previousAngle = currentAngle;
                lastMultiPress = System.currentTimeMillis();
                lastDownPress = lastMultiPress;
            } else {
                this.scaleGestureDetector.onTouchEvent(ev);
            }
        } else {
            this.mDetector.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        if (this.firstGhostHold) {
//            System.out.println("ON SCROLL IN PLAYERDEFESE");
//            return false;
//        }
        double scrollSpeed = 3000 / camera.zoomXY.x;
        double marginDetection = 30;
        double mDownX = e2.getX() / scales.x;
        double mDownY = e2.getY() / scales.y;
        mDownX = mDownX - WIDTH / 2;
        mDownY = -1 * (mDownY - HEIGHT / 2);
        double realDownx = mDownX;
        double realDowny = mDownY;
        mDownX /= camera.zoomXY.x;
        mDownY /= camera.zoomXY.y;
        mDownX -= camera.translateXY.x * MyWorld.SCALE;
        mDownY -= camera.translateXY.y * MyWorld.SCALE;

        System.out.println("SCROLLIG");
        if (holdingGhost) {
            System.out.println("SCROLLING WITH GHOST!");
            if (lastDownPress + 30 < System.currentTimeMillis()) {
                if (firstGhostHold || (e2.getPointerCount() < 2 && e1.getPointerCount() < 2)) {
                    pullTowards = new Vector2(mDownX / world.SCALE, mDownY / world.SCALE);
                    System.out.println("pullTowards in scroll: " + pullTowards);
                }
            }
            if (camera.nearEdge(e2.getX(), e2.getY(), marginDetection)) {
                camera.relativeMove(realDownx, realDowny);
            }
            return false;
        } else if (lastDownPress + 30 < System.currentTimeMillis()) {
            camera.relativeMove(scrollSpeed * (int)(distanceX) / MyWorld.SCALE, -1 * scrollSpeed
                    * (int)(distanceY) / MyWorld.SCALE);
            this.callMoveListener();
        }


        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        if (readytoPlace(e)) {
            double cost =  MySettings.getEntityNum(team.toString(), new Query(ghost.entity.getName(), "cost"), true);
            if (cost <= this.getMoney()) {
                this.ghost.place(this);
                this.addMoney(-1 * cost);
                this.ghost = null;
                holdingGhost = false;
                if (this.onGhostPlace != null) {
                    this.onGhostPlace.apply();
                }
            } else {
                System.out.println("NOT ENOUGH CASH TO GET A " + ghost.entity.getName());
            }
        } else if (!holdingGhost) {
            Vector2 clickPos = new Vector2(mDownX / world.SCALE, mDownY / world.SCALE);
            Iterator<Entity> looper = this.world.objectDatabase.getForwardTeamIterator(this.team);
            while(looper.hasNext()) {
                Entity teamEntity = looper.next();
                if (teamEntity.shape.body.contains(clickPos)) {
                    teamEntity.interact();
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        velocityX = velocityX / maxFlingVelocity;
        velocityY = velocityY / maxFlingVelocity;

        if (holdingGhost && velocityX > 0.5 || velocityY > 0.5) {
            holdingGhost = false;
            this.ghost.removeGhost();
            this.ghost = null;
        } else if (!holdingGhost) {

        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

}
