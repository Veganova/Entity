package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;

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
    public boolean collision(Body body1, BodyFixture fixture1, Body body2,
                             BodyFixture fixture2, Penetration penetration) {

        Entity ent1 = world.objectDatabase.get(body1);
        Entity ent2 = world.objectDatabase.get(body2);


        if (ent1.ghost || ent2.ghost) {
            if (ent1.ghost) {
                ent1.shape.setPaint(Paint.Style.FILL);
            }
            if (ent2.ghost) {
                ent2.shape.setPaint(Paint.Style.FILL);
            }
            return true;
        }


        boolean continueContact = true;

        if(ent1.health > 0 && ent2.health > 0) {
            double damage1 = ent1.health;
            double damage2 = ent2.health;

            //if either entity wants to continue the contact
            continueContact = ent1.onCollision(ent2, damage2);
            continueContact = ent2.onCollision(ent1, damage1) || continueContact;
        }

        if(!continueContact){
            if(ent1.health <= 0){
                world.bodiestodelete.add(ent1.shape.body);
            }
            if(ent2.health <= 0){
                world.bodiestodelete.add(ent2.shape.body);
            }
        }

        return continueContact;
        //ent1.health > 0 && ent2.health > 0
    }
}
