package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.ne.revival_games.entity.R;

import static com.ne.revival_games.entity.CustomViews.ArrowPop.SIDE.LEFT;
import static com.ne.revival_games.entity.CustomViews.ArrowPop.SIDE.RIGHT;

//TODO: bug - when startedHidden is true, there is a flicker.

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
     * Constructor for the ArrowPop class. Currently, only horizontal scrolling is supported.
     *
     * @param context
     * @param toPop           The view to pop out.
     * @param side            The side of the screen to display from. right would mean it comes out of the right side.
     */
    public ArrowPop(final Context context, final View toPop, final boolean startHidden, final boolean interactable, final SIDE side, final float SCREEN_WIDTH) {
        super(context);
        this.side = side;
        this.hidden = startHidden;

        if (side == SIDE.LEFT || side == RIGHT) {
            final MyLinearLayout container = new MyLinearLayout(context);
            container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            container.setOrientation(LinearLayout.HORIZONTAL);

            // need to add in the passed in view to check the height it will end up with
            container.addView(toPop);

            if (side == RIGHT) {
                container.setGravity(Gravity.RIGHT);
            }


            if (side == RIGHT) {
                drawResource = R.drawable.ic_down_arrow_reversed;
            }
            final Drawable drawable = VectorDrawableCompat.create(getResources(), drawResource, null);

            this.addView(container);
            this.setHorizontalScrollBarEnabled(false);

            // doing in post because the height is unknown
            container.setVisibility(INVISIBLE);
            this.post(new Runnable() {
                @Override
                public void run() {
                    // --------------initializing the arrow image which will pop out the rest of the view-----------------------------
                    ImageView arrowButton = new ImageView(context);
                    arrowButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            toPop.getMeasuredHeight()));

                    ARROW_WIDTH_FRAC = toPop.getMeasuredHeight() * drawable.getIntrinsicWidth() / (1.0 * drawable.getIntrinsicHeight()) / SCREEN_WIDTH;

                    arrowButton.setAdjustViewBounds(true);
                    arrowButton.setScaleType(ImageView.ScaleType.FIT_XY);

                    arrowButton.setImageDrawable(drawable);

                    if (interactable) {
                        arrowButton.setOnClickListener(new ImageView.OnClickListener() {
                            public void onClick(View v) {
                                if (hidden) {
                                    show();
                                } else {
                                    hide();
                                }
                            }
                        });
                    }

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

                    empty.post(new Runnable() {
                        @Override
                        public void run() {
                            if (startHidden) {
                                hide();
                            } else {
                                show();
                            }
                            container.setVisibility(VISIBLE);
                        }
                    });

                    postInvalidate();
                }
            });
        }


    }

    private void hide() {
        if (side == SIDE.LEFT) {
            ArrowPop.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        } else {
            ArrowPop.this.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        }
        hidden = true;
    }

    void show() {
        if (side == SIDE.LEFT) {
            ArrowPop.this.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        } else {
            ArrowPop.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }
        hidden = false;
    }

    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private boolean mScrollable = false;

    private void toggleScrolling() {
        mScrollable = !mScrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

    private class MyLinearLayout extends LinearLayout {

        public MyLinearLayout(Context context) {
            super(context);
        }

        // can cause the ui to go back and forth. Case when this happens: have a MoneyView on the left side initialized to be hidden, watch it expand as the digits in the money view increase.
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            if (!hidden) {
                show();
            } else {
                hide();
            }
        }
    }
}

