package com.ne.revival_games.entity.WorldObjects.Entity;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;

/**
 * NOTE: Static variables can sustain values added to them over programs.
 */
public enum Team {
    DEFENCE(Color.rgb(40, 240, 200)), NEUTRAL(Color.MAGENTA), OFFENSE(Color.rgb(236, 38, 101));


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

    public int getColor() {
        return teamColor;
    }


    /**
     * Helper method for conversion from string data (in json files) to the Team enumerator type
     *
     * @param team
     * @return
     */
    public static Team fromString(String team) {
        String uTeam = team.toUpperCase();

        for (Team t: values()) {
            if (t.toString().toUpperCase().equals(uTeam)) {
                return t;
            }
        }
        // default is defence
        return DEFENCE;
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
