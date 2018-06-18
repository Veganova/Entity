package com.ne.revival_games.entity.WorldObjects.Entity.Defence;

import com.ne.revival_games.entity.WorldObjects.Entity.ActiveBar;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.SpecialEffects.GravityEffect;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Shape.AShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ComplexShape;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjRectangle;
import com.ne.revival_games.entity.WorldObjects.Shape.ObjTriangle;

import org.dyn4j.geometry.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vishn on 6/29/2017.
 */

public class Nexus extends Entity {
    public static int HEALTH = 200;
    public static int MASS;
    public static int COST;
    private List<AShape> components;

    private GravityEffect gravEffect;

    public Nexus(double x, double y, double angle, MyWorld world, Team team, String tag) {
        super(angle, 0, team, tag + "nexus");
        components = new ArrayList<>();

        ObjRectangle rect = new ObjRectangle(120, 120);
        rect.getBuilder(false, world).setXY(0, 0)
                .setBasics(team.toString(), name_tag)
                .init();

        components.add(rect);

        double [] points1 = {0, 40, 0, -40, 40, 0};
        ObjTriangle tri1 = new ObjTriangle(points1);
        tri1.getBuilder(false, world).setXY(50, 0)
                .setBasics(team.toString(), name_tag)
                .init();
        components.add(tri1);

        double [] points2 = {0, -40, 0, 40, -40, 0};
        ObjTriangle tri2 = new ObjTriangle(points2);
        tri2.getBuilder(false, world).setXY(-50, 0)
                .setBasics(team.toString(), name_tag)
                .init();
        components.add(tri2);

        double [] points3 = {-40, 0, 40, 0, 0, 40};
        ObjTriangle tri3 = new ObjTriangle(points3);
        tri3.getBuilder(false, world).setXY(0, 50)
                .setBasics(team.toString(), name_tag)
                .init();
        components.add(tri3);

        double [] points4 = {40, 0,-40, 0,0, -40};
        ObjTriangle tri4 = new ObjTriangle(points4);
        tri4.getBuilder(false, world).setXY(0, -50)
                .setBasics(team.toString(), name_tag)
                .init();
        components.add(tri4);

        this.shape = new ComplexShape(components, x, y, world);
        world.objectDatabase.put(this.shape.body, this);
        this.team = team;
        gravEffect = new GravityEffect(this,
                MySettings.getNum(team.toString(), name_tag + " gravity_effect radius"),
                MySettings.getNum(team.toString(), name_tag + " gravity_effect strength"),
                new Vector2(0,0), world);
        gravEffect.setInitialState(false);
        this.addEffect(gravEffect);

        this.bar = new ActiveBar(this, 1f);
        this.bar.linkEffect(gravEffect);

//        this.bar.setPathType(ActiveBar.PathType.LINE, 200);
    }

    @Override
    public void interact() {
        super.interact();
    }

    boolean gameRunning = true;
    @Override
    public void onDeath(MyWorld world) {
        super.onDeath(world);

        if (gameRunning) {
            world.gameOver();
            gameRunning = false;
        }
    }


    public static List<String> getDescription() {
        return Arrays.asList(
                "This is the main entity that you will want to protect.",
                "Its special ability is a gravity well which can be toggled for a short duration and will charge back up after.",
                "The Nexus has plenty of health and is very hard to move.");
    }
}
