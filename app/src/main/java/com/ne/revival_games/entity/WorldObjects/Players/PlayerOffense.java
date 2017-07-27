package com.ne.revival_games.entity.WorldObjects.Players;

import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Created by vishn on 7/27/2017.
 */

public class PlayerOffense extends Player {

    public PlayerOffense(int id, Team team, MyWorld world, GamePanel gamePanel, MainActivity activity, boolean addListenertoPanel) {
        super(id, team, world, gamePanel, activity, addListenertoPanel);
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        return false;
    }


    @Override
    public void update() {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
