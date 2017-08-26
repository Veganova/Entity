package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.contact.ContactAdapter;
import org.dyn4j.dynamics.contact.ContactPoint;

/**
 * Created by vishn on 6/27/2017.
 */

public class ContactController extends ContactAdapter {

    private MyWorld world;

    public ContactController(MyWorld world){
        this.world = world;
    }

    @Override
    public void end(ContactPoint point) {
        Entity ent1 = world.objectDatabase.get(point.getBody1());
        Entity ent2 = world.objectDatabase.get(point.getBody2());

        if(ent1.health <= 0) {
            world.objectDatabase.remove(ent1.shape.body);
        }
        if(ent2.health <= 0) {
            world.objectDatabase.remove(ent2.shape.body);
        }
        //System.out.println("ghost contact - " + (ent1.ghost || ent2.ghost));
    }

    @Override
    public boolean preSolve(ContactPoint point) {
        Entity ent1 = world.objectDatabase.get(point.getBody1());
        Entity ent2 = world.objectDatabase.get(point.getBody2());
        return !(ent1.ghost || ent2.ghost);
    }


    public boolean begin(ContactPoint point) {
        //System.out.println("BEGIN");
        Entity ent1 = world.objectDatabase.get(point.getBody1());
        Entity ent2 = world.objectDatabase.get(point.getBody2());
        return !(ent1.ghost || ent2.ghost);
    }
}
