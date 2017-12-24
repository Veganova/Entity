//package com.ne.revival_games.entity.WorldObjects.Players;
//
//import com.ne.revival_games.entity.MainActivity;
//import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Launcher;
//import com.ne.revival_games.entity.WorldObjects.Entity.Team;
//import com.ne.revival_games.entity.WorldObjects.MyWorld;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by vishn on 12/23/2017.
// */
//
//public class AI_Bot extends Launcher {
//    private boolean duringRound = false;
//    private boolean filledAmmo = false;
//    private boolean inRound = false;
//    private int level = 1;
//
//
//    public AI_Bot(double width, double height, MyWorld world, Team team) {
//        super(width, height, world, team);
//    }
//
//    @Override
//    public void update() {
//        if(duringRound) {
//
//        }
//    }
//
//
//
//    //fill the ammo each level
//    public void fillAmmo() {
//        try {
//            InputStream is;
//
//            is = MainActivity.giveContext().getAssets().open("levels.json");
//            byte[] buffer = new byte[is.available()];
//            is.read(buffer);
//            is.close();
//
//            JSONObject json = (new JSONObject(new String(buffer, "UTF-8")))
//                    .getJSONObject("levels");
//
//            JSONArray array = json.getJSONObject(Integer.toString(level)).getJSONArray("units");
//
//            for(int i = 0; i < array.length(); ++i) {
//                String unit_type = array.getString(i);
//                int num = json.getJSONObject(Integer.toString(level))
//                        .getJSONObject(unit_type).getInt("number");
//                while(num > 0) {
//                    ammoSet.add(unit_type);
//                    --num;
//                }
//            }
//
//
//
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//        catch(JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //wrap_up
//    public void emptySet() {
//        while (ammoSet.size() > 0) {
//            fireRandom(Integer.toString(level), ammoSet.size());
//        }
//    }
//
//    //interim
//    public void interim() {
//        if(!level_updated) {
//            ++level;
//            ammoSet.clear();
//            fillAmmo();
//        }
//    }
//}
