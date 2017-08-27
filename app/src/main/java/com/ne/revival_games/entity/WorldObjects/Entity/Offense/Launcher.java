package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ne.revival_games.entity.WorldObjects.Entity.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

import java.util.HashMap;

/**
 * Created by Veganova on 7/22/2017.
 */

public class Launcher {


    private final MyWorld world;
    private final Team team;
    private double level;

    private double minX, minY, maxX, maxY;

    // These values can be manipulated to represent different levels.
    // Defaults
    private double rate = 100;
    private double atOnce = 1;
    private double ammo = 10;
    private long lastFired = 0;

    // A shape that is described by one or many RandomPodoubleSelectors.
    // To choose a podouble randomly:
    //  - Iterate over these to find the total area,
    //  - double num = random(0, total_area);
    //  - iterate over the RandomPodoubleSelectors until the correct one is chosen (the one at which
    //      the area commulates to the randomized one)
    private HashMap<Integer, RandomPointSelector> compositeShape;
    private int totalArea = 0;
    /**
     * Pass in a width and height that represents the size of the bounding box where the player is
     * centered and objects CANNOT be launched. The negative of this area is all the area where
     * bullets can be shot from.
     * Assuming center of the world is (0, 0).
     * All Values are scaled by World.SCALE
     */
    public Launcher(double width, double height, MyWorld world, Team team) {
        this.world = world;
        this.team = team;
        double scaledWidth = width / MyWorld.SCALE;
        double scaledHeight = height / MyWorld.SCALE;

        this.minX = -scaledWidth / 2;
        this.minY = -scaledHeight / 2;
        this.maxX = scaledWidth / 2;
        this.maxY = scaledHeight / 2;

        this.compositeShape = new HashMap<>();
        this.addBoundingOval(scaledWidth / 2, scaledWidth, scaledHeight / 2, scaledHeight, new Vector2(0, 0));
    }

    /**
     * Pass in a width and height that represents the size of the bounding box where the player is
     * centered and objects CANNOT be launched. The negative of this area is all the area where
     * bullets can be shot from. Pass in a center to specify the podouble around which the bounding
     * box will be centered.
     * Assuming center of the world is (0, 0).
     * All Values are scaled by World.SCALE
     *
     */
    public Launcher(double width, double height, Vector2 center, MyWorld world, Team team) {
        this.world = world;
        this.team = team;
        double scaledWidth = width / MyWorld.SCALE;
        double scaledHeight = height / MyWorld.SCALE;

        this.minX = -scaledWidth / 2;
        this.minY = -scaledHeight / 2;
        this.maxX = scaledWidth / 2;
        this.maxY = scaledHeight / 2;

        this.compositeShape = new HashMap<>();
        center.multiply(1/MyWorld.SCALE);
        this.addBoundingOval(scaledWidth / 2, scaledWidth, scaledHeight / 2, scaledHeight, center);
    }

    /**
     * Every update it checks if there is ammo to shoot and if so, if the rate of fire permits a shot at
     */
    public void update() {
        if (this.ammo > 0 && System.currentTimeMillis() - this.lastFired >= this.rate) {
            // FIRE
            int randomArea = (int) (Math.random() * this.totalArea);
//            this.lastFired = System.currentTimeMillis();

            for (int i = 0; i < this.atOnce; i += 1) {
                int culmArea = 0;
                Vector2 location = null;
                if (ammo > 0) {
                    // loop over all the smaller shapes that make this shape and choose
                    // one based on expected value using area of the shapes.
                    for (Integer areaOfOval: this.compositeShape.keySet()) {
                        if (randomArea > culmArea && randomArea < areaOfOval) {
                            location = this.compositeShape.get(areaOfOval).randomPoint();
                            System.out.println("FIRED! at time - " + this.lastFired / 1000
                                    + " at location: " + location);
                            GhostEntity comet = GhostFactory.produce(Entities.COMET, MyWorld.SCALE * location.x,
                                    MyWorld.SCALE * location.y, 0, world, team);

                            if (comet.canPlace()) {
                                comet.place().addToTeam(team);
                                this.lastFired = System.currentTimeMillis();
                                ammo -= 1;
                            } else {
                                System.out.println("CONFLICT CANNOT PLACE HERE launcher comet issue!!!!!!!");
                            }

                            break;
                        }
                        culmArea = areaOfOval;
                    }
                }

            }
        }
    }

    // temporary method to draw the default zone made in the constructor
    public void draw(Canvas canvas) {
        RectF rectInner = new RectF((float)this.minX, (float)this.minY, (float)this.maxX, (float)this.maxY);
// TODO: 8/27/2017 why do these calculations work?
        RectF rectOuter = new RectF((float)this.minX * 2, (float)this.minY * 2, (float)this.maxX * 2, (float)this.maxY * 2);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawOval(rectInner, paint);
        canvas.drawOval(rectOuter, paint);
    }
    
    public void addBoundingOval(double hMin, double hMax, double vMin, double vMax, Vector2 center) {
        OvalRandomSelector oval = new OvalRandomSelector(hMin, hMax, vMin, vMax, center);
        this.addRandomArea(oval);
    }

    public void addRandomArea(RandomPointSelector randArea) {
        int xShapeArea = (int) randArea.getArea();
        this.totalArea += xShapeArea;
        this.compositeShape.put(this.totalArea, randArea);
    }


}

/**
 * Selects a random podoubles from the shape described by the inputs of the constructor. Ovals.
 */
class OvalRandomSelector implements RandomPointSelector {

    private final double hMin;
    private final double hMax;
    private final double vMin;
    private final double vMax;
    private final Vector2 center;

    private double area;
    
    /**
     * Pass double he horizontal and verticle axis (bounds) for the oval. Values must be positive.
     * Centered around the podouble provided. The first four values represent distances from the center
     * in the specified direction.
     */
    OvalRandomSelector(double hMin, double hMax, double vMin, double vMax, Vector2 center) {
        this.hMin = hMin;
        this.hMax = hMax;
        this.vMin = vMin;
        this.vMax = vMax;
        this.center = center;

        this.area = hMax * vMax - hMin * vMin; // subtract the two rectangles (dont know how to find area of elipses)
    }

    public Vector2 randomPoint() {
        // not sure if this is completely random in the area of an ellipse (or a ellipse ring rather)
        // TODO: 7/22/2017 check if this is the correct way to randomize over an oval/ellipse 
        double angle = Math.random()* Math.PI * 2;
        double r_x = Math.random() *(hMax - hMin) + hMin;
        double r_y= Math.random() * (vMax - vMin) + vMin;

        double x = Math.cos(angle) * r_x;
        double y = Math.sin(angle) * r_y;

        return new Vector2(x, y);
    }

    @Override
    public double getArea() {
        return this.area;
    }
}

interface RandomPointSelector {
    Vector2 randomPoint();
    double getArea();
}
