package com.ne.revival_games.entity.CustomViews;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by veganova on 8/19/18.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final int containerHeight;

    public OnSwipeTouchListener(Context ctx, int height) {
        this.containerHeight = height;
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private boolean clickedIn = false;

        @Override
        public boolean onDown(MotionEvent e) {
            if (e.getY() >= 0 || e.getY() <= containerHeight) {
                clickedIn = true;
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (clickedIn && (e2.getY() < 0 || e2.getY() > containerHeight)) {
                onSwipeOut(e2);
                clickedIn = false;
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    /**
     * Called when user swipes out of context's area.
     * @param event Gives the event that contains the relX, relY and can be passed to another touch to continue
     *                relX  Relative x value where the swipe exited. 0 to component width (left to right)
     *                relY  Relative y value where the swipe exited. 0 to component height (top to bottom)
     */
    public void onSwipeOut(MotionEvent event) {

    }
}