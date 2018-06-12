package com.ne.revival_games.entity;

import android.os.Bundle;
import android.view.View;

import com.ne.revival_games.entity.CustomViews.MenuFactory;
import com.ne.revival_games.entity.Modes.GameMode;

/**
 * Created by Veganova on 7/26/2017.
 */

public class MainMenuActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View menu = new MenuFactory(GameMode.class)
                .addButton(GameMode.SINGLEPLAYER)
                .addButton(GameMode.MULTIPLAYER)
                .addButton(GameMode.TUTORIAL).build(getApplicationContext(), this, MainActivity.class);
        relativeLayout.addView(menu);
    }
}
