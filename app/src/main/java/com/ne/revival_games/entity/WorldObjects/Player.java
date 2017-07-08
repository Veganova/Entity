package com.ne.revival_games.entity.WorldObjects;

/**
 * Created by Veganova on 7/7/2017.
 */

import android.graphics.Camera;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import java.util.List;

/**
 *
 -list of entities
 -Camera object that stores info about how to display the scene (zoom, translation)
 -money
 -has listeners specific to it
 -has its own menu
 -one ghost object - have this ghost class implement the listener as well?
 */
public class Player {
    private List<Entity> entities;
    private Camera camera;
    private int money;


}
