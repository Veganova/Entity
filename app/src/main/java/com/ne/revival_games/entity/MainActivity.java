package com.ne.revival_games.entity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;


import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.WorldObjects.Entity.Entities;

import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Player;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Player player1, player2, curPlayer;
    private MyWorld world;

    private MainThread myThread;
    public float SCREEN_WIDTH;
    public float SCREEN_HEIGHT;
    public float MAP_WIDTH;
    public float MAP_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set screen to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        world = new MyWorld();
        myThread = new MainThread(this.world);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        SCREEN_WIDTH = displaymetrics.widthPixels;
        SCREEN_HEIGHT = displaymetrics.heightPixels;

        initTwoPlayer();
//        initOnePlayer();
    }

    public void initOnePlayer(){
        this.MAP_HEIGHT = 2400;
        this.MAP_WIDTH = 1350;

        GamePanel gamePanel1 = new GamePanel(this, world);

        player1 = new Player(1, Team.DEFENCE, world, gamePanel1, this, true);
        player2 = new Player(2, Team.OFFENSE, world, gamePanel1, this, false);

        curPlayer = player1;
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        world.addPlayers(players);

        ViewGroup myGroup = new DoubleScreen(this);

        DoubleScreen.LayoutParams parms = new DoubleScreen.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT);
        gamePanel1.setLayoutParams(parms);

        myGroup.addView(gamePanel1);
       // setContentView(myGroup);

        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        setContentView(R.layout.activity_main_thread);
        RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(R.id.main);
        relativeLayout.addView(myGroup);

        // loop over all players and do this..
        relativeLayout.addView(player1.getMenu());
        relativeLayout.addView(player2.getMenu());

        myThread.addNewPanel(gamePanel1, gamePanel1.getHolder());
        myThread.setRunning(true);
        myThread.start();
    }

    public void initTwoPlayer(){
        this.MAP_HEIGHT = 1600;
        this.MAP_WIDTH = 1800;

        GamePanel gamePanel1 = new GamePanel(this, world);
        GamePanel gamePanel2 = new GamePanel(this, world);

        player1 = new Player(1, Team.DEFENCE, world, gamePanel1, this, true);
        player2 = new Player(2, Team.OFFENSE, world, gamePanel2, this, true);

        curPlayer = player1;
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        world.addPlayers(players);

        ViewGroup myGroup = new DoubleScreen(this);


        DoubleScreen.LayoutParams parms =
                new DoubleScreen.LayoutParams(SCREEN_WIDTH,SCREEN_HEIGHT/2);
        parms.topMargin = 10;
        gamePanel1.setLayoutParams(parms);

        DoubleScreen.LayoutParams parms2 =
                new DoubleScreen.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT/2);
        parms2.topMargin = (int) (SCREEN_HEIGHT/2 + 50);
        gamePanel2.setLayoutParams(parms2);

        myGroup.addView(gamePanel2);
        myGroup.addView(gamePanel1);
//        setContentView(myGroup);


        setContentView(R.layout.activity_main_thread);
        RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(R.id.main);
        relativeLayout.addView(myGroup);

        // loop over all players and do this..
        relativeLayout.addView(player1.getMenu());
        relativeLayout.addView(player2.getMenu());
        relativeLayout.addView(new PlayPauseButton(getApplicationContext(), myThread));

        myThread.addNewPanel(gamePanel1, gamePanel1.getHolder());
        myThread.addNewPanel(gamePanel2, gamePanel2.getHolder());
        myThread.setRunning(true);
        myThread.start();
    }

    //similar logic to be used for end game, should also implement an 'onPause' etc.
    @Override
    public void onDestroy(){
            boolean retry = true;
            int counter = 0;
            while (retry && counter < 1000) {
                counter++;
                try {
                    myThread.setRunning(false);
                    myThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        super.onDestroy();
    }


}
