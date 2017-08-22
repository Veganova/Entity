package com.ne.revival_games.entity;

import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by Veganova on 7/26/2017.
 */

public class MainMenuActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print("Menu ");
        super.onCreate(savedInstanceState);
        //set screen to fullscreen
        this.initPlayers(true, 1);
        this.addMenu();


//        this.setContentView(R.layout.scrolls);
    }

    @Override
    public void onDestroy() {
        System.out.print("Menu ");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        System.out.print("Menu ");
        super.onResume();
    }

    @Override
    public void onPause() {
        System.out.print("Menu ");
        super.onPause();
    }

    @Override
    public void onStop() {
        System.out.print("Menu ");
        super.onStop();
    }

    @Override
    public void finish() {
        System.out.print("Menu ");
        super.finish();
    }
}
