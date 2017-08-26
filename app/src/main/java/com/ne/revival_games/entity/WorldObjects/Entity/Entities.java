package com.ne.revival_games.entity.WorldObjects.Entity;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Comet;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Veganova on 7/1/2017.
 */

public enum Entities {

    COMET(new EntityLeaf[]{new EntityLeaf("COMET") {
        @Override
        Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Comet(x, y, angle, 0, world, team);
        }
    }}),


    NEXUS(new EntityLeaf[]{new EntityLeaf("NEXUS") {
        @Override
        Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Nexus(x, y, angle, world, team);
        }
    }}),

    BARRIER(new EntityLeaf[]{new EntityLeaf("BARRIER") {
        @Override
        Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Barrier(x, y, angle, world, team);
        }
    }}),
    TURRET(new EntityLeaf[]{new EntityLeaf("TURRET-2") {
        @Override
        Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Turret(new Vector2(x, y), angle, world, team, 2);
        }
    }
            ,
            new EntityLeaf("TURRET-3") {
                @Override
                Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Turret(new Vector2(x, y), angle, world, team, 3);
                }
            }
    });

    public List<EntityLeaf> produceables;

    Entities(EntityLeaf[] leafs) {
        produceables = Arrays.asList(leafs);
    }

    public EntityLeaf getLeaf(String id) {
        for (EntityLeaf leaf: this.produceables) {
            if (leaf.name.equals(id)) {
                return leaf;
            }
        }
        System.out.println("RETURNING DEFAULT FROM getLeaf!!");
        return this.getDefaultLeaf();
    }

    public EntityLeaf getDefaultLeaf() {
        return this.produceables.get(0);
    }

    public static Entities random() {
        return Entities.values()[new Random().nextInt(Entities.values().length)];
    }


}

// x, y, angle => new Turret(x, y, angle, 0, TEAM, etc.)
