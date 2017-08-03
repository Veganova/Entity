package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import org.dyn4j.dynamics.Body;

import java.util.HashMap;

/**
 * Created by Veganova on 8/3/2017.
 */

public class Database extends HashMap<Body, Entity> {

    @Override
    public Entity put(Body key, Entity value) {
        super.put(key, value);
        key.setUserData(value);
        return value;
    }

    @Override
    public Entity get(Object key) {
        if (key instanceof Body) {
            Body b = (Body)key;
            // TODO: 8/3/2017 THIS CAUSES THREADS TO FREEZE.. 100%+ cpu usage
            System.out.println("USER DATA - " + (b.getUserData() instanceof Entity));
            Entity val =  (Entity) b.getUserData();
        }
        return super.get(key);
    }
}
