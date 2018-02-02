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
    private double breakUntil = 0, roundDuration = 0;
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
               //waits in this state until callback
            //in a round of the game
               break;
           case IN_ROUND:
                if(ammoLeft() != 0) {
                    updateTarget();
                    firingRound(Integer.toString(level) + " ", roundDuration);
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

    private void prepNextRound() {
        double moneyForRound = MySettings.getNum(Team.OFFENSE.toString(), level + " money_awarded");

        for(Player player :this.world.getPlayers()) {
            if(player.team == Team.DEFENCE)
            player.addMoney(moneyForRound);
        }

        String levelString = Integer.toString(level) + " ";
        this.breakUntil = 40*MySettings.getNum("OFFENSE", Integer.toString(level) + " break");
        this.roundDuration = 40*MySettings.getNum("OFFENSE",
                levelString + "duration");
        this.rate = 40*MySettings.getNum("OFFENSE", levelString + "breakBetweenFiring");
        this.atOnce = (int) MySettings.getNum("OFFENSE", levelString + "ammoFiredAtOnce");
        this.atOnce_range =
                (int) MySettings.getNum("OFFENSE", levelString + "ammoFiredAtOnceVariance");
        fillAmmo();

    }

    private void updateTarget() {
        this.target = targetEntity.shape.body.getWorldCenter();
    }

}
