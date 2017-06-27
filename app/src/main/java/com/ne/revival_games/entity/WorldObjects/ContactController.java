package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;
import org.dyn4j.dynamics.contact.SolvedContactPoint;

/**
 * Created by vishn on 6/27/2017.
 */

public class ContactController extends ContactAdapter {

    MyWorld world;

    public ContactController(MyWorld world){
        this.world = world;
    }

    @Override
    public void end(ContactPoint point) {
        Entity ent1 = world.objectDatabase.get(point.getBody1());
        Entity ent2 = world.objectDatabase.get(point.getBody2());
        if(ent1.health <= 0){
            world.bodiestodelete.add(ent1.shape.body);
        }
        if(ent2.health <= 0){
            world.bodiestodelete.add(ent2.shape.body);
        }

    }
}
