package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

/**
 * settings class passed into the MyWorld and encapsulated dyn4j World object
 * also maintain data on
 */

public class MySettings {
    private static JSONArray genSettings;
    private static JSONObject levelSettings;
    private static MySettings myinstance = null;

    public MySettings() {
        try {
            InputStream is;

            //initializes levelSettings
            is = MainActivity.giveContext().getAssets().open("levels.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            levelSettings = new JSONObject(new String(buffer, "UTF-8")).getJSONObject("levels");



            //initializes genSettings
            is = MainActivity.giveContext().getAssets().open("settings.json");
            buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            genSettings = new JSONArray(new String(buffer, "UTF-8"));
            System.out.println("Loaded settings JSONs");
        }
        catch(Exception e) {
            throw new IllegalArgumentException("FAILURE OF CONSTRUCTOR");
        }

    }

    public static MySettings getSettings() {
        if(myinstance == null) {
            myinstance = new MySettings();
        }

        return myinstance;
    }

    protected void configureCurrentSettings(String change) {

    }

    private int level = 0;
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Query order for offense: levels.json -> settings.json -> settings.json/default
     * Query order for defence: settings.json -> settings.json/default
     */
    public static String get(String team, Query queryOriginal, boolean unit, boolean useDefault) {
        String error = "";
        Query query = queryOriginal.getCopy();

        if(myinstance == null) {
            myinstance = new MySettings();
        }

        String result = null;

        //checks levels for 'OFFENSE' AI
        try {
            if(team.equals("OFFENSE")) {
                // offense is always gotten from levels.json. Adding on the level to the query.
                Query levelUnitsQ = null;
                if (unit) {
                    levelUnitsQ = query.getCopy();
                    levelUnitsQ.add(0, String.valueOf(myinstance.level));
                    levelUnitsQ.add(1, "units");

                    result = findVal(levelSettings, levelUnitsQ, 0);
                } else {
                    // Logic supporting values such as max_level that are not attached to a particular level but related.
                    result = findVal(levelSettings, query, 0);
                    if (result == null) {
                        Query levelQ = query.getCopy();
                        levelQ.add(0, String.valueOf(myinstance.level));
                        result = findVal(levelSettings, levelQ, 0);
                    }
                }

                if(result == null) {
                    error = "The query given is not available under the OFFENSE branch logic. Query: " +
                            (unit ? levelUnitsQ.toString() : query.toString()) + " on level: " + myinstance.level;
                }
            }


            if (result == null) {
                result = findVal(genSettings.getJSONObject(0), query,0);

                if (result == null && useDefault) {
                    // Assume that the query is looking for a unit value that exists in the default section. Check if the stat exists in default section.
                    Query defaultSecQ = new Query();
                    defaultSecQ.add("Default");
                    defaultSecQ.add(query.get(query.size() - 1));
                    result = findVal(genSettings.getJSONObject(0), defaultSecQ,0);

                    error += "\n no Default value for query: " + query.toString() + ". Default query: " + defaultSecQ.toString();
                }
            }

            if (result == null) {
                throw new IllegalArgumentException("No such json path available as shown in the query: " + query.toString() + "\n " + error);
            }

            return result;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Only to be used to query values for configuring the AI_Bot/Launcher such as number of rounds.
     */
    public static double getConfigNum(String team, Query query) {
        return Double.parseDouble(get(team, query, false, false));
    }

    /**
     * Only to be used for querying unit specific values such as health.
     */
    public static double getEntityNum(String team, Query query, boolean useDefault) {
        return Double.parseDouble(get(team, query, true, true));
    }

    private static String findVal(JSONObject obj, List<String> query, int start) {
        for(int cur = start; cur < query.size(); ++cur) {
            if(obj.has(query.get(cur))) {
                if(cur == query.size() - 1) {

                    //this could be slowing down the parse
                    try {
                        obj = obj.getJSONObject(query.get(cur));
                        double lower = obj.getDouble("lower"), upper = obj.getDouble("upper");
                        return Double.toString(Util.randomBetweenValues(lower, upper));
                    }
                    catch(JSONException e) {
                        try{
                            return obj.getString(query.get(cur));
                        }
                        catch(JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        obj = obj.getJSONObject(query.get(cur));
                    }
                    catch(JSONException e) {
                        System.out.println("query: " + query.toString());
                        e.printStackTrace();
                    }

                }

            }
            else {
                return null;
            }
        }

        return null;
    }

}
