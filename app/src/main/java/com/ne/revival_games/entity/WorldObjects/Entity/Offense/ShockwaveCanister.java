package com.ne.revival_games.entity.WorldObjects.Entity.Offense;

import com.ne.revival_games.entity.WorldObjects.Entity.Shared.ConditionalDestructible;
import com.ne.revival_games.entity.WorldObjects.Entity.Shared.Dummy;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.EMP;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.ExpandingEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;

import org.dyn4j.geometry.Vector2;

/**
 * Created by vishn on 7/27/2017.
 */

public class ShockwaveCanister extends ConditionalDestructible {
    public static int HEALTH = 20;
    private double lifeTime = 5000;

    public ShockwaveCanister(Vector2 location, double angle, double direction, double speed, Team team, MyWorld world) {
        super(direction, speed, team, "shockcan");
        this.shape = new ObjRectangle(50, 20);
        this.lifeTime = MySettings.getNum(team.toString(), "shockcan lifetime");
        this.shape.getBuilder(true, world).setXY(location.x, location.y).setAngle(angle).init();
        world.objectDatabase.put(this.shape.body, this);

    }

    @Override
    public void prime() {
        this.primed = true;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    protected boolean deathCondition() {
        if(this.health > 0){
            this.naturalDeath = true;
        }

        return this.primed && System.currentTimeMillis() > (startTime+lifeTime);
    }

    @Override
    public void onDeath(MyWorld world){
        if(this.naturalDeath) {
            ExpandingEffect emp = new EMP("shockcan", this.team, world);
            Dummy dum = new Dummy(this.shape.body.getWorldCenter().multiply(MyWorld.SCALE), emp, world, this.team);
            emp.addToWorld(dum.shape.body.getWorldCenter().multiply(MyWorld.SCALE), dum, world);
        }
        super.onDeath(world);
    }
}
