package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

public abstract class EntityLeaf {
    public String name;

    public EntityLeaf() {

    }

    /**
     * Use in the case where Entities enum name is not user friendly
     */
    public EntityLeaf(String name) {
        this.name = name;
    }

    public abstract Entity produce(double x, double y, double angle, MyWorld world, Team team);

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
