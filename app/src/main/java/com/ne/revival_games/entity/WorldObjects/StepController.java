package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Players.Player;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Step;
import org.dyn4j.dynamics.StepAdapter;
import org.dyn4j.dynamics.World;

import java.util.ArrayList;

public class StepController extends StepAdapter {
    private MyWorld earth;

    public StepController(MyWorld world) {
        this.earth = world;
    }

    @Override
    public void begin(Step step, World world) {
        // deals with concurrent modification error by creating all the projectile objects
        // (things that are shot) before the database loop
        ArrayList<GhostEntity> ghosts = new ArrayList<>(earth.ghosts.values());

        for (int x = 0; x < ghosts.size(); x++) {
            ghosts.get(x).isColliding();
        }

        for (Player player : this.earth.players) {
            player.update();
        }

        ArrayList<Entity> updater = new ArrayList<>(earth.objectDatabase.values());

        for (int x = 0; x < updater.size(); x++){
            earth.bounds.checkOutside(updater.get(x));
            if(!updater.get(x).ghost)
                updater.get(x).update(earth);
        }
    }

    public void updatePerformed(Step step, World world) {

    }

    @Override
    public void end(Step step, World world) {
        for (int i = 0; i < earth.bodiestodelete.size(); i += 1) {
            Body body = earth.bodiestodelete.get(i);
            Entity toDelete = earth.objectDatabase.get(body);
            if(toDelete != null){
                if(toDelete.team != null)
                    toDelete.team.getTeamObjects().remove(toDelete);
                toDelete.onDeath(earth);
                earth.objectDatabase.remove(body);
            }
            earth.engineWorld.removeBody(body);
            earth.bodiestodelete.remove(body);
        }
//        System.out.println("KEYSET - " + earth.objectDatabase.keySet().size());
    }


}
