package com.ne.revival_games.entity;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.Window;
import android.view.WindowManager;

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

//        view.addListeners(new TwoTouchListener(new Vector2(0,0), new Vector2(1000,1000)));
        view.addListeners(new BlankStateDetector(this));
        setContentView(view);
//        addContentView(new GamePanel(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_thread, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.P1:
                this.curPlayer = player1;
                return true;
            case R.id.P2:
                this.curPlayer = player2;
                return true;
            case R.id.barrier:
                this.curPlayer.setGhost(Entities.BARRIER);
                return true;
            case R.id.comet:
                this.curPlayer.setGhost(Entities.COMET);
                return true;
            case R.id.nexus:
                this.curPlayer.setGhost(Entities.NEXUS);
                return true;
            case R.id.turret:
                this.curPlayer.setGhost(Entities.TURRET);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
