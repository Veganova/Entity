package com.ne.revival_games.entity.WorldObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A list of json keys in order of least to most deep. Example (turret -> barrel -> missile -> shooting_speed -> 0.05)
 */
public class Query extends ArrayList<String> {

    public Query(String... queryParts) {
        this.addAll(Arrays.asList(queryParts));
    }

    public Query(List<String> partQuery, String... restQuery) {
        this.addAll(partQuery);
        this.addAll(Arrays.asList(restQuery));
    }

}
