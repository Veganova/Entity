package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.MainActivity;

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
    private static MySettings myinstance = null;

    public MySettings() {
        super();

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
        String json = null, result = null;
        String [] query = term.split(" ");

        try {
            InputStream is = MainActivity.giveContext().getAssets().open("settings.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            if(!team.equals("GENERAL")) {
                for(int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);

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


            //queries the general section (SHOULD ALWAYS BE SECTION 2)
            result = findVal(jsonArray.getJSONObject(1), query, 0);
            if(result != null) {
                return result;
            }

            //queries the default section (SHOULD ALWAYS BE SECTION 1)
            for(int x = 1; x < query.length; ++x) {
                result = findVal(jsonArray.getJSONObject(0), query, x);
                if(result != null) {
                    return result;
                }
            }

            return result;
        }
        catch(IOException e) {
            System.out.println(Arrays.asList(query));
            e.printStackTrace();
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static double getNum(String team, String query) {
        System.out.println(query);
        return Double.parseDouble(get(team, query));
    }

    private static String findVal(JSONObject obj, String[] query, int start) {

        for(int cur = start; cur < query.length; ++cur) {
            if(obj.has(query[cur])) {
                if(cur == query.length-1) {
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
        }

        return null;
    }

}
