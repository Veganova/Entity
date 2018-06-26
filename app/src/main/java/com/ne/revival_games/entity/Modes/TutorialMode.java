package com.ne.revival_games.entity.Modes;

/**
 * Created by veganova on 6/11/18.
 */

public enum TutorialMode implements BaseMode {

    GESTURE("Gestures"), NEXUSTUTORIAL("Nexus"), TURRETTUTORIAL("Turret");

    private String val;

    TutorialMode(String s) {
        this.val = s;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
