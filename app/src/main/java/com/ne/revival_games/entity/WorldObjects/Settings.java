package com.ne.revival_games.entity.WorldObjects;

import android.content.Context;
import android.content.res.AssetManager;

import com.ne.revival_games.entity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

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

    protected String getValue(String team, String term) {
        String json;
        try {
            InputStream is = MainActivity.getContext().getAssets().open("settings.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read();
            is.close();

            json = new String(buffer, "UTF-8");
            String [] query = term.split(" ");
            String result = null;

            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject obj = jsonArray.getJSONObject(i);

                if(obj.getString("name").equals(team)) {
                    result = findVal(obj, query);
                    if(result != null) {
                        return result;
                    }
                    else {
                        break;
                    }
                }
            }

            if(result == null) {
                result = findVal(jsonArray.getJSONObject(0), query);
            }

            return result;

        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String findVal(JSONObject obj, String[] query) {
            int cur = 0;

        while(true) {
            if(obj.has(query[cur])) {
                if(cur == query.length) {
                    try{
                        return obj.getString(query[cur]);
                    }
                    catch(JSONException e) {
                        e.printStackTrace();
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

            ++cur;
        }
    }

}
