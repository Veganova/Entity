package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * Created by Veganova on 6/29/2017.
 */

class Barrel {

    public enum BarrelType {
        SINGLE, LAZER
    }

    protected AShape shape;

    Barrel(BarrelType type, Vector2 location, MyWorld world) {
        initBarrel(type, location, world);
    }

    private void initBarrel(BarrelType type, Vector2 location, MyWorld world) {
        switch (type) {
            case SINGLE:
                this.shape = new ObjRectangle(50 + location.x, location.y, 100, 20, world);
        }

    }

    protected void fire() {

    }

}
