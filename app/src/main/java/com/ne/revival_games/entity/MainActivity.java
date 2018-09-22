package com.ne.revival_games.entity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;


import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.CustomViews.GestureView;
import com.ne.revival_games.entity.CustomViews.LoadingBar;
import com.ne.revival_games.entity.CustomViews.MenuFactory;
import com.ne.revival_games.entity.CustomViews.MoneyPopUp;
import com.ne.revival_games.entity.CustomViews.PlayPauseArea;
import com.ne.revival_games.entity.CustomViews.RestartHome;
import com.ne.revival_games.entity.CustomViews.Screen;
import com.ne.revival_games.entity.CustomViews.TutorialPrompt;
import com.ne.revival_games.entity.Modes.GameMode;
import com.ne.revival_games.entity.Modes.TutorialMode;
import com.ne.revival_games.entity.TouchListeners.GestureCallback;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Players.PlayerDefense;
import com.ne.revival_games.entity.WorldObjects.Sounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
            this.initPlayers(false, false, 1, false);
        }

        this.addLoadingBar();
        this.addMoneyPopUp();
        world.addUpdatable(loadingBar);

        world.initializeWorld(loadingBar, moneyPopUp);
    }

    private void initGameMode(GameMode gameChoice) {
        switch (gameChoice) {
            case SINGLEPLAYER:
                initPlayers(true, true, 1, false);
                Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.MODE);
                world.setInitializeType("single_player");
                break;

            case MULTIPLAYER:
                initPlayers(true, true, 2, false);
                Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.MODE);
                world.setInitializeType("single_player");
                break;
            case TUTORIAL:
//                    initTutorial();
//                    initPlayers(false, false, 1);
                this.initPlayers(false, false, 1, false);
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
            case BASICS:
                this.initPlayers(true, false, 1, true);


                final GestureView gestureView = new GestureView(this, R.raw.swipe, true);
                final RelativeLayout gestureContainer = new RelativeLayout(this);
                gestureContainer.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                gestureContainer.setGravity(Gravity.CENTER);
                gestureContainer.addView(gestureView);
                this.relativeLayout.addView(gestureContainer);

                RelativeLayout gestureTextContainer = new RelativeLayout(this);
                gestureTextContainer.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                gestureTextContainer.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                gestureTextContainer.setPadding(50 , 0, 50, 220);

                final TutorialPrompt gestureExplanation = new TutorialPrompt(this, "Swipe to move around the world");
                gestureTextContainer.addView(gestureExplanation);
                this.relativeLayout.addView(gestureTextContainer);

                final Player player = world.getPlayers().get(0);
                player.addMoney(9999);

                player.getMenu().getListing().getTray().setToggleListener(new GestureCallback() {
                    @Override
                    public boolean apply() {
                        return false;
                    }
                });

                player.addOnMoveListener(new GestureCallback() {
                    int timesToMove = 5;
                    @Override
                    public boolean apply() {
                        timesToMove--;
                        if (timesToMove <= 0) {
                            gestureView.pauseAnimation();
                            gestureView.setAnimation(R.raw.zoom_out);
                            gestureView.playAnimation();
                            gestureExplanation.setText("Pinch to control zoom");
                            player.addOnMoveListener(null);
                            player.addOnScaleListener(new GestureCallback() {
                                boolean scaledYet = false;
                                @Override
                                public boolean apply() {
                                    if (!scaledYet) {
                                        scaledYet = true;
                                        gestureExplanation.setText("Tap such triangles to open and close elements");

                                        int[] location = new int[2];
                                        player.getMenu().getListing().getTray().getLocationInWindow(location);
                                        gestureView.setX(location[0]);
                                        gestureView.setY(location[1]);
                                        gestureView.setAnimation(R.raw.tap);
                                        gestureView.playAnimation();
                                        player.addOnScaleListener(null);
                                        player.getMenu().getListing().getTray().setToggleListener(new GestureCallback() {
                                            @Override
                                            public boolean apply() {
                                                gestureView.pauseAnimation();
                                                gestureContainer.removeAllViews();
                                                gestureExplanation.setText("This is the unit-selection menu. Tap to a unit to select, then drag the silhouette over to your desired location and tap again to place it in the world");
                                                player.getMenu().getListing().getTray().setToggleListener(null);
                                                player.setOnGhostPlace(new GestureCallback() {
                                                    @Override
                                                    public boolean apply() {
                                                        gestureExplanation.setText("Well done! PS: you can scroll horizontally in the menu as well as vertically on certain types of units such as turrets.");
                                                        player.setOnGhostPlace(null);
                                                        return true;
                                                    }
                                                });
                                                return true;
                                            }
                                        });
                                        return true;
                                    }
                                    return true;
                                }
                            });
                        }
                        return true;
                    }
                } );



                world.setInitializeType("gesture");
                break;
            case NEXUSTUTORIAL:
                this.initPlayers(false, true, 1, false);
                world.setInitializeType("tutorial-nexus");
                restartHome.pop();

                RelativeLayout container = new RelativeLayout(this);
                container.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
                container.setGravity(Gravity.BOTTOM);
                container.setPadding(50 , 0, 50, 50);

                final TutorialPrompt b = new TutorialPrompt(this, Nexus.getDescription().get(0));
                b.setOnClickChain(Nexus.getDescription(), 1);
                container.addView(b);


                this.relativeLayout.addView(container);

                break;
            case TURRETTUTORIAL:
                System.out.println("TurretTtoirai");
                break;
        }
    }

    private LoadingBar loadingBar;
    private void addLoadingBar() {
        this.loadingBar = new LoadingBar(this);
        this.relativeLayout.addView(loadingBar);
    }


    private MoneyPopUp moneyPopUp;
    private void addMoneyPopUp() {
        this.moneyPopUp = new MoneyPopUp(this);
        this.relativeLayout.addView(moneyPopUp);
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
     * @param startHidden       If UI elements are to be present, start them off hidden
     */
    public void  initPlayers(boolean playerSelection, boolean playPause, int numPlayers, boolean startHidden) {

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
                player.addMenu((int)SCREEN_HEIGHT, SCREEN_WIDTH, startHidden);
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
