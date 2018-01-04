package com.ne.revival_games.entity.WorldObjects.Players;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.Entities;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.EntityLeaf;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostFactory;
import com.ne.revival_games.entity.WorldObjects.Entity.Defence.Nexus;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Put in the correct world items from stored json
 */
public class InitializeWorld {
    /**
     * Give it a type that matches a section in the json object 'init.json'
     *
     * @param type  If the initializations contain a Defense Nexus it will return that so that the AI can use it to fire.
     */
    public static Nexus init(String type, MyWorld world) {
        InputStream is;
        Nexus target = null;
        try {
            is = MainActivity.giveContext().getAssets().open("init.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            JSONObject info = (new JSONObject(new String(buffer, "UTF-8")))
                    .getJSONObject(type);

            for (Iterator<String> it = info.keys(); it.hasNext(); ) {
                // specifies a unit type
                String entity = it.next();

                JSONArray arr = info.getJSONArray(entity);

                for (int i = 0 ; i < arr.length(); i += 1) {
                    JSONObject entityInfo = arr.getJSONObject(i);

                    EntityLeaf entType = Entities.fromString(entity);

                    double x = entityInfo.getDouble("x");
                    double y = entityInfo.getDouble("y");
                    double angle = entityInfo.getDouble("angle");

                    Team team = Team.fromString(entityInfo.getString("team"));
                    //TODO: TURRET WONT WORK HERE!
                    GhostEntity n = GhostFactory.produce(entType, x, y, angle, world, team, "");
                    if (team == Team.DEFENCE && entType.name.equals("NEXUS")) {
                        target = (Nexus)n.entity;
                    }
                    if (n.canPlace()) {
                        n.place(team);
                    } else {
                        throw new IllegalArgumentException("Invalid position for the " + entType.name + " at (" + x + ", " + y + ")");
                    }
                }

            }

        } catch(IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return target;
    }


}
