package com.ne.revival_games.entity.WorldObjects.Entity;

/**
 * Created by Veganova on 6/10/2017.
 */

public enum ObjectType {
    DEFENCE, OFFENSE;

    public boolean opposite(ObjectType other) {
        return (this != other);
    }
}
