package com.ne.revival_games.entity;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.ne.revival_games.entity.TouchListeners.SwipeListener;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.TouchListeners.MultiTouchListener;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Player;

import org.dyn4j.geometry.Vector2;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set screen to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        GamePanel view = new GamePanel(this);
        MyWorld world = view.world;
        Player player1 = new Player(1, Team.DEFENCE, world, view.scales, 0, 800);
        view.addPlayerListener(player1);
        Player player2 = new Player(2, Team.OFFENSE, world, view.scales, -800, 0);
        view.addPlayerListener(player2);
        view.addListeners(new MultiTouchListener(new Vector2(0,0), new Vector2(1000,1000)));
        view.addListeners(new SwipeListener(this));
        setContentView(view);
//        addContentView(new GamePanel(this));
    }

    public Context returnContext(){
        return this.getApplicationContext();
    }


}
