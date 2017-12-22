package com.ne.revival_games.entity.WorldObjects;

import android.content.Context;
import android.media.MediaPlayer;

import com.ne.revival_games.entity.R;

import java.util.HashMap;

/**
 * Created by veganova on 12/21/17.
 */

public class Sounds {
    private static Sounds ourInstance;
    private Context context;
    private static HashMap<SOUND_TYPE, Integer> soundMap;

    public enum SOUND_TYPE {SHOOT, HIT, WIN, LOSE, MODE}

    public static Sounds getInstance() {
        if (ourInstance == null) {
            ourInstance = new Sounds();
        }
        return ourInstance;
    }
    
    // Make sure to set context before caling sounds 
    public void setContext(Context context) {
        this.context = context;
    }

    private Sounds() {
        System.out.println("NEW SOUND_TYPE CREATED!!");
        soundMap = new HashMap<>();
        soundMap.put(SOUND_TYPE.SHOOT, R.raw.shoot);
        soundMap.put(SOUND_TYPE.HIT, R.raw.hit);
        soundMap.put(SOUND_TYPE.WIN, R.raw.round_win);
        soundMap.put(SOUND_TYPE.LOSE, R.raw.round_lose);
        soundMap.put(SOUND_TYPE.MODE, R.raw.gamemode);
    }

    int max = 0;
    public void playSound(SOUND_TYPE s) {
        if (this.context == null) {
            throw new NullPointerException("The context is note set for the Sound class.");
        }

        if (max < 15) {
            final MediaPlayer mp = MediaPlayer.create(this.context, soundMap.get(s));

            mp.start();
            max++;
            System.out.println("num mediaplyers running - " + max);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    max--;
                    mp.release();
                }
            });
        }
    }
}
