package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrel;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.MassLazer;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Missile;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Comet;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.ExplosiveMissile;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import org.dyn4j.geometry.Vector2;

/**
 * Produces ghost entities which in turn produces entities.
 */
public class GhostFactory {

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
    public static GhostEntity produce(Entities ent, double x,
                                      double y, double angle, MyWorld world, Team team) {
        Entity entity = null;
        switch(ent) {
//            case BARREL:
//
//                entity = new Barrel(new Missile(0, 0, 0, 30, world, team),
//                        Barrel.BarrelType.SINGLE, new Vector2(x, y), world, angle, team);
//                break;
//            case MASSLAZER:
//                entity =  new MassLazer(x, y, angle, world, team);
//                break;
//            case MISSLE: // TODO: 7/1/2angle17 what case will this be made from here?
//                entity =  new Missile(x, y, Missile.SPEED, angle, world, team);
//                break;
            case BARRIER:
                entity =  (new Barrier(x, y, angle, world, team));
                break;

            case NEXUS:
                entity =  (new Nexus(x, y, angle, world, team));
                break;
            case TURRET:
                entity =  (new Turret(new Vector2(x, y), angle, world, team));
                break;
            case COMET:
                entity = new Comet(x, y, angle, 0, world, team);
//                entity = new ExplosiveMissile(new Vector2(x,y), 0, 0, 0, team, world );
                break;
        }

        GhostEntity ghost = new GhostEntity(entity, world);

        if (entity != null) {
            world.ghosts.put(entity, ghost);
        }

        return ghost;
    }
}
