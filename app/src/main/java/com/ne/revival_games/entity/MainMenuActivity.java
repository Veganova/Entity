package com.ne.revival_games.entity;

import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by Veganova on 7/26/2017.
 */

public class MainMenuActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set screen to fullscreen
        this.initPlayers(false, false, 1);
        this.addMenu();
        super.world.initializeWorld();
    }

}
