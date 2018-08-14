package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

/**
 * Produces ghost entities which in turn produces entities.
 */
public class GhostFactory {

    /**
     * Produces a ghost entity at the given coordinates (mouse usually) of the given type.
     * Use this to place an entity correctly.
     *
     * @param toProduce use its .produce method to make the entity
     * @param x Give the x.
     * @param y Give the y.
     * @param angle
     * @return
     */
    // TODO: 7/1/2017 need some way to rotate here or in the ghost entities.
    public static GhostEntity produce(EntityLeaf toProduce, double x,
                                      double y, double angle, MyWorld world, Team team) {

        Entity entity = toProduce.produce(x, y, angle, world, team);
        GhostEntity ghost = new GhostEntity(entity, world);

        if (entity != null) {
            world.ghosts.put(entity, ghost);
        }

        return ghost;
    }
}
