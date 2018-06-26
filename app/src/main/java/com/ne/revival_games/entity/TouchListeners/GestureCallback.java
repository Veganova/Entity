package com.ne.revival_games.entity.TouchListeners;

/**
 * Created by veganova on 6/25/18.
 */

public abstract class GestureCallback {

    /**
     * Regular lambda class method. Return type signifies whether the method that calls apply()
     * should continue (true) or stop midway (false).
     */
    public abstract boolean apply();
}
