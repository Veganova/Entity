package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.R;

/**
 * Created by Veganova on 7/23/2017.
 */

public class MainMenu extends LinearLayout {

    private MainActivity activity;

    public enum GameMode {
        SINGLEPLAYER("Single Player"), MULTIPLAYER("Multiplayer");
        private String val;

        GameMode(String s) {
            this.val = s;
        }

        @Override
        public String toString() {
            return this.val;
        }
    }


//    private int HEIGHT;
    public MainMenu(final Context context, final MainActivity activity, float SCREEN_WIDTH) {
        super(context);
        this.activity = activity;

//        HEIGHT = (int) (activity.MAP_HEIGHT * 0.06);

        this.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        this.setGravity(Gravity.CENTER);
        this.setOrientation(HORIZONTAL);


        LinearLayout verticle = new LinearLayout(context);
        verticle.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        verticle.setGravity(Gravity.CENTER);
        verticle.setOrientation(VERTICAL);


        boolean current = false;
        boolean leftSide = true;
        for (final GameMode mode : GameMode.values()) {

            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("GameMode", mode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    // wait for the current activity thread to end. (so that it will let go of the canvas lock)
                    activity.myThread.end();
                    if (activity.myThread.hasEnded()) {

                    } else {
                        try {
                            synchronized (activity.myThread) {
                                activity.myThread.wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.startActivity(intent);
                }
            };

            LinearLayout container = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 30;
            container.setLayoutParams(params);


//            container.setBackgroundColor(GamePanel.cream);
            if (leftSide) {
                MainMenuButton button = new MainMenuButton(context, mode.toString(), listener, SCREEN_WIDTH, 0.2f, 0.085f);
                container.addView(button);
                verticle.addView(new ArrowPop(context, container, false, true, ArrowPop.SIDE.LEFT, SCREEN_WIDTH));
            } else {
                MainMenuButton button = new MainMenuButton(context, mode.toString(), listener, SCREEN_WIDTH, 0.085f, 0.2f);
                container.addView(button);
                verticle.addView(new ArrowPop(context, container, false, true, ArrowPop.SIDE.RIGHT, SCREEN_WIDTH));
            }

            leftSide = !leftSide;
        }

        this.addView(verticle);
    }

    private class MainMenuButton extends android.support.v7.widget.AppCompatTextView {

        public MainMenuButton(Context context, String name, OnClickListener listener, float SCREEN_WIDTH, float padding_left, float padding_right) {
            super(context);

            this.setTextColor(GamePanel.background_dark);

            this.setTextSize(25);
            this.setGravity(Gravity.CENTER);
            this.setBackgroundColor(GamePanel.cream);
            this.setText(name);
            this.setOnClickListener(listener);


            int pl = (int) (SCREEN_WIDTH * padding_left);
            int pr = (int) (SCREEN_WIDTH * padding_right);
            this.setPadding(pl, 20, pr, 20);

            this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        }

    }

}


