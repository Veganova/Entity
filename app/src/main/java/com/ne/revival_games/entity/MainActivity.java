package com.ne.revival_games.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;


import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ne.revival_games.entity.CustomViews.GestureView;
import com.ne.revival_games.entity.CustomViews.MenuFactory;
import com.ne.revival_games.entity.CustomViews.PlayPauseArea;
import com.ne.revival_games.entity.CustomViews.RestartHome;
import com.ne.revival_games.entity.CustomViews.Screen;
import com.ne.revival_games.entity.Modes.BaseMode;
import com.ne.revival_games.entity.Modes.GameMode;
import com.ne.revival_games.entity.Modes.TutorialMode;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Players.PlayerDefense;
import com.ne.revival_games.entity.WorldObjects.Sounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private PlayerDefense player1, player2, curPlayer;
    protected MyWorld world;

    public MainThread myThread;
    public float SCREEN_WIDTH;
    public float SCREEN_HEIGHT;
    public float MAP_WIDTH;
    public float MAP_HEIGHT;

    protected RelativeLayout relativeLayout;
    private boolean paused = true;
    private static Context myContext = null;

    private Intent intent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("CREATING------------------------------------------------------");
        super.onCreate(savedInstanceState);

        //whenever a new mainactivity is created we reinitialize context
        myContext = getApplicationContext();
        if (myContext == null) {
            throw new NullPointerException("LOMAOL");
        }
        //set screen to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        SCREEN_WIDTH = displaymetrics.widthPixels;
        SCREEN_HEIGHT = displaymetrics.heightPixels;

        setContentView(R.layout.activity_main_thread);
        relativeLayout = (RelativeLayout)this.findViewById(R.id.main);
        world = new MyWorld(this);
        myThread = new MainThread(this.world);
        screens = new HashMap<>();

        // Initialize sounds
        Sounds.getInstance(this);

        this.intent = getIntent();

        Serializable menuType = intent.getSerializableExtra("enumType");

            // case where the actual game has been started (when it is null we are still in the main menu).
        if (menuType != null) {
            String enumTypeClass = (String) menuType;

            if (enumTypeClass.equals(GameMode.class.getSimpleName())) {
                GameMode gameChoice = (GameMode) intent.getSerializableExtra("enumVal");
                this.initGameMode(gameChoice);
            } else if(enumTypeClass.equals(TutorialMode.class.getSimpleName())) {
                TutorialMode tutorialChoice = (TutorialMode) getIntent().getSerializableExtra("enumVal");
                this.initTutorial(tutorialChoice);
            }
        } else {
            // set fullscreen
            this.initPlayers(false, false, 1);
        }
        world.initializeWorld();
    }

    private void initGameMode(GameMode gameChoice) {
        switch (gameChoice) {
            case SINGLEPLAYER:
                initPlayers(true, true, 1);
                Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.MODE);
                world.setInitializeType("single_player");
                relativeLayout.addView(new GestureView(this));
                break;

            case MULTIPLAYER:
                initPlayers(true, true, 2);
                Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.MODE);
                world.setInitializeType("single_player");
                break;
            case TUTORIAL:
//                    initTutorial();
//                    initPlayers(false, false, 1);
                this.initPlayers(false, false, 1);
                addTutorialButtons();
// todo
//                    buttons here on screeen choose - spawn tutorial, game mechanics, each unit tutorial
//                        buttons will lead to TutorialActivity.java extends MainActivity and add more cases here for clicking on the buttons above.
                Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.MODE);
//                    world.setInitializeType("tutorial");
                break;
        }
    }

    private void initTutorial(TutorialMode tutorialChoice) {
        switch(tutorialChoice) {
            case NEXUSTUTORIAL:
                this.initPlayers(false, true, 1);
                world.setInitializeType("tutorial-nexus");
                restartHome.pop();

                RelativeLayout container = new RelativeLayout(this);
                container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                container.setGravity(Gravity.BOTTOM);
                container.setPadding(50 , 0, 50, 50);

                final Button b = new Button(this);
                b.setText(Nexus.getDescription().get(0));
                b.setTextColor(GamePanel.background_dark);
                b.setTextSize(15);
                b.setAllCaps(false);
                b.setBackground(ContextCompat.getDrawable(this, R.drawable.roundedbox));
                b.setPadding(10, 0, 10, 0);

                b.setOnClickListener(new View.OnClickListener() {
                    private int i = 0;
                    @Override
                    public void onClick(View v) {
                        i  = (i + 1) % Nexus.getDescription().size();
                        b.setText(Nexus.getDescription().get(i));
                    }
                });
                container.addView(b);


                this.relativeLayout.addView(container);

                break;
            case TURRETTUTORIAL:
                System.out.println("TurretTtoirai");
                break;
        }
    }

    private void addTutorialButtons() {
        MenuFactory factory = new MenuFactory(TutorialMode.class);
        for (TutorialMode tutorialMode: TutorialMode.values()) {
            factory.addButton(tutorialMode);
        }
        this.relativeLayout.addView(factory.build(getApplicationContext(), this));
    }

    private View playPauseButton;
    private void addPlayPause() {
        playPauseButton = new PlayPauseArea(getApplicationContext(), this);
        relativeLayout.addView(playPauseButton);
    }

    private RestartHome restartHome;
    private void addRestartHome(){
        restartHome = new RestartHome(getApplicationContext(), SCREEN_HEIGHT, this, this.intent, SCREEN_WIDTH);
        relativeLayout.addView(restartHome);
    }


    private void removeSavedView(View view) {
        // If not null, then it must have been set. Look at above methods.
        if (view != null) {
            relativeLayout.removeView(view);
        }
    }

    /**
     * Initializes screen based on the provided parameters. Configures the gamepanel.
     * @param playerSelection   Whether the menu and money and other interactive UI elements should be displayed or not
     * @param playPause         Whether the play pause button should be displayed. Also whether the RestartHome buttons should be displayed
     * @param numPlayers        Number of players that are playing in this round.
     */
    public void  initPlayers(boolean playerSelection, boolean playPause, int numPlayers) {

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
            Screen screen = new Screen(this, panel);

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

            screen.setLayoutParams(parms);
//            panel.setLayoutParams(parms);
            myGroup.addView(screen);

            PlayerDefense player = new PlayerDefense(i + 1, curTeam, world, screen, this, true);
            curTeam = curTeam.getOpposite();
            players.add(player);

            screens.put(panel, panel.getHolder());
//            myThread.addNewPanel(panel, panel.getHolder());
        }

        relativeLayout.addView(myGroup);

        if (playerSelection) {
            // loop over all players and do this..
            for (Player player: players) {
//                relativeLayout.addView(player.getMenu());
                player.addMenu((int)SCREEN_HEIGHT, SCREEN_WIDTH);
                player.addMoneyView(this, SCREEN_WIDTH);
//                relativeLayout.addView(new MoneyView(this, getApplicationContext(), world, player));
            }
        }

        world.addPlayers(players);

        if (playPause) {
            this.addPlayPause();
            this.addRestartHome();
        }


  }

    public void gameOver() {
//        this.restartHome.pop();
        System.out.println("GAME OVER!");
        Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.LOSE);
        if (restartHome != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    restartHome.pop();
                }
            });
        }
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
            // Probably has ended already but just to be sure..
            myThread.end();

            myThread = new MainThread(world);
            myThread.setScreens(screens);
            myThread.start();

            paused = false;
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
        paused = true;
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


    public boolean isPaused() {
        return paused;
    }


    public static Context giveContext() {
        return myContext;
    }
}
