package com.ne.revival_games.entity.WorldObjects.Entity;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Veganova on 8/26/2017.
 */
public class Untargetable {

    private Entity owner;
    private boolean disallow = false;

    public enum FROM {
        ALLY, ENEMY, ALL
    }

    private List<Pair<Class<? extends Entity>, FROM>> data;

    public Untargetable(Entity owner) {
        this.owner = owner;
        data = new ArrayList<>();
    }

    public Untargetable addType(Class<? extends Entity> classType, FROM from) {
        data.add(new Pair<Class<? extends Entity>, FROM>(classType, from));
        return this;
    }

    public void toggle() {
        this.disallow = !disallow;
    }

    public void removeType(Class<? extends Entity> classType) {
        for (Pair<Class<? extends Entity>, FROM> p: this.data) {
            if (matchClass(p.first, classType)) {
                this.data.remove(p);
                return;
            }
        }
    }

    /**
     * Must pass the super class as the first argument currently.
     *
     * @param classType1
     * @param classType2
     * @return
     */
    private boolean matchClass(Class<? extends Entity> classType1, Class<? extends Entity> classType2) {
        return (classType1.equals(classType2) || classType1.isAssignableFrom(classType2));// || classType2.isAssignableFrom(classType1));
    }

    private boolean matchTeam(FROM type, Team toCheck) {
        switch(type) {
            case ALL:
                return true;
            case ALLY:
                return owner.team == toCheck;
            case ENEMY:
                return owner.team != toCheck;
            default:
                return false;
        }
    }

    public boolean isContactDisallowedWith(Entity contact) {
        if (this.disallow) {
            Class<? extends Entity> classType = contact.getClass();
            for (Pair<Class<? extends Entity>, FROM> p : this.data) {
                if (matchTeam(p.second, contact.team) && matchClass(p.first, classType)) {
                    return true;
                }
            }
        }
        return false;
    }

}
