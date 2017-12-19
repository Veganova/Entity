package com.ne.revival_games.entity.WorldObjects;

/**
 * settings class passed into the MyWorld and encapsulated dyn4j World object
 * also maintain data on
 */

public class Settings extends org.dyn4j.dynamics.Settings {
    private static Settings myinstance = null;

    public Settings() {
        super();

    }

    public static Settings getSettings() {
        if(myinstance == null) {
            myinstance = new Settings();
        }

        return myinstance;
    }

    protected void configureCurrentSettings(String change) {

    }

    protected String getValue(String term) {

        return "";
    }
}
