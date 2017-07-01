package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Paint;

/**
 * Created by Veganova on 6/30/2017.
 */

public class GhostEntity {
    private Entity entity;

    public GhostEntity(Entity entity) {
        this.entity = entity;

        this.entity.shape.setPaint(Paint.Style.STROKE);
        this.entity.ghost = true;
        this.entity.invulnerable = true;
    }

    public boolean canPlace() {
        return this.entity.shape.body.getInContactBodies(true).size() == 0;
    }

    public void place() {
        if (canPlace()) {
            System.out.println("BORN NEXUS");
            this.entity.shape.setPaint(Paint.Style.FILL);
            this.entity.ghost = false;
            this.entity.invulnerable = false;
        } else {
            System.out.println("CANNOT PLACE THERE.");
        }
    }


}
