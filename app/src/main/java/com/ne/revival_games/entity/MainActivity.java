package com.ne.revival_games.entity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;


import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.CustomViews.MainMenu;
import com.ne.revival_games.entity.CustomViews.MoneyView;
import com.ne.revival_games.entity.CustomViews.PlayPauseButton;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Players.PlayerDefense;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private PlayerDefense player1, player2, curPlayer;
    private MyWorld world;

    public MainThread myThread;
    public float SCREEN_WIDTH;
    public float SCREEN_HEIGHT;
    public float MAP_WIDTH;
    public float MAP_HEIGHT;

    protected RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("CREATING------------------------------------------------------");
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


        setContentView(R.layout.activity_main_thread);
        relativeLayout = (RelativeLayout)this.findViewById(R.id.main);

        screens = new HashMap<>();

        Serializable message = getIntent().getSerializableExtra("GameMode");
        if (message != null) {
            // case where the actual game has been started (when it is null we are still in the main menu)i
            System.out.println("GAME " + message.toString());
            MainMenu.GameMode choice = (MainMenu.GameMode)message;
            switch(choice) {
                case SINGLEPLAYER:
//                    initOnePlayer();
                    initPlayers(true, 1);
                    break;

                case MULTIPLAYER:
//                    initTwoPlayer(true);
                    initPlayers(true, 2);
                    break;
            }
        }

    }

    protected View menu;
    protected void addMenu(){
        menu = new MainMenu(getApplicationContext(), this);
        relativeLayout.addView(menu);
    }

    private View playPauseButton;
    private void addPlayPause(){
        playPauseButton = new PlayPauseButton(getApplicationContext(), myThread, this);
        relativeLayout.addView(playPauseButton);
    }

    private void removeSavedView(View view) {
        // If not null, then it must have been set. Look at above methods.
        if (view != null) {
            relativeLayout.removeView(view);
        }
    }


    public void initPlayers(boolean playerSelection, int numPlayers) {

        if (numPlayers == 1) {
            this.MAP_HEIGHT = 2400;
            this.MAP_WIDTH = 1350;
        } else {
            this.MAP_HEIGHT = 1600;
            this.MAP_WIDTH = 1800;
        }

        Team curTeam = Team.DEFENCE;
        ArrayList<Player> players = new ArrayList<>();
        ViewGroup myGroup = new DoubleScreen(this);
        for (int i = 0; i < numPlayers; i += 1) {
            GamePanel panel = new GamePanel(this, world);

            DoubleScreen.LayoutParams parms = new DoubleScreen.LayoutParams(SCREEN_WIDTH,
                    SCREEN_HEIGHT / numPlayers);

            if (i != 0) {
                // first player doesnt want padding at top
                parms.topMargin = (int) (i * SCREEN_HEIGHT / numPlayers + 5);

            } else {
                parms.topMargin = (int) (i * SCREEN_HEIGHT / numPlayers);
            }
            if (i != numPlayers - 1) {
                // last player doesnt want padding at bottom
                parms.bottomMargin = 5;
            }

            panel.setLayoutParams(parms);
            myGroup.addView(panel);

            PlayerDefense player = new PlayerDefense(i + 1, curTeam, world, panel, this, true);
            curTeam = curTeam.getOpposite();
            players.add(player);

            screens.put(panel, panel.getHolder());
//            myThread.addNewPanel(panel, panel.getHolder());
        }


        relativeLayout.addView(myGroup);

        if (playerSelection) {
            // loop over all players and do this..
            for (Player player: players) {
                relativeLayout.addView(player.getMenu());
                relativeLayout.addView(new MoneyView(this, getApplicationContext(), world, player));
            }
        }

        world.addPlayers(players);

        this.addPlayPause();

//        myThread.setRunning(true);
//        myThread.start();
    }

    public void initOnePlayer(boolean playerSelection){

        this.MAP_HEIGHT = 2400;
        this.MAP_WIDTH = 1350;

        GamePanel gamePanel1 = new GamePanel(this, world);

        player1 = new PlayerDefense(1, Team.DEFENCE, world, gamePanel1, this, true);
        player2 = new PlayerDefense(2, Team.OFFENSE, world, gamePanel1, this, false);

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

        relativeLayout.addView(myGroup);

        if (playerSelection) {
            // loop over all players and do this..
            for (Player player: players) {
                relativeLayout.addView(player.getMenu());
            }
        }

//        this.addMenu();
        this.addPlayPause();

        myThread.addNewPanel(gamePanel1, gamePanel1.getHolder());
        myThread.setRunning(true);
        myThread.start();
    }


    public void initTwoPlayer(boolean playerSelection){
        this.MAP_HEIGHT = 1600;
        this.MAP_WIDTH = 1800;

        GamePanel gamePanel1 = new GamePanel(this, world);
        GamePanel gamePanel2 = new GamePanel(this, world);

        Player player1 = new PlayerDefense(1, Team.DEFENCE, world, gamePanel1, this, true);
        Player player2 = new PlayerDefense(2, Team.OFFENSE, world, gamePanel2, this, true);

        Player curPlayer = player1;
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        world.addPlayers(players);

        ViewGroup myGroup = new DoubleScreen(this);

        DoubleScreen.LayoutParams parms =
                new DoubleScreen.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT / 2);
        parms.topMargin = 0;
        gamePanel1.setLayoutParams(parms);

        DoubleScreen.LayoutParams parms2 =
                new DoubleScreen.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT / 2);
        parms2.topMargin = (int) (SCREEN_HEIGHT / 2);
        gamePanel2.setLayoutParams(parms2);

        myGroup.addView(gamePanel2);
        myGroup.addView(gamePanel1);
//        setContentView(myGroup);


        relativeLayout.addView(myGroup);

        if (playerSelection) {
            // loop over all players and do this..
            for (Player player: players) {
                relativeLayout.addView(player.getMenu());
            }
        }

        this.addPlayPause();


        myThread.addNewPanel(gamePanel1, gamePanel1.getHolder());
        myThread.addNewPanel(gamePanel2, gamePanel2.getHolder());
        myThread.setRunning(true);
        myThread.start();
    }

    //similar logic to be used for end game, should also implement an 'onPause' etc.
    @Override
    public void onDestroy() {
        System.out.println("DESTROYING------------------------------------------------------");
//        boolean retry = true;
//        int counter = 0;
//        while (retry && counter < 1000) {
//            counter++;
//            try {
//                myThread.end();
//                myThread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
        super.onDestroy();
    }



    @Override
    public void onResume() {
        System.out.println("RESUMING------------------------------------------------------");
        super.onResume();
        if (this.myThread != null) {


            myThread = new MainThread(world);
            myThread.setScreens(screens);
            myThread.start();
//            synchronized (Thread.currentThread()) {
//                Thread.currentThread().notify();
//            }
        }
    }

    private HashMap<GamePanel, SurfaceHolder> screens;

    @Override
    public void onPause() {
        System.out.println("PAUSING------------------------------------------------------");

        super.onPause();
        if (this.myThread != null) {
            screens = myThread.getScreens();
            this.myThread.end();
        }
//        if (this.myThread != null) {
//            try {
//                this.myThread.pause(Thread.currentThread());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onStop() {
        System.out.println("STOPPING------------------------------------------------------");
        super.onStop();
        if (this.myThread != null) {
            this.myThread.end();
        }
    }

    @Override
    public void finish() {
        System.out.println("FINISHING------------------------------------------------------");
//        this.onStop();
        super.finish();

        if (this.myThread != null) {
            this.myThread.end();
        }

        relativeLayout.removeAllViews();
    }


}
