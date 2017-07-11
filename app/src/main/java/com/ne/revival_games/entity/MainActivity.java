package com.ne.revival_games.entity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Player;

import org.dyn4j.dynamics.World;

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

        setContentView(view);
//        addContentView(new GamePanel(this));
    }


}
