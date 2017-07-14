package com.ne.revival_games.entity.TouchListeners;

import android.view.MotionEvent;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/8/2017.
 */

public class TwoTouchListener extends MultiTouchListener{
    private int pointerID1 = -1, pointerID2 = -1;
    private TouchHandler directions;

    //issue is we don't know if someone else is handling that pointer! :D
    public TwoTouchListener(Vector2 lowerBound, Vector2 upperBound, TouchHandler directions){
        super(lowerBound, upperBound);
        this.directions = directions;
    }

    void setTouchHandler(TouchHandler directions){
        this.directions = directions;
    }

    @Override
    boolean actionDown(MotionEvent event, int pointerIndex) {
        if(inBounds(event.getRawX(), event.getRawY()) && pointerID1 == -1) {
            pointerID1 = event.getPointerId(pointerIndex);
//            directions
            return true;
        }
        return false;
    }

    @Override
    boolean actionPointerDown(MotionEvent event, int pointerIndex) {
        if(inBounds(event.getRawX(), event.getRawY())) {
            if(pointerID1 == -1){
                pointerID1 = event.getPointerId(pointerIndex);
            }
            else if(pointerID2 == -1){
                pointerID2 = event.getPointerId(pointerIndex);
            }

            //call player method
            return true;
        }
        return false;
    }

    @Override
    boolean actionUp(MotionEvent event, int pointerIndex) {
       switchPointers(event, pointerIndex);

        return false;
    }

    @Override
    boolean actionPointerUp(MotionEvent event, int pointerIndex) {
        switchPointers(event, pointerIndex);
        return false;
    }

    @Override
    boolean actionMove(MotionEvent event, int pointerIndex) {
        int index1 = event.findPointerIndex(pointerID1);
        int index2 = event.findPointerIndex(pointerID2);

        //call player if and only if 2 pointerIDs are being tracked and one of those moved
        if(trackingTwoPointers()
                && (index1 == event.getPointerId(pointerIndex) || index2 == event.getPointerId(pointerIndex))){
            //if index1 went out of bounds switch off of it
            if(!inBounds(event.getX(index1), event.getY(index1))){
                switchPointers(event, index1);
            }
            //if index2 went out of bounds switch off of it
            if(!inBounds(event.getX(index2), event.getY(index2))){
                switchPointers(event, index2);
            }
            //call player and supply 2 pointers
        }

        return false;
    }

    private int getNewID(MotionEvent event, int searchPastIndex){
        for(int i = searchPastIndex + 1; i < event.getPointerCount(); i++){
            if(inBounds(event.getX(i), event.getY(i))){
                return i;
            }
        }
        return -1;
    }

    private boolean trackingTwoPointers(){
        return (pointerID1 != -1) && (pointerID2 != -1);
    }

    private void switchPointers(MotionEvent event, int pointerIndex){
        int Oldindex1 = event.findPointerIndex(pointerID1);
        int Oldindex2 = event.findPointerIndex(pointerID2);  //MAY THROW ERRORS TODO

        if(Oldindex1 == event.getPointerId(pointerIndex)){
            if(trackingTwoPointers()){
                pointerID1 = pointerID2;
                pointerID2 = getNewID(event, Oldindex2);
            }
            else {
                pointerID1 = -1;
            }
        }
        else if(Oldindex2 == event.getPointerId(pointerIndex)){
            pointerID2 = getNewID(event, Oldindex1);
        }
    }
}
