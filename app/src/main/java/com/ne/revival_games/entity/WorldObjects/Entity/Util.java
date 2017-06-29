package com.ne.revival_games.entity.WorldObjects.Entity;

import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

public class Util {

    public static double getDistance(Vector2 point1, Vector2 point2){
        return Math.sqrt(Math.pow(point1.x-point2.x, 2) + Math.pow(point1.y-point2.y, 2));
    }
}
