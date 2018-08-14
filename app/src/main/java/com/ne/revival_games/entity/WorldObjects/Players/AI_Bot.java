package com.ne.revival_games.entity.WorldObjects.Players;

import com.ne.revival_games.entity.MainActivity;
import com.ne.revival_games.entity.WorldObjects.Entity.Creators.GhostEntity;
import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Offense.Launcher;
import com.ne.revival_games.entity.WorldObjects.Entity.Pair;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;
import com.ne.revival_games.entity.WorldObjects.FrameTime;
import com.ne.revival_games.entity.WorldObjects.MySettings;
import com.ne.revival_games.entity.WorldObjects.MyWorld;
import com.ne.revival_games.entity.WorldObjects.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;


/**
 * CURRENTLY TEAM IS ASSUMED TO BE OFFENSELEVEL. WILL NOT WORK WITH TEAM SET TO DEFENSE.
 */
public class AI_Bot extends Launcher {
    private State curState;
    private double breakUntil = 0, roundDuration = 0;
    private int max_level = 0;
    private Entity targetEntity;

    public enum State {
        IN_ROUND, NOT_READY_BREAK, READY_BREAK, GAME_OVER
    }


    public AI_Bot(double width, double height, MyWorld world, Team team, Entity target) {
        super(width, height, world, team);
        this.curState = State.NOT_READY_BREAK;
        // Set team query to random value because we don't want to have a level attached the front of the query (which happens automatically in Mysettings for offense team)
        this.max_level = (int) MySettings.getConfigNum(this.team.toString(), new Query("max_level"));
        this.setLevel(0);
        this.targetEntity = target;
    }

    @Override
    public void update() {
       switch (curState) {
           case NOT_READY_BREAK:
               this.setLevel(level + 1);
               System.out.println("LEVEL " + level + " +++++++++++++++++++++++++++++++++++++++++++");
               final int curlevel = level;
               if(max_level > level) {
                   emptySet();
                   prepNextRound();
                   this.curState = State.READY_BREAK;

                   //sets call_back for round start & round end
                   FrameTime.addCallBackAtDeltaFrames((long) breakUntil, new Runnable() {
                       @Override
                       public void run() {
                           curState = State.IN_ROUND;
                           System.out.println("BREAK iS OVER " + FrameTime.getTime());
                           FrameTime.addCallBackAtDeltaFrames((long) roundDuration + 2, new Runnable() {
                               @Override
                               public void run() {
                                   System.out.println("ROUND IS OVER: " + FrameTime.getTime());
                                   if(curlevel == level)
                                   curState = State.NOT_READY_BREAK;
                               }
                           });
                       }
                   });
               }
               else {
                   curState = State.GAME_OVER;
               }
           case READY_BREAK:
               // waits in this state until callback
               // in a round of the game
               break;
           case IN_ROUND:
                if(ammoLeft() != 0) {
                    updateTarget();
                    firingRound(roundDuration);
                }
                else {
                    this.curState = State.NOT_READY_BREAK;
                }
               break;
                //end of the game
           case GAME_OVER:
               System.out.println("YOU WIN!");
               break;
       }
    }

    private void setLevel(int level) {
        this.level = level;
        MySettings.getSettings().setLevel(level);
    }


    //make static changes prior to place
    @Override
    protected void modifyEntity(GhostEntity ghost) {
        ghost.entity.normalizeBot(ghost, getDirection(ghost.entity.shape.body.getWorldCenter()));
    }


    @Override
    protected double getSpeed(String unit_type) {
        return MySettings.getEntityNum(this.team.toString(), new Query(unit_type, "speed"), true);
    }

    @Override
    protected double getAngularVelocity(String unit_type) {
        return MySettings.getEntityNum(this.team.toString(), new Query(unit_type, "angular_velocity"), true);
    }


    //fill the ammo each level
    public void fillAmmo() {
        try {
            InputStream is;

            is = MainActivity.giveContext().getAssets().open("levels.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            JSONObject levelsJson = (new JSONObject(new String(buffer, "UTF-8"))).getJSONObject("levels").getJSONObject(Integer.toString(level));

            JSONObject unitJson = levelsJson.getJSONObject("units");

            Iterator<String> units = unitJson.keys();
            while (units.hasNext()) {
                String unit_type = units.next();
                int num = unitJson.getJSONObject(unit_type).getInt("number");
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
            fireRandom();
        }
    }

    //get break time
    private void setBreakTime() {
        breakUntil =  System.currentTimeMillis() +
                1000 * MySettings.getConfigNum(this.team.toString(), new Query("break"));
    }

    private void prepNextRound() {
        double moneyForRound = MySettings.getConfigNum(this.team.toString(), new Query("money_awarded"));

        for(Player player :this.world.getPlayers()) {
            if(player.team == Team.DEFENCE)
            player.addMoney(moneyForRound);
        }


        this.breakUntil = 40*MySettings.getConfigNum(this.team.toString(), new Query("break"));
        this.roundDuration = 40*MySettings.getConfigNum(this.team.toString(), new Query("duration"));
        this.rate = 40*MySettings.getConfigNum(this.team.toString(), new Query("breakBetweenFiring"));
        this.atOnce = (int) MySettings.getConfigNum(this.team.toString(), new Query("ammoFiredAtOnce"));
        this.atOnce_range = (int) MySettings.getConfigNum(this.team.toString(), new Query("ammoFiredAtOnceVariance"));
        fillAmmo();

    }

    private void updateTarget() {
        this.target = targetEntity.shape.body.getWorldCenter();
    }

}
