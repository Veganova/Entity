package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * settings class passed into the MyWorld and encapsulated dyn4j World object
 * also maintain data on
 */

public class MySettings {
    private static JSONArray gensettings;
    private static JSONObject levelsettings;
    private static MySettings myinstance = null;

    public MySettings() {
        try {
            InputStream is;

            //initializes levelsettings
            is = MainActivity.giveContext().getAssets().open("levels.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            levelsettings = (new JSONObject(new String(buffer, "UTF-8")))
                    .getJSONObject("levels");



            //initializes gensettings
            is = MainActivity.giveContext().getAssets().open("settings.json");
            buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            gensettings = new JSONArray(new String(buffer, "UTF-8"));
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

    public static String get(String team, String term) {

        if(myinstance == null) {
            myinstance = new MySettings();
        }

        String result = null;
        String [] query = term.split(" ");

        //checks levels for 'OFFENSE' AI
        try {


            //custom AI lookup path into levels scheme (different query format)
            if(team.equals("OFFENSE")) {
                result = findVal(levelsettings, query, 0);
             if(result != null) {
                 return result;
             }
            }

            //looks through settings.json for general and player unit details
            if(!team.equals("GENERAL")) {
                for(int i = 0; i < gensettings.length(); ++i) {
                    JSONObject obj = gensettings.getJSONObject(i);

                    if(obj.getString("name").equals(team)) {
                        result = findVal(obj, query, 0);

                        if(result != null) {
                            return result;
                        }
                        else {
                            break;
                        }
                    }
                }
            }

            //queries the default section (SHOULD ALWAYS BE SECTION 0)
            for(int x = 0; x < query.length; ++x) {
                result = findVal(gensettings.getJSONObject(0), query, x);
                if(result != null) {
                    return result;
                }
            }

            return result;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static double getNum(String team, String query) {
        try {
            return Double.parseDouble(get(team, query));
        }
        catch(Exception e) {
            System.out.printf("Query was " + query);
            System.exit(1);
        }

        return 0;
    }

    private static String findVal(JSONObject obj, String[] query, int start) {

        for(int cur = start; cur < query.length; ++cur) {
            if(obj.has(query[cur])) {
                if(cur == query.length-1) {

                    //this could be slowing down the parse
                    try {
                        obj = obj.getJSONObject(query[cur]);
                        double lower = obj.getDouble("lower"), upper = obj.getDouble("upper");
                        return Double.toString(Util.randomBetweenValues(lower, upper));
                    }
                    catch(JSONException e) {
                        try{
                            return obj.getString(query[cur]);
                        }
                        catch(JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }
                else {
                    try {
                        obj = obj.getJSONObject(query[cur]);
                    }
                    catch(JSONException e) {
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
