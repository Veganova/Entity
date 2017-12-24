package com.ne.revival_games.entity.WorldObjects;

import android.graphics.Paint;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;

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

        if(ent1 == null || ent2 == null){
            return false;
        }

        if (ent1.ghost && !ent2.isEffect(body2) || ent2.ghost && !ent1.isEffect(body1)) {
            return true;
        }

        else if(ent1.ghost || ent2.ghost || ent1.dead || ent2.dead){
            return false;
        }



        boolean continueContact = true;
        double damage1 = ent1.getDamage(body1);
        double damage2 = ent2.getDamage(body2);

        if(ent1.isEffect(body1) && ent2.isEffect(body2)){
            return false;
        }
        else if(ent1.isEffect(body1)){
            ent1.onCollision(ent2, body1, 0);
            return false;
        }
        else if(ent2.isEffect(body2)){
            ent2.onCollision(ent1, body2, 0);
            return false;
        }

        if(ent1.isCollisionAuthority) {
            continueContact = ent1.onCollision(ent2, body1, damage2);
            ent2.onCollision(ent1, body2, damage1);
        }
        else if(ent2.isCollisionAuthority){
            continueContact = ent2.onCollision(ent1, body2, damage1);
            ent1.onCollision(ent2, body1, damage2);

        }
        else {
            if (ent1.health > 0 && ent2.health > 0) {
                //if either entity wants to continue the contact
                continueContact = ent1.onCollision(ent2, body1, damage2);
                continueContact = ent2.onCollision(ent1, body2, damage1) || continueContact;
            }
        }

        if(!continueContact){
            if(ent1.health <= 0){
                world.objectDatabase.remove(ent1.shape.body);
            }
            if(ent2.health <= 0){
                world.objectDatabase.remove(ent2.shape.body);
            }
        } else {
            if (ent1.team != ent2.team) {
                Sounds.getInstance(null).playSound(Sounds.SOUND_TYPE.HIT);
            }
        }

        return continueContact;
    }
}
