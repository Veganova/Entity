package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Mine;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Totem;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Comet;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Rocket;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.ShockwaveCanister;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
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
        public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
            return new Comet(x, y, angle, 0, world, team, producerName);
        }
    }}),


    NEXUS(new EntityLeaf[]{new EntityLeaf("NEXUS") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
            return new Nexus(x, y, angle, world, team, producerName);
        }
    }}),
    TOTEM(new EntityLeaf[]{new EntityLeaf("TOTEM") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
            return new Totem(x, y, angle, world, team, 3, producerName);
        }
    }}),

    BARRIER(new EntityLeaf[]{new EntityLeaf("BARRIER") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
            return new Barrier(x, y, angle, world, team, producerName);
        }
    }}),
    TURRET(new EntityLeaf[]{
            new EntityLeaf("TURRET-1") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
                    return new Turret(new Vector2(x, y), angle, world, team, 1, producerName);
                }
            },
            new EntityLeaf("TURRET-2") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
            return new Turret(new Vector2(x, y), angle, world, team, 2, "");
        }
    },
            new EntityLeaf("MINE") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
                    return new Mine(new Vector2(x, y), angle, 200.0, team, world, producerName);
                }
            },
          new EntityLeaf("ROCKET") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
                    return new Rocket(new Vector2(x, y), angle, 200.0, 20, team, world, producerName);
                }
            },
            new EntityLeaf("EMP") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
                    return new ShockwaveCanister(new Vector2(x, y), angle, 200.0, 20, team, world, producerName);
                }
            },

            new EntityLeaf("TURRET-3") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team, String producerName) {
                    return new Turret(new Vector2(x, y), angle, world, team, 3, producerName);
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

    public static EntityLeaf findLeaf(String id) {
        for (Entities e: Entities.values()) {
            for (EntityLeaf leaf: e.produceables) {
                if (leaf.name.equals(id) || leaf.name.equals(id.toLowerCase())) {
                    return leaf;
                }
            }
        }

        throw new IllegalArgumentException("Not a valid Leaf ID");
    }

    public EntityLeaf getDefaultLeaf() {
        return this.produceables.get(0);
    }

    public static Entities random() {
        return Entities.values()[new Random().nextInt(Entities.values().length)];
    }


}

// x, y, angle => new Turret(x, y, angle, 0, TEAM, etc.)
