package com.ne.revival_games.entity.Modes;

import android.content.Context;
import android.content.Intent;

import com.ne.revival_games.entity.MainActivity;

/**
 * Created by veganova on 6/11/18.
 */

public class ModeInitUtil {

    public static void startNewActivity(final Context context, MainActivity caller, String modeClassName, BaseMode mode, boolean endCurrentActivity) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("enumType", modeClassName);
        intent.putExtra("enumVal", mode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        // wait for the current activity thread to end. (so that it will let go of the canvas lock)
        caller.myThread.end();
        if (caller.myThread.hasEnded()) {

        } else {
            try {
                synchronized (caller.myThread) {
                    caller.myThread.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (endCurrentActivity) {
            caller.startActivity(intent);
            caller.finish();
        } else {
            caller.startActivity(intent);
        }
    }

    /**
     * Uses an old intent object to configure a new one - extracts the mode values.
     */
    public static void startNewActivity(final Context context, MainActivity caller, Intent oldIntent, boolean endCurrentActivity) {
        String modeClassName = (String) oldIntent.getSerializableExtra("enumType");
        BaseMode mode = (BaseMode) oldIntent.getSerializableExtra("enumVal");

        startNewActivity(context, caller, modeClassName, mode, endCurrentActivity);
    }
}
