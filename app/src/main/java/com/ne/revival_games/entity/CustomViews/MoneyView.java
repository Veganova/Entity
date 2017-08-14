package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.R;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;
import com.ne.revival_games.entity.WorldObjects.Updatable;

import static android.R.color.holo_orange_light;

/**
 * Created by Veganova on 8/13/2017.
 */

public class MoneyView extends RelativeLayout {
    private MainActivity activity;
    private Player player;

    private Thread creator;
    public MoneyView(MainActivity activity, final Context context, final MyWorld world, final Player player) {
        super(context);
        this.activity = activity;
        this.player = player;

        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        this.setGravity(Gravity.TOP | Gravity.RIGHT);
        creator = new Thread(new Runnable() {
            MoneyTextView text;
            private boolean first = true;
            @Override
            public void run() {
                    if (first) {
                        text = new MoneyTextView(context, world, player);
                        addView(text);
                        first = false;
                    } else {
                        text.setText("$" + Integer.toString((int)player.getMoney()));
                    }
            }
        });


        creator.start();

    }

    private class MoneyTextView extends TextView implements Updatable {

        private Player player;

        public MoneyTextView(Context context, MyWorld world, Player player) {
            super(context);
            this.player = player;
            this.setTextSize(40);
//            Typeface face = Typeface.createFromAsset(context.getAssets(),   "fonts/sans-serif-smallcaps.ttf");
//            this.setTypeface(face);
            this.setTextColor(Color.YELLOW);
            world.addUpdatable(this);
        }

        @Override
        public void update() {
            activity.runOnUiThread(creator);
        }
    }




}
