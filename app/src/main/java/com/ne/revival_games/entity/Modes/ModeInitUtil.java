package com.ne.revival_games.entity.Modes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ne.revival_games.entity.MainActivity;

/**
 * Created by veganova on 6/11/18.
 */

public class ModeInitUtil {

    public static void startActivityWithIntent(final Context context, BaseMode mode, MainActivity activity) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("GameMode", mode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        // wait for the current activity thread to end. (so that it will let go of the canvas lock)
        activity.finish();
        if (activity.myThread.hasEnded()) {

        } else {
            try {
                synchronized (activity.myThread) {
                    activity.myThread.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        activity.startActivity(intent);
    }
}
