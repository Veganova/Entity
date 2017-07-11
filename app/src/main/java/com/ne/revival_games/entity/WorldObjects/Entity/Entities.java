package com.ne.revival_games.entity.WorldObjects.Entity;

import java.util.Random;

/**
 * Created by Veganova on 7/1/2017.
 */

public enum Entities {
    //BARREL, BARRIER, MASSLAZER, MISSLE, NEXUS, TURRET, BULLET;

//    BARRIER, NEXUS, TURRET, COMET;
    COMET, NEXUS, BARRIER;

    public static Entities random() {
       return Entities.values()[new Random().nextInt(Entities.values().length)];
    }
}
