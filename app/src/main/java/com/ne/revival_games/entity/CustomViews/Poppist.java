package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ne.revival_games.entity.GamePanel;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import java.util.List;

/**
 * Created by Veganova on 10/24/2017.
 */

public abstract class Poppist extends HorizontalScrollView {


    private boolean hidden = true;
    private final int MAX_WIDTH;
    private final int MIN_WIDTH;
    private ValueAnimator in, out;
    private final int ANIMATION_DURATION;

    protected LinearLayout container;
    /**
     * Generalized UI element that can be extended and implemented with very few changes required.
     *
     * @param context
     * @param startPercent  Starting percent can range from 0 - 1
     * @param endPercent    Ending percent, can range from 0 - 1
     * @param duration      Duration in milliseconds
     */
    public Poppist(Context context, double startPercent, double endPercent, int duration) {
        super(context);


        // TODO: 7/20/2017 might want to do this with weights instead.. for handling the changing screen orientation
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        MIN_WIDTH = (int) (width * startPercent);
        MAX_WIDTH = (int) (width * endPercent);
        ANIMATION_DURATION = duration;

        this.setHorizontalScrollBarEnabled(false);
        this.setLayoutParams(new HorizontalScrollView.LayoutParams(MIN_WIDTH, HorizontalScrollView.LayoutParams.MATCH_PARENT));

        this.container = new LinearLayout(context);
        container.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//        container.setBackgroundColor(GamePanel.cream);
        container.setBackgroundColor(GamePanel.background_red);
        container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT));

        this.addView(container);
    }

    int getDuration() {
        return this.ANIMATION_DURATION;
    }

    /**
     * Toggles the poppist on and off - triggering the appropriate animation.
     *
     * @return Returns the boolean status of the hidden variable after the toggle
     */
    boolean toggle() {
        if (this.hidden) {
            out = ValueAnimator.ofInt(this.getMeasuredWidth(), MAX_WIDTH);

            out.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = val;
                    setLayoutParams(layoutParams);
                }
            });

            out.setDuration(ANIMATION_DURATION);
            out.start();
        } else {
            in = ValueAnimator.ofInt(this.getMeasuredWidth(), 0);
            in.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = getLayoutParams();
                    layoutParams.width = val;
                    setLayoutParams(layoutParams);
                }
            });

            in.setDuration(ANIMATION_DURATION);
            in.start();
        }
        this.hidden = !this.hidden;
        return this.hidden;
    }
}