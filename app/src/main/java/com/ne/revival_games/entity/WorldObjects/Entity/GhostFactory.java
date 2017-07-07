package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.MassLazer;
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
     * @param angle
     * @return
     */
    // TODO: 7/1/2017 need some way to rotate here or in the ghost entities.
    public GhostEntity produce(Entities ent, double x, double y, double angle) {
        Entity entity = null;
        switch(ent) {
            case BARREL:

                entity = new Barrel(new Missile(0, 0, 0, 30, this.world), Barrel.BarrelType.SINGLE, new Vector2(x, y), world, angle);
                break;
            case BARRIER:
                entity =  (new Barrier(x, y, angle, world));
                break;
            case MASSLAZER:
                entity =  (new MassLazer(x, y, angle, world));
                break;
            case MISSLE: // TODO: 7/1/2angle17 what case will this be made from here?
                entity =  (new Missile(x, y, Missile.SPEED, angle, world));
                break;
            case NEXUS:
                entity =  (new Nexus(x, y, angle, world));
                break;
            case TURRET:
                entity =  (new Turret(new Vector2(x, y), angle, world));
                break;
        }
        GhostEntity ghost = new GhostEntity(entity, world);

        if (entity != null) {
            this.world.ghosts.put(entity, ghost);
        }

        return ghost;
    }
}
