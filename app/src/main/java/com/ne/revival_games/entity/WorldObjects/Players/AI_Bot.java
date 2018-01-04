package com.ne.revival_games.entity.WorldObjects.Players;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Comet;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Launcher;
import com.ne.revival_games.entity.WorldObjects.Entity.Pair;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.Entity.Util;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;

import org.dyn4j.geometry.Vector2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by vishn on 12/23/2017.
 */

public class AI_Bot extends Launcher {
    private State curState;
    private double breakUntil = 0, endTime = 0;
    private int max_level = 0;
    private Entity targetEntity;

    public enum State {
        IN_ROUND, NOT_READY_BREAK, READY_BREAK, GAME_OVER
    }


    public AI_Bot(double width, double height, MyWorld world, Team team, Entity target) {
        super(width, height, world, team);
        this.curState = State.NOT_READY_BREAK;
        this.max_level = (int) MySettings.getNum(team.toString(), "max_level");
        this.level = 0;
        this.targetEntity = target;
    }

    @Override
    public void update() {
       switch (curState) {
           case NOT_READY_BREAK:
               ++level;
               if(max_level > level) {
                   setBreakTime();
                   emptySet();
                   fillAmmo();
                   prepNextRound();
               }
               else {
                   curState = State.GAME_OVER;
               }
           case READY_BREAK:
               if(breakUntil <= System.currentTimeMillis()) {
                   break;
               }
               else {
                   this.curState = State.IN_ROUND;
               }
           case IN_ROUND:
                if(this.endTime >= System.currentTimeMillis() || ammoLeft() != 0) {
                    updateTarget();
                    firingRound(Integer.toString(level) + " ", endTime);
                }
                else {
                    curState = State.NOT_READY_BREAK;
                }
               break;
           case GAME_OVER:
               System.out.println("YOU WIN!");
               break;
       }
    }


    //make static changes prior to place
    @Override
    protected void modifyEntity(GhostEntity ghost) {
        ghost.entity.normalizeBot(ghost, getDirection(ghost.entity.shape.body.getWorldCenter()));
    }


    @Override
    protected double getSpeed(String unit_type) {
        return MySettings.getNum(team.toString(), Integer.toString(level) + " "
                + unit_type + " speed");
    }

    @Override
    protected double getAngularVelocity(String unit_type) {
        return MySettings.getNum(team.toString(), Integer.toString(level) + " "
                + unit_type + " angular_velocity");
    }


    //fill the ammo each level
    public void fillAmmo() {
        try {
            InputStream is;

            is = MainActivity.giveContext().getAssets().open("levels.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            JSONObject json = (new JSONObject(new String(buffer, "UTF-8")))
                    .getJSONObject("levels");

            JSONArray array = json.getJSONObject(Integer.toString(level)).getJSONArray("units");

            for(int i = 0; i < array.length(); ++i) {
                String unit_type = array.getString(i);
                int num = json.getJSONObject(Integer.toString(level))
                        .getJSONObject(unit_type).getInt("number");
                    ammoSet.add(new Pair<>(unit_type, num));
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }

    //wrap_up
    public void emptySet() {
        while (ammoLeft() > 0) {
            fireRandom(Integer.toString(level)+ " ");
        }
    }

    //get break time
    private void setBreakTime() {
        breakUntil =  System.currentTimeMillis() +
                1000 * MySettings.getNum("OFFENSE", Integer.toString(level) + " break");
    }

    private void prepNextRound() {
        String levelString = Integer.toString(level) + " ";
        this.endTime = System.currentTimeMillis() + 1000*MySettings.getNum("OFFENSE",
                levelString + "duration");
        this.rate = MySettings.getNum("OFFENSE", levelString + "breakBetweenFiring");
        this.atOnce = (int) MySettings.getNum("OFFENSE", levelString + "ammoFiredAtOnce");
        this.atOnce_range =
                (int) MySettings.getNum("OFFENSE", levelString + "ammoFiredAtOnceVariance");

    }

    private void updateTarget() {
        this.target = targetEntity.shape.body.getWorldCenter();
    }

}
