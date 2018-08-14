package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.Aim.SimpleAim;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

public class Medic extends TurretOneBarrel {
    //TODO: "medic_turret"

    public Medic(double x, double y, double angle, MyWorld world, Team team) {
        super(x, y, angle, world, team);
        this.logic = new SimpleAim(this, world.objectDatabase,
                800, this.team.getOpposite());
    }

    @Override
    protected EntityLeaf getProjectile(final Barrel b) {
        return new EntityLeaf("Medic Turret") {
            @Override
            public Entity produce(double x, double y, double angle, MyWorld world, Team team) {
                return new HealShot(x, y, angle, 0, world, team, b);
            }
        };
    }



}
