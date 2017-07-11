package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;

/**
 * Created by vishn on 7/10/2017.
 */

public class GravityZone {
    private Entity host;
    private double forceStrength;
    public AShape shape;

    public GravityZone(AShape shape, Entity host, double force, MyWorld world){
        this.host = host;
        this.shape = shape;
        this.forceStrength = force;
    }

    public double doubleApplyGravity(Entity target){

        return 0;
    }



}
