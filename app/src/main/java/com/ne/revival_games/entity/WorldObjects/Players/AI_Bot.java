package com.ne.revival_games.entity.WorldObjects.Players;

import com.ne.revival_games.entity.CustomViews.LoadingBar;
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
    private final LoadingBar loadingBar;
    private State curState;
    private double breakUntil = 0, roundDuration = 0;
    private int max_level = 0;
    private Entity targetEntity;
    private int cleanupTime;

    public enum State {
        IN_ROUND, NOT_READY_BREAK, READY_BREAK, WAIT_FOR_CLEANUP, GAME_OVER
    }


    public AI_Bot(double width, double height, MyWorld world, Team team, Entity target, LoadingBar loadingBar) {
        super(width, height, world, team);
        this.curState = State.NOT_READY_BREAK;
        // Set team query to random value because we don't want to have a level attached the front of the query (which happens automatically in Mysettings for offense team)
        this.max_level = (int) MySettings.getConfigNum(this.team.toString(), new Query("max_level"));
        this.setLevel(0);
        this.targetEntity = target;
        this.loadingBar = loadingBar;
    }

    private void setState(State state) {
        this.curState = state;
    }

    private void setState(State state, final String message) {
        this.curState = state;

//        loadingBar.setMessage(message);
        this.loadingBar.post(new Runnable() {
            @Override
            public void run() {
                loadingBar.setMessage(message);
            }
        });
    }

    /**
     * Sets the state and displays a loading bar configured to reduce by the duration given
     */
    private void setStateAndLoading(State state, final String message, final double duration) {
        this.curState = state;
//        loadingBar.setProgress((int) duration, message);
        this.loadingBar.post(new Runnable() {
            @Override
            public void run() {
                loadingBar.setProgress((int) duration, message);
            }
        });
    }

    @Override
    public void update() {
        switch (curState) {
            // prep for next round
            case NOT_READY_BREAK:
                this.setLevel(level + 1);
                final int curlevel = level;
                if (max_level > level) {
                    emptySet();
                    prepNextRound();

                    //sets call_back for round start & round end
                    if (breakUntil == 0) {
//                        setStateAndLoading(State.IN_ROUND, "Round #" + curlevel + " active. For: " + roundDuration, roundDuration);
                        setState(State.IN_ROUND, "Round " + curlevel);
                        break;
                    }

                    this.setStateAndLoading(State.READY_BREAK, "Break", breakUntil);
                    FrameTime.addCallBackAtDeltaFrames((long) breakUntil, new Runnable() {
                        @Override
                        public void run() {
                            setState(State.IN_ROUND, "Round " + curlevel);
                            loadingBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    loadingBar.hideBar();
                                }
                            });
                        }
                    });
                } else {
                    this.setState(State.GAME_OVER);
                }
                break;
            case READY_BREAK:
                // waits in this state until callback
                // in a round of the game
                break;
            case WAIT_FOR_CLEANUP:
                // waits in this state for some amount of time so that most of the comets will fly in and die
                break;
            case IN_ROUND:
                if (ammoLeft() != 0) {
                    // DOES THIS NEED TO BE CALLED EVERY TIME?
                    updateTarget();
                    firingRound(roundDuration);
                } else {
                    setState(State.WAIT_FOR_CLEANUP);
                    FrameTime.addCallBackAtDeltaFrames(this.cleanupTime, new Runnable() {
                        @Override
                        public void run() {
                            setState(State.NOT_READY_BREAK);
//                            setState(State.NOT_READY_BREAK, "IN ROUND AND OUT OF AMMO!!");
                        }
                    });

                    System.out.println("AMMO OVER. Clean up for for " + this.cleanupTime);
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


    /**
     * fill the ammo each level
     *
     * @return the total number of units
     */
    public int fillAmmo() {
        try {
            int total = 0;
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
                total += num;
                ammoSet.add(new Pair<>(unit_type, num));
            }
            return total;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;

    }

    //wrap_up
    public void emptySet() {
        while (ammoLeft() > 0) {
            throw new UnsupportedOperationException("SHOULD NOT HAVE ANY AMMO LEFT AT POINT");
//            fireRandom();
        }
    }

    //get break time
    private void setBreakTime() {
        breakUntil = System.currentTimeMillis() +
                1000 * MySettings.getConfigNum(this.team.toString(), new Query("break"));
    }

    private void prepNextRound() {
        double moneyForRound = MySettings.getConfigNum(this.team.toString(), new Query("money_awarded"));

        for (Player player : this.world.getPlayers()) {
            if (player.team == Team.DEFENCE)
                player.addMoney(moneyForRound);
        }

        double totalUnits = 1.0 * fillAmmo();
        this.breakUntil = 40 * MySettings.getConfigNum(this.team.toString(), new Query("break"));
        this.roundDuration = 40 * MySettings.getConfigNum(this.team.toString(), new Query("duration"));
        this.cleanupTime = 40 * (int) MySettings.getConfigNum(this.team.toString(), new Query("cleanupTime"));
        this.atOnce = (int) MySettings.getConfigNum(this.team.toString(), new Query("ammoFiredAtOnce"));
        this.atOnce_range = (int) MySettings.getConfigNum(this.team.toString(), new Query("ammoFiredAtOnceVariance"));

        this.rate = this.roundDuration * this.atOnce / (totalUnits);
        System.out.println(level + " ROUND PREP: " + totalUnits + " " + rate + " CLEANUP TIME - " + this.cleanupTime );
    }

    private void updateTarget() {
        this.target = targetEntity.shape.body.getWorldCenter();
    }

}
