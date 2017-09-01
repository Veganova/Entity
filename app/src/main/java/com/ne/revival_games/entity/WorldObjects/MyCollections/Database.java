package com.ne.revival_games.entity.WorldObjects.MyCollections;

import android.util.Pair;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import org.dyn4j.dynamics.Body;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Veganova on 8/3/2017.
 */

public class Database {

    private MyList values = new MyList();
    private ConcurrentLinkedQueue<Pair<Body, Entity>> toAdd = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Body> toRemove = new ConcurrentLinkedQueue<>();

    public Iterator<Entity> getTeamIterator(Team team) {
        return this.values.getTeamIterator(team);
    }

    public Iterator<Entity> getForwardTeamIterator(Team team) {
        return this.values.teamDeques.get(team).reverseIterator();
    }


    public MyList values() {
    return values;
}


    public Entity get(Body key) {
        return key.getEntity();
    }

    public void put(Body key, Entity entity) {
        key.setEntity(entity);
//        entity.team.applyTeamColor(entity);
        this.toAdd.add(new Pair<Body, Entity>(key, entity));
    }

    /**
     * Use this one when the object is created in game and not by a player (yet needs to belong to a player - ex bullet from a turret).
     *
     * @param key
     * @param entity
     * @param player
     */
    public void put(Body key, Entity entity, Player player) {
        key.setEntity(entity);
        entity.addToPlayer(player);
//        entity.team.applyTeamColor(entity);
        this.toAdd.add(new Pair<Body, Entity>(key, entity));
    }


    public String getPartitionData() {
        return this.values.sizes();
    }

    public void addPendingAdditions() {
        while (!this.toAdd.isEmpty()) {
            Pair<Body, Entity> p = this.toAdd.poll();
            this.values.add(p.second);
//            super.put(p.first, p.second);
        }
    }



    public void removePendingDeletions(MyWorld world) {
        while (!this.toRemove.isEmpty()) {

            Body body = this.toRemove.poll();
            Entity toDelete = this.get(body);
            System.out.println("REMOVING " + toDelete.simpleString());
            if (toDelete != null) {
//                if (toDelete.team != null)
//                    toDelete.team.getTeamObjects().remove(toDelete);
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

    public int size() {
        return this.values.size();
    }
}
