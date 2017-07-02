package com.ne.revival_games.entity.WorldObjects.Entity;

import org.dyn4j.geometry.Vector2;

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

    public static double absoluteAngle(Vector2 vertex, Vector2 outerPoint){
        Vector2 axisPoint = new Vector2(vertex.x + 20, vertex.y);
        double getSign = (outerPoint.y - vertex.y);
        double a = Util.getDistance(vertex, axisPoint);
        double b = Util.getDistance(vertex, outerPoint);
        double c = Util.getDistance(axisPoint, outerPoint);
        if(getSign == 0){
            if(vertex.x > outerPoint.x){
                return 0;
            }
            return Math.PI;
        }
        double angleTo = getSign/Math.abs(getSign)*Util.lawofCosines(a,b,c);
        return (Math.PI *2 + angleTo) % (Math.PI *2);

    }

}
