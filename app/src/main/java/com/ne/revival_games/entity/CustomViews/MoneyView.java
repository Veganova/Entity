package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ne.revival_games.entity.GamePanel;
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
    public MoneyView(MainActivity activity, final Context context, final MyWorld world, final Player player, float SCREEN_WIDTH) {
        super(context);
        this.activity = activity;
        this.player = player;

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params2.topMargin = 25;
        this.setLayoutParams(params2);

        this.setGravity(Gravity.TOP | Gravity.RIGHT);

        final LinearLayout container = new LinearLayout(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setOrientation(LinearLayout.HORIZONTAL);

        container.setGravity(Gravity.RIGHT);
        container.setLayoutParams(params);

        addView(new ArrowPop(context, container, false, true, ArrowPop.SIDE.RIGHT, SCREEN_WIDTH));

        creator = new Thread(new Runnable() {
            MoneyTextView text;
            private boolean first = true;
            @Override
            public void run() {
                    if (first) {
                        text = new MoneyTextView(context, world, player);
                        container.addView(text);
//                        addView(container);
                        first = false;
                        text.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                player.addMoney(100000);
                            }
                        });
                    } else {
                        if (player.getMoney() > 99999) {
                            text.setText("∞");
                        } else {
                            text.setText("$" + Integer.toString((int) player.getMoney()));
                        }
                    }
            }
        });




        creator.start();

    }

    private class MoneyTextView extends TextView implements Updatable {

        private Player player;
        private float sizePortion = 0.8f;

        public MoneyTextView(Context context, MyWorld world, Player player) {
            super(context);
            this.player = player;
            this.setTextSize(30 * sizePortion);
            this.setPadding((int)(0 * sizePortion), (int)(20 * sizePortion), (int)(20 * sizePortion), (int)(20 * sizePortion));
//            Typeface face = Typeface.createFromAsset(context.getAssets(),   "fonts/sans-serif-smallcaps.ttf");
//            this.setTypeface(face);
            this.setBackgroundColor(GamePanel.cream);
            this.setTextColor(GamePanel.background_dark);
            world.addUpdatable(this);
        }

        @Override
        public void update() {
            activity.runOnUiThread(creator);
        }
    }




}
