package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

public abstract class EntityLeaf {
    public final String name;

    EntityLeaf(String name) {
        this.name = name;
    }

    abstract Entity produce(double x, double y, double angle, MyWorld world, Team team);

    @Override
    public String toString() {
        return name;
    }
}