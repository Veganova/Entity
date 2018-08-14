package com.ne.revival_games.entity.CustomViews;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by veganova on 8/14/18.
 */

public class MenuDragListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        System.out.println("IN MENUDRAG GESTURE");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        System.out.println("just scrolling thats all");
        return super.onScroll(e1, e2, distanceX, distanceY);
    }
}
