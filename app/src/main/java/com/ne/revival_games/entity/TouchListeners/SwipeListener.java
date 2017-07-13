package com.ne.revival_games.entity.TouchListeners;

import android.graphics.Camera;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.MainActivity;

/**
 * Created by vishn on 7/13/2017.
 */

public class SwipeListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private GestureDetectorCompat mDetector;

    public SwipeListener(MainActivity activity){
        mDetector = new GestureDetectorCompat(activity.getApplicationContext(), this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        throw new RuntimeException("Stub!");
    }
}
