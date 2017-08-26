package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.MainMenuActivity;
import com.ne.revival_games.entity.MainThread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Veganova on 7/25/2017.
 */

public class PlayPauseButton extends LinearLayout {


    private final Thread game;
    private final MainActivity activity;


    public PlayPauseButton(final Context context, final MainThread game, final MainActivity activity) {
        super(context);
        this.game = game;
        this.activity = activity;

        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.RIGHT);
        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));


        float weight;


//        LinearLayout padding = new LinearLayout(context);
//        padding.setOrientation(HORIZONTAL);
//        weight = 1.0f;
//        padding.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, weight));
//        this.addView(padding);

        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(VERTICAL);
        horizontal.setGravity(Gravity.CENTER);
        horizontal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));


        weight = 1.0f;
        LinearLayout verticle = new LinearLayout(context);
        verticle.setOrientation(VERTICAL);
        verticle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
       

//        Button play = new Button(context);
//        play.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//        play.setText("||");
//        play.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                System.out.println("PAUSE");
//                activity.onPause();
//
//        verticle.addView(play);

        Button playPause = new Button(context);
        playPause.setText(">");
        playPause.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        playPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isPaused()) {
                    activity.onResume();
                } else {
                    activity.onPause();
                }
            }
        });
        verticle.addView(playPause);

        horizontal.addView(verticle);
        this.addView(horizontal);
    }
}
