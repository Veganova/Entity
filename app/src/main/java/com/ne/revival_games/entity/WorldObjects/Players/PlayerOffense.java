package com.ne.revival_games.entity.WorldObjects.Players;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.ne.revival_games.entity.CustomViews.Screen;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;

import java.util.Iterator;

/**
 * Created by vishn on 7/27/2017.
 */

public class PlayerOffense extends Player {
    protected boolean settingPower = false;
    protected double percentSpeed = 0;

    public PlayerOffense(int id, Team team, MyWorld world, Screen screen, MainActivity activity, boolean addListenertoPanel) {
        super(id, team, world, screen, activity, addListenertoPanel);
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        return false;
    }


    @Override
    public void update() {
        super.update();

        percentSpeed = (percentSpeed + 0.01) % 1;
        if(this.addToWorld != null)
            try{
                pullTowards = new Vector2(addToWorld.getDouble("x")/MyWorld.SCALE, addToWorld.getDouble("y")/MyWorld.SCALE);
                this.ghost = GhostFactory.produce(
                        (EntityLeaf)addToWorld.get("type"),
                        addToWorld.getDouble("x"),
                        addToWorld.getDouble("y"),
                        0,
                        world,
                        team,
                        "");
                this.addToWorld = null;
            }
            catch (Exception e){
                e.printStackTrace();
            }

        if (holdingGhost && ghost != null && !settingPower) {
            Vector2 delta = new Vector2(pullTowards.x - ghost.entity.shape.getX(),
                    pullTowards.y - ghost.entity.shape.getY());
            ghost.setLinearVelocity(10 * delta.x, 10 * delta.y);
        }

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(settingPower) {
        float maxFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        velocityX = velocityX / maxFlingVelocity;
        velocityY = velocityY / maxFlingVelocity;

        if (readytoPlace(e1) && settingPower && (velocityX > 0.5 || velocityY > 0.5)) {
            this.ghost.place(this);
            this.ghost.entity.setVelocity(ghost.entity.getMaxVelocity(percentSpeed));
            this.ghost = null;
            holdingGhost = false;
            settingPower = false;
        } else if (!holdingGhost) {
            if (velocityX > 0.3 || velocityY > 0.3) {

            }
        }
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) { //ex what if something comes into the zone!!!
        if (readytoPlace(e)) {
            this.ghost.place(this);
            this.ghost = null;
            holdingGhost = false;
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
}
