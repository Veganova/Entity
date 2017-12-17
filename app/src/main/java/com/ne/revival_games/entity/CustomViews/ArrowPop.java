package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.ne.revival_games.entity.R;

import static com.ne.revival_games.entity.CustomViews.ArrowPop.SIDE.RIGHT;

/**
 * A class which is responsible from bringing a view into and out of view when button is pressed.
 * The image to be clicked on to pop out the view is assumed to be an arrow.
 */
public class ArrowPop extends HorizontalScrollView {

    public enum SIDE {
        LEFT, RIGHT
    }

    private double ARROW_WIDTH_FRAC = 0.05;

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
    public ArrowPop(final Context context, final View toPop, final double startingPercent, final double endingPercent, final SIDE side, final float SCREEN_WIDTH) {
        super(context);
        this.side = side;
        if (side == SIDE.LEFT || side == RIGHT) {
            final LinearLayout container = new LinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);

            // need to add in the passed in view to check the height it will end up with
            container.addView(toPop);

            final Drawable drawable = VectorDrawableCompat.create(getResources(), drawResource, null);

            System.out.println("intrinsic - " + drawable.getIntrinsicWidth() + " " + drawable.getIntrinsicHeight());

            this.addView(container);

            // doing in post because the height is unknown
            this.post(new Runnable() {
                @Override
                public void run() {
                    // --------------initializing the arrow image which will pop out the rest of the view-----------------------------

                    ImageView arrowButton = new ImageView(context);
                    arrowButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            toPop.getMeasuredHeight()));

                    ARROW_WIDTH_FRAC = toPop.getMeasuredHeight() * drawable.getIntrinsicWidth() / (1.0 * drawable.getIntrinsicHeight()) / SCREEN_WIDTH;

                    System.out.println("HEIEHIT: " + toPop.getMeasuredHeight() + " " + ARROW_WIDTH_FRAC);

                    arrowButton.setAdjustViewBounds(true);
                    arrowButton.setScaleType(ImageView.ScaleType.FIT_XY);

                    arrowButton.setImageDrawable(drawable);

                    arrowButton.setOnClickListener(new ImageView.OnClickListener() {
                        public void onClick(View v) {
                            ArrowPop.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    });

                    final Space empty = new Space(context);
                    empty.setLayoutParams(new LinearLayout.LayoutParams((int) (SCREEN_WIDTH * (1 - ARROW_WIDTH_FRAC)),
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (side == RIGHT) {
                        container.addView(arrowButton, 0);
                        container.addView(empty, 0);
                    } else {
                        container.addView(arrowButton);
                        container.addView(empty);
                    }

                    postInvalidate();
                }
            });
        }

    }
}

