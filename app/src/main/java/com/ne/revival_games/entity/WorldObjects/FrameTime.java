package com.ne.revival_games.entity.WorldObjects;

import android.support.annotation.NonNull;

import java.util.PriorityQueue;

/**
 * Created by veganova on 12/26/17.
 */

public class FrameTime implements Updatable {

    private static FrameTime singleRef;

    /**
     * Make a new instance of this class. Should be called in world everytime a new game is made.
     * Note: Everytime this is called, the previous global instance of this class is deleted.
     *
     * @return Returns singleton instance of the FrameTime class
     */
    public static FrameTime getNewReference() {
        singleRef = new FrameTime();

        return singleRef;
    }

    /**
     * Regular singleton logic.
     *
     * @return
     */
    public static FrameTime getReference() {
        if (singleRef == null) {
            singleRef = new FrameTime();
        }
        return singleRef;
    }

    // Actual fields

    /**
     * Represents the time in terms of frames.
     */
    public long frameNum;

    /**
     * Stores the CB - CallBacks.
     * This data structure supports keeping in sorted order (min heap) in O(log(n)) time.
     */
    private PriorityQueue<CB> callbacks;

    private FrameTime() {
        this.frameNum = 0;
        this.callbacks = new PriorityQueue<>();
    }

    // will be called by the callback in list of Updateables in World
    public void update() {
        this.frameNum += 1;
        // Callback logic:
        CB top = this.callbacks.peek();

        while (top != null && top.frame == this.frameNum) {
            // if a time is found to be <= cur frame, take it out of the priority queue.
            this.callbacks.poll();

            // when the frame is a match, run the associated runnable in a separate thread.
            if (top.frame == this.frameNum || top.frame < this.frameNum) {
                top.r.run();
            }

            top = this.callbacks.peek();
        }
    }

    public static long getTime() {
        return getReference().frameNum;
    }

    public static void addCallBackAtFrame(long frame, Runnable r) {

        FrameTime me = getReference();

        if(frame == me.frameNum) {
            r.run();
            return;
        }

        me.callbacks.add(new CB(frame, r));
    }

    /**
     *
     * @param deltaFrames The number of frames from the current time to run the action at
     * @param r
     */
    public static void addCallBackAtDeltaFrames(long deltaFrames, Runnable r) {
        FrameTime me = getReference();

        addCallBackAtFrame(me.frameNum + deltaFrames, r);
    }

    /**
     * Implementation of comparable allows for this to be put in a priority queue and have O(log(n)) insertions.
     */
    private static class CB implements Comparable<CB> {
        /**
         * Time is specified in frames.
         */
        long frame;

        /**
         * An action to run once the time is reached.
         */
        Runnable r;

        private CB(long time, Runnable r) {
            this.frame = time;
            this.r = r;
        }

        @Override
        public int compareTo(@NonNull CB other) {
            long dif = this.frame - other.frame;
            if (dif < 0) {
                return -1;
            } else if (dif > 0) {
                return 1;
            }
            return 0;
        }
    }
}
