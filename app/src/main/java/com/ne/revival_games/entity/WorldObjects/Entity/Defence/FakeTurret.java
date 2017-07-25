package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import android.graphics.Canvas;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjCircle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/25/2017.
 */

public class FakeTurret extends Entity {
    public AShape tempShape;

    public FakeTurret(Vector2 location, double direction, double speed, int health, boolean invulnerable, Team team, MyWorld world) {
        super(direction, speed, health, invulnerable, team);
        this.shape = new ObjCircle(50);
        shape.getBuilder(true, world).setXY(location.x, location.y).init();
        this.tempShape = new ObjRectangle(120, 20);
        tempShape.getBuilder(true, world).setXY(50+location.x, location.y).init();
        RevoluteJoint jerk = new RevoluteJoint(shape.body, tempShape.body, new Vector2(location.x/MyWorld.SCALE, location.y/MyWorld.SCALE));
        jerk.setCollisionAllowed(false);
        world.engineWorld.addJoint(jerk);
        world.objectDatabase.put(this.tempShape.body, this);
        world.objectDatabase.put(this.shape.body, this);
    }

    @Override
    public void draw(Canvas canvas){
        this.shape.draw(canvas);
        this.tempShape.draw(canvas);
    }


}
