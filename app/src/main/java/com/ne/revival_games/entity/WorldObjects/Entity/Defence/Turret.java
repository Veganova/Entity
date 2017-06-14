package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.ObjectType;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishn on 6/11/2017.
 */

public class Turret extends Entity{
    public static int COST = 200;
    public static int MASS = 25;
    public static int HEALTH = 80;
    public static ObjectType TYPE = ObjectType.DEFENCE;

    //need to include the angle somehow
    public Turret(Vector2 location, double angle, MyWorld world){
        super(location.x, location.y, angle, 0, HEALTH, false);
        initializeTurret(location, world);
    };

    public void initializeTurret(Vector2 location, MyWorld world){
        List<AShape> components = new ArrayList<AShape>();
        //make sure relative location placement is correct
        components.add(new ObjRectangle(50+location.x, location.y , 100, 20, world));
        components.add(new ObjCircle(location.x, location.y, 50.0, world));
        world.engineWorld.addJoint(new RevoluteJoint(components.get(0).body, components.get(1).body,
                components.get(1).body.getWorldCenter()));
        this.shape = new ComplexShape(components);
        world.objectDatabase.put(components.get(0).body, this);
        world.objectDatabase.put(components.get(1).body, this);
    }


}
