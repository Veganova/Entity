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
       

        Button play = new Button(context);
        play.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        play.setText("||");
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PAUSE");
                synchronized (game) {
                    try {
                        game.pause(Thread.currentThread());
                        System.out.println("ALIVE " + game.isAlive());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        verticle.addView(play);

        Button pause = new Button(context);
        pause.setText(">");
        pause.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("PLAY");
                synchronized (Thread.currentThread()) {
                    System.out.println("NOTIFYING");
                    Thread.currentThread().notify();
//                    game.run();

                    System.out.println("ALIVE " + game.isAlive());
                }
            }
        });
        verticle.addView(pause);

        Button end = new Button(context);
        end.setText("X");
        end.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        end.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("END");
                game.end();
                try {
                    game.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("ALIVE " + game.isAlive());
            }
        });
        verticle.addView(end);


        final Intent intent = new Intent(context, MainMenuActivity.class);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                game.end(); // thread
                activity.finish();
//                        activity.myThread.pause(Thread.currentThread());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
        Button button = new Button(context);
        button.setText("<");
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(listener);
        verticle.addView(button);

        horizontal.addView(verticle);
        this.addView(horizontal);
    }
}
