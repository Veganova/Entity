package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veganova on 6/10/2017.
 */

public enum Team {
    DEFENCE(Color.rgb(167,119,208)), OFFENSE(Color.rgb(236,38,101)), NEUTRAL(Color.MAGENTA);

    Team(int color) {
        this.teamColor = color;
    }

    private int teamColor;
    private List<Entity> teamObjects = new ArrayList<>();

    public boolean opposite(Team other) {
        return (this != other);
    }

    public Team getOpposite() {
        if (this == DEFENCE)
            return OFFENSE;
        else if (this == OFFENSE)
            return DEFENCE;
        else
            return NEUTRAL;
    }

    public void add(Entity entity) {
        // TODO: 7/11/2017 test later with this and remove's outputs
//        System.out.println(this + " adding - " + this.teamObjects.size());
//        System.out.println(entity);
        entity.shape.setColor(this.teamColor);
        this.teamObjects.add(entity);
    }

    public List<Entity> getTeamObjects() {
        return this.teamObjects;
    }

    public void remove(Entity entity) {
//        System.out.println(this + " removing - " + this.teamObjects.size());
//        System.out.println(entity);
        this.teamObjects.remove(entity);
    }
}
