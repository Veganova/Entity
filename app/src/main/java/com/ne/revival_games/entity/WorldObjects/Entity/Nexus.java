package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 6/29/2017.
 */

public class Nexus extends Entity{
    public static int HEALTH;
    public static int MASS;
    public static int COST;
    public List<AShape> components;

    public Nexus(double x, double y, double angle, MyWorld world){
        super(x, y, angle, 0, HEALTH, false);
        components = new ArrayList<>();
        components.add(new ObjRectangle(0, 0, 120, 120, 0));
        double [] points1 = {0, 40, 0, -40, 40, 0};
        components.add(new ObjTriangle(50, 0, points1, 0));
        double [] points2 = {0, -40, 0, 40, -40, 0};
        components.add(new ObjTriangle(-50, 0, points2, 0));
        double [] points3 = {-40, 0, 40, 0, 0, 40};
        components.add(new ObjTriangle(0, 50, points3, 0));
        double [] points4 = {40, 0,-40, 0,0, -40};
        components.add(new ObjTriangle(0, -50, points4,0));

        this.shape = new ComplexShape(components, 0, 0, world);
        world.objectDatabase.put(this.shape.body, this);
        TYPE = ObjectType.DEFENCE;
    }

    @Override
    public boolean onCollision(Entity contact, double damage){
        if(contact.TYPE == ObjectType.DEFENCE){
            this.health -= damage;
            if(this.health <= 0){
                this.invisible = true;
                return false;
            }
        }

        return true;
    }


}
