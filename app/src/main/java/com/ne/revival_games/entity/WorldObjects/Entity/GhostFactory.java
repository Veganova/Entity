package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Missile;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;

/**
 * Produces ghost entities which in turn produces entities.
 */
public class GhostFactory {

    private MyWorld world;

    public GhostFactory(MyWorld world) {
        this.world = world;
    }


    /**
     * Produces a ghost entity at the given coordinates (mouse usually) of the given type.
     * Use this to place an entity correctly.
     *
     * @param ent   the type of entity (ghost entity) to be created
     * @param x Give the x.
     * @param y Give the y.
     * @return
     */
    // TODO: 7/1/2017 need some way to rotate here or in the ghost entities.
    public GhostEntity produce(Entities ent, double x, double y) {
        switch(ent) {
            case BARREL:
                return new GhostEntity(new Barrel(Barrel.BarrelType.SINGLE, new Vector2(x, y), world, 0), world);
            case BARRIER:
                return new GhostEntity(new Barrier(x, y, 0, world), world);
            case MASSLAZER:
                return null;
            case MISSLE: // TODO: 7/1/2017 what case will this be made from here?
                return new GhostEntity(new Missile(x, y, Missile.SPEED, 0, world), world);
            case NEXUS:
                return new GhostEntity(new Nexus(x, y, 0, world), world);
            case TURRET:
                return new GhostEntity(new Turret(new Vector2(x, y), 0, world), world);
        }
        return null;
    }
}
