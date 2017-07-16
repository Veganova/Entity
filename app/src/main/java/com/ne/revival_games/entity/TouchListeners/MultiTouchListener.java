package com.ne.revival_games.entity.TouchListeners;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;

/**
 * Created by vishn on 7/8/2017.
 */

public abstract class MultiTouchListener implements View.OnTouchListener  {
    private Vector2 lowerBound, upperBound;


    public MultiTouchListener(Vector2 lowerBound, Vector2 upperBound){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * processes double-touches and tracks first two touches
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int pointerIndex = -1;

        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_POINTER_DOWN:
                //check if pointer is in bounds
                 pointerIndex = (motionEvent.getAction()
                        & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

                return actionPointerDown(motionEvent, pointerIndex);

            case MotionEvent.ACTION_DOWN:
                pointerIndex = 0;
                return actionDown(motionEvent, pointerIndex);

            case MotionEvent.ACTION_POINTER_UP:
                pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                return actionPointerUp(motionEvent, pointerIndex);

            case MotionEvent.ACTION_UP:
                return actionUp(motionEvent, 0);

            case MotionEvent.ACTION_MOVE:
                //where we actually do stuff with the pointers
                pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                return actionMove(motionEvent, pointerIndex);
        }


        return false;
    }

    public boolean inBounds(double x, double y){
        return true;
//       return lowerBound.x <= x && x <= upperBound.x &&  lowerBound.y <= y && y <= upperBound.y;
    }

    abstract boolean actionDown(MotionEvent event, int pointerIndex);
    abstract boolean actionPointerDown(MotionEvent event, int pointerIndex);
    abstract boolean actionUp(MotionEvent event, int pointerIndex);
    abstract boolean actionPointerUp(MotionEvent event, int pointerIndex);
    abstract boolean actionMove(MotionEvent event, int pointerIndex);

}
