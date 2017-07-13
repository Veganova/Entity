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

public class MultiTouchListener implements View.OnTouchListener  {
    private Vector2 lowerBound, upperBound;
    private int pointerID1 = -1, pointerID2 = -1;

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
        ArrayList<Integer> validPointers = new ArrayList<>();

        if(motionEvent.getPointerCount() < 2){
            return false;
        }

        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_POINTER_DOWN:
                //check if pointer is in bounds
                int newIndex = (motionEvent.getAction()
                        & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

                //if pointer is not in appropriate bounding box
                if(!inBounds(motionEvent.getX(newIndex), motionEvent.getY(newIndex))){
                    return false;
                }

                //if we need a new pointer
                if(pointerID1 == -1 || pointerID2 == -1){


                    if(pointerID1 == -1){
                        pointerID1 =
                                motionEvent.getPointerId(newIndex);
                    }
                    else if(pointerID2 == -1){
                        pointerID2 =
                                motionEvent.getPointerId(newIndex);
                    }

                }
                break;
            case MotionEvent.ACTION_DOWN:
                // if pointer is not in appropriate bounding box
                if(!inBounds(motionEvent.getX(0), motionEvent.getY(0))){
                    return false;
                }
                if(pointerID1 == -1 || pointerID2 == -1){
                    if(pointerID1 == -1){
                        pointerID1 =
                                motionEvent.getPointerId(0);
                    }
                    else if(pointerID2 == -1){
                        pointerID2 =
                                motionEvent.getPointerId(0);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

                if(pointerIndex == pointerID1){
                    pointerID1 = -1;
                }
                else if(pointerIndex == pointerID2){
                    pointerID2 = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(pointerID1 == 0){
                    pointerID1 = -1;
                }
                else if(pointerID2 == 0){
                    pointerID2 = -1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //where we actually do stuff with the pointers

                return true;
        }


        return false;
    }

    private boolean inBounds(double x, double y){
        return x <= upperBound.x && x >= lowerBound.x && y <= upperBound.y && y >= lowerBound.y;
    }

}
