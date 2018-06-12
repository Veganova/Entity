package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ne.revival_games.entity.Modes.GameMode;
import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.Modes.ModeInitUtil;
import com.ne.revival_games.entity.R;

/**
 * Created by Veganova on 9/10/2017.
 */

public class RestartHome extends RelativeLayout {
    private static final int ANIMATION_DURATION = 600;
    private static float SCREEN_WIDTH;
    private static int HEIGHT;
    private RestartButton rb;
    private HomeButton home;
    private ArrowPop a1, a2;


    public RestartHome(Context context, float parentHeight, MainActivity activity, GameMode mode, float SCREEN_WIDTH) {
        super(context);
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.HEIGHT = (int) (parentHeight * 0.07);
        this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        this.setGravity(Gravity.LEFT | Gravity.CENTER);

        home = new HomeButton(context, activity);
        rb = new RestartButton(context, activity, mode);
    }

    private boolean first = true;
    public void pop() {
        if (!first) {
            return;
        }
        first = false;
        Context context = getContext();
        Container c2 = new Container(context , home, 0);
        Container c  = new Container(context, rb, 1);



        a1 = new ArrowPop(context, c, false, true, ArrowPop.SIDE.LEFT, SCREEN_WIDTH);
        a2 = new ArrowPop(context, c2, false, true, ArrowPop.SIDE.LEFT, SCREEN_WIDTH);

        LinearLayout dummy1 = new LinearLayout(context);
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p1.setMargins(0, (int)(0.1 * HEIGHT), 0, 0);
        dummy1.setLayoutParams(p1);
        dummy1.addView(a2);



        LinearLayout verticle = new LinearLayout(context);
        verticle.setOrientation(LinearLayout.VERTICAL);
        verticle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        verticle.addView(a1);
//        Space s = new Space(context);
//        s.setLayoutParams(new LinearLayout.LayoutParams(0, 10));
//        verticle.addView(s);
        verticle.addView(dummy1);
        this.addView(verticle);


        a1.show();
        a2.show();
    }

    class Container extends LinearLayout {
        public Container(Context context, final View image, int num) {
            super(context);

            this.setBackgroundColor(GamePanel.cream);

            this.setOrientation(HORIZONTAL);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    HEIGHT);
            int p = (int)(0.18 * HEIGHT);
            this.setPadding(p / 2, p, 0, p);
//            params.topMargin = p;
            this.setLayoutParams(params);

            this.addView(image);
        }
    }

    private class HomeButton extends android.support.v7.widget.AppCompatImageView {
        HomeButton(Context context, final MainActivity activity) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            this.setAdjustViewBounds(true);
            this.setScaleType(ScaleType.FIT_XY);

            Drawable drawable = VectorDrawableCompat.create(getResources(),  R.drawable.ic_home, null);
            this.setColorFilter(GamePanel.background_dark);
            this.setImageDrawable(drawable);

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    private class RestartButton extends android.support.v7.widget.AppCompatImageView {
        RestartButton(final Context context, final MainActivity activity, final GameMode mode) {
            super(context);

            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            this.setAdjustViewBounds(true);
            this.setScaleType(ScaleType.FIT_XY);

            Drawable drawable = VectorDrawableCompat.create(getResources(),  R.drawable.ic_restart, null);
            this.setColorFilter(GamePanel.background_dark);
            this.setImageDrawable(drawable);


            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModeInitUtil.startActivityWithIntent(context, mode, activity);
                }
            });
        }
    }
}
