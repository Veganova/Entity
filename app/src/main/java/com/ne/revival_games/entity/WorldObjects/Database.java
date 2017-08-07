package com.ne.revival_games.entity.WorldObjects;

import android.util.Pair;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;

import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Veganova on 8/3/2017.
 */

public class Database extends HashMap<Body, Entity> {

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
//    @Override
//    public Entity get(Object key) {
//        if (key instanceof Body) {
//            Body b = (Body)key;
////            System.out.println("USER DATA - " + (b.getUserData() instanceof Entity));
//            Entity val =  (Entity) b.getUserData();
//            return val;
////            System.out.println(val);
//        }
//        return super.get(key);
//    }

    @Override
    public Entity put(Body key, Entity entity) {
        this.toAdd.add(new Pair<Body, Entity>(key, entity));

        return this.get(key);
    }

    public void addPendingAdditions() {
        while (!this.toAdd.isEmpty()) {
                Pair<Body, Entity> p = this.toAdd.poll();
                super.put(p.first, p.second);
            }

    }

    public void removePendingDeletions(MyWorld world) {
            while (!this.toRemove.isEmpty()) {
                Body body = this.toRemove.poll();
                Entity toDelete = this.get(body);
                if (toDelete != null) {
                    if (toDelete.team != null)
                        toDelete.team.getTeamObjects().remove(toDelete);
                    toDelete.onDeath(world);
                    super.remove(body);
                }
                world.engineWorld.removeBody(body);
            }

    }

    @Override
    public Entity remove(Object object) {
        if (object instanceof Body) {
            Body body = (Body)object;
            this.toRemove.add(body);
            return this.get(body);
        } else {
            return null;
        }
    }
}
