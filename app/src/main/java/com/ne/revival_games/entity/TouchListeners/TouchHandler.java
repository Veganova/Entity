package com.ne.revival_games.entity.TouchListeners;

import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by vishn on 7/13/2017.
 */

public interface TouchHandler {
     boolean multiTouch(ArrayList<Integer> pointers, MotionEvent event);
     boolean singleTap(MotionEvent event);
     boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
     boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

}
