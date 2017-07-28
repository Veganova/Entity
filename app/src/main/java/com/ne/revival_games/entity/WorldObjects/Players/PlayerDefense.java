package com.ne.revival_games.entity.WorldObjects.Players;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/27/2017.
 */

public class PlayerDefense extends Player {
    public PlayerDefense(int id, Team team, MyWorld world, GamePanel gamePanel, MainActivity activity, boolean addListenertoPanel) {
        super(id, team, world, gamePanel, activity, addListenertoPanel);
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
        } else if (mask == MotionEvent.ACTION_UP || mask == MotionEvent.ACTION_POINTER_UP) {
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
    public void update() {
        for(int x = 0; x < entitiestoAdd.size(); x++) {
            entitiestoAdd.get(x).place().addToTeam(this.team);
        }
        entitiestoAdd.clear();

        if (holdingGhost && ghost.entity != null) {
            Vector2 delta = new Vector2(pullTowards.x - ghost.entity.shape.getX(),
                    pullTowards.y - ghost.entity.shape.getY());

            ghost.setLinearVelocity(10 * delta.x, 10 * delta.y);
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
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

        if (holdingGhost) {
            if (lastDownPress + 30 < System.currentTimeMillis()) {
                if (e2.getPointerCount() < 2 && e1.getPointerCount() < 2) {
                    pullTowards = new Vector2(mDownX / world.SCALE, mDownY / world.SCALE);
                }
            }
            if (camera.nearEdge(e2.getX(), e2.getY(), marginDetection)) {
                camera.relativeMove(realDownx, realDowny);
            }
            return false;
        } else if (lastDownPress + 30 < System.currentTimeMillis()) {
            camera.relativeMove(scrollSpeed * (int)(distanceX) / MyWorld.SCALE, -1 * scrollSpeed * (int)(distanceY) / MyWorld.SCALE);
        }


        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (lastMultiPress + 200 < System.currentTimeMillis() && e.getPointerCount() < 2
                && holdingGhost && this.ghost.canPlace()) {
            entitiestoAdd.add(ghost);
            this.ghost = null;
            holdingGhost = false;
        } else if (!holdingGhost) {
            Vector2 clickPos = new Vector2(mDownX / world.SCALE, mDownY / world.SCALE);
            for (Entity teamEntity : this.entities) {
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
//        float maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
//        velocityX = velocityX / maxFlingVelocity;
//        velocityY = velocityY / maxFlingVelocity;
//
//        if (holdingGhost && velocityX > 0.5 || velocityY > 0.5) {
//            holdingGhost = false;
//            this.ghost.removeGhost();
//            this.ghost = null;
//        } else if (!holdingGhost) {
//            if (velocityX > 0.3 || velocityY > 0.3) {
//
//            }
//        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

}
