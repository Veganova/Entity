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

import org.dyn4j.geometry.Vector2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Put in the correct world items from stored json.
 * Initializes an AI as well.
 */
public class InitializeWorld {

    /**
     * Give it a type that matches a section in the json object 'init.json'.
     * If the initializations contain a Defense Nexus it will use that so that the AI can use it to fire.
     *
     * @param type  Returns an AI_Bot if specified
     */
    public static AI_Bot init(String type, MyWorld world) {
        InputStream is;
        Nexus target = null;
        try {
            is = MainActivity.giveContext().getAssets().open("init.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            JSONObject info = (new JSONObject(new String(buffer, "UTF-8")))
                    .getJSONObject(type);


            JSONObject units = info.getJSONObject("units");
            for (Iterator<String> it = units.keys(); it.hasNext(); ) {
                // specifies a unit type
                String entity = it.next();


                JSONArray arr = units.getJSONArray(entity);

                for (int i = 0 ; i < arr.length(); i += 1) {
                    JSONObject entityInfo = arr.getJSONObject(i);

                    EntityLeaf entType = Entities.fromString(entity);

                    double x = entityInfo.getDouble("x");
                    double y = entityInfo.getDouble("y");
                    final double dx = entityInfo.has("dx") ? entityInfo.getDouble("dx") : 0;
                    final double dy = entityInfo.has("dy") ? entityInfo.getDouble("dy") : 0;

                    double angle = entityInfo.getDouble("angle");

                    Team team = Team.fromString(entityInfo.getString("team"));
                    //TODO: TURRET WONT WORK HERE!
                    GhostEntity n = GhostFactory.produce(entType, x, y, angle, world, team, "");


                    if (team == Team.DEFENCE && entType.name.toUpperCase().equals("NEXUS")) {
                        target = (Nexus)n.entity;
                    }
                    if (n.canPlace()) {
                        n.place(team, new GhostEntity.Callback<Entity>() {
                            @Override
                            public void apply(Entity entity) {
                                entity.setVelocity(new Vector2(dx, dy));
                            }
                        });
                    } else {
                        throw new IllegalArgumentException("Invalid position for the " + entType.name + " at (" + x + ", " + y + ")");
                    }
                }
            }

            if (info.getBoolean("ai") && target != null) {
                return new AI_Bot(2000, 2000, world, Team.OFFENSE, target);
            }

        } catch(IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


}
