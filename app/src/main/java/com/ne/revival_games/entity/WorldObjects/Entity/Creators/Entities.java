package com.ne.revival_games.entity.WorldObjects.Entity.Creators;

import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Barrier;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.BarrierLarge;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.BarrierSmall;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Mine;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Totem;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Turret;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.TurretOneBarrel;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.TurretThreeBarrel;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.TurretTwoBarrel;
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

    Comet(new EntityLeaf() {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Comet(x, y, angle, 0, world, team);
        }
    }),

    Nexus(new EntityLeaf() {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Nexus(x, y, angle, world, team);
        }
    }),
    Totem(new EntityLeaf() {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Totem(x, y, angle, world, team, 3).prime();
        }
    }),

    BarrierSmall(new EntityLeaf() {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new BarrierSmall(x, y, angle, world, team);
        }
    }),
    BarrierLarge(new EntityLeaf() {
         @Override
         public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
             return new BarrierLarge(x, y, angle, world, team);
         }
    }),
    TurretOneBarrel(new EntityLeaf("TURRET-1") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new TurretOneBarrel(x, y, angle, world, team);
        }
    }),
    TurretTwoBarrel(new EntityLeaf("TURRET-2") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new TurretTwoBarrel(x, y, angle, world, team);
        }
    }),
    TurretThreeBarrel(new EntityLeaf("TURRET-3") {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new TurretThreeBarrel(x, y, angle, world, team);
        }
    }),
    Mine(new EntityLeaf() {
        @Override
        public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
            return new Mine(x, y, angle, team, world).prime();
        }
    }),
    Rocket(new EntityLeaf() {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new Rocket(x, y, angle, 0, team, world).prime();
                }
            }),
    ShockwaveCanister(new EntityLeaf("SHOCKCAN") {
                @Override
                public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                    return new ShockwaveCanister(x, y, angle, team, world).prime();
                }
            });


    public EntityLeaf produceable;

    Entities(EntityLeaf leaf) {
        produceable = leaf;
        produceable.setName(this.toString());
    }

    public static EntityLeaf getLeaf(String enumName) {
        for (Entities e: Entities.values()) {
            if (e.toString().equals(enumName)) {
                return e.produceable;
            }
        }

        throw new IllegalArgumentException("Not a valid Leaf ID: " + enumName);
    }

    public static Entities random() {
        return Entities.values()[new Random().nextInt(Entities.values().length)];
    }
}

