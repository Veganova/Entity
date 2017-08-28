package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * NOTE: Static variables can sustain values added to them over programs.
 */
public enum Team {
    OFFENSE(Color.rgb(40, 240, 200)), DEFENCE(Color.rgb(236, 38, 101)), NEUTRAL(Color.MAGENTA);

    //yellow

    Team(int color) {
        this.teamColor = color;
    }

    private int teamColor;
//    private List<Entity> teamObjects = new ArrayList<>();

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


//    public void empty() {
//        this.teamObjects.clear();
//    }

    public void applyTeamColor(Entity entity) {
        entity.setColor(this.teamColor);
    }
}

//    public void add(Entity entity) {
//        // TODO: 7/11/2017 test later with this and remove's outputs
////        System.out.println(this + " adding - " + this.teamObjects.size());
////        System.out.println(entity);
//        entity.shape.setColor(this.teamColor);
//        this.teamObjects.add(entity);
//    }

//    public List<Entity> getTeamObjects() {
//        return this.teamObjects;
//    }
//
//    public void remove(Entity entity) {
////        System.out.println(this + " removing - " + this.teamObjects.size());
////        System.out.println(entity);
//        this.teamObjects.remove(entity);
//    }
//}
