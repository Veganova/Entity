package com.ne.revival_games.entity.Modes;

/**
 * Created by veganova on 6/11/18.
 */
public enum GameMode implements BaseMode {
    SINGLEPLAYER("Single Player"), MULTIPLAYER("Multiplayer"), TUTORIAL("Tutorial");

    private String val;

    GameMode(String s) {
        this.val = s;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
