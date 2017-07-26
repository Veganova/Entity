package com.ne.revival_games.entity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
        this.setOrientation(VERTICAL);

        final Intent intent = new Intent(context, MainActivity.class);
        boolean current = false;
        for (final GameMode mode: GameMode.values()) {

            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    try {
//                        activity.myThread.endThread();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    intent.putExtra("GameMode", mode);
                    activity.myThread.end();
//                        activity.myThread.pause(Thread.currentThread());
                    //                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            };
            MainMenuButton button = new MainMenuButton(context, mode.toString(), listener);
            this.addView(button);
        }
    }
}

class MainMenuButton extends Button {

    public MainMenuButton(Context context, String name, OnClickListener listener) {
        super(context);

        this.setTextColor(Color.YELLOW);
        this.setText(name);
        this.setOnClickListener(listener);
    }

}

