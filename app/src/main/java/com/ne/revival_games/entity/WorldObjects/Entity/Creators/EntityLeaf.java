package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

public abstract class EntityLeaf {
    public final String name;
    public final String[] altnames;

    public EntityLeaf(String name) {
        this.name = name;
        this.altnames = new String[0];
    }

    public EntityLeaf(String name, String [] altnames) {
        this.name = name;
        this.altnames = altnames;
    }

    public abstract Entity produce(double x, double y, double angle, MyWorld world, Team team);

    @Override
    public String toString() {
        return name;
    }
}
