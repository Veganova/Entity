package com.ne.revival_games.entity.WorldObjects.Entity;

/**
 * Created by Veganova on 6/10/2017.
 */

public enum Team {
    DEFENCE, OFFENSE;

    public boolean opposite(Team other) {
        return (this != other);
    }
}
