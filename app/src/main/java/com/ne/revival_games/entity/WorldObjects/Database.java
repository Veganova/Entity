package com.ne.revival_games.entity.WorldObjects;

import android.util.Pair;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Veganova on 8/3/2017.
 */

public class Database {

    private MyDeque values = new MyDeque();
    private ConcurrentLinkedQueue<Pair<Body, Entity>> toAdd = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Body> toRemove = new ConcurrentLinkedQueue<>();
//    @Override
//    public Entity put(Body key, Entity value) {
//        super.put(key, value);
//        Entity oldvalue = (Entity) key.getUserData();
//        key.setUserData(value);
//        return oldvalue;
//    }
//



    public Entity get(Body key) {
        return key.getEntity();
    }

    public void put(Body key, Entity entity) {
        key.setEntity(entity);
        this.toAdd.add(new Pair<Body, Entity>(key, entity));
    }


    public void addPendingAdditions() {
        while (!this.toAdd.isEmpty()) {
            Pair<Body, Entity> p = this.toAdd.poll();
            this.values.add(p.second);
//            super.put(p.first, p.second);
        }
    }

    public MyDeque values() {
        return values;
    }

    public void removePendingDeletions(MyWorld world) {
        while (!this.toRemove.isEmpty()) {

            Body body = this.toRemove.poll();
            Entity toDelete = this.get(body);
            System.out.println("REMOVING " + toDelete.simpleString());
            if (toDelete != null) {
                if (toDelete.team != null)
                    toDelete.team.getTeamObjects().remove(toDelete);
                toDelete.onDeath(world);
                this.values.remove(toDelete);

//                super.remove(body);

            }
            world.engineWorld.removeBody(body);
        }
    }


    public void remove(Body body) {
        body.setUserData(null);
        this.toRemove.add(body);
    }
}
