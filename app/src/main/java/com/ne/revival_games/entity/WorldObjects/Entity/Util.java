package com.ne.revival_games.entity.WorldObjects.Entity;

import org.dyn4j.geometry.Vector2;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Veganova on 6/29/2017.
 */

public class Util {

    public static double getDistance(Vector2 point1, Vector2 point2){
        return Math.sqrt(Math.pow(point1.x-point2.x, 2) + Math.pow(point1.y-point2.y, 2));
    }

    //warning untested
    public static double distanceFromLine(Vector2 point1line, Vector2 point2line, Vector2 target){
        //y - y2 = m * (x - x2)
        //y = m*x + c
        //m*x + -1*y + c = 0
        double  m = (point2line.y - point1line.y) / (point2line.x - point1line.x);
        double c = point1line.y - m*point1line.x;
        //http://www.intmath.com/plane-analytic-geometry/perpendicular-distance-point-line.php
        double distance = Math.abs( m*target.x -target.y + c) / Math.sqrt((Math.pow(m,2) + 1));
        return distance;
    }

    public static double lawofCosines(double a, double b, double c){
        return Math.acos((Math.pow(a, 2) + Math.pow(b, 2)
                - Math.pow(c, 2)) / (2 * a * b));
    }

    public static double absoluteAngle(Vector2 vertex, Vector2 outerPoint) {
        return (Math.PI*2+Math.atan2(outerPoint.y - vertex.y, outerPoint.x - vertex.x)) % (Math.PI*2);
    }

    public static boolean nearValue(double value, double targetvalue, double delta){
        return Math.abs(value-targetvalue) <= delta;
    }

    public static int randomBetweenValues(int min, int max) {
        return (int) (Math.random()*(max-min)) + min + 1;
    }

    public static double randomBetweenValues(double rangeMin, double rangeMax) {
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }

    public static Vector2 intersectionBetweenTwoLines(Vector2 lineOne, Vector2 lineTwo) {
        if(lineOne.x == lineTwo.x ) {
            return new Vector2(-1,-1);
        }
        else {
            double x = (lineTwo.y - lineOne.y) / (lineOne.x - lineTwo.x);
            double y = lineOne.x * x + lineOne.y;

            return new Vector2(x,y);
        }
    }

}
