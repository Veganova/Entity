package com.ne.revival_games.entity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Veganova on 7/25/2017.
 */

public class PlayPauseButton extends LinearLayout {


    private final Thread game;


    public PlayPauseButton(Context context, final MainThread game) {
        super(context);
        this.game = game;

        this.setGravity(Gravity.RIGHT);

//        LinearLayout padding = new LinearLayout(context);
//        padding.setOrientation(HORIZONTAL);
//        float weight = 3.0f;
//        padding.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, weight));
//        this.addView(padding);
//
        this.setPadding(700, 0, 0, 0);
        float weight = 1.0f;


        Button play = new Button(context);
        play.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, weight));
        play.setText("||");
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PAUSE");
                synchronized (game) {
                    try {
                        game.pause(Thread.currentThread());
                        System.out.println("ALIVE " + game.isAlive() );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.addView(play);

        Button pause = new Button(context);
        pause.setText(">");
        pause.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, weight));
        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PLAY");
                synchronized (Thread.currentThread()) {
                    System.out.println("NOTIFYING");
                    Thread.currentThread().notify();
//                    game.run();

                    System.out.println("ALIVE " + game.isAlive() );
                }
            }
        });
        this.addView(pause);

        Button end = new Button(context);
        end.setText("X");
        end.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, weight));
        end.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("END");
                game.end();

                System.out.println("ALIVE " + game.isAlive() );
            }
        });
        this.addView(end);
    }
}
