package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.contact.ContactConstraint;

/**
 * Created by vishn on 6/11/2017.
 */

class CollisionController extends CollisionAdapter {

    MyWorld world;

    CollisionController(MyWorld world) {
        this.world = world;
    }

    @Override
    public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
//        Entity ent1 = world.objectDatabase.get(body1);
//        Entity ent2 = world.objectDatabase.get(body2);
//        System.out.println(ent1.getClass() + " " + ent1.health);
//        System.out.println(ent2.getClass() + " " + ent2.health);
//        return ! ((ent1.health <= 0) || (ent2.health <=0));
        return true;
    }
}
