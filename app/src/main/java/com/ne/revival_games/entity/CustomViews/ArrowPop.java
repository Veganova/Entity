package com.ne.revival_games.entity.CustomViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.ne.revival_games.entity.R;

/**
 * A class which is responsible from bringing a view into and out of view when button is pressed.
 * The image to be clicked on to pop out the view is assumed to be an arrow.
 */
public class ArrowPop extends HorizontalScrollView {

    public enum SIDE {
        LEFT, RIGHT
    }

    private static int ARROW_PERCENT = 5;

    private int drawResource = R.drawable.ic_down_arrow;

    private boolean hidden = true;
    private int SCREEN_WIDTH;
    private int SCALED_WIDTH;


    private SIDE side;

    /**
     *
     * Constructor for the ArrowPop class. Currently, only horizontal scrolling is supported.
     *
     * @param context
     * @param toPop             The view to pop out.
     * @param startingPercent   The screen percent to start at: normally will be 0%, to be at the edge.
     * @param endingPercent     The screen percent that the pop ends at. 0 - 100 values are correct.
     * @param side              The side of the screen to display from.
     */
    public ArrowPop(Context context, View toPop, final double startingPercent, final double endingPercent, SIDE side, float SCREEN_WIDTH) {
        super(context);
        this.side = side;
        if (side == SIDE.LEFT || side == SIDE.RIGHT) {
            LinearLayout container = new LinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);
            container.setGravity(Gravity.LEFT);

            // --------------initializing the arrow image which will pop out the rest of the view-----------------------------
            ImageView arrowButton = new ImageView(context);
            arrowButton.setLayoutParams(new ViewGroup.LayoutParams((int) (SCREEN_WIDTH * ARROW_PERCENT / 100.0),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
//            arrowButton.getLayoutParams().width = ;
            arrowButton.setAdjustViewBounds(true);
            arrowButton.setScaleType(ImageView.ScaleType.FIT_XY);
//            arrowButton.setVisibility(INVISIBLE);

            Drawable drawable = VectorDrawableCompat.create(getResources(), drawResource, null);
            arrowButton.setImageDrawable(drawable);


            arrowButton.setOnClickListener(new ImageView.OnClickListener() {
                public void onClick(View v) {
                    ArrowPop.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
            });

            Space empty = new Space(context);
            empty.setLayoutParams(new LinearLayout.LayoutParams((int) (SCREEN_WIDTH * (100 - ARROW_PERCENT)/ 100.0),
                    ViewGroup.LayoutParams.WRAP_CONTENT));




            // ------------------- put all views in each other correctly -----------------------------
            container.addView(toPop);
            container.addView(arrowButton);
            container.addView(empty);
            this.addView(container);

//            this.smoothScrollTo();
        }

    }
}

