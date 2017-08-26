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
    public MainMenu(final Context context, final MainActivity activity) {
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
//                    activity.myThread.end();
//                    try {
//                        activity.myThread.join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    activity.onStop();
//                    try {
//                        activity.myThread.pause(Thread.currentThread());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

//                        activity.myThread.pause(Thread.currentThread());
                    //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                    // wait for the current activity thread to end. (so that it will let go of the canvas lock)
                    activity.myThread.end();
                    try {
                        synchronized (activity.myThread) {
                            activity.myThread.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activity.startActivity(intent);
                }
            };


            LinearLayout container = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 30;
            container.setLayoutParams(params);
            container.setGravity(Gravity.CENTER);

            LinearLayout left = new LinearLayout(context);
            left.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT,
                    1.0f));
            if (leftSide) {
                left.setBackgroundColor(GamePanel.background_dark);

                ImageView style = new ImageView(context);
                style.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                style.setAdjustViewBounds(true);
//                style.setScaleType(ScaleType.FIT_XY);

                Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_down_arrow_reversed, null);
                style.setImageDrawable(drawable);
                left.setGravity(Gravity.RIGHT);
                left.addView(style);
            }


            LinearLayout right = new LinearLayout(context);
            right.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT,
                    1.0f));
            if (!leftSide) {
                right.setBackgroundColor(GamePanel.background_dark);

                ImageView style = new ImageView(context);
                style.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                style.setAdjustViewBounds(true);
//                style.setScaleType(ScaleType.FIT_XY);

                Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_down_arrow, null);
                style.setImageDrawable(drawable);
                right.setGravity(Gravity.LEFT);
                right.addView(style);
            }

            MainMenuButton button = new MainMenuButton(context, mode.toString(), listener);

            container.setBackgroundColor(GamePanel.cream);

            container.addView(left);
            container.addView(button);
            container.addView(right);


            verticle.addView(container);

            leftSide = !leftSide;
        }

        this.addView(verticle);
    }

    private class MainMenuButton extends TextView {

        public MainMenuButton(Context context, String name, OnClickListener listener) {
            super(context);

            this.setTextColor(GamePanel.background_dark);

            this.setTextSize(25);
            this.setGravity(Gravity.CENTER);
            this.setBackgroundColor(Color.TRANSPARENT);
            this.setText(name);
            this.setOnClickListener(listener);

            this.setPadding(0, 20, 0, 20);

            this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        }

    }

}


