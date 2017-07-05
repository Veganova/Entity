package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Paint;

/**
 * Created by Veganova on 6/30/2017.
 */

public class GhostEntity {
    public Entity entity;

    public GhostEntity(Entity entity) {
        this.entity = entity;

        this.entity.shape.setPaint(Paint.Style.STROKE);
        this.entity.ghost = true;
        this.entity.invulnerable = true;
    }

    public boolean canPlace() {
        //this.entity.shape.body.b
        return this.entity.shape.body.getContacts(false).size() == 0;
    }

    public Entity place() {
        System.out.println("Born: " + this.entity);
        this.entity.shape.setPaint(Paint.Style.FILL);
        this.entity.ghost = false;
        this.entity.invulnerable = false;
        return entity;
    }


}
