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

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Veganova on 7/1/2017.
 */

public enum Entities {

    COMET(new EntityLeaf[]{new EntityLeaf("COMET") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Comet(x, y, angle, 0, world, team, producerName);
        }
    }}),


    NEXUS(new EntityLeaf[]{new EntityLeaf("NEXUS") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Nexus(x, y, angle, world, team);
        }
    }}),
    TOTEM(new EntityLeaf[]{new EntityLeaf("TOTEM") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Totem(x, y, angle, world, team, 3, producerName).prime();
        }
    }}),

    BARRIER(new EntityLeaf[]{new EntityLeaf("BARRIER SMALL") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Barrier(x, y, angle, world, team);
        }
    },
            new EntityLeaf("BARRIER LARGE") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Barrier(x, y, angle, world, team);
                }
            }
    }),
    TURRET(new EntityLeaf[]{
            new EntityLeaf("TURRET-1", new String[]{"1_turret"}) {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Turret(x, y, angle, world, team, 1, producerName + " 1_");
                }
            },
            new EntityLeaf("TURRET-2",  new String[]{"2_turret"}) {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Turret(x, y, angle, world, team, 2, producerName + " 2_");
        }
    },
            new EntityLeaf("MINE") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Mine(x, y, angle, team, world, producerName).prime();
                }
            },
          new EntityLeaf("ROCKET") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Rocket(x, y, angle, 0, team, world, producerName).prime();
                }
            },
            new EntityLeaf("SHOCKCAN") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new ShockwaveCanister(x, y, angle, team, world, producerName).prime();
                }
            },

            new EntityLeaf("TURRET-3",  new String[]{"3_turret"}) {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Turret(x, y, angle, world, team, 3, producerName + " 3_");
                }
            }
    });

    public EntityLeaf produceable;

    Entities(EntityLeaf leaf) {
        produceable = Arrays.asList(leafs);
    }

    public EntityLeaf getLeaf(String id) {
        for (EntityLeaf leaf: this.produceables flatten out the menu since there is no nesting within Entities) {
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
                if (leaf.name.toLowerCase().equals(id.toLowerCase())
                        || checkAltNames(leaf, id)) {
                    return leaf;
                }
            }
        }

        throw new IllegalArgumentException("Not a valid Leaf ID: " + id);
    }

    public static boolean checkAltNames(EntityLeaf leaf, String id) {
        for(int i = 0; i < leaf.altnames.length; ++i) {
            if(id.toLowerCase().equals(leaf.altnames[i].toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public EntityLeaf getDefaultLeaf() {
        return this.produceables.get(0);
    }

    public static Entities random() {
        return Entities.values()[new Random().nextInt(Entities.values().length)];
    }

    public static EntityLeaf fromString(String ent) {
        String uEnt = ent.toUpperCase();

        for (Entities e: values()) {

            for (EntityLeaf leaf : e.produceables) {
                if (leaf.name.toUpperCase().equals(uEnt)) {
                    return leaf;
                }
            }
        }
        // default is defence
        throw new IllegalArgumentException("No such entity exists " + ent);
    }


}

// x, y, angle => new Turret(x, y, angle, 0, TEAM, etc.)
