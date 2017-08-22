package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ne.revival_games.entity.MainActivity;

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

    public MainMenu(final Context context, final MainActivity activity) {
        super(context);
        this.activity = activity;

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
        for (final GameMode mode: GameMode.values()) {

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
            MainMenuButton button = new MainMenuButton(context, mode.toString(), listener);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            verticle.addView(button);
        }

        this.addView(verticle);
    }
}

class MainMenuButton extends Button {

    public MainMenuButton(Context context, String name, OnClickListener listener) {
        super(context);

        this.setTextColor(Color.YELLOW);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setText(name);
        this.setOnClickListener(listener);
    }

}

