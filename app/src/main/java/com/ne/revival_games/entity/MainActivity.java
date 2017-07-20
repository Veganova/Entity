package com.ne.revival_games.entity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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

import com.ne.revival_games.entity.TouchListeners.BlankStateDetector;
import com.ne.revival_games.entity.WorldObjects.Entity.Entities;

import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Player;

public class MainActivity extends AppCompatActivity {

    private Player player1, player2, curPlayer;
    private MyWorld world;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set screen to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        GamePanel view = new GamePanel(this);
        world = view.world;
        player1 = new Player(1, Team.DEFENCE, world, view.scales, 0, 800, this);
        view.addPlayerListener(player1);
        player2 = new Player(2, Team.OFFENSE, world, view.scales, -800, 0, this);

        view.addPlayerListener(player2);
        curPlayer = player1;






        // ------------------------ good code
        setContentView(R.layout.activity_main_thread);
        RelativeLayout relativeLayout = (RelativeLayout)this.findViewById(R.id.main);
        relativeLayout.addView(view);

        LinearLayout linearLayout = new com.ne.revival_games.entity.Menu(getApplicationContext(), player1, 0);
        relativeLayout.addView(linearLayout);
        LinearLayout linearLayout2 = new com.ne.revival_games.entity.Menu(getApplicationContext(),
                player2, (int)GamePanel.HEIGHT - 200);
        relativeLayout.addView(linearLayout2);

    }

}
